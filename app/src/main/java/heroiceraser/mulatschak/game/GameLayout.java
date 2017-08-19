package heroiceraser.mulatschak.game;

import heroiceraser.mulatschak.helpers.Coordinate;
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
    private int small_button_size_;

    private Coordinate hand_bottom_;
    private Coordinate hand_left_;
    private Coordinate hand_top_;
    private Coordinate hand_right_;


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
    }

    //----------------------------------------------------------------------------------------------
    //  calculateDeckPosition()
    //
    public void initDeckPosition(MulatschakDeck deck) {
        deck.setCoordinate( (int) ((screen_width / 2) - (card_width / 2)),
                            (int) (screen_height / 3.2) );
    }

    //----------------------------------------------------------------------------------------------
    //  calculateHandPositions()
    //
    public void initHandPositions(MulatschakDeck deck) {
        hand_bottom_ = new Coordinate((int) ((screen_width - card_width * 5) / 2.0),
                                (int) (screen_height - card_height * 2.0) );

        hand_left_ = new Coordinate((int) (card_width * -0.6),
                                (int) (deck.getCoordinate().getY() - card_width) );

        hand_top_ = new Coordinate(    (int) (screen_width - card_width * 3),
                                        (int) (card_height * (-0.6)) );

        hand_right_ = new Coordinate(  (int) (screen_width - (card_width * 0.6)),
                                 (int) deck.getCoordinate().getY() );
    }

    //----------------------------------------------------------------------------------------------
    //  calculateDiscardPilePositions()
    //                                    has to be called after calculateDeckPositions
    //
    public void initDiscardPilePositions(Coordinate deck_pos, DiscardPile discard_pile) {
        int midpoint_x = deck_pos.getX() + card_width / 2;
        int midpoint_y = deck_pos.getY() + card_height / 2;

        discard_pile.getCoordinates().add(new Coordinate(
                                                        (int) (midpoint_x - (card_width  / 2)),
                                                        (int) (midpoint_y) ));

        discard_pile.getCoordinates().add(new Coordinate(
                                                        (int) (midpoint_x - (card_width * 1.5)),
                                                        (int) (midpoint_y - (card_height / 2)) ));

        discard_pile.getCoordinates().add(new Coordinate(
                                                        (int) (midpoint_x - (card_width  / 2)),
                                                        (int) (midpoint_y - card_height) ));

        discard_pile.getCoordinates().add(new Coordinate(
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

    public Coordinate getHandBottom() {
        return hand_bottom_;
    }
    public Coordinate getHandLeft() {
        return hand_left_;
    }
    public Coordinate getHandTop() {
        return hand_top_;
    }
    public Coordinate getHandRight() {
        return hand_right_;
    }

    public void setHandBottom(Coordinate coordinate) {
        hand_bottom_ = coordinate;
    }
    public void setHandBottom(int x, int y) {
        hand_bottom_ = new Coordinate(x, y);
    }
    public void setHandLeft(Coordinate coordinate) {
        hand_left_ = coordinate;
    }
    public void setHandLeft(int x, int y) {
        hand_left_ = new Coordinate(x, y);
    }
    public void setHandTop(Coordinate coordinate) {
        hand_top_ = coordinate;
    }
    public void setHandTop(int x, int y) {
        hand_top_ = new Coordinate(x, y);
    }
    public void setHandRight(Coordinate coordinate) {
        hand_right_ = coordinate;
    }
    public void setHandRight(int x, int y) {
        hand_right_ = new Coordinate(x, y);
    }

    public int getSmallButtonSize() {
        return small_button_size_;
    }
}
