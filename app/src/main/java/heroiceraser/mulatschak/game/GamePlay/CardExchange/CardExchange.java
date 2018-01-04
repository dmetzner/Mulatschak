package heroiceraser.mulatschak.game.GamePlay.CardExchange;

import android.graphics.Canvas;
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


    //----------------------------------------------------------------------------------------------
    //  draw
    //
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
