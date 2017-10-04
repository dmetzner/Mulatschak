package heroiceraser.mulatschak.game.DrawableObjects;

import android.graphics.Canvas;
import android.graphics.Point;
import android.text.Layout;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.DrawableBasicObjects.MySimpleTextField;
import heroiceraser.mulatschak.game.ButtonBarWindowTitle;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 27.08.2017.
 */

public class GameTricks extends DrawableObject {

    private GameView view_;
    private ButtonBarWindowTitle title_;
    private MySimpleTextField subtitle_;
    private MySimpleTextField info_;
    private MyButton arrow_left_;
    private MyButton arrow_right_;
    private List<DiscardPile> discard_piles_;
    private int visible_round_id_;

    public GameTricks() {
        super();
        title_ = new ButtonBarWindowTitle();
        subtitle_ = new MySimpleTextField();
        info_ = new MySimpleTextField();
        arrow_left_ = new MyButton();
        arrow_right_ = new MyButton();
        discard_piles_ = new ArrayList<>();
    }

    public void init(GameView view) {
        this.view_ = view;
        GameLayout layout = view.getController().getLayout();

        // background
        setPosition(layout.getButtonBarWindowPosition());
        setWidth(layout.getButtonBarWindowSize().x);
        setHeight(layout.getButtonBarWindowSize().y);
        setBitmap(HelperFunctions.loadBitmap(view, "statistics_background", getWidth(), getHeight()));

        // title
        String title_text = view.getResources().getString(R.string.button_bar_tricks_title);
        title_.init(view.getController(), title_text);

        // info
        int info_text_size = (int) view.getResources().getDimension(R.dimen.button_bar_window_text_size);
        int info_color = view.getResources().getColor(R.color.button_bar_window_text_color);
        String info_text = view.getResources().getString(R.string.button_bar_tricks_empty_info);
        Point info_pos = new Point(layout.getButtonBarWindowTricksSubtitlePosition());
        info_pos.y += layout.getSectors().get(1).y;
        Point info_max_size = new Point(layout.getButtonBarWindowTricksSubtitleMaxSize());
        info_max_size.y += layout.getSectors().get(1).y * 1.7;
        info_.init(view, info_text_size, info_color, info_pos, info_text,
                Layout.Alignment.ALIGN_CENTER, info_max_size.x, info_max_size.y);


        // subtitle
        int subtitle_text_size = (int) view.getResources().getDimension(R.dimen.button_bar_window_subtitle_size);
        int subtitle_color = view.getResources().getColor(R.color.button_bar_window_text_color);
        Point subtitle_pos = layout.getButtonBarWindowTricksSubtitlePosition();
        Point subtitle_max_size = layout.getButtonBarWindowTricksSubtitleMaxSize();
        subtitle_.init(view, subtitle_text_size, subtitle_color, subtitle_pos, "",
                Layout.Alignment.ALIGN_CENTER, subtitle_max_size.x, subtitle_max_size.y);

        // arrow buttons
        arrow_left_.init(view, layout.getButtonBarWindowTricksLeftArrowPosition(),
                layout.getButtonBarWindowTricksArrowSize(), "drawable/tricks_arrow_left");
        arrow_right_.init(view, layout.getButtonBarWindowTricksRightArrowPosition(),
                layout.getButtonBarWindowTricksArrowSize(), "drawable/tricks_arrow_right");
        arrow_left_.setVisible(false);
        arrow_right_.setVisible(false);

        // discardPiles
        visible_round_id_ = GameController.NOT_SET;

    }

    public void draw(Canvas canvas) {
        if (isVisible()) {
            // background
            canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);

            // title
            title_.draw(canvas);

            // info
            info_.draw(canvas);

            //subtitle
            subtitle_.draw(canvas);

            // arrow buttons
            arrow_left_.draw(canvas);
            arrow_right_.draw(canvas);

            // tricks -> discardPiles;
            if (visible_round_id_ != GameController.NOT_SET && visible_round_id_ >= 0 &&
                    discard_piles_ != null && visible_round_id_ < discard_piles_.size()) {
                discard_piles_.get(visible_round_id_).draw(canvas);
            }
        }

    }


    public void showNextRound() {
        visible_round_id_++;
        if (visible_round_id_ >= discard_piles_.size()) {
            visible_round_id_ = 0;
        }
        updateVisibleRound();
    }

    public void showPrevRound() {
        visible_round_id_--;
        if (visible_round_id_ < 0) {
            visible_round_id_ = discard_piles_.size() - 1;
        }
        updateVisibleRound();
    }

    public void updateVisibleRound() {
        info_.setVisible(false);
        subtitle_.setText(view_.getResources().getString(R.string.button_bar_tricks_subtitle) + " "
                + (visible_round_id_ + 1));
        subtitle_.update();
        subtitle_.setVisible(true);
        arrow_left_.setVisible(true);
        arrow_right_.setVisible(true);
    }

    public void clear() {
        info_.setVisible(true);
        discard_piles_.clear();
        arrow_left_.setVisible(false);
        arrow_right_.setVisible(false);
        subtitle_.setVisible(false);
        visible_round_id_ = GameController.NOT_SET;
    }


    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //

    public List getDiscardPiles() {
        return discard_piles_;
    }

    public void setVisibleRoundId(int id) {
        visible_round_id_ = id;
    }

    public MyButton getArrowButtonLeft() {
        return arrow_left_;
    }

    public MyButton getArrowButtonRight() {
        return arrow_right_;
    }
}
