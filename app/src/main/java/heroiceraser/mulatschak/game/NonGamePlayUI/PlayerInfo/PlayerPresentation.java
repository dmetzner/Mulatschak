package heroiceraser.mulatschak.game.NonGamePlayUI.PlayerInfo;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;

import java.util.List;
import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextField;
import heroiceraser.mulatschak.MainActivity;
import heroiceraser.mulatschak.Message;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;


//--------------------------------------------------------------------------------------------------
//  Player presentation class
//                                 moving animation for every player from center to final position,
//                                  while the player name is blended in
//                                  player 0 gets a welcome message instead of the animation
//
public class PlayerPresentation {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private boolean animationRunning;
    private long startTime;
    private Point endPos;
    private MyTextField presentationTextField;
    private int presentationId;
    private boolean clicked;
    private Rect touchAbleArea;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    PlayerPresentation() {
        presentationTextField = new MyTextField();
        touchAbleArea = new Rect();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();

        presentationTextField.setText("");
        TextPaint tp2 = new TextPaint();
        tp2.setAntiAlias(true);
        tp2.setTextSize(layout.getDealerButtonSize());
        tp2.setTextAlign(Paint.Align.CENTER);
        tp2.setColor(Color.WHITE);
        presentationTextField.setTextPaint(tp2);
        presentationTextField.setBorder(view.getResources()
                .getColor(R.color.metallic_blue), 0.15f);
        presentationTextField.setMaxWidth(layout.getScreenWidth() / 2);
        presentationTextField.setPosition(new Point((int) (layout.getScreenWidth() / 2.0),
                (int) (layout.getScreenHeight() / 2.0)) );
        presentationTextField.setVisible(true);

        animationRunning = false;

        touchAbleArea.set(0, layout.getRoundInfoSize().y, layout.getScreenWidth(),
                layout.getButtonBarButtonPosition().y);
    }


    //----------------------------------------------------------------------------------------------
    //  start
    //
    public void start(final GameController controller) {
        startTime = System.currentTimeMillis();
        animationRunning = true;
        controller.setPlayerPresentation(true);
        presentationId = 0;
        clicked = false;
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public synchronized void draw(Canvas canvas, List<MyButton> buttons, final GameController controller) {

        double maxTime = 2500 * controller.getSettings().getAnimationSpeed().getSpeedFactor();
        long timeNow = System.currentTimeMillis();
        long timeSinceStart = timeNow - startTime;

        // player 0 not important -> show greeting + intro
        // other players show -> name,   changing alpha !!
        handlePresentationTextFieldAnimation(timeSinceStart, maxTime, controller);
        presentationTextField.draw(canvas);

        // button movement
        handleButtonMovement(timeSinceStart, maxTime, controller, buttons);

        // draw already presented buttons
        drawPresentedButtons(canvas, buttons, controller);

        // time 4 the next animation?
        if (timeSinceStart > maxTime) {
            prepareNextAnimation(buttons, controller);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  prepare next animation
    //                          called when a presentation is done
    //
    private void prepareNextAnimation(List<MyButton> buttons, GameController controller) {

        presentationId++;

        // all presentations done -> start Game!
        if (presentationId >= controller.getAmountOfPlayers()) {
            controller.setPlayerPresentation(false);
            controller.startGame();
            return;
        }

        // prep next animation
        startTime = System.currentTimeMillis();
        int playerPos = controller.getPlayerById(presentationId).getPosition();
        if (buttons != null && playerPos < buttons.size()) {
            endPos = new Point(buttons.get(playerPos).getPosition());
        }
        else {
            endPos = new Point(0, 0);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  handle textField animation
    //                                  player 0 not important -> show greeting + intro
    //                                  other players show -> name,   changing alpha !!
    //
    private void handlePresentationTextFieldAnimation(long timeSinceStart, double maxTime,
                                                      GameController controller) {

        if (presentationId == 0) {
            if (timeSinceStart < maxTime / 2) {
                presentationTextField.setText(controller.getView().getResources()
                        .getString(R.string.player_presentation_greeting));
            }
            else {
                presentationTextField.setText(controller.getView().getResources()
                        .getString(R.string.player_presentation_intro));
            }
        }

        else {
            presentationTextField.setText(
                    controller.getPlayerById(presentationId).getDisplayName());
            double percentage = timeSinceStart / (maxTime / 4.0);

            if (percentage > 3) {
                percentage = 0;
            }
            else if (percentage > 2) {
                percentage = 3 - percentage;
            }
            else if (percentage > 1) {
                percentage = 1;
            }
            int alpha = (int) (255 * percentage);
            presentationTextField.updateAlpha(alpha);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  handle button movement
    //
    private void handleButtonMovement(long timeSinceStart, double maxTime,
                                      GameController controller, List<MyButton> buttons) {
        // button movement
        if (presentationId != 0) {
            double p = timeSinceStart / (maxTime / 3.0);
            if (p > 1) {
                p = 1;
            }
            Point center = new Point(controller.getLayout().getScreenWidth() / 2,
                    controller.getLayout().getScreenHeight() / 2);

            int playerPos = controller.getPlayerById(presentationId).getPosition();
            if (buttons != null && playerPos < buttons.size()) {
                buttons.get(playerPos).setPosition(
                        new Point((int) (center.x + (endPos.x - center.x) * p),
                                (int) (center.y + (endPos.y - center.y) * p)));
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    //  draw presented buttons
    //
    private synchronized void drawPresentedButtons(Canvas canvas, List<MyButton> buttons, GameController controller) {
        for (int i = 0; i <= controller.getPlayerById(presentationId).getPosition(); i++) {
            if (buttons != null && buttons.size() > i) {
                if (buttons.get(i) != null) {
                    buttons.get(i).draw(canvas);
                }
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    //  clear
    //
    public void clear() {
        endPos = null;
        touchAbleArea = null;
        presentationTextField = null;
    }


    //----------------------------------------------------------------------------------------------
    // Touch Events
    //                  allow to skip the presentations
    //
    public synchronized void touchEventDown(int X, int Y) {
        if (!animationRunning) {
            return;
        }
        if (X > touchAbleArea.left && X < touchAbleArea.right &&
                Y > touchAbleArea.top && Y < touchAbleArea.bottom) {
            clicked = true;
        }
    }

    public synchronized void touchEventUp(int X, int Y) {
        if (!animationRunning) {
            return;
        }
        if (clicked && X > touchAbleArea.left && X < touchAbleArea.right &&
                Y > touchAbleArea.top && Y < touchAbleArea.bottom) {
            clicked = false;
            startTime = 0;
        }
    }
}
