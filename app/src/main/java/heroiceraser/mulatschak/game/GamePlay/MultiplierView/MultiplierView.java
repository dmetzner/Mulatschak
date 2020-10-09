package heroiceraser.mulatschak.game.GamePlay.MultiplierView;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.TextPaint;

import heroiceraser.mulatschak.drawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;


public class MultiplierView extends DrawableObject {

    private TextPaint textPaint;
    private Point position;
    private String text = "";
    private GameLayout layout;

    public MultiplierView(GameView view) {

        layout = view.getController().getLayout();
        position = new Point(
                layout.getScreenWidth() / 100 * 18,
                layout.getLengthOnVerticalGrid(75)
        );

        initTextPaint();

        updateMultiplier(view.getController().getLogic().getMultiplier());
        setVisible(true);
    }

    private void initTextPaint() {
        textPaint = new TextPaint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(layout.getLengthOnVerticalGrid(50));
    }


    public void updateMultiplier(int multiplier) {
        text = multiplier > 1 ? "x" + multiplier : "";
    }


    public synchronized void draw(Canvas canvas) {
        if (isVisible() && !text.equals("")) {
            canvas.drawText(text, position.x, position.y, textPaint);
        }
    }
}
