package heroiceraser.mulatschak.game;

import android.graphics.Point;

import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 08.08.2017.
 */

//----------------------------------------------------------------------------------------------
//  A Card consists of:
//      an Id
//      a bitmap -> front side Image of the card dd
//      a position -> where is the card right now
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
    //  Getter & Setter
    //
    public int getId() {
        return id_;
    }
    public void setId(int id) {
        id_ = id;
    }

    public Point getFixedPosition() { return fixed_position; }
    public void setFixedPosition(Point coordinate) { fixed_position = new Point(coordinate); }
    public void setFixedPosition(int x, int y) { fixed_position = new Point(x, y); }

    //----------------------------------------------------------------------------------------------
    //  initCard:
    //            loads, scales and sets bitmap for a card
    //
    public Card initCard(GameView view, String image_name, String package_name) {
        calculateCardSize(view);
        setBitmap(HelperFunctions.loadBitmap(view, image_name, getWidth(), getHeight(), package_name));
        return this;
    }

    //----------------------------------------------------------------------------------------------
    //  calculateCardSize:
    //                      calculates card size based on the screen width
    //
    private void calculateCardSize(GameView view) {

        final double HEIGHT_FACTOR = 1.28;
        double screen_width = view.getController().getLayout().getScreenWidth();
        double max_cards_per_hand = view.getController().getLogic().getMaxCardsPerHand();

        setWidth((int) (screen_width / (max_cards_per_hand + 1)) );
        setHeight( (int) (getWidth() * 1.28) );
    }
}
