package heroiceraser.mulatschak.game.GamePlay.CardExchange;

import android.graphics.Canvas;
import android.graphics.Point;

import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 03.01.2018.
 */

public class CardExchange {

    private CardExchangeLogic card_exchange_logic_;
    private EnemyCardExchangeLogic enemy_card_exchange_logic_;

    public CardExchange() {
        card_exchange_logic_ = new CardExchangeLogic();
        enemy_card_exchange_logic_ = new EnemyCardExchangeLogic();
    }

    public void init(GameView view) {
        card_exchange_logic_.init(view);
        // enemy_card_exchange_logic_.init(view); Not Needed
    }

    //----------------------------------------------------------------------------------------------
    //  cada
    //
    public void makeCardExchange(GameController controller) {
        makeCardExchange(false, controller); // not first call
    }

    public void makeCardExchange(boolean first_call, GameController controller) {
        GameLogic logic = controller.getLogic();
        
        if (!first_call) {
            controller.turnToNextPlayer();
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
            controller.getView().disableUpdateCanvasThread();
        }

        else if (logic.getTurn() != 0) {
            controller.getView().enableUpdateCanvasThread();
            enemy_card_exchange_logic_.exchangeCard(controller.getPlayerById(logic.getTurn()), controller);
        }
    }

    public void draw(Canvas canvas, GameController controller) {

        if (card_exchange_logic_.isAnimationRunning()) {
            card_exchange_logic_.draw(canvas, controller);
        }
        else if (enemy_card_exchange_logic_.isAnimationRunning()) {
            enemy_card_exchange_logic_.draw(canvas, controller);
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Getter
    //
    public CardExchangeLogic getCardExchangeLogic() {
        return card_exchange_logic_;
    }
}
