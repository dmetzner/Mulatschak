package heroiceraser.mulatschak.game.GamePlay.DealCards;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

import heroiceraser.mulatschak.game.BaseObjects.Card;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;
import heroiceraser.mulatschak.helpers.BitmapMethodes;

//----------------------------------------------------------------------------------------------
//  Dealing Animation Class
//                              ToDo Explain me ;)
//
public class DealingAnimation {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private boolean animation_running_;
    private Point start_pos_;
    private Point end_pos_;
    private Point position_offset_;
    private double max_rotation_;
    private double rotation_;
    private Card card_;
    private int drawn_cards_;
    private int cards_to_draw_;
    private int player_id_;
    private long start_time_;
    private boolean new_card_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    DealingAnimation() {
        start_pos_ = new Point();
        end_pos_ = new Point();
        position_offset_ = new Point();
    }


    //----------------------------------------------------------------------------------------------
    //  start
    //              -> starts the Dealing Animation
    //              ... sets all variables to their starting value
    //               - first card goes to the player after the dealer
    //
    public void start(GameController controller) {
        start_pos_ = controller.getDeck().getPosition();
        drawn_cards_ = 0;
        cards_to_draw_ =  GameLogic.MAX_CARDS_PER_HAND * controller.getAmountOfPlayers();
        player_id_ = controller.getLogic().getDealer();
        nextPlayer(controller.getAmountOfPlayers());
        MyPlayer player = controller.getPlayerById(player_id_);
        card_ = player.getHand().getCardAt(drawn_cards_);
        calculateEndPositions(controller.getPlayerById(player_id_), controller);
        calculatePositionOffsets();
        calculateRotation(controller.getPlayerById(player_id_).getPosition());
        rotation_ = max_rotation_;
        start_time_ = System.currentTimeMillis();
        animation_running_ = true;
    }


    //----------------------------------------------------------------------------------------------
    //  continue Animation
    //              ->continues the Dealing Animation
    //              ... sets all variables to their actual value based on a time percentage
    //               - animation time gets reset after ever card
    //
    private void continueAnimation(GameController controller) {
        double animation_factor = controller.getSettings().getAnimationSpeed().getSpeedFactor();
        double max_time = 150 * animation_factor;
        long time = System.currentTimeMillis();
        long time_since_start = time - start_time_;

        double percentage = time_since_start / max_time;
        if (percentage > 1) {
            percentage = 1;
            new_card_ = true;
        }

        card_.setPosition(new Point(start_pos_.x + (int) (position_offset_.x * percentage),
                start_pos_.y + (int) (position_offset_.y * percentage)));

        rotation_ = max_rotation_ * percentage;

        if (new_card_) {
            new_card_ = false;
            card_.setVisible(true);
            card_.setFixedPosition(new Point(card_.getPosition()));
            drawn_cards_++;
            nextPlayer(controller.getAmountOfPlayers());
            if (drawn_cards_ == cards_to_draw_) {
                animation_running_ = false;
                controller.continueAfterDealingAnimation();
            }
            else {
                MyPlayer player = controller.getPlayerById(player_id_);
                card_ = player.getHand().getCardAt(drawn_cards_ / controller.getAmountOfPlayers());
                calculateEndPositions(controller.getPlayerById(player_id_), controller);
                calculatePositionOffsets();
                calculateRotation(controller.getPlayerById(player_id_).getPosition());
                start_time_ = System.currentTimeMillis();
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    //  draw
    //              -> continues Animation
    //              -> draws the deck and the actual card that moves to a player
    //
    public void draw(Canvas canvas, GameController controller) {

        if (!animation_running_) {
            return;
        }

        // draw deck
        canvas.drawBitmap(controller.getDeck().getBacksideBitmap(),
                controller.getDeck().getPosition().x,
                controller.getDeck().getPosition().y, null);

        // draw the card that gets moved to a players hand
        Bitmap bitmap = controller.getDeck().getBacksideBitmap();

        if (rotation_ != 0) {
            // player left and right need a rotation
            bitmap = BitmapMethodes.rotateBitmap(bitmap, (int) rotation_);
        }

        try {
            canvas.drawBitmap(bitmap, card_.getPosition().x, card_.getPosition().y, null);
        }
        catch (Exception e) {
            if (controller.DEBUG) { Log.e("---", "draw dealing cards exception" + e); }
        }

        continueAnimation(controller);
    }


    //----------------------------------------------------------------------------------------------
    //  nextPlayer
    //                  -> sets player_id_ to the next player
    //
    private void nextPlayer(int players) {
        player_id_++;
        if (player_id_ >= players) {
            player_id_ = 0;
        }
    }


    //----------------------------------------------------------------------------------------------
    //  calculatePositionOffsets
    //                              offset = end pos - start pos
    //
    private void calculatePositionOffsets() {
        position_offset_ = new Point(end_pos_.x - start_pos_.x, end_pos_.y - start_pos_.y);
    }


    //----------------------------------------------------------------------------------------------
    //  calculateRotation
    //                      switch case sets max rotation value based on position
    //
    private void calculateRotation(int player_pos) {
        switch (player_pos) {
            case 0:
                max_rotation_ = 0;
                break;
            case 1:
                max_rotation_ = 90;
                break;
            case 2:
                max_rotation_ = 0;
                break;
            case 3:
                max_rotation_ = -90;
                break;
        }
    }


    //----------------------------------------------------------------------------------------------
    //  calculateEndPositions
    //                          -> sets the end position like defined in the layout (HandCards)
    //                             and adds an overlapping offset for each card
    //
    private void calculateEndPositions(MyPlayer player, GameController controller) {
        GameLayout layout = controller.getLayout();

        int card_number = drawn_cards_ / controller.getAmountOfPlayers();

        switch (player.getPosition()) {
            case 0:
                end_pos_.x = (int) ( layout.getHandBottom().x +
                        ( layout.getCardWidth() * card_number) / layout.getOverlapFactor(player.getPosition()) );
                end_pos_.y =  layout.getHandBottom().y;
                break;
            case 1:
                end_pos_.x = layout.getHandLeft().x;
                end_pos_.y = (int) ( layout.getHandLeft().y -
                        (layout.getCardHeight() * card_number) / layout.getOverlapFactor(player.getPosition()) );
                break;
            case 2:
                end_pos_.x = (int) ( layout.getHandTop().x +
                        (layout.getCardWidth() * card_number) / layout.getOverlapFactor(player.getPosition()) );
                end_pos_.y =  layout.getHandTop().y;
                break;
            case 3:
                end_pos_.x = layout.getHandRight().x;
                end_pos_.y = (int) ( layout.getHandRight().y +
                        (layout.getCardHeight() * card_number) / layout.getOverlapFactor(player.getPosition()) );
                break;
        }
    }
}
