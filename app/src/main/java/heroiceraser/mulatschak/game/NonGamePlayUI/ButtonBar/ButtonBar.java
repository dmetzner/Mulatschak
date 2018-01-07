package heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar;


import android.graphics.Canvas;
import android.graphics.Point;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextButton;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;


//--------------------------------------------------------------------------------------------------
//  ButtonBar Class
//
public class ButtonBar extends DrawableObject {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private MyTextButton statistics_button_;
    private MyTextButton tricks_button_;
    private MyTextButton menu_button_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public ButtonBar() {
        super();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
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

    //----------------------------------------------------------------------------------------------
    //  init Buttons
    //
    private void initStatisticButton(GameView view) {
        statistics_button_ = new MyTextButton();
        Point position = view.getController().getLayout().getButtonBarButtonPositionRight();
        int width = view.getController().getLayout().getButtonBarBigButtonWidth();
        int height = view.getController().getLayout().getButtonBarBigButtonHeight();
        String image_name = "button_blue_metallic_large";
        String text = view.getResources().getString(R.string.button_bar_state_of_the_game_title);
        statistics_button_.init(view, position, width, height, image_name, text);
    }

    private void initTricksButton(GameView view) {
        tricks_button_ = new MyTextButton();
        Point position = view.getController().getLayout().getButtonBarButtonPositionMiddle();
        int width = view.getController().getLayout().getButtonBarBigButtonWidth();
        int height = view.getController().getLayout().getButtonBarBigButtonHeight();
        String image_name = "button_blue_metallic_large";
        String text = view.getResources().getString(R.string.button_bar_tricks_title);
        tricks_button_.init(view, position, width, height, image_name, text);
    }

    private void initMenuButton(GameView view) {
        menu_button_ = new MyTextButton();
        Point position = view.getController().getLayout().getButtonBarButtonPositionLeft();
        int width = view.getController().getLayout().getButtonBarSmallButtonWidth();
        int height = view.getController().getLayout().getButtonBarSmallButtonHeight();
        String image_name = "button_blue_metallic";
        String text = view.getResources().getString(R.string.button_bar_menu_title);
        menu_button_.init(view, position, width, height, image_name, text);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas) {
        drawBackground(canvas);
        drawButtons(canvas);
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);
    }

    private void drawButtons(Canvas canvas) {
        menu_button_.draw(canvas);
        tricks_button_.draw(canvas);
        statistics_button_.draw(canvas);
    }


    //----------------------------------------------------------------------------------------------
    //  Getter
    //
    public MyTextButton getStatisticsButton() {
        return statistics_button_;
    }

    public MyTextButton getTricksButton() {
        return tricks_button_;
    }

    public MyTextButton getMenuButton() {
        return menu_button_;
    }
}
