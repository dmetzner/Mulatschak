package heroiceraser.mulatschak.game.Animations;

import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.Player;

/**
 * Created by Daniel Metzner on 18.08.2017.
 */

public class ReAnimateHands {

    private ReAnimateHands() {

    }

    public static void redrawHands(GameLayout layout, Player player) {
        switch (player.getId()) {
            case 0:
                layout.setHandBottom(layout.getHandBottom().getX() + layout.getCardWidth() / 2,
                        layout.getHandBottom().getY());
                for (int i = 0; i < player.getAmountOfCardsInHand(); i++) {
                    player.getHand().getCardAt(i).setFixedPosition(
                            layout.getHandBottom().getX() + layout.getCardWidth() * i,
                            layout.getHandBottom().getY());
                    player.getHand().getCardAt(i).setPosition(
                            layout.getHandBottom().getX() + layout.getCardWidth() * i,
                            layout.getHandBottom().getY());
                }
        }
    }
}
