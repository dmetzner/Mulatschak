package heroiceraser.mulatschak.game.NonGamePlayUI.PlayerInfo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.Gravity;
import android.widget.PopupWindow;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.MyTextButton;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextButton;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextField;
import heroiceraser.mulatschak.DrawableBasicObjects.TextField;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.DrawableObjects.MyPlayer;

/**
 * Created by Daniel Metzner on 20.09.2017.
 */

public class PlayerInfo extends DrawableObject implements PlayerInfoPopUpView.Listener{

    private GameView view_;

    private MyTextField player0Text;
    private List<Rect> rects_;
    private Paint rect_paint_;
    private int active_player_;
    private boolean showPlayer0Turn;

    private MyTextButton button_left_;
    private MyTextButton button_top_;
    private MyTextButton button_right_;
    private List<MyTextButton> buttons_;

    private int pop_up_width_;
    private int pop_up_height_;
    private PopupWindow pop_up_top_;
    private PopupWindow pop_up_left_;
    private PopupWindow pop_up_right_;


    public PlayerInfo() {
        super();
        setVisible(false);

        button_left_ = new MyTextButton();
        button_top_ = new MyTextButton();
        button_right_ = new MyTextButton();
        buttons_ = new ArrayList<>();

        buttons_.add(new MyTextButton());
        buttons_.add(button_left_);
        buttons_.add(button_top_);
        buttons_.add(button_right_);

        rects_ = new ArrayList<>();
        rect_paint_ = new Paint();
        player0Text = new MyTextField();
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
                    layout.getPlayerInfoSize(), "lil_robo_0", "");
            pop_up_top_ = makePopupWindow(GameLayout.POSITION_TOP);
        }

        if (view.getController().getAmountOfPlayers() > 2) {
            button_left_.init(view, layout.getPlayerInfoLeftPos(),
                    layout.getPlayerInfoSize(), "lil_robo_1", "");
            pop_up_left_ = makePopupWindow(GameLayout.POSITION_LEFT);
        }

        if (view.getController().getAmountOfPlayers() > 3) {
            button_right_.init(view, layout.getPlayerInfoRightPos(),
                    layout.getPlayerInfoSize(), "lil_robo_2", "");
            pop_up_right_ = makePopupWindow(GameLayout.POSITION_RIGHT);
        }

        // active player rects
        for (int i = 0; i < view.getController().getAmountOfPlayers(); i++) {
            Rect rect = new Rect();
            if (i != 0) {

                // second player is on top pos if only 2 players
                if (view.getController().getAmountOfPlayers() == 2) {
                    rect.set(buttons_.get(2).getPosition().x - padding,
                            buttons_.get(2).getPosition().y - padding,
                            buttons_.get(2).getPosition().x + layout.getPlayerInfoSize().x + padding,
                            buttons_.get(2).getPosition().y + layout.getPlayerInfoSize().y + padding);
                }
                else {
                    rect.set(buttons_.get(i).getPosition().x - padding,
                            buttons_.get(i).getPosition().y - padding,
                            buttons_.get(i).getPosition().x + layout.getPlayerInfoSize().x + padding,
                            buttons_.get(i).getPosition().y + layout.getPlayerInfoSize().y + padding);
                }
            }
            rects_.add(rect);
        }

        String text = view.getResources().getString(R.string.player_info_active_player_is_0);
        player0Text.setText(text);
        TextPaint tp = new TextPaint();
        tp.setAntiAlias(true);
        tp.setTextSize(layout.getDealerButtonSize() * (2/3f));
        tp.setTextAlign(Paint.Align.CENTER);
        tp.setColor(Color.WHITE);
        player0Text.setTextPaint(tp);
        player0Text.setBorder(view.getResources().getColor(R.color.metallic_blue), 0.25f);
        player0Text.setMaxWidth(layout.getScreenWidth() / 2);
        player0Text.setPosition(new Point(layout.getScreenWidth() / 2,
                (int) (layout.getDealerButtonPosition(0).y + layout.getDealerButtonSize() / 2.0) ));
        player0Text.setVisible(true);

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

    private PopupWindow makePopupWindow(int pos) {
        PlayerInfoPopUpView view = new PlayerInfoPopUpView(view_.getContext());
        view.setListener(this);
        MyPlayer p = view_.getController().getPlayerByPosition(pos);
        String top_display_name = p.getDisplayName();
        Bitmap bitmap = null;
        String text = "";
        switch (pos) {
            case 1:
                bitmap = button_left_.getBitmap();
                text = "Beep, beep.";
                break;
            case 2:
                bitmap = button_top_.getBitmap();
                text = "Beep, beep, beep?";
                break;
            case 3:
                text = "Beeeeeep!";
                bitmap = button_right_.getBitmap();
                break;
        }
        view.init(top_display_name, text, bitmap);
        return new PopupWindow(view, pop_up_width_, pop_up_height_, true);
    }

    public void setActivePlayer(int id) {
        active_player_ = id;
    }

    public void draw(Canvas canvas) {
        if (isVisible()) {

            if (active_player_ == 0 && showPlayer0Turn) {
                player0Text.draw(canvas);
            }

            if (active_player_ >= 0 && active_player_ < rects_.size()){
                canvas.drawRect(rects_.get(active_player_), rect_paint_);
            }

            for (MyTextButton b : buttons_) {
                b.draw(canvas);
            }
        }
    }

    public MyTextButton getButtonLeft() {
        return button_left_;
    }

    public MyTextButton getButtonTop() {
        return button_top_;
    }

    public MyTextButton getButtonRight() {
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

    public void setShowPlayer0Turn(boolean showPlayer0Turn) {
        this.showPlayer0Turn = showPlayer0Turn;
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
