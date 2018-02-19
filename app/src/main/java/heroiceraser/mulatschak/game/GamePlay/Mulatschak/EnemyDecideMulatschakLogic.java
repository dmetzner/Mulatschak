package heroiceraser.mulatschak.game.GamePlay.Mulatschak;

import heroiceraser.mulatschak.game.BaseObjects.Card;
import heroiceraser.mulatschak.game.BaseObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLogic;

//--------------------------------------------------------------------------------------------------
//  Enemy Say Mulatschak Logic
//                              -> handles if an Enemy wants to try a Mulatschak
//                                 or just passes the turn to the next player
//
class EnemyDecideMulatschakLogic {

    //----------------------------------------------------------------------------------------------
    //  decideMulatschak
    //                      -> if min 4 cards of a trump, and at 3 high cards
    //
    boolean decideMulatschak(GameController controller) {

        MyPlayer player = controller.getPlayerById(controller.getLogic().getTurn());

        // something went wrong!
        if (player == null || player.getHand() == null || player.getHand().getCardStack() == null
                || player.getHand().getCardStack().size() < 1) {
            return false;
        }

        int highCards = 0;
        int hearts = 0;
        int diamonds = 0;
        int spades = 0;
        int clubs = 0;
        for (Card card : player.getHand().getCardStack()) {
           int cardValue = MulatschakDeck.getCardValue(card);
           if (cardValue > MulatschakDeck.LAST_CARD - 2) {
               highCards++;
           }
           int cardSuit = MulatschakDeck.getCardSuit(card);
           switch (cardSuit) {
               case MulatschakDeck.HEART:
                   hearts++;
                   break;
               case MulatschakDeck.DIAMOND:
                   diamonds++;
                   break;
               case MulatschakDeck.SPADE:
                   spades++;
                   break;
               case MulatschakDeck.CLUB:
                   clubs++;
                   break;
               case MulatschakDeck.WELI:
                   hearts++;
                   diamonds++;
                   clubs++;
                   spades++;
                   break;
           }
        }

        // to less good cards
        if (highCards < 3) {
            return false;
        }

        // all cards should be the same suit to return true
        return !(hearts < GameLogic.MAX_CARDS_PER_HAND &&
                spades < GameLogic.MAX_CARDS_PER_HAND &&
                clubs < GameLogic.MAX_CARDS_PER_HAND &&
                diamonds < GameLogic.MAX_CARDS_PER_HAND);
    }
}
