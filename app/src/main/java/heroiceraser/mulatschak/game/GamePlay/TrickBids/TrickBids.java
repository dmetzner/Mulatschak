package heroiceraser.mulatschak.game.GamePlay.TrickBids;

import android.util.Log;

import java.util.List;

import heroiceraser.mulatschak.drawableBasicObjects.MyButton;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GameView;

import static heroiceraser.mulatschak.game.GamePlay.TrickBids.MakeBidsAnimation.MISS_A_TURN;


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
            int winnerId = controller.getPlayerById(logic.getTrumpPlayerId()).getPosition();
            bids_view_.startEndingAnimation(winnerId, layout);
            return;         // mulatschak stops the bidding
        }

        if (!first_call && logic.getTurn() == logic.getFirstBidder(controller.getAmountOfPlayers())) {
            int winnerPos = controller.getPlayerById(logic.getTrumpPlayerId()).getPosition();
            bids_view_.startEndingAnimation(winnerPos, layout);
            return;         // stops the recursion after all players made their bids
        }

        if (logic.getTurn() == 0) {
            makeBidsAnimation.prepareAnimationButtons(controller);
            makeBidsAnimation.turnOnAnimationNumbers();
            //controller.getView().disableUpdateCanvasThread();
            // makeTrickBids should get called when player chooses his tricks
        } else if (logic.getTurn() != 0) {
            //controller.getView().enableUpdateCanvasThread();

            // single player
            if (controller.getPlayerById(logic.getTurn()).isEnemyLogic()) {
                handleEnemyAction(controller);
            }
        }
    }

    private void handleEnemyAction(final GameController controller) {
        enemyMakeBidsLogic.makeTrickBids(
                controller.getPlayerById(controller.getLogic().getTurn()), controller);
    }


    void handleMainPlayersDecision(int buttonId, GameController controller) {

        if (controller.DEBUG) {
            Log.d("-------", "I made my trick bids");
        }

        setTricks(controller, buttonId);
    }

    //----------------------------------------------------------------------------------------------
    //  setTricks
    //                  -> called by touch events, animating numbers
    //                  -> ends choose bids animation
    //                  -> sets Bids
    //
    private void setTricks(GameController controller, int buttonId) {
        buttonId--;  // because of miss a turn button

        //---- don't play this round
        if (buttonId == MISS_A_TURN) {
            controller.getPlayerById(0).setMissATurn(true);
            makeBidsAnimation.clearHand(controller, 0);
            controller.getGamePlay().getTrickBids().setNewMaxTrumps(MISS_A_TURN, 0, controller);
            return;
        }

        //---- play this round
        controller.getPlayerById(0).setMissATurn(false);
        makeBidsAnimation.getNumberButtons().get(0).setEnabled(true); // played this round -> can skip the next one
        controller.getGamePlay().getTrickBids().setNewMaxTrumps(buttonId, 0, controller);
    }


    //----------------------------------------------------------------------------------------------
    //  setNewMaxTrumps
    //
    void setNewMaxTrumps(int amount, int id, GameController controller) {
        GameLogic logic = controller.getLogic();

        int playerPos = controller.getPlayerById(logic.getTurn()).getPosition();

        if (amount == MakeBidsAnimation.MULATSCHAK) {
            logic.setMulatschakRound(true);
            logic.setTrumpPlayerId(id);
            logic.setTricksToMake(MakeBidsAnimation.MULATSCHAK);
            controller.getPlayerById(id).setTricksToMake(MakeBidsAnimation.MULATSCHAK);
            bids_view_.startAnimation(controller, playerPos, "M");
        } else if (amount == MISS_A_TURN) {
            controller.getPlayerById(id).setTricksToMake(amount);
            bids_view_.startAnimation(controller, playerPos, "X");
        }

        // amount have to be bigger, except dealers turn
        else if (amount > logic.getTricksToMake() ||
                amount == logic.getTricksToMake() && logic.getTurn() == logic.getDealer()) {
            List<MyButton> buttons = makeBidsAnimation.getNumberButtons();
            // disable lower amount buttons, but button 0 is always clickable // miss a turn
            for (int i = 2; i <= (amount + 1) && i < buttons.size(); i++) {
                buttons.get(i).setEnabled(false);
            }
            controller.getPlayerById(id).setTricksToMake(amount);
            logic.setTricksToMake(amount);
            logic.setTrumpPlayerId(id);
            bids_view_.startAnimation(controller, playerPos, "" + amount);
        } else {
            controller.getPlayerById(id).setTricksToMake(0);
            bids_view_.startAnimation(controller, playerPos, "0");
        }

        // there have to be at least two active players every round!
        int amount_of_playing_players = controller.getAmountOfPlayers();
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
