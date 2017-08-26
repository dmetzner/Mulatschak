package heroiceraser.mulatschak.DrawableBasicObjects;

import android.graphics.Bitmap;

/**
 * Created by Daniel Metzner on 19.08.2017.
 */

//----------------------------------------------------------------------------------------------
//  A Button is a drawable object with
//      2 more states ->  enabled, pressed
//      for both exist different bitmaps
//
public class Button extends DrawableObject{

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
    public Button() {
        super();
        enabled_ = true;
        pressed_= false;
        bitmap_pressed_ = null;
        bitmap_disabled_ = null;
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
