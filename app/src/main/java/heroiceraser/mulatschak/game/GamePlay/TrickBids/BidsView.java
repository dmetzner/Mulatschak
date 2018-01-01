package heroiceraser.mulatschak.game.GamePlay.TrickBids;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 25.12.2017.
 */

public class BidsView extends DrawableObject{

    private List<BidsField> bids_field_list_;
    private boolean ending_animation_;
    private long start_time_;
    private int winner_id_;

    public BidsView() {
        super();
        bids_field_list_ = new ArrayList<>();
    }

    public void init(GameView view) {
        for (int i = 0; i < view.getController().getLayout().getDiscardPilePositions().size(); i++) {
            BidsField bids_field = new BidsField();
            Point start_pos = new Point(view.getController().getLayout().getPlayerInfoPositions().get(i));
            Point final_pos = new Point(view.getController().getLayout().getDiscardPilePositions().get(i));
            int radius = view.getController().getLayout().getCardWidth() / 2;
            start_pos.x += radius;
            start_pos.y += view.getController().getLayout().getCardHeight() / 2;
            final_pos.x += radius;
            final_pos.y += view.getController().getLayout().getCardHeight() / 2;
            bids_field.init(view, start_pos, final_pos, radius);

            bids_field_list_.add(bids_field);
        }
        setVisible(false);
    }

    public void draw(Canvas canvas, GameController controller) {
        if (isVisible()) {
            for (BidsField bf : bids_field_list_) {
                bf.draw(canvas, controller);
            }
            if (ending_animation_) {
                continueEndingAnimation(controller);
            }
        }
    }

    public void endingAnimation(int winner_pos, GameLayout layout) {
        ending_animation_ = true;
        winner_id_ = winner_pos;
        start_time_ = System.currentTimeMillis();
        if (winner_pos >= 0 && winner_pos < bids_field_list_.size()) {
            Point end_position = layout.getTrickBidsGamePlayPositions().get(winner_pos);
            Point offset = new Point (end_position);
            offset.x -= bids_field_list_.get(winner_pos).getPosition().x;
            offset.y -= bids_field_list_.get(winner_pos).getPosition().y;

            bids_field_list_.get(winner_pos).setOffset(offset);
            bids_field_list_.get(winner_pos).setStartPosition(
                    bids_field_list_.get(winner_pos).getPosition());
        }
    }

    private void continueEndingAnimation(GameController controller) {
        double max_time = 1000;
        long time = System.currentTimeMillis();
        long time_since_start = time - start_time_;

        double percentage = time_since_start / max_time;

        if (percentage > 1) {
            percentage = 1;
        }

        for (int i = 0; i < bids_field_list_.size(); i++) {
            if ( i == winner_id_ ) {
                bids_field_list_.get(i).setPosition(
                        (int) (bids_field_list_.get(i).getStartPosition().x +
                                bids_field_list_.get(i).getOffset().x * percentage),
                        (int) (bids_field_list_.get(i).getStartPosition().y +
                                bids_field_list_.get(i).getOffset().y * percentage) );
            }
            // reduce alpha
            else {
                int new_alpha = (int) (255 - percentage * 255);
                if (new_alpha == 0) {
                    bids_field_list_.get(i).setVisible(false);
                    bids_field_list_.get(i).updateAlpha(255);
                }
                bids_field_list_.get(i).updateAlpha(new_alpha);
            }

            // reduce size
            double size_percentage = 1 - percentage;
            if (size_percentage < 0.45) {
                size_percentage = 0.45;
            }
            bids_field_list_.get(i).changeSizeBasedOnPercentage(size_percentage);
        }

        if (percentage >= 1) {
            ending_animation_ = false;
            controller.continueAfterTrickBids();
            return;
        }
    }


    public void reset() {
        for (BidsField bf : bids_field_list_) {
            bf.reset();
        }
        winner_id_ = GameController.NOT_SET;
        ending_animation_ = false;
        setVisible(false);
    }



    public List<BidsField> getBidsFieldList() {
        return bids_field_list_;
    }
}
