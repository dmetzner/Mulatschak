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
    private boolean bg_turned_on_;
    private Rect bg_rect_;
    private Paint bg_paint_;

    public TextField() {
        super();
    }

    public void init(GameView view, String text, int width) {
        init(view, text, width, false);
    }
    public void init(GameView view, String text, int width, boolean bg) {
        text_ = text;
        setWidth(width);
        bg_turned_on_ = bg;

        text_paint_ = new TextPaint();
        text_paint_.setAntiAlias(true);
        text_paint_.setTextSize(20 * view.getResources().getDisplayMetrics().density);
        text_paint_.setColor(Color.WHITE);

        bg_paint_ = new Paint();
        bg_paint_.setColor(Color.BLACK);

        static_layout_ = new StaticLayout(text_, text_paint_, width,
                Layout.Alignment.ALIGN_NORMAL, 1, 1, true);

        setHeight(static_layout_.getHeight());

        setVisible(false);
    }

    public void update(String text_, int max_height) {
        static_layout_ = new StaticLayout(text_, text_paint_, getWidth(),
                Layout.Alignment.ALIGN_NORMAL, 1, 1, true);

        TextPaint new_text_paint = text_paint_;
        int height = static_layout_.getHeight();
        while (max_height != -1 && height > max_height) {
            int new_text_size = (int) (new_text_paint.getTextSize() * 0.9);
            new_text_paint.setTextSize(new_text_size);
            static_layout_ = new StaticLayout(text_, new_text_paint, getWidth(),
                    Layout.Alignment.ALIGN_NORMAL, 1, 1, true);
            height = static_layout_.getHeight();
        }

    }

    public void draw(Canvas canvas, Point position) {
        if (isVisible()) {
            canvas.save();
            canvas.translate(position.x, position.y);
            if (bg_turned_on_) {
                bg_rect_ = new Rect(-0, -0, static_layout_.getWidth(), static_layout_.getHeight());
                canvas.drawRect(bg_rect_, bg_paint_);
            }
            static_layout_.draw(canvas);
            canvas.restore();
        }
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
