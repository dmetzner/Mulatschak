package heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar.Windows.GameMenu;

import android.graphics.Canvas;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar.Windows.ButtonBarWindow;


//--------------------------------------------------------------------------------------------------
//  GameMenu - ButtonBarWindow
//
public class GameMenu extends ButtonBarWindow {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private AnimationSpeedGameMenu animationSpeedGameMenu;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameMenu() {
        super();
        animationSpeedGameMenu = new AnimationSpeedGameMenu();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {

        // background
        super.init(view);

        //---- title
        String title_text = view.getResources().getString(R.string.button_bar_menu_title);
        super.titleInit(view.getController(), title_text);

        animationSpeedGameMenu.init(view.getController());
    }


    //----------------------------------------------------------------------------------------------
    //  Touch Events
    //
    public void touchEventDown(int X, int Y) {
        animationSpeedGameMenu.touchEventDown(X, Y);
    }

    public void touchEventMove(int X, int Y) {
        animationSpeedGameMenu.touchEventMove(X, Y);
    }

    public void touchEventUp(int X, int Y, GameController controller) {
        animationSpeedGameMenu.touchEventUp(X, Y, controller);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas, GameController controller) {
        if (isVisible()) {

            super.drawBackground(canvas);
            super.drawTitle(canvas);

            animationSpeedGameMenu.draw(canvas);
        }
    }

}
