package heroiceraser.mulatschak.game.DrawableObjects;


import android.graphics.Bitmap;
import android.graphics.Point;

import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 25.08.2017.
 */

public class ButtonBar extends DrawableObject {

    private Bitmap border_down_;
    private Bitmap border_up_;
    private Bitmap decoration_;
    private MyButton statistics_button_;
    private MyButton tricks_button_;
    private MyButton menu_button_;

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
        border_down_ = HelperFunctions.loadBitmap(view, "game_border_down", getWidth(), getHeight() * 2);
        border_up_ = HelperFunctions.loadBitmap(view, "game_border_up", getWidth(), getHeight() * 2);
    }

    private void initStatisticButton(GameView view) {
        statistics_button_ = new MyButton();
        Point position = view.getController().getLayout().getButtonBarButtonPositionRight();
        int width = view.getController().getLayout().getButtonBarBigButtonWidth();
        int height = view.getController().getLayout().getButtonBarBigButtonHeight();
        String image_name = "button_spielstand";
        statistics_button_.init(view, position, width, height, image_name);
    }

    private void initTricksButton(GameView view) {
        tricks_button_ = new MyButton();
        Point position = view.getController().getLayout().getButtonBarButtonPositionMiddle();
        int width = view.getController().getLayout().getButtonBarBigButtonWidth();
        int height = view.getController().getLayout().getButtonBarBigButtonHeight();
        String image_name = "button_stiche";
        tricks_button_.init(view, position, width, height, image_name);
    }

    private void initMenuButton(GameView view) {
        menu_button_ = new MyButton();
        Point position = view.getController().getLayout().getButtonBarButtonPositionLeft();
        int width = view.getController().getLayout().getButtonBarSmallButtonWidth();
        int height = view.getController().getLayout().getButtonBarSmallButtonHeight();
        String image_name = "button_menu";
        menu_button_.init(view, position, width, height, image_name);
    }

    public Bitmap getDecoration() { return decoration_; }

    public MyButton getStatisticsButton() {
        return statistics_button_;
    }

    public MyButton getTricksButton() {
        return tricks_button_;
    }

    public MyButton getMenuButton() {
        return menu_button_;
    }

    public Bitmap getBorderDown() {
        return border_down_;
    }

    public Bitmap getBorderUp() {
        return border_up_;
    }
}
