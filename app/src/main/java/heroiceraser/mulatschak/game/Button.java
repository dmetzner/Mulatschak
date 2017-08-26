package heroiceraser.mulatschak.game;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * Created by Daniel Metzner on 19.08.2017.
 */

public class Button {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //
    private boolean visible_;
    private boolean enabled_;
    private boolean pressed_;
    private Bitmap bitmap_;
    private Bitmap bitmap_pressed_;
    private Bitmap bitmap_disabled_;
    private Point coordinate_;


    //----------------------------------------------------------------------------------------------
    // Constructor
    //
    public Button() {
        visible_ = true;
        enabled_ = true;
        pressed_= false;
        bitmap_ = null;
        bitmap_pressed_ = null;
        bitmap_disabled_ = null;
        coordinate_ = null;
    }

    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public boolean isVisible() {
        return visible_;
    }
    public void setVisible(boolean visible_) {
        this.visible_ = visible_;
    }

    public boolean IsPressed() {
        return pressed_;
    }
    public void setPressed(boolean is_pressed) { pressed_ = is_pressed; }

    public boolean IsEnabled() {
        return enabled_;
    }
    public void setEnabled(boolean enabled) { enabled_ = enabled; }

    public Bitmap getBitmap() {
        return bitmap_;
    }
    public void setBitmap(Bitmap bitmap) { bitmap_ = bitmap; }

    public Bitmap getBitmapPressed() { return bitmap_pressed_; }
    public void setBitmapPressed(Bitmap bitmap_pressed) { bitmap_pressed_ = bitmap_pressed; }

    public Bitmap getBitmapDisabled() { return bitmap_disabled_; }
    public void setBitmapDisabled(Bitmap bitmap_disabled) { bitmap_disabled_ = bitmap_disabled; }

    public Point getPoint() {
        return coordinate_;
    }
    public void setPoint(Point coordinate) {
        coordinate_ = new Point(coordinate);
    }
    public void setPoint(int x, int y) {
        coordinate_ = new Point(x, y);
    }

}
