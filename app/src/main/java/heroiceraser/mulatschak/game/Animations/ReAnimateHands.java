package heroiceraser.mulatschak.game.Animations;

import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.MyPlayer;

/**
 * Created by Daniel Metzner on 18.08.2017.
 */

public class ReAnimateHands {

    public ReAnimateHands() {

    }

    public void redrawHands(GameLayout layout, MyPlayer myPlayer) {
        switch (myPlayer.getId()) {
            case 0:
                for (int i = 0; i < myPlayer.getAmountOfCardsInHand(); i++) {
                    myPlayer.getHand().getCardAt(i).setFixedPosition(layout.getHandBottom().x +
                            layout.getCardWidth() * i +  (int) (layout.getCardWidth() *
                            ((GameLogic.MAX_CARDS_PER_HAND - myPlayer.getAmountOfCardsInHand()) / 2.0)),
                            layout.getHandBottom().y);
                    myPlayer.getHand().getCardAt(i).setPosition(
                            myPlayer.getHand().getCardAt(i).getFixedPosition());
                }
        }
    }
}
