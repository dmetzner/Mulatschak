package heroiceraser.mulatschak.game.GamePlay.ChooseTrump;

import heroiceraser.mulatschak.game.BaseObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;


//----------------------------------------------------------------------------------------------
//  enemy choose trump class
//
class EnemyChooseTrumpLogic {

    //----------------------------------------------------------------------------------------------
    //  chooseTrump
    //
    void chooseTrump(MyPlayer myPlayer, GameLogic logic) {
        // ToDo: put in some fancy logic here

        int[] cards_per_suit = new int[MulatschakDeck.CARD_SUITS];

        // init array with zeros;
        for (int i = 0; i < cards_per_suit.length; i++) {
            cards_per_suit[i] = 0;
        }

        // count cards per suit
        for (int i = 0; i < myPlayer.getAmountOfCardsInHand(); i++) {
            int suit = MulatschakDeck.getCardSuit(myPlayer.getHand().getCardAt(i));
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
    }
}
