package heroiceraser.mulatschak.game.Animations;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import java.util.ArrayList;
import java.util.List;
import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.DrawableBasicObjects.HelpText;
import heroiceraser.mulatschak.game.DrawableObjects.Card;
import heroiceraser.mulatschak.game.DrawableObjects.CardStack;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.Player;


//----------------------------------------------------------------------------------------------
//  CardExchange:
//                      //
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

    private Camera camera_;     // needed for 3D rotation od bitmaps
    private long timePrev = 0;  // keeps track of the previous timestamp

    private boolean animation_spinning_running_;    // is the spinning animation running
    private boolean animation_end_running_; // is the animation ending running

    private List<Card> exchanged_cards_; // stores old cards
    private List<Card> new_drawn_cards_; // stores new cards

    private boolean cards_changed_; // triggered after 50% of the animation is done,
    //                                   handles which container gets displayed

    private int degree_;    // at which degree is the card in the moment of drawing
    private double spin_speed_; // how fast is the rotation spinning


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public CardExchange() {
        animation_running_ = false;
        animation_spinning_running_ = false;
        animation_end_running_ = false;
        help_text_ = new HelpText();
        exchange_buttons_ = new ArrayList<>();
        active_button_ = 0;
        camera_ = new Camera();
        exchanged_cards_ = new ArrayList<>();
        new_drawn_cards_ = new ArrayList<>();
        cards_changed_ = false;
        degree_ = 0;
        spin_speed_ = 1;
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


    //----------------------------------------------------------------------------------------------
    //  draw:
    //        draws the spinning animations, or the correct exchange button (with a helptext)
    //
    public void draw(Canvas canvas, GameController controller) {

        // spinning animation
        if (animation_spinning_running_) {

            // rotation fun
            Matrix matrix = new Matrix();
            camera_.save();
            camera_.rotateX(0);
            degree_ += spin_speed_;

            // can't see anything
            if (degree_ % 90 == 0) {
                degree_++;
            }
            camera_.rotateY(degree_);
            camera_.getMatrix(matrix);
            float x = (controller.getDeck().getBacksideBitmap().getWidth() / 2);
            // float y = (bitmap.getHeight() / 2);
            matrix.preTranslate(-x, 0);
            matrix.postTranslate(x, 0);

            for (int i = 0; i < exchanged_cards_.size(); i++) {

                // decide which image has to be shown
                Bitmap bitmap;

                // show frontside
                if (degree_ % 360 < 90 || degree_ % 360 > 270) {
                    // show starting image
                    bitmap = exchanged_cards_.get(i).getBitmap();
                    //show animation ending image
                    if (cards_changed_ && i < new_drawn_cards_.size()) {
                        bitmap = new_drawn_cards_.get(i).getBitmap();
                    }
                }
                // show backside
                else {
                    bitmap = controller.getDeck().getBacksideBitmap();
                }

                Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                // drawing
                int w = controller.getDeck().getBacksideBitmap().getWidth();
                int new_x = exchanged_cards_.get(i).getPosition().x + ((w - rotated.getWidth()) / 2);
                canvas.drawBitmap(rotated, new_x, exchanged_cards_.get(i).getPosition().y, null);
            }
            camera_.restore();
            continueExchangeCards(controller);
        }

        // continueAfter the spinning animation
        else if (animation_end_running_) {
            oldCardsToTrash(controller);
            moveNewDrawnCardsToPlayerHand(controller);
        }

        // draws the correct exchange button
        else {
            exchange_buttons_.get(active_button_).draw(canvas);
        }
    }


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


    //----------------------------------------------------------------------------------------------
    // exchangeCards:
    //                  -> get called by touch events!
    //
    public void exchangeCards(GameController controller) {

        active_button_ = 0;

        resetMemberVars();          // just to be sure ;)
        animation_running_ = true;

        moveExchangeCardsFromPlayerToContainer(controller.getPlayerById(0), exchanged_cards_);

        // start spinning animation
        if (exchanged_cards_.size() > 0) {
            animation_spinning_running_ = true;
            controller.getView().enableUpdateCanvasThread();
            continueExchangeCards(controller);
        }
        // nothing got exchanged, move on without a animation
        else {
            endCardExchange(controller);
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
    // continueExchangeCards
    //                  -> gets called from draw
    //                  recalculates new spinning speed and degree
    //
    private void continueExchangeCards(GameController controller) {

        // nothing to do if spinning is over
        if (!animation_spinning_running_) {
            return;
        }

        // calculate time interval
        long timeNow = System.currentTimeMillis();
        long timeDelta = timeNow - timePrev;

        if (timeDelta > 300) {

            // increase speed in the first x rotations
            if (degree_ / 360 < 4.2) {
                spin_speed_ *= 2;
                if (spin_speed_ > 35) {
                    spin_speed_ = 35;
                }
            }
            // decrease speed in the last rotations
            else {
                spin_speed_ /= 2;
                if (spin_speed_ < 1) {
                    spin_speed_ = 1;
                }
            }

            timePrev = System.currentTimeMillis();
        }

        // after around 50% switch the cards
        if (!cards_changed_ && degree_ / 360 > 2) {
            takeNewCards(controller);
            cards_changed_ = true;
        }

        // animation is done after x rotations
        if (degree_ / 360 > 5) {
            spin_speed_ = 0;
            animation_spinning_running_ = false;
            animation_end_running_ = true;
        }
    }


    //----------------------------------------------------------------------------------------------
    // takeNewCards
    //                  draws new cards and saved them in new drawn cards container
    //
    private void takeNewCards(GameController controller) {

        // take new cards from the deck
        for (int i = 0; i < exchanged_cards_.size(); i++) {
            controller.takeCardFromDeck(0, controller.getDeck());
            Card card = controller.getPlayerById(0).getHand().getCardAt(controller.getPlayerById(0)
                    .getAmountOfCardsInHand() - 1);
            card.setPosition(exchanged_cards_.get(i).getPosition());
            card.setFixedPosition(exchanged_cards_.get(i).getFixedPosition());

            // but right now just have them in the animation container
            // adding to new_drawn_cards_ for an easier handling in the animation
            new_drawn_cards_.add(card);

            // and remove them from the hand
            controller.getPlayerById(0).getHand().getCardStack().remove(card);
        }
    }


    //----------------------------------------------------------------------------------------------
    // oldCardsToTrash
    //
    private void oldCardsToTrash(GameController controller) {

        // add old cards to trash
        for (int i = 0; i < exchanged_cards_.size(); i++) {
            controller.getTrash().addCard(exchanged_cards_.get(i));
        }
    }


    //----------------------------------------------------------------------------------------------
    //  moveNewDrawnCardsToPlayerHand
    //
    private void moveNewDrawnCardsToPlayerHand(GameController controller) {

        // now really move tha cards to the hand and set positions correctly
        for (Card card : new_drawn_cards_) {
            card.setPosition(card.getFixedPosition());
            controller.getPlayerById(0).getHand().addCard(card);
        }
        endCardExchange(controller);
    }


    //----------------------------------------------------------------------------------------------
    //  endCard exchange
    //
    private void endCardExchange(GameController controller) {
        resetMemberVars();
        controller.makeCardExchange();
    }


    //----------------------------------------------------------------------------------------------
    //  endCard exchange
    //
    private void resetMemberVars() {
        exchanged_cards_.clear();
        new_drawn_cards_.clear();
        animation_spinning_running_ = false;
        animation_end_running_ = false;
        animation_running_ = false;
        spin_speed_ = 1;
        degree_ = 0;
        cards_changed_ = false;
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
