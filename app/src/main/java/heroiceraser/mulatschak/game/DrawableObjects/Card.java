package heroiceraser.mulatschak.game.DrawableObjects;

import android.graphics.Point;
import android.util.Log;

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
    public Card initCard(GameView view, String image_name) {
        setWidth(view.getController().getLayout().getCardWidth());
        setHeight(view.getController().getLayout().getCardHeight());
        setBitmap(HelperFunctions.loadBitmap(view, image_name, getWidth(), getHeight()));
        setVisible(false);
        return this;
    }

    public static Card copy(Card card_to_copy) {
        Card tmp = new Card();
        if (card_to_copy != null) {
            tmp.setId(card_to_copy.getId());
            tmp.setFixedPosition(card_to_copy.getFixedPosition());
            tmp.setPosition(card_to_copy.getPosition());
            tmp.setWidth(card_to_copy.getWidth());
            tmp.setHeight(card_to_copy.getHeight());
            tmp.setBitmap(card_to_copy.getBitmap().copy(card_to_copy.getBitmap().getConfig(), true));
            tmp.setVisible(card_to_copy.isVisible());
        }
        return tmp;
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
