package heroiceraser.mulatschak.game;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.game.DrawableObjects.Card;

/**
 * Created by Daniel Metzner on 13.12.2017.
 */

public class CardExchangeLogic {

    public CardExchangeLogic() {
    }

    public void exchangeCard(Player player, GameController controller) {

        // container for the cards that should be exchanged
        List<Card> move_cards = new ArrayList<>();

        int randomness;
        switch (controller.getLogic().getDifficulty()) {

            case GameLogic.DIFFICULTY_EASY:
                randomness = 100;//%
                break;

            case GameLogic.DIFFICULTY_NORMAL:
                randomness = 20;//%
                break;

            case GameLogic.DIFFICULTY_HARD:
                randomness = 1;//%
                break;

            default:
                randomness = 20;//%
                break;
        }

        // moves weak cards based on their trump/value to the container
        int weak_border = 13;
        moveWeakCards(player.getHand().getCardStack(), move_cards,
                controller.getLogic(), weak_border, randomness);

        // if deck has to less cards to draw
        //      -> give back the best cards from move cards to the players hand
        handleToLessCardsInDeck(move_cards, player, controller.getDeck().getCardStack().size());

        // adding new cards from the deck to the hand
        drawNewCards(move_cards, player, controller);

        Log.d("Card Exchange", "Player " + player.getId() + " : "
                + move_cards.size() + " cards");

        // add old cards to trash
        controller.moveCardsToTrash(move_cards);
    }


    private int getRandomPercent() {
        return (int) (Math.random() * 100);
    }

    private void moveWeakCards(List<Card> hand, List<Card> exchanged_cards, GameLogic logic,
                               int weak_border, int randomness) {
        for (int i = 0; i < hand.size(); i++) {

            // built in random stupidity, easier modes have a higher chance of doing something stupid
            if (getRandomPercent() <= randomness) {
                // we keep it random
                if (getRandomPercent() < 50) {
                    // don't trade the card, even if it may sucks
                    continue;
                }
                if (getRandomPercent() < 25) {
                    // just trade a card without a reason
                    exchanged_cards.add(hand.get(i));
                    hand.remove(i);
                    i--;
                    continue;
                }
            }

            // check if the card should get sorted out
            if (!logic.isCardTrump(hand.get(i)) &&
                    GameLogic.getCardValue(hand.get(i)) < weak_border) {

                exchanged_cards.add(hand.get(i));
                hand.remove(i);
                i--;
            }
        }
    }


    private void debugMove(List<Card> cards) {
        for (Card c : cards) {
            Log.d("debugMove", " " + c.getId());
        }

    }

    // if deck has to less cards to draw
    //      -> give back the best cards from cards to remove list to the players hand
    private void handleToLessCardsInDeck(List<Card> cards_to_remove, Player player, int deck_size) {

        int cards_to_give_back = cards_to_remove.size() - deck_size;

        for (int i = 0; i < cards_to_give_back; i++) {
            int highest_id = -1;
            int highest_val = -1;
            for (int j = 0; j < cards_to_remove.size(); j++) {
                int card_value = GameLogic.getCardValue(cards_to_remove.get(i));
                if (GameLogic.getCardValue(cards_to_remove.get(j)) < highest_val) {
                    highest_id = j;
                    highest_val = card_value;
                }
            }
            if (highest_id != -1) {
                player.getHand().addCard(cards_to_remove.get(highest_id));
                cards_to_remove.remove(highest_id);
            }
        }
    }

    private void drawNewCards(List<Card> cards_to_remove, Player player, GameController controller) {
        for (int i = 0; i < cards_to_remove.size(); i++) {

            // drawing may not work, caused by an empty deck, but should never happen
            if (!controller.takeCardFromDeck(player.getId(), controller.getDeck())) {
                player.getHand().addCard(cards_to_remove.get(0));
                cards_to_remove.remove(0);
            } else {
                // added card has to be at the last position in the hand
                Card card = player.getHand().getCardAt(player.getAmountOfCardsInHand() - 1);
                card.setPosition(cards_to_remove.get(i).getFixedPosition());
                card.setFixedPosition(card.getPosition());
            }
        }
    }

}