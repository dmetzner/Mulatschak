package heroiceraser.mulatschak.game;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * Created by Daniel Metzner on 26.08.2017.
 */

public abstract class DrawableObject {

    private boolean visible_;
    private Bitmap bitmap_;
    private Point position_;
    private int width_;
    private int height_;

    public DrawableObject() {
        width_ = 0;
        height_ = 0;
        position_ = null;
        bitmap_ = null;
        visible_ = false;
    }

    public boolean isVisible() {
        return this.visible_;
    }

    public void setVisible(boolean visible) {
        this.visible_ = visible;
    }

    public Bitmap getBitmap() {
        return this.bitmap_;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap_ = bitmap;
    }

    public Point getPosition() {
        return this.position_;
    }

    public void setPosition(int x, int y) {
        this.position_ = new Point(x, y);
    }

    public void setPosition(Point position) {
        this.position_ = new Point(position);
    }

    public void setWidth(int width) {
        this.width_ = width;
    }

    public int getWidth() {
        return this.width_;
    }

    public void setHeight(int height) {
        this.height_ = height;
    }
    public int getHeight() {
        return this.height_;
    }

}
