package heroiceraser.mulatschak.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import heroiceraser.mulatschak.helpers.Coordinate;

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
public class Card {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private int id_;
    private Bitmap bitmap_;
    private Coordinate position_;
    private Coordinate fixed_position;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public Card() {
        id_ = -1;
        bitmap_ = null;
        position_ = null;
        fixed_position = null;
    }
    public Card(int id) {
        id_ = id;
        bitmap_ = null;
        position_ = null;
        fixed_position = null;
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public int getId() {
        return id_;
    }
    public void setId(int id) { id_ = id; }

    public Bitmap getBitmap() {
        return bitmap_;
    }
    public void setBitmap(Bitmap bitmap) {
        bitmap_ = bitmap;
    }

    public Coordinate getPosition() { return position_; }
    public void setPosition(Coordinate coordinate) {position_ = new Coordinate(coordinate); }
    public void setPosition(int x, int y) {position_ = new Coordinate(x, y); }

    public Coordinate getFixedPosition() { return fixed_position; }
    public void setFixedPosition(Coordinate coordinate) { fixed_position = new Coordinate(coordinate); }
    public void setFixedPosition(int x, int y) { fixed_position = new Coordinate(x, y); }


    //----------------------------------------------------------------------------------------------
    //  initCard:
    //            loads, scales and sets bitmap for a card
    //
    public Card initCard(GameView view, String image_name, String package_name) {

        // load Bitmap
        int resourceId = view.getResources()
                .getIdentifier(image_name, package_name, view.getContext().getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(view.getContext().getResources(), resourceId);

        // scale Bitmap
        int width = view.getController().getLayout().getCardWidth();
        int height = view.getController().getLayout().getCardHeight();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

        setBitmap(scaledBitmap);
        assert bitmap != null : "Card: 'bitmap == null' Error";
        return this;
    }
}
