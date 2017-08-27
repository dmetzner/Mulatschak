package heroiceraser.mulatschak.game.DrawableObjects;

import android.graphics.Bitmap;

import heroiceraser.mulatschak.DrawableBasicObjects.Button;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.DrawableObjects.ButtonBar;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 23.08.2017.
 */

public class GameStatistics extends DrawableObject{

    public GameStatistics() {
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
