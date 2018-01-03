package heroiceraser.mulatschak.game.GamePlay.DealCards;

import heroiceraser.mulatschak.game.DrawableObjects.CardStack;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.MyPlayer;


//----------------------------------------------------------------------------------------------
//  DealCards Class
//                      ToDo Explain me ;)
//
public class DealCards {

    //----------------------------------------------------------------------------------------------
    //  MemberVariables
    //
    private DealingAnimation dealing_animation_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public DealCards() {
        dealing_animation_ = new DealingAnimation();
    }

    //----------------------------------------------------------------------------------------------
    //  dealCards
    //                  -> deals cards to every player
    //                  -> starts an dealing animation
    //
    public void dealCards(GameController controller) {
        deal(controller);
        dealing_animation_.start(controller);
    }


    //----------------------------------------------------------------------------------------------
    //  deal
    //              -> add the top card of the the deck to a player,
    //                  till all players have a full hand
    //
    private void deal(GameController controller) {
        for (int hand_card = 0; hand_card < GameLogic.MAX_CARDS_PER_HAND; hand_card++) {
            for (MyPlayer player : controller.getPlayerList()) {
                takeCardFromDeck(player, controller.getDeck());
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    //  takeCardFromDeck
    //                               -> player take a card from deck
    //                               -> return false if deck is empty (take card from deck)
    //
    public boolean takeCardFromDeck(MyPlayer player, MulatschakDeck deck){

        if (deck.getCardStack().isEmpty()) {
            return false;
        }

        // remove a card from the deck and push it to the players hand
        CardStack player_hand =  player.getHand();
        player_hand.addCard(deck.getCardAt(0));
        deck.getCardStack().remove(0);
        return true;
    }


    //----------------------------------------------------------------------------------------------
    //  Getter
    //
    public DealingAnimation getDealingAnimation() {
        return dealing_animation_;
    }
}
