package heroiceraser.mulatschak.game;

import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Random;

import heroiceraser.mulatschak.game.DrawableObjects.Card;
import heroiceraser.mulatschak.game.DrawableObjects.CardStack;
import heroiceraser.mulatschak.game.DrawableObjects.DiscardPile;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;

/**
 * Created by Daniel Metzner on 21.08.2017.
 */

public class EnemyLogic {
    public EnemyLogic() {

    }

    //----------------------------------------------------------------------------------------------
    //  TrickBids
    //
    public void makeTrickBids(Player player, GameController controller) {

        // ToDo: put in some fancy logic here

        int[] cards_per_trumph = new int[5];
        for (int i = 0; i < player.getAmountOfCardsInHand(); i++) {
            int trumph = (player.getHand().getCardAt(i).getId() / 100) % 5;
            cards_per_trumph[trumph]++;
        }

        int max = 0;
        for (int i = 0; i < cards_per_trumph.length; i++){
            if (cards_per_trumph[i] > max) {
                max = cards_per_trumph[i];
            }
        }
        controller.setNewMaxTrumphs(max, player.getId());
    }


    //----------------------------------------------------------------------------------------------
    //  chooseTrump
    //
    public void chooseTrump(Player player, GameLogic logic, GameView view) {
        // ToDo: put in some fancy logic here

        int[] cards_per_suit = new int[MulatschakDeck.CARD_SUITS];

        // init array with zeros;
        for (int i = 0; i < cards_per_suit.length; i++) {
            cards_per_suit[i] = 0;
        }

        // count cards per suit
        for (int i = 0; i < player.getAmountOfCardsInHand(); i++) {
            int suit = MulatschakDeck.getCardSuit(player.getHand().getCardAt(i));
            cards_per_suit[suit]++;
        }

        int trump_to_set = 0;
        for (int i = 0; i < cards_per_suit.length; i++) {
            if (cards_per_suit[i] > trump_to_set) {
                trump_to_set = i;
            }
            if (cards_per_suit[i] == trump_to_set) {
                // ToDo choose Better cards
            }
        }

        logic.setTrump(trump_to_set);

        // DEBUG ////////////////////////////////////////////////////////////////////////////////////////
        CharSequence text = "player: " + player.getId() + " choose trumph " + 1;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(view.getContext(), text, duration);
        toast.show();//////////////////////////////////////////////////////////////////////////////////
    }


    //----------------------------------------------------------------------------------------------
    //  Play Card
    //
    public void playCard(GameLogic logic, Player player, DiscardPile discard_pile) {
        Random random_generator = new Random();
        boolean valid = false;
        int random_number = -1;
        while (!valid) {
            random_number = random_generator.nextInt(player.getAmountOfCardsInHand());
            Card card = player.getHand().getCardAt(random_number);
            CardStack hand = player.getHand();
            valid = logic.isAValidCardPlay(card, hand, discard_pile);
        }

        Card card = player.getHand().getCardAt(random_number);
        player.getHand().getCardStack().remove(random_number);
        discard_pile.setCard(player.getPosition(), card);

    }

}
