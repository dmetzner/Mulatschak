package heroiceraser.mulatschak.game.NonGamePlayUI.SideBorders;

import android.graphics.Canvas;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;


//----------------------------------------------------------------------------------------------
//  Side Borders
//
public class SideBorders extends DrawableObject{

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public SideBorders() {
        super();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        setWidth(view.getController().getLayout().getButtonBarWidth());
        setHeight(view.getController().getLayout().getButtonBarHeight());
        setPosition(view.getController().getLayout().getSectors().get(2));
        setBitmap(HelperFunctions.loadBitmap(view, "side_borders", getWidth(), getHeight() * 4));
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas) {
        canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);
    }
}
