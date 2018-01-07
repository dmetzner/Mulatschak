package heroiceraser.mulatschak.game.GamePlay.TrickBids.DecideMulatschak;

import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;
import heroiceraser.mulatschak.game.DrawableObjects.Card;
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
    private MulatschakAnimation mulatschakRound;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public DecideMulatschak() {
        enemyDecideMulatschakLogic = new EnemyDecideMulatschakLogic();
        decideMulatschakAnimation = new DecideMulatschakAnimation();
        mulatschakRound = new MulatschakAnimation();
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
        controller.turnToNextPlayer();

        // all players had their chance to try a mulatschak  --> return
        if (!first_call && logic.getTurn() == logic.getFirstBidder(controller.getAmountOfPlayers())) {
            logic.setTurn(logic.getDealer());
            controller.continueAfterDecideMulatschak();
            return;
        }

        // Player 0 -> Choose Animation Muli, (Yes or NO?)
        if (logic.getTurn() == 0) {
            decideMulatschakAnimation.turnOnAnimation();
            controller.getView().disableUpdateCanvasThread();
            //animation calls make makeMul again
        }

        // Enemies
        else if (logic.getTurn() != 0) {
            controller.getView().enableUpdateCanvasThread();
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
    }


    //----------------------------------------------------------------------------------------------
    //  setMulatschakUp
    //
    void setMulatschakUp(GameController controller) {
        GameLogic logic = controller.getLogic();

        for (Card card : controller.getPlayerById(logic.getTurn()).getHand().getCardStack()) {
            Log.d("MULATSCHAK:", card.getId() + "");
        }

        logic.setMulatschakRound(true);
        controller.getPlayerById(logic.getTurn()).setTricksToMake(MakeBidsAnimation.MULATSCHAK);
        logic.setTricksToMake(MakeBidsAnimation.MULATSCHAK);
        logic.setTrumpPlayerId(logic.getTurn());
        logic.setTurn(logic.getDealer());
        mulatschakRound.setUp(controller);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas, GameController controller) {
        decideMulatschakAnimation.draw(canvas, controller);
        mulatschakRound.draw(canvas, controller);
    }


    //----------------------------------------------------------------------------------------------
    //  Touch Events
    //
    public void touchEventDown(int X, int Y) {
        decideMulatschakAnimation.touchEventDown(X, Y);
    }

    public void touchEventMove(int X, int Y) {
        decideMulatschakAnimation.touchEventMove(X, Y);
    }

    public void touchEventUp(int X, int Y, GameController controller) {
        decideMulatschakAnimation.touchEventUp(X, Y, controller);
    }
}
