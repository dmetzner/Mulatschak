package heroiceraser.mulatschak.game.GamePlay.TrickBids;

import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import heroiceraser.mulatschak.Message;
import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.MainActivity;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;

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

        if (!first_call) {
            controller.getPlayerById(logic.getTurn()).gameState = Message.gameStateWaitForChooseTrump;
        }

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
        }
        else if (logic.getTurn() != 0) {
            //controller.getView().enableUpdateCanvasThread();

            // single player
            if (controller.getPlayerById(logic.getTurn()).isEnemyLogic()) {
                handleEnemyAction(controller);
            }

            // multiplayer
            else {
                controller.waitForOnlineInteraction = Message.trickBids;
                Gson gson = new Gson();
                String oId = controller.getPlayerById(logic.getTurn()).getOnlineId();
                if (controller.DEBUG) { Log.d("-------", "wait for " +
                        controller.getPlayerById(controller.getLogic().getTurn()).getDisplayName()
                        + " to make trick bids"); }
                controller.requestMissedMessagePlayerCheck(controller.fillGameStates(),
                        controller.getPlayerById(controller.getLogic().getTurn()).getOnlineId(),
                        controller.mainActivity.gameState, Message.requestTrickBids, oId);
                // wait 4 online interaction
            }
        }
    }

    public void handleEnemyAction(final GameController controller) {
        enemyMakeBidsLogic.makeTrickBids(
                controller.getPlayerById(controller.getLogic().getTurn()), controller);
    }


    public void handleOnlineInteraction(int buttonId, GameController controller) {
        controller.waitForOnlineInteraction = Message.noMessage;
        setTricks(controller, buttonId, controller.getLogic().getTurn());
    }

    void handleMainPlayersDecision(int buttonId, GameController controller) {

        if (controller.multiplayer_) {
            // broadcast to all the decision
            MainActivity activity = (MainActivity) controller.getView().getContext();
            Gson gson = new Gson();
            activity.broadcastMessage(Message.trickBids, gson.toJson(buttonId));
        }
        if (controller.DEBUG) { Log.d("-------", "I made my trick bids"); }

        setTricks(controller, buttonId, 0);
    }

    //----------------------------------------------------------------------------------------------
    //  setTricks
    //                  -> called by touch events, animating numbers
    //                  -> ends choose bids animation
    //                  -> sets Bids
    //
    private void setTricks(GameController controller, int buttonId, int playerId) {
        buttonId--;  // because of miss a turn button

        //---- don't play this round
        if (buttonId == MISS_A_TURN) {
            controller.getPlayerById(playerId).setMissATurn(true);
            makeBidsAnimation.clearHand(controller, playerId);
            controller.getGamePlay().getTrickBids().setNewMaxTrumps(MISS_A_TURN, playerId, controller);
            return;
        }

        //---- play this round
        controller.getPlayerById(playerId).setMissATurn(false);
        if (playerId == 0) {
            makeBidsAnimation.getNumberButtons().get(0).setEnabled(true); // played this round -> can skip the next one
        }
        controller.getGamePlay().getTrickBids().setNewMaxTrumps(buttonId, playerId, controller);
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
        }

        else if (amount == MISS_A_TURN) {
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
        }

        else {
            controller.getPlayerById(id).setTricksToMake(0);
            bids_view_.startAnimation(controller, playerPos, "0");
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
