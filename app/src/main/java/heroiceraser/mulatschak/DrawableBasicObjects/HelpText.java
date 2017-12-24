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
 * Created by Daniel Metzner on 30.08.2017.
 */

public class HelpText extends DrawableObject{

    private float rect_rx_;
    private float rect_ry_;
    private RectF background_rect_;
    private Paint bg_color_;
    private StaticLayout static_layout_;
    private TextPaint text_paint_;
    private String text_;

    public HelpText() {
        super();
    }

    public void init(GameView view, String text, int width) {
        init(view, text, width, -1);
    }

    public void init(GameView view, String text, int width, int max_height) {
        text_ = text;

        text_paint_ = new TextPaint();
        text_paint_.setAntiAlias(true);
        text_paint_.setTextSize(22 * view.getResources().getDisplayMetrics().density);
        text_paint_.setColor(Color.WHITE);

        bg_color_ = new Paint();
        bg_color_.setColor(Color.DKGRAY);
        bg_color_.setAlpha((int) (256.0 * (1-0.05)));

        static_layout_ = new StaticLayout(text_, text_paint_, width,
                Layout.Alignment.ALIGN_CENTER, 1, 1, true);

        TextPaint new_text_paint = text_paint_;
        while (max_height != -1 && static_layout_.getHeight() > max_height) {
            new_text_paint.setTextSize((int) (new_text_paint.getTextSize() / 1.5));
            static_layout_ = new StaticLayout(text_, new_text_paint, width,
                    Layout.Alignment.ALIGN_CENTER, 1, 1, true);
        }

        int padding = (int) ((view.getController().getLayout().getScreenWidth() / 8.0 * 0.5));
        Rect rect = new Rect(0 - padding, 0 - padding,
                static_layout_.getWidth() + padding,  static_layout_.getHeight() + padding);
        background_rect_ = new RectF(rect);
        rect_rx_ = static_layout_.getWidth() / 5.f;
        rect_ry_ = static_layout_.getHeight() / 2.f;
    }

    public void draw(Canvas canvas, Point position) {
        canvas.save();
        canvas.translate(position.x, position.y);
        canvas.drawRoundRect(background_rect_, rect_rx_, rect_ry_, bg_color_);
        static_layout_.draw(canvas);
        canvas.restore();
    }

    public TextPaint getTextPaint() {
        return text_paint_;
    }

    public String getText() {
        return text_;
    }

    public Paint getBgColor() {
        return bg_color_;
    }

    public RectF getBackgroundRect() {
        return background_rect_;
    }

    public StaticLayout getStaticLayout() {
        return static_layout_;
    }
}
