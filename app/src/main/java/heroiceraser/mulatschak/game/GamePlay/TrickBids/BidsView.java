package heroiceraser.mulatschak.game.GamePlay.TrickBids;

import android.graphics.Canvas;
import android.graphics.Point;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;


//--------------------------------------------------------------------------------------------------
//  ToDo describe me    DO only needed for visible^^
//
public class BidsView extends DrawableObject{

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


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public BidsView() {
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
    public void draw(Canvas canvas, GameController controller) {
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
    //                          -> starts ending animation and saves start time
    //                          -> sets the end position for the winner bid field
    //
    public void startEndingAnimation(int winner_pos, GameLayout layout) {
        ending_animation_ = true;
        winner_id_ = winner_pos;
        if (winner_pos >= 0 && winner_pos < bids_field_list_.length) {
            Point end_position = layout.getTrickBidsGamePlayPositions().get(winner_pos);
            Point offset = new Point (end_position);
            offset.x -= bids_field_list_[winner_pos].getPosition().x;
            offset.y -= bids_field_list_[winner_pos].getPosition().y;
            bids_field_list_[winner_pos].setOffset(offset);
            bids_field_list_[winner_pos].setStartPos(
                    bids_field_list_[winner_pos].getPosition());
        }
        start_time_ = System.currentTimeMillis();
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

            if ( i == winner_id_ ) {
                bids_field_list_[i].setPosition(
                        (int) (bids_field_list_[i].getStartPos().x +
                                bids_field_list_[i].getOffset().x * percentage),
                        (int) (bids_field_list_[i].getStartPos().y +
                                bids_field_list_[i].getOffset().y * percentage) );
            }
            // reduce alpha
            else {
                int MAX_ALPHA = 255;
                int new_alpha = (int) (MAX_ALPHA - percentage * MAX_ALPHA);
                if (new_alpha == 0) {
                    bids_field_list_[i].setVisible(false);
                    bids_field_list_[i].updateAlpha(MAX_ALPHA);
                }
                bids_field_list_[i].updateAlpha(new_alpha);
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


    //----------------------------------------------------------------------------------------------
    //  reset
    //
    public void reset() {
        for (int i = 0; i < bids_field_list_.length; i++) {
            bids_field_list_[i] = null;
        }
        winner_id_ = GameController.NOT_SET;
        ending_animation_ = false;
        setVisible(false);
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public BidsField[] getBidsFieldList() {
        return bids_field_list_;
    }
}
