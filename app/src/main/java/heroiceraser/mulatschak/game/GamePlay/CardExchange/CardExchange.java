package heroiceraser.mulatschak.game.GamePlay.CardExchange;

import android.graphics.Canvas;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.Message;
import heroiceraser.mulatschak.MainActivity;
import heroiceraser.mulatschak.game.BaseObjects.Card;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;
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
            controller.getPlayerById(logic.getTurn()).gameState = Message.gameStateWaitForPlayACard;
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
        }

        else if (logic.getTurn() != 0) {
            //controller.getView().enableUpdateCanvasThread();
            // single player
            if (controller.getPlayerById(logic.getTurn()).isEnemyLogic()) {
                handleEnemyAction(controller);
            }
            else {
                controller.waitForOnlineInteraction = Message.cardExchange;
                String oId = controller.getPlayerById(logic.getTurn()).getOnlineId();
                if (controller.DEBUG){ Log.d("-------", "wait for " +
                        controller.getPlayerById(controller.getLogic().getTurn()).getDisplayName()
                        + " card exchange"); }
                controller.requestMissedMessagePlayerCheck(controller.fillGameStates(),
                        controller.getPlayerById(controller.getLogic().getTurn()).getOnlineId(),
                        controller.mainActivity.gameState, Message.requestCardExchange, oId);
            }
        }
    }

    public void handleEnemyAction(final GameController controller) {
        enemy_card_exchange_logic_.exchangeCard(
                controller.getPlayerById(controller.getLogic().getTurn()), controller);
    }

    public void handleOnlineInteraction(ArrayList<Integer> handCardsToRemoveIds, GameController controller) {
        controller.waitForOnlineInteraction = Message.noMessage;
        MyPlayer player = controller.getPlayerById(controller.getLogic().getTurn());
        ArrayList<Integer> handCardsToRemovePos = new ArrayList<>();
        for (int i = 0; i < handCardsToRemoveIds.size(); i++) {
            for (int j = 0; j < player.getHand().getCardStack().size(); j++) {
                if (handCardsToRemoveIds.get(i) == player.getHand().getCardAt(j).getId()) {
                    handCardsToRemovePos.add(j);
                }
            }
        }

        enemy_card_exchange_logic_.exchangeCardOnline(
                controller.getPlayerById(controller.getLogic().getTurn()),
                controller, handCardsToRemovePos);
    }

    private void handleMainPlayersDecision(GameController controller) {

        if (controller.multiplayer_) {
            // broadcast to all the decision
            MainActivity activity = (MainActivity) controller.getView().getContext();
            Gson gson = new Gson();
            ArrayList<Integer> cardHandIds = new ArrayList<>();
            List<Card> playerHand = controller.getPlayerById(0).getHand().getCardStack();
            for (int i = 0; i < playerHand.size(); i++) {
                if (playerHand.get(i).getFixedPosition().y != playerHand.get(i).getPosition().y) {
                    cardHandIds.add(playerHand.get(i).getId());
                }
            }
            activity.broadcastMessage(Message.cardExchange, gson.toJson(cardHandIds));
        }
        if (controller.DEBUG){ Log.d("-------", "I made my card exchange"); }
        card_exchange_logic_.exchangeCards(controller);
    }

    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public synchronized void draw(Canvas canvas, GameController controller) {

        if (card_exchange_logic_.isAnimationRunning()) {
            card_exchange_logic_.draw(canvas, controller);
        }
        else if (enemy_card_exchange_logic_.isAnimationRunning()) {
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
                if (X >= card.getPosition().x &&  X < card.getPosition().x + card.getWidth() &&
                        Y >= card.getPosition().y &&  Y < card.getPosition().y + card.getHeight()) {
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
