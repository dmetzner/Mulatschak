package heroiceraser.mulatschak.game.Animations;

import android.graphics.Point;

import heroiceraser.mulatschak.game.DrawableObjects.Card;
import heroiceraser.mulatschak.game.GameController;

/**
 * Created by Daniel Metzner on 23.08.2017.
 */

public class CardAnimations {
    private boolean card_moveable_;

    public CardAnimations() {
        card_moveable_ = false;
    }

    public void returnCardToHand(Card card) {
        card.setPosition(new Point(card.getFixedPosition()));
    }

    public boolean isCardMoveable() {
        return card_moveable_;
    }

    public boolean getCardMovable() {
        return card_moveable_;
    }
    public void setCardMoveable(boolean card_moveable_) {
        this.card_moveable_ = card_moveable_;
    }
}
