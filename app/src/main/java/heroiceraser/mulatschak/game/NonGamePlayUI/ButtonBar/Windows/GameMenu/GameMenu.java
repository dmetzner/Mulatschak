package heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar.Windows.GameMenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import heroiceraser.mulatschak.DrawableBasicObjects.MyRadioButton;
import heroiceraser.mulatschak.GameSettings.AnimationSpeed;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.DrawableObjects.MyPlayer;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar.Windows.ButtonBarWindow;
import heroiceraser.mulatschak.game.NonGamePlayUI.PlayerInfo.PlayerInfoPopUpView;


//--------------------------------------------------------------------------------------------------
//  GameMenu - ButtonBarWindow
//
public class GameMenu extends ButtonBarWindow {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private AnimationSpeedRadioButtons animation_speed_radio_buttons_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameMenu() {
        super();
        animation_speed_radio_buttons_ = new AnimationSpeedRadioButtons();
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

        animation_speed_radio_buttons_.init(view.getController());
    }


    // Touch Events
    public void touchEventDown(int X, int Y) {
        animation_speed_radio_buttons_.touchEventDown(X, Y);
    }


    public void touchEventMove(int X, int Y) {
        animation_speed_radio_buttons_.touchEventMove(X, Y);
    }


    public void touchEventUp(int X, int Y, GameController controller) {
        if (animation_speed_radio_buttons_.touchEventUp(X, Y)) {

            int id = animation_speed_radio_buttons_.getRadioButtonGroup().getCheckedButton().getId();

            switch (id) {
                case AnimationSpeed.NO_ANIMATION:
                    controller.getSettings().setAnimationSpeed(AnimationSpeed.NO_ANIMATION);
                    break;
                case AnimationSpeed.SPEED_SLOW:
                    controller.getSettings().setAnimationSpeed(AnimationSpeed.SPEED_SLOW);
                    break;
                case AnimationSpeed.SPEED_NORMAL:
                    controller.getSettings().setAnimationSpeed(AnimationSpeed.SPEED_NORMAL);
                    break;
                case AnimationSpeed.SPEED_FAST:
                    controller.getSettings().setAnimationSpeed(AnimationSpeed.SPEED_FAST);
                    break;
                default:
                    controller.getSettings().setAnimationSpeed(AnimationSpeed.SPEED_NORMAL);
                    break;
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas, GameController controller) {
        if (isVisible()) {

            super.drawBackground(canvas);
            super.drawTitle(canvas);

            animation_speed_radio_buttons_.draw(canvas);


        }
    }

}
