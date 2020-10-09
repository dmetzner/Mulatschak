package heroiceraser.mulatschak.game;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.game.GamePlay.ChooseTrump.ChooseTrumpAnimation;
import heroiceraser.mulatschak.game.GamePlay.TrickBids.MakeBidsAnimation;
import heroiceraser.mulatschak.utils.DisplayDimension;


// -----------------------------------------------------------------
//               MyPlayer 2             |
//                                      |
// MyPlayer 1                           |  -  -  -  -  -  -  -  -  -
//               DP0                    |
//           Dp3 Deck Dp1               |  -  6/8 size -  -  -  -  -
//               Dp2                    |
//                           MyPlayer 3 |  -  -  -  -  -  -  -  -  -
//                                      |
//--      CARD 0-5 from player 0       -|  -  -  -  -  -  -  -  -  -
//---  |  C ||  C ||  C ||  C ||  C | --|     1/8 size
//---------------ButtonBar--------------|  -  -  -  -  -  -  -  -  -
//-----MENU---TRICKS---GAMESTATUS-------|     1/8 size
//--------------------------------------|---------------------------

public class GameLayout {

    private Point screen_dimensions;

    private final int VERTICAL_GRID_SIZE = 1000;

    //-- Card Size
    private Point cardSize;


    //---- GamePlay -> Sector 0,1,2,3,6 ---------

    //-- Card Deck
    private Point deck_position_;

    //-- DiscardPile
    private Point discard_pile_size_;
    private List<Point> discard_pile_positions_;

    //-- MyPlayer Hands
    static final int POSITION_BOTTOM = 0;
    public static final int POSITION_LEFT = 1;
    public static final int POSITION_TOP = 2;
    public static final int POSITION_RIGHT = 3;
    private Point hand_bottom_;
    private Point hand_left_;
    private Point hand_top_;
    private Point hand_right_;
    private List<Point> hand_positions_;

    //-- DealerButton
    private Point dealer_button_size_;
    private Point dealer_button_bottom_;
    private Point dealer_button_left_;
    private Point dealer_button_top_;
    private Point dealer_button_right_;

    //-- TrickBidsView
    private List<Point> trick_bids_game_play_positions_;

    //-- MyPlayer Info
    private List<Point> player_info_positions_;
    private Point player_info_bottom_position_;
    private Point player_info_left_position_;
    private Point player_info_top_position_;
    private Point player_info_right_position_;
    private Point player_info_size_;


    //---- ButtonBar  -> Sector 7 ---------------
    private Point button_bar_size_;
    private Point button_bar_position_;
    private Point button_bar_button_size_;
    private Point button_bar_button_position_;

    //---- ButtonBar Windows
    private Point button_bar_window_position_;
    private Point button_bar_window_size_;
    private Point button_bar_window_title_position_;
    private Point button_bar_window_title_size_;

    private Point button_bar_window_tricks_left_arrow_position_;
    private Point button_bar_window_tricks_right_arrow_position_;
    private Point button_bar_window_tricks_arrow_size_;
    private Point button_bar_window_tricks_subtitle_pos_;
    private Point button_bar_window_tricks_subtitle_max_size_;
    private List<Point> button_bar_window_tricks_discard_pile_positions_;

    //---- Animations ---------------------------
    //-- Trick Bids
    private Point trick_bids_number_button_size_;
    private Point trick_bids_number_button_position_;
    private Point trick_bids_trump_button_size;
    private Point trick_bids_trump_button_position_;

    //-- CardExchangeLogic
    private Point card_exchange_text_size_;
    private Point card_exchange_text_position_;
    private Point card_exchange_button_size_;
    private Point card_exchange_button_position_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameLayout(GameView view) {
        calculateDisplayDimensions(view);
        calculateCardSize();
        initGamePlayLayout();
        initPlayerInfoLayout();
        initButtonBarLayout();
        initAnimationLayout();
    }

    private void calculateDisplayDimensions(GameView view) {
        screen_dimensions = new Point();
        screen_dimensions.x = DisplayDimension.getWidth(view);
        screen_dimensions.y = DisplayDimension.getHeight(view);
    }

    public int getGridPosition(int gridOffset) {
        return (int) ((screen_dimensions.y / (double) (VERTICAL_GRID_SIZE)) * gridOffset);
    }

    public int getLengthOnVerticalGrid(int gridOffset) {
        return getGridPosition(gridOffset);
    }

    private void calculateCardSize() {
        cardSize = new Point();
        double max_cards_per_hand = GameLogic.MAX_CARDS_PER_HAND;
        cardSize.x = (int) (screen_dimensions.x / (max_cards_per_hand + 1));
        cardSize.y = getLengthOnVerticalGrid(125);
    }


    //----------------------------------------------------------------------------------------------
    //  Button Bar
    //
    private void initButtonBarLayout() {
        // Button Bar
        calculateButtonBarSize();
        calculateButtonBarPosition();
        // Button Bar Buttons
        calculateButtonBarButtonSize();
        initButtonBarButtonPosition();
        //Button Bar Windows
        calculateButtonBarWindowPosition();
        calculateButtonBarWindowSize();
        calculateButtonBarWindowTitleSize();
        calculateButtonBarWindowTitlePosition();

        calculateButtonBarWindowTricksSubtitlePosition();
        calculateButtonBarWindowTricksSubtitleMaxSize();
        calculateButtonBarWindowTricksArrowSize();
        calculateButtonBarWindowTricksArrowLeftPosition();
        calculateButtonBarWindowTricksArrowRightPosition();
        calculateButtonBarWindowTricksDiscardPilePositions();
    }

    private void calculateButtonBarSize() {
        button_bar_size_ = new Point();
        button_bar_size_.x = screen_dimensions.x;
        button_bar_size_.y = getLengthOnVerticalGrid(101);
    }

    private void calculateButtonBarPosition() {
        button_bar_position_ = new Point(0, getLengthOnVerticalGrid(VERTICAL_GRID_SIZE) - button_bar_size_.y);
    }

    private void calculateButtonBarButtonSize() {
        button_bar_button_size_ = new Point();
        button_bar_button_size_.x = (int) (button_bar_size_.x / 3.0);
        button_bar_button_size_.y = button_bar_size_.y;
    }

    private void initButtonBarButtonPosition() {
        button_bar_button_position_ = new Point();
        button_bar_button_position_.x = button_bar_position_.x;
        button_bar_button_position_.y = button_bar_position_.y;
    }

    private void calculateButtonBarWindowPosition() {
        button_bar_window_position_ = new Point();
        button_bar_window_position_.x = 0;
        button_bar_window_position_.y = 0;
    }

    private void calculateButtonBarWindowSize() {
        button_bar_window_size_ = new Point();
        button_bar_window_size_.x = getScreenWidth();
        button_bar_window_size_.y = getScreenHeight() - getButtonBarHeight();
    }

    private void calculateButtonBarWindowTitleSize() {
        button_bar_window_title_size_ = new Point();
        button_bar_window_title_size_.x = (int) (button_bar_window_size_.x * (8.0 / 10.0));
        button_bar_window_title_size_.y = getLengthOnVerticalGrid(100);
    }

    private void calculateButtonBarWindowTitlePosition() {
        button_bar_window_title_position_ = new Point();
        button_bar_window_title_position_.x = getScreenWidth() / 2;
        button_bar_window_title_position_.y = getLengthOnVerticalGrid(225);
    }

    private void calculateButtonBarWindowTricksArrowSize() {
        button_bar_window_tricks_arrow_size_ = new Point();
        button_bar_window_tricks_arrow_size_.x = (int) (getScreenWidth() / 13.0);
        button_bar_window_tricks_arrow_size_.y = getLengthOnVerticalGrid(80);
    }

    private void calculateButtonBarWindowTricksArrowLeftPosition() {
        button_bar_window_tricks_left_arrow_position_ = new Point();
        button_bar_window_tricks_left_arrow_position_.x = (int) (getScreenWidth() / 10.0);
        button_bar_window_tricks_left_arrow_position_.y = getLengthOnVerticalGrid(495);
    }

    private void calculateButtonBarWindowTricksArrowRightPosition() {
        button_bar_window_tricks_right_arrow_position_ = new Point();
        button_bar_window_tricks_right_arrow_position_.x =
                (int) (getScreenWidth() * (9.0 / 10.0) - button_bar_window_tricks_arrow_size_.x);
        button_bar_window_tricks_right_arrow_position_.y = getLengthOnVerticalGrid(495);
    }

    private void calculateButtonBarWindowTricksSubtitlePosition() {
        button_bar_window_tricks_subtitle_pos_ = new Point();
        button_bar_window_tricks_subtitle_pos_.x = 0;
        button_bar_window_tricks_subtitle_pos_.y = getLengthOnVerticalGrid(300);
    }

    private void calculateButtonBarWindowTricksSubtitleMaxSize() {
        button_bar_window_tricks_subtitle_max_size_ = new Point();
        button_bar_window_tricks_subtitle_max_size_.x = (int) (getScreenWidth() * (8.0 / 10.0));
        button_bar_window_tricks_subtitle_max_size_.y = getLengthOnVerticalGrid(125);
    }

    private void calculateButtonBarWindowTricksDiscardPilePositions() {
        button_bar_window_tricks_discard_pile_positions_ = new ArrayList<>();
        for (int i = 0; i < discard_pile_positions_.size(); i++) {
            Point tmp = new Point(discard_pile_positions_.get(i));
            tmp.y += getLengthOnVerticalGrid(125);
            button_bar_window_tricks_discard_pile_positions_.add(tmp);
        }
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
        initTrickBidsGamePlayPositions();    // uses dealer button positions
    }

    //
    //----  calculateDeckPosition() -------------
    //
    private void initDeckPosition() {
        deck_position_ = new Point((int) ((screen_dimensions.x / 2.0) - (cardSize.x / 2.0)),
                (int) (getLengthOnVerticalGrid(375) - (cardSize.y / 4.0)));
    }

    //
    //----  calculateDiscardPile ----------------
    //
    private void calculateDiscardPileSize() {
        discard_pile_size_ = new Point(cardSize);
    }

    private void initDiscardPilePositions() {
        discard_pile_positions_ = new ArrayList<>();
        int midpoint_x = (int) (deck_position_.x + cardSize.x / 2.0);
        int midpoint_y = (int) (deck_position_.y + cardSize.y / 2.0);

        int offset = (int) (((getOnePercentOfScreenWidth() + getCardHeight()) / 2) / 10);

        // down
        discard_pile_positions_.add(new Point(midpoint_x - (cardSize.x / 2),
                (midpoint_y + offset / 2)));

        // left
        discard_pile_positions_.add(new Point((int) (midpoint_x - (cardSize.x * 1.50) - offset),
                midpoint_y - (cardSize.y / 2)));

        // top
        discard_pile_positions_.add(new Point(midpoint_x - (cardSize.x / 2),
                (midpoint_y - (cardSize.y) - offset / 2)));

        // right
        discard_pile_positions_.add(new Point(midpoint_x + (cardSize.x / 2) + offset,
                midpoint_y - (cardSize.y / 2)));
    }

    //
    //---- calculateHandPositions() -------------
    //
    private void initHandPositions() {
        hand_positions_ = new ArrayList<>();

        hand_bottom_ = new Point((int) ((screen_dimensions.x - cardSize.x * 5) / 2.0),
                getLengthOnVerticalGrid(720));

        hand_left_ = new Point((int) (cardSize.x * -0.6),
                (int) (deck_position_.y - cardSize.y / 3.2));

        hand_top_ = new Point((screen_dimensions.x - (int) (cardSize.x * 2.8)),
                (int) (cardSize.y * (-0.6)));

        hand_right_ = new Point((int) (screen_dimensions.x - (cardSize.x * 0.6)),
                (int) (deck_position_.y + cardSize.y / 3.0));

        hand_positions_.add(hand_bottom_);
        hand_positions_.add(hand_left_);
        hand_positions_.add(hand_top_);
        hand_positions_.add(hand_right_);

    }

    //
    //----  HandCard Overlap factor -------------
    //
    public double getOverlapFactor(int pos) {
        switch (pos) {
            case 1:
            case 3:
                return 9.5;
            case 2:
                return 7.0;
            case 0:
            default:
                return 1;
        }
    }

    //
    //---- Dealer Button ------------------------
    //
    private void calculateDealerButtonSize() {
        dealer_button_size_ = new Point();
        dealer_button_size_.x = getCardWidth() / 2;
        dealer_button_size_.y = getCardWidth() / 2;
    }


    private int smallPadding() {
        return (int) (getCardHeight() * 0.1);
    }

    private void initDealerButtonPositions() {
        dealer_button_bottom_ = new Point(hand_bottom_.x - dealer_button_size_.x / 2,
                hand_bottom_.y - dealer_button_size_.y - smallPadding());

        dealer_button_left_ = new Point(hand_left_.x + getCardHeight() + smallPadding(),
                hand_left_.y + getCardWidth() - dealer_button_size_.x - smallPadding());

        dealer_button_top_ = new Point(hand_top_.x + smallPadding(),
                hand_top_.y + getCardHeight() + smallPadding());

        dealer_button_right_ = new Point(hand_right_.x - smallPadding() - dealer_button_size_.x,
                hand_right_.y + smallPadding());

    }

    private void initTrickBidsGamePlayPositions() {

        // offset caused by radius drawing
        int offset = dealer_button_size_.x / 2;

        Point trick_bids_game_play_bottom_ = new Point(
                dealer_button_bottom_.x + dealer_button_size_.x + offset,
                dealer_button_bottom_.y + offset);

        Point trick_bids_game_play_left_ = new Point(dealer_button_left_.x + offset,
                dealer_button_left_.y - dealer_button_size_.y + offset);

        Point trick_bids_game_play_top_ = new Point(
                dealer_button_top_.x + dealer_button_size_.x + offset,
                dealer_button_top_.y + offset);

        Point trick_bids_game_play_right_ = new Point(dealer_button_right_.x + offset,
                dealer_button_right_.y + dealer_button_size_.y + offset);

        trick_bids_game_play_positions_ = new ArrayList<>();
        trick_bids_game_play_positions_.add(trick_bids_game_play_bottom_);
        trick_bids_game_play_positions_.add(trick_bids_game_play_left_);
        trick_bids_game_play_positions_.add(trick_bids_game_play_top_);
        trick_bids_game_play_positions_.add(trick_bids_game_play_right_);
    }


    private void initPlayerInfoLayout() {
        int size = (int) (cardSize.y * (5.0 / 6.0));
        player_info_size_ = new Point(size, size);

        int offset = (int) (cardSize.y / 5.0);

        player_info_bottom_position_ = new Point(screen_dimensions.x / 2 - player_info_size_.x / 2,
                getLengthOnVerticalGrid(750) - player_info_size_.y - offset);

        player_info_left_position_ = new Point(offset,
                hand_left_.y + (int) (1.25 * player_info_size_.y));

        player_info_top_position_ = new Point(hand_top_.x - (int) (player_info_size_.x * 1.2),
                (int) (offset * (2.0 / 3.0)));

        player_info_right_position_ = new Point(screen_dimensions.x - (player_info_size_.x + offset),
                hand_right_.y - (int) (player_info_size_.y * 1.2));

        player_info_positions_ = new ArrayList<>();
        player_info_positions_.add(player_info_bottom_position_);
        player_info_positions_.add(player_info_left_position_);
        player_info_positions_.add(player_info_top_position_);
        player_info_positions_.add(player_info_right_position_);

    }

    //-----------------------

    //----------------------------------------------------------------------------------------------
    // Animations
    //
    private void initAnimationLayout() {
        calculateTrickBidsNumberButtonSize();
        calculateTrickBidsTrumpButtonSize();
        calculateTrickBidsNumberButtonPosition();
        calculateTrickBidsTrumpButtonPosition();
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
        int size_x = (int) ((screen_dimensions.x * 8.0 / 10.0) / MakeBidsAnimation.MAX_BID_COLS);
        int size_y = getLengthOnVerticalGrid(125);
        trick_bids_number_button_size_.x = size_x;
        trick_bids_number_button_size_.y = size_y;
    }

    private void calculateTrickBidsNumberButtonPosition() {
        trick_bids_number_button_position_ = new Point();
        trick_bids_number_button_position_.x = (int) ((screen_dimensions.x -
                MakeBidsAnimation.MAX_BID_COLS * trick_bids_number_button_size_.x) / 2.0);
        trick_bids_number_button_position_.y = getLengthOnVerticalGrid(250);
    }

    private void calculateTrickBidsTrumpButtonSize() {
        trick_bids_trump_button_size = new Point();
        int size_x = (int) ((screen_dimensions.x * 8.0 / 10.0) / (ChooseTrumpAnimation.MAX_TRUMP_COLS + 0.5));
        int size_y = getLengthOnVerticalGrid(190);
        trick_bids_trump_button_size.x = size_x;
        trick_bids_trump_button_size.y = size_y;
    }

    private void calculateTrickBidsTrumpButtonPosition() {
        trick_bids_trump_button_position_ = new Point();
        trick_bids_trump_button_position_.x = (int) ((screen_dimensions.x -
                ChooseTrumpAnimation.MAX_TRUMP_COLS * trick_bids_trump_button_size.x) / 2.0);
        trick_bids_trump_button_position_.y = getLengthOnVerticalGrid(250);
    }

    //
    //---- Card Exchange ------------------------
    //
    private void calculateCardExchangeSizes() {
        card_exchange_text_size_ = new Point();
        card_exchange_text_size_.x = (int) ((screen_dimensions.x / 8.0 * 6.0));
        card_exchange_text_size_.y = 0;
    }

    private void calculateCardExchangeButtonSize() {
        card_exchange_button_size_ = new Point();
        card_exchange_button_size_.x = (int) ((screen_dimensions.x / 8.0 * 6.0));
        card_exchange_button_size_.y = button_bar_button_size_.y;
    }

    private void initCardExchangePositions() {
        card_exchange_text_position_ = new Point();
        card_exchange_text_position_.x = (int) ((screen_dimensions.x - card_exchange_text_size_.x) / 2.0);
        card_exchange_text_position_.y = getLengthOnVerticalGrid(315);
    }

    private void initCardExchangeButtonPosition() {
        card_exchange_button_position_ = new Point();
        card_exchange_button_position_.x = (int)
                ((screen_dimensions.x - card_exchange_button_size_.x) / 2.0);
        card_exchange_button_position_.y = getLengthOnVerticalGrid(540);
    }

    //----------------------

    //---------------------- Sizes -----------------------------------------------------------------
    public int getScreenWidth() {
        return screen_dimensions.x;
    }

    public int getScreenHeight() {
        return screen_dimensions.y;
    }

    public int getCardWidth() {
        return cardSize.x;
    }

    public int getCardHeight() {
        return cardSize.y;
    }

    public Point getDiscardPileSize() {
        return discard_pile_size_;
    }

    public int getDiscardPileWidth() {
        return discard_pile_size_.x;
    }

    public int getDiscardPileHeight() {
        return discard_pile_size_.y;
    }

    //-------------------- Positions ---------------------------------------------------------------
    public Point getDeckPosition() {
        return deck_position_;
    }

    public List<Point> getDiscardPilePositions() {
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

    public Point getTrickBidsTrumpButtonPosition() {
        return trick_bids_trump_button_position_;
    }

    public Point getTrickBidsNumberButtonPosition() {
        return trick_bids_number_button_position_;
    }

    public Point getTrickBidsNumberButtonSize() {
        return trick_bids_number_button_size_;
    }

    public Point getTrickBidsTrumpButtonSize() {
        return trick_bids_trump_button_size;
    }

    public int getButtonBarWidth() {
        return button_bar_size_.x;
    }

    public int getButtonBarHeight() {
        return button_bar_size_.y;
    }

    public Point getButtonBarPosition() {
        return button_bar_position_;
    }

    public int getButtonBarButtonWidth() {
        return button_bar_button_size_.x;
    }

    public int getButtonBarButtonHeight() {
        return button_bar_button_size_.y;
    }

    public Point getButtonBarButtonPosition() {
        return button_bar_button_position_;
    }

    public Point getButtonBarWindowTitlePosition() {
        return button_bar_window_title_position_;
    }

    public Point getButtonBarWindowTitleSize() {
        return button_bar_window_title_size_;
    }

    public Point getButtonBarWindowPosition() {
        return button_bar_window_position_;
    }

    public Point getButtonBarWindowSize() {
        return button_bar_window_size_;
    }

    public Point getButtonBarWindowTricksLeftArrowPosition() {
        return button_bar_window_tricks_left_arrow_position_;
    }

    public Point getButtonBarWindowTricksRightArrowPosition() {
        return button_bar_window_tricks_right_arrow_position_;
    }

    public Point getButtonBarWindowTricksArrowSize() {
        return button_bar_window_tricks_arrow_size_;
    }

    public Point getButtonBarWindowTricksSubtitlePosition() {
        return button_bar_window_tricks_subtitle_pos_;
    }

    public Point getButtonBarWindowTricksSubtitleMaxSize() {
        return button_bar_window_tricks_subtitle_max_size_;
    }

    public List<Point> getButtonBarWindowTricksDiscardPilePositions() {
        return button_bar_window_tricks_discard_pile_positions_;
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

    public Point getCardExchangeButtonPosition() {
        return card_exchange_button_position_;
    }

    public Point getPlayerInfoSize() {
        return new Point(player_info_size_);
    }

    public Point getPlayerInfoLeftPos() {
        return new Point(player_info_left_position_);
    }

    public Point getPlayerInfoTopPos() {
        return new Point(player_info_top_position_);
    }

    public Point getPlayerInfoRightPos() {
        return new Point(player_info_right_position_);
    }

    public Point getPlayerInfoBottomPos() {
        return new Point(player_info_bottom_position_);
    }

    public List<Point> getPlayerInfoPositions() {
        return player_info_positions_;
    }

    public List<Point> getHandPositions() {
        return hand_positions_;
    }

    public List<Point> getTrickBidsGamePlayPositions() {
        return trick_bids_game_play_positions_;
    }

    public float getOnePercentOfScreenWidth() {
        return getScreenWidth() / 100.0f;
    }

    public float getOnePercentOfScreenHeight() {
        return getScreenHeight() / 100.0f;
    }

    public Point getCenter() {
        return new Point(getScreenWidth() / 2, getScreenHeight() / 2);
    }
}
