package heroiceraser.mulatschak.game.DrawableObjects;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 27.08.2017.
 */

public class DealerButton extends DrawableObject{

    public DealerButton() {
        super();
    }

    public void init(GameView view) {
        setPosition(0, 0);
        setWidth(view.getController().getLayout().getDealerButtonSize());
        setHeight(getWidth());
        setBitmap(HelperFunctions.loadBitmap(view, "dealer_button" ,getWidth(), getHeight(), "drawable"));
        setVisible(true);
    }

}
