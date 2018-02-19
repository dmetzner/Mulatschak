package heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBarWindows.GameTricks;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.Layout;
import android.text.TextPaint;
import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.DrawableBasicObjects.MySimpleTextField;
import heroiceraser.mulatschak.game.BaseObjects.DiscardPile;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBarWindows.ButtonBarWindow;
import heroiceraser.mulatschak.helpers.HelperFunctions;


//--------------------------------------------------------------------------------------------------
// Game Tricks
//
public class GameTricks extends ButtonBarWindow {

    //----------------------------------------------------------------------------------------------
    // MemberVariables
    //
    private GameView view_;
    private MySimpleTextField subtitle_;
    private MySimpleTextField info_;
    private MyButton arrow_left_;
    private MyButton arrow_right_;
    private List<DiscardPile> discard_piles_;
    private Bitmap card_back_bitmap_;
    private List<Integer> winners_;
    private TextPaint winner_name_text_paint_;
    private int visible_round_id_;


    //----------------------------------------------------------------------------------------------
    // Constructor
    //
    public GameTricks() {
        super();
        subtitle_ = new MySimpleTextField();
        info_ = new MySimpleTextField();
        arrow_left_ = new MyButton();
        arrow_right_ = new MyButton();
        discard_piles_ = new ArrayList<>();
        winners_ = new ArrayList<>();
        winner_name_text_paint_ = new TextPaint();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        this.view_ = view;
        GameLayout layout = view.getController().getLayout();

        // background
        super.init(view);

        // title
        String title_text = view.getResources().getString(R.string.button_bar_tricks_title);
        super.titleInit(view.getController(), title_text);

        // info text if no tricks were made till now
        int info_text_size = (int) (layout.getSectors().get(1).y * 0.30);
        int info_color = view.getResources().getColor(R.color.button_bar_window_text_color);
        String info_text = view.getResources().getString(R.string.button_bar_tricks_empty_info);
        Point info_pos = new Point(layout.getButtonBarWindowTricksSubtitlePosition());
        info_pos.y += layout.getSectors().get(1).y;
        Point info_max_size = new Point(layout.getButtonBarWindowTricksSubtitleMaxSize());
        info_max_size.y += layout.getSectors().get(1).y * 1.7;
        info_.init(view, info_text_size, info_color, info_pos, info_text,
                Layout.Alignment.ALIGN_CENTER, info_max_size.x, info_max_size.y);

        // subtitle
        int subtitle_text_size = (int) (layout.getSectors().get(1).y * 0.40);
        int subtitle_color = view.getResources().getColor(R.color.button_bar_window_text_color);
        Point subtitle_pos = layout.getButtonBarWindowTricksSubtitlePosition();
        Point subtitle_max_size = layout.getButtonBarWindowTricksSubtitleMaxSize();
        subtitle_.init(view, subtitle_text_size, subtitle_color, subtitle_pos, "",
                Layout.Alignment.ALIGN_CENTER, subtitle_max_size.x, subtitle_max_size.y);

        winner_name_text_paint_.setAntiAlias(true);
        winner_name_text_paint_.setColor(Color.WHITE);
        winner_name_text_paint_.setTextSize(subtitle_text_size);
        winner_name_text_paint_.setTextAlign(Paint.Align.CENTER);

        // arrow buttons
        arrow_left_.init(view, layout.getButtonBarWindowTricksLeftArrowPosition(),
                layout.getButtonBarWindowTricksArrowSize(), "drawable/tricks_arrow_left", "");
        arrow_right_.init(view, layout.getButtonBarWindowTricksRightArrowPosition(),
                layout.getButtonBarWindowTricksArrowSize(), "drawable/tricks_arrow_right", "");
        arrow_left_.setVisible(false);
        arrow_right_.setVisible(false);

        // discardPiles
        // discard piles get added via the controller after every round
        card_back_bitmap_ = HelperFunctions.loadBitmap(view, "card_back",
                view.getController().getLayout().getCardWidth(),
                view.getController().getLayout().getCardHeight());
        visible_round_id_ = GameController.NOT_SET;
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas, GameController controller) {
        if (isVisible()) {

            super.drawBackground(canvas);
            super.drawTitle(canvas);

            // - - - - - - - - - - - - - - - - - - - - - - - - - -

            //subtitle                                          //
            subtitle_.draw(canvas);                             //
                                                                //         Subtitle
            // arrow buttons                                    //            C
            arrow_left_.draw(canvas);                           //     <   C     C   >
            arrow_right_.draw(canvas);                          //            C
                                                                //
            // tricks -> discardPiles;
            if (visible_round_id_ != GameController.NOT_SET && visible_round_id_ >= 0 &&
                    discard_piles_ != null && visible_round_id_ < discard_piles_.size()) {
                DiscardPile dp = discard_piles_.get(visible_round_id_);
                String winner_name = controller.getPlayerById(winners_.get(visible_round_id_)).getDisplayName();
                int max_width = (int) (controller.getLayout().getScreenWidth() * 0.6);
                while (winner_name_text_paint_.measureText(winner_name) > max_width) {
                    winner_name = winner_name.substring(1, winner_name.length() - 2);
                }
                drawDiscardPile(canvas, dp, winners_.get(visible_round_id_), winner_name);
            }

            info_.draw(canvas);                      // show a info if no cards were played
        }
    }


    //----------------------------------------------------------------------------------------------
    //  drawDiscardPile
    //                  -> not like the default discard pile draw!
    //                  -> just see the cards player 0 won the round!
    //
    private void drawDiscardPile(Canvas canvas, DiscardPile dp, int winner_id, String winner_name) {
        // draw discard pile
        for (int j = 0; j < dp.getPositions().size(); j++) {
            if (dp.getCard(j) == null || dp.getCard(j).getBitmap() == null) {
                canvas.drawBitmap(dp.getBitmap(),
                        dp.getPoint(j).x,
                        dp.getPoint(j).y, null);
            } else {

                if (winner_id != 0) {
                    canvas.drawBitmap(card_back_bitmap_,
                            dp.getPoint(j).x,
                            dp.getPoint(j).y, null);
                }
                else {
                    canvas.drawBitmap(dp.getCard(j).getBitmap(),
                            dp.getPoint(j).x,
                            dp.getPoint(j).y, null);
                }
            }
        }

        // draws the player name on top of the discard pile
        if (winner_id != 0) {
            Rect bounds = new Rect();
            winner_name_text_paint_.getTextBounds(winner_name, 0, winner_name.length(), bounds);
            Point dp_center = new Point(dp.getPoint(0).x + card_back_bitmap_.getWidth() / 2,
                    dp.getPoint(0).y + bounds.height() / 2 );
            canvas.drawText(winner_name, dp_center.x, dp_center.y, winner_name_text_paint_);
        }

    }


    //----------------------------------------------------------------------------------------------
    //  add Tricks and update
    //                          -> should get called after ever player played a card
    //                              and we know the round winner
    //
    public void addTricksAndUpdate(DiscardPile discardPile, int winner_id) {
        addDiscardPile(DiscardPile.copy(discardPile), winner_id);
        visible_round_id_ = discard_piles_.size() - 1; // last round index
        updateVisibleRound();
    }

    //----------------------------------------------------------------------------------------------
    //  showNextRound
    //                  -> called by click on the right arrow
    //
    private void showNextRound() {
        visible_round_id_++;
        if (visible_round_id_ >= discard_piles_.size()) {
            visible_round_id_ = 0;
        }
        updateVisibleRound();
    }


    //----------------------------------------------------------------------------------------------
    //  showPrevRound
    //                  -> called by click on the left arrow
    //
    private void showPrevRound() {
        visible_round_id_--;
        if (visible_round_id_ < 0) {
            visible_round_id_ = discard_piles_.size() - 1;
        }
        updateVisibleRound();
    }


    //----------------------------------------------------------------------------------------------
    //  updateVisibleRound
    //
    private void updateVisibleRound() {
        info_.setVisible(false);
        subtitle_.setText(view_.getResources().getString(R.string.button_bar_tricks_subtitle) + " "
                + (visible_round_id_ + 1));
        subtitle_.update();
        subtitle_.setVisible(true);
        arrow_left_.setVisible(true);
        arrow_right_.setVisible(true);
    }


    //----------------------------------------------------------------------------------------------
    //  clear
    //                  ->  resets the view
    //
    public void clear() {
        info_.setVisible(true);
        winners_.clear();
        discard_piles_.clear();
        arrow_left_.setVisible(false);
        arrow_right_.setVisible(false);
        subtitle_.setVisible(false);
        visible_round_id_ = GameController.NOT_SET;
    }


    //----------------------------------------------------------------------------------------------
    // addDiscardPile
    //                  ->  add a discard pile and sets Position correct
    //
    private void addDiscardPile(DiscardPile dp, int winner_id) {
        dp.setPositions(view_.getController().getLayout().getButtonBarWindowTricksDiscardPilePositions());
        winners_.add(winner_id);
        discard_piles_.add(dp);
    }

    //----------------------------------------------------------------------------------------------
    //  Touch Events
    //                 window close button
    //                  go left/right arrow button (next tricks)
    //
    public void touchEventDown(int X, int Y){
        if (!isVisible()) {
            return;
        }
        getCloseButton().touchEventDown(X, Y);

        arrow_left_.touchEventDown(X, Y);
        arrow_right_.touchEventDown(X, Y);
    }

    public void touchEventMove(int X, int Y) {
        if (!isVisible()) {
            return;
        }
        getCloseButton().touchEventMove(X, Y);

        arrow_left_.touchEventMove(X, Y);
        arrow_right_.touchEventMove(X, Y);
    }

    public void touchEventUp(int X, int Y) {
        if (!isVisible()) {
            return;
        }
        if (getCloseButton().touchEventUp(X, Y)) {
            this.setVisible(false);
        }
        else if (arrow_left_.touchEventUp(X, Y) ) {
            showPrevRound();
        }
        else if (arrow_right_.touchEventUp(X, Y) ) {
            showNextRound();
        }
    }
}
