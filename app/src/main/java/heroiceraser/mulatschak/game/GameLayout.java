package heroiceraser.mulatschak.game;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.helpers.DisplayDimension;

/**
 * Created by Daniel Metzner on 11.08.2017.
 */

public class GameLayout {

    //----------------------------------------------------------------------------------------------
    //  Constants
    //
    public final int POSITION_BOTTOM = 0;
    public final int POSITION_LEFT = 1;
    public final int POSITION_TOP = 2;
    public final int POSITION_RIGHT = 3;

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //

    // Dimensions
    private Point screen_size_;

    private int card_width, card_height;
    private int discardPile_width, discardPile_height;

    private Point deck_position_;
    private List<Point> discard_pile_positions_;

    private int small_button_size_;
    private int symbol_button_size;

    private Point hand_bottom_;
    private Point hand_left_;
    private Point hand_top_;
    private Point hand_right_;

    private Point dealer_button_size_;
    private Point dealer_button_bottom_;
    private Point dealer_button_left_;
    private Point dealer_button_top_;
    private Point dealer_button_right_;

    //---- ButtonBar
    private Point button_bar_size_;
    private Point button_bar_position_;
    private Point button_bar_big_button_size_;
    private Point button_bar_small_button_size_;
    private Point button_bar_button_position_right_;
    private Point button_bar_button_position_middle_;
    private Point button_bar_button_position_left_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameLayout() { }

    public void init(GameView view) {
       // calculate sizes
        calculateDimensions(view);
        calculateCardSize(view);
        calculateButtonBarSize();
        calculateButtonBarSmallButtonSize();
        calculateButtonBarBigButtonSize();
        calculateDiscardPileSize();     // right now same as card size
        calculateDealerButtonSize();

        // calculate positions
        initButtonBarPosition();
        initButtonBarButtonPositionRight();
        initButtonBarButtonPositionMiddle(); // relative to right one
        initButtonBarButtonPositionLeft(); // relative to the middle one
        initDeckPosition();
        initDiscardPilePositions();         // uses deck position
        initHandPositions();                // uses deck position
        initDealerButtonPositions();        // uses hand position
        small_button_size_ = screen_size_.x / 4;
        symbol_button_size = screen_size_.x / 3;
    }

    //----------------------------------------------------------------------------------------------
    //  calculateDimensions()
    //
    private void calculateDimensions(GameView view) {
        screen_size_ = new Point();
        screen_size_.x = (int) DisplayDimension.getWidth(view);
        screen_size_.y = (int) DisplayDimension.getHeight(view);
    }

    private void calculateCardSize(GameView view) {
        final double HEIGHT_FACTOR = 1.28;
        double max_cards_per_hand = view.getController().getLogic().getMaxCardsPerHand();
        card_width = (int) (screen_size_.x / (max_cards_per_hand + 1));
        card_height = (int) (card_width * HEIGHT_FACTOR);
    }

    private void calculateDiscardPileSize() {
        discardPile_width = card_width;
        discardPile_height = card_height;
    }

    private void calculateButtonBarSize() {
        button_bar_size_ = new Point();
        button_bar_size_.x = screen_size_.x;
        button_bar_size_.y = screen_size_.y / 8;
    }

    private void initButtonBarPosition() {
        button_bar_position_ = new Point();
        button_bar_position_.x = 0;
        button_bar_position_.y = screen_size_.y - button_bar_size_.y;
    }

    private void calculateButtonBarBigButtonSize() {
        button_bar_big_button_size_ = new Point();
        button_bar_big_button_size_.x = (int) (button_bar_size_.x / 3.0);
        button_bar_big_button_size_.y = (int) (button_bar_size_.y * (7.0 / 9.0));
    }

    private void calculateButtonBarSmallButtonSize() {
        button_bar_small_button_size_ = new Point();
        button_bar_small_button_size_.x = (int) (button_bar_size_.x / 5.0);
        button_bar_small_button_size_.y = (int) (button_bar_size_.y * (7.0 / 9.0));
    }

    private void calculateDealerButtonSize() {
        dealer_button_size_ = new Point();
        dealer_button_size_.x = (int) (card_width / 3.0 * 2.0);
        dealer_button_size_.y = dealer_button_size_.x;
    }

    public double getOverlapFactor(int pos) {
        switch (pos) {
            case 0: return 1;
            case 1: return 9.5;
            case 2: return 7.0;
            case 3: return 9.5;
            default: return 1;
        }
    }

    private int dealerButtonOffset(int position) {
        return (int) (card_width + (5.1 * card_width / getOverlapFactor(position)));
    }

    private void initDealerButtonPositions() {

        dealer_button_bottom_ = new Point(hand_bottom_.x - (int) (card_width / 3.0),
                hand_bottom_.y - dealer_button_size_.y);

        dealer_button_left_ = new Point(1,
                hand_left_.y - dealerButtonOffset(POSITION_LEFT));

        dealer_button_top_ = new Point(hand_top_.x + dealerButtonOffset(POSITION_TOP),
                1);

        dealer_button_right_ = new Point(screen_size_.x - dealer_button_size_.x,
                hand_right_.y + (int) (dealerButtonOffset(POSITION_RIGHT) * 1.1) );
    }

    private void initButtonBarButtonPositionRight() {
        button_bar_button_position_right_ = new Point();
        button_bar_button_position_right_.x = (int)
                (button_bar_position_.x + button_bar_size_.x - button_bar_big_button_size_.x * 1.1);
        button_bar_button_position_right_.y = (int)
                (button_bar_position_.y + (button_bar_size_.y * (1 / 9.0)));
    }

    private void initButtonBarButtonPositionMiddle() {
        button_bar_button_position_middle_ = new Point();
        button_bar_button_position_middle_.x = (int) (button_bar_button_position_right_.x -
                button_bar_big_button_size_.x * 1.1);
        button_bar_button_position_middle_.y = button_bar_button_position_right_.y;
    }

    private void initButtonBarButtonPositionLeft() {
        button_bar_button_position_left_ = new Point();
        button_bar_button_position_left_.x = (int) (button_bar_small_button_size_.x * 0.15);
        button_bar_button_position_left_.y = button_bar_button_position_right_.y;
    }





    //----------------------------------------------------------------------------------------------
    //  calculateDeckPosition()
    //
    private void initDeckPosition() {
        deck_position_ = new Point( (int) ((screen_size_.x / 2.0) - (card_width / 2.0)),
                                    (int) (screen_size_.y / 3.2) );
    }

    //----------------------------------------------------------------------------------------------
    //  calculateHandPositions()
    //
    private void initHandPositions() {
        hand_bottom_ = new Point((int) ((screen_size_.x - card_width * 5) / 2.0),
                                (int) (screen_size_.y - button_bar_size_.y - card_height * 1.2) );

        hand_left_ = new Point((int) (card_width * -0.6),
                                (int) (deck_position_.y - card_width) );

        hand_top_ = new Point(    (int) (screen_size_.x - card_width * 3),
                                        (int) (card_height * (-0.6)) );

        hand_right_ = new Point(  (int) (screen_size_.x - (card_width * 0.6)),
                                 (int) deck_position_.y );
    }

    //----------------------------------------------------------------------------------------------
    //  calculateDiscardPilePositions()
    //                                    has to be called after calculateDeckPositions
    //
    private void initDiscardPilePositions() {
        discard_pile_positions_ = new ArrayList<Point>();
        int midpoint_x = deck_position_.x + card_width / 2;
        int midpoint_y = deck_position_.y + card_height / 2;

        discard_pile_positions_.add(new Point( (int) (midpoint_x - (card_width  / 2)),
                                                (int) (midpoint_y) ));

        discard_pile_positions_.add(new Point( (int) (midpoint_x - (card_width * 1.5)),
                                                (int) (midpoint_y - (card_height / 2)) ));

        discard_pile_positions_.add(new Point( (int) (midpoint_x - (card_width  / 2)),
                                                (int) (midpoint_y - card_height) ));

        discard_pile_positions_.add(new Point( (int) (midpoint_x + (card_width / 2)),
                                                (int) (midpoint_y - (card_height / 2)) ));
    }

    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //

    //---------------------- Sizes -----------------------------------------------------------------
    public int getScreenWidth() { return screen_size_.x; }
    public int getScreenHeight() { return screen_size_.y; }

    public int getCardWidth() { return card_width; }
    public int getCardHeight() { return card_height; }

    public int getDiscardPileWidth() { return discardPile_width; }
    public int getDiscardPileHeight() { return discardPile_height; }

    //-------------------- Positions ---------------------------------------------------------------
    public Point getDeckPosition() {
        return deck_position_;
    }

    public List<Point> getDiscardPilePositions_() {
        return discard_pile_positions_;
    }

    public Point getHandBottom() {
        return hand_bottom_;
    }
    public Point getHandLeft() {
        return hand_left_;
    }
    public Point getHandTop() {
        return hand_top_;
    }
    public Point getHandRight() {
        return hand_right_;
    }

    public void setHandBottom(Point coordinate) {
        hand_bottom_ = coordinate;
    }
    public void setHandBottom(int x, int y) {
        hand_bottom_ = new Point(x, y);
    }
    public void setHandLeft(Point coordinate) {
        hand_left_ = coordinate;
    }
    public void setHandLeft(int x, int y) {
        hand_left_ = new Point(x, y);
    }
    public void setHandTop(Point coordinate) {
        hand_top_ = coordinate;
    }
    public void setHandTop(int x, int y) {
        hand_top_ = new Point(x, y);
    }
    public void setHandRight(Point coordinate) {
        hand_right_ = coordinate;
    }
    public void setHandRight(int x, int y) {
        hand_right_ = new Point(x, y);
    }

    public int getSmallButtonSize() {
        return small_button_size_;
    }
    public int getSymbolButtonSize() { return symbol_button_size; }

    public int getButtonBarWidth() {
        return button_bar_size_.x;
    }

    public int getButtonBarHeight() {
        return button_bar_size_.y;
    }

    public Point getButtonBarPosition() {
        return button_bar_position_;
    }

    public int getButtonBarBigButtonWidth() {
        return button_bar_big_button_size_.x;
    }

    public int getButtonBarBigButtonHeight() {
        return button_bar_big_button_size_.y;
    }

    public Point getButtonBarButtonPositionRight() {
        return button_bar_button_position_right_;
    }

    public Point getButtonBarButtonPositionMiddle() {
        return button_bar_button_position_middle_;
    }

    public int getButtonBarSmallButtonWidth() {
        return button_bar_small_button_size_.x;
    }

    public int getButtonBarSmallButtonHeight() {
        return button_bar_small_button_size_.y;
    }

    public Point getButtonBarButtonPositionLeft() {
        return button_bar_button_position_left_;
    }

    public int getDealerButtonSize() {
        return dealer_button_size_.x;
    }

    public Point getDealerButtonPosition(int position) {
        switch (position) {
            case POSITION_BOTTOM:
                return dealer_button_bottom_;
            case POSITION_LEFT:
                return dealer_button_left_;
            case POSITION_TOP:
                return dealer_button_top_;
            case POSITION_RIGHT:
                return dealer_button_right_;
        }
        Log.e("GameLayout", "getDealerButtonPosition() was called with: " + position +" ,but ony 0-4 is valid");
        return new Point(0, 0);
    }
}
