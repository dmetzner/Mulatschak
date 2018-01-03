package heroiceraser.mulatschak.game.GamePlay.TrickBids;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.game.DrawableObjects.CardStack;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 03.01.2018.
 */

public class MakeBidsAnimation {

    public static int MAX_BID_ROWS = 3;
    public static int MAX_BID_COLS = 4;

    //
    public static final int MULATSCHAK = 5;
    public static final int MISS_A_TURN = -1;
    public static final int NO_CARD = 0;

    private List<MyButton> number_buttons_;
    private boolean animating_numbers_;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    MakeBidsAnimation() {
        animating_numbers_ = false;
        number_buttons_ = new ArrayList<>();
    }

    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();

        String image_name = "trick_bids_button_";
        int x = layout.getTrickBidsNumberButtonPosition().x;
        int y = layout.getTrickBidsNumberButtonPosition().y;
        int max_buttons_per_row = 4;
        int button_per_row = 0;
        for (int button_id = MISS_A_TURN; button_id <= MULATSCHAK; button_id++) {
            MyButton button = new MyButton();
            int width = view.getController().getLayout().getTrickBidsNumberButtonSize().x;
            int height = view.getController().getLayout().getTrickBidsNumberButtonSize().y;

            if(button_id == MISS_A_TURN) {
                width *= 4;
                button_per_row += 4;
                button.init(view, new Point(x, y), width, height, image_name + "x");
            }
            else if (button_id == NO_CARD || button_id ==  MULATSCHAK) {
                width *= 2;
                button_per_row += 2;
                button.init(view, new Point(x, y), width, height, image_name + button_id);
            }
            else {
                button_per_row++;
                button.init(view, new Point(x, y), width, height, image_name + button_id);
            }
            number_buttons_.add(button);

            if (button_per_row >= max_buttons_per_row) {
                x = layout.getTrickBidsNumberButtonPosition().x;
                int remove_button_drop_shadow = (int) (layout.getTrickBidsNumberButtonSize().y / 18.0);
                y += layout.getTrickBidsNumberButtonSize().y - remove_button_drop_shadow;
                button_per_row = 0;
            }
            else {
                x += width;
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas) {
        for (MyButton button : number_buttons_) {
            Bitmap bitmap = button.getBitmap();
            if (button.IsPressed()) {
                bitmap = button.getBitmapPressed();
            } else if (!button.IsEnabled()) {
                bitmap = button.getBitmapDisabled();
            }
            canvas.drawBitmap(bitmap, button.getPosition().x,
                    button.getPosition().y, null);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  setTricks
    //                  -> called by touch events, animating numbers
    //                  -> ends choose bids animation
    //                  -> sets Bids
    //
    public void setTricks(GameController controller, int button_id) {
        animating_numbers_ = false;
        button_id--;  // because of miss a turn button

        //---- don't play this round
        if (button_id == MISS_A_TURN) {
            controller.getPlayerById(0).setMissATurn(true);
            clearHand(controller);
            controller.getGamePlay().getTrickBids().setNewMaxTrumps(MISS_A_TURN, 0, controller);
            return;
        }

        //---- play this round
        if (button_id == MULATSCHAK) {
            // all players have to play this round
            for (int i = 0; i < controller.getAmountOfPlayers(); i++) {
                controller.getPlayerById(i).setMissATurn(false);
            }
        }
        controller.getPlayerById(0).setMissATurn(false);
        number_buttons_.get(0).setEnabled(true); // played this round -> can skip the next one
        controller.getGamePlay().getTrickBids().setNewMaxTrumps(button_id, 0, controller);
    }


    //----------------------------------------------------------------------------------------------
    //  clear Hand
    //                 -> moves all player 0 hand cards to the trash
    //                  ToDo: question? can i clear my hand if i don't want to play but a player after me says MULI?
    //
    private void clearHand(GameController controller) {
        CardStack hand = controller.getPlayerById(0).getHand();
        for (int i = 0; i < hand.getCardStack().size(); i++) {
            hand.getCardAt(i).setPosition(controller.getLayout().getDeckPosition());
            hand.getCardAt(i).setFixedPosition(controller.getLayout().getDeckPosition());
            controller.moveCardToTrash(hand.getCardAt(i));
            hand.getCardStack().remove(i);
            i--;
        }
    }


    //----------------------------------------------------------------------------------------------
    //  reEnableButtons
    //
    void reEnableButtons(int amount_of_player) {
        for (int i = 0; i < number_buttons_.size(); i++) {
            if (amount_of_player <= 2 && i == 0) {
                number_buttons_.get(i).setEnabled(false);
            }
            else {
                number_buttons_.get(i).setEnabled(true);
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public boolean getAnimationNumbers() {
        return animating_numbers_;
    }

    void turnOnAnimationNumbers() {
        animating_numbers_ = true;
    }

    public List<MyButton> getNumberButtons() {
        return number_buttons_;
    }
}
