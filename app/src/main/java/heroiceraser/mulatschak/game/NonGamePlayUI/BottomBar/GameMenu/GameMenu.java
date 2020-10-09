package heroiceraser.mulatschak.game.NonGamePlayUI.BottomBar.GameMenu;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.NonGamePlayUI.BottomBar.ButtonBarWindow;
import heroiceraser.mulatschak.game.NonGamePlayUI.NonGamePlayUIContainer;


//--------------------------------------------------------------------------------------------------
//  GameMenu - ButtonBarWindow
//
public class GameMenu extends ButtonBarWindow {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private AnimationSpeedGameMenu animationSpeedGameMenu;
    private NonGamePlayUIContainer UI;


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
    public void init(@NonNull GameView view) {

        UI = view.getController().getNonGamePlayUIContainer();

        // background
        super.init(view);

        //---- title
        String title_text = "Menu";
        super.titleInit(view.getController(), title_text);

        animationSpeedGameMenu.init(view.getController());
    }


    //----------------------------------------------------------------------------------------------
    //  Touch Events
    //
    public synchronized void touchEventDown(int X, int Y) {
        if (!isVisible()) {
            return;
        }
        getCloseButton().touchEventDown(X, Y);

        animationSpeedGameMenu.touchEventDown(X, Y);
    }

    public synchronized void touchEventMove(int X, int Y) {
        if (!isVisible()) {
            return;
        }
        getCloseButton().touchEventMove(X, Y);

        animationSpeedGameMenu.touchEventMove(X, Y);
    }

    public synchronized void touchEventUp(int X, int Y, GameController controller) {
        if (!isVisible()) {
            return;
        }
        if (getCloseButton().touchEventUp(X, Y)) {
            this.setVisible(false);
            UI.closeAllButtonBarWindows();
        }
        animationSpeedGameMenu.touchEventUp(X, Y, controller);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public synchronized void draw(Canvas canvas) {
        if (isVisible()) {

            super.drawBackground(canvas);
            super.drawTitle(canvas);

            animationSpeedGameMenu.draw(canvas);
        }
    }

}
