package heroiceraser.mulatschak.game.NonGamePlayUI;

import android.graphics.Canvas;

import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.GamePlay.AllCardsPlayed.AllCardsPlayedView;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar.ButtonBar;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar.ButtonBarDecoration;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar.Windows.GameChat;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar.Windows.GameMenu.GameMenu;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar.Windows.GameStatistics;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar.Windows.GameTricks;
import heroiceraser.mulatschak.game.NonGamePlayUI.GameOver.GameOver;
import heroiceraser.mulatschak.game.NonGamePlayUI.RoundInfo.ChatView;
import heroiceraser.mulatschak.game.NonGamePlayUI.SideBorders.SideBorders;


//----------------------------------------------------------------------------------------------
//  Container with all UI Elements that are not directly game play
//
public class NonGamePlayUIContainer {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
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



    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public NonGamePlayUIContainer() {
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

    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
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
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas, GameController controller) {
        chatView.draw(canvas);
        button_bar_.draw(canvas);
        gameOver.draw(canvas, controller);

        // Display Overlays with the functionality
        statistics_.draw(canvas, controller);
        tricks_.draw(canvas, controller);
        menu_.draw(canvas, controller);

        all_cards_played_view_.draw(canvas, controller);
        gameOver.getEndGameButton().draw(canvas);

        chat.draw(canvas, controller);

        // additional layout decoration
        side_borders_.draw(canvas);
        decoration_.draw(canvas);
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

}
