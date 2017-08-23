package heroiceraser.mulatschak.game;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Daniel Metzner on 21.08.2017.
 */

public class EnemyLogic {
    public EnemyLogic() {

    }

    public void playCard(Player player, DiscardPile discard_pile) {
        Random random_generator = new Random();
        int random_number = random_generator.nextInt(player.getAmountOfCardsInHand());
        Card card = player.getHand().getCardAt(random_number);
        player.getHand().getCardStack().remove(random_number);
        discard_pile.setCard(player.getPosition(), card);
    }

    public void trickBids(Player player, GameController controller) {

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

    public void chooseTrumph(Player player, GameLogic logic, GameView view) {
        // ToDo: put in some fancy logic here
        logic.setTrumph(0);
        // DEBUG ////////////////////////////////////////////////////////////////////////////////////////
        CharSequence text = "player: " + player.getId() + " choose trumph " + 0;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(view.getContext(), text, duration);
        toast.show();//////////////////////////////////////////////////////////////////////////////////
    }

}
