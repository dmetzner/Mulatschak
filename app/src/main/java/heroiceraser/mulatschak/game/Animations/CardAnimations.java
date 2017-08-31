package heroiceraser.mulatschak.game.Animations;

/**
 * Created by Daniel Metzner on 23.08.2017.
 */

public class CardAnimations {
    private boolean card_moveable_;

    public CardAnimations() {
        card_moveable_ = false;
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
