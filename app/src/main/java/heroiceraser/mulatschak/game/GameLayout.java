package heroiceraser.mulatschak.game;

import android.graphics.Point;

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
    private int screen_width, screen_height;

    private int card_width, card_height;
    private int discardPile_width, discardPile_height;

    private Point deck_position_;
    private List<Point> discard_pile_positions_;

    private int button_bar_size_;
    private int small_button_size_;
    private int symbol_button_size;

    private Point button_bar_;

    private Point hand_bottom_;
    private Point hand_left_;
    private Point hand_top_;
    private Point hand_right_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameLayout() { }

    public void init(GameView view) {
        calculateDimensions(view);
        calculateCardSize(view);
        calculateButtonBarSize();
        calculateDiscardPileSize();     // right now same as card size
        initButtonBarPosition();
        initDeckPosition();
        initDiscardPilePositions();         // uses deck position
        initHandPositions();                // uses deck position
        small_button_size_ = screen_width / 4;
        symbol_button_size = screen_width / 3;
    }

    //----------------------------------------------------------------------------------------------
    //  calculateDimensions()
    //
    private void calculateDimensions(GameView view) {
        screen_width = DisplayDimension.getWidth(view);
        screen_height = DisplayDimension.getHeight(view);
    }

    //----------------------------------------------------------------------------------------------
    //  calculateCardSize:
    //                      calculates card size based on the screen width
    //
    private void calculateCardSize(GameView view) {
        final double HEIGHT_FACTOR = 1.28;
        double max_cards_per_hand = view.getController().getLogic().getMaxCardsPerHand();
        card_width = (int) (screen_width / (max_cards_per_hand + 1));
        card_height = (int) (card_width * HEIGHT_FACTOR);
    }

    private void calculateDiscardPileSize() {
        discardPile_width = card_width;
        discardPile_height = card_height;
    }

    private void calculateButtonBarSize() {
        button_bar_size_ = screen_height / 6;
    }

    public void initButtonBarPosition() {
        button_bar_ = new Point(0, screen_height - button_bar_size_);
}


    //----------------------------------------------------------------------------------------------
    //  calculateDeckPosition()
    //
    private void initDeckPosition() {
        deck_position_ = new Point( (int) ((screen_width / 2.0) - (card_width / 2.0)),
                                    (int) (screen_height / 3.2) );
    }

    //----------------------------------------------------------------------------------------------
    //  calculateHandPositions()
    //
    private void initHandPositions() {
        hand_bottom_ = new Point((int) ((screen_width - card_width * 5) / 2.0),
                                (int) (screen_height - button_bar_size_ - card_height * 1.2) );

        hand_left_ = new Point((int) (card_width * -0.6),
                                (int) (deck_position_.y - card_width) );

        hand_top_ = new Point(    (int) (screen_width - card_width * 3),
                                        (int) (card_height * (-0.6)) );

        hand_right_ = new Point(  (int) (screen_width - (card_width * 0.6)),
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
    public int getScreenWidth() { return screen_width; }
    public int getScreenHeight() { return screen_height; }

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

    public int getButtonBarSize() {
        return button_bar_size_;
    }

    public Point getButtonBar() {
        return button_bar_;
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
}
