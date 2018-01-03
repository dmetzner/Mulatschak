package heroiceraser.mulatschak.DrawableBasicObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextPaint;

import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 19.08.2017.
 */

//----------------------------------------------------------------------------------------------
//  A Button is a drawable object with
//      2 more states ->  enabled, pressed
//      for both exist different bitmaps
//
public class MyTextButton extends DrawableObject{

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private boolean enabled_;
    private boolean pressed_;
    private String text_;
    private Point text_position_;
    private TextPaint text_paint_;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public MyTextButton() {
        super();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view, Point position, int width, int height, String image_name, String text) {
        setPosition(position);
        setWidth(width);
        setHeight(height);
        setBitmap(HelperFunctions.loadBitmap(view, image_name, width, height));
        text_ = text;
        text_paint_ = new TextPaint();
        text_paint_.setColor(Color.WHITE);
        text_paint_.setAntiAlias(true);
        text_paint_.setTextSize(getHeight() / 2.5f);
        text_paint_.setTextAlign(Paint.Align.CENTER);
        text_paint_.setTextScaleX(0.8f);
        Rect text_bounds_ = new Rect();
        text_paint_.getTextBounds(text_, 0, text_.length(), text_bounds_);

        while (text_bounds_.width() > 0.8 * getWidth()) {
            text_paint_.setTextSize(text_paint_.getTextSize() * 0.95f);
            text_paint_.getTextBounds(text_, 0, text_.length(), text_bounds_);
        }

        Point center = new Point(getPosition().x + getWidth() / 2, getPosition().y + getHeight() / 2);
        text_position_ = new Point(center.x, center.y + (int) text_paint_.getTextSize() / 4);
        enabled_ = true;
        pressed_= false;
        setVisible(true);
    }


    //----------------------------------------------------------------------------------------------
    // Draw
    //
    public void draw(Canvas canvas) {
        if (!isVisible()) {
            return;
        }
        canvas.save();

        float pressed_scale_ = 1;

        Bitmap bitmap = getBitmap();
        if (!IsEnabled()) {
            // bitmap = getBitmapDisabled();
        }
        else if (IsPressed()) {
            canvas.scale(0.96f, 0.96f);
            pressed_scale_ = 1.043f;
        }

        float offset_x = (getWidth() - getWidth() * pressed_scale_) / 2;
        float offset_y = (getHeight() - getHeight() * pressed_scale_) / 2;

        // background bitmap
        canvas.drawBitmap(bitmap,
                getPosition().x * pressed_scale_ - offset_x,
                getPosition().y * pressed_scale_, null);

        // text
        canvas.drawText(text_, text_position_.x * pressed_scale_,
                text_position_.y * pressed_scale_ + offset_y,
                text_paint_);

        canvas.restore();
    }


    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public boolean IsPressed() {
        return pressed_;
    }

    public void setPressed(boolean is_pressed) {
        this.pressed_ = is_pressed;
    }

    public boolean IsEnabled() {
        return enabled_;
    }

    public void setEnabled(boolean enabled) {
        this.enabled_ = enabled; }


}
