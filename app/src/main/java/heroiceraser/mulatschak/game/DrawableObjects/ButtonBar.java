package heroiceraser.mulatschak.game.DrawableObjects;


import android.graphics.Point;

import heroiceraser.mulatschak.DrawableBasicObjects.Button;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameStatistics;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 25.08.2017.
 */

public class ButtonBar extends DrawableObject {

    private Button statistics_button_;
    private Button tricks_button_;

    public ButtonBar() {
        super();
    }

    public void init(GameView view) {
        setWidth(view.getController().getLayout().getScreenWidth());
        int divided_parts = 8;
        setHeight(view.getController().getLayout().getScreenHeight() / divided_parts);
        setPosition(new Point(0, getHeight() * (divided_parts - 1)));
        setVisible(true);

        String image_name = "button_spielstand";
        String package_name = "drawable";

        statistics_button_ = new Button();
        int width = (int) (getWidth() / 3.0);
        int height = (int) (getHeight() * (7.0 / 9.0));
        statistics_button_.setBitmap(HelperFunctions.loadBitmap(view, image_name, width, height, package_name));
        statistics_button_.setBitmapPressed(HelperFunctions.loadBitmap(view, image_name + "_pressed", width, height, package_name));

        int x = (int) (getWidth() - width  * 1.1);
        int y = (int) (getPosition().y + (2.0 / 18.0) * (getHeight()));
        statistics_button_.setPosition(x, y);
        statistics_button_.setVisible(true);

    }

    public Button getStatisticsButton() {
        return statistics_button_;
    }

    public Button getTricksButton() {
        return tricks_button_;
    }
}
