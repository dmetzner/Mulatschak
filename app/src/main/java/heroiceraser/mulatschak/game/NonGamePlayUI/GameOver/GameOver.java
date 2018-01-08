package heroiceraser.mulatschak.game.NonGamePlayUI.GameOver;

import android.graphics.Canvas;
import android.graphics.Point;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextButton;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextButton;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GamePlay.Background4Player0Animations;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.DrawableObjects.MyPlayer;
import heroiceraser.mulatschak.helpers.HelperFunctions;


//--------------------------------------------------------------------------------------------------
//  Game Over Class
//
public class GameOver {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private boolean visible;
    private Background4Player0Animations background;
    private MyTextButton endGameButton;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameOver() {
        super();
        background = new Background4Player0Animations();
        endGameButton = new MyTextButton();
        visible = false;
    }

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();

        // background
        background.init(layout);

        // button
        Point endGameButtonSize = new Point((int)(layout.getButtonBarBigButtonWidth() * 1.5),
                layout.getButtonBarBigButtonHeight());

        Point endGameButtonPosition = new Point(layout.getScreenWidth() / 2 - endGameButtonSize.x / 2,
                layout.getSectors().get(6).y);

        String text = view.getResources().getString(R.string.game_over_button);
        endGameButton.init(view, endGameButtonPosition,
                endGameButtonSize.x, endGameButtonSize.y, "button_blue_metallic_large", text);
        endGameButton.setVisible(false);

    }

    public void draw(Canvas canvas, GameController controller) {
        if (isVisible()) {
            background.draw(canvas, controller);
            //controller.getNonGamePlayUIContainer().getStatistics().setVisible(true);
            endGameButton.draw(canvas);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  isGameOver
    //              -> checks is a player has reached 0 lives and therefore won the game
    //
    public boolean isGameOver(GameController controller) {
        for (MyPlayer player : controller.getPlayerList()) {
            if (player.getLives() <= 0)
                return true;
        }
        return false;
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

}
