package heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar.Windows;

import android.graphics.Canvas;

import heroiceraser.mulatschak.GameKeyBoard.MyKeyBoard;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;


//--------------------------------------------------------------------------------------------------
//  GameMenu - ButtonBarWindow
//
public class GameChat extends ButtonBarWindow {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private MyKeyBoard keyBoard;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameChat() {
        super();
        keyBoard = new MyKeyBoard();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {

        // background
        super.init(view);

        //---- title
        String title_text = view.getResources().getString(R.string.button_bar_chat_title);
        super.titleInit(view.getController(), title_text);

        keyBoard.init(view);

    }


    //----------------------------------------------------------------------------------------------
    //  Touch Events
    //
    public void touchEventDown(int X, int Y){
    }

    public void touchEventMove(int X, int Y) {

    }

    public void touchEventUp(int X, int Y, GameController controller) {

    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas, GameController controller) {
        if (isVisible()) {

            super.drawBackground(canvas);
            super.drawTitle(canvas);

            keyBoard.draw(canvas);
        }
    }

}
