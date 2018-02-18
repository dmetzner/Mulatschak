package heroiceraser.mulatschak.DrawableBasicObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

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
public class MyButton extends DrawableObject{

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //
    private boolean enabled_;
    private boolean pressed_;
    private Bitmap bitmap_pressed_;
    private Bitmap bitmap_disabled_;

    //----------------------------------------------------------------------------------------------
    // Constructor
    //
    public MyButton() {
        super();
        enabled_ = true;
        pressed_= false;
        bitmap_pressed_ = null;
        bitmap_disabled_ = null;
    }

    public void init(GameView view, Point position, Point size, String image_name) {
        init(view, position, size.x, size.y, image_name);
    }

    public void init(GameView view, Point position, int width, int height, String image_name) {
        setPosition(position);
        setWidth(width);
        setHeight(height);
        setBitmap(HelperFunctions.loadBitmap(view, image_name, width, height));
        setBitmapPressed(HelperFunctions.loadBitmap(view, image_name + "_pressed", width, height));
        setBitmapDisabled(HelperFunctions.loadBitmap(view, image_name + "_disabled", width, height));
        setVisible(true);
    }



    //----------------------------------------------------------------------------------------------
    // Draw
    //
    public void draw(Canvas canvas) {
        if (!isVisible()) {
            return;
        }
        Bitmap bitmap = getBitmap();
        if (!IsEnabled()) {
            bitmap = getBitmapDisabled();
        }
        else if (IsPressed()) {
            bitmap = getBitmapPressed();
        }
        canvas.drawBitmap(bitmap,
                getPosition().x,
                getPosition().y, null);
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

    public Bitmap getBitmapPressed() {
        return bitmap_pressed_;
    }
    public void setBitmapPressed(Bitmap bitmap_pressed) {
        this.bitmap_pressed_ = bitmap_pressed;
    }

    public Bitmap getBitmapDisabled() {
        return bitmap_disabled_;
    }
    public void setBitmapDisabled(Bitmap bitmap_disabled) {
        this.bitmap_disabled_ = bitmap_disabled;
    }

}
