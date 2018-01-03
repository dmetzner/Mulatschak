package heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar.Windows;

import android.graphics.Canvas;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;


//--------------------------------------------------------------------------------------------------
//  GameMenu - ButtonBarWindow
//
public class GameMenu extends ButtonBarWindow{

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameMenu() {
        super();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();

        // background
        super.init(view);

        //---- title
        String title_text = view.getResources().getString(R.string.button_bar_menu_title);
        super.titleInit(view.getController(), title_text);

        // ToDO
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas) {
        if (isVisible()) {

            super.drawBackground(canvas);
            super.drawTitle(canvas);

            // ToDO
        }
    }

}
