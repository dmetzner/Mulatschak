package heroiceraser.mulatschak.game.GamePlay.TrickBids;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;


import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameController;

public class BidsField extends DrawableObject{

    private Paint circle_paint_border_;
    private Paint circle_paint_default_;
    private float radius_;
    private float end_radius_;
    private TextPaint text_paint_;
    private float default_text_size_;
    private String text_;

    // Animation
    private boolean animation_running_;
    private long start_time_;
    private Point real_start_pos_;
    private Point start_pos_;
    private Point offset_;
    private Point end_pos_;

    public BidsField() {
        super();
    }


    public void init(View view, final Point start_pos, final Point end_pos, final int radius) {
        text_ = "-";

        start_pos_ = start_pos;
        real_start_pos_= new Point(start_pos);
        end_pos_ = end_pos;
        calculateOffset();

        // Circle
        end_radius_ = radius;
        radius_ = (float) (end_radius_ * (20.0/100.0));

        // border
        circle_paint_border_ = new Paint();
        circle_paint_border_.setColor(Color.BLACK);
        circle_paint_border_.setAntiAlias(true);
        int stroke_width = (radius / 10);
        if (stroke_width < 2) {
            stroke_width = 2;
        }
        circle_paint_border_.setStrokeWidth(stroke_width);
        circle_paint_border_.setStyle(Paint.Style.STROKE);

        // fill
        circle_paint_default_ = new Paint();
        circle_paint_default_.setAlpha(100);
        circle_paint_default_.setColor(view.getResources().getColor(R.color.metallic_blue));
        circle_paint_default_.setAntiAlias(true);
        circle_paint_default_.setStyle(Paint.Style.FILL);


        // text
        default_text_size_ = view.getResources().getDimension(R.dimen.bids_field_text_size);
        text_paint_ = new TextPaint();
        text_paint_.setAntiAlias(true);
        text_paint_.setTextSize(view.getResources().getDimension(R.dimen.bids_field_text_size));
        text_paint_.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        text_paint_.setColor(Color.WHITE);
        text_paint_.setStyle(Paint.Style.FILL);
        text_paint_.setTextAlign(Paint.Align.CENTER);
        text_ = "";


        animation_running_ = false;
        setVisible(false);
    }

    private void calculateOffset() {
        offset_ = new Point (end_pos_.x - start_pos_.x, end_pos_.y - start_pos_.y);
    }


    public void startAnimation(String text, GameController controller) {
        text_ = text;
        start_time_ = System.currentTimeMillis();
        controller.getView().enableUpdateCanvasThread();
        setPosition(real_start_pos_);
        start_pos_ = real_start_pos_;
        calculateOffset();
        setVisible(true);
        animation_running_ = true;
        text_paint_.setTextSize(default_text_size_ * (20.f / 100.f) );
        updateAlpha(255);
    }

    public void updateAlpha(int alpha) {
        circle_paint_border_.setAlpha(alpha);
        circle_paint_default_.setAlpha(alpha);
        text_paint_.setAlpha(alpha);
    }

    public void moveToFinalPositionBasedOnPercentage(double percentage) {

        int offset_x = (int) (offset_.x * percentage);
        int offset_y = (int) (offset_.y * percentage);

        setPosition(new Point(start_pos_.x + offset_x,
                start_pos_.y + offset_y));
    }

    public void changeSizeBasedOnPercentage(double percentage) {
        radius_ = (float) percentage * end_radius_;
        text_paint_.setTextSize((radius_));
    }


    public void continueAnimation(GameController controller) {
        double max_time = 1000;
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
            controller.makeTrickBids();
        }
    }

    public void draw(Canvas canvas, GameController controller) {
        if (isVisible()) {
            canvas.drawCircle(getPosition().x, getPosition().y, radius_, circle_paint_default_);
            canvas.drawCircle(getPosition().x, getPosition().y, radius_, circle_paint_border_);

            canvas.drawText(text_, getPosition().x,
                    getPosition().y + radius_ / 2.5f, text_paint_);

            if (animation_running_) {
                continueAnimation(controller);
            }
        }
    }

    public void reset() {
        setVisible(false);
    }

    public void setStartPosition(Point start_pos) {
        start_pos_ = start_pos;
    }

    public Point getStartPosition() {
        return start_pos_;
    }

    public void setOffset(Point offset) {
        offset_ = offset;
    }

    public Point getOffset() {
        return offset_;
    }

    public String getText() {
        return text_;
    }

    public void setText(String text) {
        text_ = text;
    }

    public float getRadius() {
        return radius_;
    }
}
