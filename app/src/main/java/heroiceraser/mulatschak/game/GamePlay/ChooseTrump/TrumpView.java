package heroiceraser.mulatschak.game.GamePlay.ChooseTrump;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.text.TextPaint;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 28.12.2017.
 */
// ToDo
public class TrumpView extends DrawableObject {

    private Bitmap active_bitmap_;
    private String active_id_;
    private int max_height_;
    private int max_width_;
    private Point start_position_;
    private Point end_position_;
    private Point offset_;
    private Paint paint_;
    private boolean animation_running_;
    private long start_time_;
    private Point game_position_;
    private boolean ending_animation_running_;
    private Point game_size_;
    private TextPaint text_paint_;
    private Point multi_pos_;
    private String multi_text_;


    public TrumpView() {
        offset_ = new Point();
        end_position_ = new Point();
        start_position_ = new Point();
        active_bitmap_ = null;
        paint_ = new Paint();
        game_position_ = new Point();
        game_size_ = new Point();
        text_paint_ = new TextPaint();
    }


    public void init(GameView view) {

        setPosition(new Point());
        setWidth(0);
        setHeight(0);

        max_width_ = (int) (view.getController().getLayout().getCardWidth() * 3.5);
        max_height_ = view.getController().getLayout().getCardHeight() * 3;
        end_position_ = new Point(
                view.getController().getLayout().getScreenWidth() / 2 - max_width_ / 2,
                (view.getController().getLayout().getSectors().get(3).y)
        );
        game_position_ = new Point(view.getController().getLayout().getScreenWidth() / 45 ,
                view.getController().getLayout().getSectors().get(2).y +
                        (int) (view.getController().getLayout().getSectors().get(1).y * 0.1)
        );
        game_size_ = new Point(view.getController().getLayout().getPlayerInfoSize());
        game_size_.x *= 0.8;
        game_size_.y *= 0.9;

        paint_.setAlpha(255);

        multi_text_ = "";
        multi_pos_ = new Point(game_position_);
        multi_pos_.x += game_size_.x * 1.1;
        multi_pos_.y += game_size_.y * 0.7;

        text_paint_.setStyle(Paint.Style.FILL);
        text_paint_.setAntiAlias(true);
        text_paint_.setColor(Color.BLACK);
        text_paint_.setTextSize(game_size_.y * 0.45f);

        setVisible(false);
    }

    //----------------------------------------------^^------------------------------------------------
    //  startAnimation
    //
    public void startAnimation(int active_symbol, int player_id, GameController controller) {
        if (active_symbol == MulatschakDeck.HEART) {
            controller.getLogic().raiseMultiplier();
        }
        active_id_ = "trumps_basic_" + active_symbol;
        start_time_ = System.currentTimeMillis();
        controller.getView().enableUpdateCanvasThread();
        if (player_id >= 0 && player_id < controller.getAmountOfPlayers()) {
            start_position_ = new Point(controller.getLayout()
                    .getPlayerInfoPositions().get(player_id));
        }
        else {
            start_position_.x = end_position_.x + max_width_ / 2;
            start_position_.y = end_position_.y + max_height_ / 2;
        }
        offset_ = new Point(end_position_.x - start_position_.x,
                end_position_.y - start_position_.y);
        setPosition(start_position_);
        paint_.setAlpha(255);

        int multiplier = controller.getLogic().getMultiplier();
        if (multiplier > 1) {
            multi_text_ = "x" + multiplier;
        }
        else {
            multi_text_ = "";
        }

        setVisible(true);
        animation_running_ = true;
    }

    private void continueAnimation(GameController controller) {
        double speed_factor = controller.getSettings().getAnimationSpeed().getSpeedFactor();
        double max_time = 1000 * speed_factor;
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
            Handler myHandler = new Handler();
            Runnable startEndingAnimation = new Runnable() {
                @Override
                public void run() {
                    start_time_ = System.currentTimeMillis();
                    ending_animation_running_ = true;
                }
            };
            myHandler.postDelayed(startEndingAnimation, 750);
        }
    }

    private void continueEndingAnimation(GameController controller) {
        double animation_factor = controller.getSettings().getAnimationSpeed().getSpeedFactor();
        double max_time = 750 * animation_factor;
        long time = System.currentTimeMillis();
        long time_since_start = time - start_time_;

        double percentage = time_since_start / max_time;

        if (percentage > 1) {
            percentage = 1;
        }
        paint_.setAlpha(255 - (int) (255 * percentage));

        if (percentage >= 1) {
            ending_animation_running_ = false;
            paint_.setAlpha(255);
            setPosition(game_position_);
            setWidth(game_size_.x);
            setHeight(game_size_.y);
            active_bitmap_ = HelperFunctions
                    .loadBitmap(controller.getView(), active_id_, getWidth(), getHeight());
            controller.continueAfterTrumpWasChosen();
        }
    }


    public void draw(Canvas canvas, GameController controller) {
        if (isVisible()) {

            if (active_bitmap_ != null) {
                canvas.drawBitmap(active_bitmap_, getPosition().x, getPosition().y, paint_);
            }

            canvas.drawText(multi_text_, multi_pos_.x, multi_pos_.y, text_paint_);

            if (animation_running_) {
                continueAnimation(controller);
            }

            if (ending_animation_running_) {
                continueEndingAnimation(controller);
            }
        }
    }
}
