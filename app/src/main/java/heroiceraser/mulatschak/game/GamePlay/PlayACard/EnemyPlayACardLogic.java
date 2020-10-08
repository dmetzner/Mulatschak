package heroiceraser.mulatschak.game.GamePlay.PlayACard;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import heroiceraser.mulatschak.game.BaseObjects.Card;
import heroiceraser.mulatschak.game.BaseObjects.CardStack;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.utils.BitmapMethodes;


//--------------------------------------------------------------------------------------------------
//  EnemyPlayACard Class
//
public class EnemyPlayACardLogic {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //
    private Bitmap backside_bitmap_;
    private Bitmap rotated_bitmap_;
    private int rotation_start_;
    private int rotation_offset_;
    private int rotation_end_;
    private int player_pos_;
    private boolean animation_running_;
    private Card move_card_;
    private Point offset_;
    private Point start_position_;
    private Point end_position_;
    private long time_start_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    EnemyPlayACardLogic() {
        animation_running_ = false;
        move_card_ = null;
        offset_ = new Point();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //                  -> loads backside bitmap
    //
    public void init(GameController controller) {
        backside_bitmap_ = BitmapMethodes.loadBitmap(controller.getView(), "card_back",
                controller.getLayout().getCardWidth(), controller.getLayout().getCardHeight());
    }


    //----------------------------------------------------------------------------------------------
    //  playACard
    //                  ToDo: do some magic here ;)
    //                  right now, just plays a random valid card
    //
    void playACard(GameController controller, MyPlayer myPlayer) {
        GameLogic logic = controller.getLogic();

        if (myPlayer.getMissATurn()) {
            controller.getGamePlay().getPlayACardRound().playACard(false, controller);
            return;
        }

        boolean valid = false;
        int random_number = -1;
        while (!valid) {
            random_number++;
            Card card = myPlayer.getHand().getCardAt(random_number);
            CardStack hand = myPlayer.getHand();
            valid = logic.isAValidCardPlay(card, hand, controller.getDiscardPile());
        }
        Card card = myPlayer.getHand().getCardAt(random_number);
        myPlayer.getHand().getCardStack().remove(random_number);

        move_card_ = card;
        end_position_ = controller.getDiscardPile().getPositions().get(myPlayer.getPosition());

        player_pos_ = myPlayer.getPosition();
        if (player_pos_ % 2 != 0) {
            rotation_start_ = 90;
        } else {
            rotation_start_ = 0;
        }
        rotation_end_ = 0;

        start_position_ = move_card_.getPosition();
        time_start_ = System.currentTimeMillis();
        animation_running_ = true;
        animateCardMovement(controller);
    }


    void playACardOnline(GameController controller, MyPlayer myPlayer, int cardId) {

        int posId = 0;

        for (int i = 0; i < myPlayer.getHand().getCardStack().size(); i++) {
            if (cardId == myPlayer.getHand().getCardAt(i).getId()) {
                posId = i;
            }
        }

        Card card = myPlayer.getHand().getCardAt(posId);

        // first card set starting card in logic!
        if (controller.getLogic().isDiscardPileEmpty(controller.getDiscardPile())) {
            int card_to_play_symbol = (card.getId() / 100) % 5;
            controller.getLogic().setStartingCard(card_to_play_symbol);
        }

        myPlayer.getHand().getCardStack().remove(posId);

        move_card_ = card;
        end_position_ = controller.getDiscardPile().getPositions().get(myPlayer.getPosition());

        player_pos_ = myPlayer.getPosition();
        if (player_pos_ % 2 != 0) {
            rotation_start_ = 90;
        } else {
            rotation_start_ = 0;
        }
        rotation_end_ = 0;

        start_position_ = move_card_.getPosition();
        time_start_ = System.currentTimeMillis();
        animation_running_ = true;
        animateCardMovement(controller);
    }

    //----------------------------------------------------------------------------------------------
    //  animate card movement
    //
    private void animateCardMovement(GameController controller) {

        if (!animation_running_) {
            return;
        }

        double animation_factor = controller.getSettings().getAnimationSpeed().getSpeedFactor();
        double max_time = 750 * animation_factor;
        long time = System.currentTimeMillis();
        long time_since_start = time - time_start_;

        double percentage = time_since_start / max_time;
        if (percentage >= 1) {
            percentage = 1;
        }

        // set card to correct position and rotation for this moment
        reCalculateOffset(percentage);
        move_card_.setPosition(start_position_.x + offset_.x,
                start_position_.y + offset_.y);
        rotated_bitmap_ = BitmapMethodes.rotateBitmap(backside_bitmap_,
                rotation_start_ + rotation_offset_);

        // end animation
        if (percentage == 1) {
            endAnimation(controller);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  endAnimation
    //                  -> calls playACard
    //
    private void endAnimation(GameController controller) {
        controller.getDiscardPile().setCard(player_pos_, move_card_);
        animation_running_ = false;
        controller.getGamePlay().getPlayACardRound().playACard(false, controller);
    }


    //----------------------------------------------------------------------------------------------
    //  recalculate offset
    //                              -> position and rotation
    //
    private void reCalculateOffset(double percentage) {
        offset_.x = (int) (percentage * (end_position_.x - start_position_.x));
        offset_.y = (int) (percentage * (end_position_.y - start_position_.y));
        rotation_offset_ = (int) (percentage * (rotation_end_ - rotation_start_));
    }

    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public synchronized void draw(Canvas canvas, GameController controller) {

        if (!animation_running_) {
            return;
        }

        // draw the moving card
        canvas.drawBitmap(rotated_bitmap_,
                move_card_.getPosition().x,
                move_card_.getPosition().y,
                null);

        // continue animation
        animateCardMovement(controller);
    }
}
