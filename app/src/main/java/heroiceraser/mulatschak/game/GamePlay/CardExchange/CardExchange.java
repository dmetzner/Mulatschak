package heroiceraser.mulatschak.game.GamePlay.CardExchange;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;

import heroiceraser.mulatschak.game.BaseObjects.Card;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GameView;


//--------------------------------------------------------------------------------------------------
//  Card Exchange Class
//
public class CardExchange {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private CardExchangeLogic card_exchange_logic_;
    private EnemyCardExchangeLogic enemy_card_exchange_logic_;
    private ArrayList<Integer> cardHandIds;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public CardExchange() {
        card_exchange_logic_ = new CardExchangeLogic();
        enemy_card_exchange_logic_ = new EnemyCardExchangeLogic();
    }


    //----------------------------------------------------------------------------------------------
    //  Init
    //
    public void init(GameView view) {
        card_exchange_logic_.init(view);
        // enemy_card_exchange_logic_.init(view); Not Needed
    }


    //----------------------------------------------------------------------------------------------
    //  makeCardExchange
    //                      -> handles the card exchange process
    //
    void makeCardExchange(GameController controller) {
        makeCardExchange(false, controller); // not first call
    }

    public void makeCardExchange(boolean first_call, GameController controller) {
        GameLogic logic = controller.getLogic();

        if (!first_call) {
            controller.turnToNextPlayer(true);
        }

        if (!first_call && logic.getTurn() == logic.getStartingPlayer()) {
            controller.continueAfterCardExchange();
            return;   // stops the recursion after all players had a chance to exchange their cards
        }

        if (controller.getPlayerById(logic.getTurn()).getMissATurn()) {
            makeCardExchange(controller);
            return;
        }

        if (logic.getTurn() == 0) {
            // handle the card exchange buttons (-> to less cards in deck to exchange to much cards)
            card_exchange_logic_.handleExchangeButtons(controller.getDeck().getCardStack().size());
            card_exchange_logic_.startAnimation();
            //controller.getView().disableUpdateCanvasThread();
        } else if (logic.getTurn() != 0) {
            //controller.getView().enableUpdateCanvasThread();
            // single player
            if (controller.getPlayerById(logic.getTurn()).isEnemyLogic()) {
                handleEnemyAction(controller);
            }
        }
    }

    private void handleEnemyAction(final GameController controller) {
        enemy_card_exchange_logic_.exchangeCard(
                controller.getPlayerById(controller.getLogic().getTurn()), controller);
    }

    private void handleMainPlayersDecision(GameController controller) {
        if (controller.DEBUG) {
            Log.d("-------", "I made my card exchange");
        }
        card_exchange_logic_.exchangeCards(controller);
    }

    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public synchronized void draw(Canvas canvas, GameController controller) {

        if (card_exchange_logic_.isAnimationRunning()) {
            card_exchange_logic_.draw(canvas, controller);
        } else if (enemy_card_exchange_logic_.isAnimationRunning()) {
            enemy_card_exchange_logic_.draw(canvas, controller);
        }
    }


    //----------------------------------------------------------------------------------------------
    // Touch Events
    //                  cards touchable, to prepare
    //                  exchange button
    //
    synchronized public void touchEventDown(int X, int Y, GameController controller) {
        if (!card_exchange_logic_.isAnimationRunning()) {
            return;
        }

        // prepare -> cards touchable
        if (card_exchange_logic_.isPreparationRunning()) {
            for (int i = 0; i < controller.getPlayerById(0).getAmountOfCardsInHand(); i++) {
                Card card = controller.getPlayerById(0).getHand().getCardAt(i);
                if (X >= card.getPosition().x && X < card.getPosition().x + card.getWidth() &&
                        Y >= card.getPosition().y && Y < card.getPosition().y + card.getHeight()) {
                    card_exchange_logic_.prepareCardExchange(card);
                }
            }
        }


        card_exchange_logic_.getButton().touchEventDown(X, Y);
    }

    synchronized public void touchEventMove(int X, int Y) {
        if (!card_exchange_logic_.isAnimationRunning()) {
            return;
        }
        card_exchange_logic_.getButton().touchEventMove(X, Y);
    }

    synchronized public void touchEventUp(int X, int Y, GameController controller) {
        if (!card_exchange_logic_.isAnimationRunning()) {
            return;
        }
        if (card_exchange_logic_.getButton().touchEventUp(X, Y)) {
            handleMainPlayersDecision(controller);
        }
    }
}
