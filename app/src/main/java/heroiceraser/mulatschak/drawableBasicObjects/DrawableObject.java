package heroiceraser.mulatschak.drawableBasicObjects;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

//--------------------------------------------------------------------------------------------------
//  simple base class for drawable objects
//
public abstract class DrawableObject {

    //----------------------------------------------------------------------------------------------
    //  Member variables
    //
    private boolean visible;
    private Bitmap bitmap;
    private Rect rect;
    private Paint rectPaint;
    private Point position;
    private int width;
    private int height;

    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point position) {
        this.position = new Point(position);
    }

    public void setPosition(int x, int y) {
        this.position = new Point(x, y);
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public Paint getRectPaint() {
        return rectPaint;
    }

    public void setRectPaint(Paint rectPaint) {
        this.rectPaint = rectPaint;
    }

}
