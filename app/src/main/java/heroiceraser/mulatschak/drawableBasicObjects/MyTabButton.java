package heroiceraser.mulatschak.drawableBasicObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


public class MyTabButton extends MyButton {

    private boolean active;
    private Rect activeIndicator;
    private Paint activeIndicatorPaint;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void createActiveIndicator() {
        activeIndicator = new Rect();
        activeIndicator.set(
                getPosition().x,
                (int) (getPosition().y + getHeight() * 93 / 100.0),
                getPosition().x + getWidth(),
                getPosition().y + getHeight());
        activeIndicatorPaint = new Paint();
        activeIndicatorPaint.setColor(Color.WHITE);
    }

    @Override
    public synchronized void draw(Canvas canvas) {
        super.draw(canvas);

        if (active && null != activeIndicator) {
            canvas.drawRect(activeIndicator, activeIndicatorPaint);
        }
    }
}
