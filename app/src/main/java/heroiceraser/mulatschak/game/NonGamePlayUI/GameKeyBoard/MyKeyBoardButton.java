package heroiceraser.mulatschak.game.NonGamePlayUI.GameKeyBoard;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextPaint;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.BitmapMethodes;


//----------------------------------------------------------------------------------------------
//  A Button is a drawable object with
//      2 more states ->  enabled, pressed
//      for both exist different bitmaps
//
public class MyKeyBoardButton extends DrawableObject{

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private boolean pressed;
    private String text;
    private Point textPosition;
    private TextPaint textPaint;
    private Rect background;
    private Paint bgPaint;
    private Paint bgPaintPressed;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    MyKeyBoardButton() {
        super();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view, Point position, int width, int height, String image_name, String text) {
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
        textPaint.setTextSize(getHeight() / 1.8f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextScaleX(0.75f);
        Rect text_bounds_ = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), text_bounds_);

        while (text_bounds_.width() > 0.8 * getWidth()) {
            textPaint.setTextSize(textPaint.getTextSize() * 0.95f);
            textPaint.getTextBounds(text, 0, text.length(), text_bounds_);
        }

        Point center = new Point(getPosition().x + getWidth() / 2, getPosition().y + getHeight() / 2);
        textPosition = new Point(center.x, center.y + (int) textPaint.getTextSize() / 4);


        background = new Rect(position.x, position.y,
                position.x + getWidth(), position.y + getHeight());
        bgPaint = new Paint();
        bgPaint.setStrokeWidth(2);
        bgPaint.setColor(Color.WHITE);
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.STROKE);

        bgPaintPressed = new Paint(bgPaint);
        bgPaintPressed.setStyle(Paint.Style.FILL);
        bgPaintPressed.setColor(view.getResources().getColor(R.color.metallic_blue));

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

        Bitmap bitmap = getBitmap();
        Paint paint = bgPaint;
        Rect rect = new Rect(background);

        if (isPressed()) {
            canvas.scale(0.96f, 0.96f);
            pressed_scale_ = 1.043f;
            paint = bgPaintPressed;
        }

        float offset_x = (getWidth() - getWidth() * pressed_scale_) / 2;
        float offset_y = (getHeight() - getHeight() * pressed_scale_) / 2;

        if (isPressed()) {
            rect.set((int) (background.left * pressed_scale_ - offset_x),
                    (int) (background.top * pressed_scale_),
                    (int) (background.right * pressed_scale_),
                    (int) (background.bottom * pressed_scale_) );
        }

        // background bitmap
        if (bitmap != null) {
            canvas.drawBitmap(bitmap,
                    getPosition().x * pressed_scale_ - offset_x,
                    getPosition().y * pressed_scale_, null);
        }
        else {
            canvas.drawRect(rect, paint);
        }

        // text
        canvas.drawText(text, textPosition.x * pressed_scale_,
                textPosition.y * pressed_scale_ + offset_y,
                textPaint);


        canvas.restore();
    }


    //----------------------------------------------------------------------------------------------
    //  Touch Events
    //
    public boolean touchEventDown(int X, int Y) {
        if (isVisible() &&
                X >= getPosition().x && X < getPosition().x + getWidth() &&
                Y >= getPosition().y && Y < getPosition().y + getHeight()) {
            setPressed(true);
            return true;
        }
        return false;
    }

    public boolean touchEventMove(int X, int Y) {
        if (isVisible() && isPressed()) {
            if ( X >= getPosition().x && X < getPosition().x + getWidth() &&
                    Y >= getPosition().y && Y < getPosition().y + getHeight()) {
                setPressed(true);
                return true;
            }
            else {
                if (isPressed()) {
                    setPressed(false);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean touchEventUp(int X, int Y) {
        if (isVisible()) { // && is pressed
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
    private boolean isPressed() {
        return pressed;
    }

    private void setPressed(boolean is_pressed) {
        this.pressed = is_pressed;
    }

    public String getText() {
        return text;
    }

}
