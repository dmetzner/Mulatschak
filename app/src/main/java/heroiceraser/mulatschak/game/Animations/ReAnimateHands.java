package heroiceraser.mulatschak.game.Animations;

import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.Player;

/**
 * Created by Daniel Metzner on 18.08.2017.
 */

public class ReAnimateHands {

    public ReAnimateHands() {

    }

    public void redrawHands(GameLayout layout, Player player) {
        switch (player.getId()) {
            case 0:
                for (int i = 0; i < player.getAmountOfCardsInHand(); i++) {
                    player.getHand().getCardAt(i).setFixedPosition(layout.getHandBottom().x +
                            layout.getCardWidth() * i +  (int) (layout.getCardWidth() *
                            ((GameLogic.MAX_CARDS_PER_HAND - player.getAmountOfCardsInHand()) / 2.0)),
                            layout.getHandBottom().y);
                    player.getHand().getCardAt(i).setPosition(
                            player.getHand().getCardAt(i).getFixedPosition());
                }
        }
    }
}
