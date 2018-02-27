package heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar;


import android.graphics.Canvas;
import android.graphics.Point;
import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBarWindows.ButtonBarWindow;
import heroiceraser.mulatschak.game.NonGamePlayUI.NonGamePlayUIContainer;
import heroiceraser.mulatschak.helpers.BitmapMethodes;


//--------------------------------------------------------------------------------------------------
//  ButtonBar Class
//
public class ButtonBar extends DrawableObject {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private MyButton statisticsButton;
    private MyButton tricksButton;
    private MyButton menuButton;
    private MyButton chatButton;


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
        setBitmap(BitmapMethodes.loadBitmap(view, "button_bar", getWidth(), getHeight()));
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
        menuButton = new MyButton();
        Point position = new Point(view.getController().getLayout().getButtonBarButtonPosition());
        position.x = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 2);
        int width = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 23.75);
        int height = view.getController().getLayout().getButtonBarSmallButtonHeight();
        String image_name = "button_blue_metallic";
        String text = view.getResources().getString(R.string.button_bar_menu_title);
        menuButton.init(view, position, width, height, image_name, text);
    }

    private void initChatButton(GameView view) {
        chatButton = new MyButton();
        Point position = new Point(view.getController().getLayout().getButtonBarButtonPosition());
        position.x = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 26.083);
        int width = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 23.75);
        int height = view.getController().getLayout().getButtonBarSmallButtonHeight();
        String image_name = "button_blue_metallic";
        String text = view.getResources().getString(R.string.button_bar_chat_title);
        chatButton.init(view, position, width, height, image_name, text);
    }

    private void initTricksButton(GameView view) {
        tricksButton = new MyButton();
        Point position = new Point(view.getController().getLayout().getButtonBarButtonPosition());
        position.x = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 50.083);
        int width = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 23.75);
        int height = view.getController().getLayout().getButtonBarSmallButtonHeight();
        String image_name = "button_blue_metallic";
        String text = view.getResources().getString(R.string.button_bar_tricks_title);
        tricksButton.init(view, position, width, height, image_name, text);
    }

    private void initStatisticButton(GameView view) {
        statisticsButton = new MyButton();
        Point position = new Point(view.getController().getLayout().getButtonBarButtonPosition());
        position.x = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 74.083);
        int width = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 23.75);
        int height = view.getController().getLayout().getButtonBarBigButtonHeight();
        String image_name = "button_blue_metallic";
        String text = view.getResources().getString(R.string.button_bar_state_of_the_game_title);
        statisticsButton.init(view, position, width, height, image_name, text);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas) {
        if (!isVisible()) {
            return;
        }
        drawBackground(canvas);
        drawButtons(canvas);
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);
    }

    private void drawButtons(Canvas canvas) {
        menuButton.draw(canvas);
        tricksButton.draw(canvas);
        statisticsButton.draw(canvas);
        chatButton.draw(canvas);
    }


    //----------------------------------------------------------------------------------------------
    //  Touch Events
    //                 button open/close corresponding windows
    //                  only one button can be clicked at once
    //
    public void touchEventDown(int X, int Y){
        if (!isVisible()) {
            return;
        }
        if (statisticsButton.touchEventDown(X, Y) ) {
            return;
        }
        if (tricksButton.touchEventDown(X, Y) ) {
            return;
        }
        if (menuButton.touchEventDown(X, Y) ) {
            return;
        }
        chatButton.touchEventDown(X, Y);
    }

    public void touchEventMove(int X, int Y) {
        if (!isVisible()) {
            return;
        }

        statisticsButton.touchEventMove(X, Y);

        tricksButton.touchEventMove(X, Y);

        menuButton.touchEventMove(X, Y);

        chatButton.touchEventMove(X, Y);
    }

    public void touchEventUp(int X, int Y, NonGamePlayUIContainer ui) {
        if (!isVisible()) {
            return;
        }

        if (statisticsButton.touchEventUp(X, Y) ) {
            handleClickedButton(ui, ui.getStatistics());
        }
        else if (tricksButton.touchEventUp(X, Y) ) {
            handleClickedButton(ui, ui.getTricks());
        }
        else if (menuButton.touchEventUp(X, Y) ) {
            handleClickedButton(ui, ui.getMenu());
        }
        else if (chatButton.touchEventUp(X, Y) ) {
            handleClickedButton(ui, ui.getChat());
        }
    }


    //----------------------------------------------------------------------------------------------
    //  handle clicked button
    //                          closes all windows and switches visibility of clicked one
    //
    private void handleClickedButton(NonGamePlayUIContainer ui, ButtonBarWindow window) {
        boolean bool = window.isVisible();
        ui.closeAllButtonBarWindows();
        window.setVisible(!bool);
    }
}
