package heroiceraser.mulatschak.game.Animations;

import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;

import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;


import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.DrawableBasicObjects.HelpText;
import heroiceraser.mulatschak.game.DrawableObjects.Card;
import heroiceraser.mulatschak.game.DrawableObjects.CardStack;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 29.08.2017.
 */

public class CardExchange {

    private boolean animation_running_;
    private HelpText help_text_;
    private List<MyButton> exchange_buttons_;
    private int active_buttons_;

    private boolean animation_spinning_running_;
    private Camera camera_;
    private int degree_;
    private double spin_speed_;
    private int spin_skipper_;
    private boolean animation_end_running_;
    private List<Card>  move_cards_;

    public long timeNow;
    public long timePrev = 0;
    public  long timeDelta;


    public CardExchange(GameView view) {
        animation_running_ = false;
        animation_spinning_running_ = false;
        animation_end_running_ = false;
        active_buttons_ = 0;
        help_text_ = new HelpText();
        exchange_buttons_ = new ArrayList<>();
        camera_ = new Camera();
        degree_ = 0;
        spin_skipper_ = 0;
        spin_speed_ = 1;
    }

    public void init(GameView view) {
        String text = "Berühre alle Karten die du austauschen möchtest. " +
                "Du kannst Keine, Eine, Zwei, Drei oder alle Karten austauschen";
        int width = view.getController().getLayout().getCardExchangeTextWidth();
        int max_height = view.getController().getLayout().getCardExchangeButtonPosition().y -
                view.getController().getLayout().getCardExchangeTextPosition().y;
        help_text_.init(view, text, width, max_height);

        active_buttons_ = 0;

        Point position = view.getController().getLayout().getCardExchangeButtonPosition();
        width =  view.getController().getLayout().getCardExchangeButtonSize().x;
        int height =  view.getController().getLayout().getCardExchangeButtonSize().y;
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

    public void draw(Canvas canvas, GameController controller) {
        if (animation_spinning_running_) {
            for (Card card : move_cards_) {
                Bitmap bitmap = card.getBitmap();
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
                float x = (bitmap.getWidth()/2);
                float y = (bitmap.getHeight()/2);
                matrix.preTranslate(-x, 0);
                matrix.postTranslate(x, 0);
                Bitmap rotated = null;

                if (degree_ % 360 < 90 || degree_ % 360 > 270) {
                    rotated = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                }
                else {
                    rotated = Bitmap.createBitmap(controller.getDeck().getBacksideBitmap(), 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                }

                if (rotated != null && rotated.getWidth() != 0 && rotated.getHeight() != 0) {
                    int w = controller.getDeck().getBacksideBitmap().getWidth();
                    int new_x = card.getPosition().x + ((w - rotated.getWidth()) / 2);
                    canvas.drawBitmap(rotated, new_x, card.getPosition().y, null);
                }
                camera_.restore();
            }
            continueExchangeCards(controller);
        }
        else if (animation_end_running_) {
            Log.d("--->", "end running");
            endExchangeCards(controller);
        }
        else {
            exchange_buttons_.get(active_buttons_).draw(canvas);
        }
    }


    public void prepareCardExchange(Card card) {
        if (card.getPosition().equals(card.getFixedPosition())) {
            moveCardUp(card);
            active_buttons_++;
        }
        else {
            moveCardDown(card);
            active_buttons_--;
        }
    }


    public void exchangeCards(GameController controller) {

        active_buttons_ = 0;

        move_cards_ = new ArrayList<>();
        CardStack hand = controller.getPlayerById(0).getHand();

        // removing cards from hand
        for (int i = 0; i < hand.getCardStack().size(); i++) {
            if (hand.getCardAt(i).getPosition().equals(hand.getCardAt(i).getFixedPosition())) {
                continue;
            }
            else {
                move_cards_.add(hand.getCardAt(i));
                hand.getCardStack().remove(i);
                i--;
            }
        }
        if (move_cards_.size() > 0) {
            Log.d("--->", "start running");
            animation_spinning_running_ = true;
            controller.getView().enableUpdateCanvasThread();
            continueExchangeCards(controller);
        }
        else {
            Log.d("--->", "nothing to exchange");
            animation_running_ = false;
            controller.makeCardExchange();
        }

    }

    public void continueExchangeCards(GameController controller) {

        if (!animation_spinning_running_) {
            return;
        }

        timeNow = System.currentTimeMillis();
        timeDelta = timeNow - timePrev;
        if (timeDelta > 300) {
            spin_speed_ *= 2;
            if (spin_speed_ > 35) {
                spin_speed_ = 35;
            }
            timePrev = System.currentTimeMillis();
        }

        // todo change bitmap after 50%

        // animation is done
        if (degree_ / 360 > 5){
            spin_speed_ = 0;
            animation_spinning_running_ = false;
            animation_end_running_ = true;
        }


    }



    public void endExchangeCards(GameController controller) {

        // adding new cards to hand
        for (int i = 0; i < move_cards_.size(); i++) {
            controller.takeCardFromDeck(0, controller.getDeck());
            Card card = controller.getPlayerById(0).getHand().getCardAt(controller.getPlayerById(0)
                    .getAmountOfCardsInHand() - 1);
            card.setPosition(move_cards_.get(i).getFixedPosition());
            card.setFixedPosition(card.getPosition());
        }

        // add old cards to trash
        for (int i = 0; i < move_cards_.size(); i++) {
            controller.getTrash().addCard(move_cards_.get(i));
        }

        animation_running_ = false;
        controller.makeCardExchange();
    }


    private void moveCardUp(Card card) {
        card.setPosition(card.getPosition().x,
                card.getPosition().y - (int) (card.getHeight() / 2.0));
    }

    private void moveCardDown(Card card) {
        card.setPosition(card.getFixedPosition());
    }

    public boolean isAnimationRunning() {
        return animation_running_;
    }

    public void setAnimationRunning(boolean animation_running) {
        this.animation_running_ = animation_running;
    }

    public HelpText getHelpText() {
        return help_text_;
    }

    public MyButton getButton() {
        if (active_buttons_ < 0 || active_buttons_ > exchange_buttons_.size() - 1) {
            active_buttons_ = 0;
        }

        // taking four cards is not allowed
        if (active_buttons_ == 4) {
            exchange_buttons_.get(active_buttons_).setEnabled(false);
        }

        return exchange_buttons_.get(active_buttons_);
    }

    // handle the card exchange buttons (-> to less cards in deck to exchange to much cards)
    public void handleExchangeButtons(int deck_size) {
        int max_cards_to_trade = 5;
        int last_button_idx = exchange_buttons_.size() - 1;

        if (deck_size >= max_cards_to_trade) {
            for (int i = last_button_idx; i > last_button_idx - max_cards_to_trade; i--) {
                exchange_buttons_.get(i).setEnabled(true);
            }
        }
        else {
            int cards_to_disable = (max_cards_to_trade - deck_size);
            for (int i = last_button_idx; i > last_button_idx - cards_to_disable; i--) {
                    exchange_buttons_.get(i).setEnabled(false);
            }
        }
    }
}
