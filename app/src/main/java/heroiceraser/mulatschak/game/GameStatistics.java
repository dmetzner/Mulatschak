package heroiceraser.mulatschak.game;

import android.graphics.Bitmap;

import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 23.08.2017.
 */

public class GameStatistics {

    private boolean on_;
    private Bitmap background_;
    private Button stats_button_;

    public GameStatistics() {
        on_ = false;
        stats_button_ = null;
    }


    public void init(GameView view) {

        GameLayout layout = view.getController().getLayout();
        background_ = HelperFunctions.loadBitmap(view, "statistics_background",
                layout.getScreenWidth(), layout.getCardHeight(), "drawable");

        String image_name = "statistics_button";
        String package_name = "drawable";

        stats_button_ = new Button();
        int width = (int) (view.getController().getLayout().getScreenWidth() / 3);
        int height = (int) (view.getController().getLayout().getSymbolButtonSize() / 1.8);
        stats_button_.setBitmap(HelperFunctions.loadBitmap(view, image_name, width, height, package_name));
        stats_button_.setBitmapPressed(HelperFunctions.loadBitmap(view, image_name + "_pressed", width, height, package_name));

        int x = (int) (layout.getScreenWidth() - width  * 1.1);
        int y = (int) (layout.getButtonBar().getY() + 15);

        stats_button_.setCoordinate(x, y);
    }


    public Button getStatsButton() {
        return stats_button_;
    }

    public void setStatsButton(Button stats_button_) {
        this.stats_button_ = stats_button_;
    }

    public boolean isOn() {
        return on_;
    }

    public void setOn(boolean on) {
        this.on_ = on;
    }
}
