package heroiceraser.mulatschak.game.GamePlay.ChooseTrump;

import android.os.Handler;

import com.google.gson.Gson;

import heroiceraser.mulatschak.Message;
import heroiceraser.mulatschak.MainActivity;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GameView;


//--------------------------------------------------------------------------------------------------
//  Choose trump class
//
public class ChooseTrump {

    //----------------------------------------------------------------------------------------------
    //  member Variables
    //
    private ChooseTrumpAnimation choose_trump_animation_;
    private EnemyChooseTrumpLogic enemyChooseTrumpLogic;
    private TrumpView trump_view_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public ChooseTrump() {
        choose_trump_animation_ = new ChooseTrumpAnimation();
        enemyChooseTrumpLogic = new EnemyChooseTrumpLogic();
        trump_view_ = new TrumpView();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        choose_trump_animation_.init(view);
        // enemy logic no init needed
        trump_view_.init(view);
    }


    //----------------------------------------------------------------------------------------------
    //  startRound
    //
    public void startRound() {
        trump_view_.setVisible(false);
        choose_trump_animation_.reEnableButtons();
    }


    //----------------------------------------------------------------------------------------------
    //  letHighestBidderChooseTrump
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

            // single player
            if (controller.getPlayerById(logic.getTurn()).isEnemyLogic()) {
                handleEnemyAction(controller);
            }
            else {
                controller.waitForOnlineInteraction = Message.chooseTrump;
                controller.mainActivity.requestMissedMessage(controller.mainActivity.gameState, Message.requestChooseTrump, "");
            }
        }
    }


    public void handleEnemyAction(final GameController controller) {
        final GameLogic logic = controller.getLogic();
        enemyChooseTrumpLogic.
                chooseTrump(controller.getPlayerById(logic.getTrumpPlayerId()), logic);
        Handler mhandler = new Handler();
        Runnable codeToRun = new Runnable() {
            @Override
            public void run() {
                trump_view_.startAnimation(logic.getTrump(),
                        logic.getTrumpPlayerId(), controller);
            }
        };
        mhandler.postDelayed(codeToRun, 500);
    }

    public void handleOnlineInteraction(int trump, GameController controller) {
        controller.waitForOnlineInteraction = Message.noMessage;
        setTrump(controller, trump);
    }

    public void handleMainPlayersDecision(int trump, GameController controller) {

        if (controller.multiplayer_) {
            // broadcast to all the decision
            MainActivity activity = (MainActivity) controller.getView().getContext();
            Gson gson = new Gson();
            activity.broadcastMessage(Message.chooseTrump, gson.toJson(trump));
        }
        setTrump(controller, trump);
    }


    //----------------------------------------------------------------------------------------------
    //  setTrump
    //                 -> called by Touch events, choose trump animation
    //                 -> ends choose trump animation
    //
    private void setTrump(GameController controller, int button_id) {
        controller.getLogic().setTrump(button_id + 1); // No Joker Button (card suit include joker)
        controller.getGamePlay().getChooseTrump().getTrumpView()
                .startAnimation(controller.getLogic().getTrump(), controller.getLogic().getTrumpPlayerId(), controller);
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
