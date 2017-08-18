package heroiceraser.mulatschak.game.Animations;

import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 14.08.2017.
 */

public class GameAnimation {

    private boolean animation_turned_on_;
    private DealingAnimation dealingAnimation_;

    public GameAnimation(GameView view) {
        animation_turned_on_ = true;
        dealingAnimation_ = new DealingAnimation(view);
    }

    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public boolean getTurnedOn() { return animation_turned_on_; }
    public DealingAnimation getDealingAnimation() {
        return dealingAnimation_;
    }

  //  public Boolean getDealingAnimation() { return dealing_animation_; }

    //public void setDealingAnimation(boolean bool) { dealing_animation_ = bool; }

}
