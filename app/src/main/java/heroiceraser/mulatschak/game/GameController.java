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

import heroiceraser.mulatschak.Message;
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
    private boolean gameStarted;

    // online sheesh
    public boolean multiplayer_;
    private ArrayList<Participant> participants_;
    private String my_display_name_;
    public String myPlayerId;
    private String hostOnlineId;
    public int waitForOnlineInteraction;
    private ArrayList<Integer> originalShuffledDeck;


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

    public MainActivity mainActivity;
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
        player_info_.clear();

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
        mainActivity = (MainActivity) view.getContext();
        view_ = view;
        enable_drawing_ = false;
        playerPresentation = false;
        gameStarted = false;
        waitForOnlineInteraction = Message.noMessage;

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
                      final String myName, final String my_id, final ArrayList<Participant> participants,
                     final String hostOnlineId, final ArrayList<String> sortedPlayerIds) {

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
        initPlayers(enemies, sortedPlayerIds);                                      // special multi
        resetPlayerLives();
        setPlayerPositions();
        player_info_.init(view_);
        non_game_play_ui_.init(view_);
        player_info_.startPresentation(this);

        long time = System.currentTimeMillis() - start;
        Log.d("gebrauchte start zeit: ", time + "");

        // init the rest while the players get presented
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

                // tell everyone my game is fully laoded
                getPlayerByPosition(0).setGameStarted(true);
                mainActivity.broadcastMessage(Message.gameReady, "");

                enable_drawing_ = true;
                long time = System.currentTimeMillis() - start;
                Log.d("gebrauchte zeit init2:", time + "");
            }
        };
        Thread t = new Thread(runnable);
        t.start();
    }

    public void startGame() {

        if (enable_drawing_) {
            Log.d(".......", "start game");
            playerPresentation = false;
            if (multiplayer_) {

                boolean mGameForAllStarted = true;
                for (MyPlayer p : getPlayerList()) {
                    if (p.getOnlineId().equals("")) {
                        continue;
                    }
                    if (!p.isGameStarted()) {
                        mGameForAllStarted = false;
                        mainActivity.requestMissedMessage(mainActivity.gameState, Message.requestGameReady, "");
                        break;
                    }
                }
                if (mGameForAllStarted) {
                    mainActivity.gameState++;
                    Log.d("----------", "host id " + Integer.toString(getPlayerByOnlineId(hostOnlineId).getId()));
                    chooseHostAsDealer(getPlayerByOnlineId(hostOnlineId).getId());
                    startRound();
                }
            }
            else {
                chooseFirstDealerRandomly();
                startRound();
            }
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
        Log.d("------------", "StartRound -----------------------------------------------------------");
        mainActivity.gameState = Message.gameStateWaitForShuffleDeck;
        for (MyPlayer player : getPlayerList()) {
            player.gameState = Message.gameStateWaitForShuffleDeck;
            player.exchanged_cards_.getCardStack().clear();
            player.played_cards_.getCardStack().clear();
        }
        waitForOnlineInteraction = Message.noMessage;
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
        Log.d("------------", "-----------------------------------------------------------");
        shuffleDeck();
    }


    private void continueAfterShuffledDeck() {
        Log.d("------------", "Shuffled -----------------------------------------------------------");
        for (int i = 0; i < 10; i++) {
            Log.d("Deck is: ", " " + deck_.getCardAt(i).getId());
        }
        game_play_.getDealCards().dealCards(this);  // starts an dealing animation
    }

    private void shuffleDeck() {
        Log.d("------------", "Shuffle -----------------------------------------------------------");
        if (multiplayer_) {
            if (myPlayerId.equals(hostOnlineId)) {
                deck_.shuffleDeck();
                originalShuffledDeck = new ArrayList<Integer>();
                for (int i = 0; i < deck_.getCardStack().size(); i++) {
                    originalShuffledDeck.add(deck_.getCardAt(i).getId());
                }
                for (int i = 0; i < 10; i++) {
                    Log.d("Deck: should be ", " " + deck_.getCardAt(i).getId());
                }
                sendShuffledDeck();
                continueAfterShuffledDeck();
            }
            else {
                waitForOnlineInteraction = Message.shuffledDeck;
                mainActivity.requestMissedMessage(mainActivity.gameState, Message.requestShuffledDeck, "");
            }
        }
        else {
            deck_.shuffleDeck();
            continueAfterShuffledDeck();
        }
    }

    private void sendShuffledDeck(String sendToId) {
        for (int i = 0; i < 10; i++) {
            Log.d("Deck: send", " " + originalShuffledDeck.get(i));
        }
        Gson gson = new Gson();
        mainActivity.sendUnReliable(sendToId, Message.shuffledDeck, gson.toJson(originalShuffledDeck));
    }

    private void sendShuffledDeck() {
        for (int i = 0; i < 10; i++) {
            Log.d("Deck: send to All", " " + originalShuffledDeck.get(i));
        }
        Gson gson = new Gson();
        mainActivity.broadcastMessage(Message.shuffledDeck, gson.toJson(originalShuffledDeck));
    }


    //----------------------------------------------------------------------------------------------
    //  continueAfterDealingAnimation
    //
    public void continueAfterDealingAnimation() {
        Log.d("------------", "decide Muli -----------------------------------------------------------");
        Log.d("------------", "Turn: " +getPlayerById(logic_.getTurn()).getDisplayName() + " -----------------------------------------------------------");
        mainActivity.gameState = Message.gameStateWaitForMulatschakDecision;
        for (MyPlayer player : getPlayerList()) {
            player.gameState = Message.gameStateWaitForMulatschakDecision;
        }
        game_play_.getDecideMulatschak().startMulatschakDecision(this);
    }


    //----------------------------------------------------------------------------------------------
    //  continueAfterDecideMulatschak
    //
    public void continueAfterDecideMulatschak() {
        Log.d("------------", "trick bids -----------------------------------------------------------");
        mainActivity.gameState = Message.gameStateWaitForTrickBids;
        for (MyPlayer player : getPlayerList()) {
            player.gameState = Message.gameStateWaitForTrickBids;
        }
        game_play_.getTrickBids().startTrickBids(this);
    }


    //----------------------------------------------------------------------------------------------
    //  continueAfterTrickBids
    //
    public void continueAfterTrickBids() {
        Log.d("------------", "choose trumps -----------------------------------------------------------");
        mainActivity.gameState = Message.gameStateWaitForChooseTrump;
        for (MyPlayer player : getPlayerList()) {
            player.gameState = Message.gameStateWaitForChooseTrump;
        }
        if (getPlayerByPosition(0).getMissATurn()) {
            getPlayerHandsView().setMissATurnInfoVisible(true);
        }

        final GameController controller = this;
        Handler mHandler = new Handler();
        Runnable newRoundRunnable = new Runnable() {
            @Override
            // Todo
            public void run() {
                non_game_play_ui_.getAllCardsPlayedView().startAnimation(controller);
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
        Log.d("------------", "exchange cards -----------------------------------------------------------");
        mainActivity.gameState = Message.gameStateWaitForCardExchange;
        for (MyPlayer player : getPlayerList()) {
            player.gameState = Message.gameStateWaitForCardExchange;
        }
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
        Log.d("------------", "play a card -----------------------------------------------------------");
        mainActivity.gameState = Message.gameStateWaitForPlayACard;
        for (MyPlayer player : getPlayerList()) {
            player.gameState = Message.gameStateWaitForPlayACard;
        }
        discardPile_.setVisible(true);
        player_info_.setShowPlayer0Turn(true);
        playACardCounter = 0;
        nextCardRound(true); // first call
    }



    //----------------------------------------------------------------------------------------------
    //  nextCardRound
    //                      -> playACard gets called for ever player once
    //                          then nextCard Round should get called again
    //

    private void nextCardRound(boolean x) {
        getGamePlay().getPlayACardRound().playACard(true, this);
    }

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
        logic_.setTurn(logic_.getDealer());
    }

    public void setTurn(int id) {
        player_info_.setActivePlayer(id);
        logic_.setTurn(id);
    }

    private void initPlayers(int enemies, ArrayList<String> sortedPlayerIds) {

        if (multiplayer_) {

            int mainPlayerId = 0;
            ArrayList<MyPlayer> tmpPlayerList = new ArrayList<>();

            for (int i = 0; i < participants_.size(); i++) {
                MyPlayer newPlayer = new MyPlayer();
                newPlayer.participant_ = participants_.get(i);
                newPlayer.initMultiplayer(); // in your own app you are always player 0
                tmpPlayerList.add(i, newPlayer);
            }

            for (int i = 0; i < enemies; i++) {
                MyPlayer newPlayer = new MyPlayer();
                newPlayer.participant_ = null;
                newPlayer.setEnemyLogic(true);
                newPlayer.setOnlineId("");
                newPlayer.setDisplayName("Muli Bot " + (i + 1));
                tmpPlayerList.add(i, newPlayer);
            }

            ArrayList<MyPlayer> tmpPlayerList2 = new ArrayList<>();
            for (int i = 0; i < sortedPlayerIds.size(); i++) {
                for (int j = 0; j < tmpPlayerList.size(); j++) {
                    if (sortedPlayerIds.get(i).equals(tmpPlayerList.get(j).getOnlineId())) {
                        tmpPlayerList2.add(tmpPlayerList.get(i));
                        break;
                    }
                }
            }

            tmpPlayerList.clear();

            // keep the correct order between players
            for (int i = 0; i < tmpPlayerList2.size(); i++) {
                if (tmpPlayerList2.get(i).getOnlineId().equals(myPlayerId)) {
                    mainPlayerId = i;
                }
            }

            for (int i = mainPlayerId; i < tmpPlayerList2.size(); i++) {
                myPlayer_list_.add(tmpPlayerList2.get(i));
            }

            for (int i = 0; i < mainPlayerId; i++) {
                myPlayer_list_.add(tmpPlayerList2.get(i));
            }

            tmpPlayerList2.clear();

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
    private void chooseHostAsDealer(int hostId) {
        logic_.setDealer(hostId);
        dealer_button_.startMoveAnimation(this, getPlayerById(hostId).getPosition());
        logic_.setStartingPlayer(logic_.getFirstBidder(getAmountOfPlayers()));
        setTurn(hostId);
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



    public void handleLeftPeer(final String leftPeerOnlineId) {

        MyPlayer leftPeerPlayer = getPlayerByOnlineId(leftPeerOnlineId);
        if (leftPeerPlayer == null) {
            return;
        }

        if (player_info_.arePopUpsBlocked()) {
            Handler h = new Handler();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    handleLeftPeer(leftPeerOnlineId);
                }
            };
            h.postDelayed(r, 100);
            return;
        }

        Log.d("--------------", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        int onlinePlayers = getAmountOfOnlinePlayer();
        Log.d("--------------", "op: " + onlinePlayers);

        Log.d("--_>", "" + Thread.currentThread().getId());

        if (onlinePlayers == 2) {
            player_info_.makeLastPlayerPopUp(leftPeerPlayer);
        }
        else if (onlinePlayers > 2) {
            player_info_.makeLeftPlayerPopUp(leftPeerPlayer);
        }
    }


    public void lastPlayerLeftSoLetMeWin() {
        for (int i = 0; i < getAmountOfPlayers(); i++) {
            getPlayerById(i).setLives(1);
            if (getPlayerById(i).getPosition() == 0) {
                getPlayerById(i).setLives(0);
            }
        }

        getNonGamePlayUIContainer().getStatistics().updatePlayerLives(this);
        getGameOver().setGameGameOver(this);
    }


    private int getAmountOfOnlinePlayer() {
        int onlinePlayers = 0;
        for (MyPlayer p : getPlayerList()) {
            if (!p.getOnlineId().equals("")) {
                onlinePlayers++;
            }
        }
        return onlinePlayers;
    }

    public void playerLeftContinueWithAI(MyPlayer leftPlayer) {
        leftPlayer.setEnemyLogic(true);

        if (leftPlayer.getOnlineId().equals(hostOnlineId)) {
            leftPlayer.setOnlineId("");
            for (Participant p : participants_) {
                if (getPlayerByOnlineId(p.getParticipantId()) != null &&
                        !getPlayerByOnlineId(p.getParticipantId()).equals("")) {
                    hostOnlineId = p.getParticipantId();
                    break;
                }
            }
        }
        else {
            leftPlayer.setOnlineId("");
        }

        // check active player
        int onlinePlayers = getAmountOfOnlinePlayer();
        if (onlinePlayers < 2) {
            multiplayer_ = false;
        }

        switch (waitForOnlineInteraction) {

            case Message.gameReady:
                if (!playerPresentation) {
                    startGame();
                }
                break;

            case Message.shuffledDeck:
                shuffleDeck();
                continueAfterShuffledDeck();
                break;

            case Message.mulatschakDecision:
                getGamePlay().getDecideMulatschak().handleEnemyAction(this);
                break;

            case Message.chooseTrump:
                getGamePlay().getChooseTrump().handleEnemyAction(this);
                break;

            case Message.cardExchange:
                getGamePlay().getCardExchange().handleEnemyAction(this);
                break;

            case Message.trickBids:
                getGamePlay().getTrickBids().handleEnemyAction(this);
                break;

            case Message.playACard:
                getGamePlay().getPlayACardRound().handleEnemyAction(this);
                break;
        }
    }

    public int playACardCounter = 0;

    public void handleReceivedMessage(final Message message) {
        if (!enable_drawing_) {
            return;
        }
        switch (message.type) {
            case Message.chatMessage:
                non_game_play_ui_.getChatView().addMessage(getPlayerByOnlineId(message.senderId), message.data, this);
                break;

            case Message.gameReady:
                if (mainActivity.gameState != Message.gameStateWaitForGameReady) {
                    return;
                }
                MyPlayer p = getPlayerByOnlineId(message.senderId);
                if (p != null) {
                    p.setGameStarted(true);
                }
                if (!playerPresentation) {
                    startGame();
                }
                break;
            case Message.requestGameReady:
                if (mainActivity.gameState <= Message.gameStateWaitForGameReady) {
                    return;
                }
                mainActivity.sendUnReliable(message.senderId, Message.gameReady, "");
                break;


            case Message.shuffledDeck:
                if (mainActivity.gameState != Message.gameStateWaitForShuffleDeck) {
                    return;
                }
                receiveShuffledDeck(message);
                break;
            case Message.requestShuffledDeck:
                if (mainActivity.gameState <= Message.gameStateWaitForShuffleDeck) {
                    return;
                }
                sendShuffledDeck(message.senderId);
                break;


            case Message.mulatschakDecision:
                if (mainActivity.gameState < Message.gameStateWaitForMulatschakDecision) {
                    return;
                }
                receiveMulatschakDecision(message);
                break;
            case Message.requestMulatschakDecision:
                if (mainActivity.gameState < Message.gameStateWaitForMulatschakDecision ||
                        getPlayerByOnlineId(message.data).gameState <= Message.gameStateWaitForMulatschakDecision) {
                    return;
                }
                boolean muli = false;
                if (logic_.isMulatschakRound() &&
                        message.data.equals(getPlayerById(logic_.getTrumpPlayerId()).getOnlineId())) {
                    muli = true;
                }
                sendMulatschakDecision(message.senderId, muli);
                break;

            case Message.trickBids:
                if (mainActivity.gameState != Message.gameStateWaitForTrickBids) {
                    return;
                }
                receiveTrickBids(message);
                break;
            case Message.requestTrickBids:
                if (mainActivity.gameState < Message.gameStateWaitForTrickBids ||
                        getPlayerByOnlineId(message.data).gameState <= Message.gameStateWaitForTrickBids) {
                    return;
                }
                if (getPlayerByOnlineId(message.data) != null) {
                    int tricksButton = getPlayerByOnlineId(message.data).getTricksToMake() + 1;
                    sendTrickBids(message.senderId, tricksButton);
                }
                break;



            case Message.chooseTrump:
                if (mainActivity.gameState != Message.gameStateWaitForChooseTrump) {
                    return;
                }
                receiveChooseTrump(message);
                break;
            case Message.requestChooseTrump:
                if (mainActivity.gameState <= Message.gameStateWaitForChooseTrump) {
                    return;
                }
                int trump = logic_.getTrump() - 1;
                sendChooseTrump(message.senderId, trump);
                break;



            case Message.cardExchange:
                if (mainActivity.gameState != Message.gameStateWaitForCardExchange) {
                    return;
                }
                receiveCardExchange(message);
                break;

            case Message.requestCardExchange:
                if (mainActivity.gameState < Message.gameStateWaitForCardExchange ||
                        getPlayerByOnlineId(message.data).gameState <= Message.gameStateWaitForCardExchange) {
                    return;
                }
                sendCardExchange(message.data, message.senderId);
                break;


            case Message.playACard:
                if (mainActivity.gameState != Message.gameStateWaitForPlayACard) {
                    return;
                }
                receivePlayACard(message);
                break;

            case Message.requestPlayACard:
                if (mainActivity.gameState < Message.gameStateWaitForPlayACard) {
                    return;
                }
                Type listType = new TypeToken<ArrayList<String>>() {}.getType();
                Gson gson = new Gson();
                ArrayList<String> data = gson.fromJson(message.data, listType);
                if (getPlayerByOnlineId(data.get(0)).gameState <= Message.gameStateWaitForPlayACard) {
                    return;
                }
                sendPlayACard(data.get(0), Integer.parseInt(data.get(1)), message.senderId);
                break;


            case Message.waitForNewRound:
                if (mainActivity.gameState != Message.gameStateWaitForNewRound) {
                    return;
                }
                receiveNextRound(message);
                break;

            case Message.requestWaitForNewRound:
                if (mainActivity.gameState < Message.gameStateWaitForNewRound &&
                        mainActivity.gameState > Message.gameStateWaitForMulatschakDecision) {
                    return;
                }
                mainActivity.sendUnReliable(message.senderId, Message.waitForNewRound, "");
                break;

            }
    }



    private void receiveShuffledDeck(final Message message) {
        if (waitForOnlineInteraction == Message.shuffledDeck) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Integer>>() {}.getType();
            ArrayList<Integer> cardIds = gson.fromJson(message.data, listType);
            waitForOnlineInteraction = Message.noMessage;
            for (int i = 0; i < 10; i++) {
                Log.d("Deck: what we got ", " " + cardIds.get(i));
            }
            deck_.sortByIdList(cardIds);
            continueAfterShuffledDeck();
        }
    }

    public void sendMulatschakDecision(String sendTo, boolean muli) {
        Gson gson = new Gson();
        mainActivity.sendUnReliable(sendTo, Message.mulatschakDecision, gson.toJson(muli));
    }

    private void receiveMulatschakDecision(final Message message) {
        if (waitForOnlineInteraction == Message.mulatschakDecision) {
            Gson gson = new Gson();
            boolean muli = gson.fromJson(message.data, boolean.class);
            game_play_.getDecideMulatschak().handleOnlineInteraction(muli, this);
        }
    }

    public void sendTrickBids(String sendTo, int tricks) {
        Gson gson = new Gson();
        mainActivity.sendUnReliable(sendTo, Message.trickBids, gson.toJson(tricks));
    }

    private void receiveTrickBids(final Message message) {
        if (waitForOnlineInteraction == Message.trickBids) {
            Gson gson = new Gson();
            int tricks = gson.fromJson(message.data, int.class);
            game_play_.getTrickBids().handleOnlineInteraction(tricks, this);
        }
    }


    public void sendChooseTrump(String sendTo, int trump) {
        Gson gson = new Gson();
        mainActivity.sendUnReliable(sendTo, Message.chooseTrump, gson.toJson(trump));
    }

    private void receiveChooseTrump(final Message message) {
        if (waitForOnlineInteraction == Message.chooseTrump) {
            Gson gson = new Gson();
            int trump = gson.fromJson(message.data, int.class);
            game_play_.getChooseTrump().handleOnlineInteraction(trump, this);
        }
    }

    public void sendCardExchange(String oId, String sendTo) {
        CardStack exchanged_cards_ = getPlayerByOnlineId(oId).exchanged_cards_;
        ArrayList<Integer> exchangedId = new ArrayList<>();
        for (Card c : exchanged_cards_.getCardStack()) {
            exchangedId.add(c.getId());
        }
        Gson gson = new Gson();
        mainActivity.sendUnReliable(sendTo, Message.cardExchange, gson.toJson(exchangedId));
    }

    private void receiveCardExchange(final Message message) {
        if (waitForOnlineInteraction == Message.cardExchange) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Integer>>() {}.getType();
            ArrayList<Integer> handCardsToRemoveIds = gson.fromJson(message.data, listType);
            game_play_.getCardExchange().handleOnlineInteraction(handCardsToRemoveIds, this);
        }
    }

    public void sendPlayACard(String oId, int counter, String sendTo) {
        CardStack played_cards_ = getPlayerByOnlineId(oId).played_cards_;
        int id = played_cards_.getCardAt(counter).getId();
        Gson gson = new Gson();
        mainActivity.sendUnReliable(sendTo, Message.playACard, gson.toJson(id));
    }
    private void receivePlayACard(final Message message) {
        if (waitForOnlineInteraction == Message.playACard) {
            Gson gson = new Gson();
            int cardId = gson.fromJson(message.data, int.class);
            game_play_.getPlayACardRound().handleOnlineInteraction(cardId, this);
        }
    }

    private void receiveNextRound(final Message message) {
        if (waitForOnlineInteraction == Message.waitForNewRound) {
            getPlayerByOnlineId(message.senderId).gameState = Message.gameStateWaitForNewRound;
        }
    }


    //----------------------------------------------------------------------------------------------
    //  Getter
    //
    public GameView getView() { return view_; }

    public GameLayout getLayout() { return layout_; }

    public PlayerHandsView getPlayerHandsView() { return playerHandsView; }

    public GameLogic getLogic() { return logic_; }

    TouchEvents getTouchEvents() { return touch_events_; }

    public MulatschakDeck getDeck() {
        return deck_;
    }

    public DiscardPile getDiscardPile() {
        return discardPile_;
    }

    public List<MyPlayer> getPlayerList() { return myPlayer_list_; }

    public MyPlayer getPlayerById(int id) {
        try {
            return myPlayer_list_.get(id);
        } catch (Exception e) {
            Log.e("con", "player not found by online id");
            return null;
        }
    }

    public MyPlayer getPlayerByOnlineId(String playerId) {

        try {
            for (MyPlayer p : getPlayerList()) {
                if (playerId.equals(p.getOnlineId())) {
                    return p;
                }
            }
        } catch (Exception e) {
            Log.e("con", "player not found by online id");
            return null;
        }

        Log.w("con", "player not found by online id");
        return null;
    }

    public MyPlayer getPlayerByPosition(int pos) {

        try {
            for (MyPlayer p : getPlayerList()) {
                if (pos == p.getPosition()) {
                    return p;
                }
            }
        } catch (Exception e) {
            Log.e("con", "player not found by online id");
            return null;
        }
        Log.w("con", "player not found by pos");
        return null;
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

    boolean isPlayerPresentationRunning() {
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

    public boolean isGameStarted() {
        return gameStarted;
    }
}
