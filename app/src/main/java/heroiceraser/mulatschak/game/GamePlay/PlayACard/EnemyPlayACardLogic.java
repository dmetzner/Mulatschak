package heroiceraser.mulatschak.game.GamePlay.PlayACard;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;

import java.util.Random;

import heroiceraser.mulatschak.game.DrawableObjects.Card;
import heroiceraser.mulatschak.game.DrawableObjects.CardStack;
import heroiceraser.mulatschak.game.DrawableObjects.DiscardPile;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.MyPlayer;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 20.12.2017.
 */

public class EnemyPlayACardLogic {

    private GameController controller_;
    private DiscardPile discard_pile_;
    private Bitmap backside_bitmap_;
    private Bitmap rotated_bitmap_;
    private int rotation_start_;
    private int rotation_offset_;
    private int rotation_end_;
    private int player_pos_;
    private boolean animation_running_;
    private Card move_card_;
    private Point offset_;
    private Point end_position_;
    private Point last_position_;

    public EnemyPlayACardLogic() {
        animation_running_ = false;
        move_card_ = null;
        offset_ = new Point();
    }

    public void init(GameController controller) {
        backside_bitmap_ = HelperFunctions.loadBitmap(controller.getView(), "card_back",
                controller.getLayout().getCardWidth(), controller.getLayout().getCardHeight());
        controller_ = controller;
    }

    // ToDo: do some magic here ;)

    public void playACard(GameLogic logic, MyPlayer myPlayer, DiscardPile discard_pile) {
        Random random_generator = new Random();
        boolean valid = false;
        int random_number = -1;
        while (!valid) {
            random_number = random_generator.nextInt(myPlayer.getAmountOfCardsInHand());
            Card card = myPlayer.getHand().getCardAt(random_number);
            CardStack hand = myPlayer.getHand();
            valid = logic.isAValidCardPlay(card, hand, discard_pile);
        }
        Card card = myPlayer.getHand().getCardAt(random_number);
        myPlayer.getHand().getCardStack().remove(random_number);

        move_card_ = card;
        animation_running_ = true;
        end_position_ = discard_pile.getPositions().get(myPlayer.getPosition());

        player_pos_ = myPlayer.getPosition();
        if (player_pos_ % 2 != 0) {
            rotation_start_ = 90;
        } else {
            rotation_start_ = 0;
        }
        rotation_end_ = 0;

        discard_pile_ = discard_pile;
        animateCardMovement();

    }


    private void animateCardMovement() {

        Point position = move_card_.getPosition();

        reCalculateOffset(position, 0.15);

        if (offset_.x == 0 && offset_.y == 0) {
            endAnimation();
            return ;
        }

        move_card_.setPosition(position.x + offset_.x,
                position.y + offset_.y);

        rotated_bitmap_ = rotateBitmap(backside_bitmap_);
    }

    private void endAnimation() {
        discard_pile_.setCard(player_pos_, move_card_);
        animation_running_ = false;
        controller_.continueAfterEnemeyPlayedACard();
    }


    private void reCalculateOffset(Point position, double alpha) {

        offset_.x = (int) (alpha * (end_position_.x - position.x));
        offset_.y = (int) (alpha * (end_position_.y - position.y));
        rotation_offset_ = (int) (alpha * (rotation_end_ - rotation_start_));

    }

    public Bitmap rotateBitmap(Bitmap bitmap) {

        if (rotation_start_ == rotation_end_) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        if (rotation_start_ <  rotation_end_) {
            rotation_start_ += rotation_offset_;
        }
        else if (rotation_start_ >  rotation_end_) {
            rotation_start_ += rotation_offset_;
        }

        matrix.postRotate(rotation_start_);

        Bitmap rotatedBitmap = Bitmap.createBitmap(backside_bitmap_ , 0, 0,
                backside_bitmap_.getWidth(), backside_bitmap_.getHeight(), matrix, true);
        return rotatedBitmap;
    }


    public void draw(Canvas canvas) {

        if (animation_running_) {
            canvas.drawBitmap(rotated_bitmap_,
                    move_card_.getPosition().x,
                    move_card_.getPosition().y,
                    null);
            animateCardMovement();
        }
    }

    public boolean isAnimationRunning() {
        return animation_running_;
    }

}
