package heroiceraser.mulatschak.game.DrawableObjects;


import android.graphics.Bitmap;
import android.graphics.Point;

import heroiceraser.mulatschak.DrawableBasicObjects.Button;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
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
        setBitmap(HelperFunctions.loadBitmap(view, "button_bar", getWidth(), getHeight()));
        setVisible(true);
        initDecoration(view);
        initStatisticButton(view);
        initTricksButton(view);
        initMenuButton(view);
    }

    private void initDecoration(GameView view) {
        decoration_ = HelperFunctions.loadBitmap(view, "button_bar_decoration", getWidth(), getHeight());
    }

    private void initStatisticButton(GameView view) {
        statistics_button_ = new Button();
        Point position = view.getController().getLayout().getButtonBarButtonPositionRight();
        int width = view.getController().getLayout().getButtonBarBigButtonWidth();
        int height = view.getController().getLayout().getButtonBarBigButtonHeight();
        String image_name = "button_spielstand";
        statistics_button_.init(view, position, width, height, image_name);
    }

    private void initTricksButton(GameView view) {
        tricks_button_ = new Button();
        Point position = view.getController().getLayout().getButtonBarButtonPositionMiddle();
        int width = view.getController().getLayout().getButtonBarBigButtonWidth();
        int height = view.getController().getLayout().getButtonBarBigButtonHeight();
        String image_name = "button_stiche";
        tricks_button_.init(view, position, width, height, image_name);
    }

    private void initMenuButton(GameView view) {
        menu_button_ = new Button();
        Point position = view.getController().getLayout().getButtonBarButtonPositionLeft();
        int width = view.getController().getLayout().getButtonBarSmallButtonWidth();
        int height = view.getController().getLayout().getButtonBarSmallButtonHeight();
        String image_name = "button_menu";
        menu_button_.init(view, position, width, height, image_name);
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
