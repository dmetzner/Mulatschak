package heroiceraser.mulatschak.game.NonGamePlayUI.AllCardsPlayed;

import android.graphics.Canvas;
import android.graphics.Point;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.game.GameView;


//----------------------------------------------------------------------------------------------
//  New Round View
//
public class AllCardsPlayedView extends DrawableObject {


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
        // todo
        Point next_round_button_position = new Point(400, 400);
        Point next_round_button_size = new Point(400, 400);
        next_round_button_.init(view, next_round_button_position,
                next_round_button_size.x, next_round_button_size.y, null);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas) {
        if (isVisible()) {
            next_round_button_.draw(canvas);
        }
    }

}
