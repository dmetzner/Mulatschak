package heroiceraser.mulatschak.drawableBasicObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    private boolean fitTextSizeForMaxWidth;
    private int maxWidth;
    private float borderPercent;
    private TextPaint borderPaint;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public MyTextField() {
        position = new Point(0, 0);
        textPaint = new TextPaint();
        textPaint.setTextSize(10);
        textPaint.setColor(Color.BLACK);
        text = "";
        borderPercent = 0;
        maxWidth = 0;
        visible = false;
        fitTextSizeForMaxWidth = true;
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public synchronized void draw(Canvas canvas) {
        if (visible) {
            if (fitTextSizeForMaxWidth && maxWidth != 0) {
                TextPaint tmpPaint = new TextPaint(textPaint);
                while (tmpPaint.measureText(text) > maxWidth) {
                    tmpPaint.setTextSize(tmpPaint.getTextSize() * 0.98f);
                }
                if (borderPercent != 0) {
                    TextPaint tmpBorder = new TextPaint(borderPaint);
                    tmpBorder.setTextSize(tmpPaint.getTextSize());
                    tmpBorder.setStrokeWidth(tmpPaint.getTextSize() * borderPercent);
                    tmpPaint.setStrokeWidth(textPaint.getTextSize() * borderPercent);
                    canvas.drawText(text, position.x, position.y, tmpBorder);
                }
                canvas.drawText(text, position.x, position.y, tmpPaint);
            } else {
                canvas.drawText(text, position.x, position.y, textPaint);
            }
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

    public void setBorder(int color, float borderPercent) {
        this.borderPercent = borderPercent;
        borderPaint = new TextPaint(textPaint);
        borderPaint.setColor(color);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(textPaint.getTextSize() * borderPercent);
        textPaint.setStrokeWidth(textPaint.getTextSize() * borderPercent);
        textPaint.setStyle(Paint.Style.FILL);
    }

    public void updateAlpha(int alpha) {
        textPaint.setAlpha(alpha);
        borderPaint.setAlpha(alpha);
    }

}
