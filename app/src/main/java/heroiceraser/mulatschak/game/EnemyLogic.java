package heroiceraser.mulatschak.game;

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

}
