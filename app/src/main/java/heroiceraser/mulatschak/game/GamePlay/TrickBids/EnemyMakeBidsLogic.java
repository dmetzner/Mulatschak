package heroiceraser.mulatschak.game.GamePlay.TrickBids;

import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;
import heroiceraser.mulatschak.game.GameController;


//----------------------------------------------------------------------------------------------
//  Enemy MakeTrickBidsLogic
//
class EnemyMakeBidsLogic {

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    EnemyMakeBidsLogic() {
    }


    //----------------------------------------------------------------------------------------------
    //  makeTrickBids
    //
    void makeTrickBids(MyPlayer myPlayer, GameController controller) {

        // ToDo: put in some fancy logic here
        int[] cards_per_trump = new int[5];
        for (int i = 0; i < myPlayer.getAmountOfCardsInHand(); i++) {
            int trump = (myPlayer.getHand().getCardAt(i).getId() / 100) % 5;
            cards_per_trump[trump]++;
        }

        int max = 0;
        for (int i : cards_per_trump) {
            if (i > max) {
                max = i;
            }
        }

        if (max > 0) {
            max--;
        }

        controller.getGamePlay().getTrickBids().setNewMaxTrumps(max, myPlayer.getId(), controller);
    }
}
