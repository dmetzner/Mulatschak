package heroiceraser.mulatschak.game.NonGamePlayUI.BottomBar;

import android.graphics.Color;
import android.graphics.Point;
import android.text.TextPaint;

import heroiceraser.mulatschak.drawableBasicObjects.MyButton;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;

public class ButtonBarWindowCloseButton extends MyButton {

    public void init(GameView view) {

        GameLayout layout = view.getController().getLayout();

        Point closeButtonSize = new Point(
                (int) (layout.getOnePercentOfScreenWidth() * 10),
                (int) (layout.getOnePercentOfScreenHeight() * 10)
        );

        Point closeButtonPosition = new Point(
                layout.getScreenWidth() - (int) (closeButtonSize.x * 1.1),
                0
        );

        init(view, closeButtonPosition, closeButtonSize, "", "X");

        TextPaint closeButtonTextPaint = getTextPaint();
        closeButtonTextPaint.setTextScaleX(1.3f);
        closeButtonTextPaint.setColor(Color.LTGRAY);
        changeTextPaint(closeButtonTextPaint);
    }
}
