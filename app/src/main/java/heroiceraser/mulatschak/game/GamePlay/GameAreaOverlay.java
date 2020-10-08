package heroiceraser.mulatschak.game.GamePlay;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import heroiceraser.mulatschak.game.GameLayout;


public class GameAreaOverlay {

    private final int ALPHA_MAX = 150;
    private Rect background;
    private Paint backgroundPaint;

    public GameAreaOverlay(@NonNull GameLayout layout) {
        background = new Rect();
        backgroundPaint = new Paint();
        background.set(0, 0, layout.getScreenWidth(), layout.getLengthOnVerticalGrid(900));
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setAlpha(ALPHA_MAX);
    }

    public void updateAlpha(int alpha) {
        if (alpha > ALPHA_MAX) {
            alpha = ALPHA_MAX;
        }
        backgroundPaint.setAlpha(alpha);
    }

    public synchronized void draw(Canvas canvas) {
        canvas.drawRect(background, backgroundPaint);
    }

    public Rect getBackground() {
        return background;
    }
}
