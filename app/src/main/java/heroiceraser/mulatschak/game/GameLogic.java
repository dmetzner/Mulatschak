package heroiceraser.mulatschak.game;

/**
 * Created by Daniel Metzner on 11.08.2017.
 */

public class GameLogic {

    //----------------------------------------------------------------------------------------------
    //  Constants
    //
    private final static int MAX_PLAYERS = 4;
    private final static int MAX_CARDS_PER_HAND = 5;
    private final static int START_LIVES = 21;

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private int dealer_;
    private int turn_;
    private int turn_last_round_;
    private int trumph_player_id_;
    private int trumphs_to_make_;
    private int trumph_;
    private int last_trick_id_;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameLogic() {
        dealer_ = -1;
        turn_ = -1;
        turn_last_round_ = -1;
        trumph_player_id_ = -1;
        trumphs_to_make_ = -1;
        trumph_ = -1;
        last_trick_id_ = -1;
    }

    public void turnToNextPlayer(int players) {
        turn_++;
        if (turn_ >= players) {
            turn_ = 0;
        }
    }

    public int getFirstBidder(int players) {
        int first_bidder = dealer_ + 1;
        if (first_bidder >= players) {
            first_bidder = 0;
        }
        return first_bidder;
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public int getMaxCardsPerHand() {
        return MAX_CARDS_PER_HAND;
    }

    public int getMaxPlayers() { return  MAX_PLAYERS; }

    public int getStartLives() { return START_LIVES; }

    public int getDealer() { return dealer_; }

    public void setDealer(int dealer) { dealer_ = dealer; }

    public void setTurn(int turn) { turn_ = turn; }

    public int getTurn() { return turn_; }

    public int getTurnLastRound() {
        return turn_last_round_;
    }

    public int getTrumph() { return trumph_; }

    public int getTrumphPlayerId() { return  trumph_player_id_; }

    public void setTrumphPlayerId(int trumph_player_id) { trumph_player_id_ = trumph_player_id; }

    public void setTrumphsToMake(int trumphs) {
        trumphs_to_make_ = trumphs;
    }

    public int getTrumphsToMake() { return trumphs_to_make_; }

    public void setTrumph(int trumph){
        trumph_ = trumph;
    }

    public int getLastTrickId() {
        return last_trick_id_;
    }

    public void setLastTrickId(int last_trick_id) {
        last_trick_id_ = last_trick_id;
    }
}
