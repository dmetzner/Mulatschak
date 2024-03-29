package heroiceraser.mulatschak.game;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import heroiceraser.mulatschak.MainActivity;
import heroiceraser.mulatschak.game.BaseObjects.Card;
import heroiceraser.mulatschak.game.BaseObjects.CardStack;
import heroiceraser.mulatschak.game.BaseObjects.DealerButton;
import heroiceraser.mulatschak.game.BaseObjects.DiscardPile;
import heroiceraser.mulatschak.game.BaseObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;
import heroiceraser.mulatschak.game.BaseObjects.PlayerHandsView;
import heroiceraser.mulatschak.game.GamePlay.GamePlay;
import heroiceraser.mulatschak.game.NonGamePlayUI.GameOver.GameOver;
import heroiceraser.mulatschak.game.NonGamePlayUI.NonGamePlayUIContainer;
import heroiceraser.mulatschak.game.NonGamePlayUI.PlayerInfo.PlayerInfo;
import heroiceraser.mulatschak.gameSettings.GameSettings;


public class GameController {

    //----------------------------------------------------------------------------------------------
    //  Constants
    //
    public static int NOT_SET = -9999;
    public boolean DEBUG;

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private boolean enable_drawing_;
    private boolean playerPresentation;
    private boolean gameStarted;


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

    public void clear() {

        player_info_.clear();

        view_ = null;
        layout_ = null;
        touch_events_ = null;
        logic_ = null;
        settings_ = null;

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
        DEBUG = mainActivity.DEBUG;
        view_ = view;
        enable_drawing_ = false;
        playerPresentation = false;
        gameStarted = false;

        logic_ = new GameLogic();

        playerHandsView = new PlayerHandsView();
        touch_events_ = new TouchEvents();
        settings_ = new GameSettings();

        non_game_play_ui_ = new NonGamePlayUIContainer();

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
    public void init(final GameSettings gameSettings) {

        long start = System.currentTimeMillis();
        logic_.init(gameSettings.playerLives, gameSettings.difficulty, gameSettings.maxLives);
        layout_ = new GameLayout(view_);

        settings_.init(view_);
        initPlayers(gameSettings.enemies);                                      // special multi
        resetPlayerLives();
        player_info_.init(view_);
        non_game_play_ui_.init(view_);
        player_info_.startPresentation(this);

        game_play_ = new GamePlay(view_);

        long time = System.currentTimeMillis() - start;
        if (DEBUG) {
            Log.d("gebrauchte start zeit: ", time + "");
        }

        // init the rest while the players get presented
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                if (DEBUG) {
                    Log.d("--", 6 + " - " + System.currentTimeMillis());
                }
                discardPile_.init(view_);
                if (DEBUG) {
                    Log.d("--", 8 + " - " + System.currentTimeMillis());
                }
                deck_.initDeck(view_, gameSettings.cardDesign);
                if (DEBUG) {
                    Log.d("--", 7 + " - " + System.currentTimeMillis());
                }
                game_play_.init(view_);
                if (DEBUG) {
                    Log.d("--", 9 + " - " + System.currentTimeMillis());
                }
                dealer_button_.init(view_);
                if (DEBUG) {
                    Log.d("--", 10 + " - " + System.currentTimeMillis());
                }
                playerHandsView.init(view_);
                if (DEBUG) {
                    Log.d("--", 11 + " - " + System.currentTimeMillis());
                }

                // tell everyone my game is fully laoded
                getPlayerByPosition(0).setGameStarted(true);

                enable_drawing_ = true;
                long time = System.currentTimeMillis() - start;
                if (DEBUG) {
                    Log.d("gebrauchte zeit init2:", time + "");
                }
            }
        };
        Thread t = new Thread(runnable);
        t.start();
    }

    public void startGame() {

        if (enable_drawing_) {
            if (DEBUG) {
                Log.d("Game Controller", "start game");
            }
            playerPresentation = false;
            chooseFirstDealerRandomly();
            startRound();
        } else {
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
        if (DEBUG) {
            Log.d("------------", "StartRound -----------------------------------------------------------");
        }
        for (MyPlayer player : getPlayerList()) {
            player.exchanged_cards_.getCardStack().clear();
            player.played_cards_.getCardStack().clear();
        }

        player_info_.setVisible(true);

        // clean up last round
        non_game_play_ui_.getTricks().clear();
        allCardsBackToTheDeck();
        logic_.setMulatschakRound(false);
        resetTricksToMake();

        player_info_.setActivePlayer(NOT_SET);
        player_info_.setShowPlayer0Turn(false);
        discardPile_.setVisible(false);
        discardPile_.setOverlaysVisible(false);
        game_play_.startRound(this);
        for (MyPlayer player : getPlayerList()) {
            player.setMissATurn(false);
        }

        dealer_button_.startMoveAnimation(this, getPlayerById(logic_.getDealer()).getPosition());
        if (DEBUG) {
            Log.d("------------", "-----------------------------------------------------------");
        }
        shuffleDeck();
    }

    private void shuffleDeck() {
        if (DEBUG) {
            Log.d("------------", "Shuffle -----------------------------------------------------------");
        }
        deck_.shuffleDeck();
        continueAfterShuffledDeck();
    }

    private void continueAfterShuffledDeck() {
        if (DEBUG) {
            Log.d("------------", "Shuffled -----------------------------------------------------------");
        }
        for (int i = 0; i < 10; i++) {
            if (DEBUG) {
                Log.d("Deck is: ", " " + deck_.getCardAt(i).getId());
            }
        }
        game_play_.getDealCards().dealCards(this);  // starts an dealing animation
    }

    public void continueAfterDealingAnimation() {
        if (DEBUG) {
            Log.d("------------", "decide Muli -----------------------------------------------------------");
        }
        if (DEBUG) {
            Log.d("------------", "Turn: " + getPlayerById(logic_.getTurn()).getDisplayName() + " -----------------------------------------------------------");
        }
        game_play_.getDecideMulatschak().startMulatschakDecision(this);
    }

    public void continueAfterDecideMulatschak() {
        if (DEBUG) {
            Log.d("------------", "trick bids -----------------------------------------------------------");
        }
        game_play_.getTrickBids().startTrickBids(this);
    }

    public void continueAfterTrickBids() {
        if (DEBUG) {
            Log.d("------------", "choose trumps -----------------------------------------------------------");
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
                logic_.raiseMultiplier(controller);
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

    public void continueAfterTrumpWasChosen() {
        if (DEBUG) {
            Log.d("------------", "exchange cards -----------------------------------------------------------");
        }
        for (MyPlayer player : getPlayerList()) {
            for (Card c : player.getHand().getCardStack()) {
                if (DEBUG) {
                    Log.d("------------", player.getDisplayName() + ": " + c.getId() + "");
                }
            }
            if (DEBUG) {
                Log.d("---------", "-----");
            }
        }
        game_play_.getTrickBids().getBidsView().setVisible(true);
        setTurn(logic_.getTrumpPlayerId());
        logic_.setStartingPlayer(logic_.getTrumpPlayerId());
        if (logic_.isMulatschakRound()) {
            continueAfterCardExchange();
        } else {
            game_play_.getCardExchange().makeCardExchange(true, this); // first player to bid is the player next to the dealer
        }
    }

    public void continueAfterCardExchange() {
        if (DEBUG) {
            Log.d("------------", "play a card -----------------------------------------------------------");
        }
        for (MyPlayer player : getPlayerList()) {
            for (Card c : player.getHand().getCardStack()) {
                if (DEBUG) {
                    Log.d("------------", player.getDisplayName() + ": " + c.getId() + "");
                }
            }
            if (DEBUG) {
                Log.d("---------", "-----");
            }
        }
        discardPile_.setVisible(true);
        player_info_.setShowPlayer0Turn(true);
        playACardCounter = 0;
        logic_.setStartingPlayer(logic_.getTrumpPlayerId());
        logic_.setTurn(logic_.getTrumpPlayerId());
        getGamePlay().getPlayACardRound().playACard(true, this);
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
        if (DEBUG) {
            Log.d("Next Round", "___________________________________");
        }
        logic_.setStartingPlayer(logic_.getRoundWinnerId());
        logic_.setTurn(logic_.getRoundWinnerId());
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

    private void initPlayers(int enemies) {
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
                getPlayerById(i).setDisplayName("Du");
                getPlayerById(i).setOnlineId("Du");
                getPlayerById(i).setEnemyLogic(false);
            } else {
                getPlayerById(i).setEnemyLogic(true);
            }
        }

        setPlayerPositions();
    }

    private void setDisplayName(@NonNull MyPlayer myPlayer) {

        String text;

        if (myPlayer.getId() == 0) {
            text = "todo";
        } else {
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
            getPlayerById(i).setLives(logic_.getStartLives());
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

    private void chooseFirstDealerRandomly() {
        Random random_generator = new Random();
        int dealer_id = random_generator.nextInt(getAmountOfPlayers());
        logic_.setDealer(dealer_id);
        dealer_button_.startMoveAnimation(this, getPlayerById(dealer_id).getPosition());
        logic_.setStartingPlayer(logic_.getFirstBidder(getAmountOfPlayers()));
        setTurn(dealer_id);
    }


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


    private void allCardsBackToTheDeck() {

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
            deck_.initDeck(view_, deck_.getDesign());
        }

    }

    public void moveCardToTrash(Card card) {
        trash_.addCard(card);
    }


    public void lastPlayerLeftSoLetMeWin() {
        //view_.enableUpdateCanvasThread();
        for (int i = 0; i < getAmountOfPlayers(); i++) {
            getPlayerById(i).setLives(1);
            if (getPlayerById(i).getPosition() == 0) {
                getPlayerById(i).setLives(0);
            }
        }

        getNonGamePlayUIContainer().getStatistics().updatePlayerLives(this);
        getGameOver().setGameGameOver(this);
    }


    public int playACardCounter = 0;

    //----------------------------------------------------------------------------------------------
    //  Getter
    //
    public GameView getView() {
        return view_;
    }

    public GameLayout getLayout() {
        return layout_;
    }

    public PlayerHandsView getPlayerHandsView() {
        return playerHandsView;
    }

    public GameLogic getLogic() {
        return logic_;
    }

    TouchEvents getTouchEvents() {
        return touch_events_;
    }

    public MulatschakDeck getDeck() {
        return deck_;
    }

    public DiscardPile getDiscardPile() {
        return discardPile_;
    }

    public List<MyPlayer> getPlayerList() {
        return myPlayer_list_;
    }

    public MyPlayer getPlayerById(int id) {
        try {
            return myPlayer_list_.get(id);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e("con", "player not found by online id");
            }
            return null;
        }
    }

    public MyPlayer getPlayerByPosition(int pos) {

        try {
            for (MyPlayer p : getPlayerList()) {
                if (pos == p.getPosition()) {
                    return p;
                }
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.e("con", "player not found by online id");
            }
            return null;
        }
        if (DEBUG) {
            Log.e("con", "player not found by pos");
        }
        return null;
    }

    public int getAmountOfPlayers() {
        return myPlayer_list_.size();
    }

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

    public PlayerInfo getPlayerInfo() {
        return player_info_;
    }

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
