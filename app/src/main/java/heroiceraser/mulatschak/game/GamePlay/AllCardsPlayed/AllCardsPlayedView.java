package heroiceraser.mulatschak.game.GamePlay.AllCardsPlayed;

import android.graphics.Canvas;
import android.graphics.Point;

import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import at.heroiceraser.mulatschak.R;
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
            controller.getView().postInvalidateOnAnimation();
            controller.startRound();
        }
    }
}
