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


//--------------------------------------------------------------------------------------------------
// Card Exchange Animation:
//
//    -> What does this animation?
//        Cards that should get exchanged move up, and spin in a 3D space around the y-axis.
//        spinning speed increases till it reaches a maximum, than the displayed image switches
//        to the new image and reduces the spinning speed. At the end the cards move back
//        to the player hand
//
//    -> How does this animation work?
//        It uses 2 containers; cards that get exchanged are stored in the first one,
//        while the new drawn cards are stored in container two.
//        This allows an easy switch from one card to another in the animation.
//        The Rotation animation is handled via a Camera and matrix setup,
//        which gets called recursive and based on a time interval
//
public class CardExchangeAnimation {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
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
    private final int MIN_SPIN_SPEED = 1;

    private Bitmap backside_bitmap_;    // backside of a card


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    CardExchangeAnimation() { }


    //----------------------------------------------------------------------------------------------
    //  Init
    //
    public void init(GameController controller) {
        camera_ = new Camera();
        animation_spinning_running_ = false;
        animation_end_running_ = false;
        exchanged_cards_ = new ArrayList<>();
        new_drawn_cards_ = new ArrayList<>();
        cards_changed_ = false;
        degree_ = 0;
        spin_speed_ = MIN_SPIN_SPEED;
        backside_bitmap_ = HelperFunctions.loadBitmap(controller.getView(), "card_back",
                controller.getLayout().getCardWidth(), controller.getLayout().getCardHeight());
    }


    //----------------------------------------------------------------------------------------------
    //  startSpinning
    //
    void startSpinning() {
        animation_spinning_running_ = true;
        recalculateSpinningParameters();
    }


    //----------------------------------------------------------------------------------------------
    //  drawRotation
    //
    void drawRotation(Canvas canvas) {

        Matrix matrix = new Matrix();
        camera_.save();

        degree_ += spin_speed_;
        // can't see anything
        if (degree_ % 90 == 0) {
            degree_++;
        }

        camera_.rotateX(0);
        camera_.rotateY(degree_);
        camera_.getMatrix(matrix)
        ;
        float x = (backside_bitmap_.getWidth() / 2);
        // float y = (bitmap.getHeight() / 2);
        matrix.preTranslate(-x, 0);
        matrix.postTranslate(x, 0);

        // decide which image has to be shown based on card and time and degree
        Bitmap bitmap;

        for (int i = 0; i < exchanged_cards_.size(); i++) {

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
    // continueExchangeCards
    //                  -> gets called from draw
    //                  recalculates new spinning speed and degree
    //
    void recalculateSpinningParameters() {

        // nothing to do if spinning is over
        if (!animation_spinning_running_) {
            return;
        }

        final int MAX_SPIN_SPEED = 35;

        // calculate time interval
        long timeNow = System.currentTimeMillis();
        long timeDelta = timeNow - timePrev;

        if (timeDelta > 300) {

            // increase speed in the first x rotations
            if (degree_ / 360.0 < 4.6) {
                spin_speed_ *= 2;
                if (spin_speed_ > MAX_SPIN_SPEED) {
                    spin_speed_ = MAX_SPIN_SPEED;
                }
            }
            // decrease speed in the last rotations
            else {
                spin_speed_ /= 2;
                if (spin_speed_ < MIN_SPIN_SPEED) {
                    spin_speed_ = MIN_SPIN_SPEED;
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


    //----------------------------------------------------------------------------------------------
    //  reset
    //
    void reset() {
        exchanged_cards_.clear();
        new_drawn_cards_.clear();
        animation_spinning_running_ = false;
        animation_end_running_ = false;
        spin_speed_ = MIN_SPIN_SPEED;
        degree_ = 0;
        cards_changed_ = false;
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    List<Card> getExchangedCards() {
        return exchanged_cards_;
    }

    List<Card> getNewDrawnCards() {
        return new_drawn_cards_;
    }

    boolean isSpinning() {
        return animation_spinning_running_;
    }

    boolean hasSpinningStopped() {
        return animation_end_running_;
    }
}