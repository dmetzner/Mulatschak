package heroiceraser.mulatschak.game;

import android.widget.Toast;

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

    public void sayStiche(Player player, GameView view) {
        player.setTrumphsToMake(1); // ToDo: put in some fancy logic here
        // DEBUG ////////////////////////////////////////////////////////////////////////////////////////
        CharSequence text = "player: " + player.getId() + " tries " + 1;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(view.getContext(), text, duration);
        toast.show();//////////////////////////////////////////////////////////////////////////////////
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
