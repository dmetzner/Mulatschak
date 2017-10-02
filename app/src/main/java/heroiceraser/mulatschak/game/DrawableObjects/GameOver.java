package heroiceraser.mulatschak.game.DrawableObjects;

import android.graphics.Canvas;
import android.graphics.Point;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.DrawableBasicObjects.MySimpleTextField;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 08.09.2017.
 */

public class GameOver extends DrawableObject {

    private MyButton back_button_;

    public GameOver() {
        super();
        back_button_ = new MyButton();
    }

    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();

        // background
        setWidth(layout.getScreenWidth());
        setHeight(layout.getSectors().get(7).y - layout.getSectors().get(2).y);
        setPosition(layout.getSectors().get(2));
        setBitmap(HelperFunctions.loadBitmap(view, "game_over", getWidth(), getHeight()));

        // backbutton
        back_button_.init(view, layout.getSectors().get(6), new Point(200, 100), "lil_robo");

    }

    public void draw(Canvas canvas, GameController controller) {
        if (isVisible()) {
            // canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);
            controller.getStatistics().setVisible(true);
            back_button_.draw(canvas);
        }
    }
}
