package heroiceraser.mulatschak.game.GamePlay.Mulatschak;


import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;

import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GamePlay.TrickBids.BidsField;


//--------------------------------------------------------------------------------------------------
//  Mulatschak Round
//
public class MulatschakActivateAnimation {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private boolean active;
    private BidsField bidsField;
    private boolean startAnimation;
    private boolean endAnimation;
    private long startTime;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    MulatschakActivateAnimation() {
        bidsField = null;
        startAnimation = false;
        endAnimation = false;
    }


    public void setUp(GameController controller) {
        GameLayout layout = controller.getLayout();
        bidsField = new BidsField();
        Point startPos = new Point(layout.getPlayerInfoPositions()
                .get(controller.getPlayerById(
                        controller.getLogic().getTrumpPlayerId()).getPosition()));
        startPos.x += layout.getCardWidth() / 2;
        startPos.y += layout.getCardHeight() / 2;      // offsets to center 'em
        Point final_pos = new Point(layout.getDiscardPilePositions().get(0).x + layout.getCardWidth() / 2,
                layout.getDiscardPilePositions().get(0).y);
        bidsField.init(controller.getView(), "M", startPos);
        bidsField.startAnimation(controller, startPos, final_pos);
        active = true;
        startAnimation = true;
        startTime = System.currentTimeMillis();
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public synchronized void draw(Canvas canvas, GameController controller) {
        if (active) {
            bidsField.draw(canvas);
            if (startAnimation || endAnimation) {
                continueStartAnimation(controller);
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    //  continueStartAnimation
    //                          -> gets called till animation is done
    //                          -> winner bid field moves to end position
    //                          -> other field fade out (alpha reduction)
    //                          -> all field get their size reduced
    //
    private void continueStartAnimation(GameController controller) {
        double speed_factor = controller.getSettings().getAnimationSpeed().getSpeedFactor();
        double max_time = 1000 * speed_factor;
        long time = System.currentTimeMillis();
        long time_since_start = time - startTime;

        double percentage = time_since_start / max_time;

        if (percentage > 1) {
            percentage = 1;
        }

        // just reverse if ending animation (moves from end to start pos)
        if (endAnimation) {
            percentage = 1 - percentage;
        }

        bidsField.changeSizeBasedOnPercentage(percentage);
        bidsField.moveToFinalPositionBasedOnPercentage(percentage);

        if (endAnimation) {
            percentage = 1 - percentage;
        }

        if (percentage >= 1) {
            if (startAnimation) {
                bidsField.setStartPos(
                        controller.getLayout().getTrickBidsGamePlayPositions().get(
                                controller.getPlayerById(controller.getLogic().getTrumpPlayerId())
                                        .getPosition()));
                Point offset = new Point(bidsField.getPosition().x - bidsField.getStartPos().x,
                        bidsField.getPosition().y - bidsField.getStartPos().y);
                bidsField.setOffset(offset);
                startAnimation = false;
                Handler myHandler = new Handler();
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        startTime = System.currentTimeMillis();
                        endAnimation = true;
                    }
                };
                myHandler.postDelayed(myRunnable, (int) (500 * speed_factor));
            } else {
                endAnimation = false;
                bidsField.setAnimationRunning(false);
                controller.getGamePlay().getDecideMulatschak().makeMulatschakDecision(false, controller);
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    //  clear
    //
    public void clear() {
        bidsField = null;
        active = false;
        startAnimation = false;
        endAnimation = false;
    }
}
