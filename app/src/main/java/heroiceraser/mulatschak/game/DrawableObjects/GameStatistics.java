package heroiceraser.mulatschak.game.DrawableObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import heroiceraser.mulatschak.DrawableBasicObjects.Button;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.DrawableObjects.ButtonBar;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 23.08.2017.
 */

public class GameStatistics extends DrawableObject{

    private TextPaint textPaint;
    private String text;

    public GameStatistics() {
        super();
    }

    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();
        setPosition(0, layout.getSectors().get(2).y);
        setHeight(layout.getScreenHeight() - layout.getButtonBarHeight() - layout.getRoundInfoSize().y);
        setWidth(layout.getScreenWidth());
        setBitmap(HelperFunctions.loadBitmap(view, "statistics_background", getWidth(), getHeight()));

        text = "Player X:   XXX";
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(40 * view.getResources().getDisplayMetrics().density);
        textPaint.setColor(Color.GREEN);
    }

    public void draw(Canvas canvas, GameController controller) {
        if (isVisible()) {
            canvas.drawBitmap(getBitmap(),
                    getPosition().x,
                    getPosition().y, null);

            int width = (int) textPaint.measureText(text);
            int x = (int) ((controller.getLayout().getScreenWidth() - width) / 2.0);
            int y = (int) getPosition().y + controller.getLayout().getSectors().get(1).y;
            for (int i = 0; i < controller.getAmountOfPlayers(); i++) {
                canvas.save();
                canvas.translate(x, y);
                text = "Player " + (i + 1);
                text += ":     " + controller.getPlayerById(i).getLives();
                StaticLayout staticLayout = new StaticLayout(text, textPaint,
                        width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
                staticLayout.draw(canvas);
                canvas.restore();
                y += (int) (textPaint.getTextSize() * 1.5);
            }
        }
    }
}
