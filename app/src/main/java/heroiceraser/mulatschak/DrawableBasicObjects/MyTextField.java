package heroiceraser.mulatschak.DrawableBasicObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.text.TextPaint;


//----------------------------------------------------------------------------------------------
//  My Text Field Class
//
public class MyTextField {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private String text;
    private TextPaint textPaint;
    private Point position;
    private boolean visible;
    private int maxWidth;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public MyTextField() {
        position = new Point(0, 0);
        textPaint = new TextPaint();
        textPaint.setTextSize(10);
        textPaint.setColor(Color.BLACK);
        text = "";
        visible = false;
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas) {
        if (visible) {
            TextPaint tmpPaint = new TextPaint(textPaint);
            while (tmpPaint.measureText(text) > maxWidth) {
                tmpPaint.setTextSize(tmpPaint.getTextSize() * 0.95f);
            }
            canvas.drawText(text, position.x, position.y, tmpPaint);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TextPaint getTextPaint() {
        return textPaint;
    }

    public void setTextPaint(TextPaint textpaint) {
        this.textPaint = textpaint;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }
}
