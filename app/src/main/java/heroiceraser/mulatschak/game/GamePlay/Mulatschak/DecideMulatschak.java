package heroiceraser.mulatschak.game.GamePlay.Mulatschak;

import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import heroiceraser.mulatschak.Message;
import heroiceraser.mulatschak.MainActivity;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GamePlay.TrickBids.MakeBidsAnimation;
import heroiceraser.mulatschak.game.GameView;

//--------------------------------------------------------------------------------------------------
//  Say Mulatschak Class
//
public class DecideMulatschak {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private EnemyDecideMulatschakLogic enemyDecideMulatschakLogic;
    private DecideMulatschakAnimation decideMulatschakAnimation;
    private MulatschakActivateAnimation mulatschakRoundAnimation;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public DecideMulatschak() {
        enemyDecideMulatschakLogic = new EnemyDecideMulatschakLogic();
        decideMulatschakAnimation = new DecideMulatschakAnimation();
        mulatschakRoundAnimation = new MulatschakActivateAnimation();
    }


    //----------------------------------------------------------------------------------------------
    //  Init
    //
    public void init(GameView view) {
        decideMulatschakAnimation.init(view);
    }

    //----------------------------------------------------------------------------------------------
    //  startMulatschakDecision
    //
    public void startMulatschakDecision(GameController controller) {
        makeMulatschakDecision(true, controller); // not first call
    }

    //----------------------------------------------------------------------------------------------
    //  makeMulatschakDecision
    //                          -> gets recursive called for all players
    //                          -> start a Muli Decision Animation for Player 0
    //                          -> Enemy Logic decides for enemies
    //
    void makeMulatschakDecision(boolean first_call, final GameController controller) {
        GameLogic logic = controller.getLogic();

        if (!first_call) {
            controller.getPlayerById(logic.getTurn()).gameState = Message.gameStateWaitForTrickBids;
        }

        // if someone tries a Mulatschak skip choose Tricks
        if (logic.getTricksToMake() == MakeBidsAnimation.MULATSCHAK) {
            controller.continueAfterTrickBids();
            return;
        }

        // player next to the dealer starts
        controller.turnToNextPlayer(false);

        // all players had their chance to try a mulatschak  --> return
        if (!first_call && logic.getTurn() == logic.getFirstBidder(controller.getAmountOfPlayers())) {
            logic.setTurn(logic.getDealer());
            controller.continueAfterDecideMulatschak();
            return;
        }

        // Player 0 -> Choose Animation Muli, (Yes or NO?)
        if (logic.getTurn() == 0) {
            decideMulatschakAnimation.turnOnAnimation();
            //controller.getView().disableUpdateCanvasThread();
            //animation calls make makeMul again
        }

        // Enemies
        else if (logic.getTurn() != 0) {
            //controller.getView().enableUpdateCanvasThread();

            // single player
            if (controller.getPlayerById(logic.getTurn()).isEnemyLogic()) {
                handleEnemyAction(controller);
            }
            // multiplayer
            else {
                controller.waitForOnlineInteraction = Message.mulatschakDecision;
                Gson gson = new Gson();
                String oId = controller.getPlayerById(logic.getTurn()).getOnlineId();
                if (controller.DEBUG) { Log.d("-------", "wait for " +
                        controller.getPlayerById(controller.getLogic().getTurn()).getDisplayName()
                        + " muli decision"); }
                controller.requestMissedMessagePlayerCheck(controller.fillGameStates(),
                        controller.getPlayerById(controller.getLogic().getTurn()).getOnlineId(),
                        controller.mainActivity.gameState, Message.requestMulatschakDecision, oId);
            }
        }
    }

    public void handleEnemyAction(final GameController controller) {
        if (enemyDecideMulatschakLogic.decideMulatschak(controller)) {
            setMulatschakUp(controller);
            return;
        }
        // simulate thinking
        Handler myHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                makeMulatschakDecision(false, controller);
            }
        };
        double animation_factor =  controller.getSettings().getAnimationSpeed().getSpeedFactor();
        myHandler.postDelayed(runnable, (int) (500 * animation_factor));
    }

    public void handleOnlineInteraction(boolean muli, GameController controller) {
        controller.waitForOnlineInteraction = Message.noMessage;
        if (muli) {
            setMulatschakUp(controller);
        }
        else {
            makeMulatschakDecision(false, controller);
        }
    }

    void handleMainPlayersDecision(boolean muli, GameController controller) {

        if (controller.multiplayer_) {
            // broadcast to all the decision
            MainActivity activity = (MainActivity) controller.getView().getContext();
            Gson gson = new Gson();
            activity.broadcastMessage(Message.mulatschakDecision, gson.toJson(muli));
        }

        if (controller.DEBUG) { Log.d("-------", "I made my Muli decision"); }

        if (muli) {
            controller.getGamePlay().getDecideMulatschak().setMulatschakUp(controller);
            // makeMulatschakDecision(false, controller) gets called after animation
        }
        else {
            controller.getGamePlay().getDecideMulatschak().makeMulatschakDecision(false, controller);
        }
    }

    //----------------------------------------------------------------------------------------------
    //  startRound
    //
    public void startRound() {
        mulatschakRoundAnimation.clear();
    }

    //----------------------------------------------------------------------------------------------
    //  setMulatschakUp
    //
    private void setMulatschakUp(GameController controller) {
        GameLogic logic = controller.getLogic();
        logic.setMulatschakRound(true);
        controller.getPlayerById(logic.getTurn()).setTricksToMake(MakeBidsAnimation.MULATSCHAK);
        logic.setTricksToMake(MakeBidsAnimation.MULATSCHAK);
        logic.setTrumpPlayerId(logic.getTurn());
        logic.setTurn(logic.getDealer());
        mulatschakRoundAnimation.setUp(controller);
        // makeMulatschakDecision(false, controller) gets called after animation
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public synchronized void draw(Canvas canvas, GameController controller) {
        decideMulatschakAnimation.draw(canvas, controller);
        mulatschakRoundAnimation.draw(canvas, controller);
    }



    //----------------------------------------------------------------------------------------------
    //  Touch Events
    //
    public synchronized void touchEventDown(int X, int Y) {
        decideMulatschakAnimation.touchEventDown(X, Y);
    }

    synchronized public void touchEventMove(int X, int Y) {
        decideMulatschakAnimation.touchEventMove(X, Y);
    }

    synchronized public void touchEventUp(int X, int Y, GameController controller) {
        decideMulatschakAnimation.touchEventUp(X, Y, controller);
    }
}
