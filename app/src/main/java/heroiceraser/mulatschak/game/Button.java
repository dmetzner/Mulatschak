package heroiceraser.mulatschak.game;

import android.graphics.Bitmap;

import heroiceraser.mulatschak.helpers.Coordinate;

/**
 * Created by Daniel Metzner on 19.08.2017.
 */

public class Button {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //
    private boolean is_pressed_;
    private Bitmap bitmap_;
    private Bitmap bitmap_pressed_;
    private Coordinate coordinate_;

    //----------------------------------------------------------------------------------------------
    // Constructor
    //
    public Button() {
        is_pressed_= false;
        bitmap_ = null;
        bitmap_pressed_ = null;
    }

    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public boolean IsPressed() {
        return is_pressed_;
    }
    public void setPressed(boolean is_pressed) { is_pressed_ = is_pressed; }

    public Bitmap getBitmap() {
        return bitmap_;
    }
    public void setBitmap(Bitmap bitmap) { bitmap_ = bitmap; }

    public Bitmap getBitmapPressed() { return bitmap_pressed_; }
    public void setBitmapPressed(Bitmap bitmap_pressed) { bitmap_pressed_ = bitmap_pressed; }

    public Coordinate getCoordinate() {
        return coordinate_;
    }
    public void setCoordinate(Coordinate coordinate) {
        coordinate_ = new Coordinate(coordinate);
    }
    public void setCoordinate(int x, int y) {
        coordinate_ = new Coordinate(x, y);
    }

}
