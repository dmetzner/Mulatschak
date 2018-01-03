package heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar;


import android.graphics.Canvas;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 22.12.2017.
 */

public class ButtonBarDecoration extends DrawableObject{

    public ButtonBarDecoration() {
        super();
    }

    public void init(GameView view) {
        setWidth(view.getController().getLayout().getButtonBarWidth());
        setHeight(view.getController().getLayout().getButtonBarHeight());
        setPosition(view.getController().getLayout().getButtonBarPosition().x,
                view.getController().getLayout().getButtonBarPosition().y - getHeight());
        setBitmap(HelperFunctions.loadBitmap(view, "button_bar_decoration", getWidth(), getHeight()));
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);
    }


}
