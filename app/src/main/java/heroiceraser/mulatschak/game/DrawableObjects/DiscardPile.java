package heroiceraser.mulatschak.game.DrawableObjects;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 14.08.2017.
 */

public class DiscardPile extends DrawableObject {

    private final String BMP_DISCARD_PILE = "discard_pile";
    private final String PACKAGE_DISCARD_PILE = "drawable";

    private Card card_bottom_;
    private Card card_left_;
    private Card card_top_;
    private Card card_right_;

    private List<Point> positions_;

    public DiscardPile() {
        super();
        positions_ = new ArrayList<Point>();
        card_bottom_ = null;
        card_left_ = null;
        card_top_ = null;
        card_right_ = null;

    }

    public void init(GameView view) {
        setWidth(view.getController().getLayout().getDiscardPileWidth());
        setHeight(view.getController().getLayout().getDiscardPileHeight());
        positions_ = view.getController().getLayout().getDiscardPilePositions_();
        setBitmap(HelperFunctions.loadBitmap(view, BMP_DISCARD_PILE, getWidth(), getHeight(), PACKAGE_DISCARD_PILE ));
        setVisible(true);
    }

    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public Point getPoint(int pos) {
        return positions_.get(pos);
    }
    public List<Point> getPoints() {
        return positions_;
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
                return;
            case 1:
                setCardLeft(card);
                return;
            case 2:
                setCardTop(card);
                return;
            case 3:
                setCardRight(card);
                return;
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

}
