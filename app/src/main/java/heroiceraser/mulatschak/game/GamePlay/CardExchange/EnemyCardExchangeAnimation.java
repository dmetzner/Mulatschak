package heroiceraser.mulatschak.game.GamePlay.CardExchange;

import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.game.DrawableObjects.Card;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.Player;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 23.12.2017.
 */

public class EnemyCardExchangeAnimation {

    Player player_;
    private Camera camera_;     // needed for 3D rotation od bitmaps

    boolean clean_hand = true;
    boolean part1;
    boolean part1_5;
    boolean part2;

    private long time_prev_;  // keeps track of the previous timestamp
    private int degree_;    // at which degree is the card in the moment of drawing
    private double spin_speed_; // how fast is the rotation spinning


    private boolean animation_running_;    // is the Animation animation running
    private boolean animation_end_running_; // is the animation ending running

    private List<Card> exchanged_cards_; // stores old cards
    private List<Card> new_drawn_cards_; // stores new cards

    private long start_time_;
    private int alpha_;
    private final int MAX_ALPHA = 255;
    private Point offset_;
    private int index_;

    private Bitmap backside_bitmap_;


    public EnemyCardExchangeAnimation() {
        camera_ = new Camera();
    }

    public void init(GameController controller, Player player) {
        animation_running_ = false;
        animation_end_running_ = false;
        time_prev_ = 0;
        degree_ = 0;
        spin_speed_ = 1;

        player_ = player;
        clean_hand = false;
        
        exchanged_cards_ = new ArrayList<>();
        new_drawn_cards_ = new ArrayList<>();
        
        backside_bitmap_ = HelperFunctions.loadBitmap(controller.getView(), "card_back",
                controller.getLayout().getCardWidth(), controller.getLayout().getCardHeight());
    }


    public void draw(Canvas canvas, GameController controller, Player player) {

        if (part1 || part2) {
            Paint alphaPaint = new Paint();
            alphaPaint.setAlpha(alpha_);
            Bitmap bitmap = backside_bitmap_;
            if (player.getPosition() % 2 != 0) {
                bitmap = rotateBitmap(bitmap, 90);
            }
            for (int i = 0; i < exchanged_cards_.size(); i++) {
                canvas.drawBitmap(bitmap, exchanged_cards_.get(i).getPosition().x,
                        exchanged_cards_.get(i).getPosition().y, alphaPaint);
            }
            if (exchanged_cards_.size() <= 0) {
                for (int i = 0; i < new_drawn_cards_.size(); i++) {
                    canvas.drawBitmap(bitmap, new_drawn_cards_.get(i).getPosition().x,
                            new_drawn_cards_.get(i).getPosition().y, alphaPaint);
                }
            }
        }

        if (part1_5) {
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

            if (player.getPosition() % 2 != 0) {

                bitmap = rotateBitmap(bitmap, 90);
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

        if (clean_hand) {
            Bitmap bitmap = backside_bitmap_;
            if (player.getPosition() % 2 != 0) {
                bitmap = rotateBitmap(bitmap, 90);
            }
            for (Card card : player.getHand().getCardStack()) {
                canvas.drawBitmap(bitmap, card.getPosition().x, card.getPosition().y, null);
            }
            clean_hand = false;
        }
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        return Bitmap.createBitmap(bitmap , 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
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


    public void recalculateParameters(GameController controller) {

        int max_time_section_1 = 500;
        alpha_ = 255;

        Log.d("------------------rec", "");

        long time = System.currentTimeMillis();
        long time_since_start = time - start_time_;

        if (part1) {

            if (index_ >= exchanged_cards_.size()) {
                part1 = false;
                part1_5 = true;
                part2 = false;
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

            //alpha_ = MAX_ALPHA - (int) (MAX_ALPHA * percentage);
            int offset_x = (int) (offset_.x * percentage);
            int offset_y = (int) (offset_.y * percentage);

            card.setPosition(new Point( card.getFixedPosition().x + offset_x,
                    card.getFixedPosition().y + offset_y));
        }


        if (part1_5) {
            // calculate time interval
            long timeNow = System.currentTimeMillis();
            long timeDelta = timeNow - time_prev_;

            if (timeDelta > 300) {

                // increase speed in the first x rotations
                if (degree_ / 360.0 < 2.5) {
                    spin_speed_ *= 2;
                    if (spin_speed_ > 28) {
                        spin_speed_ = 28;
                    }
                }
                // decrease speed in the last rotations
                else {
                    spin_speed_ /= 2;
                    if (spin_speed_ < 1) {
                        spin_speed_ = 1;
                    }
                }

                time_prev_ = System.currentTimeMillis();

            }

            // animation is done after x rotations
            if (degree_ / 360.0 > 3) {
                spin_speed_ = 0;
                part1 = false;
                part1_5 = false;
                part2 = true;
                index_ = -1;
                start_time_ = System.currentTimeMillis();
            }
        }


        if (part2) {

            if (index_ == -1) {
                start_time_ = System.currentTimeMillis();
                index_ = 0;
                return;
            }

            if (index_ >= exchanged_cards_.size()) {
                part1 = false;
                part1_5 = false;
                part2 = false;
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

    private void drawNewCard(GameController controller) {
        controller.takeCardFromDeck(player_.getId(), controller.getDeck());
        Card card = player_.getHand().getCardAt(player_.getHand().getCardStack().size() - 1);
        card.setPosition(exchanged_cards_.get(index_).getFixedPosition());
        card.setFixedPosition(exchanged_cards_.get(index_).getFixedPosition());

        player_.getHand().getCardStack().remove(card);

        boolean inserted = false;

        if (player_.getPosition() % 2 == 0) {
            // x
            for (int i = 0; i < player_.getHand().getCardStack().size(); i++) {
                if (player_.getHand().getCardAt(i).getPosition().x > card.getPosition().x) {
                    player_.getHand().getCardStack().add(i, card);
                    inserted = true;
                    break;
                }
            }
        }
        else { // y
            for (int i = 0; i < player_.getHand().getCardStack().size(); i++) {
                if (player_.getHand().getCardAt(i).getPosition().y > card.getPosition().y) {
                    player_.getHand().getCardStack().add(i, card);
                    inserted = true;
                    break;
                }
            }
        }

        if (!inserted) {
            player_.getHand().addCard(card);
        }

        Log.d("------------>", player_.getHand().getCardStack().size() +"");
    }

    //----------------------------------------------------------------------------------------------
    //  reset
    //
    public void reset() {
        exchanged_cards_.clear();
        new_drawn_cards_.clear();
        animation_running_ = false;
        animation_end_running_ = false;
    }


    public List<Card> getExchangedCards() {
        return exchanged_cards_;
    }


    public void startAnimation(GameController controller, Point offset) {
        animation_running_ = true;
        animation_end_running_ = false;
        index_ = 0;
        degree_ = 0;
        spin_speed_ = 1;
        part1 = true;
        part2 = false;
        offset_ = offset;
        start_time_ = System.currentTimeMillis();
    }

    public boolean isAnimationRunning() {
        return animation_running_;
    }

    public boolean hasAnimationStopped() {
        return animation_end_running_;
    }


}