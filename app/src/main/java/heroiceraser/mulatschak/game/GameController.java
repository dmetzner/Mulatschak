package heroiceraser.mulatschak.game;

import android.os.Handler;
import android.util.Log;

import com.google.android.gms.games.multiplayer.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import heroiceraser.mulatschak.GameSettings.GameSettings;
import heroiceraser.mulatschak.game.DrawableObjects.MyPlayer;
import heroiceraser.mulatschak.game.GamePlay.GamePlay;
import heroiceraser.mulatschak.game.NonGamePlayUI.NonGamePlayUIContainer;
import heroiceraser.mulatschak.game.DrawableObjects.Card;
import heroiceraser.mulatschak.game.DrawableObjects.CardStack;
import heroiceraser.mulatschak.game.DrawableObjects.DealerButton;
import heroiceraser.mulatschak.game.DrawableObjects.DiscardPile;
import heroiceraser.mulatschak.game.NonGamePlayUI.GameOver.GameOver;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar.Windows.GameTricks;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.Animations.GameAnimation;
import heroiceraser.mulatschak.game.NonGamePlayUI.PlayerInfo.PlayerInfo;
import heroiceraser.mulatschak.game.NonGamePlayUI.RoundInfo.RoundInfo;



public class GameController{

    //----------------------------------------------------------------------------------------------
    //  Constants
    //
    public static int NOT_SET = -9999;

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private boolean enable_drawing_;

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

    private GameAnimation animations_;

    private NonGamePlayUIContainer non_game_play_ui_;
    private GamePlay game_play_;

    private List<MyPlayer> myPlayer_list_;
    private PlayerInfo player_info_;

    private GameOver game_over_;

    private MulatschakDeck deck_;
    private DiscardPile discardPile_;
    private CardStack trash_;
    private DealerButton dealer_button_;

    private boolean wait_for_end_card_round_;
    // ToDo


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameController(GameView view) {
        view_ = view;
        enable_drawing_ = false;

        logic_ = new GameLogic();
        layout_ = new GameLayout();
        animations_ = new GameAnimation(view);
        touch_events_ = new TouchEvents();
        settings_ = new GameSettings();

        non_game_play_ui_ = new NonGamePlayUIContainer();
        game_play_ = new GamePlay();

        myPlayer_list_ = new ArrayList<>();
        player_info_ = new PlayerInfo();

        game_over_ = new GameOver();

        deck_ = new MulatschakDeck();
        trash_ = new CardStack();
        discardPile_ = new DiscardPile();

        dealer_button_ = new DealerButton();


        wait_for_end_card_round_ = false;

    }

    //----------------------------------------------------------------------------------------------
    //  start
    //
    public void start(int start_lives, final int enemies, final int difficulty, boolean multiplayer,
                      final String myName, String my_id, ArrayList<Participant> participants) {
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

        logic_.init(start_lives, difficulty);
        layout_.init(view_);
        settings_.init(view_);
        initPlayers(my_id, enemies);
        resetPlayerLives();
        setPlayerPositions();
        player_info_.init(view_);
        non_game_play_ui_.init(view_);
        discardPile_.init(view_);
        game_play_.init(view_);
        deck_.initDeck(view_);
        dealer_button_.init(view_);
        game_over_.init(view_);

        enable_drawing_ = true;
        chooseFirstDealerRandomly();
        startRound();
    }

    // ---------------------------------------------------------------------------------------------
    // ----------------------  Game Flow

    // ---------------------------------------------------------------------------------------------
    //  startRound
    //
    public void startRound() {
        player_info_.setVisible(true);
        non_game_play_ui_.getRoundInfo().setVisible(true);
        non_game_play_ui_.getRoundInfo().setInfoBoxEmpty();
        non_game_play_ui_.getRoundInfo().updateNewRound(this);
        non_game_play_ui_.getRoundInfo().getNewRound().setVisible(true);
        non_game_play_ui_.getTricks().clear();

        allCardsBackToTheDeck();

        logic_.setMulatschakRound(false);
        resetTricksToMake();
        // getGamePlay().getPlayACard().setCardMovable(false);
        player_info_.setActivePlayer(NOT_SET);
        player_info_.setShowPlayer0Turn(false);

        discardPile_.setVisible(false);
        discardPile_.setOverlaysVisible(false);

        game_play_.startRound(this);

        view_.enableUpdateCanvasThread();
        dealer_button_.startMoveAnimation(this, logic_.getDealer());
        deck_.shuffleDeck();
        game_play_.getDealCards().dealCards(this);  // starts an dealing animation
    }


    //----------------------------------------------------------------------------------------------
    //  continueAfterDealingAnimation
    //
    public void continueAfterDealingAnimation() {
        non_game_play_ui_.getRoundInfo().setInfoBoxEmpty();
        game_play_.getDecideMulatschak().startMulatschakDecision(this);
    }


    //----------------------------------------------------------------------------------------------
    //  continueAfterDecideMulatschak
    //
    public void continueAfterDecideMulatschak() {
        non_game_play_ui_.getRoundInfo().getTrickBidsTextField().setVisible(true);
        game_play_.getTrickBids().startTrickBids(this);
    }


    //----------------------------------------------------------------------------------------------
    //  continueAfterTrickBids
    //
    public void continueAfterTrickBids() {
        non_game_play_ui_.getRoundInfo().setInfoBoxEmpty();
        non_game_play_ui_.getRoundInfo().getChooseTrumpTextField().setVisible(true);

        final GameController controller = this;
        Handler mHandler = new Handler();
        Runnable case_0 = new Runnable() {
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
                non_game_play_ui_.getRoundInfo().updateChooseTrump(this, 0);
                mHandler.postDelayed(case_0, 3000);
                break;

            case 1: // heart round -> no trumps to choose
                logic_.setTrump(MulatschakDeck.HEART);
                setTurn(logic_.getTrumpPlayerId());
                non_game_play_ui_.getRoundInfo().updateChooseTrump(this, 1);
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
        non_game_play_ui_.getRoundInfo().setInfoBoxEmpty();
        non_game_play_ui_.getRoundInfo().updateRoundInfo(this);
        non_game_play_ui_.getRoundInfo().getRoundInfoTextField().setVisible(true);
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

        String text = "";

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

        Log.d("PlayerInfo", "MyPlayer " + myPlayer.getId() + " -> " + text);
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
                getPlayerById(0).setPosition(layout_.POSITION_BOTTOM);
                break;
            case 2:
                getPlayerById(0).setPosition(layout_.POSITION_BOTTOM);
                getPlayerById(1).setPosition(layout_.POSITION_TOP);
                break;
            case 3:
                getPlayerById(0).setPosition(layout_.POSITION_BOTTOM);
                getPlayerById(1).setPosition(layout_.POSITION_LEFT);
                getPlayerById(2).setPosition(layout_.POSITION_TOP);
                break;
            case 4:
                getPlayerById(0).setPosition(layout_.POSITION_BOTTOM);
                getPlayerById(1).setPosition(layout_.POSITION_LEFT);
                getPlayerById(2).setPosition(layout_.POSITION_TOP);
                getPlayerById(3).setPosition(layout_.POSITION_RIGHT);
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
    //  cada
    //
    public void turnToNextPlayer() {
        logic_.turnToNextPlayer(getAmountOfPlayers());
        player_info_.setActivePlayer(logic_.getTurn());
    }


    public void nextTurn() {
        nextTurn(false);
    }

    private void nextCardRound() {

        // update player lives, game over? or new round?
        // starts the animations if needed
        if (game_play_.getAllCardsPlayed().areAllCardsPlayed(this)) {
            game_play_.getAllCardsPlayed().allCardsArePlayedLogic(this);
            return;
        }

        // next round
        non_game_play_ui_.getRoundInfo().updateRoundInfo(this);
        nextTurn(true);
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

        if (deck_.getCardStack().size() != MulatschakDeck.CARDS_PER_DECK) {
            Log.e("DECK", " not large enough");
        }

    }


    private void endCardRound() {
        discardPile_.setOverlaysVisible(false);
        discardPile_.clear();
        nextCardRound();
    }

    private void nextTurn(boolean first_call) {

        getGamePlay().getPlayACard().setCardMovable(false);
        non_game_play_ui_.getRoundInfo().setInfoBoxEmpty();
        non_game_play_ui_.getRoundInfo().getRoundInfoTextField().setVisible(true);
        non_game_play_ui_.getRoundInfo().updateRoundInfo(this);

        if (!first_call && logic_.getTurn() == logic_.getStartingPlayer()) {
            logic_.chooseCardRoundWinner(this, this.getDiscardPile());
            addTricksToWinner();
            setTurn(logic_.getRoundWinnerId());
            logic_.setStartingPlayer(logic_.getRoundWinnerId());

            non_game_play_ui_.getRoundInfo().setInfoBoxEmpty();
            non_game_play_ui_.getRoundInfo().updateEndOfCardRound(this);
            non_game_play_ui_.getRoundInfo().getEndOfCardRound().setVisible(true);

            wait_for_end_card_round_ = true;

            // ToDO show winner of the round, click to next round button
            discardPile_.setOverlaysVisible(true);

            Handler end_round_handler = new Handler();
            Runnable end_round_runnable = new Runnable() {
                @Override
                public void run() {
                    if (wait_for_end_card_round_) {
                        endCardRound();
                    }
                }
            };
            int max_waiting_time = 2000;
            end_round_handler.postDelayed(end_round_runnable, max_waiting_time);
            return;
        }

        if (getPlayerById(logic_.getTurn()).getMissATurn()) {
            Log.d("GameController2", "MyPlayer " + logic_.getTurn() + " is MISSING his turn");
            turnToNextPlayer();
            nextTurn();
            return;
        }

        Log.d("GameController2", "MyPlayer " +logic_.getTurn() + " is playing his card");

        if (logic_.getTurn() == 0) {
            getGamePlay().getPlayACard().setCardMovable(true);
            view_.disableUpdateCanvasThread();
            // touch event calls next turnToNextPlayer & next turn
        }
        else if (logic_.getTurn() != 0) {
            view_.enableUpdateCanvasThread();
            game_play_.getPlayACard().getEnemyPlayACardLogic()
                    .playACard(logic_, getPlayerById(logic_.getTurn()), discardPile_);
        }
    }


    public void continueAfterEnemeyPlayedACard() {
        turnToNextPlayer();
        nextTurn();
    }


    private void addTricksToWinner() {

        GameTricks tricks = non_game_play_ui_.getTricks();
        tricks.addDiscardPile(DiscardPile.copy(discardPile_), logic_.getRoundWinnerId());
        int index = tricks.getDiscardPiles().size() - 1;
        tricks.setVisibleRoundId(index);
        tricks.updateVisibleRound();


        for (int i = 0; i < discardPile_.SIZE; i++) {
            if (discardPile_.getCard(i) != null) {
                getPlayerById(logic_.getRoundWinnerId()).getTricks().addCard(discardPile_.getCard(i));
            }
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

    public GameAnimation getAnimation() { return  animations_; }

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
        Log.d("GameController", "getPlayerByPosition: wrong pos!");
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
        return game_over_;
    }

    public RoundInfo getRoundInfo() {
        return non_game_play_ui_.getRoundInfo();
    }

    public PlayerInfo getPlayerInfo() { return player_info_; }

    public boolean isDrawingEnabled() {
        return enable_drawing_;
    }

    public GamePlay getGamePlay() {
        return game_play_;
    }

    public GameSettings getSettings() {
        return settings_;
    }
}
