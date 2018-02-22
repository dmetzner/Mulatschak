package heroiceraser.mulatschak.game;

import android.os.Handler;
import android.util.Log;

import com.google.android.gms.games.multiplayer.Participant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import heroiceraser.Message;
import heroiceraser.mulatschak.GameSettings.GameSettings;
import heroiceraser.mulatschak.MainActivity;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;
import heroiceraser.mulatschak.game.GamePlay.GamePlay;
import heroiceraser.mulatschak.game.NonGamePlayUI.NonGamePlayUIContainer;
import heroiceraser.mulatschak.game.BaseObjects.Card;
import heroiceraser.mulatschak.game.BaseObjects.CardStack;
import heroiceraser.mulatschak.game.BaseObjects.DealerButton;
import heroiceraser.mulatschak.game.BaseObjects.DiscardPile;
import heroiceraser.mulatschak.game.NonGamePlayUI.GameOver.GameOver;
import heroiceraser.mulatschak.game.BaseObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.BaseObjects.PlayerHandsView;
import heroiceraser.mulatschak.game.NonGamePlayUI.PlayerInfo.PlayerInfo;


public class GameController{

    //----------------------------------------------------------------------------------------------
    //  Constants
    //
    public static int NOT_SET = -9999;

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private boolean enable_drawing_;
    private boolean playerPresentation;

    public boolean multiplayer_;
    public ArrayList<Participant> participants_;

    public String my_display_name_;
    public String myPlayerId;
    public String hostOnlineId;

    private GameView view_;
    private GameLayout layout_;
    private TouchEvents touch_events_;
    private GameLogic logic_;
    private GameSettings settings_;

    private PlayerHandsView playerHandsView;

    private NonGamePlayUIContainer non_game_play_ui_;
    private GamePlay game_play_;

    private List<MyPlayer> myPlayer_list_;
    private PlayerInfo player_info_;

    private MulatschakDeck deck_;
    private DiscardPile discardPile_;
    private CardStack trash_;
    private DealerButton dealer_button_;


    //----------------------------------------------------------------------------------------------
    //  clear
    //
    public void clear() {

        if (participants_ != null) {
            participants_.clear();
            participants_ = null;
        }
        my_display_name_ = null;
        myPlayerId = null;


        view_ = null;
        layout_ = null;
        touch_events_ = null;
        logic_ = null;
        settings_= null;

        playerHandsView = null;
        non_game_play_ui_ = null;
        game_play_ = null;

        if (myPlayer_list_ != null) {
            myPlayer_list_.clear();
            myPlayer_list_ = null;
        }

        player_info_ = null;

        deck_ = null;
        discardPile_ = null;
        trash_ = null;
        dealer_button_ = null;
    }


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameController(GameView view) {
        view_ = view;
        enable_drawing_ = false;
        playerPresentation = false;

        logic_ = new GameLogic();
        layout_ = new GameLayout();
        playerHandsView = new PlayerHandsView();
        touch_events_ = new TouchEvents();
        settings_ = new GameSettings();

        non_game_play_ui_ = new NonGamePlayUIContainer();
        game_play_ = new GamePlay();

        myPlayer_list_ = new ArrayList<>();
        player_info_ = new PlayerInfo();

        deck_ = new MulatschakDeck();
        trash_ = new CardStack();
        discardPile_ = new DiscardPile();

        dealer_button_ = new DealerButton();
    }

    //----------------------------------------------------------------------------------------------
    //  start
    //
    public void init(final int start_lives, final int enemies, final int difficulty, final boolean multiplayer,
                      final String myName, final String my_id, final ArrayList<Participant> participants, final String hostOnlineId) {

        multiplayer_ = multiplayer;
        my_display_name_ = myName;
        if (my_display_name_ == null) {
            my_display_name_ = "notSignedIn";
        }
        this.hostOnlineId = hostOnlineId;
        myPlayerId = my_id;
        participants_ = participants;

        long start = System.currentTimeMillis();
        // essential inits
        logic_.init(start_lives, difficulty);
        layout_.init(view_);
        settings_.init(view_);
        initPlayers(my_id, enemies);                                                    // special multi
        resetPlayerLives();
        setPlayerPositions();
        player_info_.init(view_);
        non_game_play_ui_.init(view_);
        player_info_.startPresentation(this);

        long time = System.currentTimeMillis() - start;
        Log.d("gebrauchte start zeit: ", time + "");

        // init the rest while the players get presented
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                Log.d("--", 6 + " - " + System.currentTimeMillis());
                discardPile_.init(view_);
                Log.d("--", 7 + " - " + System.currentTimeMillis());
                game_play_.init(view_);
                Log.d("--", 8 + " - " + System.currentTimeMillis());
                deck_.initDeck(view_);
                Log.d("--", 9 + " - " + System.currentTimeMillis());
                dealer_button_.init(view_);
                Log.d("--", 10 + " - " + System.currentTimeMillis());
                playerHandsView.init(view_);
                Log.d("--", 11 + " - " + System.currentTimeMillis());
                enable_drawing_ = true;
                long time = System.currentTimeMillis() - start;
                Log.d("gebrauchte zeit init2:", time + "");
            }
        };
        handler.postDelayed(runnable, 100);
    }

    public void startGame() {
        if (enable_drawing_) {
            playerPresentation = false;
            if (multiplayer_) {
                Log.d("----------", "host id " + Integer.toString(getPlayerByOnlineId(hostOnlineId).getId()));
                chooseHostAsDealer(getPlayerByOnlineId(hostOnlineId).getId());
            }
            else {
                chooseFirstDealerRandomly();
            }
            startRound();
        }
        else {
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    startGame();
                }
            };
            handler.postDelayed(runnable, 100);
        }
    }


    // ---------------------------------------------------------------------------------------------
    // ----------------------  Game Flow


    // ---------------------------------------------------------------------------------------------
    //  startRound
    //
    public void startRound() {
        waitForOnlineInteraction = false;
        player_info_.setVisible(true);

        // clean up last round
        non_game_play_ui_.getTricks().clear();
        allCardsBackToTheDeck();
        logic_.setMulatschakRound(false);
        resetTricksToMake();
        // getGamePlay().getPlayACardRound().setCardMovable(false);
        player_info_.setActivePlayer(NOT_SET);
        player_info_.setShowPlayer0Turn(false);
        discardPile_.setVisible(false);
        discardPile_.setOverlaysVisible(false);
        game_play_.startRound(this);
        for (MyPlayer player : getPlayerList()) {
            player.setMissATurn(false);
        }

        // start round -> move dealer button, and deal the cards
        view_.enableUpdateCanvasThread();
        //gameModerator.startRoundMessage(non_game_play_ui_.getChatView(),
        //        getPlayerById(logic_.getDealer()).getDisplayName());
        dealer_button_.startMoveAnimation(this, getPlayerById(logic_.getDealer()).getPosition());

        if (multiplayer_) {
            if (myPlayerId.equals(hostOnlineId)) {
                deck_.shuffleDeck();
                // send to all participants
                ArrayList<Integer> cardIds = new ArrayList<>();
                for (int i = 0; i < deck_.getCardStack().size(); i++) {
                    cardIds.add(deck_.getCardAt(i).getId());
                }
                MainActivity mainActivity = (MainActivity) view_.getContext();
                Gson gson = new Gson();
                mainActivity.broadcastMessage(Message.shuffledDeck, gson.toJson(cardIds));
                game_play_.getDealCards().dealCards(this);  // starts an dealing animation
            }
            else {
                // show shuffle animation
                // wait for shuffled deck
                waitForOnlineInteraction = true;
            }
        }
        else {
            deck_.shuffleDeck();
            game_play_.getDealCards().dealCards(this);  // starts an dealing animation
        }
    }


    //----------------------------------------------------------------------------------------------
    //  continueAfterDealingAnimation
    //
    public void continueAfterDealingAnimation() {
        game_play_.getDecideMulatschak().startMulatschakDecision(this);
    }


    //----------------------------------------------------------------------------------------------
    //  continueAfterDecideMulatschak
    //
    public void continueAfterDecideMulatschak() {
        game_play_.getTrickBids().startTrickBids(this);
    }


    //----------------------------------------------------------------------------------------------
    //  continueAfterTrickBids
    //
    public void continueAfterTrickBids() {
        if (getPlayerById(0).getMissATurn()) {
            getPlayerHandsView().setMissATurnInfoVisible(true);
        }

        final GameController controller = this;
        Handler mHandler = new Handler();
        Runnable newRoundRunnable = new Runnable() {
            @Override
            // Todo
            public void run() {
                startRound();
            }
        };

        // checkButton the highest bid
        switch (game_play_.getTrickBids().getHighestBid(controller)) {
            case 0:  // start a new round if every player said 0 tricks
                // ToDo animation
                logic_.raiseMultiplier();
                setTurn(logic_.getDealer());
                mHandler.postDelayed(newRoundRunnable, 3000);
                break;

            case 1: // heart round -> no trumps to choose
                logic_.setTrump(MulatschakDeck.HEART);
                setTurn(logic_.getTrumpPlayerId());
                game_play_.getChooseTrump().getTrumpView()
                        .startAnimation(MulatschakDeck.HEART, logic_.getTrumpPlayerId(), controller);
                break;

            default:
                setTurn(logic_.getTrumpPlayerId());
                game_play_.getChooseTrump().letHighestBidderChooseTrump(this);
                break;
        }
    }


    //----------------------------------------------------------------------------------------------
    //  continueAfterTrumpWasChosen
    //
    public void continueAfterTrumpWasChosen() {
        game_play_.getTrickBids().getBidsView().setVisible(true);
        setTurn(logic_.getTrumpPlayerId());
        logic_.setStartingPlayer(logic_.getTrumpPlayerId());
        if (logic_.isMulatschakRound()) {
            continueAfterCardExchange();
        }
        else {
            game_play_.getCardExchange().makeCardExchange(true, this); // first player to bid is the player next to the dealer
        }
    }


    //----------------------------------------------------------------------------------------------
    //  continueAfterCardExchange
    //
    public void continueAfterCardExchange() {
        discardPile_.setVisible(true);
        player_info_.setShowPlayer0Turn(true);
        nextCardRound(); // first call
    }

    //----------------------------------------------------------------------------------------------
    //  nextCardRound
    //                      -> playACard gets called for ever player once
    //                          then nextCard Round should get called again
    //
    public void nextCardRound() {

        // if all card got played -> this Round is over
        // update player lives, game over? or new round?
        // starts the animations if needed
        if (game_play_.getAllCardsPlayed().areAllCardsPlayed(this)) {
            getPlayerHandsView().setMissATurnInfoVisible(false);
            game_play_.getAllCardsPlayed().allCardsArePlayedLogic(this);
            return;
        }

        // next round
        getGamePlay().getPlayACardRound().playACard(true, this);
    }

    //----------------------- Game Flow



    public void prepareNewRound() {
        logic_.moveDealer(getAmountOfPlayers());
        resetTurn();
    }

    public void setTurn(int id) {
        player_info_.setActivePlayer(id);
        logic_.setTurn(id);
    }

    private void initPlayers(String my_id, int enemies) {

        if (multiplayer_) {

            int mainPlayerId = 0;
            ArrayList<MyPlayer> tmpPlayerList = new ArrayList<>();

            for (int i = 0; i < participants_.size(); i++) {
                MyPlayer newPlayer = new MyPlayer();
                newPlayer.participant_ = participants_.get(i);
                newPlayer.initMultiplayer(); // in your own app you are always player 0
                tmpPlayerList.add(i, newPlayer);
                if (participants_.get(i).getPlayer().getPlayerId().equals(myPlayerId)) {
                    mainPlayerId = i;
                }
            }

            // keep the correct order between players
            for (int i = mainPlayerId; i < participants_.size(); i++) {
                myPlayer_list_.add(tmpPlayerList.get(i));
            }

            for (int i = 0; i < mainPlayerId; i++) {
                myPlayer_list_.add(tmpPlayerList.get(i));
            }

            // check debug
            for (int i = 0; i < myPlayer_list_.size(); i++) {
                Log.d("P", "" + myPlayer_list_.get(i).getDisplayName() + " -- " + myPlayer_list_.get(i).getOnlineId());
                getPlayerById(i).setId(i);
            }
        }

        // simple Singleplayer
        if (!multiplayer_) {
            int players = enemies + 1;
            for (int i = 0; i < players; i++) {
                MyPlayer new_My_player = new MyPlayer();
                new_My_player.setId(i);
                myPlayer_list_.add(new_My_player);
            }
            for (int i = 0; i < myPlayer_list_.size(); i++) {
                setDisplayName(getPlayerById(i));
                getPlayerById(i).setId(i);
                if (i == 0) {
                    getPlayerById(i).setEnemyLogic(false);
                }
                else {
                    getPlayerById(i).setEnemyLogic(true);
                }
            }
        }
    }

    private void setDisplayName(MyPlayer myPlayer) {

        String text;

        if (myPlayer.getId() == 0) {
            text = view_.getController().my_display_name_;
        }
        else {
            text = "Muli Bot " + myPlayer.getId();
        }
        if (text.length() > 15) {
            text = text.substring(0, 15);
        }

        myPlayer.setDisplayName(text);
    }


    //----------------------------------------------------------------------------------------------
    //  setPlayer Lives
    //
    private void resetPlayerLives() {
        for (int i = 0; i < getAmountOfPlayers(); i++) {
            getPlayerById(i).setLives(logic_.getStartLives()); ////////////////////////////////////////////////////////////////////////7
            getPlayerById(i).setTricksToMake(NOT_SET);
        }
    }

    //----------------------------------------------------------------------------------------------
    //  setPlayer Position
    //
    private void setPlayerPositions() {
        switch (getAmountOfPlayers()) {
            case 1:
                getPlayerById(0).setPosition(GameLayout.POSITION_BOTTOM);
                break;
            case 2:
                getPlayerById(0).setPosition(GameLayout.POSITION_BOTTOM);
                getPlayerById(1).setPosition(GameLayout.POSITION_TOP);
                break;
            case 3:
                getPlayerById(0).setPosition(GameLayout.POSITION_BOTTOM);
                getPlayerById(1).setPosition(GameLayout.POSITION_LEFT);
                getPlayerById(2).setPosition(GameLayout.POSITION_TOP);
                break;
            case 4:
                getPlayerById(0).setPosition(GameLayout.POSITION_BOTTOM);
                getPlayerById(1).setPosition(GameLayout.POSITION_LEFT);
                getPlayerById(2).setPosition(GameLayout.POSITION_TOP);
                getPlayerById(3).setPosition(GameLayout.POSITION_RIGHT);
                break;
        }
    }

    //----------------------------------------------------------------------------------------------
    //
    //
    private void chooseFirstDealerRandomly() {
        Random random_generator = new Random();
        int dealer_id = random_generator.nextInt(getAmountOfPlayers());
        logic_.setDealer(dealer_id);
        dealer_button_.startMoveAnimation(this, getPlayerById(dealer_id).getPosition());
        logic_.setStartingPlayer(logic_.getFirstBidder(getAmountOfPlayers()));
        setTurn(dealer_id);
    }

    //----------------------------------------------------------------------------------------------
    //
    //
    private void chooseHostAsDealer(int hostOnlineId) {
        logic_.setDealer(hostOnlineId);
        dealer_button_.startMoveAnimation(this, getPlayerById(hostOnlineId).getPosition());
        logic_.setStartingPlayer(logic_.getFirstBidder(getAmountOfPlayers()));
        setTurn(hostOnlineId);
    }


    //----------------------------------------------------------------------------------------------
    //  cada
    //
    private void resetTurn() {
        setTurn(logic_.getDealer());
    }

    //----------------------------------------------------------------------------------------------
    //  cada
    //
    private void resetTricksToMake() {
        for (int i = 0; i < getAmountOfPlayers(); i++) {
            getPlayerById(i).setTricksToMake(NOT_SET);
        }
        logic_.setTricksToMake(NOT_SET);
        logic_.setTrump(NOT_SET);
    }

    //----------------------------------------------------------------------------------------------
    //  turn to next player
    //                          -> passes turn to the next player
    //                          -> updates the active player for the player info
    //
    public void turnToNextPlayer(boolean skipMissATurns) {
        logic_.turnToNextPlayer(getAmountOfPlayers());
        if (skipMissATurns) {
            while (getPlayerById(logic_.getTurn()).getMissATurn()) {
                logic_.turnToNextPlayer(getAmountOfPlayers());
            }
        }
        player_info_.setActivePlayer(logic_.getTurn());
    }


    public void allCardsBackToTheDeck() {

        for (int player_id = 0; player_id < getAmountOfPlayers(); player_id++) {

            // all tricks to the deck
            CardStack tricks = getPlayerById(player_id).getTricks();
            for (int i = 0; i < tricks.getCardStack().size(); i++) {
                tricks.getCardAt(i).setPosition(layout_.getDeckPosition());
                tricks.getCardAt(i).setFixedPosition(layout_.getDeckPosition());
                deck_.addCard(tricks.getCardAt(i));
                tricks.getCardStack().remove(i);
                i--;
            }

            // all hand cards to the deck
            CardStack hand = getPlayerById(player_id).getHand();
            for (int i = 0; i < hand.getCardStack().size(); i++) {
                hand.getCardAt(i).setPosition(layout_.getDeckPosition());
                hand.getCardAt(i).setFixedPosition(layout_.getDeckPosition());
                deck_.addCard(hand.getCardAt(i));
                hand.getCardStack().remove(i);
                i--;
            }
        }

        // trash from card exchange to the deck
        for (int i = 0; i < trash_.getCardStack().size(); i++) {
            trash_.getCardAt(i).setPosition(layout_.getDeckPosition());
            trash_.getCardAt(i).setFixedPosition(layout_.getDeckPosition());
            deck_.addCard(trash_.getCardAt(i));
            trash_.getCardStack().remove(i);
            i--;
        }

        // reset Deck Positions
        for (int i = 0; i < deck_.getCardStack().size(); i++) {
            deck_.getCardAt(i).setPosition(layout_.getDeckPosition());
            deck_.getCardAt(i).setFixedPosition(getLayout().getDeckPosition());
            deck_.getCardAt(i).setVisible(false);
        }

        // something went wrong, generate a new deck -> better than a crash
        if (deck_.getCardStack().size() != MulatschakDeck.CARDS_PER_DECK) {
            deck_ = new MulatschakDeck();
            deck_.initDeck(view_);
        }

    }

    public void moveCardToTrash(Card card) {
        trash_.addCard(card);
    }


    public void handleReceivedMessage(final Message message) {
        if (enable_drawing_) {
            switch (message.type) {
                case Message.chatMessage:
                    non_game_play_ui_.getChatView().addMessage(getPlayerByOnlineId(message.senderId), message.data, this);
                    break;
                case Message.shuffledDeck:
                    receiveShuffledDeck(message);
                    break;
                case Message.mulatschakDecision:
                    receiveMulatschakDecision(message);
                    break;
                case Message.trickBids:
                    receiveTrickBids(message);
                    break;
                case Message.chooseTrump:
                    receiveChooseTrump(message);
                    break;
                case Message.cardExchange:
                    receiveCardExchange(message);
                    break;
                case Message.playACard:
                    receivePlayACard(message);
                    break;


            }

        }
        else {
            Handler h = new Handler();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    handleReceivedMessage(message);
                }
            };
            h.postDelayed(r, 200);
        }

    }

    public boolean waitForOnlineInteraction;

    private void receiveShuffledDeck(final Message message) {
        if (waitForOnlineInteraction) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Integer>>() {}.getType();
            ArrayList<Integer> cardIds = gson.fromJson(message.data, listType);
            waitForOnlineInteraction = false;
            deck_.sortByIdList(cardIds);
            getGamePlay().getDealCards().dealCards(this);
        }
        // wait & try again
        else  {
            final GameController gc = this;
            Handler h = new Handler();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    receiveShuffledDeck(message);
                }
            };
            h.postDelayed(r, 200);
        }
    }

    private void receiveMulatschakDecision(final Message message) {
        if (waitForOnlineInteraction) {
            Gson gson = new Gson();
            boolean muli = gson.fromJson(message.data, boolean.class);
            game_play_.getDecideMulatschak().handleOnlineInteraction(muli, this);
        }
        // wait & try again
        else {
            Handler h = new Handler();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    receiveMulatschakDecision(message);
                }
            };
            h.postDelayed(r, 200);
        }
    }
    private void receiveTrickBids(final Message message) {
        if (waitForOnlineInteraction) {
            Gson gson = new Gson();
            int tricks = gson.fromJson(message.data, int.class);
            game_play_.getTrickBids().handleOnlineInteraction(tricks, this);
        }
        // wait & try again
        else {
            Handler h = new Handler();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    receiveTrickBids(message);
                }
            };
            h.postDelayed(r, 200);
        }
    }

    private void receiveChooseTrump(final Message message) {
        if (waitForOnlineInteraction) {
            Gson gson = new Gson();
            int trump = gson.fromJson(message.data, int.class);
            game_play_.getChooseTrump().handleOnlineInteraction(trump, this);
        }
        // wait & try again
        else {
            Handler h = new Handler();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    receiveChooseTrump(message);
                }
            };
            h.postDelayed(r, 200);
        }
    }

    private void receiveCardExchange(final Message message) {
        if (waitForOnlineInteraction) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Integer>>() {}.getType();
            ArrayList<Integer> handCardsToRemoveIds = gson.fromJson(message.data, listType);
            game_play_.getCardExchange().handleOnlineInteraction(handCardsToRemoveIds, this);
        }
        // wait & try again
        else {
            Handler h = new Handler();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    receiveCardExchange(message);
                }
            };
            h.postDelayed(r, 200);
        }
    }

    private void receivePlayACard(final Message message) {
        if (waitForOnlineInteraction) {
            Gson gson = new Gson();
            int cardId = gson.fromJson(message.data, int.class);
            game_play_.getPlayACardRound().handleOnlineInteraction(cardId, this);
        }
        // wait & try again
        else {
            Handler h = new Handler();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    receivePlayACard(message);
                }
            };
            h.postDelayed(r, 200);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  Getter
    //
    public GameView getView() { return view_; }

    public GameLayout getLayout() { return layout_; }

    public PlayerHandsView getPlayerHandsView() { return playerHandsView; }

    public GameLogic getLogic() { return logic_; }

    public TouchEvents getTouchEvents() { return touch_events_; }

    public MulatschakDeck getDeck() {
        return deck_;
    }

    public DiscardPile getDiscardPile() {
        return discardPile_;
    }

    public List<MyPlayer> getPlayerList() { return myPlayer_list_; }

    public MyPlayer getPlayerById(int id) { return myPlayer_list_.get(id); }

    public MyPlayer getPlayerByOnlineId(String playerId) {
        for (int i = 0; i < getAmountOfPlayers(); i++) {
            if (playerId.equals(getPlayerById(i).getOnlineId())) {
                return getPlayerById(i);
            }
        }
        Log.w("con", "player not found by online id");
        return getPlayerByPosition(0);
    }

    public MyPlayer getPlayerByPosition(int pos) {
        for (int i = 0; i < getAmountOfPlayers(); i++) {
            if (pos == getPlayerById(i).getPosition()) {
                return getPlayerById(i);
            }
        }
        Log.w("con", "player not found by pos");
        return getPlayerByPosition(0);
    }

    public int getAmountOfPlayers() { return myPlayer_list_.size(); }

    public NonGamePlayUIContainer getNonGamePlayUIContainer() {
        return non_game_play_ui_;
    }

    public DealerButton getDealerButton() {
        return dealer_button_;
    }

    public CardStack getTrash() {
        return trash_;
    }

    public GameOver getGameOver() {
        return non_game_play_ui_.getGameOver();
    }

    public PlayerInfo getPlayerInfo() { return player_info_; }

    public boolean isDrawingEnabled() {
        return enable_drawing_;
    }

    public boolean isPlayerPresentationRunning() {
        return playerPresentation;
    }

    public void setPlayerPresentation(boolean playerPresentation) {
        this.playerPresentation = playerPresentation;
    }

    public GamePlay getGamePlay() {
        return game_play_;
    }

    public GameSettings getSettings() {
        return settings_;
    }
}
