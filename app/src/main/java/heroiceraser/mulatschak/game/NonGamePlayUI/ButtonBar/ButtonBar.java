package heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 25.08.2017.
 */

public class ButtonBar extends DrawableObject {

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
        initStatisticButton(view);
        initTricksButton(view);
        initMenuButton(view);
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

    public void draw(Canvas canvas) {
        drawBackground(canvas);
        drawButtons(canvas);
    }

    private void drawBackground(Canvas canvas) {
        // background
        canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);
    }

    private void drawButtons(Canvas canvas) {
        menu_button_.draw(canvas);
        tricks_button_.draw(canvas);
        statistics_button_.draw(canvas);
    }


    public MyButton getStatisticsButton() {
        return statistics_button_;
    }

    public MyButton getTricksButton() {
        return tricks_button_;
    }

    public MyButton getMenuButton() {
        return menu_button_;
    }
}
