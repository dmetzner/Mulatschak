package heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBarWindows.GameMenu;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import heroiceraser.mulatschak.DrawableBasicObjects.MyRadioButton;
import heroiceraser.mulatschak.DrawableBasicObjects.MyRadioButtonGroup;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextField;
import heroiceraser.mulatschak.GameSettings.AnimationSpeed;
import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;


//--------------------------------------------------------------------------------------------------
//  Animation Speed Menu Class
//                              -> shows a textField title
//                              -> shows RadioButtons to select AnimationSpeed
//
public class AnimationSpeedGameMenu {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private MyTextField title;
    private MyRadioButtonGroup radioButtonGroup;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    AnimationSpeedGameMenu() {
       radioButtonGroup = new MyRadioButtonGroup();
       for (int i = 0; i < 4; i++) {
           MyRadioButton rb = new MyRadioButton();
           radioButtonGroup.getRadioButtons().add(rb);
       }
       title = new MyTextField();
    }


    //----------------------------------------------------------------------------------------------
    //  Init
    //
    public void init(GameController controller) {
        GameLayout layout = controller.getLayout();

        // init radio buttons
        for (MyRadioButton rb : radioButtonGroup.getRadioButtons()) {
            rb.setRadius(layout.getSectors().get(1).y / 8);
            rb.setBorderSize(rb.getRadius() / 10);
            rb.getBorderPaint().setColor(Color.WHITE);
            rb.getBorderPaintChecked().setColor(Color.WHITE);
            rb.getBorderPaintDisabled().setColor(Color.WHITE);
            rb.getTextPaint().setColor(Color.WHITE);
        }
        double x_p = layout.getScreenWidth() / 100.0;
        int x0 = (int) (x_p * 20);
        int x1 = (int) (x_p * 40);
        int x2 = (int) (x_p * 60);
        int x3 = (int) (x_p * 80);
        int y = (int) (layout.getSectors().get(1).y * 4.5);
        radioButtonGroup.getRadioButtons().get(0).setPosition(new Point(x0, y));
        radioButtonGroup.getRadioButtons().get(0).setText(
                controller.getView().getResources().getString(R.string.animation_speed_slow));
        radioButtonGroup.getRadioButtons().get(1).setPosition(new Point(x1, y));
        radioButtonGroup.getRadioButtons().get(1).setText(
                controller.getView().getResources().getString(R.string.animation_speed_normal));
        radioButtonGroup.getRadioButtons().get(2).setPosition(new Point(x2, y));
        radioButtonGroup.getRadioButtons().get(2).setText(
                controller.getView().getResources().getString(R.string.animation_speed_fast));
        radioButtonGroup.getRadioButtons().get(3).setPosition(new Point(x3, y));
        radioButtonGroup.getRadioButtons().get(3).setText(
                controller.getView().getResources().getString(R.string.animation_speed_no_animations));

        radioButtonGroup.getRadioButtons().get(0).setId(AnimationSpeed.SPEED_SLOW);
        radioButtonGroup.getRadioButtons().get(1).setId(AnimationSpeed.SPEED_NORMAL);
        radioButtonGroup.getRadioButtons().get(1).setChecked(true);
        radioButtonGroup.getRadioButtons().get(2).setId(AnimationSpeed.SPEED_FAST);
        radioButtonGroup.getRadioButtons().get(3).setId(AnimationSpeed.NO_ANIMATION);


        // init title
        title.setPosition(new Point((int) (x_p * 50), (int) (layout.getSectors().get(1).y * 3.7)) );
        title.setText(controller.getView().getResources().getString(R.string.animation_speed_title));
        title.getTextPaint().setColor(Color.WHITE);
        title.getTextPaint().setTextSize(layout.getSectors().get(1).y / 3);
        title.getTextPaint().setAntiAlias(true);
        title.getTextPaint().setTextAlign(Paint.Align.CENTER);
        title.setMaxWidth((int)(x_p * 90));
        title.setVisible(true);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public synchronized void draw(Canvas canvas) {
        radioButtonGroup.draw(canvas);
        title.draw(canvas);
    }


    //----------------------------------------------------------------------------------------------
    // Touch Events
    //              -> Touch up sets the correct Speed in the Game Settings
    //
    synchronized void touchEventDown(int X, int Y) {
        radioButtonGroup.touchEventDown(X, Y);
    }

    synchronized void touchEventMove(int X, int Y) {
        radioButtonGroup.touchEventMove(X, Y);
    }

    synchronized void touchEventUp(int X, int Y, GameController controller) {
        if (radioButtonGroup.touchEventUp(X, Y)) {

            int id = radioButtonGroup.getCheckedButton().getId();

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
}

