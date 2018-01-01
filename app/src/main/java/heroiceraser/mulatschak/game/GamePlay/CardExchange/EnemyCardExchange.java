package heroiceraser.mulatschak.game.GamePlay.CardExchange;

import android.graphics.Canvas;
import java.util.List;
import heroiceraser.mulatschak.game.DrawableObjects.Card;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.MyPlayer;

//----------------------------------------------------------------------------------------------
//  EnemyCardExchange:
//                 enemy player card exchange process
//
//
// Exchange Animation:
//    -> move each card a bit to the center, spins it, and moves it back
//
public class EnemyCardExchange {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private boolean animation_running_;

    private MyPlayer myPlayer_;     // animation for which player?

    private EnemyCardExchangeAnimation card_exchange_animation_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public EnemyCardExchange() {
        card_exchange_animation_ = new EnemyCardExchangeAnimation();
    }


    //----------------------------------------------------------------------------------------------
    // exchangeCards:
    //
    public void exchangeCard(MyPlayer myPlayer, GameController controller) {

        animation_running_ = true;

        // based on difficulty introduce some randomness
        int randomness;
        switch (controller.getLogic().getDifficulty()) {

            case GameLogic.DIFFICULTY_EASY:
                randomness = 100;//%
                break;

            case GameLogic.DIFFICULTY_NORMAL:
                randomness = 20;//%
                break;

            case GameLogic.DIFFICULTY_HARD:
                randomness = 1;//%
                break;

            default:
                randomness = 20;//%
                break;
        }

        myPlayer_ = myPlayer;
        card_exchange_animation_.init(controller, myPlayer);

        // moves weak cards based on their trump/value to the container
        int weak_border = 13;
        moveWeakCards(myPlayer.getHand().getCardStack(), card_exchange_animation_.getExchangedCards(),
                controller.getLogic(), weak_border, randomness);

        // if deck has to less cards to draw
        //      -> give back the best cards from move cards to the players hand
        handleToLessCardsInDeck(card_exchange_animation_.getExchangedCards(), myPlayer,
                controller.getDeck().getCardStack().size());

        // no cards to change? -> nothing to do
        if (card_exchange_animation_.getExchangedCards().size() <= 0) {
            endCardExchange(controller);
            return;
        }

        // start animation -> enables canvas thread
        controller.getView().enableUpdateCanvasThread();
        card_exchange_animation_.startAnimation();

        // --> gets continued by draw method
    }

    //----------------------------------------------------------------------------------------------
    //  draw:
    //        draws the spinning animations, or the correct exchange button (with a helptext)
    //
    public void draw(Canvas canvas, GameController controller) {

        // spinning animation
        if (card_exchange_animation_.isAnimationRunning()) {
            card_exchange_animation_.draw(canvas, myPlayer_);
            card_exchange_animation_.recalculateParameters(controller);
        }

        // continueAfter the spinning animation
        if (card_exchange_animation_.hasAnimationStopped()) {
            endCardExchange(controller);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  returns a random percentage
    //
    private int getRandomPercent() {
        return (int) (Math.random() * 100);
    }


    //----------------------------------------------------------------------------------------------
    //  move weak card from hand to exchanged cards
    //
    private void moveWeakCards(List<Card> hand, List<Card> exchanged_cards, GameLogic logic,
                               int weak_border, int randomness) {
        for (int i = 0; i < hand.size(); i++) {

            // built in random stupidity, easier modes have a higher chance of doing something stupid
            if (getRandomPercent() <= randomness) {
                // we keep it random
                if (getRandomPercent() < 50) {
                    // don't trade the card, even if it may sucks
                    continue;
                }
                if (getRandomPercent() < 25) {
                    // just trade a card without a reason
                    exchanged_cards.add(hand.get(i));
                    hand.remove(i);
                    i--;
                    continue;
                }
            }

            // check if the card should get sorted out
            if (!logic.isCardTrump(hand.get(i)) &&
                    GameLogic.getCardValue(hand.get(i)) < weak_border) {

                exchanged_cards.add(hand.get(i));
                hand.remove(i);
                i--;
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    // if deck has to less cards to draw
    //      -> give back the best cards from cards to remove list to the players hand
    private void handleToLessCardsInDeck(List<Card> cards_to_remove, MyPlayer myPlayer, int deck_size) {

        int cards_to_give_back = cards_to_remove.size() - deck_size;

        for (int i = 0; i < cards_to_give_back; i++) {
            int highest_id = -1;
            int highest_val = -1;
            for (int j = 0; j < cards_to_remove.size(); j++) {
                int card_value = GameLogic.getCardValue(cards_to_remove.get(i));
                if (GameLogic.getCardValue(cards_to_remove.get(j)) < highest_val) {
                    highest_id = j;
                    highest_val = card_value;
                }
            }
            if (highest_id != -1) {
                myPlayer.getHand().addCard(cards_to_remove.get(highest_id));
                cards_to_remove.remove(highest_id);
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    //  endCard exchange
    //
    private void endCardExchange(GameController controller) {
        animation_running_ = false;
        card_exchange_animation_.reset();
        controller.makeCardExchange();
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public boolean isAnimationRunning() {
        return animation_running_;
    }
}