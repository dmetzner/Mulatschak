package heroiceraser.mulatschak.game.Animations;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import heroiceraser.mulatschak.game.CardStack;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.Coordinate;

/**
 * Created by Daniel Metzner on 11.08.2017.
 */

public class DealingAnimation {

    private boolean animation_running_;
    private Bitmap bitmap_;

    private final double ANIMATION_FACTOR = 45;
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

    private double getOverlapFactor(int pos) {
        switch (pos) {
            case 0: return 1;
            case 1: return 9.5;
            case 2: return 7.0;
            case 3: return 9.5;
            default: return 1;
        }
    }

    private void calculateEndPositions(int player_id, int pos) {
        int card_number = view_.getController().getLogic().getMaxCardsPerHand() - (cards_to_draw
                / view_.getController().getAmountOfPlayers());

        GameLayout layout = view_.getController().getLayout();
        switch (pos) {
            case 0:
                endposition_x = (int) ( layout.getHandBottom().getX() +
                        ( layout.getCardWidth() * card_number) / getOverlapFactor(pos) );
                endposition_y =  layout.getHandBottom().getY();
                break;
            case 1:
                endposition_x = layout.getHandLeft().getX();
                endposition_y = (int) ( layout.getHandLeft().getY() -
                        (layout.getCardHeight() * card_number) / getOverlapFactor(pos) );
                break;
            case 2:
                endposition_x = (int) ( layout.getHandTop().getX() +
                        (layout.getCardWidth() * card_number) / getOverlapFactor(pos) );
                endposition_y =  layout.getHandTop().getY();
                break;
            case 3:
                endposition_x = layout.getHandRight().getX();
                endposition_y = (int) ( layout.getHandRight().getY() +
                        (layout.getCardHeight() * card_number) / getOverlapFactor(pos) );
                break;
        }
    }

    public Bitmap getBitmap() {
        return bitmap_;
    }

    public void setAnimationToDeck() {
        hand_card_x = view_.getController().getDeck().getCoordinate().getX();
        hand_card_y = view_.getController().getDeck().getCoordinate().getY();
    }

    //----------------------------------------------------------------------------------------------
    public void start() {
        cards_to_draw =  view_.getController().getLogic().getMaxCardsPerHand() *
                view_.getController().getAmountOfPlayers();
        animation_running_ = true;
        hand_card_x = view_.getController().getDeck().getCoordinate().getX();
        hand_card_y = view_.getController().getDeck().getCoordinate().getY();
        rotation_start_ = 0;
        deal();
    }

    public void deal() {

        if (cards_to_draw <= 0) {
            animation_running_ = false;
            return;
        }
        int player_id = 0;
        int position_id = 0;
        int modulo = cards_to_draw % view_.getController().getAmountOfPlayers();
        switch (view_.getController().getAmountOfPlayers()) {
            case 1:
                player_id = 0;
                position_id = 0;
                break;
            case 2:
                if      (modulo == 0) { player_id = 0;  position_id = 0; }
                else if (modulo == 1) { player_id = 1;  position_id = 2; }
                break;
            case 3:
                if      (modulo == 0) { player_id = 0;  position_id = 0; }
                else if (modulo == 1) { player_id = 2;  position_id = 2; }
                else if (modulo == 2) { player_id = 1;  position_id = 1; }
                break;
            case 4:
                if      (modulo == 0) { player_id = 0;  position_id = 0; }
                else if (modulo == 1) { player_id = 3;  position_id = 3; }
                else if (modulo == 2) { player_id = 2;  position_id = 2; }
                else if (modulo == 3) { player_id = 1;  position_id = 1; }
                break;
        }
        animatePos(player_id, position_id);
    }

    public void animatePos(int player_id, int pos) {
        calculateEndPositions(player_id, pos);
        calculateOffsets(pos);
        if (pos % 2 == 0) {
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
        }
        return 0;
    }

    private int getFixedPosition(int player_id) {
        CardStack cs = view_.getController().getPlayerById(player_id).getHand();
        for (int i = 0; i < cs.getCardStack().size(); i++) {
            if (cs.getCardAt(i).getFixedPosition() == null) {
                return i;
            }
        }
        return 0;
    }

    private void goToNextAnimation(int player_id) {//////////////////////////////////////////////////////////////TODO
        bitmap_ = view_.getController().getDeck().getBacksideBitmap();
        cards_to_draw--;
        Coordinate card_position = new Coordinate(hand_card_x, hand_card_y);
        view_.getController().getPlayerById(player_id).getHand().getCardAt(getPosition(player_id))
                .setPosition(card_position);
        view_.getController().getPlayerById(player_id).getHand().getCardAt(getFixedPosition(player_id)).
                setFixedPosition(new Coordinate(card_position));
        hand_card_x = view_.getController().getDeck().getCoordinate().getX();
        hand_card_y = view_.getController().getDeck().getCoordinate().getY();
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
                backside.getWidth(), backside .getHeight(), matrix, true);
        return rotatedBitmap;
    }
}

