package heroiceraser.mulatschak.game.GamePlay.AllCardsPlayed;

import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;


import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.Message;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;


//--------------------------------------------------------------------------------------------------
//  AllCardsPlayedView
//                      -> new round button
//
public class AllCardsPlayedView{


    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private MyButton next_round_button_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public AllCardsPlayedView() {
        next_round_button_ = new MyButton();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {

        GameLayout layout = view.getController().getLayout();

        Point next_round_button_size = new Point((int)(layout.getButtonBarBigButtonWidth() * 1.5),
                layout.getButtonBarBigButtonHeight());

        Point next_round_button_position = new Point(layout.getScreenWidth() / 2 - next_round_button_size.x / 2,
                layout.getSectors().get(6).y);

        String text = view.getResources().getString(R.string.next_round_button);
        next_round_button_.init(view, next_round_button_position,
                next_round_button_size.x, next_round_button_size.y, "button_blue_metallic_large", text);
        next_round_button_.setVisible(false);
    }


    //----------------------------------------------------------------------------------------------
    //  startAnimation
    //
    public void startAnimation(GameController controller) {
        init(controller.getView());
        controller.getView().enableUpdateCanvasThread();
        controller.getNonGamePlayUIContainer().closeAllButtonBarWindows();
        controller.getNonGamePlayUIContainer().getStatistics().setVisible(true);
        controller.getLogic().setMulatschakRound(false);
        next_round_button_.setVisible(true);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas, GameController controller) {
        if (next_round_button_.isVisible()) {
            controller.getView().disableUpdateCanvasThread();
            next_round_button_.draw(canvas);
        }
    }

    public void waitOrStartNewRound(final GameController controller) {
        try {
            controller.waitForOnlineInteraction = Message.waitForNewRound;
            if (controller.mainActivity.gameState == Message.gameStateWaitForNewRound) {
                boolean wait = false;
                for (MyPlayer p : controller.getPlayerList()) {
                    if (p.gameState != Message.gameStateWaitForNewRound &&
                            !p.getOnlineId().equals("") &&
                                    !p.getOnlineId().equals(controller.myPlayerId)) {
                        controller.mainActivity.sendUnReliable(p.getOnlineId(), Message.requestWaitForNewRound, "");
                        wait = true;
                    }
                }
                if (wait) {
                    Handler h = new Handler();
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            waitOrStartNewRound(controller);
                        }
                    };
                    h.postDelayed(r, 150);
                }
                else {
                    controller.mainActivity.gameState = Message.gameStateWaitForShuffleDeck;
                    for (MyPlayer p : controller.getPlayerList()) {
                        p.gameState = Message.gameStateWaitForShuffleDeck;
                        p.exchanged_cards_.getCardStack().clear();
                        p.played_cards_.getCardStack().clear();
                    }
                    controller.waitForOnlineInteraction = 0;
                    controller.prepareNewRound();
                    controller.startRound();
                }
            }
        }
        catch (Exception e) {
            Log.e("All cards played", "exception" + e);
        }

    }


    //----------------------------------------------------------------------------------------------
    // Touch Events
    //                  next round button
    //
    public void touchEventDown(int X, int Y) {
        next_round_button_.touchEventDown(X, Y);
    }

    public void touchEventMove(int X, int Y) {
        next_round_button_.touchEventMove(X, Y);
    }

    public void touchEventUp(int X, int Y, GameController controller) {
        if (next_round_button_.touchEventUp(X, Y)) {
            next_round_button_.setVisible(false);
            controller.getNonGamePlayUIContainer().closeAllButtonBarWindows();
            controller.getDiscardPile().setVisible(false);
            controller.getView().postInvalidateOnAnimation();
            controller.mainActivity.gameState = Message.gameStateWaitForNewRound;
            controller.getPlayerByPosition(0).gameState = Message.gameStateWaitForNewRound;
            waitOrStartNewRound(controller);
        }
    }
}
