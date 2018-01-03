package heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar.Windows;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public ButtonBarWindow() {
        background_ = new Rect();
        background_paint_ = new Paint();
        title_ = new ButtonBarWindowTitle();
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

    }

    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void titleInit(GameController controller, String text) {
        title_.init(controller, text);
    }


    //----------------------------------------------------------------------------------------------
    //  drawBackground
    //
    public void drawBackground(Canvas canvas) {
        canvas.drawRect(background_, background_paint_);
    }


    //----------------------------------------------------------------------------------------------
    //  drawTitle
    //
    public void drawTitle(Canvas canvas) {
        title_.draw(canvas);
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public boolean isVisible() {
        return visible_;
    }

    public void switchVisibility() {
        visible_ = !visible_;
    }

    public void setVisible(boolean visible) {
        visible_ = visible;
    }
}
