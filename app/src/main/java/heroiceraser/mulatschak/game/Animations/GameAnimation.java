package heroiceraser.mulatschak.game.Animations;

import heroiceraser.mulatschak.game.GamePlay.CardExchange.CardExchangeLogic;
import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 14.08.2017.
 */

public class GameAnimation {

    private ReAnimateHands re_animate_hands;

    public GameAnimation(GameView view) {
        re_animate_hands = new ReAnimateHands();
    }

    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public ReAnimateHands getReAnimateHands() { return re_animate_hands; }

}
