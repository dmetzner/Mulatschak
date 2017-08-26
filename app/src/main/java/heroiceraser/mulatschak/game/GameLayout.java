package heroiceraser.mulatschak.game;

import android.graphics.Point;

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
    private int button_bar_size_;
    private int card_width, card_height;
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

    //----------------------------------------------------------------------------------------------
    //  calculateDimensions()
    //
    public void calculateDimensions(GameView view) {
        screen_width = DisplayDimension.getWidth(view);
        screen_height = DisplayDimension.getHeight(view);
        card_width = (int) (screen_width /
                (view.getController().getLogic().getMaxCardsPerHand() + 1));
        card_height = (int) (card_width * 1.28);
        small_button_size_ = screen_width / 4;
        symbol_button_size = screen_width / 3;
    }

    public void calculateButtonBarPosition() {
        button_bar_size_ = screen_height / 6;
        button_bar_ = new Point();
        button_bar_.x = (0);
        button_bar_.y = (screen_height - button_bar_size_);
    }

    //----------------------------------------------------------------------------------------------
    //  calculateDeckPosition()
    //
    public void initDeckPosition(MulatschakDeck deck) {
        deck.setPoint( (int) ((screen_width / 2) - (card_width / 2)),
                            (int) (screen_height / 3.2) );
    }

    //----------------------------------------------------------------------------------------------
    //  calculateHandPositions()
    //
    public void initHandPositions(MulatschakDeck deck) {
        hand_bottom_ = new Point((int) ((screen_width - card_width * 5) / 2.0),
                                (int) (screen_height - button_bar_size_ - card_height * 1.2) );

        hand_left_ = new Point((int) (card_width * -0.6),
                                (int) (deck.getPoint().y - card_width) );

        hand_top_ = new Point(    (int) (screen_width - card_width * 3),
                                        (int) (card_height * (-0.6)) );

        hand_right_ = new Point(  (int) (screen_width - (card_width * 0.6)),
                                 (int) deck.getPoint().y );
    }

    //----------------------------------------------------------------------------------------------
    //  calculateDiscardPilePositions()
    //                                    has to be called after calculateDeckPositions
    //
    public void initDiscardPilePositions(Point deck_pos, DiscardPile discard_pile) {
        int midpoint_x = deck_pos.x + card_width / 2;
        int midpoint_y = deck_pos.y + card_height / 2;

        discard_pile.getPoints().add(new Point(
                                                        (int) (midpoint_x - (card_width  / 2)),
                                                        (int) (midpoint_y) ));

        discard_pile.getPoints().add(new Point(
                                                        (int) (midpoint_x - (card_width * 1.5)),
                                                        (int) (midpoint_y - (card_height / 2)) ));

        discard_pile.getPoints().add(new Point(
                                                        (int) (midpoint_x - (card_width  / 2)),
                                                        (int) (midpoint_y - card_height) ));

        discard_pile.getPoints().add(new Point(
                                                        (int) (midpoint_x + (card_width / 2)),
                                                        (int) (midpoint_y - (card_height / 2)) ));
    }

    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public int getCardWidth() { return card_width; }
    public int getCardHeight() { return card_height; }

    public int getScreenWidth() { return screen_width; }
    public int getScreenHeight() { return screen_height; }

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
