package heroiceraser.mulatschak.game.NonGamePlayUI.BottomBar;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.drawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.drawableBasicObjects.MyTabButton;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.NonGamePlayUI.NonGamePlayUIContainer;
import heroiceraser.mulatschak.utils.BitmapMethodes;


public class BottomBar extends DrawableObject {

    private MyTabButton statisticsButton;
    private MyTabButton tricksButton;
    private MyTabButton menuButton;

    public void init(@NonNull GameView view) {
        setWidth(view.getController().getLayout().getButtonBarWidth());
        setHeight(view.getController().getLayout().getButtonBarHeight());
        setPosition(view.getController().getLayout().getButtonBarPosition());

        setBitmap(BitmapMethodes.loadBitmap(view, "bottom_bar", getWidth(), getHeight()));
        setVisible(true);

        menuButton = initTabButton(view, 0, R.string.button_bar_menu_title);
        tricksButton = initTabButton(view, 1, R.string.button_bar_tricks_title);
        statisticsButton = initTabButton(view, 2, R.string.button_bar_state_of_the_game_title);
    }


    @NonNull
    private MyTabButton initTabButton(@NonNull GameView view, int offset, int textId) {
        MyTabButton button = new MyTabButton();
        button.setPosition(
                view.getController().getLayout().getButtonBarButtonWidth() * offset,
                view.getController().getLayout().getButtonBarButtonPosition().y
        );
        button.setWidth(view.getController().getLayout().getButtonBarButtonWidth());
        button.setHeight(view.getController().getLayout().getButtonBarButtonHeight());
        button.setText(view.getResources().getString(textId));
        button.useDefaultConfig();
        button.createActiveIndicator();
        return button;
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public synchronized void draw(Canvas canvas) {
        if (!isVisible()) {
            return;
        }

        drawBackground(canvas);
        drawButtons(canvas);
    }

    private synchronized void drawBackground(@NonNull Canvas canvas) {
        canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);
    }

    private synchronized void drawButtons(Canvas canvas) {
        menuButton.draw(canvas);
        tricksButton.draw(canvas);
        statisticsButton.draw(canvas);
    }


    //----------------------------------------------------------------------------------------------
    //  Touch Events
    //                 button open/close corresponding windows
    //                  only one button can be clicked at once
    //
    public void touchEventDown(int X, int Y) {
        if (!isVisible()) {
            return;
        }
        statisticsButton.touchEventDown(X, Y);
        tricksButton.touchEventDown(X, Y);
        menuButton.touchEventDown(X, Y);
    }

    public void touchEventMove(int X, int Y) {
        if (!isVisible()) {
            return;
        }

        statisticsButton.touchEventMove(X, Y);
        tricksButton.touchEventMove(X, Y);
        menuButton.touchEventMove(X, Y);
    }

    public void touchEventUp(int X, int Y, NonGamePlayUIContainer ui) {
        if (!isVisible()) {
            return;
        }

        if (statisticsButton.touchEventUp(X, Y)) {
            handleClickedButton(ui, ui.getStatistics(), statisticsButton);
        } else if (tricksButton.touchEventUp(X, Y)) {
            handleClickedButton(ui, ui.getTricks(), tricksButton);
        } else if (menuButton.touchEventUp(X, Y)) {
            handleClickedButton(ui, ui.getMenu(), menuButton);
        }
    }

    private void handleClickedButton(@NonNull NonGamePlayUIContainer ui,
                                     @NonNull ButtonBarWindow window, @NonNull MyTabButton button) {
        boolean visible = window.isVisible();
        ui.closeAllButtonBarWindows();
        setAllButtonInactive();
        window.setVisible(!visible);
        button.setActive(!visible);
    }

    public void setAllButtonInactive() {
        menuButton.setActive(false);
        statisticsButton.setActive(false);
        tricksButton.setActive(false);
    }
}
