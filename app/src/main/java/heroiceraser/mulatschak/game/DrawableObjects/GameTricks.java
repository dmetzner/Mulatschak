package heroiceraser.mulatschak.game.DrawableObjects;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 27.08.2017.
 */

public class GameTricks extends DrawableObject{

    public GameTricks() {
        super();
    }

    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();
        setPosition(0, 0);
        setHeight(layout.getScreenHeight() - layout.getButtonBarHeight());
        setWidth(layout.getScreenWidth());
        setBitmap(HelperFunctions.loadBitmap(view, "statistics_background",
                getWidth(), getHeight(), "drawable"));
    }
}
