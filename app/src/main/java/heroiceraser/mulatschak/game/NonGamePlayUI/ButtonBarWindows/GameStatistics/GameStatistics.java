package heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBarWindows.GameStatistics;

import android.graphics.Canvas;
import android.graphics.Point;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.MySimpleTextField;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.DrawableObjects.MyPlayer;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBarWindows.ButtonBarWindow;


//----------------------------------------------------------------------------------------------
//  GameStatistics - ButtonBarWindow
//
public class GameStatistics extends ButtonBarWindow {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private int players_;
    private List<MyPlayer> playerList;
    private TextPaint text_paint_;
    private ArrayList<StaticLayout> display_name_layouts_;
    private Point display_name_text_pos_;
    private ArrayList<StaticLayout> player_lives_layouts_;
    private Point player_lives_text_pos_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameStatistics() {
        super();
        playerList = new ArrayList<>();
    }

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();

        //---- background
        super.init(view);

        //---- title
        String title_text = view.getResources()
                .getString(R.string.button_bar_state_of_the_game_title);
        super.titleInit(view.getController(), title_text);

        //---- state of the game
        String font = "fonts/nk57-monospace-no-rg.ttf";
        int text_size = (int) (layout.getSectors().get(1).y * 0.23);
        int text_color = view.getResources().getColor(R.color.button_bar_window_text_color);
        text_paint_ = MySimpleTextField.createTextPaint(view, text_size, text_color, font);

        display_name_text_pos_ = new Point((int) (layout.getOnePercentOfScreenWidth() * 10),
                (int) (layout.getSectors().get(1).y * 3.7));

        setPlayerList(view.getController());
        initDisplayNameLayouts(view.getController());
        initPlayerLivesLayouts(view.getController());
        setPlayerLivesPosition(layout);

        players_ = view.getController().getAmountOfPlayers();
    }

    //----------------------------------------------------------------------------------------------
    //  setTitle
    //
    public void setTitle(GameController controller, String text) {
        super.titleInit(controller, text);
    }

    //----------------------------------------------------------------------------------------------
    //  UpdatePlayerLives
    //
    public void updatePlayerLives(GameController controller) {
        setPlayerList(controller);
        initPlayerLivesLayouts(controller);
        initDisplayNameLayouts(controller);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas) {
        if (isVisible()) {

            super.drawBackground(canvas);
            super.drawTitle(canvas);

            // display names & lives
            drawStateOfTheGame(canvas, players_);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  draw State of the game
    //                          -> draw player names and their player lives
    //
    //                                    name_1      *lives*
    //                                    name_2      *lives*
    //                                    name_3      *lives*
    //                                    ...
    //
    private void drawStateOfTheGame(Canvas canvas, int players) {

        for (int i = 0; i < players; i++) {
            canvas.save();
            Point pos = new Point(display_name_text_pos_);
            pos.y += (int) (text_paint_.getTextSize() * 2) * i;

            canvas.translate(pos.x, pos.y);
            if (display_name_layouts_.size() > i && display_name_layouts_.get(i) != null) {
                display_name_layouts_.get(i).draw(canvas);
            }

            canvas.translate(player_lives_text_pos_.x, 0);
            if (player_lives_layouts_.size() > i &&  player_lives_layouts_.get(i) != null) {
                player_lives_layouts_.get(i).draw(canvas);
            }

            canvas.restore();
        }
    }


    private void setPlayerList(GameController controller) {

        playerList.clear();

        List<MyPlayer> unsorted = new ArrayList<>();
        unsorted.addAll(controller.getPlayerList());

        for (int i = 0; i < unsorted.size(); i++) {
            int min = 0;
            for (int j = i; j < unsorted.size(); j++) {
                if (unsorted.get(j).getLives() < unsorted.get(i).getLives()) {
                    min = j;
                }
            }
            playerList.add(unsorted.get(min));
            unsorted.remove(min);
            i--;
        }
    }

    //----------------------------------------------------------------------------------------------
    //  initDisplayNameLayouts
    //
    private void initDisplayNameLayouts(GameController controller) {
        display_name_layouts_ = new ArrayList<>();
        int max_width = (int) (controller.getLayout().getOnePercentOfScreenWidth() * 65);

        for (int i = 0; i < controller.getAmountOfPlayers(); i++) {

            String text = (i + 1) + ". " + playerList.get(i).getDisplayName() + ":";

            // reduce too large names!
            while (text_paint_.measureText(text) > max_width) {
                text = text.substring(0, text.length() - 2) + ":";
            }
            StaticLayout display_name_layout = new StaticLayout(text, text_paint_,
                    max_width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);

            int max_height = (int) (controller.getLayout().getSectors().get(1).y * 0.5);
            while (display_name_layout.getHeight() > max_height) {
                MySimpleTextField.reduceTextSize(text_paint_);
                display_name_text_pos_.y = display_name_text_pos_.y + (int)
                        (controller.getLayout().getSectors().get(1).y * 0.1);
                display_name_layout = new StaticLayout(text, text_paint_,
                        max_width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
            }
            display_name_layouts_.add(i, display_name_layout);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  initPlayerLivesLayouts
    //
    private void initPlayerLivesLayouts(GameController controller) {
        player_lives_layouts_ = new ArrayList<>();
        int max_width = (int) (controller.getLayout().getOnePercentOfScreenWidth() * 20);

        for (int i = 0; i < controller.getAmountOfPlayers(); i++) {
            String player_lives_text = "" + playerList.get(i).getLives();

            StaticLayout player_lives_layout = new StaticLayout(player_lives_text, text_paint_,
                    max_width, Layout.Alignment.ALIGN_OPPOSITE, 1.0f, 0, false);

            player_lives_layouts_.add(i, player_lives_layout);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  setPlayerLivesPosition
    //
    private void setPlayerLivesPosition(GameLayout layout) {
        if (player_lives_layouts_ != null &&
                player_lives_layouts_.size() > 1 &&
                player_lives_layouts_.get(0) != null) {

            player_lives_text_pos_ = new Point(0, 0);

            player_lives_text_pos_.x = layout.getScreenWidth()
                    - 2 * display_name_text_pos_.x - player_lives_layouts_.get(0).getWidth();
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Touch Events
    //                 window close button
    //
    public void touchEventDown(int X, int Y){
        if (!isVisible()) {
            return;
        }
        getCloseButton().touchEventDown(X, Y);
    }

    public void touchEventMove(int X, int Y) {
        if (!isVisible()) {
            return;
        }
        getCloseButton().touchEventMove(X, Y);
    }

    public void touchEventUp(int X, int Y) {
        if (!isVisible()) {
            return;
        }
        if (getCloseButton().touchEventUp(X, Y)) {
            this.setVisible(false);
        }
    }

}
