package heroiceraser.mulatschak.game.Animations;

import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 14.08.2017.
 */

public class GameAnimation {

    private boolean animation_turned_on_;

    private DealingAnimation dealing_animation_;
    private ReAnimateHands re_animate_hands;
    private StichAnsage stich_ansage_;

    public GameAnimation(GameView view) {
        animation_turned_on_ = true;
        dealing_animation_ = new DealingAnimation(view);
        re_animate_hands = new ReAnimateHands();
        stich_ansage_ = new StichAnsage();
    }

    public void init(GameView view) {
        stich_ansage_.init(view);
    }

    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public boolean getTurnedOn() { return animation_turned_on_; }
    public DealingAnimation getDealingAnimation() {
        return dealing_animation_;
    }
    public ReAnimateHands getReAnimateHands() { return re_animate_hands; }

    public StichAnsage getStichAnsage() { return stich_ansage_; }
}
