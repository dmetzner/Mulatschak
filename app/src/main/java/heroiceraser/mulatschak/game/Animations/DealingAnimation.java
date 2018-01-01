package heroiceraser.mulatschak.game.Animations;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.Log;

import heroiceraser.mulatschak.game.DrawableObjects.CardStack;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 11.08.2017.
 */

public class DealingAnimation {

    private boolean animation_running_;
    private Bitmap bitmap_;

    private double ANIMATION_FACTOR;
    private double offset_x_;
    private double offset_y_;
    private double rotation_offset_;

    int cards_to_draw;

    private int hand_card_x, hand_card_y;
    private int endposition_x, endposition_y;
    private double rotation_start_;

    private GameView view_;

    public DealingAnimation(GameView view) {
        view_ = view;
        animation_running_ = false;
    }

    public void init(GameView view) {
        view_ = view;
        ANIMATION_FACTOR = (view.getController().getLayout().getScreenWidth() +
                view.getController().getLayout().getScreenHeight()) / 2.0 / 30.0;
    }

    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public boolean getAnimationRunning() { return animation_running_; }

    public void setAnimationRunning(boolean animation_running) {
        animation_running_ = animation_running;
    }

    public int getHandCardX() { return hand_card_x; }
    public int getHandCardY() { return hand_card_y; }

    private void calculateOffsets(int pos) {
        switch(pos) {
            case 0:
                offset_x_ = 1;
                offset_y_ = 2;
                rotation_offset_ = 0;
                break;
            case 1:
                offset_x_ = -2;
                offset_y_ = 1;
                rotation_offset_ = 90;
                break;
            case 2:
                offset_x_ = 1;
                offset_y_ = -2;
                rotation_offset_ = 0;
                break;
            case 3:
                offset_x_ = 2;
                offset_y_ = 1;
                rotation_offset_ = 90;
                break;
        }
        offset_x_ *= ANIMATION_FACTOR;
        offset_y_ *= ANIMATION_FACTOR;
        rotation_offset_ /= (ANIMATION_FACTOR / 12.5);
    }


    private void calculateEndPositions(int player_id, int pos) {
        int card_number = GameLogic.MAX_CARDS_PER_HAND - (cards_to_draw
                / view_.getController().getAmountOfPlayers());

        GameLayout layout = view_.getController().getLayout();
        switch (pos) {
            case 0:
                endposition_x = (int) ( layout.getHandBottom().x +
                        ( layout.getCardWidth() * card_number) / layout.getOverlapFactor(pos) );
                endposition_y =  layout.getHandBottom().y;
                break;
            case 1:
                endposition_x = layout.getHandLeft().x;
                endposition_y = (int) ( layout.getHandLeft().y -
                        (layout.getCardHeight() * card_number) / layout.getOverlapFactor(pos) );
                break;
            case 2:
                endposition_x = (int) ( layout.getHandTop().x +
                        (layout.getCardWidth() * card_number) / layout.getOverlapFactor(pos) );
                endposition_y =  layout.getHandTop().y;
                break;
            case 3:
                endposition_x = layout.getHandRight().x;
                endposition_y = (int) ( layout.getHandRight().y +
                        (layout.getCardHeight() * card_number) / layout.getOverlapFactor(pos) );
                break;
        }
    }

    public Bitmap getBitmap() {
        return bitmap_;
    }

    public void setAnimationToDeck() {
        hand_card_x = view_.getController().getDeck().getPosition().x;
        hand_card_y = view_.getController().getDeck().getPosition().y;
    }

    //----------------------------------------------------------------------------------------------
    public void start() {
        cards_to_draw =  GameLogic.MAX_CARDS_PER_HAND *
                view_.getController().getAmountOfPlayers();
        animation_running_ = true;
        resetDealingConfig();
        deal();
    }

    public void deal() {

        if (cards_to_draw <= 0) {
            animation_running_ = false;
            view_.getController().continueAfterDealingAnimation();
            return;
        }

        GameController controller = view_.getController();
        int player_id = 0;
        int modulo = cards_to_draw % controller.getAmountOfPlayers();
        switch (controller.getAmountOfPlayers()) {
            case 1:
                                        player_id = 0; controller.getPlayerById(0).setPosition(0);
                break;
            case 2:
                if      (modulo == 0) { player_id = 0; controller.getPlayerById(0).setPosition(0); }
                else if (modulo == 1) { player_id = 1; controller.getPlayerById(1).setPosition(2); }
                break;
            case 3:
                if      (modulo == 0) { player_id = 0; controller.getPlayerById(0).setPosition(0); }
                else if (modulo == 1) { player_id = 2; controller.getPlayerById(2).setPosition(2); }
                else if (modulo == 2) { player_id = 1; controller.getPlayerById(1).setPosition(1); }
                break;
            case 4:
                if      (modulo == 0) { player_id = 0; controller.getPlayerById(0).setPosition(0); }
                else if (modulo == 1) { player_id = 3; controller.getPlayerById(3).setPosition(3); }
                else if (modulo == 2) { player_id = 2; controller.getPlayerById(2).setPosition(2); }
                else if (modulo == 3) { player_id = 1; controller.getPlayerById(1).setPosition(1); }
                break;
        }
        animatePos(player_id);
    }

    public void animatePos(int player_id) {
        int position = view_.getController().getPlayerById(player_id).getPosition();
        calculateEndPositions(player_id, position);
        calculateOffsets(position);
        if (position % 2 == 0) {
            animateDealingVertical(player_id);
        }
        else {
            animateDealingHorizontal(player_id);
        }
    }

    public void animateDealingVertical(int player_id) {
        boolean animation_x_done = true;
        boolean animation_y_done = true;
        boolean bool1 = false;
        boolean bool2 = false;

        if (hand_card_x > endposition_x) {
            animation_x_done = false;
            offset_x_ *= -1;
            bool1 = true;
            bool2 = false;
            hand_card_x += offset_x_;
        }
        else if (hand_card_x < endposition_x) {
            animation_x_done = false;
            hand_card_x += offset_x_;
            bool1 = false;
            bool2 = true;
        }

        if (bool1 && hand_card_x <= endposition_x) {
            hand_card_x = endposition_x;
        }
        else if (bool2 && hand_card_x >= endposition_x) {
            hand_card_x = endposition_x;
        }

        if (offset_y_ > 0) {
            if (hand_card_y < endposition_y) {
                animation_y_done = false;
                hand_card_y += offset_y_;
            }
            if (hand_card_y >= endposition_y) {
                hand_card_y = endposition_y;
                animation_y_done = true;
            }
        }
        else {
            if (hand_card_y > endposition_y) {
                animation_y_done = false;
                hand_card_y += offset_y_;
            }
            if (hand_card_y <= endposition_y) {
                hand_card_y = endposition_y;
                animation_y_done = true;
            }
        }

        bitmap_ = view_.getController().getDeck().getBacksideBitmap();

        if (animation_x_done && animation_y_done) {
            goToNextAnimation(player_id);
        }
    }

    private int getPosition(int player_id) {
        CardStack cs = view_.getController().getPlayerById(player_id).getHand();
        for (int i = 0; i < cs.getCardStack().size(); i++) {
            if (cs.getCardAt(i).getPosition() == null) {
                return i;
            }
            if (cs.getCardAt(i).getPosition().
                    equals(view_.getController().getLayout().getDeckPosition())) {
                return i;
            }
        }
        return 0;
    }


    private void goToNextAnimation(int player_id) {//////////////////////////////////////////////////////////////TODO
        bitmap_ = view_.getController().getDeck().getBacksideBitmap();
        cards_to_draw--;
        Point card_position = new Point(hand_card_x, hand_card_y);
        int card_pos = getPosition(player_id);
        Log.d("------------> player:",+ player_id + " card_pos: " + card_pos + "");
        view_.getController().getPlayerById(player_id).getHand().getCardAt(card_pos)
                .setPosition(card_position);
        view_.getController().getPlayerById(player_id).getHand().getCardAt(card_pos)
                .setFixedPosition(new Point(card_position));
        resetDealingConfig();
    }

    public void resetDealingConfig() {
        hand_card_x = view_.getController().getDeck().getPosition().x;
        hand_card_y = view_.getController().getDeck().getPosition().y;
        rotation_start_ = 0;
    }

    public void animateDealingHorizontal(int player_id) {
        boolean animation_x_done = true;
        boolean animation_y_done = true;
        boolean goUp = false;
        boolean goDown = false;

        if (hand_card_y > endposition_y) {
            animation_y_done = false;
            offset_y_ *= -1;
            goUp = true;
            goDown = false;
            hand_card_y += offset_y_;
        }
        else if (hand_card_y < endposition_y) {
            animation_y_done = false;
            hand_card_y += offset_y_;
            goUp = false;
            goDown = true;
        }

        if (goUp && hand_card_y <= endposition_y) {
            hand_card_y = endposition_y;
        }
        else if (goDown && hand_card_y >= endposition_y) {
            hand_card_y = endposition_y;
        }

        if (offset_x_ > 0) {
            if (hand_card_x < endposition_x) {
                animation_x_done = false;
                hand_card_x += offset_x_;
            }
            if (hand_card_x >= endposition_x) {
                hand_card_x = endposition_x;
                animation_x_done = true;
            }
        }
        else {
            if (hand_card_x > endposition_x) {
                animation_x_done = false;
                hand_card_x += offset_x_;
            }
            if (hand_card_x <= endposition_x) {
                hand_card_x = endposition_x;
                animation_x_done = true;
            }
        }

        bitmap_ = rotateBitmap(view_.getController().getDeck().getBacksideBitmap(), 90);

        if (animation_x_done && animation_y_done) {
            goToNextAnimation(player_id);
        }
    }

    public Bitmap rotateBitmap(Bitmap bitmap, double degree) {

        if (degree > -0.001 && degree < 0.001 ) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate((int)rotation_start_);
        if (rotation_start_< 90) {
            rotation_start_ += rotation_offset_;
        }
        else {
            rotation_start_ = 90;
        }

        Bitmap backside = view_.getController().getDeck().getBacksideBitmap();
        Bitmap rotatedBitmap = Bitmap.createBitmap(backside , 0, 0,
                backside.getWidth(), backside.getHeight(), matrix, true);
        return rotatedBitmap;
    }
}

