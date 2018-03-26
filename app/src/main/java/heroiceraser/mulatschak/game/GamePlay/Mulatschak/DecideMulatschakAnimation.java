package heroiceraser.mulatschak.game.GamePlay.Mulatschak;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.TextPaint;

import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextField;
import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GamePlay.Background4Player0Animations;
import heroiceraser.mulatschak.game.GameView;

//--------------------------------------------------------------------------------------------------
// Decide Mulatschak Class
//
//
class DecideMulatschakAnimation {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private boolean animationRunning;
    private Background4Player0Animations background;
    private MyTextField muliText;
    private MyButton yesButton;
    private MyButton noButton;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    DecideMulatschakAnimation() {
        background = new Background4Player0Animations();
        muliText = new MyTextField();
        yesButton = new MyButton();
        noButton = new MyButton();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    void init(GameView view) {

        GameLayout layout = view.getController().getLayout();

        background.init(layout);

        // yes & no button
        String yesButtonImageName = "button_blue_metallic";
        String yesButtonText = view.getResources().getString(R.string.yes_button);
        Point yesButtonPosition = new Point((int)(layout.getOnePercentOfScreenWidth() * 19),
                (int)(layout.getOnePercentOfScreenHeight() * 60));
        Point yesButtonSize = new Point((int)(layout.getOnePercentOfScreenWidth() * 29.5),
                (int)(layout.getOnePercentOfScreenHeight() * 10));

        String noButtonImageName = "button_blue_metallic";
        String noButtonText = view.getResources().getString(R.string.no_button);
        Point noButtonPosition = new Point((int)(layout.getOnePercentOfScreenWidth() * 51.5),
                (int)(layout.getOnePercentOfScreenHeight() * 60));
        Point noButtonSize = new Point((int)(layout.getOnePercentOfScreenWidth() * 29.5),
                (int)(layout.getOnePercentOfScreenHeight() * 10));

        yesButton.init(view, yesButtonPosition, yesButtonSize.x, yesButtonSize.y, yesButtonImageName, yesButtonText);
        noButton.init(view, noButtonPosition, noButtonSize.x, noButtonSize.y, noButtonImageName, noButtonText);

        // muli text  relative to the buttons
        TextPaint muliTextPaint = new TextPaint();
        muliTextPaint.setAntiAlias(true);
        muliTextPaint.setTextSize((layout.getOnePercentOfScreenHeight() * 15));
        muliTextPaint.setTextAlign(Paint.Align.CENTER);
        muliTextPaint.setColor(Color.WHITE);
        muliText.setMaxWidth((int)(layout.getOnePercentOfScreenWidth() * 80));
        muliText.setText(view.getResources().getString(R.string.muli_text));

        while (muliTextPaint.measureText(muliText.getText()) > muliText.getMaxWidth()) {
            muliTextPaint.setTextSize(muliTextPaint.getTextSize() * 0.98f);
        }
        muliText.setTextPaint(muliTextPaint);
        muliText.setBorder(Color.BLACK, 0.05f);
        int muliTextY = yesButton.getPosition().y;
        muliTextY -= (int)(layout.getOnePercentOfScreenHeight());
        muliTextY -= muliTextPaint.getTextSize() / 2;
        muliText.setPosition(new Point((int)(layout.getOnePercentOfScreenWidth() * 50),
                muliTextY));
        muliText.setVisible(true);

    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas, GameController controller) {
        if (animationRunning) {
            background.draw(canvas);
            controller.getPlayerHandsView().drawPlayer0Hand(canvas, controller);
            muliText.draw(canvas);
            yesButton.draw(canvas);
            noButton.draw(canvas);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  Touch Events
    //
    synchronized void touchEventDown(int X, int Y) {
       if (animationRunning) {
           yesButton.touchEventDown(X, Y);
           noButton.touchEventDown(X, Y);
       }
    }

    synchronized void touchEventMove(int X, int Y) {
        if (animationRunning) {
            yesButton.touchEventMove(X, Y);
            noButton.touchEventMove(X, Y);
        }
    }

    synchronized void touchEventUp(int X, int Y, GameController controller) {
        if (animationRunning) {
            if (yesButton.touchEventUp(X, Y)) {
                animationRunning = false;
                controller.getGamePlay().getDecideMulatschak()
                        .handleMainPlayersDecision(true, controller);
            }
            if (noButton.touchEventUp(X, Y)) {
                animationRunning = false;
                controller.getGamePlay().getDecideMulatschak()
                        .handleMainPlayersDecision(false, controller);
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    void turnOnAnimation() {
        this.animationRunning = true;
    }
}
