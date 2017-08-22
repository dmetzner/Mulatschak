package heroiceraser.mulatschak.game;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

    private List<Player> player_list_;
    private MulatschakDeck deck_;
    private DiscardPile discardPile_;
    private CardStack trash_;

    private boolean playing = true;

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

        deck_ = new MulatschakDeck();
        discardPile_ = new DiscardPile();
        player_list_ = new ArrayList<Player>();
        for (int i = 0; i < players; i++) {
            player_list_.add(new Player(i));
        }
    }

    //----------------------------------------------------------------------------------------------
    //  start
    //
    public void start() {
        init();
        setPlayer();
        startRound();
    }

    //----------------------------------------------------------------------------------------------
    //  init:
    //         initialises the game
    //
    private void init() {
        layout_.calculateDimensions(view_);
        deck_.initDeck(view_);
        layout_.initDeckPosition(deck_);
        discardPile_.init(view_);
        layout_.initDiscardPilePositions(deck_.getCoordinate(), discardPile_);
        layout_.initHandPositions(deck_);
        animations_.init(view_);
    }

    //----------------------------------------------------------------------------------------------
    //  setPlayer Lives &
    //
    public void setPlayer() {
        for (int i = 0; i < getAmountOfPlayers(); i++) {
            getPlayerById(i).setLives(logic_.getStartLives());
            getPlayerById(i).setTrumphsToMake(0);
        }
    }

    //----------------------------------------------------------------------------------------------
    //  startRound
    //
    public void startRound() {
        Collections.shuffle(deck_.getStack(), new Random());
        chooseStartingPlayer();
        dealCards(getAmountOfPlayers());
        getAnimation().getDealingAnimation().start();
    }

    //----------------------------------------------------------------------------------------------
    //  cada
    //
    public void continueAfterDealingAnimation() {
        for (int i = 0; i < getAmountOfPlayers(); i++) {
            getPlayerById(i).setTrumphsToMake(-1);
        }
        sayStiche();
        // nextTurn();
    }

    public void sayStiche() {
        if (getPlayerById(logic_.getTurn()).getTrumphsToMake() != -1) {
            letMasterChoose();
            return;
        }
        if (logic_.getTurn() == 0) {
            animations_.getStichAnsage().setAnimationNumbers(true);
        }
        else if (logic_.getTurn() != 0) {
            EnemyLogic el = new EnemyLogic();
            el.sayStiche(getPlayerById(logic_.getTurn()), view_);
            logic_.turnToNextPlayer(getAmountOfPlayers());
            sayStiche();
        }
    }

    private void letMasterChoose() {
        int master = -1;     // who is master
        int max = 0;
        for (int i = 0; i < getAmountOfPlayers(); i++) {
            if (getPlayerById(i).getTrumphsToMake() > max) {
                master = i;
                max = getPlayerById(i).getTrumphsToMake();
            }
        }

        if (master == -1)
        {
            // ToDo new round
        }

        if (master == 0) {
            animations_.getStichAnsage().setAnimationSymbols(true);
        }
        else if (master != 0) {
            EnemyLogic el = new EnemyLogic();
            el.chooseTrumph(getPlayerById(master), logic_, view_);
            nextTurn();
        }
    }


    private void chooseStartingPlayer() {
        Random random_generator = new Random();
        int random_number = random_generator.nextInt(getAmountOfPlayers());
        logic_.setTurn(random_number);

        // DEBUG ////////////////////////////////////////////////////////////////////////////////////////
        CharSequence text = "starting turn: " + random_number;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(view_.getContext(), text, duration);
        toast.show();//////////////////////////////////////////////////////////////////////////////////
    }

    public void nextTurn() {

        if (logic_.getTurn() != 0) {
            EnemyLogic el = new EnemyLogic();
            el.playCard(getPlayerById(logic_.getTurn()), discardPile_);
            getLogic().turnToNextPlayer(getAmountOfPlayers());
            nextTurn();
        }

    }

    //----------------------------------------------------------------------------------------------
    //  dealCards
    //
    public void dealCards(int players) {
        for (int hand_card = 0; hand_card < logic_.getMaxCardsPerHand(); hand_card++) {
            for (int player_id = 0; player_id < players; player_id++) {
                drawCard(player_id, deck_);
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    //  drawCard
    //
    public void drawCard(int player_id, MulatschakDeck deck){
        if (deck.getStack().isEmpty()) {
////////////////////////////////////////////////////////////////////////////DO WHAT WHEN DECK EMPTY
        }
        else
        {
            CardStack player_hand =  getPlayerById(player_id).getHand();
            player_hand.addCard(deck_.getCardAt(0));
            deck.getStack().remove(0);
        }

    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
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


}
