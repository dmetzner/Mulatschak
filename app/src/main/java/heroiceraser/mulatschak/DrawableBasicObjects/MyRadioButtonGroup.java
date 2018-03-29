package heroiceraser.mulatschak.DrawableBasicObjects;

import android.graphics.Canvas;
import java.util.ArrayList;
import java.util.List;


//--------------------------------------------------------------------------------------------------
//  MyRadioButtonGroup
//
public class MyRadioButtonGroup {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private List<MyRadioButton> radioButtons;
    public MyRadioButtonGroup() {
        radioButtons = new ArrayList<>();
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //              -> draws all radio buttons
    //
    public synchronized void draw(Canvas canvas) {
        for (MyRadioButton rb: radioButtons) {
            rb.draw(canvas);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  checkButton
    //                -> checks the button and un-check all others
    //
    public void checkButton(int pos) {
        if (pos >= radioButtons.size()) {
            return;
        }
        for (MyRadioButton rb: radioButtons) {
            rb.setChecked(false);
        }
        radioButtons.get(pos).setChecked(true);
    }


    //----------------------------------------------------------------------------------------------
    //  get Checked Button
    //                      -> returns the checked button
    //
    public MyRadioButton getCheckedButton() {
        for (int i = 0; i < radioButtons.size(); i++) {
            if (radioButtons.get(i).isChecked()) {
                return radioButtons.get(i);
            }
        }
        return null;
    }


    //----------------------------------------------------------------------------------------------
    //  Touch Events
    //

    private static final int BR = 3;

    public synchronized void touchEventDown(int X, int Y) {
        for (MyRadioButton button : getRadioButtons()) {
            if (button.isVisible() && button.isEnabled() &&
                    X >= button.getPosition().x - BR * button.getRadius() && X < button.getPosition().x + BR * button.getRadius() &&
                    Y >= button.getPosition().y - BR * button.getRadius() && Y < button.getPosition().y + BR * button.getRadius() ) {
                button.setPressed(true);
            }
        }
    }

    public synchronized void touchEventMove(int X, int Y) {
        for (MyRadioButton button : getRadioButtons()) {
            if (button.isVisible() && button.isEnabled() && button.isPressed()) {
                if ( X >= button.getPosition().x - BR * button.getRadius() && X < button.getPosition().x + BR * button.getRadius() &&
                        Y >= button.getPosition().y - BR * button.getRadius() && Y < button.getPosition().y + BR * button.getRadius()) {
                    button.setPressed(true);
                }
                else {
                    button.setPressed(false);
                }
            }
        }
    }

    public synchronized boolean touchEventUp(int X, int Y) {
        int i = 0;
        for (MyRadioButton button : getRadioButtons()) {
            if (button.isVisible() && button.isEnabled() && button.isPressed()) {
                if ( X >= button.getPosition().x - BR * button.getRadius() && X < button.getPosition().x + BR * button.getRadius() &&
                        Y >= button.getPosition().y - BR * button.getRadius() && Y < button.getPosition().y + BR * button.getRadius()) {
                    button.setPressed(false);
                    checkButton(i);
                    return true;
                }
            }
            i++;
        }
        return false;
    }


    //----------------------------------------------------------------------------------------------
    //  Getter
    //
    public List<MyRadioButton> getRadioButtons() {
        return radioButtons;
    }
}
