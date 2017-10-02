package heroiceraser.mulatschak.DrawableBasicObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.widget.PopupWindow;

import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 30.09.2017.
 */

public class MySimpleTextField {

    private TextPaint text_paint_;
    private Point position_;
    private StaticLayout static_layout_;
    private String default_font;

    public MySimpleTextField() {
        default_font = "Ariel";
    }

    public void init(GameView view, int text_size, int text_color, Point position,
                     String text, Layout.Alignment alignment, int max_width, int max_height) {
        init(view, text_size, text_color, position, text,
                alignment, max_width, max_height, default_font);
    }

    public void init(GameView view, int text_size, int text_color, Point position, String text,
                     Layout.Alignment alignment, int max_width, int max_height, String font) {

        text_paint_ = createTextPaint(view, text_size, text_color, font);

        this.position_ = position;

        initLayout(view.getController().getLayout(), text, alignment, max_width, max_height);
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(position_.x, position_.y);
        static_layout_.draw(canvas);
        canvas.restore();
    }

    private TextPaint createTextPaint(GameView view, int text_size, int color, String font) {
        Typeface tf = Typeface.createFromAsset(view.getContext().getAssets(), font);
        TextPaint text_paint = new TextPaint();
        text_paint.setTypeface(tf);
        text_paint.setAntiAlias(true);
        text_paint.setTextSize(text_size);
        text_paint.setColor(color);
        return text_paint;
    }
    private void initLayout(GameLayout layout, String text,
                            Layout.Alignment alignment, int max_width, int max_height) {

        static_layout_ = new StaticLayout(text, text_paint_,
                layout.getScreenWidth(), alignment, 1.0f, 0, false);

        while (static_layout_.getHeight() > max_height) {
            reduceTextSize(text_paint_);
            position_.y = position_.y - (int ) (layout.getSectors().get(1).y * 0.05);
            static_layout_ = new StaticLayout(text, text_paint_,
                    layout.getScreenWidth(), alignment, 1.0f, 0, false);
        }

        while (text_paint_.measureText(text) > max_width) {
            reduceTextSize(text_paint_);
            static_layout_ = new StaticLayout(text, text_paint_,
                    layout.getScreenWidth(), alignment, 1.0f, 0, false);
        }
    }

    private void reduceTextSize(TextPaint text_paint) {
        text_paint.setTextSize((int) (text_paint.getTextSize() * 0.9));
    }

}
