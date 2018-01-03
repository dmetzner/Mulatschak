package heroiceraser.mulatschak.game.GamePlay.PlayACard;

import android.graphics.Canvas;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GamePlay.CardExchange.EnemyCardExchangeLogic;
import heroiceraser.mulatschak.game.GameView;


//-------------------------------------------------------------------------------------------------
//  Play A Card Class
//
public class PlayACard {

    private PlayACardLogic play_a_card_logic_;
    private EnemyPlayACardLogic enemy_play_a_card_logic_;

    public PlayACard() {
        play_a_card_logic_ = new PlayACardLogic();
        enemy_play_a_card_logic_ = new EnemyPlayACardLogic();
    }

    public void init(GameView view) {
        play_a_card_logic_.init(view);
        enemy_play_a_card_logic_.init(view.getController());
    }


    public void draw(Canvas canvas, GameController controller) {

        play_a_card_logic_.draw(canvas, controller);
        enemy_play_a_card_logic_.draw(canvas);
    }


    //----------------------------------------------------------------------------------------------
    // Getter
    //
    public boolean isEnemyPlayACardAnimationRunning() {
        return enemy_play_a_card_logic_.isAnimationRunning();
    }

    public void setCardMoveable(boolean moveable) {
        play_a_card_logic_.setCardMoveable(moveable);
    }

    public PlayACardLogic getPlayACardLogic() {
        return play_a_card_logic_;
    }

    public EnemyPlayACardLogic getEnemyPlayACardLogic() {
        return enemy_play_a_card_logic_;
    }

}
