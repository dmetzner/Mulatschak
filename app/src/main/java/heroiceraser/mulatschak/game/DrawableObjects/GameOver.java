package heroiceraser.mulatschak.game.DrawableObjects;

import android.graphics.Canvas;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 08.09.2017.
 */

public class GameOver extends DrawableObject {

    public GameOver() {
        super();
    }

    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();
        setWidth(layout.getScreenWidth());
        setHeight(layout.getSectors().get(7).y - layout.getSectors().get(2).y);
        setPosition(layout.getSectors().get(2));
        setBitmap(HelperFunctions.loadBitmap(view, "game_over", getWidth(), getHeight()));
    }

    public void draw(Canvas canvas, GameController controller) {
        if (isVisible()) {
            canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);
            controller.getStatistics().setVisible(true);
        }
    }
}
