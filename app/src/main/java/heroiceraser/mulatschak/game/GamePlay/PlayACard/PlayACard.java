package heroiceraser.mulatschak.game.GamePlay.PlayACard;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import heroiceraser.mulatschak.game.DrawableObjects.Card;
import heroiceraser.mulatschak.game.DrawableObjects.CardStack;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;


//----------------------------------------------------------------------------------------------
//  ToDo
//
public class PlayACard {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private Bitmap invalid_move_symbol_;
    private Point invalid_move_symbol_position_;
    private boolean invalid_move_symbol_running_;
    private int invalid_symbol_alpha_;
    private long invalid_time_start_;

    private int move_card_idx_;
    private boolean card_movable_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public PlayACard() { }


    //----------------------------------------------------------------------------------------------
    //  Init
    //
    public void init(GameView view) {
        move_card_idx_ = GameController.NOT_SET;
        card_movable_ = false;
        invalid_move_symbol_running_ = false;
        invalid_move_symbol_ = HelperFunctions.loadBitmap(view, "no_way",
                view.getController().getLayout().getCardHeight(),
                view.getController().getLayout().getCardHeight());
        invalid_move_symbol_position_ = new Point(
                view.getController().getDiscardPile().getPositions().get(0));
        invalid_move_symbol_position_.y += view.getController().getLayout().getCardHeight() / 2;
        invalid_move_symbol_position_.x -= (view.getController().getLayout().getCardHeight() -
                view.getController().getLayout().getCardWidth()) / 2;
        invalid_symbol_alpha_ = 0;
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas, GameController controller) {

        if (move_card_idx_ != GameController.NOT_SET) {
            Card card = controller.getPlayerById(0).getHand().getCardAt(move_card_idx_);
            if (card != null) {
                canvas.drawBitmap(card.getBitmap(), card.getPosition().x, card.getPosition().y,
                        null);
            }
        }

        if (invalid_move_symbol_running_ && invalid_symbol_alpha_ > 0) {
            Paint alphaPaint = new Paint();
            alphaPaint.setAlpha(invalid_symbol_alpha_);
            canvas.drawBitmap(invalid_move_symbol_, invalid_move_symbol_position_.x,
                    invalid_move_symbol_position_.y, alphaPaint);
            reduceInvalidSymbolAlpha(controller);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  reduceInvalidSymbolAlpha
    //                              --> reduces the alpha value
    //
    private void reduceInvalidSymbolAlpha(GameController controller) {

        invalid_symbol_alpha_ = calculateAlphaPercentage(1500, 255, invalid_time_start_);

        if (invalid_symbol_alpha_ < 0) {
            invalid_symbol_alpha_ = 0;
            invalid_move_symbol_running_ = false;
            controller.getView().disableUpdateCanvasThreadOnly4TouchEvents();
        }
        else {
            invalid_move_symbol_running_ = true;
        }
    }



    //----------------------------------------------------------------------------------------------
    //  calculateAlphaPercentage()
    //
    private int calculateAlphaPercentage(int max_time, int max_value, long start_time) {
        long time = System.currentTimeMillis();
        long time_since_start = time - start_time;

        if (time_since_start > max_time) {
            return 0;
        }

        double percentage  = ((double)time_since_start / max_time);
        return max_value - ((int) (percentage * max_value));
    }

    //----------------------------------------------------------------------------------------------
    //  touchActionDOWN
    //                  checks if a hand card gets touched, saved index to move_card_idx
    //
    public void touchActionDown(GameController controller, int X, int Y) {

        if (isCardMoveable() &&
            !controller.getNonGamePlayUIContainer().isAWindowActive() &&
             move_card_idx_ == GameController.NOT_SET) {

            for (int i = 0; i < controller.getPlayerById(0).getAmountOfCardsInHand(); i++) {
                Card card = controller.getPlayerById(0).getHand().getCardAt(i);
                if (card.getFixedPosition() == null) {
                    break;
                }
                if (X >= card.getFixedPosition().x &&
                        X < card.getFixedPosition().x + controller.getLayout().getCardWidth() &&
                        Y >= card.getFixedPosition().y &&
                        Y < card.getFixedPosition().y + controller.getLayout().getCardHeight()) {
                    move_card_idx_ = i;
                    break;
                } else {
                    move_card_idx_ = GameController.NOT_SET;
                }
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    //  touchActionMove:
    //                      move card according to the movement
    //
    public void touchActionMove(GameController controller, int X, int Y) {
        if (move_card_idx_ >= 0) {
        controller.getPlayerById(0).getHand().getCardAt(move_card_idx_).setPosition(
                X -  controller.getLayout().getCardWidth() / 2,
                Y -  controller.getLayout().getCardHeight() / 2);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  touchActionUP
    //
    public void touchActionUp(GameController controller) {
        if (move_card_idx_ >= 0) {
            CardStack hand =  controller.getPlayerById(0).getHand();
            int card_y = hand.getCardAt(move_card_idx_).getPosition().y;
            int fixed_y = hand.getCardAt(move_card_idx_).getFixedPosition().y;

            // if the card was not pulled high enough to reach the discard pile
            //      -> return card to hand
            if (card_y > fixed_y - controller.getLayout().getCardHeight() * 1.5) {
                returnCardToHand(controller.getPlayerById(0).getHand().getCardAt(move_card_idx_));
                return;
            }

            // not player 0s turn! -> return card to hand + INVALID SYMBOL
            if (controller.getLogic().getTurn() != 0) {
                showInvalidSymbol(controller);
                returnCardToHand(controller.getPlayerById(0).getHand().getCardAt(move_card_idx_));
                return;
            }

            // check if card is a valid play
            boolean valid = controller.getLogic().isAValidCardPlay(hand.getCardAt(move_card_idx_),
                    hand, controller.getDiscardPile());

            // invalid card play -> return card to hand + INVALID SYMBOL
            if (!valid ) {
                showInvalidSymbol(controller);
                returnCardToHand(controller.getPlayerById(0).getHand().getCardAt(move_card_idx_));
                return;
            }

            // valid card play -> move card to discard pile and give turn to the next player
            if (valid) {
                moveCardFromHandToDiscardPile(controller);

                // recalculate hand positions!
                controller.getAnimation().getReAnimateHands()
                        .redrawHands(controller.getLayout(), controller.getPlayerById(0));

                // give turn to next player
                move_card_idx_ = GameController.NOT_SET;
                invalid_move_symbol_running_ = false;
                invalid_symbol_alpha_ = 0;
                controller.getLogic().turnToNextPlayer(controller.getAmountOfPlayers());
                controller.nextTurn();
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    //  showInvalidSymbol
    //
    private void showInvalidSymbol(final GameController controller) {
        invalid_move_symbol_running_ = true;
        invalid_symbol_alpha_ = 255;
        invalid_time_start_ = System.currentTimeMillis();
        controller.getView().enableUpdateCanvasThreadOnly4TouchEvents();
    }


    //----------------------------------------------------------------------------------------------
    //  returnCardToHand
    //
    private void returnCardToHand(Card card) {
        move_card_idx_ = GameController.NOT_SET;
        card.setPosition(new Point(card.getFixedPosition()));
    }


    //----------------------------------------------------------------------------------------------
    //  moveCardFromHandToDiscardPile
    //
    private void moveCardFromHandToDiscardPile(GameController controller) {
        CardStack hand = controller.getPlayerById(0).getHand();

        hand.getCardAt(move_card_idx_).setPosition(
                new Point(controller.getDiscardPile().getPoint(0)) );

        controller.getDiscardPile().setCardBottom(hand.getCardAt(move_card_idx_));

        hand.getCardStack().remove(move_card_idx_);
    }


    //----------------------------------------------------------------------------------------------
    //  Getter and Setter
    //
    private boolean isCardMoveable() {
        return card_movable_;
    }

    public boolean isInvalidMoveSymbolRunning() {
        return invalid_move_symbol_running_;
    }

    public void setCardMoveable(boolean card_moveable_) {
        this.card_movable_ = card_moveable_;
    }
}
