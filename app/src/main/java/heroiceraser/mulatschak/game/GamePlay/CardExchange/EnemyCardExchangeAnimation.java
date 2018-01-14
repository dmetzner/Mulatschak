package heroiceraser.mulatschak.game.GamePlay.CardExchange;

import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import java.util.ArrayList;
import java.util.List;
import heroiceraser.mulatschak.game.DrawableObjects.Card;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.DrawableObjects.MyPlayer;
import heroiceraser.mulatschak.helpers.HelperFunctions;

//--------------------------------------------------------------------------------------------------
//What does this animation?
//        Cards that should get exchanged move up, and spin in a 3D space.
//        spinning speed increases till it reaches a maximum, than the displayed image switches
//        to the new image and reduces the spinning speed. At the end the cards move back
//        to the player hand

//    -> How does this animation work?
//        It uses 2 containers; cards that get exchanged are stored in the first one,
//        while the new drawn cards are stored in container two.
//        This allows an easy switch from one card to another in the animation.
//        The Rotation animation is handled via a Camera and matrix setup,
//        which gets called recursive and based on a time interval
//
public class EnemyCardExchangeAnimation {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private MyPlayer myPlayer_;

    private boolean clean_hand;      // reorder hand cards

    private Camera camera_;     // needed for 3D rotation od bitmaps
    private long time_prev_;  // keeps track of the previous timestamp
    private int degree_;    // at which degree is the card in the moment of drawing
    private double spin_speed_; // how fast is the rotation spinning
    private final int MIN_SPIN_SPEED = 1;

    private boolean animation_running_;    // is the Animation animation running
    private boolean moving_up_;
    private boolean spinning_;
    private boolean moving_down_;
    private boolean animation_end_running_; // is the animation ending running

    private List<Card> exchanged_cards_; // stores old cards


    private long start_time_;
    private Point offset_;
    private int index_;

    private Bitmap backside_bitmap_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    EnemyCardExchangeAnimation() {
        camera_ = new Camera();
    }


    //----------------------------------------------------------------------------------------------
    //  Init
    //
    public void init(GameController controller, MyPlayer myPlayer) {
        animation_running_ = false;
        animation_end_running_ = false;
        time_prev_ = 0;
        degree_ = 0;
        spin_speed_ = MIN_SPIN_SPEED;
        myPlayer_ = myPlayer;
        clean_hand = false;
        exchanged_cards_ = new ArrayList<>();
        backside_bitmap_ = HelperFunctions.loadBitmap(controller.getView(), "card_back",
                controller.getLayout().getCardWidth(), controller.getLayout().getCardHeight());
    }


    //----------------------------------------------------------------------------------------------
    //  StartAnimation
    //
    void startAnimation() {
        animation_running_ = true;
        moving_up_ = true;
        spinning_ = false;
        moving_down_ = false;
        animation_end_running_ = false;
        index_ = 0;
        degree_ = 0;
        spin_speed_ = MIN_SPIN_SPEED;
        offset_ = calculateOffsets((int) (backside_bitmap_.getHeight() * 1.2));
        start_time_ = System.currentTimeMillis();
    }


    //----------------------------------------------------------------------------------------------
    //  calculateOffsets
    //
    private Point calculateOffsets(int shift) {

        Point offset = new Point(0, 0);
        switch (myPlayer_.getPosition()) {
            case 1:
                offset.x += shift;
                offset.y += 0;
                break;
            case 2:
                offset.x += 0;
                offset.y += shift;
                break;
            case 3:
                offset.x += (-1) * shift;
                offset.y += 0;
                break;
        }
        return offset;
    }


    //----------------------------------------------------------------------------------------------
    // draw
    //
    public void draw(Canvas canvas, MyPlayer myPlayer) {

        //  draw bitmap without 3d rotation
        if (moving_up_ || moving_down_) {

            Bitmap bitmap = backside_bitmap_;

            // myPlayer 1 & 3 show a rotated bitmap
            if (myPlayer.getPosition() % 2 != 0) {
                bitmap = HelperFunctions.rotateBitmap(bitmap, 90);
            }

            for (int i = 0; i < exchanged_cards_.size(); i++) {
                canvas.drawBitmap(bitmap, exchanged_cards_.get(i).getPosition().x,
                        exchanged_cards_.get(i).getPosition().y, null);
            }
        }

        // draw bitmap with 3d rotation
        if (spinning_) {
            // rotation fun
            Matrix matrix = new Matrix();
            camera_.save();
            degree_ += spin_speed_;

            // can't see anything
            if (degree_ % 90 == 0) {
                degree_++;
            }


            // decide which image has to be shown
            Bitmap bitmap = backside_bitmap_;

            if (myPlayer.getPosition() % 2 != 0) {

                bitmap = HelperFunctions.rotateBitmap(bitmap, 90);
                camera_.rotateX(degree_);
                camera_.rotateY(0);
                camera_.getMatrix(matrix);
                float x = (backside_bitmap_.getWidth() / 2);
                // float y = (bitmap.getHeight() / 2);
                matrix.preTranslate(-x, 0);
                matrix.postTranslate(x, 0);
            }
            else {
                camera_.rotateX(0);
                camera_.rotateY(degree_);
                camera_.getMatrix(matrix);
                float x = (backside_bitmap_.getWidth() / 2);
                float y = (bitmap.getHeight() / 2);
                matrix.preTranslate( -x, -y);
                matrix.postTranslate(x, y);
            }

            Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            for (int i = 0; i < exchanged_cards_.size(); i++) {
                // drawing
                int w = backside_bitmap_.getWidth();
                int h = backside_bitmap_.getHeight();
                int new_x = exchanged_cards_.get(i).getPosition().x + ((w - rotated.getWidth()) / 2);
                int new_y = exchanged_cards_.get(i).getPosition().y + ((h - rotated.getHeight()) / 2);
                canvas.drawBitmap(rotated, new_x, new_y, null);
            }
            camera_.restore();
        }

        // if hands got reordered -> redraw
        if (clean_hand) {
            Bitmap bitmap = backside_bitmap_;
            if (myPlayer.getPosition() % 2 != 0) {
                bitmap = HelperFunctions.rotateBitmap(bitmap, 90);
            }
            for (Card card : myPlayer.getHand().getCardStack()) {
                canvas.drawBitmap(bitmap, card.getPosition().x, card.getPosition().y, null);
            }
            clean_hand = false;
        }
    }


    //----------------------------------------------------------------------------------------------
    // oldCardsToTrash
    //
    private void oldCardsToTrash(GameController controller) {
        // add old cards to trash
        for (int i = 0; i < exchanged_cards_.size(); i++) {
            controller.getTrash().addCard(exchanged_cards_.get(i));
        }
    }


    //----------------------------------------------------------------------------------------------
    // recalculateParameter
    //
    void recalculateParameters(GameController controller) {
        double animation_factor = controller.getSettings().getAnimationSpeed().getSpeedFactor();
        double max_time_section_1 = 250 * animation_factor;
        double max_time_section_2 = 1750 * animation_factor;
        long time = System.currentTimeMillis();
        long time_since_start = time - start_time_;

        if (moving_up_) {

            if (index_ >= exchanged_cards_.size()) {
                moving_up_ = false;
                spinning_ = true;
                moving_down_ = false;
                index_ = 0;
                start_time_ = System.currentTimeMillis();
                time_prev_ = start_time_;
                return;
            }
            Card card = exchanged_cards_.get(index_);
            double percentage  = ((double)time_since_start / max_time_section_1);
            if (percentage >= 1) {
                percentage = 1;
                start_time_ = System.currentTimeMillis();
                index_++;
            }

            int offset_x = (int) (offset_.x * percentage);
            int offset_y = (int) (offset_.y * percentage);

            card.setPosition(new Point( card.getFixedPosition().x + offset_x,
                    card.getFixedPosition().y + offset_y));
        }


        if (spinning_) {
            // calculate time interval

            double percentage = time_since_start / max_time_section_2;
            if (percentage > 1) {
                percentage = 1;
            }

            double t1 = 0.21;
            double t2 = 0.38;
            double t3 = 0.62;
            double t4 = 0.79;
            double t5 = 0.93;

            // first spin
            if (percentage <= t1) {
                degree_ = (int) (180 * ((percentage ) / (t1)));
            }
            else if (percentage <= t2) {
                degree_ = 180 + (int) (180 * ((percentage - t1 ) / (t2 - t1)));
            }

            // second spin
            else if (percentage <= t3) {
                degree_ = (int) (360 * ((percentage - t2 ) / (t3 - t2)));
            }

            // third spin
            else if (percentage <= t4) {
                degree_ = (int) (180 * ((percentage - t3 ) / (t4 - t3)));
            }
            else if (percentage <= t5) {
                degree_ =  180 + (int) (180 * ((percentage - t4 ) / (t5 - t4)));
            }
            else {
                degree_ = 0;
            }

            // animation is done after x rotations
            if (percentage >= 1) {
                spin_speed_ = 0;
                moving_up_ = false;
                spinning_ = false;
                moving_down_ = true;
                index_ = -1;
                start_time_ = System.currentTimeMillis();
            }
        }

        if (moving_down_) {

            if (index_ == -1) {
                start_time_ = System.currentTimeMillis();
                index_ = 0;
                return;
            }

            if (index_ >= exchanged_cards_.size()) {
                moving_up_ = false;
                spinning_ = false;
                moving_down_ = false;
                animation_end_running_ = true;
                index_ = 0;
                oldCardsToTrash(controller);
                return;
            }
            Card card =  exchanged_cards_.get(index_);
            double percentage = ((double) time_since_start / max_time_section_1);
            if (percentage >= 1) {
                percentage = 1;
            }

            //alpha_ = (int) (MAX_ALPHA * percentage);
            int offset_x = (int) (offset_.x * percentage);
            int offset_y = (int) (offset_.y * percentage);

            card.setPosition(new Point(card.getFixedPosition().x + offset_.x - offset_x,
                    card.getFixedPosition().y + offset_.y - offset_y));

            if (percentage >= 1) {
                drawNewCard(controller);
                clean_hand = true;
                index_++;
                start_time_ = System.currentTimeMillis();
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    //  drawNewCards
    //
    private void drawNewCard(GameController controller) {
        controller.getGamePlay().getDealCards().takeCardFromDeck(myPlayer_, controller.getDeck());
        Card card = myPlayer_.getHand().getCardAt(myPlayer_.getHand().getCardStack().size() - 1);
        card.setPosition(exchanged_cards_.get(index_).getFixedPosition());
        card.setFixedPosition(exchanged_cards_.get(index_).getFixedPosition());
        card.setVisible(true);

        myPlayer_.getHand().getCardStack().remove(card);

        boolean inserted = false;

        if (myPlayer_.getPosition() == 0) {
            // < x
            for (int i = 0; i < myPlayer_.getHand().getCardStack().size(); i++) {
                if (myPlayer_.getHand().getCardAt(i).getPosition().x < card.getPosition().x) {
                    myPlayer_.getHand().getCardStack().add(i, card);
                    inserted = true;
                    break;
                }
            }
        }
        else if (myPlayer_.getPosition() == 1) {
            // < y
            for (int i = 0; i < myPlayer_.getHand().getCardStack().size(); i++) {
                if (myPlayer_.getHand().getCardAt(i).getPosition().y < card.getPosition().y) {
                    myPlayer_.getHand().getCardStack().add(i, card);
                    inserted = true;
                    break;
                }
            }
        }
        else if (myPlayer_.getPosition() == 2) {
            // > x
            for (int i = 0; i < myPlayer_.getHand().getCardStack().size(); i++) {
                if (myPlayer_.getHand().getCardAt(i).getPosition().x > card.getPosition().x) {
                    myPlayer_.getHand().getCardStack().add(i, card);
                    inserted = true;
                    break;
                }
            }
        }
        else if (myPlayer_.getPosition() == 3) {
            // > y
            for (int i = 0; i < myPlayer_.getHand().getCardStack().size(); i++) {
                if (myPlayer_.getHand().getCardAt(i).getPosition().y > card.getPosition().y) {
                    myPlayer_.getHand().getCardStack().add(i, card);
                    inserted = true;
                    break;
                }
            }
        }
        if (!inserted) {
            myPlayer_.getHand().addCard(card);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  reset
    //
    void reset() {
        exchanged_cards_.clear();
        animation_running_ = false;
        animation_end_running_ = false;
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    List<Card> getExchangedCards() {
        return exchanged_cards_;
    }

    boolean isAnimationRunning() {
        return animation_running_;
    }

    boolean hasAnimationStopped() {
        return animation_end_running_;
    }


}