package heroiceraser.mulatschak.game.NonGamePlayUI.SideBorders;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 22.12.2017.
 */

public class SideBorders extends DrawableObject{

    public SideBorders() {
        super();
    }

    public void init(GameView view) {
        setWidth(view.getController().getLayout().getButtonBarWidth());
        setHeight(view.getController().getLayout().getButtonBarHeight());
        setPosition(view.getController().getLayout().getSectors().get(2));
        setBitmap(HelperFunctions.loadBitmap(view, "side_borders", getWidth(), getHeight() * 4));
 }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);
    }
}
