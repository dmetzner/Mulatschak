package heroiceraser.mulatschak.game.DrawableObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;

import heroiceraser.mulatschak.DrawableBasicObjects.Button;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.DrawableBasicObjects.TextField;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.Player;

/**
 * Created by Daniel Metzner on 20.09.2017.
 */

public class PlayerInfo extends DrawableObject {

    private GameView view_;

    private Button button_left_;
    private Button button_top_;
    private Button button_right_;

    private TextField player_bottom_;
    private TextField player_left_;
    private TextField player_top_;
    private TextField player_right_;

    public PlayerInfo() {
        super();
        setVisible(false);

        button_left_ = new Button();
        button_right_ = new Button();
        button_top_ = new Button();

        player_bottom_ = new TextField();
        player_left_ = new TextField();
        player_top_ = new TextField();
        player_right_ = new TextField();
    }

    public void init(GameView view) {
        view_ = view;
        setVisible(true);

        GameLayout layout = view.getController().getLayout();

        button_left_.init(view, layout.getPlayerInfoLeftPos(),
                layout.getPlayerInfoSize(), "player_info_bg");

        button_top_.init(view, layout.getPlayerInfoTopPos(),
                layout.getPlayerInfoSize(), "player_info_bg");

        button_right_.init(view, layout.getPlayerInfoRightPos(),
                layout.getPlayerInfoSize(), "player_info_bg");


        int max_size = view.getController().getLayout().getScreenWidth() / 2;
        int color = Color.GREEN;
        player_bottom_.init(view, "", max_size, color);
        player_left_.init(view, "", max_size, color);
        player_top_.init(view, "", max_size, color);
        player_right_.init(view, "", max_size, color);
    }

    public void updateTextField(Player player) {

        String text = "";

        if (view_.getController().my_participant_id_ != null) {
            text = view_.getController().my_participant_id_;
        }
        else if (player.getId() == 0) {
            text = view_.getController().my_display_name_;
        }
        else {
            text = "enemie_bot" + player.getId();
        }

        switch (player.getPosition()) {
            case 0:
                player_bottom_.update(text, 500);
                player_bottom_.setVisible(true);
                break;
            case 1:
                player_left_.update(text, 500);
                player_left_.setVisible(true);
                break;
            case 2:
                player_top_.update(text, 500);
                player_top_.setVisible(true);
                break;
            case 3:
                player_right_.update(text, 500);
                player_right_.setVisible(true);
                break;
        }
    }

    public void draw(Canvas canvas) {
        if (isVisible()) {

            button_left_.draw(canvas);
            button_top_.draw(canvas);
            button_right_.draw(canvas);

            // player_bottom_.draw(canvas, view_.getController().getLayout().getHandBottom());
            // player_left_.draw(canvas,  view_.getController().getLayout().getHandLeft());
            // player_top_.draw(canvas,  view_.getController().getLayout().getHandTop());
            // player_right_.draw(canvas,  view_.getController().getLayout().getHandRight());

        }
    }

    public Button getButtonLeft() {
        return button_left_;
    }

    public Button getButtonTop() {
        return button_top_;
    }

    public Button getButtonRight() {
        return button_right_;
    }

}
