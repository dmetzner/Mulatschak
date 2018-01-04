package heroiceraser.mulatschak.DrawableBasicObjects;

import android.graphics.Canvas;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Metzner on 04.01.2018.
 */

public class MyRadioButtonGroup {

    private List<MyRadioButton> radioButtons;

    public MyRadioButtonGroup() {
        radioButtons = new ArrayList<>();
    }

    public List<MyRadioButton> getRadioButtons() {
        return radioButtons;
    }

    public void draw(Canvas canvas) {
        for (MyRadioButton rb: radioButtons) {
            rb.draw(canvas);
        }
    }

    public void check(int pos) {
        if (pos >= radioButtons.size()) {
            return;
        }
        for (MyRadioButton rb: radioButtons) {
            rb.setChecked(false);
        }
        radioButtons.get(pos).setChecked(true);
    }


    public MyRadioButton getCheckedButton() {
        for (int i = 0; i < radioButtons.size(); i++) {
            if (radioButtons.get(i).isChecked()) {
                return radioButtons.get(i);
            }
        }
        return null;
    }



    public void touchEventDown(int X, int Y) {
        for (MyRadioButton button : getRadioButtons()) {
            if (button.isVisible() && button.isEnabled() &&
                    X >= button.getPosition().x - button.getRadius() && X < button.getPosition().x + button.getRadius() &&
                    Y >= button.getPosition().y - button.getRadius() && Y < button.getPosition().y + button.getRadius() ) {
                button.setPressed(true);
            }
        }
    }


    public void touchEventMove(int X, int Y) {
        for (MyRadioButton button : getRadioButtons()) {
            if (button.isVisible() && button.isEnabled() && button.isPressed()) {
                if ( X >= button.getPosition().x - button.getRadius() && X < button.getPosition().x + button.getRadius() &&
                        Y >= button.getPosition().y - button.getRadius() && Y < button.getPosition().y + button.getRadius()) {
                    button.setPressed(true);
                }
                else {
                    button.setPressed(false);
                }
            }
        }
    }


    public boolean touchEventUp(int X, int Y) {
        int i = 0;
        for (MyRadioButton button : getRadioButtons()) {
            if (button.isVisible() && button.isEnabled() && button.isPressed()) {
                if ( X >= button.getPosition().x - button.getRadius() && X < button.getPosition().x + button.getRadius() &&
                        Y >= button.getPosition().y - button.getRadius() && Y < button.getPosition().y + button.getRadius()) {
                    button.setPressed(false);
                    check(i);
                    return true;
                }
            }
            i++;
        }
        return false;
    }


}
