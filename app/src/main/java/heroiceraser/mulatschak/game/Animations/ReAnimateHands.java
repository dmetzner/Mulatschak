package heroiceraser.mulatschak.game.Animations;

import heroiceraser.mulatschak.game.GameLayout;
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
                layout.setHandBottom(layout.getHandBottom().x + layout.getCardWidth() / 2,
                        layout.getHandBottom().y);
                for (int i = 0; i < player.getAmountOfCardsInHand(); i++) {
                    player.getHand().getCardAt(i).setFixedPosition(
                            layout.getHandBottom().x + layout.getCardWidth() * i,
                            layout.getHandBottom().y);
                    player.getHand().getCardAt(i).setPosition(
                            layout.getHandBottom().x + layout.getCardWidth() * i,
                            layout.getHandBottom().y);
                }
        }
    }
}
