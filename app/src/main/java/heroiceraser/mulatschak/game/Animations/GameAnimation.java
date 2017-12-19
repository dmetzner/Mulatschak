package heroiceraser.mulatschak.game.Animations;

import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 14.08.2017.
 */

public class GameAnimation {

    private boolean animation_turned_on_;

    private CardAnimations card_animations_;
    private DealingAnimation dealing_animation_;
    private ReAnimateHands re_animate_hands;
    private CardExchange card_exchange_;
    private TrickBids trick_bids_;

    public GameAnimation(GameView view) {
        animation_turned_on_ = true;
        card_animations_ = new CardAnimations();
        dealing_animation_ = new DealingAnimation(view);
        re_animate_hands = new ReAnimateHands();
        card_exchange_ = new CardExchange();
        trick_bids_ = new TrickBids();
    }

    public void init(GameView view) {
        dealing_animation_.init(view);
        card_exchange_.init(view);
        trick_bids_.init(view);
    }

    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public boolean getTurnedOn() { return animation_turned_on_; }
    public DealingAnimation getDealingAnimation() {
        return dealing_animation_;
    }
    public ReAnimateHands getReAnimateHands() { return re_animate_hands; }

    public CardAnimations getCardAnimations() {
        return card_animations_;
    }

    public CardExchange getCardExchange() { return card_exchange_; }
    public TrickBids getTrickBids() { return trick_bids_; }
}
