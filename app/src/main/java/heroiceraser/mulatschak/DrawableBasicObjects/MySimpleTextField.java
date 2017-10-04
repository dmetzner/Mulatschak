package heroiceraser.mulatschak.DrawableBasicObjects;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 30.09.2017.
 */

public class MySimpleTextField {

    private boolean visible_;
    private TextPaint text_paint_;
    private int text_size_;
    private int text_color_;
    private String font_;

    private String text_;
    private Point position_;
    private Layout.Alignment alignment_;

    private Point size_;
    private Point max_size_;
    private StaticLayout static_layout_;

    private static final String DEFAULT_FONT = "DEFAULT_FONT";
    private int reduction_offset_;

    public MySimpleTextField() {
        position_ = new Point();
        size_ = new Point();
        max_size_ = new Point();
    }

    public void init(GameView view, int text_size, int text_color, Point position,
                     String text, Layout.Alignment alignment, int max_width, int max_height) {
        init(view, text_size, text_color, position, text,
                alignment, max_width, max_height, DEFAULT_FONT);
    }

    public void init(GameView view, int text_size, int text_color, Point position, String text,
                     Layout.Alignment alignment, int max_width, int max_height, String font) {

        this.text_size_ = text_size;
        this.text_color_ = text_color;
        this.font_ = font;
        this.text_paint_ = createTextPaint(view, text_size, text_color, font);

        this.text_ = text;
        this.alignment_ = alignment;
        this.position_ = position;
        this.max_size_.x = max_width;
        this.max_size_.y = max_height;

        size_.x = view.getController().getLayout().getScreenWidth();
        reduction_offset_ = (int) (view.getController().getLayout().getSectors().get(1).y * 0.05);
        initLayout();
        this.visible_ = true;
    }

    public void update() {
        initLayout();
    }

    public void draw(Canvas canvas) {
        if (isVisible()) {
            canvas.save();
            canvas.translate(position_.x, position_.y);
            static_layout_.draw(canvas);
            canvas.restore();
        }
    }

    public static TextPaint createTextPaint(GameView view, int text_size, int color, String font) {
        TextPaint text_paint = new TextPaint();
        if (!font.equals(DEFAULT_FONT)) {
            Typeface tf = Typeface.createFromAsset(view.getContext().getAssets(), font);
            text_paint.setTypeface(tf);
        }
        text_paint.setAntiAlias(true);
        text_paint.setTextSize(text_size);
        text_paint.setColor(color);
        return text_paint;
    }
    private void initLayout() {

        static_layout_ = new StaticLayout(text_, text_paint_,
                size_.x, alignment_, 1.0f, 0, false);

        while (static_layout_.getHeight() > max_size_.y) {
            reduceTextSize(text_paint_);
            position_.y = position_.y - reduction_offset_;
            static_layout_ = new StaticLayout(text_, text_paint_,
                    size_.x, alignment_, 1.0f, 0, false);
        }

        while (text_paint_.measureText(text_) > max_size_.x) {
            reduceTextSize(text_paint_);
            static_layout_ = new StaticLayout(text_, text_paint_,
                    size_.x, alignment_, 1.0f, 0, false);
        }

        size_.y = static_layout_.getHeight();
    }

    public static void reduceTextSize(TextPaint text_paint) {
        text_paint.setTextSize((int) (text_paint.getTextSize() * 0.9));
    }

    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //

    public boolean isVisible() {
        return visible_;
    }

    public void setVisible(boolean visible) {
        this.visible_ = visible;
    }

    public String getText() {
        return text_;
    }

    public void setText(String text) {
        this.text_ = text;
    }

    public Layout.Alignment getAlignment() {
        return alignment_;
    }

    public void setAlignment(Layout.Alignment alignment) {
        this.alignment_ = alignment;
    }

    public TextPaint getTextPaint() {
        return text_paint_;
    }

    public void setTextPaint(TextPaint text_paint_) {
        this.text_paint_ = text_paint_;
    }

    public int getTextSize() {
        return text_size_;
    }

    public void setTextSize(int text_size_) {
        this.text_size_ = text_size_;
    }

    public int getTextColor() {
        return text_color_;
    }

    public void setTextColor(int text_color_) {
        this.text_color_ = text_color_;
    }

    public String getFont() {
        return font_;
    }

    public void setFont(String font_) {
        this.font_ = font_;
    }

    public Point getPosition() {
        return position_;
    }

    public void setPosition(Point position_) {
        this.position_ = position_;
    }

    public Point getSize() {
        return size_;
    }

    public void setSize(Point size_) {
        this.size_ = size_;
    }

    public Point getMaxSize() {
        return max_size_;
    }

    public void setMaxSize(Point max_size_) {
        this.max_size_ = max_size_;
    }

    public StaticLayout getStaticLayout() {
        return static_layout_;
    }

    public void setStaticLayout(StaticLayout static_layout_) {
        this.static_layout_ = static_layout_;
    }

    public int getReductionOffset() {
        return reduction_offset_;
    }

    public void setReductionOffset(int reduction_offset_) {
        this.reduction_offset_ = reduction_offset_;
    }
}
