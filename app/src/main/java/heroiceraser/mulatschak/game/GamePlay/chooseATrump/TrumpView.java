package heroiceraser.mulatschak.game.GamePlay.chooseATrump;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.TextPaint;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 28.12.2017.
 */

public class TrumpView extends DrawableObject {

    private Bitmap active_bitmap_;
    private String active_id_;
    private int max_height_;
    private int max_width_;
    private Point start_position_;
    private Point end_position_;
    private Point offset_;

    private boolean animation_running_;
    private long start_time_;


    public TrumpView() {
        offset_ = new Point();
        end_position_ = new Point();
        start_position_ = new Point();
        active_bitmap_ = null;
    }


    public void init(GameView view) {

        setPosition(new Point());
        setWidth(0);
        setHeight(0);

        max_width_ = view.getController().getLayout().getCardWidth() * 4;
        max_height_ = view.getController().getLayout().getCardHeight() * 3;
        end_position_ = new Point(
                view.getController().getLayout().getDiscardPilePositions().get(1).x,
                view.getController().getLayout().getDiscardPilePositions().get(2).y
        );

        setVisible(false);
    }

    public void startAnimation(int active_symbol, int player_id, GameController controller) {
        active_id_ = "trumps_basic_" + active_symbol;
        start_time_ = System.currentTimeMillis();
        controller.getView().enableUpdateCanvasThread();
        if (player_id >= 0 && player_id < controller.getAmountOfPlayers()) {
            start_position_ = controller.getLayout().getPlayerInfoPositions().get(player_id);
        }
        else {
            start_position_.x = end_position_.x + max_width_ / 2;
            start_position_.y = end_position_.y + max_height_ / 2;
        }
        offset_ = new Point(end_position_.x - start_position_.x,
                end_position_.x - start_position_.x);
        setPosition(start_position_);
        setVisible(true);
        animation_running_ = true;
    }

    public void continueAnimation(GameController controller) {
        double max_time = 1000;
        long time = System.currentTimeMillis();
        long time_since_start = time - start_time_;

        double percentage = time_since_start / max_time;

        if (percentage > 1) {
            percentage = 1;
        }

        int width = (int) (max_width_ * percentage);
        int height = (int) (max_height_ * percentage);

        if (width < 1) {
            width = 1;
        }
        if (height < 1) {
            height = 1;
        }

        setWidth(width);
        setHeight(height);

        setPosition(start_position_.x + (int) (offset_.x * percentage),
                start_position_.y + (int) (offset_.y * percentage));

        active_bitmap_ = HelperFunctions.loadBitmap(controller.getView(), active_id_, getWidth(), getHeight());

        if (percentage >= 1) {
            animation_running_ = false;
            controller.continueAfterTrumpWasChoosen();
        }
    }

    public void draw(Canvas canvas, GameController controller) {
        if (isVisible()) {

            if (active_bitmap_ != null) {
                canvas.drawBitmap(active_bitmap_, getPosition().x, getPosition().y, null);
            }

            if (animation_running_) {
                continueAnimation(controller);
            }
        }
    }

}
