package heroiceraser.mulatschak.game.NonGamePlayUI.Background;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import heroiceraser.mulatschak.game.GameLayout;


public class Background {

    private Rect background;
    private Paint paint;

    public Background() {
        background = new Rect();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAlpha(20);
    }

    public void init(GameLayout layout) {
        background.set(0, 0, layout.getScreenWidth(), layout.getLengthOnVerticalGrid(900));
    }

    public synchronized void draw(Canvas canvas) {
        canvas.drawRect(background, paint);
    }
}
