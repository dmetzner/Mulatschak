package heroiceraser.mulatschak.game;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.game.GamePlay.ChooseTrump.ChooseTrumpAnimation;
import heroiceraser.mulatschak.game.GamePlay.TrickBids.MakeBidsAnimation;
import heroiceraser.mulatschak.helpers.DisplayDimension;


// -----------------------------------------------------------------
//-----------Round Info Box ------------|  -  -  -  -  -  -  -  -  -
//                                      |                              Sector 0
//                                      |  -  2/8 size -  -  -  -  -
//                                      |                              Sector 1
// - - - - - - - - - - - - - - - - - - -|  -  -  -  -  -  -  -  -  -
//               MyPlayer 2               |                              Sector 2
// MyPlayer 1                             |  -  -  -  -  -  -  -  -  -
//               DP0                    |                              Sector 3
//           Dp3 Deck Dp1               |  -  4/8 size -  -  -  -  -
//               Dp2                    |                              Sector 4
//                             MyPlayer 3 |  -  -  -  -  -  -  -  -  -
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


    //---- RoundInfoOld -> Sector 0,1 --------------
    private Point round_info_size_;
    private Point round_info_position_;


    //---- ButtonBar  -> Sector 7 ---------------
    private Point button_bar_size_;
    private Point button_bar_position_;
    private Point button_bar_big_button_size_;
    private Point button_bar_small_button_size_;
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
    public GameLayout() {
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
        initPlayerInfoLayout();
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
        //final double HEIGHT_FACTOR = 1.28;
        double max_cards_per_hand = GameLogic.MAX_CARDS_PER_HAND;
        card_size_.x = (int) (screen_size_.x / (max_cards_per_hand + 1));
        card_size_.y = (int) ((screen_size_.y) / (double) (SECTORS + 1));
        // card_size_.y = (int) (card_size_.x * HEIGHT_FACTOR);
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
        round_info_size_.y = (int) (screen_size_.y * (2.0 / SECTORS));
    }

    private void calculateRoundInfoPosition() {
        round_info_position_ = new Point(sectors_.get(0));
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

    private void initButtonBarButtonPosition() {
        button_bar_button_position_ = new Point();
        button_bar_button_position_.x = button_bar_position_.x;
        button_bar_button_position_.y = (int)
                (button_bar_position_.y + (button_bar_size_.y * (1 / 9.0)));
    }

    private void calculateButtonBarWindowPosition() {
        button_bar_window_position_ = new Point();
        button_bar_window_position_.x = 0;
        button_bar_window_position_.y = getRoundInfoSize().y;
    }

    private void calculateButtonBarWindowSize() {
        button_bar_window_size_ = new Point();
        button_bar_window_size_.x = getScreenWidth();
        button_bar_window_size_.y = getScreenHeight() - getButtonBarHeight() - getRoundInfoSize().y;
    }

    private void calculateButtonBarWindowTitleSize() {
        button_bar_window_title_size_ = new Point();
        button_bar_window_title_size_.x = (int) (button_bar_window_size_.x * (8.0 / 10.0));
        button_bar_window_title_size_.y = (int) (sectors_.get(1).y * 0.8);
    }

    private void calculateButtonBarWindowTitlePosition() {
        button_bar_window_title_position_ = new Point();
        button_bar_window_title_position_.x = getScreenWidth() / 2;
        button_bar_window_title_position_.y = (int)
                (sectors_.get(1).y * 3.15);
    }

    private void calculateButtonBarWindowTricksArrowSize() {
        button_bar_window_tricks_arrow_size_ = new Point();
        button_bar_window_tricks_arrow_size_.x = (int) (getScreenWidth() / 13.0);
        button_bar_window_tricks_arrow_size_.y = (int) (sectors_.get(1).y * 0.65);
    }

    private void calculateButtonBarWindowTricksArrowLeftPosition() {
        button_bar_window_tricks_left_arrow_position_ = new Point();
        button_bar_window_tricks_left_arrow_position_.x = (int) (getScreenWidth() / 10.0);
        button_bar_window_tricks_left_arrow_position_.y = (int) (sectors_.get(1).y * 4.6);
    }

    private void calculateButtonBarWindowTricksArrowRightPosition() {
        button_bar_window_tricks_right_arrow_position_ = new Point();
        button_bar_window_tricks_right_arrow_position_.x = (int) (getScreenWidth() * (9.0 / 10.0) -
                button_bar_window_tricks_arrow_size_.x);
        button_bar_window_tricks_right_arrow_position_.y = (int) (sectors_.get(1).y * 4.6);
    }

    private void calculateButtonBarWindowTricksSubtitlePosition() {
        button_bar_window_tricks_subtitle_pos_ = new Point();
        button_bar_window_tricks_subtitle_pos_.x = 0;
        button_bar_window_tricks_subtitle_pos_.y = (int) (sectors_.get(1).y * 3.3);
    }

    private void calculateButtonBarWindowTricksSubtitleMaxSize() {
        button_bar_window_tricks_subtitle_max_size_ = new Point();
        button_bar_window_tricks_subtitle_max_size_.x = (int) (getScreenWidth() * (8.0/10.0));
        button_bar_window_tricks_subtitle_max_size_.y = sectors_.get(1).y;
    }

    private void calculateButtonBarWindowTricksDiscardPilePositions() {
        button_bar_window_tricks_discard_pile_positions_ = new ArrayList<>();
        for (int i = 0; i < discard_pile_positions_.size(); i++) {
            Point tmp = new Point(discard_pile_positions_.get(i));
            tmp.y += sectors_.get(1).y * 0.7;
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
        deck_position_ = new Point((int) ((screen_size_.x / 2.0) - (card_size_.x / 2.0)),
                (int) ((sectors_.get(4).y) - (card_size_.y / 4.0)));
    }

    //
    //----  calculateDiscardPile ----------------
    //
    private void calculateDiscardPileSize() {
        discard_pile_size_ = new Point(card_size_);
    }

    private void initDiscardPilePositions() {
        discard_pile_positions_ = new ArrayList<>();
        int midpoint_x = (int) (deck_position_.x + card_size_.x / 2.0);
        int midpoint_y = (int) (deck_position_.y + card_size_.y / 2.0);

        int offset = (int) (((getOnePercentOfScreenWidth() + getCardHeight()) / 2) / 10);

        // down
        discard_pile_positions_.add(new Point(midpoint_x - (card_size_.x / 2),
                (midpoint_y + offset / 2)));

        // left
        discard_pile_positions_.add(new Point((int) (midpoint_x - (card_size_.x * 1.50) - offset),
                midpoint_y - (card_size_.y / 2)));

        // top
        discard_pile_positions_.add(new Point(midpoint_x - (card_size_.x / 2),
                (midpoint_y - (card_size_.y) - offset / 2)));

        // right
        discard_pile_positions_.add(new Point(midpoint_x + (card_size_.x / 2) + offset,
                midpoint_y - (card_size_.y / 2)));
    }

    //
    //---- calculateHandPositions() -------------
    //
    private void initHandPositions() {
        hand_positions_ = new ArrayList<>();

        hand_bottom_ = new Point((int) ((screen_size_.x - card_size_.x * 5) / 2.0),
                sectors_.get(6).y);

        hand_left_ = new Point((int) (card_size_.x * -0.6),
                (int) (deck_position_.y - card_size_.y / 3.2));

        hand_top_ = new Point((screen_size_.x - (int) (card_size_.x * 2.8)),
                (int) (sectors_.get(2).y + card_size_.y * (-0.6)));

        hand_right_ = new Point((int) (screen_size_.x - (card_size_.x * 0.6)),
                (int) (deck_position_.y + card_size_.y / 3.0));

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
            case 0:
                return 1;
            case 1:
                return 9.5;
            case 2:
                return 7.0;
            case 3:
                return 9.5;
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
        //dealer_button_size_.x = (int) (card_size_.x / 3.0 * 2.0);
        //noinspection SuspiciousNameCombination
        dealer_button_size_.y = dealer_button_size_.x;
    }


    private int smallPadding() {
        return (int)(getCardHeight() * 0.1);
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
        int size = (int) (card_size_.y * (5.0 / 6.0));
        player_info_size_ = new Point(size, size);

        int offset = (int) (card_size_.y / 5.0);

        player_info_bottom_position_ = new Point(screen_size_.x / 2 - player_info_size_.x / 2,
                sectors_.get(sectors_.size() - 2).y - player_info_size_.y - offset);

        player_info_left_position_ = new Point(offset,
                hand_left_.y + (int) (1.25 * player_info_size_.y));

        player_info_top_position_ = new Point(hand_top_.x - (int) (player_info_size_.x * 1.2),
                sectors_.get(2).y + (int) (offset * (2.0 / 3.0)));

        player_info_right_position_ = new Point(screen_size_.x - (player_info_size_.x + offset),
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
        int size_x = (int) ((screen_size_.x * 8.0 / 10.0) / MakeBidsAnimation.MAX_BID_COLS);
        int size_y = sectors_.get(1).y;
        trick_bids_number_button_size_.x = size_x;
        trick_bids_number_button_size_.y = size_y;
    }

    private void calculateTrickBidsNumberButtonPosition() {
        trick_bids_number_button_position_ = new Point();
        trick_bids_number_button_position_.x = (int) ((screen_size_.x -
                MakeBidsAnimation.MAX_BID_COLS * trick_bids_number_button_size_.x) / 2.0);
        trick_bids_number_button_position_.y = (int) (sectors_.get(1).y * 2.8);
    }

    private void calculateTrickBidsTrumpButtonSize() {
        trick_bids_trump_button_size = new Point();
        int size_x = (int) ((screen_size_.x * 8.0 / 10.0) / (ChooseTrumpAnimation.MAX_TRUMP_COLS + 0.5));
        int size_y = (int) (sectors_.get(1).y * 1.5);
        trick_bids_trump_button_size.x = size_x;
        trick_bids_trump_button_size.y = size_y;
    }

    private void calculateTrickBidsTrumpButtonPosition() {
        trick_bids_trump_button_position_ = new Point();
        trick_bids_trump_button_position_.x = (int) ((screen_size_.x -
                ChooseTrumpAnimation.MAX_TRUMP_COLS * trick_bids_trump_button_size.x) / 2.0);
        trick_bids_trump_button_position_.y = (int) (sectors_.get(1).y * 2.8);
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
        card_exchange_text_position_.x = (int) ((screen_size_.x - card_exchange_text_size_.x) / 2.0);
        card_exchange_text_position_.y = sectors_.get(2).y + sectors_.get(1).y / 2;
    }

    private void initCardExchangeButtonPosition() {
        card_exchange_button_position_ = new Point();
        card_exchange_button_position_.x = (int)
                ((screen_size_.x - card_exchange_button_size_.x) / 2.0);
        card_exchange_button_position_.y = (sectors_.get(4).y + sectors_.get(1).y / 2);
    }

    //-----------------------


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //

    //---------------------- Sizes -----------------------------------------------------------------
    public int getScreenWidth() {
        return screen_size_.x;
    }

    public int getScreenHeight() {
        return screen_size_.y;
    }

    public int getCardWidth() {
        return card_size_.x;
    }

    public int getCardHeight() {
        return card_size_.y;
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

    public int getButtonBarBigButtonWidth() {
        return button_bar_big_button_size_.x;
    }

    public int getButtonBarBigButtonHeight() {
        return button_bar_big_button_size_.y;
    }

    public Point getButtonBarButtonPosition() {
        return button_bar_button_position_;
    }

    public int getButtonBarSmallButtonWidth() {
        return button_bar_small_button_size_.x;
    }

    public int getButtonBarSmallButtonHeight() {
        return button_bar_small_button_size_.y;
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

    public Point getButtonBarWindowTricksSubtitlePosition(){
            return button_bar_window_tricks_subtitle_pos_;
    }
    public Point getButtonBarWindowTricksSubtitleMaxSize(){
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

    public Point getRoundInfoSize() {
        return round_info_size_;
    }

    public Point getRoundInfoPositon() {
        return round_info_position_;
    }

    public List<Point> getSectors() {
        return sectors_;
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
}
