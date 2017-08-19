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
    private int turn_;
    private int turn_last_round_;
    private int trumph_player_id;
    private int trumph_;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameLogic() {


    }

    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public int getMaxCardsPerHand() {
        return MAX_CARDS_PER_HAND;
    }

    public int getMaxPlayers() { return  MAX_PLAYERS; }

    public int getStartLives() { return START_LIVES; }

    public int getTurn() { return turn_; }

    public int getTurnLastRound() {
        return turn_last_round_;
    }

    public int getTrumph() { return trumph_; }

    public void setTrumph(int trumph){
        trumph_ = trumph;
    }
}
