package heroiceraser.mulatschak.DrawableBasicObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextPaint;

import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.BitmapMethodes;


//----------------------------------------------------------------------------------------------
//  A Button is a drawable object with
//      2 more states ->  enabled, pressed
//      for both exist different bitmaps
//
public class MyButton extends DrawableObject{

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private boolean enabled;
    private boolean pressed;
    private String text;
    private Point textPosition;
    private TextPaint textPaint;
    private Bitmap bitmapOverlay;
    private Paint overlay;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public MyButton() {
        super();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view, Point position, Point size, Bitmap image, String text) {
        setWidth(size.x);
        setHeight(size.y);
        Bitmap bmp = BitmapMethodes.getResizedBitmap(image, size.x, size.y);
        setBitmap(bmp);
        init(view, position, size.x, size.y, "", text);
    }


    public void init(GameView view, Point position, Point size, String image_name, String text) {
        init(view, position, size.x, size.y, image_name, text);
    }

    public void init(GameView view, Point position, int width, int height, String image_name,
                     String text) {
        setPosition(position);
        setWidth(width);
        setHeight(height);
        if (!image_name.equals("")) {
            setBitmap(BitmapMethodes.loadBitmap(view, image_name, width, height));
        }
        this.text = text;
        textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(getHeight() / 2.5f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextScaleX(0.8f);
        Rect text_bounds_ = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), text_bounds_);

        while (text_bounds_.width() > 0.8 * getWidth()) {
            textPaint.setTextSize(textPaint.getTextSize() * 0.95f);
            textPaint.getTextBounds(text, 0, text.length(), text_bounds_);
        }

        Point center = new Point(getPosition().x + getWidth() / 2,
                getPosition().y + getHeight() / 2);
        textPosition = new Point(center.x,
                center.y + (int) textPaint.getTextSize() / 4);

        // Overlay
        if (getBitmap() != null) {
            overlay = new Paint();
            overlay.setAlpha(125);
            bitmapOverlay = BitmapMethodes.createBitmapOverlay(getBitmap());
        }

        enabled = true;
        pressed= false;
        setVisible(true);
    }


    //----------------------------------------------------------------------------------------------
    //  Draw
    //
    public void draw(Canvas canvas) {
        if (!isVisible()) {
            return;
        }
        canvas.save();

        float pressed_scale_ = 1;

        if (isPressed()) {
            canvas.scale(0.96f, 0.96f);
            pressed_scale_ = 1.043f;
        }

        float offset_x = (getWidth() - getWidth() * pressed_scale_) / 2;
        float offset_y = (getHeight() - getHeight() * pressed_scale_) / 2;

        // background bitmap
        if (getBitmap() != null) {
            canvas.drawBitmap(getBitmap(),
                    getPosition().x * pressed_scale_ - offset_x,
                    getPosition().y * pressed_scale_, null);
        }

        // text
        canvas.drawText(text, textPosition.x * pressed_scale_,
                textPosition.y * pressed_scale_ + offset_y,
                textPaint);


        if (!isEnabled()) {
            if (getBitmap() != null) {
                canvas.drawBitmap(bitmapOverlay,
                        getPosition().x * pressed_scale_ - offset_x,
                        getPosition().y * pressed_scale_, overlay);
            }
        }
        canvas.restore();
    }


    //----------------------------------------------------------------------------------------------
    //  Touch Events
    //
    synchronized public boolean touchEventDown(int X, int Y) {
        if (isVisible() && isEnabled() &&
                X >= getPosition().x && X < getPosition().x + getWidth() &&
                Y >= getPosition().y && Y < getPosition().y + getHeight()) {
            setPressed(true);
            return true;
        }
        return false;
    }

    synchronized public void touchEventMove(int X, int Y) {
        if (isVisible() && isEnabled() && isPressed()) {
            if ( X >= getPosition().x && X < getPosition().x + getWidth() &&
                    Y >= getPosition().y && Y < getPosition().y + getHeight()) {
                setPressed(true);
            }
            else {
                setPressed(false);
            }
        }
    }

    synchronized public boolean touchEventUp(int X, int Y) {
        if (isVisible() && isEnabled() && isPressed()) {
            if (X >= getPosition().x && X < getPosition().x + getWidth() &&
                    Y >= getPosition().y && Y < getPosition().y + getHeight()) {
                setPressed(false);
                return true;
            }
        }
        return false;
    }




    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean is_pressed) {
        this.pressed = is_pressed;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled; }

    public TextPaint getTextPaint() {
        return this.textPaint;
    }

    public void changeTextPaint(TextPaint textPaint) {
        this.textPaint = textPaint;
        Rect text_bounds_ = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), text_bounds_);

        while (text_bounds_.width() > 0.8 * getWidth()) {
            textPaint.setTextSize(textPaint.getTextSize() * 0.95f);
            textPaint.getTextBounds(text, 0, text.length(), text_bounds_);
        }

        Point center = new Point(getPosition().x + getWidth() / 2, getPosition().y + getHeight() / 2);
        textPosition = new Point(center.x, center.y + (int) textPaint.getTextSize() / 4);
    }


}
