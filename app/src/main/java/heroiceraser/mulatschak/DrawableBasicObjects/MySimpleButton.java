package heroiceraser.mulatschak.DrawableBasicObjects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.text.TextPaint;

import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 02.01.2018.
 */

public class MySimpleButton extends DrawableObject{


    //----------------------------------------------------------------------------------------------
    // Member Variables
    //
    private boolean enabled_;
    private boolean pressed_;

    // Border
    private int border_padding_;
    private Rect border_rect_;
    private Paint border_paint_;

    // Background4Player0Animations
    private Rect background_rect_;
    private Paint background_paint_;

    // Text
    private TextPaint textPaint_;
    private String text_;


    //----------------------------------------------------------------------------------------------
    // Constructor
    //
    public MySimpleButton() {
        super();
        enabled_ = true;
        pressed_= false;
        border_padding_ = 0;
        border_rect_ = new Rect();
        border_paint_ = new Paint();
        background_rect_ = new Rect();
        background_paint_ = new Paint();
        text_ = "";
        textPaint_ = new TextPaint();
    }


    public void init(GameView view, Point position, int width, int height, String text) {
        setPosition(position);
        setWidth(width);
        setHeight(height);
        text_ = text;

        // - default settings -

        // border
        border_rect_.set(position.x, position.y,
                position.x + width, position.y + height);
        border_padding_ = (width + height) / 2 / 100;
        if (border_padding_ < 1) {
            border_padding_ = 1;
        }
        border_paint_.setStyle(Paint.Style.FILL);
        border_paint_.setColor(view.getResources().getColor(R.color.game_play_button_border));
        border_paint_.setAntiAlias(true);


        // background
        background_rect_.set(position.x + border_padding_,
                position.y + border_padding_,
                position.x + width - border_padding_,
                position.y + height - border_padding_);

        background_paint_.setStyle(Paint.Style.FILL);
        background_paint_.setAntiAlias(true);
        background_paint_.setColor(view.getResources().getColor(R.color.game_play_button_background));

        /*
        RadialGradient gradient = new RadialGradient(200, 200, 200, 0xFFFFFFFF,
                0xFF000000, android.graphics.Shader.TileMode.CLAMP);
        background_paint_.setDither(true);
        background_paint_.setShader(gradient);
        */

        setVisible(true);
    }

    //----------------------------------------------------------------------------------------------
    // Draw
    //
    public void draw(Canvas canvas) {
        if (!isVisible()) {
            return;
        }

        if (!IsEnabled()) {
            // bitmap = getBitmapDisabled();
        }
        else if (IsPressed()) {
         //   bitmap = getBitmapPressed();
        }

        canvas.drawRect(border_rect_, border_paint_);
        canvas.drawRect(background_rect_, background_paint_);
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

}
