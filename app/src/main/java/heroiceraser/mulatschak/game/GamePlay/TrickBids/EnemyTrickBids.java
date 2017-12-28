package heroiceraser.mulatschak.game.GamePlay.TrickBids;

import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.Player;



public class EnemyTrickBids {

    public EnemyTrickBids() {

    }

    public void init() {

    }

    //----------------------------------------------------------------------------------------------
    //  TrickBids
    //
    public void makeTrickBids(Player player, GameController controller) {

        // ToDo: put in some fancy logic here

        int[] cards_per_trump = new int[5];
        for (int i = 0; i < player.getAmountOfCardsInHand(); i++) {
            int trump = (player.getHand().getCardAt(i).getId() / 100) % 5;
            cards_per_trump[trump]++;
        }

        int max = 0;
        for (int i = 0; i < cards_per_trump.length; i++){
            if (cards_per_trump[i] > max) {
                max = cards_per_trump[i];
            }
        }

        if (max > 0) {
            max--;
        }

        controller.setNewMaxTrumps(max, player.getId());
    }
}
