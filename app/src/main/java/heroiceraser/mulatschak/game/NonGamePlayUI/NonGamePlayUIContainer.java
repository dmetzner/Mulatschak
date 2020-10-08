package heroiceraser.mulatschak.game.NonGamePlayUI;

import android.graphics.Canvas;

import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GamePlay.AllCardsPlayed.AllCardsPlayedView;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.NonGamePlayUI.Background.Background;
import heroiceraser.mulatschak.game.NonGamePlayUI.BottomBar.BottomBar;
import heroiceraser.mulatschak.game.NonGamePlayUI.BottomBar.GameMenu.GameMenu;
import heroiceraser.mulatschak.game.NonGamePlayUI.BottomBar.GameStatistics.GameStatistics;
import heroiceraser.mulatschak.game.NonGamePlayUI.BottomBar.GameTricks.GameTricks;
import heroiceraser.mulatschak.game.NonGamePlayUI.GameOver.GameOver;
import heroiceraser.mulatschak.game.NonGamePlayUI.PlayerInfo.ConnectionProblem;


//----------------------------------------------------------------------------------------------
//  Container with all UI Elements that are not directly game play
//
public class NonGamePlayUIContainer {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private Background background;
    private BottomBar button_bar_;                  // The bottom bar with its buttons

    // The functionality Overlay classes invoked by button bar the buttons
    private GameStatistics statistics_;
    private GameTricks tricks_;
    private GameMenu menu_;

    //
    private AllCardsPlayedView all_cards_played_view_;  // all cards played view
    private GameOver gameOver;

    private ConnectionProblem connectionProblem;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public NonGamePlayUIContainer() {
        button_bar_ = new BottomBar();
        statistics_ = new GameStatistics();
        tricks_ = new GameTricks();
        menu_ = new GameMenu();
        background = new Background();
        all_cards_played_view_ = new AllCardsPlayedView();
        gameOver = new GameOver();
        connectionProblem = new ConnectionProblem();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        background.init(view.getController().getLayout());
        button_bar_.init(view);
        statistics_.init(view);
        tricks_.init(view);
        menu_.init(view);
        all_cards_played_view_.init(view);
        gameOver.init(view);

        connectionProblem.init(view);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public synchronized void draw(Canvas canvas, GameController controller) {

        button_bar_.draw(canvas);

        gameOver.draw(canvas);

        // Display Overlays with the functionality
        statistics_.draw(canvas);
        tricks_.draw(canvas, controller);
        menu_.draw(canvas);

        all_cards_played_view_.draw(canvas, controller);
        gameOver.getEndGameButton().draw(canvas);

        connectionProblem.draw(canvas, controller);
    }

    public synchronized void drawBackground(Canvas canvas) {
        background.draw(canvas);
    }

    //----------------------------------------------------------------------------------------------
    //  isAWindowActive
    //
    public boolean isAWindowActive() {
        return statistics_.isVisible() ||
                tricks_.isVisible() ||
                menu_.isVisible();
    }


    //----------------------------------------------------------------------------------------------
    //  Close all windows
    //
    public void closeAllButtonBarWindows() {
        statistics_.setVisible(false);
        tricks_.setVisible(false);
        menu_.setVisible(false);
        button_bar_.setAllButtonInactive();
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public BottomBar getButtonBar() {
        return button_bar_;
    }

    public AllCardsPlayedView getAllCardsPlayedView() {
        return all_cards_played_view_;
    }

    public GameStatistics getStatistics() {
        return statistics_;
    }

    public GameTricks getTricks() {
        return tricks_;
    }

    public GameMenu getMenu() {
        return menu_;
    }

    public GameOver getGameOver() {
        return gameOver;
    }

    public ConnectionProblem getConnectionProblem() {
        return connectionProblem;
    }
}
