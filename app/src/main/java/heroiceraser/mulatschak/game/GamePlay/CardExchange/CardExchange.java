package heroiceraser.mulatschak.game.GamePlay.CardExchange;
import android.graphics.Canvas;
import android.graphics.Point;
import java.util.ArrayList;
import java.util.List;
import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.DrawableBasicObjects.HelpText;
import heroiceraser.mulatschak.game.DrawableObjects.Card;
import heroiceraser.mulatschak.game.DrawableObjects.CardStack;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.Player;


//----------------------------------------------------------------------------------------------
//  CardExchange:
//                 player 0 card exchange process
//
//
// Card Exchange Animation:
//
//    -> What does this animation?
//        Cards that should get exchanged move up, and spin in a 3D space around the y-axis.
//        spinning speed increases till it reaches a maximum, than the displayed image switches
//        to the new image and reduces the spinning speed. At the end the cards move back
//        to the player hand

//    -> How does this animation work?
//        It uses 2 containers; cards that get exchanged are stored in the first one,
//        while the new drawn cards are stored in container two.
//        This allows an easy switch from one card to another in the animation.
//        The Rotation animation is handled via a Camera and matrix setup,
//        which gets called recursive and based on a time interval
//
public class CardExchange {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private boolean animation_running_; // should be true while the card exchange process is active

    private List<MyButton> exchange_buttons_; // container for all card exchange buttons
    private int active_button_; // keeps track of which button should be displayed

    private HelpText help_text_;    // shows an information text

    private CardExchangeAnimation card_exchange_animation_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public CardExchange() {
        animation_running_ = false;
        help_text_ = new HelpText();
        exchange_buttons_ = new ArrayList<>();
        active_button_ = 0;
        card_exchange_animation_ = new CardExchangeAnimation();
    }


    //----------------------------------------------------------------------------------------------
    //  init:
    //           sets the helpText, and all exchange buttons
    //
    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();
        int width = layout.getCardExchangeTextWidth();
        int max_height = layout.getCardExchangeButtonPosition().y - layout.getCardExchangeTextPosition().y;

        String help_text = "Berühre alle Karten die du austauschen möchtest. " +
                "Du kannst Keine, Eine, Zwei, Drei oder alle Karten austauschen";
        help_text_.init(view, help_text, width, max_height);

        Point position = layout.getCardExchangeButtonPosition();
        width = layout.getCardExchangeButtonSize().x;
        int height = layout.getCardExchangeButtonSize().y;
        for (int i = 0; i < 6; i++) {
            MyButton button = new MyButton();
            int id = i;
            if (i == 4) {
                id = i + 1;
            }
            button.init(view, position, width, height, "button_" + id + "_karten");
            exchange_buttons_.add(button);
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
    public void prepareCardExchange(Card card) {
        if (card.getPosition().equals(card.getFixedPosition())) {
            moveCardUp(card);
            active_button_++;
        } else {
            moveCardDown(card);
            active_button_--;
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
    //                        --> has to be called in the controller!
    //                   if there are too less cards in the deck left, disable the exchange buttons
    //
    public void handleExchangeButtons(int deck_size) {
        int max_cards_to_trade = GameLogic.MAX_CARDS_PER_HAND;
        int last_button_idx = exchange_buttons_.size() - 1;

        // if there are enough cards, enable the valid buttons
        if (deck_size >= max_cards_to_trade) {
            for (int i = last_button_idx; i > last_button_idx - max_cards_to_trade; i--) {
                exchange_buttons_.get(i).setEnabled(true);
            }
        }
        // if there are not enough cards, disable the invalid buttons
        else {
            int cards_to_disable = (max_cards_to_trade - deck_size);
            for (int i = last_button_idx; i > last_button_idx - cards_to_disable; i--) {
                exchange_buttons_.get(i).setEnabled(false);
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
    public void exchangeCards(GameController controller) {

        active_button_ = GameController.NOT_SET;
        animation_running_ = true;
        card_exchange_animation_.init(controller);

        // move exchange cards to animation container
        moveExchangeCardsFromPlayerToContainer(controller.getPlayerById(0),
                card_exchange_animation_.getExchangedCards());

        // no cards to change? -> nothing to do
        if (card_exchange_animation_.getExchangedCards().size() <= 0) {
            endCardExchange(controller);
            return;
        }

        //
        takeNewCards(controller, card_exchange_animation_.getExchangedCards(),
                card_exchange_animation_.getNewDrawnCards());

        // start a wonderful spinning animation -> enables canvas thread
        controller.getView().enableUpdateCanvasThread();
        card_exchange_animation_.startSpinning(controller);

        // --> gets continued by draw method
    }


    //----------------------------------------------------------------------------------------------
    //  draw:
    //        draws the spinning animations, or the correct exchange button (with a helptext)
    //
    public void draw(Canvas canvas, GameController controller) {

        // spinning animation
        if (card_exchange_animation_.isSpinning()) {
            card_exchange_animation_.drawRotation(canvas);
            card_exchange_animation_.recalculateSpinningParameters(controller);
        }

        // continueAfter the spinning animation
        else if (card_exchange_animation_.hasSpinningStopped()) {
            oldCardsToTrash(controller);
            moveNewDrawnCardsToPlayerHand(controller);
        }

        // draws the correct exchange button
        else {
            exchange_buttons_.get(active_button_).draw(canvas);
        }
    }


    //----------------------------------------------------------------------------------------------
    // moveExchangeCardsFromPlayerToContainer:
    //
    private void moveExchangeCardsFromPlayerToContainer(Player player, List<Card> container) {
        CardStack hand = player.getHand();
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
            controller.takeCardFromDeck(0, controller.getDeck());
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
        for (int i = 0; i < card_exchange_animation_.getExchangedCards().size(); i++) {
            controller.getTrash().addCard(card_exchange_animation_.getExchangedCards().get(i));
        }
    }


    //----------------------------------------------------------------------------------------------
    //  moveNewDrawnCardsToPlayerHand
    //
    private void moveNewDrawnCardsToPlayerHand(GameController controller) {

        // now really move tha cards to the hand and set positions correctly
        for (Card card : card_exchange_animation_.getNewDrawnCards()) {
            card.setPosition(card.getFixedPosition());
            controller.getPlayerById(0).getHand().addCard(card);
        }
        endCardExchange(controller);
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
    public MyButton getButton() {
        if (active_button_ < 0 || active_button_ > exchange_buttons_.size() - 1) {
            active_button_ = 0;
        }

        // taking four cards is not allowed
        if (active_button_ == 4) {
            exchange_buttons_.get(active_button_).setEnabled(false);
        }

        return exchange_buttons_.get(active_button_);
    }


    //----------------------------------------------------------------------------------------------
    //  simple Getter & Setter
    //
    public boolean isAnimationRunning() {
        return animation_running_;
    }

    public void setAnimationRunning(boolean animation_running) {
        this.animation_running_ = animation_running;
    }

    public HelpText getHelpText() {
        return help_text_;
    }
}
