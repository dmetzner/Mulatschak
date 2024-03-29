package heroiceraser.mulatschak.game.GamePlay.PlayACard;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import heroiceraser.mulatschak.game.BaseObjects.Card;
import heroiceraser.mulatschak.game.BaseObjects.CardStack;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.utils.BitmapMethodes;


//----------------------------------------------------------------------------------------------
//  ToDo
//
public class PlayACardLogic {

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
    PlayACardLogic() {
    }


    //----------------------------------------------------------------------------------------------
    //  Init
    //
    public void init(GameView view) {
        move_card_idx_ = GameController.NOT_SET;
        card_movable_ = false;
        invalid_move_symbol_running_ = false;
        invalid_move_symbol_ = BitmapMethodes.loadBitmap(view, "no_way",
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
    public synchronized void draw(Canvas canvas, GameController controller) {

        if (move_card_idx_ != GameController.NOT_SET) {
            Card card = controller.getPlayerByPosition(0).getHand().getCardAt(move_card_idx_);
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
            controller.getView().postInvalidateOnAnimation();
        }
    }


    //----------------------------------------------------------------------------------------------
    //  reduceInvalidSymbolAlpha
    //                              --> reduces the alpha value
    //
    private void reduceInvalidSymbolAlpha(GameController controller) {

        invalid_symbol_alpha_ = calculateAlphaPercentage(1500, invalid_time_start_);

        if (invalid_symbol_alpha_ < 0) {
            invalid_symbol_alpha_ = 0;
            invalid_move_symbol_running_ = false;
            //controller.getView().disableUpdateCanvasThreadOnly4TouchEvents();
        } else {
            invalid_move_symbol_running_ = true;
        }
    }


    //----------------------------------------------------------------------------------------------
    //  calculateAlphaPercentage()
    //
    private int calculateAlphaPercentage(int max_time, long start_time) {
        int max_value = 255;
        long time = System.currentTimeMillis();
        long time_since_start = time - start_time;

        if (time_since_start > max_time) {
            return 0;
        }

        double percentage = ((double) time_since_start / max_time);
        return max_value - ((int) (percentage * max_value));
    }


    //----------------------------------------------------------------------------------------------
    //  touchActionDOWN
    //                  checks if a hand card gets touched, saved index to move_card_idx
    //
    synchronized void touchActionDown(GameController controller, int X, int Y) {

        if (isCardMoveable() &&
                move_card_idx_ == GameController.NOT_SET) {

            for (int i = 0; i < controller.getPlayerByPosition(0).getAmountOfCardsInHand(); i++) {
                Card card = controller.getPlayerByPosition(0).getHand().getCardAt(i);
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
    synchronized void touchActionMove(GameController controller, int X, int Y) {
        if (move_card_idx_ >= 0) {
            controller.getPlayerByPosition(0).getHand().getCardAt(move_card_idx_).setPosition(
                    X - controller.getLayout().getCardWidth() / 2,
                    Y - controller.getLayout().getCardHeight() / 2);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  touchActionUP
    //
    synchronized void touchActionUp(GameController controller) {
        if (move_card_idx_ >= 0) {
            CardStack hand = controller.getPlayerByPosition(0).getHand();
            int card_y = hand.getCardAt(move_card_idx_).getPosition().y;
            int fixed_y = hand.getCardAt(move_card_idx_).getFixedPosition().y;

            // if the card was not pulled high enough to reach the discard pile
            //      -> return card to hand
            if (card_y > fixed_y - controller.getLayout().getCardHeight() * 1.5) {
                returnCardToHand(controller.getPlayerByPosition(0).getHand().getCardAt(move_card_idx_));
                return;
            }

            // not player 0s turn! -> return card to hand + INVALID SYMBOL
            if (controller.getLogic().getTurn() != 0) {
                showInvalidSymbol(controller);
                returnCardToHand(controller.getPlayerById(0).getHand().getCardAt(move_card_idx_));
                return;
            }

            // checkButton if card is a valid play
            boolean valid = controller.getLogic().isAValidCardPlay(hand.getCardAt(move_card_idx_),
                    hand, controller.getDiscardPile());

            // invalid card play -> return card to hand + INVALID SYMBOL
            if (!valid) {
                showInvalidSymbol(controller);
                returnCardToHand(controller.getPlayerById(0).getHand().getCardAt(move_card_idx_));
                return;
            }

            // valid card play -> move card to discard pile call playACard again
            moveCardFromHandToDiscardPile(controller);

//            controller.getGamePlay().getPlayACardRound().handleMainPlayersDecision(controller);

            // recalculate hand positions!
            controller.getPlayerHandsView().redrawHands(controller.getLayout(), controller.getPlayerById(0));

            // give turn to next player
            move_card_idx_ = GameController.NOT_SET;
            invalid_move_symbol_running_ = false;
            invalid_symbol_alpha_ = 0;
            controller.getGamePlay().getPlayACardRound().playACard(false, controller);

        }
    }


    //----------------------------------------------------------------------------------------------
    //  showInvalidSymbol
    //
    private void showInvalidSymbol(final GameController controller) {
        invalid_move_symbol_running_ = true;
        invalid_symbol_alpha_ = 255;
        invalid_time_start_ = System.currentTimeMillis();
        //controller.getView().enableUpdateCanvasThreadOnly4TouchEvents();
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
                new Point(controller.getDiscardPile().getPoint(0)));

        controller.getDiscardPile().setCardBottom(hand.getCardAt(move_card_idx_));

        hand.getCardStack().remove(move_card_idx_);
    }


    //----------------------------------------------------------------------------------------------
    //  Getter and Setter
    //
    private boolean isCardMoveable() {
        return card_movable_;
    }

    void setCardMoveable(boolean card_moveable_) {
        this.card_movable_ = card_moveable_;
    }
}
