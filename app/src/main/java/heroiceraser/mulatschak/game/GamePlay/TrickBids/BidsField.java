package heroiceraser.mulatschak.game.GamePlay.TrickBids;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.TextPaint;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;


//--------------------------------------------------------------------------------------------------
//  BidsField
//              ToDo
//
public class BidsField extends DrawableObject{

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    //---- circle
    private float radius_;
    private int stroke_width_;
    private Paint circle_paint_border_;
    private Paint circle_paint_default_;

    //---- Text
    private TextPaint text_paint_;
    private String text_;

    //---- Animation
    private boolean animation_running_;
    private long start_time_;

    private Point start_pos_;               // position movement
    private Point end_pos_;
    private Point saved_start_pos_;
    private Point offset_;

    private float min_radius_;              // size changes
    private float max_radius_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    BidsField() {
        super();
    }


    //----------------------------------------------------------------------------------------------
    //  Init
    //
    public void init(GameView view, final Point start_pos, final Point end_pos) {

        //---- position
        start_pos_ = start_pos;
        saved_start_pos_= new Point(start_pos);
        end_pos_ = end_pos;
        calculatePositionOffset();

        //---- size
        // dealerButton <= radius <= min(cardWidth, cardHeight)
        max_radius_ = Math.min(view.getController().getLayout().getCardWidth(),
        view.getController().getLayout().getCardHeight()) / 2.03f;
        min_radius_ = view.getController().getLayout().getDealerButtonSize() / 2;
        radius_ = min_radius_;

        //---- border
        circle_paint_border_ = new Paint();
        circle_paint_border_.setColor(Color.BLACK);
        circle_paint_border_.setAntiAlias(true);
        stroke_width_ = (int) (radius_ / 10);
        if (stroke_width_ < 2) {
            stroke_width_ = 2;
        }
        circle_paint_border_.setStrokeWidth(stroke_width_);
        circle_paint_border_.setStyle(Paint.Style.STROKE);

        //---- fill
        circle_paint_default_ = new Paint();
        circle_paint_default_.setColor(view.getResources().getColor(R.color.metallic_blue));
        circle_paint_default_.setAntiAlias(true);
        circle_paint_default_.setStyle(Paint.Style.FILL);

        //---- text
        text_paint_ = new TextPaint();
        text_paint_.setAntiAlias(true);
        text_paint_.setTextSize(radius_);
        text_paint_.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        text_paint_.setColor(Color.WHITE);
        text_paint_.setStyle(Paint.Style.FILL);
        text_paint_.setTextAlign(Paint.Align.CENTER);
        text_ = "";

        animation_running_ = false;
        setVisible(false);
    }


    //----------------------------------------------------------------------------------------------
    //  calculatePositionOffset
    //
    private void calculatePositionOffset() {
        offset_ = new Point (end_pos_.x - start_pos_.x, end_pos_.y - start_pos_.y);
    }


    //----------------------------------------------------------------------------------------------
    //  startEndingAnimation
    //                          -> starts animation and saves start time
    //                          -> set field back to origin start position,
    //                             max alpha, min radius
    //
    public void startAnimation(String text, GameController controller) {
        text_ = text;
        controller.getView().enableUpdateCanvasThread();
        setPosition(saved_start_pos_);
        start_pos_ = saved_start_pos_;
        calculatePositionOffset();
        radius_ = min_radius_;
        text_paint_.setTextSize(radius_);
        updateAlpha(255);
        start_time_ = System.currentTimeMillis();
        animation_running_ = true;
        setVisible(true);
    }


    //----------------------------------------------------------------------------------------------
    //  continueEndingAnimation
    //                          -> gets called till animation is done
    //                          -> winner bid field moves to end position
    //                          -> other field fade out (alpha reduction)
    //                          -> all field get their size reduced
    //
    private void continueAnimation(GameController controller) {
        double speed_factor = controller.getSettings().getAnimationSpeed().getSpeedFactor();
        double max_time = 1000 * speed_factor;
        long time = System.currentTimeMillis();
        long time_since_start = time - start_time_;

        double percentage = time_since_start / max_time;

        if (percentage > 1) {
            percentage = 1;
        }

        changeSizeBasedOnPercentage(percentage);

        moveToFinalPositionBasedOnPercentage(percentage);

        if (percentage >= 1) {
            animation_running_ = false;
            controller.getGamePlay().getTrickBids().makeTrickBids(false, controller);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  UpdateAlpha value
    //
    void updateAlpha(int alpha) {
        circle_paint_border_.setAlpha(alpha);
        circle_paint_default_.setAlpha(alpha);
        text_paint_.setAlpha(alpha);
    }


    //----------------------------------------------------------------------------------------------
    //  moveToFinalPositionBasedOnPercentage
    //
    private void moveToFinalPositionBasedOnPercentage(double percentage) {

        int offset_x = (int) (offset_.x * percentage);
        int offset_y = (int) (offset_.y * percentage);

        setPosition(new Point(start_pos_.x + offset_x,
                start_pos_.y + offset_y));
    }


    //----------------------------------------------------------------------------------------------
    //  changeSizeBasedOnPercentage
    //                              -> can't be smaller than min radius or bigger than max radius
    //
    void changeSizeBasedOnPercentage(double percentage) {
        radius_ = (float) percentage * max_radius_;
        if (radius_ < min_radius_) {
            radius_ = min_radius_;
        }
        if (radius_ > max_radius_) {
            radius_ = max_radius_;
        }
        text_paint_.setTextSize((radius_));
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //          -> draws a circle with a border and a text in it
    //          -> continues ending animation if it's running
    //
    public void draw(Canvas canvas, GameController controller) {
        if (isVisible()) {

            // reduce radius by the stroke width
            float radius = radius_ - stroke_width_;

            // draw filling
            canvas.drawCircle(getPosition().x, getPosition().y, radius, circle_paint_default_);
            // draw border
            canvas.drawCircle(getPosition().x, getPosition().y, radius, circle_paint_border_);

            canvas.drawText(text_, getPosition().x,
                    getPosition().y + radius_ / 2.5f, text_paint_);

            //----
            if (animation_running_) {
                continueAnimation(controller);
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    void reset() {
        setVisible(false);
    }

    void setStartPosition(Point start_pos) {
        start_pos_ = start_pos;
    }

    Point getStartPosition() {
        return start_pos_;
    }

    void setOffset(Point offset) {
        offset_ = offset;
    }

    Point getOffset() {
        return offset_;
    }

    public String getText() {
        return text_;
    }

    public void setText(String text) {
        text_ = text;
    }
}
