package heroiceraser.mulatschak.game;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.game.Animations.TrickBids;
import heroiceraser.mulatschak.helpers.DisplayDimension;

/**
 * Created by Daniel Metzner on 11.08.2017.
 */


// ------------------------------------------------------------------
//               Player 2               |                              Sector 0
// Player 1                             |  -  -  -  -  -  -  -  -  -
//               DP0                    |                              Sector 1
//           Dp3 Deck Dp1               |  -  4/8 size -  -  -  -  -
//               Dp2                    |                              Sector 2
//                             Player 3 |  -  -  -  -  -  -  -  -  -
//                                      |                              Sector 3
//-----------Round Info Box ------------|  -  -  -  -  -  -  -  -  -
//                                      |                              Sector 4
//                                      |  -  2/8 size -  -  -  -  -
//                                      |                              Sector 5
//--      CARD 0-5 from player 0       -|  -  -  -  -  -  -  -  -  -
//---  |  C ||  C ||  C ||  C ||  C | --|     1/8 size                 Sector 6
//---------------ButtonBar--------------|  -  -  -  -  -  -  -  -  -
//-----MENU---TRICKS---GAMESTATUS-------|     1/8 size                 Sector 7
//--------------------------------------|---------------------------

public class GameLayout {

    //-- Screen Dimension
    private Point screen_size_;

    // subdivide layout in 8 sector
    private final int SECTORS = 8;
    private List<Point> sectors_;
    
    //-- Card Size
    private Point card_size_;


    //---- GamePlay -> Sector 0,1,2,3,6 ---------

    //-- Card Deck
    private Point deck_position_;

    //-- DiscardPile
    private Point discard_pile_size_;
    private List<Point> discard_pile_positions_;

    //-- Player Hands
    final int POSITION_BOTTOM = 0;
    final int POSITION_LEFT = 1;
    final int POSITION_TOP = 2;
    final int POSITION_RIGHT = 3;
    private Point hand_bottom_;
    private Point hand_left_;
    private Point hand_top_;
    private Point hand_right_;

    //-- DealerButton
    private Point dealer_button_size_;
    private Point dealer_button_bottom_;
    private Point dealer_button_left_;
    private Point dealer_button_top_;
    private Point dealer_button_right_;


    //---- RoundInfo -> Sector 4,5 --------------
    private Point round_info_size_;
    private Point round_info_position_;


    //---- ButtonBar  -> Sector 7 ---------------
    private Point button_bar_size_;
    private Point button_bar_position_;
    private Point button_bar_big_button_size_;
    private Point button_bar_small_button_size_;
    private Point button_bar_button_position_right_;
    private Point button_bar_button_position_middle_;
    private Point button_bar_button_position_left_;


    //---- Animations ---------------------------
    //-- Trick Bids
    private Point trick_bids_number_button_size_;
    private Point trick_bids_trump_button_size;

    //-- CardExchange
    private Point card_exchange_text_size_;
    private Point card_exchange_text_position_;
    private Point card_exchange_button_size_;
    private Point card_exchange_button_position_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    GameLayout() {
        sectors_ = new ArrayList<>();
    }

    //----------------------------------------------------------------------------------------------
    //  Init
    //
    public void init(GameView view) {
        calculateDimensions(view);
        calculateSectors();
        calculateCardSize();
        initGamePlayLayout();
        initRoundInfoLayout();
        initButtonBarLayout();
        initAnimationLayout();
    }

    //----------------------------------------------------------------------------------------------
    //  calculateDimensions()
    //
    private void calculateDimensions(GameView view) {
        screen_size_ = new Point();
        screen_size_.x = DisplayDimension.getWidth(view);
        screen_size_.y = DisplayDimension.getHeight(view);
    }

    //----------------------------------------------------------------------------------------------
    //  calculateSectors
    //
    private void calculateSectors() {
        for (int i = 0; i < SECTORS; i++) {
            int y = (int) ((screen_size_.y / (double) (SECTORS)) * i);
            Point sector = new Point(0, y);
            sectors_.add(sector);
        }
    }

    //----------------------------------------------------------------------------------------------
    //  calculate Card size
    //
    private void calculateCardSize() {
        card_size_ = new Point();
        final double HEIGHT_FACTOR = 1.28;
        double max_cards_per_hand = GameLogic.MAX_CARDS_PER_HAND;
        card_size_.x = (int) (screen_size_.x / (max_cards_per_hand + 1));
        card_size_.y = (int) (card_size_.x * HEIGHT_FACTOR);
    }

    //----------------------------------------------------------------------------------------------
    // Round Info
    //
    private void initRoundInfoLayout() {
        calculateRoundInfoSize();
        calculateRoundInfoPosition();
    }

    private void calculateRoundInfoSize() {
        round_info_size_ = new Point();
        round_info_size_.x = screen_size_.x;
        round_info_size_.y = (int)(screen_size_.y * (2.0 / SECTORS));
    }

    private void calculateRoundInfoPosition() {
        round_info_position_ = new Point(sectors_.get(4));
    }

    //-----------------------

    //----------------------------------------------------------------------------------------------
    //  Button Bar
    //
    private void initButtonBarLayout() {
        // Button Bar
        calculateButtonBarSize();
        calculateButtonBarPosition();
        // Button Bar Buttons
        calculateButtonBarSmallButtonSize();
        calculateButtonBarBigButtonSize();
        initButtonBarButtonPositionRight();
        initButtonBarButtonPositionMiddle(); // relative to right one
        initButtonBarButtonPositionLeft();   // relative to the middle one
    }

    private void calculateButtonBarSize() {
        button_bar_size_ = new Point();
        button_bar_size_.x = screen_size_.x;
        button_bar_size_.y = (int) (screen_size_.y / (double) SECTORS);
    }

    private void calculateButtonBarPosition() {
        button_bar_position_ = new Point(sectors_.get(7));
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

    //-----------------------

    //----------------------------------------------------------------------------------------------
    //  GamePlayLayout
    //
    private void initGamePlayLayout() {
        calculateDealerButtonSize();
        calculateDiscardPileSize();     // right now same as card size
        initDeckPosition();
        initDiscardPilePositions();         // uses deck position
        initHandPositions();                // uses deck position
        initDealerButtonPositions();        // uses hand position
    }
    //
    //----  calculateDeckPosition() -------------
    //
    private void initDeckPosition() {
        deck_position_ = new Point( (int) ((screen_size_.x / 2.0) - (card_size_.x / 2.0)),
                                    (int)  (screen_size_.y / 4.0) );
    }
    //
    //----  calculateDiscardPile ----------------
    //
    private void calculateDiscardPileSize() {
        discard_pile_size_ = new Point(card_size_);
    }

    private void initDiscardPilePositions() {
        discard_pile_positions_ = new ArrayList<>();
        int midpoint_x = (int)(deck_position_.x + card_size_.x / 2.0);
        int midpoint_y = (int)(deck_position_.y + card_size_.y / 2.0);

        discard_pile_positions_.add(new Point((int)(midpoint_x - (card_size_.x  / 2.0)),
                                                   (midpoint_y) ));

        discard_pile_positions_.add(new Point((int)(midpoint_x - (card_size_.x * 1.5)),
                                              (int)(midpoint_y - (card_size_.y / 2.0)) ));

        discard_pile_positions_.add(new Point((int)(midpoint_x - (card_size_.x  / 2.0)),
                                                   (midpoint_y - (card_size_.y)) ));

        discard_pile_positions_.add(new Point((int)(midpoint_x + (card_size_.x / 2.0)),
                                              (int)(midpoint_y - (card_size_.y / 2.0)) ));
    }
    //
    //---- calculateHandPositions() -------------
    //
    private void initHandPositions() {
        hand_bottom_ = new Point((int) ((screen_size_.x - card_size_.x * 5) / 2.0),
                                 (int)  (sectors_.get(7).y - card_size_.y * 1.2));

        hand_left_ = new Point(  (int) (card_size_.x * -0.6),
                                       (deck_position_.y - card_size_.x) );

        hand_top_ = new Point(         (screen_size_.x - card_size_.x * 3),
                                 (int) (card_size_.y * (-0.6)) );

        hand_right_ = new Point( (int) (screen_size_.x - (card_size_.x * 0.6)),
                                       (deck_position_.y ) );
    }
    //
    //----  HandCard Overlap factor -------------
    //
    public double getOverlapFactor(int pos) {
        switch (pos) {
            case 0: return 1;
            case 1: return 9.5;
            case 2: return 7.0;
            case 3: return 9.5;
            default: return 1;
        }
    }
    //
    //---- Dealer Button ------------------------
    //
    private void calculateDealerButtonSize() {
        dealer_button_size_ = new Point();
        dealer_button_size_.x = (int) (card_size_.x / 3.0 * 2.0);
        dealer_button_size_.y = dealer_button_size_.x;
    }

    private int dealerButtonOffset(int position) {
        return (int) (card_size_.x + (5.1 * card_size_.x / getOverlapFactor(position)));
    }

    private void initDealerButtonPositions() {

        dealer_button_bottom_ = new Point(hand_bottom_.x - (int) (card_size_.x / 3.0),
                hand_bottom_.y - dealer_button_size_.y);

        dealer_button_left_ = new Point(1,
                hand_left_.y - dealerButtonOffset(POSITION_LEFT));

        dealer_button_top_ = new Point(hand_top_.x + dealerButtonOffset(POSITION_TOP),
                1);

        dealer_button_right_ = new Point(screen_size_.x - dealer_button_size_.x,
                hand_right_.y + (int) (dealerButtonOffset(POSITION_RIGHT) * 1.1) );
    }

    //-----------------------
    
    //----------------------------------------------------------------------------------------------
    // Animations
    //
    private void initAnimationLayout() {
        calculateTrickBidsNumberButtonSize();
        calculateTrickBidsTrumpButtonSize();
        calculateCardExchangeSizes();
        calculateCardExchangeButtonSize();
        initCardExchangePositions();
        initCardExchangeButtonPosition();
    }
    //
    //---- Trick Bids ---------------------------
    //
    private void calculateTrickBidsNumberButtonSize() {
        trick_bids_number_button_size_ = new Point();
        trick_bids_number_button_size_.x = (int) ((screen_size_.x * 8.0 / 10.0) / TrickBids.MAX_BID_COLS);
        if (trick_bids_number_button_size_.x * TrickBids.MAX_BID_ROWS > screen_size_.y / SECTORS * 4) {
            trick_bids_number_button_size_.y = ((int)((screen_size_.y * (6.0 / SECTORS)) / TrickBids.MAX_BID_ROWS));
        }
        else {
            trick_bids_number_button_size_.y = trick_bids_number_button_size_.x;
        }
    }

    private void calculateTrickBidsTrumpButtonSize() {
        trick_bids_trump_button_size = new Point();
        trick_bids_trump_button_size.x = (int) ((screen_size_.x * 8.0 / 10.0) / (TrickBids.MAX_TRUMP_COLS + 0.5));
        if (trick_bids_trump_button_size.x * TrickBids.MAX_TRUMP_ROWS > screen_size_.y / SECTORS * 4) {
            trick_bids_trump_button_size.y = (int)((screen_size_.y * (6.0 / SECTORS)) / (TrickBids.MAX_TRUMP_ROWS + 0.5));
        }
        else {
            trick_bids_trump_button_size.y = trick_bids_trump_button_size.x;
        }
    }

    //
    //---- Card Exchange ------------------------
    //
    private void calculateCardExchangeSizes() {
        card_exchange_text_size_ = new Point();
        card_exchange_text_size_.x = (int) ((screen_size_.x / 8.0 * 6.0));
        card_exchange_text_size_.y = 0;
    }

    private void calculateCardExchangeButtonSize() {
        card_exchange_button_size_ = new Point();
        card_exchange_button_size_.x = (int) ((screen_size_.x / 8.0 * 6.0));
        card_exchange_button_size_.y = button_bar_big_button_size_.y;
    }
    
    private void initCardExchangePositions() {
        card_exchange_text_position_ = new Point();
        card_exchange_text_position_.x = (int)((screen_size_.x - card_exchange_text_size_.x) / 2.0);
        card_exchange_text_position_.y = (int)(screen_size_.y / 8.0 * 1.5);
    }

    private void initCardExchangeButtonPosition() {
        card_exchange_button_position_ = new Point();
        card_exchange_button_position_.x = (int)
                ((screen_size_.x - card_exchange_button_size_.x) / 2.0);
        card_exchange_button_position_.y = hand_bottom_.y -
                (int) (card_size_.y / 3.0 * 2.0) - card_exchange_button_size_.y;
    }
    
    //-----------------------


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //

    //---------------------- Sizes -----------------------------------------------------------------
    public int getScreenWidth() { return screen_size_.x; }
    public int getScreenHeight() { return screen_size_.y; }

    public int getCardWidth() { return card_size_.x; }
    public int getCardHeight() { return card_size_.y; }

    public int getDiscardPileWidth() { return discard_pile_size_.x; }
    public int getDiscardPileHeight() { return discard_pile_size_.y; }

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

    public Point getTrickBidsNumberButtonSize() {
        return trick_bids_number_button_size_;
    }
    public Point getTrickBidsTrumpButtonSize() { return trick_bids_trump_button_size; }

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

    public int getCardExchangeTextWidth() {
        return card_exchange_text_size_.x;
    }

    public Point getCardExchangeTextPosition() {
        return card_exchange_text_position_;
    }

    public Point getCardExchangeButtonSize() {
        return card_exchange_button_size_;
    }

    public Point getCardExchangePosition() {
        return card_exchange_button_position_;
    }

    public Point getRoundInfoSize() {
        return round_info_size_;
    }

    public Point getRoundInfoPositon() {
        return round_info_position_;
    }

}
