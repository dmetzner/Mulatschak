package heroiceraser.mulatschak.game;

import android.graphics.Bitmap;

import heroiceraser.mulatschak.DrawableBasicObjects.Button;
import heroiceraser.mulatschak.game.DrawableObjects.ButtonBar;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 23.08.2017.
 */

public class GameStatistics {

    private boolean on_;
    private Bitmap background_;

    public GameStatistics() {
        on_ = false;
    }


    public void init(GameView view) {
        view = view;
/*
        GameLayout layout = view.getController().getLayout();
        background_ = HelperFunctions.loadBitmap(view, "statistics_background",
                layout.getScreenWidth(), layout.getCardHeight(), "drawable");

        String image_name = "button_spielstand";
        String package_name = "drawable";

        stats_button_ = new Button();
        int width = (int) (buttonBar.getWidth() / 3.0);
        int height = (int) (buttonBar.getHeight() * (7.0 / 9.0));
        stats_button_.setBitmap(HelperFunctions.loadBitmap(view, image_name, width, height, package_name));
        stats_button_.setBitmapPressed(HelperFunctions.loadBitmap(view, image_name + "_pressed", width, height, package_name));

        int x = (int) (buttonBar.getWidth() - width  * 1.1);
        int y = (int) (buttonBar.getPosition().y + (2.0 / 18.0) * (buttonBar.getHeight()));

        stats_button_.setPosition(x, y);

        tricks_button_ = new Button();
        image_name = "button_stiche";
        tricks_button_.setBitmap(HelperFunctions.loadBitmap(view, image_name, width, height, package_name));
        tricks_button_.setBitmapPressed(HelperFunctions.loadBitmap(view, image_name + "_pressed", width, height, package_name));

        x = (int) (buttonBar.getWidth() - width  * 2.1);
        y = (int) (buttonBar.getPosition().y + (2.0 / 18.0) * (buttonBar.getHeight()));

        tricks_button_.setPosition(x, y); */
    }

    public boolean isOn() {
        return on_;
    }

    public void setOn(boolean on) {
        this.on_ = on;
    }
}
