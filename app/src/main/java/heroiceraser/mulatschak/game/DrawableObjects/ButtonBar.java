package heroiceraser.mulatschak.game.DrawableObjects;


import android.graphics.Bitmap;
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

    private Bitmap decoration_;
    private Button statistics_button_;
    private Button tricks_button_;
    private Button menu_button_;

    public ButtonBar() {
        super();
    }

    public void init(GameView view) {
        setWidth(view.getController().getLayout().getButtonBarWidth());
        setHeight(view.getController().getLayout().getButtonBarHeight());
        setPosition(view.getController().getLayout().getButtonBarPosition());
        setBitmap(HelperFunctions.loadBitmap(view, "button_bar", getWidth(), getHeight(), "drawable"));
        setVisible(true);
        initDecoration(view);
        initStatisticButton(view);
        initTricksButton(view);
        initMenuButton(view);

    }

    private void initDecoration(GameView view) {
        decoration_ = HelperFunctions.loadBitmap(view, "button_bar_decoration", getWidth(),
                getHeight(), "drawable" );
    }

    private void initStatisticButton(GameView view) {
        String image_name = "button_spielstand";
        String package_name = "drawable";
        statistics_button_ = new Button();
        int width = view.getController().getLayout().getButtonBarBigButtonWidth();
        int height = view.getController().getLayout().getButtonBarBigButtonHeight();
        statistics_button_.setBitmap(HelperFunctions
                .loadBitmap(view, image_name, width, height, package_name));
        statistics_button_.setBitmapPressed(HelperFunctions
                .loadBitmap(view, image_name + "_pressed", width, height, package_name));
        statistics_button_.setPosition(
                view.getController().getLayout().getButtonBarButtonPositionRight());
        statistics_button_.setVisible(true);
    }

    private void initTricksButton(GameView view) {
        String image_name = "button_stiche";
        String package_name = "drawable";
        tricks_button_ = new Button();
        int width = view.getController().getLayout().getButtonBarBigButtonWidth();
        int height = view.getController().getLayout().getButtonBarBigButtonHeight();
        tricks_button_.setBitmap(HelperFunctions
                .loadBitmap(view, image_name, width, height, package_name));
        tricks_button_.setBitmapPressed(HelperFunctions
                .loadBitmap(view, image_name + "_pressed", width, height, package_name));
        tricks_button_.setPosition(
                view.getController().getLayout().getButtonBarButtonPositionMiddle());
        tricks_button_.setVisible(true);
    }

    private void initMenuButton(GameView view) {
        String image_name = "button_menu";
        String package_name = "drawable";
        menu_button_ = new Button();
        int width = view.getController().getLayout().getButtonBarSmallButtonWidth();
        int height = view.getController().getLayout().getButtonBarSmallButtonHeight();
        menu_button_.setBitmap(HelperFunctions
                .loadBitmap(view, image_name, width, height, package_name));
        menu_button_.setBitmapPressed(HelperFunctions
                .loadBitmap(view, image_name + "_pressed", width, height, package_name));
        menu_button_.setPosition(
                view.getController().getLayout().getButtonBarButtonPositionLeft());
        menu_button_.setVisible(true);
    }

    public Bitmap getDecoration() { return decoration_; }

    public Button getStatisticsButton() {
        return statistics_button_;
    }

    public Button getTricksButton() {
        return tricks_button_;
    }

    public Button getMenuButton() {
        return menu_button_;
    }
}
