package heroiceraser.mulatschak.game.DrawableObjects;

import android.graphics.Point;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 08.08.2017.
 */

//----------------------------------------------------------------------------------------------
//  A Card is a drawable object with
//      an Id
//      a bitmap -> front side Image of the card dd
//      a fixed position -> where should the card be
//
public class Card extends DrawableObject {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private int id_;
    private Point fixed_position;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public Card() {
        super();
        id_ = -1;
        fixed_position = null;
    }
    public Card(int id) {
        super();
        id_ = id;
        fixed_position = null;
    }

    //----------------------------------------------------------------------------------------------
    //  initCard:
    //            loads, scales and sets bitmap for a card
    //
    public Card initCard(GameView view, String image_name, String package_name) {
        setWidth(view.getController().getLayout().getCardWidth());
        setHeight(view.getController().getLayout().getCardHeight());
        setBitmap(HelperFunctions.loadBitmap(view, image_name, getWidth(), getHeight(), package_name));
        setVisible(true);
        return this;
    }

    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public int getId() {
        return id_;
    }
    public void setId(int id) {
        id_ = id;
    }

    public Point getFixedPosition() {
        return fixed_position;
    }
    public void setFixedPosition(Point coordinate) {
        fixed_position = new Point(coordinate);
    }
    public void setFixedPosition(int x, int y) {
        fixed_position = new Point(x, y);
    }
}
