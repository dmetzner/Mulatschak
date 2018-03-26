package heroiceraser.mulatschak.game.GamePlay.CardExchange;
import android.graphics.Canvas;
import android.graphics.Point;
import android.support.annotation.StringRes;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.DrawableBasicObjects.HelpText;
import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.BaseObjects.Card;
import heroiceraser.mulatschak.game.BaseObjects.CardStack;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;


//----------------------------------------------------------------------------------------------
//  CardExchangeLogic:
//                 player 0 card exchange process
//
//
// Card Exchange Animation:
//
//    -> 3D spinning rotation (more details in CardExchangeAnimation class)
//
public class CardExchangeLogic {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private boolean animationRunning; // should be true while the card exchange process is active
    private boolean preparationRunning;
    
    private List<MyButton> exchangeButtons; // container for all card exchange buttons
    private int activeButton; // keeps track of which button should be displayed

    private HelpText helpText;    // shows an information text

    private CardExchangeAnimation cardExchangeAnimation;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    CardExchangeLogic() {
        helpText = new HelpText();
        exchangeButtons = new ArrayList<>();
        cardExchangeAnimation = new CardExchangeAnimation();
    }


    //----------------------------------------------------------------------------------------------
    //  init:
    //           sets the helpText, and all exchange buttons
    //
    public void init(GameView view) {
        animationRunning = false;
        preparationRunning = false;
        activeButton = 0;

        GameLayout layout = view.getController().getLayout();
        int width = layout.getCardExchangeTextWidth();
        int max_height = layout.getCardExchangeButtonPosition().y - layout.getCardExchangeTextPosition().y;

        String help_text = view.getResources().getString(R.string.card_exchange_help_text);
        helpText.init(view, help_text, width, max_height);

        Point position = layout.getCardExchangeButtonPosition();
        width = layout.getCardExchangeButtonSize().x;
        int height = layout.getCardExchangeButtonSize().y;

        int buttons_size = 6;
        for (int i = 0; i < buttons_size; i++) {
            MyButton button = new MyButton();

            String xmlName = "card_exchange_button_" + i;
            // Get the identifier of the resource by its name.
            @StringRes int resId = view.getResources().getIdentifier(xmlName, "string",
                    view.getContext().getPackageName());
            // Use the value of the resource.
            String buttonText = view.getContext().getString(resId);
            button.init(view, position, width, height, "button_blue_metallic_large", buttonText);

            exchangeButtons.add(button);
        }
    }

    //
    //----------------------- Choose Cards to Exchange ---------------------------------------------
    //

    //----------------------------------------------------------------------------------------------
    //  prepareCardExchange:
    //                       --> must be called in the touch events!!
    //
    //                      if a card gets clicked, move it up or down
    //                      and handle the amount of cards to exchange
    //
    void prepareCardExchange(Card card) {
        if (card.getPosition().equals(card.getFixedPosition())) {
            moveCardUp(card);
            activeButton++;
        } else {
            moveCardDown(card);
            activeButton--;
        }
    }

    //----------------------------------------------------------------------------------------------
    //  moveCardUp:
    //              moves a card, half the card size, up
    //
    private void moveCardUp(Card card) {
        int shift_y = (int) (card.getHeight() / 2.0);
        card.setPosition(card.getPosition().x, card.getPosition().y - shift_y);
    }

    //----------------------------------------------------------------------------------------------
    //  moveDown:
    //              just resets the card position to the fixed position
    //
    private void moveCardDown(Card card) {
        card.setPosition(card.getFixedPosition());
    }


    //----------------------------------------------------------------------------------------------
    // handleExchangeButtons:
    //                   if there are too less cards in the deck left, disable the exchange buttons
    //
    void handleExchangeButtons(int deck_size) {
        int max_cards_to_trade = GameLogic.MAX_CARDS_PER_HAND;
        int last_button_idx = exchangeButtons.size() - 1;

        // if there are enough cards, enable the valid buttons
        if (deck_size >= max_cards_to_trade) {
            for (int i = last_button_idx; i > last_button_idx - max_cards_to_trade; i--) {
                exchangeButtons.get(i).setEnabled(true);
            }
        }
        // if there are not enough cards, disable the invalid buttons
        else {
            int cards_to_disable = (max_cards_to_trade - deck_size);
            for (int i = last_button_idx; i > last_button_idx - cards_to_disable; i--) {
                exchangeButtons.get(i).setEnabled(false);
            }
        }
    }

    //
    //----------------------- Exchange Cards -------------------------------------------------------
    //

    //----------------------------------------------------------------------------------------------
    // exchangeCards:
    //                  -> get called by touch events!
    //
    void exchangeCards(GameController controller) {

        activeButton = 0;
        preparationRunning = false;
        animationRunning = true;
        cardExchangeAnimation.init(controller);

        // move exchange cards to animation container
        moveExchangeCardsFromPlayerToContainer(controller.getPlayerById(0),
                cardExchangeAnimation.getExchangedCards());

        // no cards to change? -> nothing to do
        if (cardExchangeAnimation.getExchangedCards().size() <= 0) {
            endCardExchange(controller);
            return;
        }

        //
        takeNewCards(controller, cardExchangeAnimation.getExchangedCards(),
                cardExchangeAnimation.getNewDrawnCards());

        // start a wonderful spinning animation -> enables canvas thread
        controller.getView().enableUpdateCanvasThread();
        cardExchangeAnimation.startSpinning(controller);

        // --> gets continued by draw method
    }


    //----------------------------------------------------------------------------------------------
    //  draw:
    //        draws the spinning animations, or the correct exchange button (with a helptext)
    //
    public void draw(Canvas canvas, GameController controller) {

        // spinning animation
        if (cardExchangeAnimation.isSpinning()) {
            cardExchangeAnimation.drawRotation(canvas);
            cardExchangeAnimation.recalculateSpinningParameters(controller);
        }

        // continueAfter the spinning animation
        else if (cardExchangeAnimation.hasSpinningStopped()) {
            oldCardsToTrash(controller);
            moveNewDrawnCardsToPlayerHand(controller);
        }

        // choose how many cards to exchange
        if (preparationRunning) {
            // Help Text
            Point position = controller.getLayout().getCardExchangeTextPosition();
            helpText.draw(canvas, position);
            // button
            exchangeButtons.get(activeButton).draw(canvas);
        }
    }


    //----------------------------------------------------------------------------------------------
    // moveExchangeCardsFromPlayerToContainer:
    //
    private void moveExchangeCardsFromPlayerToContainer(MyPlayer myPlayer, List<Card> container) {
        CardStack hand = myPlayer.getHand();
        for (int i = 0; i < hand.getCardStack().size(); i++) {
            if (!hand.getCardAt(i).getPosition().equals(hand.getCardAt(i).getFixedPosition())) {
                container.add(hand.getCardAt(i));
                hand.getCardStack().remove(i);
                i--;
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    // takeNewCards
    //                  draws new cards and saved them in new drawn cards container
    //
    private void takeNewCards(GameController controller, List<Card> exchanged_cards,
                              List<Card> new_drawn_cards) {

        // take new cards from the deck
        for (int i = 0; i < exchanged_cards.size(); i++) {
            controller.getGamePlay().getDealCards().takeCardFromDeck(controller.getPlayerById(0), controller.getDeck());
            Card card = controller.getPlayerById(0).getHand().getCardAt(controller.getPlayerById(0)
                    .getAmountOfCardsInHand() - 1);
            card.setPosition(exchanged_cards.get(i).getPosition());
            card.setFixedPosition(exchanged_cards.get(i).getFixedPosition());

            // but right now just have them in the animation container
            // adding to new_drawn_cards_ for an easier handling in the animation
            new_drawn_cards.add(card);

            // and remove them from the hand
            controller.getPlayerById(0).getHand().getCardStack().remove(card);
        }
    }


    //----------------------------------------------------------------------------------------------
    // oldCardsToTrash
    //
    private void oldCardsToTrash(GameController controller) {

        // add old cards to trash
        for (int i = 0; i < cardExchangeAnimation.getExchangedCards().size(); i++) {
            controller.getPlayerById(0).exchanged_cards_.addCard(cardExchangeAnimation.getExchangedCards().get(i));
            controller.getTrash().addCard(cardExchangeAnimation.getExchangedCards().get(i));
        }
    }


    //----------------------------------------------------------------------------------------------
    //  moveNewDrawnCardsToPlayerHand
    //
    private void moveNewDrawnCardsToPlayerHand(GameController controller) {

        // now really move tha cards to the hand and set positions correctly
        for (Card card : cardExchangeAnimation.getNewDrawnCards()) {
            boolean inserted = false;
            MyPlayer myPlayer_ = controller.getPlayerById(0);
            for (int i = 0; i < myPlayer_.getHand().getCardStack().size(); i++) {
                if (myPlayer_.getHand().getCardAt(i).getPosition().x > card.getPosition().x) {
                    myPlayer_.getHand().getCardStack().add(i, card);
                    inserted = true;
                    break;
                }
            }
            if (!inserted) {
                myPlayer_.getHand().addCard(card);
            }
            card.setPosition(card.getFixedPosition());
            card.setVisible(true);
        }
        endCardExchange(controller);
    }


    //----------------------------------------------------------------------------------------------
    //  endCard exchange
    //
    private void endCardExchange(GameController controller) {
        animationRunning = false;
        cardExchangeAnimation.reset();
        controller.getGamePlay().getCardExchange().makeCardExchange(controller);
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public MyButton getButton() {
        if (activeButton < 0 || activeButton > exchangeButtons.size() - 1) {
            activeButton = 0;
        }

        // taking four cards is not allowed
        // if (activeButton == 4) {
           // exchangeButtons.get(activeButton).setEnabled(false);
        //}

        return exchangeButtons.get(activeButton);
    }


    //----------------------------------------------------------------------------------------------
    //  simple Getter & Setter
    //
    boolean isAnimationRunning() {
        return animationRunning;
    }

    boolean isPreparationRunning() {
        return preparationRunning;
    }

    public void startAnimation() {
        this.animationRunning = true;
        this.preparationRunning = true;
    }
}
