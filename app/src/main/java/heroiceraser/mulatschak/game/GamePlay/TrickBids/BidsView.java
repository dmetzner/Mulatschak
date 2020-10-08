package heroiceraser.mulatschak.game.GamePlay.TrickBids;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;

import androidx.core.graphics.ColorUtils;

import java.util.Arrays;

import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.drawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;


//--------------------------------------------------------------------------------------------------
//  ToDo describe me    DO only needed for visible^^
//
public class BidsView extends DrawableObject {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private BidsField[] bids_field_list_;               // container for all bid buttons

    // - Animation -
    //   -> starting animation is handled in each bid field for it's own
    //   -> ending animation
    private boolean ending_animation_;
    private int winner_id_;           // needed to know which bid field stays visible for the round
    private long start_time_;
    private int color1;
    private int color2;
    private int color3;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    BidsView() {
        super();
        bids_field_list_ = new BidsField[4];
        bids_field_list_[0] = null;
        bids_field_list_[1] = null;
        bids_field_list_[2] = null;
        bids_field_list_[3] = null;
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //          -> draws all bid fields
    //          -> keeps ending animation running if running
    //
    public synchronized void draw(Canvas canvas, GameController controller) {
        if (!isVisible()) {
            return;
        }
        // draws all bid fields
        int pos = 0;
        for (BidsField bf : bids_field_list_) {
            if (bf != null) {
                bf.draw(canvas);
                if (bf.isAnimationRunning()) {
                    continueStartAnimation(controller, pos);
                }
            }
            pos++;
        }
        // keep animation running
        if (ending_animation_) {
            continueEndingAnimation(controller);
        }
    }


    //----------------------------------------------------------------------------------------------
    // startAnimation
    //
    public void startAnimation(GameController controller, int pos, String text) {
        GameLayout layout = controller.getLayout();
        BidsField bidsField = new BidsField();
        Point start_pos = new Point(layout.getPlayerInfoPositions().get(pos));
        Point final_pos = new Point(layout.getDiscardPilePositions().get(pos));
        start_pos.x += layout.getCardWidth() / 2;
        start_pos.y += layout.getCardHeight() / 2;      // offsets to center 'em
        final_pos.x += layout.getCardWidth() / 2;
        final_pos.y += layout.getCardHeight() / 2;
        bidsField.init(controller.getView(), text, start_pos);
        bidsField.startAnimation(controller, start_pos, final_pos);
        start_time_ = System.currentTimeMillis();
        bids_field_list_[pos] = bidsField;
        color1 = controller.getView().getResources().getColor(R.color.metallic_blue);
        color2 = Color.GRAY;
        color3 = Color.RED;
    }


    //----------------------------------------------------------------------------------------------
    //  continueAnimation
    //                          -> gets called till animation is done
    //                          -> winner bid field moves to end position
    //                          -> other field fade out (alpha reduction)
    //                          -> all field get their size reduced
    //
    private void continueStartAnimation(GameController controller, int pos) {
        double speed_factor = controller.getSettings().getAnimationSpeed().getSpeedFactor();
        double max_time = 1000 * speed_factor;
        long time = System.currentTimeMillis();
        long time_since_start = time - start_time_;

        double percentage = time_since_start / max_time;

        if (percentage > 1) {
            percentage = 1;
        }

        bids_field_list_[pos].changeSizeBasedOnPercentage(percentage);

        bids_field_list_[pos].moveToFinalPositionBasedOnPercentage(percentage);

        if (percentage >= 1) {
            bids_field_list_[pos].setAnimationRunning(false);
            controller.getGamePlay().getTrickBids().makeTrickBids(false, controller);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  startEndingAnimation
    //                          -> wait half a second
    //                          -> starts ending animation and saves start time
    //                          -> sets the end position for the winner bid field
    //
    void startEndingAnimation(final int winner_pos, GameLayout layout) {
        final GameLayout layoutF = layout;
        Handler handler = new Handler();

        Runnable moveOnlyWinnerRunnable = new Runnable() {
            @Override
            public void run() {
                ending_animation_ = true;
                winner_id_ = winner_pos;
                if (winner_pos >= 0 && winner_pos < bids_field_list_.length) {
                    Point end_position = layoutF.getTrickBidsGamePlayPositions().get(winner_pos);
                    Point offset = new Point(end_position);
                    offset.x -= bids_field_list_[winner_pos].getPosition().x;
                    offset.y -= bids_field_list_[winner_pos].getPosition().y;
                    bids_field_list_[winner_pos].setOffset(offset);
                    bids_field_list_[winner_pos].setStartPos(
                            bids_field_list_[winner_pos].getPosition());
                }
                start_time_ = System.currentTimeMillis();
            }
        };

        Runnable moveEmAllRunnable = new Runnable() {
            @Override
            public void run() {
                ending_animation_ = true;
                winner_id_ = winner_pos;
                for (int i = 0; i < bids_field_list_.length; i++) {
                    if (bids_field_list_[i] == null) {
                        continue;
                    }
                    Point end_position = layoutF.getTrickBidsGamePlayPositions().get(i);
                    Point offset = new Point(end_position);
                    offset.x -= bids_field_list_[i].getPosition().x;
                    offset.y -= bids_field_list_[i].getPosition().y;
                    bids_field_list_[i].setOffset(offset);
                    bids_field_list_[i].setStartPos(
                            bids_field_list_[i].getPosition());
                }
                start_time_ = System.currentTimeMillis();
            }
        };
        handler.postDelayed(moveEmAllRunnable, 1000);

    }


    //----------------------------------------------------------------------------------------------
    //  continueEndingAnimation
    //                          -> gets called till animation is done
    //                          -> winner bid field moves to end position
    //                          -> other field fade out (alpha reduction)
    //                          -> all field get their size reduced
    //
    private void continueEndingAnimation(GameController controller) {
        double speed_factor = controller.getSettings().getAnimationSpeed().getSpeedFactor();
        double max_time = 1000 * speed_factor;
        long time = System.currentTimeMillis();
        long time_since_start = time - start_time_;

        double percentage = time_since_start / max_time;
        if (percentage > 1) {
            percentage = 1;
        }

        for (int i = 0; i < bids_field_list_.length; i++) {
            // move winner bid field in end pos direction
            if (bids_field_list_[i] == null) {
                continue;
            }

            bids_field_list_[i].setPosition(
                    (int) (bids_field_list_[i].getStartPos().x +
                            bids_field_list_[i].getOffset().x * percentage),
                    (int) (bids_field_list_[i].getStartPos().y +
                            bids_field_list_[i].getOffset().y * percentage));


            if (i != winner_id_) {

                int color = color2;
                if (controller.getPlayerByPosition(i).getMissATurn()) {
                    color = color3;
                }
                color = interpolateColorInCIEL(color1, color, (float) percentage);

                Paint p = bids_field_list_[i].getCirclePaintDefault();
                p.setColor(color);
                bids_field_list_[i].setCirclePaintDefault(p);
            }

            // reduce size
            double size_percentage = 1 - percentage;
            bids_field_list_[i].changeSizeBasedOnPercentage(size_percentage);
        }

        if (percentage >= 1) {
            ending_animation_ = false;
            controller.continueAfterTrickBids();
        }
    }

    private double interpolate(double a, double b, double percentage) {
        return (a + ((b - a) * percentage));
    }


    private int interpolateColorInCIEL(int a, int b, double percentage) {
        double[] ciel1 = new double[3];
        double[] ciel2 = new double[3];
        ColorUtils.colorToLAB(a, ciel1);
        ColorUtils.colorToLAB(b, ciel2);
        for (int i = 0; i < 3; i++) {
            ciel2[i] = interpolate(ciel1[i], ciel2[i], percentage);
        }
        return ColorUtils.LABToColor(ciel2[0], ciel2[1], ciel2[2]);
    }


    public void reset() {
        Arrays.fill(bids_field_list_, null);
        winner_id_ = GameController.NOT_SET;
        ending_animation_ = false;
        setVisible(false);
    }
}
