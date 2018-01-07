package heroiceraser.mulatschak.DrawableBasicObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.TextPaint;


//--------------------------------------------------------------------------------------------------
//  My Radio Button Class
//                          -> ToDo , can be improved but works for now
//
public class MyRadioButton {

    private int id_;
    private TextPaint text_paint_;
    private String text_;

    private Point position_;
    private int radius_;
    private int border_size_;

    private Paint border_paint_;
    private Paint fill_paint_;
    private Paint border_paint_disabled_;
    private Paint fill_paint_disabled_;
    private Paint border_paint_checked_;
    private Paint fill_paint_checked_;

    private boolean visible_;
    private boolean pressed_;
    private boolean checked_;
    private boolean enabled_;

    public MyRadioButton() {
        super();
        position_ = new Point(0,0);
        text_paint_ = new TextPaint();
        border_paint_ = new Paint();
        border_paint_checked_ = new Paint();
        border_paint_disabled_ = new Paint();
        fill_paint_ = new Paint();
        fill_paint_checked_ = new Paint();
        fill_paint_disabled_ = new Paint();
        initDefault();
    }

    private void initDefault() {
        radius_ = 10;
        text_ = "Radio Button";
        setEnabled(true);
        setVisible(true);

        setChecked(false);
        setBorderSize(2);
        border_paint_.setStyle(Paint.Style.STROKE);
        border_paint_.setAntiAlias(true);
        border_paint_.setColor(Color.BLACK);
        border_paint_disabled_.setStyle(Paint.Style.STROKE);
        border_paint_disabled_.setAntiAlias(true);
        border_paint_.setColor(Color.BLACK);
        border_paint_checked_.setStyle(Paint.Style.STROKE);
        border_paint_checked_.setAntiAlias(true);
        border_paint_.setColor(Color.BLACK);
        fill_paint_.setStyle(Paint.Style.FILL);
        fill_paint_.setAntiAlias(true);
        fill_paint_.setColor(Color.WHITE);
        fill_paint_.setAlpha(10);
        fill_paint_disabled_.setStyle(Paint.Style.FILL);
        fill_paint_disabled_.setAntiAlias(true);
        fill_paint_disabled_.setColor(Color.WHITE);
        fill_paint_disabled_.setAlpha(10);
        fill_paint_checked_.setStyle(Paint.Style.FILL);
        fill_paint_checked_.setAntiAlias(true);
        fill_paint_checked_.setColor(Color.WHITE);
        text_paint_.setTextAlign(Paint.Align.CENTER);
        text_paint_.setAntiAlias(true);
        text_paint_.setTextSize(radius_);
        text_paint_.setTextSize(Color.BLACK);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas) {
        if (!visible_) {
            return;
        }
        if (!enabled_) {
            canvas.drawCircle(position_.x, position_.y, radius_, fill_paint_disabled_);
            canvas.drawCircle(position_.x, position_.y, radius_, border_paint_disabled_);
        }
        else if (checked_) {
            canvas.drawCircle(position_.x, position_.y, radius_, fill_paint_checked_);
            canvas.drawCircle(position_.x, position_.y, radius_, border_paint_checked_);
        }
        else  {
            canvas.drawCircle(position_.x, position_.y, radius_, fill_paint_);
            canvas.drawCircle(position_.x, position_.y, radius_, border_paint_);
        }

        canvas.drawText(text_, position_.x, position_.y - radius_ * 2.5f, text_paint_);

    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public void setPosition(Point position) {
        position_ = new Point(position);
    }

    public Point getPosition() {
        return position_;
    }

    public void setRadius(int radius) {
        radius_ = radius;
        text_paint_.setTextSize((int) (radius * 1.5));
    }

    public int getRadius() {
        return  radius_;
    }

    public void setVisible(boolean visible) {
        visible_ = visible;
    }

    public boolean isVisible() {
        return visible_;
    }

    public void setPressed(boolean pressed) {
        pressed_ = pressed;
    }

    public boolean isPressed() {
        return pressed_;
    }

    public boolean isChecked() {
        return checked_;
    }

    public void setChecked(boolean checked_) {
        this.checked_ = checked_;
    }

    public boolean isEnabled() {
        return enabled_;
    }

    public void setEnabled(boolean enabled_) {
        this.enabled_ = enabled_;
    }
    
    public void setBorderSize(int border_size) {
        if (border_size < 1) {
            border_size = 1;
        }
        border_size_ = border_size;
        fill_paint_.setStrokeWidth(border_size_);
        fill_paint_checked_.setStrokeWidth(border_size_);
        fill_paint_disabled_.setStrokeWidth(border_size_);
        border_paint_.setStrokeWidth(border_size_);
        border_paint_checked_.setStrokeWidth(border_size_);
        border_paint_disabled_.setStrokeWidth(border_size_);
    }
    
    public Paint getBorderPaint() {
        return border_paint_;
    }
    public Paint getBorderPaintChecked() {
        return border_paint_checked_;
    }
    public Paint getBorderPaintDisabled() {
        return border_paint_disabled_;
    }
    public Paint getFillpaint() {
        return fill_paint_;
    }
    public Paint getFillPaintDisabled() {
        return fill_paint_disabled_;
    }
    public Paint getFillPaintChecked() {
        return fill_paint_checked_;
    }

    public TextPaint getTextPaint() {
        return text_paint_;
    }

    public void setText(String text) {
        text_ = text;
    }

    public String getText() {
        return text_;
    }
    public int getId() {
        return id_;
    }

    public void setId(int id) {
        this.id_ = id;
    }
}
