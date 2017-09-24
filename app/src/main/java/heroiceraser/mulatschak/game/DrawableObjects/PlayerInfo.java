package heroiceraser.mulatschak.game.DrawableObjects;

import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.Gravity;
import android.widget.PopupWindow;

import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.Player;
import heroiceraser.mulatschak.game.PlayerInfoPopUpView;

/**
 * Created by Daniel Metzner on 20.09.2017.
 */

public class PlayerInfo extends DrawableObject implements PlayerInfoPopUpView.Listener{

    private GameView view_;

    private MyButton button_left_;
    private MyButton button_top_;
    private MyButton button_right_;

    private int pop_up_width_;
    private int pop_up_height_;
    private PopupWindow pop_up_top_;
    private PlayerInfoPopUpView pop_up_top_view_;
    private PopupWindow pop_up_left_;
    private PlayerInfoPopUpView pop_up_left_view_;
    private PopupWindow pop_up_right_;
    private PlayerInfoPopUpView pop_up_right_view_;



    public PlayerInfo() {
        super();
        setVisible(false);

        button_left_ = new MyButton();
        button_right_ = new MyButton();
        button_top_ = new MyButton();

    }

    public void init(GameView view) {
        view_ = view;
        GameLayout layout = view.getController().getLayout();

        setWidth(layout.getPlayerInfoSize().x);
        setHeight(layout.getPlayerInfoSize().y);

        button_left_.init(view, layout.getPlayerInfoLeftPos(),
                layout.getPlayerInfoSize(), "lil_robo");

        button_top_.init(view, layout.getPlayerInfoTopPos(),
                layout.getPlayerInfoSize(), "lil_robo");

        button_right_.init(view, layout.getPlayerInfoRightPos(),
                layout.getPlayerInfoSize(), "lil_robo");

        setPopDimensions();
        pop_up_left_ = makePopupWindow(pop_up_left_view_, GameLayout.POSITION_LEFT);
        pop_up_top_ = makePopupWindow(pop_up_top_view_, GameLayout.POSITION_TOP);
        pop_up_right_ = makePopupWindow(pop_up_right_view_, GameLayout.POSITION_RIGHT);

        setVisible(true);

    }

    private void setPopDimensions() {
        pop_up_width_ = (int) view_.getResources()
                .getDimension(R.dimen.player_info_pop_up_width);
        pop_up_height_ = (int) view_.getResources()
                .getDimension(R.dimen.player_info_pop_up_height);
    }

    private PopupWindow makePopupWindow(PlayerInfoPopUpView view, int pos) {
        view = new PlayerInfoPopUpView(view_.getContext());
        view.setListener(this);
        Player p = view_.getController().getPlayerByPosition(pos);
        setDisplayName(p);
        String top_display_name = p.getDisplayName();
        view.init(top_display_name);
        return new PopupWindow(view, pop_up_width_, pop_up_height_, true);
    }

    public void setDisplayName(Player player) {

        String text = "";

        if (view_.getController().my_participant_id_ != null) {
            text = view_.getController().my_participant_id_;
        }
        else if (player.getId() == 0) {
            text = view_.getController().my_display_name_;
        }
        else {
            text = "Muli Bot " + player.getId();
        }
        player.setDisplayName(text);
    }

    public void draw(Canvas canvas) {
        if (isVisible()) {
            button_left_.draw(canvas);
            button_top_.draw(canvas);
            button_right_.draw(canvas);
        }
    }

    public MyButton getButtonLeft() {
        return button_left_;
    }

    public MyButton getButtonTop() {
        return button_top_;
    }

    public MyButton getButtonRight() {
        return button_right_;
    }

    public void popUpInfoLeft() {
        Point pos = view_.getController().getLayout().getPlayerInfoLeftPos();
        pop_up_left_.showAtLocation(view_, Gravity.NO_GRAVITY,
                pos.x, pos.y - (int) (pop_up_height_ / 3.0) + (getHeight() / 2));
    }
    public void popUpInfoTop() {
        Point pos = view_.getController().getLayout().getPlayerInfoTopPos();
        pop_up_top_.showAtLocation(view_, Gravity.NO_GRAVITY,
                pos.x - (int) (pop_up_width_ / 3.0) + (getWidth() / 2), pos.y);

    }
    public void popUpInfoRight() {
        Point pos = view_.getController().getLayout().getPlayerInfoRightPos();
        pop_up_right_.showAtLocation(view_, Gravity.NO_GRAVITY,
                pos.x  - pop_up_width_ + getWidth(),
                pos.y);

    }

    @Override
    public void onBackButtonRequested() {
        pop_up_top_.dismiss();
        pop_up_left_.dismiss();
        pop_up_right_.dismiss();
    }

}
