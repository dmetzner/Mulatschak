package heroiceraser.mulatschak.DrawableBasicObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 01.09.2017.
 */

public class TextField extends DrawableObject{

    private StaticLayout static_layout_;
    private TextPaint text_paint_;
    private String text_;

    public TextField() {
        super();
    }

    public void init(GameView view, String text, int width) {
        text_ = text;

        text_paint_ = new TextPaint();
        text_paint_.setAntiAlias(true);
        text_paint_.setTextSize(16 * view.getResources().getDisplayMetrics().density);
        text_paint_.setColor(Color.BLACK);

        static_layout_ = new StaticLayout(text_, text_paint_, width,
                Layout.Alignment.ALIGN_NORMAL, 1, 1, true);

        setWidth(width);
        setHeight(static_layout_.getHeight());

    }

    public void update(String text_) {
        static_layout_ = new StaticLayout(text_, text_paint_, getWidth(),
                Layout.Alignment.ALIGN_NORMAL, 1, 1, true);
    }

    public void draw(Canvas canvas, Point position) {
        canvas.save();
        canvas.translate(position.x, position.y);
        static_layout_.draw(canvas);
        canvas.restore();
    }

    public TextPaint getTextPaint() {
        return text_paint_;
    }

    public String getText() {
        return text_;
    }

    public StaticLayout getStaticLayout() {
        return static_layout_;
    }
}
