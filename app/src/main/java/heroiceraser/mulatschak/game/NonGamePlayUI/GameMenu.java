package heroiceraser.mulatschak.game.NonGamePlayUI;


import android.graphics.Canvas;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 27.08.2017.
 */

public class GameMenu extends DrawableObject{

    private ButtonBarWindowTitle title_;

    public GameMenu() {
        super();
        title_ = new ButtonBarWindowTitle();
    }

    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();

        // background
        setPosition(layout.getButtonBarWindowPosition());
        setWidth(layout.getButtonBarWindowSize().x);
        setHeight(layout.getButtonBarWindowSize().y);
        setBitmap(HelperFunctions.loadBitmap(view, "statistics_background", getWidth(), getHeight()));

        //---- title
        String title_text = view.getResources().getString(R.string.button_bar_menu_title);
        title_.init(view.getController(), title_text);

    }

    public void draw(Canvas canvas) {
        if (isVisible()) {

            // draw background
            canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);

            // title
            title_.draw(canvas);

        }
    }

}
