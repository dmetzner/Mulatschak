package heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBarWindows;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextPaint;

import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;

//----------------------------------------------------------------------------------------------
//  ButtonBarWindow
//
public class ButtonBarWindow {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private boolean visible_;
    private Rect background_;
    private Paint background_paint_;
    private ButtonBarWindowTitle title_;
    private MyButton closeButton;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public ButtonBarWindow() {
        background_ = new Rect();
        background_paint_ = new Paint();
        title_ = new ButtonBarWindowTitle();
        closeButton = new MyButton();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {

        GameLayout layout = view.getController().getLayout();

        background_.set(0, layout.getSectors().get(2).y,
            layout.getScreenWidth(), layout.getButtonBarPosition().y);

        background_paint_.setAntiAlias(true);
        background_paint_.setColor(Color.BLACK);
        background_paint_.setAlpha(230);
        background_paint_.setStyle(Paint.Style.FILL);

        Point closeButtonSize = new Point((int) (layout.getOnePercentOfScreenWidth() * 10),
                (int) (layout.getOnePercentOfScreenHeight() * 10));
        Point closeButtonPosition = new Point(layout.getScreenWidth() - (int)
                (closeButtonSize.x * 1.1), background_.top - (int) (layout.getOnePercentOfScreenHeight() * 1.5));
        // "" as image name results in no button image
        closeButton.init(view, closeButtonPosition, closeButtonSize, "", "X");
        TextPaint closeButtonTextPaint = closeButton.getTextPaint();
        closeButtonTextPaint.setTextScaleX(1.3f);
        closeButtonTextPaint.setColor(Color.LTGRAY);
        closeButton.changeTextPaint(closeButtonTextPaint);
    }

    //----------------------------------------------------------------------------------------------
    //  init
    //
    protected void titleInit(GameController controller, String text) {
        title_.init(controller, text);
    }


    //----------------------------------------------------------------------------------------------
    //  drawBackground
    //
    protected void drawBackground(Canvas canvas) {
        canvas.drawRect(background_, background_paint_);
        closeButton.draw(canvas);
    }


    //----------------------------------------------------------------------------------------------
    //  drawTitle
    //
    protected void drawTitle(Canvas canvas) {
        title_.draw(canvas);
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public boolean isVisible() {
        return visible_;
    }

    public void setVisible(boolean visible) {
        visible_ = visible;
    }

    protected MyButton getCloseButton() {
        return closeButton;
    }
}
