package heroiceraser.mulatschak.game.DrawableObjects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Gravity;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.MyPlayer;
import heroiceraser.mulatschak.game.PlayerInfoPopUpView;

/**
 * Created by Daniel Metzner on 20.09.2017.
 */

public class PlayerInfo extends DrawableObject implements PlayerInfoPopUpView.Listener{

    private GameView view_;

    private List<Rect> rects_;
    private Paint rect_paint_;
    private int active_player_;

    private MyButton button_left_;
    private MyButton button_top_;
    private MyButton button_right_;
    private List<MyButton> buttons_;

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
        button_top_ = new MyButton();
        button_right_ = new MyButton();
        buttons_ = new ArrayList<>();

        buttons_.add(new MyButton());
        buttons_.add(button_left_);
        buttons_.add(button_top_);
        buttons_.add(button_right_);

        rects_ = new ArrayList<>();
        rect_paint_ = new Paint();
    }

    public void init(GameView view) {
        view_ = view;
        GameLayout layout = view.getController().getLayout();
        int active_player = GameController.NOT_SET;

        int padding = layout.getPlayerInfoSize().x / 12;
        if (padding < 1) {
            padding = 1;
        }

        setWidth(layout.getPlayerInfoSize().x);
        setHeight(layout.getPlayerInfoSize().y);
        setPopDimensions();

        if (view.getController().getAmountOfPlayers() > 1) {
            button_top_.init(view, layout.getPlayerInfoTopPos(),
                    layout.getPlayerInfoSize(), "lil_robo");
            pop_up_top_ = makePopupWindow(pop_up_top_view_, GameLayout.POSITION_TOP);
        }

        if (view.getController().getAmountOfPlayers() > 2) {
            button_left_.init(view, layout.getPlayerInfoLeftPos(),
                    layout.getPlayerInfoSize(), "lil_robo");
            pop_up_left_ = makePopupWindow(pop_up_left_view_, GameLayout.POSITION_LEFT);
        }

        if (view.getController().getAmountOfPlayers() > 3) {
            button_right_.init(view, layout.getPlayerInfoRightPos(),
                    layout.getPlayerInfoSize(), "lil_robo");
            pop_up_right_ = makePopupWindow(pop_up_right_view_, GameLayout.POSITION_RIGHT);
        }

        // active player rects
        for (int i = 0; i < view.getController().getAmountOfPlayers(); i++) {
            Rect rect = new Rect();
            if (i != 0) {

                rect.set(buttons_.get(i).getPosition().x - padding,
                        buttons_.get(i).getPosition().y - padding,
                        buttons_.get(i).getPosition().x + layout.getPlayerInfoSize().x + padding,
                        buttons_.get(i).getPosition().y + layout.getPlayerInfoSize().y + padding);

            }
            rects_.add(rect);
        }

        rect_paint_.setStyle(Paint.Style.FILL);
        rect_paint_.setColor(view.getResources().getColor(R.color.metallic_blue));
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
        MyPlayer p = view_.getController().getPlayerByPosition(pos);
        String top_display_name = p.getDisplayName();
        view.init(top_display_name);
        return new PopupWindow(view, pop_up_width_, pop_up_height_, true);
    }

    public void setActivePlayer(int id) {
        active_player_ = id;
    }

    public void draw(Canvas canvas) {
        if (isVisible()) {
            if (active_player_ >= 0 && active_player_ < rects_.size()){
                canvas.drawRect(rects_.get(active_player_), rect_paint_);
            }

            for (MyButton b : buttons_) {
                b.draw(canvas);
            }
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
        if (pop_up_top_ != null) {
            pop_up_top_.dismiss();
        }
        if (pop_up_left_ != null) {
            pop_up_left_.dismiss();
        }
        if (pop_up_right_ != null) {
            pop_up_right_.dismiss();
        }
    }

}
