package heroiceraser.mulatschak.game;

import heroiceraser.mulatschak.game.DrawableObjects.Card;
import heroiceraser.mulatschak.game.DrawableObjects.CardStack;
import heroiceraser.mulatschak.game.DrawableObjects.DiscardPile;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;

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
    private int starting_card_;
    private int trump_;
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
        starting_card_ = -1;
        trump_ = -1;
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

    public boolean isAValidCardPlay(Card card_to_play, CardStack hand, DiscardPile dp) {

        int card_to_play_symbol = (card_to_play.getId() / 100) % 5;

        if (isDiscardPileEmpty(dp)) { // first card
            setStartingCard(card_to_play_symbol);
            return true;
        }

        if (card_to_play_symbol == MulatschakDeck.JOKER) {
            return true;
        }

        boolean has_a_starting_card = false;
        boolean has_a_trump = false;
        for (int i = 0; i < hand.getCardStack().size(); i++) {
            if (hand.getCardAt(i).getId() / 100 % 5 == starting_card_) {
                has_a_starting_card = true;
            }
            if (hand.getCardAt(i).getId() / 100 % 5 == trump_) {
                has_a_trump = true;
            }
        }
        if (has_a_starting_card && card_to_play_symbol != starting_card_) {
            return false;
        }
        if (!has_a_starting_card && has_a_trump && card_to_play_symbol != trump_) {
            return false;
        }

        int highestCard = getHighestCardOnDiscardPile(dp);
        int h_sym = highestCard / 100 % 5;
        int h_value = highestCard % 100;
        if (h_sym == starting_card_ && card_to_play_symbol == starting_card_ && card_to_play.getId() % 100 <= h_value) {
            for (int i = 0; i < hand.getCardStack().size(); i++) {
                if (hand.getCardAt(i).getId() / 100 % 5 == starting_card_ &&
                        hand.getCardAt(i).getId() % 100 > h_value) {
                    return false;
                }
            }
        }

        if (h_sym == trump_ && card_to_play_symbol == trump_ && card_to_play.getId() % 100 <= h_value) {
            for (int i = 0; i < hand.getCardStack().size(); i++) {
                if (hand.getCardAt(i).getId() / 100 % 5 == starting_card_ &&
                        hand.getCardAt(i).getId() % 100 > h_value) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isDiscardPileEmpty(DiscardPile discard_pile) {
        for (int i = 0; i < 4; i++) {
            if (discard_pile.getCard(i) != null) {
             return false;
            }
        }
        return true;
    }

    private int getHighestCardOnDiscardPile(DiscardPile dp) {
        int highest_card_sym = -1;
        int highest_card_value = -11;

        for (int i = 0; i < 4; i++) {
            if (dp.getCard(i) != null) {
                int sym = dp.getCard(i).getId() / 100 % 5;
                int value = dp.getCard(i).getId() % 100;
                if (sym == starting_card_ && highest_card_sym != trump_) {
                    if (value > highest_card_value) {
                        highest_card_sym = sym;
                        highest_card_value = value;
                    }
                }
                if (sym == trump_) {
                    if (highest_card_sym == trump_) {
                        if (value > highest_card_value) {
                            highest_card_sym = sym;
                            highest_card_value = value;
                        }
                    }
                    else if(highest_card_sym != trump_) {
                        highest_card_sym = sym;
                        highest_card_value = value;
                    }
                }
            }
        }
        return highest_card_sym * 100 + highest_card_value;
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

    public int getTrumph() { return trump_; }

    public int getTrumphPlayerId() { return  trumph_player_id_; }

    public void setTrumphPlayerId(int trumph_player_id) { trumph_player_id_ = trumph_player_id; }

    public void setTrumphsToMake(int trumphs) {
        trumphs_to_make_ = trumphs;
    }

    public void setStartingCard(int starting_card_) {
        this.starting_card_ = starting_card_;
    }

    public int getStartingCard() {
        return starting_card_;
    }

    public int getTrumphsToMake() { return trumphs_to_make_; }

    public void setTrumph(int trump){
        trump_ = trump;
    }

    public int getLastTrickId() {
        return last_trick_id_;
    }

    public void setLastTrickId(int last_trick_id) {
        last_trick_id_ = last_trick_id;
    }
}
