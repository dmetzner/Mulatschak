package heroiceraser.mulatschak.game.GamePlay.Mulatschak;

import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;

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

        if (logic.getTurn() == 0) {
            // Player 0 -> Choose Animation Muli, (Yes or NO?)
            decideMulatschakAnimation.turnOnAnimation();
            // animation calls make `makeMulatschakDecision` again
        } else {
            handleEnemyAction(controller);
        }
    }

    private void handleEnemyAction(final GameController controller) {
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
        double animation_factor = controller.getSettings().getAnimationSpeed().getSpeedFactor();
        myHandler.postDelayed(runnable, (int) (500 * animation_factor));
    }

    void handleMainPlayersDecision(boolean muli, GameController controller) {

        if (controller.DEBUG) {
            Log.d("-------", "I made my Muli decision");
        }

        if (muli) {
            controller.getGamePlay().getDecideMulatschak().setMulatschakUp(controller);
            // makeMulatschakDecision(false, controller) gets called after animation
        } else {
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
