package heroiceraser.mulatschak.game.Animations;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.Button;
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
    private List<Button> exchange_buttons_;
    private int active_buttons_;


    public CardExchange(GameView view) {
        animation_running_ = false;
        active_buttons_ = 0;
        help_text_ = new HelpText();
        exchange_buttons_ = new ArrayList<>();
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
            Button button = new Button();
            int id = i;
            if (i == 4) {
                id = i + 1;
            }
            button.init(view, position, width, height, "button_" + id + "_karten");
            exchange_buttons_.add(button);
        }
    }

    public void draw(Canvas canvas) {
        exchange_buttons_.get(active_buttons_).draw(canvas);
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

        animation_running_ = false;
        active_buttons_ = 0;

        List<Card> move_cards = new ArrayList<>();
        CardStack hand = controller.getPlayerById(0).getHand();

        // removing cards from hand
        for (int i = 0; i < hand.getCardStack().size(); i++) {
            if (hand.getCardAt(i).getPosition().equals(hand.getCardAt(i).getFixedPosition())) {
                continue;
            }
            else {
                move_cards.add(hand.getCardAt(i));
                hand.getCardStack().remove(i);
                i--;
            }
        }

        // adding new cards to hand
        for (int i = 0; i < move_cards.size(); i++) {
            controller.drawCard(0, controller.getDeck());
            Card card = controller.getPlayerById(0).getHand().getCardAt(controller.getPlayerById(0)
                    .getAmountOfCardsInHand() - 1);
            card.setPosition(move_cards.get(i).getFixedPosition());
            card.setFixedPosition(card.getPosition());
        }

        // add old cards to trash
        for (int i = 0; i < move_cards.size(); i++) {
            controller.getTrash().addCard(move_cards.get(i));
        }

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

    public Button getButton() {
        if (active_buttons_ < 0) {
            active_buttons_ = 0;
        }
        return exchange_buttons_.get(active_buttons_);
    }
}
