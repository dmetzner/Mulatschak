package heroiceraser.mulatschak.game;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import heroiceraser.mulatschak.DrawableBasicObjects.Button;
import heroiceraser.mulatschak.game.DrawableObjects.ButtonBar;
import heroiceraser.mulatschak.game.DrawableObjects.CardStack;
import heroiceraser.mulatschak.game.DrawableObjects.DealerButton;
import heroiceraser.mulatschak.game.DrawableObjects.DiscardPile;
import heroiceraser.mulatschak.game.DrawableObjects.GameMenu;
import heroiceraser.mulatschak.game.DrawableObjects.GameStatistics;
import heroiceraser.mulatschak.game.DrawableObjects.GameTricks;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.Animations.GameAnimation;


/**
 * Created by Daniel Metzner on 10.08.2017.
 */

public class GameController{

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private GameView view_;
    private GameLayout layout_;
    private GameAnimation animations_;
    private TouchEvents touch_events_;
    private GameLogic logic_;

    private ButtonBar button_bar_;
    private GameStatistics statistics_;
    private GameTricks tricks_;
    private GameMenu menu_;

    private List<Player> player_list_;
    private MulatschakDeck deck_;
    private DiscardPile discardPile_;
    private CardStack trash_;
    private DealerButton dealer_button_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameController(GameView view, int players) {
        view_ = view;
        new InvalidateViewThread(view); // a new thread, which is updating the view

        logic_ = new GameLogic();
        layout_ = new GameLayout();
        animations_ = new GameAnimation(view);
        touch_events_ = new TouchEvents();

        button_bar_ = new ButtonBar();
        statistics_ = new GameStatistics();
        tricks_ = new GameTricks();
        menu_ = new GameMenu();


        deck_ = new MulatschakDeck();
        discardPile_ = new DiscardPile();
        player_list_ = new ArrayList<>();
        for (int i = 0; i < players; i++) {
            player_list_.add(new Player(i));
        }
        dealer_button_ = new DealerButton();
    }

    //----------------------------------------------------------------------------------------------
    //  start
    //
    public void start() {
        init();
        setPlayerLives();
        setPlayerPositions();
        chooseFirstDealerRandomly();
        startRound();
    }

    public void startRound() {
        shuffleDeck();
        resetTurn();
        dealCards();  // starts an dealing animation
    }

    public void continueAfterDealingAnimation() {
        resetTricksToMake();
        trickBids(true);        // first player to bid is the player next to the dealer
    }

    public void continueAfterTrickBids() {
        switch (checkHighestBid()) {
            case 0:
                startRound(); // start a new round if every player said 0 tricks
                break;
            case 1:
                //ToDo: start the round with heart as trumph
                break;
            default:
                letHighestBidderChooseTrump();
                break;
        }
    }

    public void continueAfterTrumpWasChoosen() {
        logic_.setTurn(logic_.getTrumphPlayerId());
        logic_.setLastTrickId(logic_.getTrumphPlayerId());
        nextCardRound(); // first call
    }



    //----------------------------------------------------------------------------------------------
    //  init:
    //         initialises the game
    //
    private void init() {
        layout_.init(view_);
        deck_.initDeck(view_);
        discardPile_.init(view_);
        dealer_button_.init(view_);
        animations_.init(view_);
        button_bar_.init(view_);
        statistics_.init(view_);
        tricks_.init(view_);
        menu_.init(view_);
    }

    //----------------------------------------------------------------------------------------------
    //  setPlayer Lives
    //
    private void setPlayerLives() {
        for (int i = 0; i < getAmountOfPlayers(); i++) {
            getPlayerById(i).setLives(logic_.getStartLives());
            getPlayerById(i).setTrumphsToMake(0);
        }
    }
    //----------------------------------------------------------------------------------------------
    //  setPlayer Lives
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
        int random_number = random_generator.nextInt(getAmountOfPlayers());
        logic_.setDealer(random_number);
        dealer_button_.setPosition(
                layout_.getDealerButtonPosition(getPlayerById(random_number).getPosition()));
        logic_.setTurn(random_number);

        // DEBUG ////////////////////////////////////////////////////////////////////////////////////////
        CharSequence text = "First Dealer is player: " + random_number;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(view_.getContext(), text, duration);
        toast.show();//////////////////////////////////////////////////////////////////////////////////
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
        for (int hand_card = 0; hand_card < logic_.getMaxCardsPerHand(); hand_card++) {
            for (int player_id = 0; player_id < players; player_id++) {
                drawCard(player_id, deck_);
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    //  drawCard
    //
    private void drawCard(int player_id, MulatschakDeck deck){
        if (deck.getCardStack().isEmpty()) {
            // ToDo do something when deck is empty
        }
        else
        {
            CardStack player_hand =  getPlayerById(player_id).getHand();
            player_hand.addCard(deck_.getCardAt(0));
            deck.getCardStack().remove(0);
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
            getPlayerById(i).setTrumphsToMake(0);
        }
        logic_.setTrumphsToMake(0);
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
    public void trickBids() {
        trickBids(false);
    }

    public void trickBids(boolean first_call) {

        turnToNextPlayer();

        if (!first_call && logic_.getTurn() == logic_.getFirstBidder(getAmountOfPlayers())) {
            continueAfterTrickBids();
            return;         // stops the recursion after all players made their bids
        }

        if (logic_.getTurn() == 0) {
            animations_.getStichAnsage().setAnimationNumbers(true);
            // trickBids should get called when player chooses his tricks
        }
        else if (logic_.getTurn() != 0) {
            EnemyLogic el = new EnemyLogic();
            el.trickBids(getPlayerById(logic_.getTurn()), this);
            trickBids();
        }
    }

    //----------------------------------------------------------------------------------------------
    //  cada
    //
    private int checkHighestBid() {
        int highest_bid = 0;
        for (int id = 0; id < getAmountOfPlayers(); id++) {
            if (getPlayerById(id).getTrumphsToMake() > highest_bid) {
                highest_bid = getPlayerById(id).getTrumphsToMake();
            }
        }
        return highest_bid;
    }

    //----------------------------------------------------------------------------------------------
    //  cada
    //
    public void setNewMaxTrumphs(int amount, int id) {
        CharSequence text = "player: " + id + " tries ";

        if (amount > logic_.getTrumphsToMake()) {
            List<Button> buttons = getAnimation().getStichAnsage().getNumberButtons();
            for (int i = 1; i <= amount; i++) {
                buttons.get(i).setEnabled(false);
            }
            getPlayerById(id).setTrumphsToMake(amount);
            logic_.setTrumphsToMake(amount);
            logic_.setTrumphPlayerId(id);
            text = text + Integer.toString(amount);
        }
        else {
            getPlayerById(id).setTrumphsToMake(0);
            text = text + "0";
        }
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(view_.getContext(), text, duration);
        toast.show();

    }

    //----------------------------------------------------------------------------------------------
    //  cada
    //
    private void letHighestBidderChooseTrump() {
        if (logic_.getTrumphPlayerId() == 0) {
            animations_.getStichAnsage().setAnimationSymbols(true);
            // touch event should call continueAfterTrumpWasChoosen();
        }
        else if (logic_.getTrumphPlayerId() != 0) {
            EnemyLogic el = new EnemyLogic();
            el.chooseTrumph(getPlayerById(logic_.getTrumphPlayerId()), logic_, view_);
            continueAfterTrumpWasChoosen();
        }
    }

    public void nextTurn() {
        nextTurn(false);
    }

    private void nextCardRound() {

        if (getPlayerById(logic_.getTurn()).getAmountOfCardsInHand() == 0) {
            return;
        }
        nextTurn(true);
    }

    public boolean waiting = false;
    public void endCardRound() {

        if (!waiting) {
            // ToDo chooseCardRoundWinner();
            // ToDO moveDiscardPileToWinner();
            clearDiscardPile();
            nextCardRound();
        }
    }

    private void clearDiscardPile() {
        discardPile_.setCardBottom(null);
        discardPile_.setCardLeft(null);
        discardPile_.setCardTop(null);
        discardPile_.setCardRight(null);
    }

    private void nextTurn(boolean first_call) {

        animations_.getCardAnimations().setCardMoveable(false);

        if (!first_call && logic_.getTurn() == logic_.getLastTrickId()) {
            waiting = true;
            endCardRound();
            return;
        }

        if (logic_.getTurn() == 0) {
            animations_.getCardAnimations().setCardMoveable(true);
            // touch event calls next turnToNextPlayer & next turn
        }
        else if (logic_.getTurn() != 0) {
            EnemyLogic el = new EnemyLogic();
            el.playCard(logic_, getPlayerById(logic_.getTurn()), discardPile_);
            getLogic().turnToNextPlayer(getAmountOfPlayers());
            nextTurn();
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
}
