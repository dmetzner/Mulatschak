package heroiceraser.mulatschak.game.GamePlay.TrickBids;

import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;


//----------------------------------------------------------------------------------------------
//  Trick Bids Class
//                      ToDo explain me ;) +
//
public class TrickBids {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private MakeBidsAnimation makeBidsAnimation;

    private EnemyMakeBidsLogic enemyMakeBidsLogic;

    private BidsView bids_view_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public TrickBids() {
        makeBidsAnimation = new MakeBidsAnimation();
        enemyMakeBidsLogic = new EnemyMakeBidsLogic();
        bids_view_ = new BidsView();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        makeBidsAnimation.init(view);
        //  enemyMakeBidsLogic    NO init needed!
        //  bids_view_.init(view);
    }


    //----------------------------------------------------------------------------------------------
    //  startRound
    //
    public void startRound(GameController controller) {
        makeBidsAnimation.reEnableButtons(controller);
        bids_view_.reset();
        bids_view_.setVisible(false);
    }


    //----------------------------------------------------------------------------------------------
    //  startTrickBids
    //                  -> first makeTrickBids call, bids_view get visible
    //
    public void startTrickBids(GameController controller) {
        bids_view_.setVisible(true);
        makeTrickBids(true, controller); // not first call
    }


    //----------------------------------------------------------------------------------------------
    //  makeTrickBids
    //
    void makeTrickBids(boolean first_call, GameController controller) {
        GameLogic logic = controller.getLogic();
        GameLayout layout = controller.getLayout();

        controller.turnToNextPlayer(false);

        if (logic.getTricksToMake() == MakeBidsAnimation.MULATSCHAK) {
            bids_view_.startEndingAnimation(logic.getTrumpPlayerId(), layout);
            return;
        }

        if (!first_call && logic.getTurn() == logic.getFirstBidder(controller.getAmountOfPlayers())) {
            bids_view_.startEndingAnimation(logic.getTrumpPlayerId(), layout);
            return;         // stops the recursion after all players made their bids
        }

        if (logic.getTurn() == 0) {
            makeBidsAnimation.prepareAnimationButtons(controller);
            makeBidsAnimation.turnOnAnimationNumbers();
            controller.getView().disableUpdateCanvasThread();
            // makeTrickBids should get called when player chooses his tricks
        }
        else if (logic.getTurn() != 0) {
            controller.getView().enableUpdateCanvasThread();
            enemyMakeBidsLogic.makeTrickBids(controller.getPlayerById(logic.getTurn()), controller);
            // makeTrickBids should get called after animation
        }
    }


    //----------------------------------------------------------------------------------------------
    //  setNewMaxTrumps
    //
    void setNewMaxTrumps(int amount, int id, GameController controller) {
        GameLogic logic = controller.getLogic();

        if (amount == MakeBidsAnimation.MULATSCHAK) {
            logic.setMulatschakRound(true);
            logic.setTrumpPlayerId(id);
            logic.setTricksToMake(MakeBidsAnimation.MULATSCHAK);
            controller.getPlayerById(id).setTricksToMake(MakeBidsAnimation.MULATSCHAK);
            bids_view_.startAnimation(controller, logic.getTurn(), "M");
        }

        else if (amount == MakeBidsAnimation.MISS_A_TURN) {
            controller.getPlayerById(id).setTricksToMake(amount);
            bids_view_.startAnimation(controller, logic.getTurn(), "X");
        }

        // amount have to be bigger, except dealers turn
        else if (amount > logic.getTricksToMake() ||
                amount == logic.getTricksToMake() && logic.getTurn() == logic.getDealer()) {
            List<MyButton> buttons = makeBidsAnimation.getNumberButtons();
            // disable lower amount buttons, but button 0 is always clickable // miss a turn
            for (int i = 2; i <= (amount + 1); i++) {
                buttons.get(i).setEnabled(false);
            }
            controller.getPlayerById(id).setTricksToMake(amount);
            logic.setTricksToMake(amount);
            logic.setTrumpPlayerId(id);
            bids_view_.startAnimation(controller, logic.getTurn(), "" + amount);
        }

        else {
            controller.getPlayerById(id).setTricksToMake(0);
            bids_view_.startAnimation(controller, logic.getTurn(), "-");
        }

        // there have to be at least two active players every round!
        int amount_of_playing_players =  controller.getAmountOfPlayers();
        for (int i = 0; i < controller.getAmountOfPlayers(); i++) {
            if (controller.getPlayerById(i).getMissATurn()) {
                amount_of_playing_players--;
            }
        }
        if (amount_of_playing_players <= 2) {
            List<MyButton> buttons = makeBidsAnimation.getNumberButtons();
            buttons.get(0).setEnabled(false);
        }
    }

    //----------------------------------------------------------------------------------------------
    //  getHighestBid
    //                  -> return the highest bid of all players
    //
    public int getHighestBid(GameController controller) {
        int highest_bid = 0;
        for (MyPlayer player : controller.getPlayerList()) {
            if (player.getTricksToMake() > highest_bid) {
                highest_bid = player.getTricksToMake();
            }
        }
        return highest_bid;
    }


    //----------------------------------------------------------------------------------------------
    //  Getter
    //
    public MakeBidsAnimation getMakeBidsAnimation() {
        return makeBidsAnimation;
    }

    public BidsView getBidsView() {
        return bids_view_;
    }
}
