package heroiceraser.mulatschak.game.GamePlay.AllCardsPlayed;

import android.graphics.Canvas;
import android.graphics.Point;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextButton;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;


//----------------------------------------------------------------------------------------------
//  New Round Button
//
public class AllCardsPlayedView{


    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private MyTextButton next_round_button_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public AllCardsPlayedView() {
        next_round_button_ = new MyTextButton();
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
        controller.getNonGamePlayUIContainer().getTricks().setVisible(false);
        controller.getNonGamePlayUIContainer().getMenu().setVisible(false);
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
    //  Getter & Setter
    //
    public MyTextButton getNextRoundButton() {
        return next_round_button_;
    }
}
