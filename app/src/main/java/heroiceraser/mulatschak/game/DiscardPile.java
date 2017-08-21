package heroiceraser.mulatschak.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.helpers.Coordinate;

/**
 * Created by Daniel Metzner on 14.08.2017.
 */

public class DiscardPile {

    private Bitmap bitmap_;
    private final String IMAGE_NAME = "discard_pile";
    private final String PACKAGE_NAME = "drawable";

    private List<Coordinate> coordinates_;

    private Card card_bottom_;
    private Card card_left_;
    private Card card_top_;
    private Card card_right_;

    public DiscardPile() {
        coordinates_ = new ArrayList<Coordinate>();
        card_bottom_ = null;
        card_left_ = null;
        card_top_ = null;
        card_right_ = null;

    }

    public void init(GameView view) {

        int resourceId = view.getResources()
                .getIdentifier(IMAGE_NAME, PACKAGE_NAME, view.getContext().getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(view.getContext().getResources(), resourceId);

        // scale Bitmap
        int width = view.getController().getLayout().getCardWidth();
        int height = view.getController().getLayout().getCardHeight();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

        bitmap_ = scaledBitmap;
        assert bitmap_ != null : "Card: 'bitmap == null' Error";
    }

    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public Coordinate getCoordinate(int pos) {
        return coordinates_.get(pos);
    }
    public List<Coordinate> getCoordinates() {
        return coordinates_;
    }

    public Card getCard(int pos) {
        switch (pos) {
            case 0:
                return getCardBottom();
            case 1:
                return getCardLeft();
            case 2:
                return getCardTop();
            case 3:
                return getCardRight();
        }
        return null;
    }

    public void setCard(int pos, Card card) {
        switch (pos) {
            case 0:
                setCardBottom(card);
            case 1:
                setCardLeft(card);
            case 2:
                setCardTop(card);
            case 3:
                setCardRight(card);
        }
    }


    public Card getCardBottom() {
        return card_bottom_;
    }
    public void setCardBottom(Card card_bottom) { card_bottom_ = card_bottom; }

    public Card getCardLeft() {
        return card_left_;
    }
    public void setCardLeft(Card card_left) { card_left_ = card_left; }

    public Card getCardTop() {
        return card_top_;
    }
    public void setCardTop(Card card_up) { card_top_ = card_up; }

    public Card getCardRight() {
        return card_right_;
    }
    public void setCardRight(Card card_right) { card_right_ = card_right; }

    public Bitmap getBitmap() { return bitmap_; }
    public void setBitmap() { bitmap_ = bitmap_; }

}

