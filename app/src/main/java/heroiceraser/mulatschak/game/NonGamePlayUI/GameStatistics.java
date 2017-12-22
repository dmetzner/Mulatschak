package heroiceraser.mulatschak.game.NonGamePlayUI;

import android.graphics.Canvas;
import android.graphics.Point;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.util.ArrayList;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.DrawableBasicObjects.MySimpleTextField;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.ButtonBarWindowTitle;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 23.08.2017.
 */

public class GameStatistics extends DrawableObject{

    // Title
    private ButtonBarWindowTitle title_;

    // state of the game
    private int players_;
    private TextPaint text_paint_;
    private ArrayList<StaticLayout> display_name_layouts_;
    private Point display_name_text_pos_;
    private ArrayList<StaticLayout> player_lives_layouts_;
    private Point player_lives_text_pos_;

    public GameStatistics() {
        super();
        title_ = new ButtonBarWindowTitle();
    }

    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();

        //---- background
        setPosition(layout.getButtonBarWindowPosition());
        setWidth(layout.getButtonBarWindowSize().x);
        setHeight(layout.getButtonBarWindowSize().y);
        setBitmap(HelperFunctions.loadBitmap(view, "statistics_background", getWidth(), getHeight()));

        //---- title
        String title_text = view.getResources()
                .getString(R.string.button_bar_state_of_the_game_title);
        title_.init(view.getController(), title_text);

        // state of the game
        String font = "fonts/nk57-monospace-no-rg.ttf";
        int text_size = view.getResources().getDimensionPixelSize(R.dimen.button_bar_window_text_size);
        int text_color = view.getResources().getColor(R.color.button_bar_window_text_color);
        text_paint_ = MySimpleTextField.createTextPaint(view, text_size, text_color, font);

        display_name_text_pos_ = new Point((int) (layout.getScreenWidth() * (1.0/10.0)),
                (int) (getPosition().y + layout.getSectors().get(1).y * 1.7));

        initDisplayNameLayouts(view.getController());

        initPlayerLivesLayouts(view.getController());

        setPlayerLivesPosition(layout);

        players_ = view.getController().getAmountOfPlayers();
    }

    public void updatePlayerLives(GameController controller) {
        initPlayerLivesLayouts(controller);
    }

    public void draw(Canvas canvas) {
        if (isVisible()) {

            // draw background
            canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);

            // title
            title_.draw(canvas);

            // display names & lives
            drawStateOfTheGame(canvas, players_);
        }
    }


    private void drawStateOfTheGame(Canvas canvas, int players) {
        Point pos = new Point(display_name_text_pos_);

        for (int i = 0; i < players; i++) {
            canvas.save();

            canvas.translate(pos.x, pos.y);
            if (display_name_layouts_.size() > i && display_name_layouts_.get(i) != null) {
                display_name_layouts_.get(i).draw(canvas);
            }

            canvas.translate(player_lives_text_pos_.x, 0);
            if (player_lives_layouts_.size() > i &&  player_lives_layouts_.get(i) != null) {
                player_lives_layouts_.get(i).draw(canvas);
            }

            canvas.restore();
            pos.y += (int) (text_paint_.getTextSize() * 2);
        }
    }


    private void initDisplayNameLayouts(GameController controller) {
        display_name_layouts_ = new ArrayList<>();
        int max_width = (int) (controller.getLayout().getScreenWidth() * (7.0/10.0));

        for (int i = 0; i < controller.getAmountOfPlayers(); i++) {
            String text = controller.getPlayerById(i).getDisplayName() + ":";

            // reduce too large names!
            while (text_paint_.measureText(text) > max_width) {
                text = text.substring(1, text.length() - 2) + ":";
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

    private void initPlayerLivesLayouts(GameController controller) {
        player_lives_layouts_ = new ArrayList<>();
        int max_width = (int) (controller.getLayout().getScreenWidth() * (2/10.0));

        for (int i = 0; i < controller.getAmountOfPlayers(); i++) {
            String player_lives_text = "" + controller.getPlayerById(i).getLives();

            StaticLayout player_lives_layout = new StaticLayout(player_lives_text, text_paint_,
                    max_width, Layout.Alignment.ALIGN_OPPOSITE, 1.0f, 0, false);

            player_lives_layouts_.add(i, player_lives_layout);
        }
    }

    private void setPlayerLivesPosition(GameLayout layout) {
        if (player_lives_layouts_ != null &&
                player_lives_layouts_.size() > 1 &&
                player_lives_layouts_.get(0) != null) {

            player_lives_text_pos_ = new Point(0, 0);

            player_lives_text_pos_.x = layout.getScreenWidth()
                    - 2 * display_name_text_pos_.x - player_lives_layouts_.get(0).getWidth();
        }
    }

}
