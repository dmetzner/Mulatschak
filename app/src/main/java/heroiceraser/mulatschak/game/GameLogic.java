package heroiceraser.mulatschak.game;


import heroiceraser.mulatschak.game.BaseObjects.Card;
import heroiceraser.mulatschak.game.BaseObjects.CardStack;
import heroiceraser.mulatschak.game.BaseObjects.DiscardPile;
import heroiceraser.mulatschak.game.BaseObjects.MulatschakDeck;


//--------------------------------------------------------------------------------------------------
//  Game Logic
//
public class GameLogic {

    //----------------------------------------------------------------------------------------------
    //  Constants
    //
    public final static int DEFAULT_PLAYER_START_LIVES = 21;
    public final static int MAX_CARDS_PER_HAND = 5;

    public final static int DIFFICULTY_EASY = 1;
    public final static int DIFFICULTY_NORMAL = 2;
    public final static int DIFFICULTY_HARD = 3;


    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //

    private int start_lives_;
    private int difficulty_;

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
    private int tricks_to_make_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameLogic() {
        multiplier_ = GameController.NOT_SET;
        start_lives_ = GameController.NOT_SET;

        game_over_ = false;
        mulatschak_round_ = false;

        dealer_ = GameController.NOT_SET;
        starting_player_ = GameController.NOT_SET;
        turn_ = GameController.NOT_SET;

        starting_card_symbol_ = GameController.NOT_SET;
        trump_ = GameController.NOT_SET;
        trump_player_id_ = GameController.NOT_SET;
        tricks_to_make_ = GameController.NOT_SET;

    }

    public void init(int lives, int difficulty) {
        multiplier_ = 1;
        start_lives_ = lives;
        game_over_ = false;
        mulatschak_round_ = false;
        setDifficulty(difficulty);
    }



    void moveDealer(int players) {
        dealer_++;
        if (dealer_ >= players) {
            dealer_ = 0;
        }
    }

    void turnToNextPlayer(int players) {
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
        if (multiplier_ > 32) {
            multiplier_ = 32;
        }
    }

    public void resetMultiplier() {
        this.multiplier_ = 1;
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

    public boolean isDiscardPileEmpty(DiscardPile discard_pile) {
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
        int highest_card_owner_pos = GameController.NOT_SET;

        for (int i = 0; i < dp.SIZE; i++) {
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
                    highest_card_owner_pos = i;
                }
            }
            if (dp_card_sym == trump_) {
                if (highest_card_sym == trump_ && dp_card_value > highest_card_value) {
                    highest_card_sym = dp_card_sym;
                    highest_card_value = dp_card_value;
                    highest_card_owner_pos = i;
                } else if (highest_card_sym != trump_) {
                    highest_card_sym = dp_card_sym;
                    highest_card_value = dp_card_value;
                    highest_card_owner_pos = i;
                }
            }

        }

        round_winner_id_ = controller.getPlayerByPosition(highest_card_owner_pos).getId();
    }


    public boolean isCardTrump(Card card) {
        int card_suit = MulatschakDeck.getCardSuit(card);
        return (card_suit == trump_ || card_suit == MulatschakDeck.WELI);
    }

    public static int getCardValue(Card card) {
        return MulatschakDeck.getCardValue(card);
    }

    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public int getDealer() { return dealer_; }

    void setDealer(int dealer) { dealer_ = dealer; }

    public void setTurn(int turn) { turn_ = turn; }

    public int getTurn() { return turn_; }

    public int getTrump() { return trump_; }

    public int getTrumpPlayerId() { return  trump_player_id_; }

    public void setTrumpPlayerId(int trumph_player_id) { trump_player_id_ = trumph_player_id; }

    public void setTricksToMake(int tricks) {
        tricks_to_make_ = tricks;
    }

    public void setStartingCard(int starting_card_symbol_) {
        this.starting_card_symbol_ = starting_card_symbol_;
    }

    public int getStartingCard() {
        return starting_card_symbol_;
    }

    public int getTricksToMake() { return tricks_to_make_; }

    public void setTrump(int trump){
        trump_ = trump;
    }

    public int getMultiplier() {
        return multiplier_;
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

    public int getStartLives() {
        return start_lives_;
    }

    public void setStartLives(int start_lives) {
        this.start_lives_ = start_lives;
    }

    public int getDifficulty() {
        return difficulty_;
    }

    public void setDifficulty(int difficulty) {
        switch (difficulty) {

            case DIFFICULTY_EASY:
                difficulty_ = DIFFICULTY_EASY;
                break;

            case DIFFICULTY_NORMAL:
                difficulty_ = DIFFICULTY_NORMAL;
                break;

            case DIFFICULTY_HARD:
                difficulty_ = DIFFICULTY_HARD;
                break;

            default:
                difficulty_ = DIFFICULTY_NORMAL;
                break;
        }
    }

}
