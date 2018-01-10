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
    private MyTextButton chatButton;

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
        initChatButton(view);
    }

    //----------------------------------------------------------------------------------------------
    //  init Buttons
    //

    private void initMenuButton(GameView view) {
        menu_button_ = new MyTextButton();
        Point position = new Point(view.getController().getLayout().getButtonBarButtonPosition());
        position.x = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 2);
        int width = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 23.75);
        int height = view.getController().getLayout().getButtonBarSmallButtonHeight();
        String image_name = "button_blue_metallic";
        String text = view.getResources().getString(R.string.button_bar_menu_title);
        menu_button_.init(view, position, width, height, image_name, text);
    }

    private void initChatButton(GameView view) {
        chatButton = new MyTextButton();
        Point position = new Point(view.getController().getLayout().getButtonBarButtonPosition());
        position.x = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 26.083);
        int width = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 23.75);
        int height = view.getController().getLayout().getButtonBarSmallButtonHeight();
        String image_name = "button_blue_metallic";
        String text = view.getResources().getString(R.string.button_bar_chat_title);
        chatButton.init(view, position, width, height, image_name, text);
    }

    private void initTricksButton(GameView view) {
        tricks_button_ = new MyTextButton();
        Point position = new Point(view.getController().getLayout().getButtonBarButtonPosition());
        position.x = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 50.083);
        int width = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 23.75);
        int height = view.getController().getLayout().getButtonBarSmallButtonHeight();
        String image_name = "button_blue_metallic";
        String text = view.getResources().getString(R.string.button_bar_tricks_title);
        tricks_button_.init(view, position, width, height, image_name, text);
    }

    private void initStatisticButton(GameView view) {
        statistics_button_ = new MyTextButton();
        Point position = new Point(view.getController().getLayout().getButtonBarButtonPosition());
        position.x = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 74.083);
        int width = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 23.75);
        int height = view.getController().getLayout().getButtonBarBigButtonHeight();
        String image_name = "button_blue_metallic";
        String text = view.getResources().getString(R.string.button_bar_state_of_the_game_title);
        statistics_button_.init(view, position, width, height, image_name, text);
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
        chatButton.draw(canvas);
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

    public MyTextButton getChatButton() {
        return chatButton;
    }
}
