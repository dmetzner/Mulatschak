package heroiceraser.mulatschak.game;

import android.os.Handler;
import android.util.Log;

import com.google.android.gms.games.multiplayer.Participant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.game.Animations.TrickBids;
import heroiceraser.mulatschak.game.DrawableObjects.ButtonBar;
import heroiceraser.mulatschak.game.DrawableObjects.Card;
import heroiceraser.mulatschak.game.DrawableObjects.CardStack;
import heroiceraser.mulatschak.game.DrawableObjects.DealerButton;
import heroiceraser.mulatschak.game.DrawableObjects.DiscardPile;
import heroiceraser.mulatschak.game.DrawableObjects.GameMenu;
import heroiceraser.mulatschak.game.DrawableObjects.GameOver;
import heroiceraser.mulatschak.game.DrawableObjects.GameStatistics;
import heroiceraser.mulatschak.game.DrawableObjects.GameTricks;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.Animations.GameAnimation;
import heroiceraser.mulatschak.game.DrawableObjects.PlayerInfo;
import heroiceraser.mulatschak.game.DrawableObjects.RoundInfo;


/**
 * Created by Daniel Metzner on 10.08.2017.
 */

public class GameController{

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
    private GameAnimation animations_;
    private TouchEvents touch_events_;
    private GameLogic logic_;

    private ButtonBar button_bar_;
    private GameStatistics statistics_;
    private GameTricks tricks_;
    private GameMenu menu_;

    private RoundInfo round_info_;
    private PlayerInfo player_info_;
    private GameOver game_over_;

    private List<Player> player_list_;
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

        button_bar_ = new ButtonBar();
        statistics_ = new GameStatistics();
        tricks_ = new GameTricks();
        menu_ = new GameMenu();

        round_info_ = new RoundInfo();
        player_info_ = new PlayerInfo();
        game_over_ = new GameOver();

        deck_ = new MulatschakDeck();
        trash_ = new CardStack();
        discardPile_ = new DiscardPile();
        player_list_ = new ArrayList<>();

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

        initPlayers(my_id, enemies);
        resetPlayerLives();
        setPlayerPositions();
        player_info_.init(view_);

        deck_.initDeck(view_);
        discardPile_.init(view_);
        dealer_button_.init(view_);
        animations_.init(view_);
        button_bar_.init(view_);
        statistics_.init(view_);
        tricks_.init(view_);
        menu_.init(view_);
        round_info_.init(view_);
        game_over_.init(view_);

        enable_drawing_ = true;
        chooseFirstDealerRandomly();
        startRound();
    }

    public void startRound() {
        player_info_.setVisible(true);
        round_info_.setVisible(true);
        round_info_.setInfoBoxEmpty();
        tricks_.clear();
        round_info_.updateNewRound(this);
        round_info_.getNewRound().setVisible(true);
        logic_.setMulatschakRound(false);
        reEnableButtons();
        resetTricksToMake();
        animations_.getCardAnimations().setCardMoveable(false);
        resetTurn();
        view_.enableUpdateCanvasThread();
        shuffleDeck();
        discardPile_.setVisible(false);
        dealCards();  // starts an dealing animation
    }

    public void continueAfterDealingAnimation() {
        discardPile_.setVisible(true);
        Log.d("GameController2", "Dealer is Player" + logic_.getDealer());
        boolean first_call = true;
        round_info_.setInfoBoxEmpty();
        round_info_.getTrickBidsTextField().setVisible(true);
        makeTrickBids(first_call);
    }

    public void continueAfterTrickBids() {
        round_info_.setInfoBoxEmpty();
        round_info_.getChooseTrumpTextField().setVisible(true);
        Handler mHandler = new Handler();
        Runnable case_0 = new Runnable() {
            @Override
            public void run() {
                startRound();
            }
        };
        Runnable case_1 = new Runnable() {
            @Override
            public void run() {
                continueAfterTrumpWasChoosen();
            }
        };
        switch (checkHighestBid()) {
            case 0:  // start a new round if every player said 0 tricks
                logic_.raiseMultiplier();
                round_info_.updateChooseTrump(this, 0);
                mHandler.postDelayed(case_0, 3000);
                break;
            case 1:
                logic_.setTrump(MulatschakDeck.HEART);
                round_info_.updateChooseTrump(this, 1);
                mHandler.postDelayed(case_1, 4000);
                break;
            default:
                letHighestBidderChooseTrump();
                break;
        }
    }

    public void continueAfterTrumpWasChoosen() {
        if (logic_.getTrump() == MulatschakDeck.HEART) {
            logic_.raiseMultiplier();
        }
        round_info_.setInfoBoxEmpty();
        round_info_.updateRoundInfo(this);
        round_info_.getRoundInfoTextField().setVisible(true);
        logic_.setTurn(logic_.getTrumpPlayerId());
        logic_.setStartingPlayer(logic_.getTrumpPlayerId());
        boolean first_call = true;
        if (logic_.isMulatschakRound()) {
            continueAfterCardExchange();
        }
        else {
            makeCardExchange(first_call);        // first player to bid is the player next to the dealer
        }

    }

    public void continueAfterCardExchange() {
        nextCardRound(); // first call
    }


    private void initPlayers(String my_id, int enemies) {

        if (multiplayer_) {
            for (int i = 0; i < participants_.size(); i++) {
                Player new_player = new Player(view_);
                new_player.participant_ = participants_.get(i);
                int player_id = i;
                player_list_.add(player_id, new_player);
            }

        }

        // simple Singleplayer
        if (!multiplayer_) {
            int players = enemies + 1;
            for (int i = 0; i < players; i++) {
                Player new_player = new Player(view_);
                new_player.setId(i);
                player_list_.add(new_player);
            }
        }

        for (int i = 0; i < player_list_.size(); i++) {
            setDisplayName(getPlayerById(i));
        }
    }

    public void setDisplayName(Player player) {

        String text = "";

        if (player.participant_ != null) {
            text = player.participant_.getDisplayName();
        }
        else if (player.getId() == 0) {
            text = view_.getController().my_display_name_;
        }
        else {
            text = "Muli Bot " + player.getId();
        }
        if (text.length() > 15) {
            text = text.substring(0, 15);
        }

        Log.d("PlayerInfo", "Player " + player.getId() + " -> " + text);
        player.setDisplayName(text);
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

    private void reEnableButtons() {
        List<MyButton> buttons = getAnimation().getTrickBids().getNumberButtons();
        for (int i = 0; i < buttons.size(); i++) {
            if (getAmountOfPlayers() <= 2 && i == 0) {
                buttons.get(i).setEnabled(false);
            }
            else {
                buttons.get(i).setEnabled(true);
            }
        }

        List<MyButton> buttons_sym = getAnimation().getTrickBids().getTrumpButtons();
        for (int i = 0; i < buttons_sym.size(); i++) {
            buttons_sym.get(i).setEnabled(true);
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
        dealer_button_.setPosition(
                layout_.getDealerButtonPosition(getPlayerById(dealer_id).getPosition()));
        logic_.setStartingPlayer(logic_.getFirstBidder(getAmountOfPlayers()));
        logic_.setTurn(dealer_id);
    }

    //----------------------------------------------------------------------------------------------
    //
    //
    private void shuffleDeck() {
        Collections.shuffle(deck_.getCardStack(), new Random());
    }

    //----------------------------------------------------------------------------------------------
    //
    //
    private void dealCards() {
        deal(getAmountOfPlayers());
        getAnimation().getDealingAnimation().start();
    }

    //----------------------------------------------------------------------------------------------
    //  deal
    //
    private void deal(int players) {
        for (int hand_card = 0; hand_card < logic_.MAX_CARDS_PER_HAND; hand_card++) {
            for (int player_id = 0; player_id < players; player_id++) {
                takeCardFromDeck(player_id, deck_);
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    //  draw cards from deck
    //
    public boolean takeCardFromDeck(int player_id, MulatschakDeck deck){

        if (deck.getCardStack().isEmpty()) {
            //  do something when deck is empty  ->
            Log.d("take a card", " yo we a got a prob");
            return false;
        }
        else
        {
            CardStack player_hand =  getPlayerById(player_id).getHand();
            player_hand.addCard(deck_.getCardAt(0));
            deck.getCardStack().remove(0);
            return true;
        }
    }


    //----------------------------------------------------------------------------------------------
    //  cada
    //
    private void resetTurn() {
        logic_.setTurn(logic_.getDealer());
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
    private void turnToNextPlayer() {
        logic_.turnToNextPlayer(getAmountOfPlayers());
    }

    //----------------------------------------------------------------------------------------------
    //  cada
    //
    public void makeCardExchange() {
        makeCardExchange(false); // not first call
    }

    private void makeCardExchange(boolean first_call) {

        if (!first_call) {
            turnToNextPlayer();
        }

        if (!first_call && logic_.getTurn() == logic_.getStartingPlayer()) {
            continueAfterCardExchange();
            return;         // stops the recursion after all players had a chance to exchange their cards
        }

        if (getPlayerById(logic_.getTurn()).getMissATurn()) {
            Log.d("GameController2", "Player " + logic_.getTurn() + " is MISSING his turn");
            makeCardExchange();
            return;
        }

        Log.d("GameController2", "Player " + logic_.getTurn() + " is making his turn");

        if (logic_.getTurn() == 0) {
            animations_.getCardExchange().setAnimationRunning(true);
            view_.disableUpdateCanvasThread();
        }
        else if (logic_.getTurn() != 0) {
            view_.enableUpdateCanvasThread();
            EnemyLogic enemy_logic = new EnemyLogic();
            enemy_logic.makeCardExchange(getPlayerById(logic_.getTurn()), this);
            makeCardExchange();
        }
    }


    //----------------------------------------------------------------------------------------------
    //  cada
    //
    public void makeTrickBids() {
        makeTrickBids(false); // not first call
    }

    private void makeTrickBids(boolean first_call) {

        turnToNextPlayer();
        round_info_.updateBids(view_);

        if (logic_.getTricksToMake() == TrickBids.MULATSCHAK) {
            continueAfterTrickBids();
            return;
        }

        if (!first_call && logic_.getTurn() == logic_.getFirstBidder(getAmountOfPlayers())) {
            continueAfterTrickBids();
            return;         // stops the recursion after all players made their bids
        }

        Log.d("GameController2", "Player " +logic_.getTurn() + " is making his bids");
        if (logic_.getTurn() == 0) {
            if (getPlayerById(0).getMissATurn()) {
                animations_.getTrickBids().getNumberButtonAt(0).setEnabled(false);
            }
            animations_.getTrickBids().setAnimationNumbers(true);
            view_.disableUpdateCanvasThread();
            // makeTrickBids should get called when player chooses his tricks
        }
        else if (logic_.getTurn() != 0) {
            EnemyLogic el = new EnemyLogic();
            view_.enableUpdateCanvasThread();
            el.makeTrickBids(getPlayerById(logic_.getTurn()), this);

            Handler mHandler = new Handler();
            Runnable codeToRun = new Runnable() {
                @Override
                public void run() {
                    makeTrickBids();
                }
            };
            mHandler.postDelayed(codeToRun, 1000);
        }
    }

    //----------------------------------------------------------------------------------------------
    //  cada
    //
    private int checkHighestBid() {
        int highest_bid = 0;
        for (int id = 0; id < getAmountOfPlayers(); id++) {
            if (getPlayerById(id).getTricksToMake() > highest_bid) {
                highest_bid = getPlayerById(id).getTricksToMake();
            }
        }
        return highest_bid;
    }

    //----------------------------------------------------------------------------------------------
    //  cada
    //
    public void setNewMaxTrumps(int amount, int id) {
        CharSequence text = "player: " + id + " tries ";

        if (amount == TrickBids.MULATSCHAK) {
            logic_.setMulatschakRound(true);
            logic_.setTrumpPlayerId(id);
            logic_.setTricksToMake(TrickBids.MULATSCHAK);
            getPlayerById(id).setTricksToMake(TrickBids.MULATSCHAK);
            text = text + "Mulatschak";
        }

        else if (amount > logic_.getTricksToMake()) {
            List<MyButton> buttons = getAnimation().getTrickBids().getNumberButtons();
            // disable lower amount buttons, but button 0 is always clickable // miss a turn
            for (int i = 2; i <= (amount + 1); i++) {
                buttons.get(i).setEnabled(false);
            }
            getPlayerById(id).setTricksToMake(amount);
            logic_.setTricksToMake(amount);
            logic_.setTrumpPlayerId(id);
            text = text + Integer.toString(amount);
        }
        else {
            getPlayerById(id).setTricksToMake(0);
            text = text + "0";
        }

        int amount_of_playing_players =  getAmountOfPlayers();
        for (int i = 0; i < getAmountOfPlayers(); i++) {
            if (getPlayerById(i).getMissATurn()) {
                amount_of_playing_players--;
            }
        }
        if (amount_of_playing_players <= 2) {
            List<MyButton> buttons = getAnimation().getTrickBids().getNumberButtons();
            buttons.get(0).setEnabled(false);
        }

    }

    //----------------------------------------------------------------------------------------------
    //  cada
    //
    private void letHighestBidderChooseTrump() {
        Log.d("GameController2", "Player " + logic_.getTrumpPlayerId() + " chooses a trump");
        round_info_.updateChooseTrump(this);

        if (logic_.getTrumpPlayerId() == 0) {
            animations_.getTrickBids().setAnimationTrumps(true);
            view_.disableUpdateCanvasThread();
            // touch event should call continueAfterTrumpWasChoosen();
        }
        else if (logic_.getTrumpPlayerId() != 0) {
            EnemyLogic el = new EnemyLogic();
            view_.enableUpdateCanvasThread();
            el.chooseTrump(getPlayerById(logic_.getTrumpPlayerId()), logic_, view_);
            Handler mhandler = new Handler();
            Runnable codeToRun = new Runnable() {
                @Override
                public void run() {
                    continueAfterTrumpWasChoosen();
                }
            };
            mhandler.postDelayed(codeToRun, 3000);
        }
    }

    public void nextTurn() {
        nextTurn(false);
    }

    private void nextCardRound() {

        // view_.enableUpdateCanvasThread(); // Todo
        if (getPlayerById(logic_.getTurn()).getAmountOfCardsInHand() == 0) {
            updatePlayerLives();
            statistics_.updatePlayerLives(this);
            if (logic_.isGameOver()) {
                round_info_.setInfoBoxEmpty();
                round_info_.getGameOver().setVisible(true);
                game_over_.setVisible(true);
                return;
            }
            round_info_.setInfoBoxEmpty();
            round_info_.getEndOfRound().setVisible(true);
            round_info_.updateEndOfRound(this);
            allCardsBackToTheDeck();
            logic_.moveDealer(getAmountOfPlayers());
            dealer_button_.setPosition(layout_.getDealerButtonPosition(logic_.getDealer()));
            waiting2 = true;
            // touch events starts new round        ^^
            return;
        }
        boolean first_call = true;
        round_info_.updateRoundInfo(this);
        nextTurn(first_call);

    }

    public boolean waiting2 = false;


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
        }

    }
    private void endCardRound() {
        clearDiscardPile();
        nextCardRound();
    }
    private void clearDiscardPile() {
        discardPile_.setCardBottom(null);
        discardPile_.setCardLeft(null);
        discardPile_.setCardTop(null);
        discardPile_.setCardRight(null);
    }

    private void nextTurn(boolean first_call) {

        animations_.getCardAnimations().setCardMoveable(false);
        round_info_.setInfoBoxEmpty();
        round_info_.getRoundInfoTextField().setVisible(true);
        round_info_.updateRoundInfo(this);

        if (!first_call && logic_.getTurn() == logic_.getStartingPlayer()) {
            logic_.chooseCardRoundWinner(this, this.getDiscardPile());
            addTricksToWinner();
            logic_.setTurn(logic_.getRoundWinnerId());
            logic_.setStartingPlayer(logic_.getRoundWinnerId());

            round_info_.setInfoBoxEmpty();
            round_info_.updateEndOfCardRound(this);
            round_info_.getEndOfCardRound().setVisible(true);

            wait_for_end_card_round_ = true;

            // ToDO show winner of the round, click to next round button

            Handler end_round_handler = new Handler();
            Runnable end_round_runnable = new Runnable() {
                @Override
                public void run() {
                    if (wait_for_end_card_round_) {
                        endCardRound();
                    }
                }
            };
            int max_waiting_time = 5000;
            end_round_handler.postDelayed(end_round_runnable, max_waiting_time);
            return;
        }

        if (getPlayerById(logic_.getTurn()).getMissATurn()) {
            Log.d("GameController2", "Player " + logic_.getTurn() + " is MISSING his turn");
            turnToNextPlayer();
            nextTurn();
            return;
        }

        Log.d("GameController2", "Player " +logic_.getTurn() + " is playing his card");

        if (logic_.getTurn() == 0) {
            animations_.getCardAnimations().setCardMoveable(true);
            view_.disableUpdateCanvasThread();
            // touch event calls next turnToNextPlayer & next turn
        }
        else if (logic_.getTurn() != 0) {
            EnemyLogic el = new EnemyLogic();
            view_.enableUpdateCanvasThread();
            el.playCard(logic_, getPlayerById(logic_.getTurn()), discardPile_);
            getLogic().turnToNextPlayer(getAmountOfPlayers());
            nextTurn();
        }

    }

    private void addTricksToWinner() {

        tricks_.getDiscardPiles().add(DiscardPile.copy(discardPile_));
        int index = tricks_.getDiscardPiles().size() - 1;
        if (index >= 0 && tricks_.getDiscardPiles() != null) {
            ((DiscardPile) (tricks_.getDiscardPiles().get(index)))
                    .setPositions(layout_.getButtonBarWindowTricksDiscardPilePositions());
            tricks_.setVisibleRoundId(index);
            tricks_.updateVisibleRound();
        }

        //tricks_.getRoundWinners().add(logic_.getRoundWinnerId());

        for (int i = 0; i < discardPile_.SIZE; i++) {
            if (discardPile_.getCard(i) != null) {
                getPlayerById(logic_.getRoundWinnerId()).getTricks().addCard(discardPile_.getCard(i));
            }
        }
    }

    public void updatePlayerLives() {

        boolean mulatschak = false;
        if (logic_.isMulatschakRound()) {
            if ((int) (getPlayerById(logic_.getTrumpPlayerId()).getTricks().getCardStack().size()
                    / getAmountOfPlayers()) == GameLogic.MAX_CARDS_PER_HAND) {
                mulatschak = true;
            }
            if (!mulatschak) {
                for (int i = 0; i < getAmountOfPlayers(); i++) {
                    int add_lives = 0;
                    if (logic_.getTrumpPlayerId() == i) {
                        add_lives = 10 * logic_.getMultiplier();
                    }
                    else {
                        add_lives = -10 * logic_.getMultiplier();
                    }
                    int new_lives = getPlayerById(i).getLives() + add_lives;
                    if (new_lives <= 0 ) {
                        new_lives = 0;
                        logic_.setGameOver(true);
                    }
                    getPlayerById(i).setLives(new_lives);
                }
                return;
            }
            else if (mulatschak) {
                for (int i = 0; i < getAmountOfPlayers(); i++) {
                    int add_lives = 0;
                    if (logic_.getTrumpPlayerId() == i) {
                        add_lives = -10 * logic_.getMultiplier();
                    }
                    else {
                        add_lives = 10 * logic_.getMultiplier();
                    }
                    int new_lives = getPlayerById(i).getLives() + add_lives;
                    if (new_lives <= 0 ) {
                        new_lives = 0;
                        logic_.setGameOver(true);
                    }
                    getPlayerById(i).setLives(new_lives);
                }
                return;
            }
        }

        for (int i = 0; i < getAmountOfPlayers(); i++) {
            int add_lives = 0;
            int tricks = getPlayerById(i).getTricks().getCardStack().size() / getAmountOfPlayers();

            if (getPlayerById(i).getMissATurn()) {
                add_lives = 2 * logic_.getMultiplier();
            }
            else if (i == logic_.getTrumpPlayerId() && tricks < logic_.getTricksToMake()) {
                add_lives = 10 * logic_.getMultiplier();
            }
            else if (tricks <= 0) {
                add_lives = 5 * logic_.getMultiplier();
            }
            else {
                add_lives = (-1) * tricks;
            }
            int new_lives = getPlayerById(i).getLives() + add_lives;
            if (new_lives <= 0 ) {
                new_lives = 0;
                logic_.setGameOver(true);
            }
            getPlayerById(i).setLives(new_lives);
        }
    }

    public void moveCardToTrash(Card card) {
        trash_.addCard(card);
    }

    public void moveCardsToTrash(List<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            trash_.addCard(cards.get(i));
        }
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

    public List<Player> getPlayerList() { return player_list_; }

    public Player getPlayerById(int id) { return player_list_.get(id); }

    public Player getPlayerByPosition(int pos) {
        for (int i = 0; i < getAmountOfPlayers(); i++) {
            if (pos == getPlayerById(i).getPosition()) {
                return getPlayerById(i);
            }
        }
        Log.d("GameController", "getPlayerByPosition: wrong pos!");
        return getPlayerByPosition(0);
    }

    public int getAmountOfPlayers() { return player_list_.size(); }

    public ButtonBar getButtonBar() {
        return button_bar_;
    }

    public GameStatistics getStatistics() {
        return statistics_;
    }

    public GameTricks getTricks() { return tricks_; }

    public GameMenu getMenu() { return  menu_; }

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
        return round_info_;
    }

    public PlayerInfo getPlayerInfo() { return player_info_; }

    public boolean isDrawingEnabled() {
        return enable_drawing_;
    }
}
