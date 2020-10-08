package heroiceraser.mulatschak.game.NonGamePlayUI.GameOver;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.text.TextPaint;

import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.MainActivity;
import heroiceraser.mulatschak.drawableBasicObjects.MyButton;
import heroiceraser.mulatschak.drawableBasicObjects.MyTextField;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GamePlay.GameAreaOverlay;
import heroiceraser.mulatschak.game.GameView;


//--------------------------------------------------------------------------------------------------
//  Game Over Class
//
public class GameOver {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private boolean visible;
    private GameAreaOverlay gameAreaOverlay;
    private MyTextField textField;
    private MyButton endGameButton;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameOver() {
        super();
        endGameButton = new MyButton();
        textField = new MyTextField();
        visible = false;
    }

    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();

        gameAreaOverlay = new GameAreaOverlay(layout);

        textField.setPosition(new Point(layout.getScreenWidth() / 2, layout.getScreenHeight() / 2));
        textField.setText(view.getResources().getString(R.string.game_over_text));
        textField.setVisible(false);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(layout.getLengthOnVerticalGrid(125));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textField.setTextPaint(textPaint);
        textField.setBorder(view.getResources().getColor(R.color.metallic_blue), 0.2f);
        textField.setMaxWidth((int) layout.getOnePercentOfScreenWidth() * 80);

        // button
        Point endGameButtonSize = new Point((int) (layout.getButtonBarButtonWidth() * 1.5),
                layout.getButtonBarButtonHeight());

        Point endGameButtonPosition = new Point(layout.getScreenWidth() / 2 - endGameButtonSize.x / 2,
                layout.getLengthOnVerticalGrid(750));

        String text = view.getResources().getString(R.string.game_over_button);
        endGameButton.init(view, endGameButtonPosition,
                endGameButtonSize.x, endGameButtonSize.y, "button_blue_metallic_large", text);
        endGameButton.setVisible(false);
    }

    //----------------------------------------------------------------------------------------------
    //  set game to game over
    //
    public void setGameGameOver(GameController controller) {
        setVisible(true);
        controller.getLogic().setGameOver(true);

        // disable things

        // show things

        boolean mainPlayerWon = true;
        for (MyPlayer p : controller.getPlayerList()) {
            if (controller.getPlayerByPosition(0).getLives() > p.getLives()) {
                mainPlayerWon = false;
            }
        }

        String text;
        if (mainPlayerWon) {
            text = controller.getView().getResources().getString(R.string.game_over_won);
        } else {
            text = controller.getView().getResources().getString(R.string.game_over_lost);
        }
        textField.setVisible(true);
        controller.getNonGamePlayUIContainer().getStatistics().setTitle(controller, text);
        endGameButton.setVisible(true);
        endGameButton.setEnabled(true);
        controller.getNonGamePlayUIContainer().getStatistics().setVisible(true);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public synchronized void draw(Canvas canvas) {
        if (isVisible()) {
            gameAreaOverlay.draw(canvas);
            gameAreaOverlay.draw(canvas);
            textField.draw(canvas);
            // button should get call on top of button bar windows!!
        }
    }


    //----------------------------------------------------------------------------------------------
    //  isGameOver
    //              -> checks is a player has reached 0 lives and therefore won the game
    //
    public boolean isGameOver(GameController controller) {
        for (MyPlayer player : controller.getPlayerList()) {
            if (player.getLives() <= 0 || player.getLives() > controller.getLogic().getMaxLives())
                return true;
        }
        return false;
    }


    //----------------------------------------------------------------------------------------------
    //  Touch Events
    //
    public void touchEventDown(int X, int Y) {
        if (!isVisible()) {
            return;
        }

        endGameButton.touchEventDown(X, Y);

    }

    public void touchEventMove(int X, int Y) {
        if (!isVisible()) {
            return;
        }

        endGameButton.touchEventMove(X, Y);
    }

    public boolean touchEventUp(int X, int Y, GameController controller) {
        if (!isVisible()) {
            return false;
        }
        if (endGameButton.touchEventUp(X, Y)) {
            backToMainMenu(controller);
            return true;
        }
        return false;
    }


    //----------------------------------------------------------------------------------------------
    //  endGame
    //
    private void backToMainMenu(final GameController controller) {
        controller.getView().stopAll = true;
        MainActivity mainActivity = (MainActivity) controller.getView().getContext();
        mainActivity.switchToLoadingScreen();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                MainActivity mainActivity = (MainActivity) controller.getView().getContext();
                mainActivity.finishGame(controller.getPlayerById(0).getLives() <= 0);
            }
        };
        handler.postDelayed(runnable, 100);
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public MyButton getEndGameButton() {
        return endGameButton;
    }
}
