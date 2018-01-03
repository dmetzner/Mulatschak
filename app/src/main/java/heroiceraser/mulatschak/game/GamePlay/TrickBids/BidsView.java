package heroiceraser.mulatschak.game.GamePlay.TrickBids;

import android.graphics.Canvas;
import android.graphics.Point;
import java.util.ArrayList;
import java.util.List;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;


//--------------------------------------------------------------------------------------------------
//  ToDo describe me    DO only needed for visible^^
//
public class BidsView extends DrawableObject{

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private List<BidsField> bids_field_list_;               // container for all bid buttons

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
        bids_field_list_ = new ArrayList<>();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //          -> creates bid_fields
    //          -> sets start positions to playerInfo positions
    //          -> set endPositions to the discard pile positions
    //          -> still nothing visible!
    //
    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();

        for (int i = 0; i < layout.getDiscardPilePositions().size(); i++) {
            BidsField bids_field = new BidsField();
            Point start_pos = new Point(layout.getPlayerInfoPositions().get(i));
            Point final_pos = new Point(layout.getDiscardPilePositions().get(i));
            start_pos.x += layout.getCardWidth() / 2;
            start_pos.y += layout.getCardHeight() / 2;      // offsets to center 'em
            final_pos.x += layout.getCardWidth() / 2;
            final_pos.y += layout.getCardHeight() / 2;
            bids_field.init(view, start_pos, final_pos);
            bids_field_list_.add(bids_field);
        }
        setVisible(false);
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
        for (BidsField bf : bids_field_list_) {
            bf.draw(canvas, controller);
        }
        // keep animation running
        if (ending_animation_) {
            continueEndingAnimation(controller);
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
        if (winner_pos >= 0 && winner_pos < bids_field_list_.size()) {
            Point end_position = layout.getTrickBidsGamePlayPositions().get(winner_pos);
            Point offset = new Point (end_position);
            offset.x -= bids_field_list_.get(winner_pos).getPosition().x;
            offset.y -= bids_field_list_.get(winner_pos).getPosition().y;
            bids_field_list_.get(winner_pos).setOffset(offset);
            bids_field_list_.get(winner_pos).setStartPosition(
                    bids_field_list_.get(winner_pos).getPosition());
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

        for (int i = 0; i < bids_field_list_.size(); i++) {
            // move winner bid field in end pos direction
            if ( i == winner_id_ ) {
                bids_field_list_.get(i).setPosition(
                        (int) (bids_field_list_.get(i).getStartPosition().x +
                                bids_field_list_.get(i).getOffset().x * percentage),
                        (int) (bids_field_list_.get(i).getStartPosition().y +
                                bids_field_list_.get(i).getOffset().y * percentage) );
            }
            // reduce alpha
            else {
                int MAX_ALPHA = 255;
                int new_alpha = (int) (MAX_ALPHA - percentage * MAX_ALPHA);
                if (new_alpha == 0) {
                    bids_field_list_.get(i).setVisible(false);
                    bids_field_list_.get(i).updateAlpha(MAX_ALPHA);
                }
                bids_field_list_.get(i).updateAlpha(new_alpha);
            }

            // reduce size
            double size_percentage = 1 - percentage;
            bids_field_list_.get(i).changeSizeBasedOnPercentage(size_percentage);
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
        for (BidsField bf : bids_field_list_) {
            bf.reset();
        }
        winner_id_ = GameController.NOT_SET;
        ending_animation_ = false;
        setVisible(false);
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public List<BidsField> getBidsFieldList() {
        return bids_field_list_;
    }
}
