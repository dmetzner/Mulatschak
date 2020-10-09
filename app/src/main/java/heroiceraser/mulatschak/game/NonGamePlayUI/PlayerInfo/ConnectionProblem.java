package heroiceraser.mulatschak.game.NonGamePlayUI.PlayerInfo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.drawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.utils.BitmapMethodes;


public class ConnectionProblem extends DrawableObject {

    Paint circlePaint;


    public ConnectionProblem() {
        super();
    }

    public void init(GameView view) {
        setWidth(view.getController().getLayout().getPlayerInfoSize().x);
        setHeight(getWidth());
        setPosition(view.getController().getLayout().getCenter().x - getWidth() / 2,
                view.getController().getLayout().getCenter().y - getHeight() / 2);
        setBitmap(BitmapMethodes.loadBitmap(view, "wlan", getWidth(), getHeight()));

        circlePaint = new Paint();
        circlePaint.setColor(view.getResources().getColor(R.color.my_dark_gray));
        circlePaint.setAntiAlias(true);
        setVisible(false);
    }

    public synchronized void draw(Canvas canvas, GameController controller) {
        if (isVisible() && getBitmap() != null) {


            long time = System.currentTimeMillis();

            int alphaTime = (int) (time % 2048);

            double percentage = (alphaTime * 100.0) / 2048;

            int alpha = (int) percentage * 255;

            if (alpha > 125) {
                alpha = 125 - (alpha - 125);
            }

            circlePaint.setAlpha(alpha);

            Bitmap bmp = getBitmap();

            canvas.drawCircle(getPosition().x + getWidth() / 2,
                    getPosition().y + getHeight() / 2, (getHeight() / 1.5f), circlePaint);
            canvas.drawBitmap(bmp, getPosition().x, getPosition().y, null);

            controller.getView().postInvalidate();
        }
    }

}