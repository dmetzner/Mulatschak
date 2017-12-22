package heroiceraser.mulatschak.game.NonGamePlayUI;

import android.graphics.Canvas;

import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 22.12.2017.
 */

//----------------------------------------------------------------------------------------------
//  Container with all UI Elements that are not directly game play
//
public class NonGamePlayUIContainer {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private RoundInfo round_info_;                  // round info box at the top
    private SideBorders side_borders_;              // decorative borders on the left/right side
    private ButtonBarDecoration decoration_;
    private ButtonBar button_bar_;                  // The bottom bar with its buttons

    // The functionality Overlay classes invoked by button bar the buttons
    private GameStatistics statistics_;
    private GameTricks tricks_;
    private GameMenu menu_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public NonGamePlayUIContainer() {
        round_info_ = new RoundInfo();
        side_borders_ = new SideBorders();
        decoration_ = new ButtonBarDecoration();
        button_bar_ = new ButtonBar();
        statistics_ = new GameStatistics();
        tricks_ = new GameTricks();
        menu_ = new GameMenu();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        round_info_.init(view);
        side_borders_.init(view);
        decoration_.init(view);
        button_bar_.init(view);
        statistics_.init(view);
        tricks_.init(view);
        menu_.init(view);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas) {

        round_info_.draw(canvas);

        button_bar_.draw(canvas);

        // Display Overlays with the functionality
        statistics_.draw(canvas);
        tricks_.draw(canvas);
        menu_.draw(canvas);

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
                menu_.isVisible();
    }

    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public RoundInfo getRoundInfo() {
        return round_info_;
    }

    public ButtonBar getButtonBar() {
        return button_bar_;
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

}
