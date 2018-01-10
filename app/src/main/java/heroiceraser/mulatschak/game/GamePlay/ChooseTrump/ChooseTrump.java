package heroiceraser.mulatschak.game.GamePlay.ChooseTrump;

import android.os.Handler;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 03.01.2018.
 */

public class ChooseTrump {

    private ChooseTrumpAnimation choose_trump_animation_;
    private EnemyChooseTrumpLogic enemyChooseTrumpLogic;
    private TrumpView trump_view_;

    public ChooseTrump() {
        choose_trump_animation_ = new ChooseTrumpAnimation();
        enemyChooseTrumpLogic = new EnemyChooseTrumpLogic();
        trump_view_ = new TrumpView();
    }

    public void init(GameView view) {
        choose_trump_animation_.init(view);
        // enemylogic
        trump_view_.init(view);
    }

    public void startRound(GameController controller) {
        trump_view_.setVisible(false);
        choose_trump_animation_.reEnableButtons();
    }


    //----------------------------------------------------------------------------------------------
    //  cada
    //
    public void letHighestBidderChooseTrump(final GameController controller) {
        final GameView view = controller.getView();
        final GameLogic logic = controller.getLogic();

        if (logic.getTrumpPlayerId() == 0) {
            choose_trump_animation_.turnOnAnimationTrumps();
            view.disableUpdateCanvasThread();
            // touch event should call continueAfterTrumpWasChosen();
        }
        else if (logic.getTrumpPlayerId() != 0) {
            view.enableUpdateCanvasThread();
            enemyChooseTrumpLogic.chooseTrump(controller.getPlayerById(logic.getTrumpPlayerId()), logic, view);
            Handler mhandler = new Handler();
            Runnable codeToRun = new Runnable() {
                @Override
                public void run() {
                    trump_view_.startAnimation(logic.getTrump(), logic.getTrumpPlayerId(), controller);
                }
            };
            mhandler.postDelayed(codeToRun, 500);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  Getter
    //
    public TrumpView getTrumpView() {
        return trump_view_;
    }

    public ChooseTrumpAnimation getChooseTrumpAnimation() {
        return choose_trump_animation_;
    }
}
