package heroiceraser.mulatschak.game.GamePlay.CardExchange;

import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.game.DrawableObjects.Card;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 23.12.2017.
 */

public class CardExchangeAnimation {

    private Camera camera_;     // needed for 3D rotation od bitmaps
    private long timePrev = 0;  // keeps track of the previous timestamp

    private boolean animation_spinning_running_;    // is the spinning animation running
    private boolean animation_end_running_; // is the animation ending running

    private List<Card> exchanged_cards_; // stores old cards
    private List<Card> new_drawn_cards_; // stores new cards

    private boolean cards_changed_; // triggered after 50% of the animation is done,
    //                                   handles which container gets displayed

    private int degree_;    // at which degree is the card in the moment of drawing
    private double spin_speed_; // how fast is the rotation spinning

    private Bitmap backside_bitmap_;


    public CardExchangeAnimation() {
    }

    public void init(GameController controller) {
        animation_spinning_running_ = false;
        animation_end_running_ = false;
        camera_ = new Camera();
        exchanged_cards_ = new ArrayList<>();
        new_drawn_cards_ = new ArrayList<>();
        cards_changed_ = false;
        degree_ = 0;
        spin_speed_ = 1;
        backside_bitmap_ = HelperFunctions.loadBitmap(controller.getView(), "card_back",
                controller.getLayout().getCardWidth(), controller.getLayout().getCardHeight());
    }


    public void drawRotation(Canvas canvas) {
        // rotation fun
        Matrix matrix = new Matrix();
        camera_.save();
        camera_.rotateX(0);
        degree_ += spin_speed_;

        // can't see anything
        if (degree_ % 90 == 0) {
            degree_++;
        }
        camera_.rotateY(degree_);
        camera_.getMatrix(matrix);
        float x = (backside_bitmap_.getWidth() / 2);
        // float y = (bitmap.getHeight() / 2);
        matrix.preTranslate(-x, 0);
        matrix.postTranslate(x, 0);

        for (int i = 0; i < exchanged_cards_.size(); i++) {

            // decide which image has to be shown
            Bitmap bitmap;

            // show frontside
            if (degree_ % 360 < 90 || degree_ % 360 > 270) {
                // show starting image
                bitmap = exchanged_cards_.get(i).getBitmap();
                //show animation ending image
                if (cards_changed_ && i < new_drawn_cards_.size()) {
                    bitmap = new_drawn_cards_.get(i).getBitmap();
                }
            }
            // show backside
            else {
                bitmap = backside_bitmap_;
            }

            Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            // drawing
            int w = backside_bitmap_.getWidth();
            int new_x = exchanged_cards_.get(i).getPosition().x + ((w - rotated.getWidth()) / 2);
            canvas.drawBitmap(rotated, new_x, exchanged_cards_.get(i).getPosition().y, null);
        }
        camera_.restore();

    }


    //----------------------------------------------------------------------------------------------
    //  reset
    //
    public void reset() {
        exchanged_cards_.clear();
        new_drawn_cards_.clear();
        animation_spinning_running_ = false;
        animation_end_running_ = false;
        spin_speed_ = 1;
        degree_ = 0;
        cards_changed_ = false;
    }


    public List<Card> getExchangedCards() {
        return exchanged_cards_;
    }

    public List<Card> getNewDrawnCards() {
        return new_drawn_cards_;
    }


    public void startSpinning(GameController controller) {
        animation_spinning_running_ = true;
        controller.getView().enableUpdateCanvasThread();
        recalculateSpinningParameters(controller);
    }

    public boolean isSpinning() {
        return animation_spinning_running_;
    }

    public boolean hasSpinningStopped() {
        return animation_end_running_;
    }


    //----------------------------------------------------------------------------------------------
// continueExchangeCards
//                  -> gets called from draw
//                  recalculates new spinning speed and degree
//
    public void recalculateSpinningParameters(GameController controller) {

        // nothing to do if spinning is over
        if (!animation_spinning_running_) {
            return;
        }

        // calculate time interval
        long timeNow = System.currentTimeMillis();
        long timeDelta = timeNow - timePrev;

        if (timeDelta > 300) {

            // increase speed in the first x rotations
            if (degree_ / 360 < 4.2) {
                spin_speed_ *= 2;
                if (spin_speed_ > 35) {
                    spin_speed_ = 35;
                }
            }
            // decrease speed in the last rotations
            else {
                spin_speed_ /= 2;
                if (spin_speed_ < 1) {
                    spin_speed_ = 1;
                }
            }

            timePrev = System.currentTimeMillis();
        }

        // after around 50% switch the cards
        if (!cards_changed_ && degree_ / 360 > 2) {
            cards_changed_ = true;
        }

        // animation is done after x rotations
        if (degree_ / 360 > 5) {
            spin_speed_ = 0;
            animation_spinning_running_ = false;
            animation_end_running_ = true;
        }
    }
}