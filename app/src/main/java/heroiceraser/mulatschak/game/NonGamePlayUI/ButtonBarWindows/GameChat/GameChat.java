package heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBarWindows.GameChat;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.TextPaint;

import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.MyTextField;
import heroiceraser.mulatschak.GameKeyBoard.MyKeyBoard;
import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBarWindows.ButtonBarWindow;


//--------------------------------------------------------------------------------------------------
//  GameMenu - ButtonBarWindow
//
public class GameChat extends ButtonBarWindow {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private MyKeyBoard keyBoard;
    private MyTextField textField;
    private float maxTextFieldSize;
    private String text;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameChat() {
        super();
        keyBoard = new MyKeyBoard();
        textField = new MyTextField();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {

        // background
        super.init(view);

        //---- title
        String title_text = view.getResources().getString(R.string.button_bar_chat_title);
        super.titleInit(view.getController(), title_text);

        keyBoard.init(view);

        textField.setPosition(new Point((int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 8),
                (int) (view.getController().getLayout().getOnePercentOfScreenHeight() * 49) ));

        textField.setTextPaint(
                new TextPaint(view.getController().getNonGamePlayUIContainer().getChatView().getTextPaint()));

        maxTextFieldSize =  view.getController().getLayout()
                .getOnePercentOfScreenWidth() * 80 - (textField.getTextPaint().measureText("12345678"));
        textField.getTextPaint().setTextAlign(Paint.Align.LEFT);
        text = "";
        textField.setText("Nachricht: " + text);
        textField.setVisible(true);
    }


    //----------------------------------------------------------------------------------------------
    //  Touch Events
    //
    public void touchEventDown(int X, int Y){
        if (!isVisible()) {
            return;
        }

        getCloseButton().touchEventDown(X, Y);

        keyBoard.touchEventDown(X, Y);
    }

    public void touchEventMove(int X, int Y) {
        if (!isVisible()) {
            return;
        }

        getCloseButton().touchEventMove(X, Y);

        keyBoard.touchEventMove(X, Y);
    }

    public void touchEventUp(int X, int Y, GameController controller) {
        if (!isVisible()) {
            return;
        }

        if (getCloseButton().touchEventUp(X, Y)) {
            this.setVisible(false);
        }

        List<String> keys = keyBoard.touchEventUp(X, Y, controller);
        if (keys == null) {
            return;
        }

        for (String key : keys) {
            switch (key) {
                case "send":
                    if (!text.equals("")) {
                    controller.getNonGamePlayUIContainer().getChatView().addMessage(controller.getPlayerById(0), text);
                    }
                    text = "";
                    textField.setText("Nachricht: " + text);
                    break;
                case "del":
                    if (text.length() > 0) {
                        text = text.substring(0, text.length() - 1);
                    }
                    key = "";
                    // no break ;)
                default:
                    text += key;
                    while (textField.getTextPaint().measureText(text) > maxTextFieldSize && !text.equals("")) {
                        text = text.substring(0, text.length() - 1);
                    }
                    textField.setText("Nachricht: " + text);
                    break;
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas) {
        if (isVisible()) {

            super.drawBackground(canvas);
            super.drawTitle(canvas);

            keyBoard.draw(canvas);
            textField.draw(canvas);
        }
    }

}
