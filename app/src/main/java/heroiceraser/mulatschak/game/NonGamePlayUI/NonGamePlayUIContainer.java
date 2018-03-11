package heroiceraser.mulatschak.game.NonGamePlayUI;

import android.graphics.Canvas;

import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.GamePlay.AllCardsPlayed.AllCardsPlayedView;
import heroiceraser.mulatschak.game.NonGamePlayUI.Background.Background;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar.ButtonBar;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar.ButtonBarDecoration;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBarWindows.GameChat.GameChat;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBarWindows.GameMenu.GameMenu;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBarWindows.GameStatistics.GameStatistics;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBarWindows.GameTricks.GameTricks;
import heroiceraser.mulatschak.game.NonGamePlayUI.GameOver.GameOver;
import heroiceraser.mulatschak.game.NonGamePlayUI.ChatView.ChatView;
import heroiceraser.mulatschak.game.NonGamePlayUI.PlayerInfo.ConnectionProblem;
import heroiceraser.mulatschak.game.NonGamePlayUI.SideBorders.SideBorders;


//----------------------------------------------------------------------------------------------
//  Container with all UI Elements that are not directly game play
//
public class NonGamePlayUIContainer {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private Background background;
    private ChatView chatView;
    private SideBorders side_borders_;              // decorative borders on the left/right side
    private ButtonBarDecoration decoration_;
    private ButtonBar button_bar_;                  // The bottom bar with its buttons

    // The functionality Overlay classes invoked by button bar the buttons
    private GameStatistics statistics_;
    private GameTricks tricks_;
    private GameMenu menu_;
    private GameChat chat;

    //
    private AllCardsPlayedView all_cards_played_view_;  // all cards played view
    private GameOver gameOver;

    private ConnectionProblem connectionProblem;



    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public NonGamePlayUIContainer() {
        background = new Background();
        side_borders_ = new SideBorders();
        decoration_ = new ButtonBarDecoration();
        button_bar_ = new ButtonBar();
        statistics_ = new GameStatistics();
        tricks_ = new GameTricks();
        menu_ = new GameMenu();

        all_cards_played_view_ = new AllCardsPlayedView();
        gameOver = new GameOver();

        chatView = new ChatView();
        chat = new GameChat();

        connectionProblem = new ConnectionProblem();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        background.init(view.getController().getLayout());
        chatView.init(view);
        side_borders_.init(view);
        decoration_.init(view);
        button_bar_.init(view);
        statistics_.init(view);
        tricks_.init(view);
        menu_.init(view);
        chat.init(view);
        all_cards_played_view_.init(view);
        gameOver.init(view);

        connectionProblem.init(view);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas, GameController controller) {

        chatView.draw(canvas);
        button_bar_.draw(canvas);

        gameOver.draw(canvas);

        // Display Overlays with the functionality
        statistics_.draw(canvas);
        tricks_.draw(canvas, controller);
        menu_.draw(canvas);

        all_cards_played_view_.draw(canvas, controller);
        gameOver.getEndGameButton().draw(canvas);

        chat.draw(canvas);

        // additional layout decoration
        side_borders_.draw(canvas);
        decoration_.draw(canvas);

        connectionProblem.draw(canvas, controller);
    }

    public void drawBackground(Canvas canvas) {
        background.draw(canvas);
    }

    //----------------------------------------------------------------------------------------------
    //  isAWindowActive
    //
    public boolean isAWindowActive() {
        return statistics_.isVisible() ||
                tricks_.isVisible() ||
                menu_.isVisible() ||
                chat.isVisible();
    }


    //----------------------------------------------------------------------------------------------
    //  Close all windows
    //
    public void closeAllButtonBarWindows() {
        statistics_.setVisible(false);
        chat.setVisible(false);
        tricks_.setVisible(false);
        menu_.setVisible(false);
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public ButtonBar getButtonBar() {
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

    public ChatView getChatView() {
        return chatView;
    }

    public GameChat getChat() {
        return chat;
    }

    public ConnectionProblem getConnectionProblem() {
        return connectionProblem;
    }

}
