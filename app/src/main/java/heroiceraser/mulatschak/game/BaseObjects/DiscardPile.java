package heroiceraser.mulatschak.game.BaseObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.BitmapMethodes;


//--------------------------------------------------------------------------------------------------
//  Discard Pile class
//
public class DiscardPile extends DrawableObject {

    //----------------------------------------------------------------------------------------------
    //  Constants
    //
    public final int SIZE = 4;

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private Card card_bottom_;
    private Card card_left_;
    private Card card_top_;
    private Card card_right_;

    private boolean overlays_visible_;
    private Bitmap overlay_lost_;

    private List<Point> positions_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public DiscardPile() {
        super();
        positions_ = new ArrayList<>();
        card_bottom_ = null;
        card_left_ = null;
        card_top_ = null;
        card_right_ = null;
        overlays_visible_ = false;
        overlay_lost_ = null;
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        setWidth(view.getController().getLayout().getDiscardPileWidth());
        setHeight(view.getController().getLayout().getDiscardPileHeight());
        setPosition(new Point(view.getController().getLayout().getDiscardPilePositions().get(1).x,
                view.getController().getLayout().getDiscardPilePositions().get(2).y));
        finishInit(view);

    }

    public void init(GameView view, Point size, List<Point> positions) {
        setWidth(size.x);
        setHeight(size.y);
        this.positions_ = positions ;
        finishInit(view);
    }

    private void finishInit(GameView view) {
        String BMP_DISCARD_PILE = "discard_pile";
        this.positions_ = view.getController().getLayout().getDiscardPilePositions();
        setBitmap(BitmapMethodes.loadBitmap(view, BMP_DISCARD_PILE, getWidth(), getHeight()));
        Bitmap backside = BitmapMethodes.loadBitmap(view, "card_back", getWidth(), getHeight());
        Bitmap overlay = BitmapMethodes.createBitmapOverlay(backside);
        overlay_lost_ = BitmapMethodes.adjustOpacity(overlay, 120);
        if (backside != null) {
            backside.recycle();
        }
        if (overlay != null) {
            overlay.recycle();
        }
        setVisible(false);
    }


    //----------------------------------------------------------------------------------------------
    //  copy
    //
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
        tmp.overlay_lost_ = dp_to_copy.getBitmap().copy(dp_to_copy.getBitmap().getConfig(), true);
        return tmp;

    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public synchronized void draw(Canvas canvas, GameController controller) {
        if (isVisible()) {
            // draw discard pile
            for (int j = 0; j < positions_.size(); j++) {
                if (controller.getAmountOfPlayers() == 2 && (j == 1 || j > 2)) {
                    continue;
                }
                if (controller.getAmountOfPlayers() == 3 && (j > 2)) {
                    continue;
                }
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


    //----------------------------------------------------------------------------------------------
    //  draw Overlays
    //
    public synchronized void drawOverlays(Canvas canvas, GameController controller) {
        if (overlays_visible_) {
            // draw discard pile
            int roundWinnerPos = controller.getPlayerById(controller.getLogic()
                    .getRoundWinnerId()).getPosition();
            for (int j = 0; j < positions_.size(); j++) {
                if (j != roundWinnerPos) {
                    if (controller.getAmountOfPlayers() == 2 && (j == 1 || j > 2)) {
                        continue;
                    }
                    if (controller.getAmountOfPlayers() == 3 && (j > 2)) {
                        continue;
                    }
                    canvas.drawBitmap(overlay_lost_,
                            getPoint(j).x,
                            getPoint(j).y, null);
                }
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    //  clear
    //
    public void clear() {
        this.setCardBottom(null);
        this.setCardLeft(null);
        this.setCardTop(null);
        this.setCardRight(null);
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public List<Card> getCards() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(getCardBottom());
        cards.add(getCardLeft());
        cards.add(getCardTop());
        cards.add(getCardRight());
        return cards;
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

    public void setOverlaysVisible(boolean visible) {
        overlays_visible_ = visible;
    }

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
                break;
            case 1:
                setCardLeft(card);
                break;
            case 2:
                setCardTop(card);
                break;
            case 3:
                setCardRight(card);
                break;
        }
    }
}