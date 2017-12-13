package heroiceraser.mulatschak.game.DrawableObjects;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 14.08.2017.
 */

public class DiscardPile extends DrawableObject {

    public final int SIZE = 4;
    private final String BMP_DISCARD_PILE = "discard_pile";

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
        setPosition(new Point(0, 0));
        this.positions_ = view.getController().getLayout().getDiscardPilePositions_();
        setBitmap(HelperFunctions.loadBitmap(view, BMP_DISCARD_PILE, getWidth(), getHeight()));
        setVisible(true);
    }

    public void init(GameView view, Point size, List<Point> positions) {
        setWidth(size.x);
        setHeight(size.y);
        this.positions_ = positions ;
        setBitmap(HelperFunctions.loadBitmap(view, BMP_DISCARD_PILE, getWidth(), getHeight()));
        setVisible(true);
    }

    public static DiscardPile copy(DiscardPile dp_to_copy) {
        DiscardPile tmp = new DiscardPile();
        tmp.setWidth(dp_to_copy.getWidth());
        tmp.setHeight(dp_to_copy.getHeight());
        tmp.setBitmap(dp_to_copy.getBitmap().copy(dp_to_copy.getBitmap().getConfig(), true));
        tmp.setVisible(dp_to_copy.isVisible());
        tmp.setPosition(new Point(0, 0));
        tmp.setPositions(dp_to_copy.getPositions());
        tmp.card_bottom_ = Card.copy(dp_to_copy.card_bottom_);
        tmp.card_left_ = Card.copy(dp_to_copy.card_left_);
        tmp.card_right_ = Card.copy(dp_to_copy.card_right_);
        tmp.card_top_ = Card.copy(dp_to_copy.card_top_);
        return tmp;

    }

    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public Point getPoint(int pos) {
        return positions_.get(pos);
    }
    public List<Point> getPositions() {
        return positions_;
    }

    public void setPositions(List<Point> positions) {
        this.positions_ = positions;
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


    public void draw(Canvas canvas) {
        if (isVisible()) {
            for (int j = 0; j < positions_.size(); j++) {
                if (getCard(j) == null || getCard(j).getBitmap() == null) {
                    canvas.drawBitmap(getBitmap(),
                            getPoint(j).x,
                            getPoint(j).y, null);
                } else {
                    canvas.drawBitmap(getCard(j).getBitmap(),
                            getPoint(j).x,
                            getPoint(j).y, null);
                }
            }
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

