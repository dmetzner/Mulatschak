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
    public final static int MAX_PLAYERS = 4;
    public final static int MAX_CARDS_PER_HAND = 5;
    public final static int START_LIVES = 21;

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private int multiplier_;
    private boolean game_over_;
    private boolean mulatschak_round_;

    private int dealer_;
    private int turn_;
    private int starting_player_;
    private int round_winner_id_;

    private int starting_card_symbol_;
    private int trump_;

    private int trump_player_id_;
    private int trumps_to_make_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameLogic() {
        multiplier_ = 1;
        game_over_ = false;
        mulatschak_round_ = false;

        dealer_ = GameController.NOT_SET;
        starting_player_ = GameController.NOT_SET;
        turn_ = GameController.NOT_SET;

        starting_card_symbol_ = GameController.NOT_SET;
        trump_ = GameController.NOT_SET;
        trump_player_id_ = GameController.NOT_SET;
        trumps_to_make_ = GameController.NOT_SET;

    }

    public void moveDealer(int players) {
        dealer_++;
        if (dealer_ >= players) {
            dealer_ = 0;
        }
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

    public void raiseMultiplier() {
        this.multiplier_ *= 2;
    }

    public boolean isAValidCardPlay(Card card_to_play, CardStack hand, DiscardPile dp) {

        int card_to_play_symbol = (card_to_play.getId() / 100) % 5;
        if (card_to_play_symbol == MulatschakDeck.WELI) {
            card_to_play_symbol = trump_;
        }

        if (isDiscardPileEmpty(dp)) { // first card
            setStartingCard(card_to_play_symbol);
            return true;
        }

        int highestCard = getHighestCardOnDiscardPile(dp);
        int h_sym = highestCard / 100 % 5;
        int h_value = highestCard % 100;

        boolean has_a_starting_card = false;
        boolean has_a_trump = false;
        for (int i = 0; i < hand.getCardStack().size(); i++) {
            if (hand.getCardAt(i).getId() / 100 % 5 == starting_card_symbol_) {
                has_a_starting_card = true;
            }
            if (hand.getCardAt(i).getId() / 100 % 5 == trump_) {
                has_a_trump = true;
            }
            if (hand.getCardAt(i).getId() / 100 % 5 == MulatschakDeck.WELI) {
                has_a_trump = true;
            }
        }
        if (has_a_starting_card && card_to_play_symbol != starting_card_symbol_) {
            return false;
        }
        if (!has_a_starting_card && has_a_trump && card_to_play_symbol != trump_) {
            return false;
        }

        if (h_sym == starting_card_symbol_ && card_to_play_symbol == starting_card_symbol_ && card_to_play.getId() % 100 <= h_value) {
            for (int i = 0; i < hand.getCardStack().size(); i++) {
                if (hand.getCardAt(i).getId() / 100 % 5 == starting_card_symbol_ &&
                        hand.getCardAt(i).getId() % 100 > h_value) {
                    return false;
                }
            }
        }

        if (h_sym == trump_ && card_to_play_symbol == trump_ && card_to_play.getId() % 100 <= h_value) {
            for (int i = 0; i < hand.getCardStack().size(); i++) {
                if (hand.getCardAt(i).getId() / 100 % 5 == trump_ &&
                        hand.getCardAt(i).getId() % 100 > h_value) {
                    return false;
                }
                if (hand.getCardAt(i).getId() / 100 % 5 == MulatschakDeck.WELI &&
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
        int highest_card_sym = GameController.NOT_SET;
        int highest_card_value = GameController.NOT_SET;

        for (int i = 0; i < 4; i++) {
            if (dp.getCard(i) == null) {
                continue;
            }

            int dp_card_sym = dp.getCard(i).getId() / 100 % 5;
            int dp_card_value = dp.getCard(i).getId() % 100;
            if (dp_card_sym == MulatschakDeck.WELI) {
                dp_card_sym = trump_;
            }
            if (dp_card_sym == starting_card_symbol_ && highest_card_sym != trump_) {
                if (dp_card_value > highest_card_value) {
                    highest_card_sym = dp_card_sym;
                    highest_card_value = dp_card_value;
                }
            }
            if (dp_card_sym == trump_) {
                if (highest_card_sym == trump_ && dp_card_value > highest_card_value) {
                        highest_card_sym = dp_card_sym;
                        highest_card_value = dp_card_value;
                }
                else if(highest_card_sym != trump_) {
                    highest_card_sym = dp_card_sym;
                    highest_card_value = dp_card_value;
                }
            }

        }
        return highest_card_sym * 100 + highest_card_value;
    }


    public void chooseCardRoundWinner(GameController controller, DiscardPile dp) {
        int highest_card_sym = GameController.NOT_SET;
        int highest_card_value = GameController.NOT_SET;
        int highest_card_owner = GameController.NOT_SET;

        for (int i = 0; i < 4; i++) {
            if (dp.getCard(i) == null) {
                continue;
            }

            int dp_card_sym = dp.getCard(i).getId() / 100 % 5;
            int dp_card_value = dp.getCard(i).getId() % 100;
            if (dp_card_sym == MulatschakDeck.WELI) {
                dp_card_sym = trump_;
                highest_card_owner = i;
            }
            if (dp_card_sym == starting_card_symbol_ && highest_card_sym != trump_) {
                if (dp_card_value > highest_card_value) {
                    highest_card_sym = dp_card_sym;
                    highest_card_value = dp_card_value;
                    highest_card_owner = i;
                }
            }
            if (dp_card_sym == trump_) {
                if (highest_card_sym == trump_ && dp_card_value > highest_card_value) {
                        highest_card_sym = dp_card_sym;
                        highest_card_value = dp_card_value;
                        highest_card_owner = i;
                }
                else if(highest_card_sym != trump_) {
                    highest_card_sym = dp_card_sym;
                    highest_card_value = dp_card_value;
                    highest_card_owner = i;
                }
            }

        }

        round_winner_id_ = highest_card_owner;

        for (int i = 0; i < 4; i++) {
            if (dp.getCard(i) != null) {
                controller.getPlayerById(highest_card_owner).getTricks().addCard(dp.getCard(i));
            }
        }

        turn_ = highest_card_owner;
        starting_player_ = highest_card_owner;

    }

    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public int getDealer() { return dealer_; }

    public void setDealer(int dealer) { dealer_ = dealer; }

    public void setTurn(int turn) { turn_ = turn; }

    public int getTurn() { return turn_; }

    public int getTrump() { return trump_; }

    public int getTrumphPlayerId() { return  trump_player_id_; }

    public void setTrumphPlayerId(int trumph_player_id) { trump_player_id_ = trumph_player_id; }

    public void setTrumphsToMake(int trumphs) {
        trumps_to_make_ = trumphs;
    }

    public void setStartingCard(int starting_card_symbol_) {
        this.starting_card_symbol_ = starting_card_symbol_;
    }

    public int getStartingCard() {
        return starting_card_symbol_;
    }

    public int getTrumphsToMake() { return trumps_to_make_; }

    public void setTrump(int trump){
        trump_ = trump;
    }

    public int getMultiplier() {
        return multiplier_;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier_ = multiplier;
    }

    public int getStartingPlayer() {
        return starting_player_;
    }

    public void setStartingPlayer(int id) {
        starting_player_ = id;
    }

    public boolean isGameOver() {
        return game_over_;
    }

    public void setGameOver(boolean game_over) {
        this.game_over_ = game_over;
    }

    public boolean isMulatschakRound() {
        return mulatschak_round_;
    }

    public void setMulatschakRound(boolean bool) {
        this.mulatschak_round_ = bool;
    }

    public int getRoundWinnerId() {
        return round_winner_id_;
    }
}
