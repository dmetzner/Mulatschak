package heroiceraser.mulatschak.game.NonGamePlayUI.BottomBar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import heroiceraser.mulatschak.drawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;


public class ButtonBarWindow extends DrawableObject {

    private ButtonBarWindowTitle title;
    private ButtonBarWindowCloseButton closeButton;
    private BottomBar bottomBar;


    public ButtonBarWindow() {
        title = new ButtonBarWindowTitle();
        closeButton = new ButtonBarWindowCloseButton();
    }

    public void init(@NonNull GameView view) {
        GameLayout layout = view.getController().getLayout();
        setRect(new Rect(0, 0, layout.getScreenWidth(), layout.getButtonBarPosition().y));
        initBackgroundPaint();
        closeButton.init(view);
    }

    private void initBackgroundPaint() {
        Paint backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setAlpha(230);
        backgroundPaint.setStyle(Paint.Style.FILL);
        setRectPaint(backgroundPaint);
    }

    protected void titleInit(GameController controller, String text) {
        title.init(controller, text);
    }

    protected synchronized void drawBackground(@NonNull Canvas canvas) {
        canvas.drawRect(getRect(), getRectPaint());
        closeButton.draw(canvas);
    }

    protected synchronized void drawTitle(Canvas canvas) {
        title.draw(canvas);
    }

    protected ButtonBarWindowCloseButton getCloseButton() {
        return closeButton;
    }
}
