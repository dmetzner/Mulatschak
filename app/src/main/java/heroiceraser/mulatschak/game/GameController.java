package heroiceraser.mulatschak.game;

import android.os.Handler;
import android.util.Log;

import com.google.android.gms.games.multiplayer.Participant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import heroiceraser.mulatschak.GameSettings.GameSettings;
import heroiceraser.mulatschak.game.DrawableObjects.MyPlayer;
import heroiceraser.mulatschak.game.GameModerator.GameModerator;
import heroiceraser.mulatschak.game.GamePlay.GamePlay;
import heroiceraser.mulatschak.game.NonGamePlayUI.NonGamePlayUIContainer;
import heroiceraser.mulatschak.game.DrawableObjects.Card;
import heroiceraser.mulatschak.game.DrawableObjects.CardStack;
import heroiceraser.mulatschak.game.DrawableObjects.DealerButton;
import heroiceraser.mulatschak.game.DrawableObjects.DiscardPile;
import heroiceraser.mulatschak.game.NonGamePlayUI.GameOver.GameOver;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.Animations.AnimateHands;
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
    public Participant host_;
    public String my_display_name_;
    public String my_participant_id_;

    private GameView view_;
    private GameLayout layout_;
    private TouchEvents touch_events_;
    private GameLogic logic_;
    private GameSettings settings_;

    private AnimateHands animateHands;

    private GameModerator gameModerator;
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
        host_ = null;
        my_display_name_ = null;
        my_participant_id_ = null;

        view_ = null;
        layout_ = null;
        touch_events_ = null;
        logic_ = null;
        settings_= null;

        animateHands = null;
        gameModerator = null;
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
        animateHands = new AnimateHands();
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
        gameModerator = new GameModerator();
    }

    //----------------------------------------------------------------------------------------------
    //  start
    //
    public void init(final int start_lives, final int enemies, final int difficulty, final boolean multiplayer,
                      final String myName, final String my_id, final ArrayList<Participant> participants) {

        multiplayer_ = multiplayer;

        my_display_name_ = myName;
        if (my_display_name_ == null) {
            my_display_name_ = "notSignedIn";
        }

        // if singlePlayer -> == null
        my_participant_id_ = my_id;
        participants_ = participants;
        if (participants != null && participants.size() > 0) {
            host_ = participants.get(0);
        }


        long start = System.currentTimeMillis();
        // essential inits
        logic_.init(start_lives, difficulty);
        layout_.init(view_);
        settings_.init(view_);
        initPlayers(my_id, enemies);
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
                animateHands.init(view_);
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
            chooseFirstDealerRandomly();
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
        dealer_button_.startMoveAnimation(this, logic_.getDealer());
        deck_.shuffleDeck();
        game_play_.getDealCards().dealCards(this);  // starts an dealing animation
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
            getAnimateHands().setMissATurnInfoVisible(true);
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
                // ToDo
                logic_.raiseMultiplier();
                setTurn(NOT_SET);
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
            getAnimateHands().setMissATurnInfoVisible(false);
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
            for (int i = 0; i < participants_.size(); i++) {
                MyPlayer new_My_player = new MyPlayer(view_);
                new_My_player.participant_ = participants_.get(i);
                int player_id = i;
                myPlayer_list_.add(player_id, new_My_player);
            }

        }

        // simple Singleplayer
        if (!multiplayer_) {
            int players = enemies + 1;
            for (int i = 0; i < players; i++) {
                MyPlayer new_My_player = new MyPlayer(view_);
                new_My_player.setId(i);
                myPlayer_list_.add(new_My_player);
            }
        }

        for (int i = 0; i < myPlayer_list_.size(); i++) {
            setDisplayName(getPlayerById(i));
        }

        switch (getAmountOfPlayers()) {
            case 1:
                getPlayerById(0).setPosition(0);
                break;
            case 2:
                getPlayerById(0).setPosition(0);
                getPlayerById(1).setPosition(2);
                break;
            case 3:
                getPlayerById(0).setPosition(0);
                getPlayerById(1).setPosition(1);
                getPlayerById(2).setPosition(2);
                break;
            case 4:
                getPlayerById(0).setPosition(0);
                getPlayerById(1).setPosition(1);
                getPlayerById(3).setPosition(3);
                getPlayerById(2).setPosition(2);
                break;
        }
    }

    public void setDisplayName(MyPlayer myPlayer) {

        String text;

        if (myPlayer.participant_ != null) {
            text = myPlayer.participant_.getDisplayName();
        }
        else if (myPlayer.getId() == 0) {
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


    //----------------------------------------------------------------------------------------------
    //  Getter
    //
    public GameView getView() { return view_; }

    public GameLayout getLayout() { return layout_; }

    public AnimateHands getAnimateHands() { return  animateHands; }

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

    public MyPlayer getPlayerByPosition(int pos) {
        for (int i = 0; i < getAmountOfPlayers(); i++) {
            if (pos == getPlayerById(i).getPosition()) {
                return getPlayerById(i);
            }
        }
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
