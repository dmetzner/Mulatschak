package heroiceraser.mulatschak.game;


import android.graphics.Point;

import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.Button;
import heroiceraser.mulatschak.game.DrawableObjects.Card;
import heroiceraser.mulatschak.game.DrawableObjects.CardStack;

/**
 * Created by Daniel Metzner on 14.08.2017.
 */

public class TouchEvents {

    private int move_card_;

    //----------------------------------------------------------------------------------------------
    // Constructor
    //
    public TouchEvents() {
        move_card_ = -1;
    }

    //----------------------------------------------------------------------------------------------
    // Down
    //
    public void ActionDown(GameController controller, int X, int Y) {

        // enables a update thread for the canvas, in case there is no running update thread
        controller.getView().enableUpdateCanvasThreadOnly4TouchEvents();

        if (controller.waiting) {
            // ToDO Just 4 now Hack, better solution please :D
            controller.waiting = false;
            controller.endCardRound();
        }

        //------------------------

        //------------------ Play a Card -----------------------------------------------------------
        // moves the card with the touch movement
        if (controller.getAnimation().getCardAnimations().getCardMovable() &&
                controller.getLogic().getTurn() == 0 && move_card_ < 0) {
            for (int i = 0; i < controller.getPlayerById(0).getAmountOfCardsInHand(); i++) {
                Card card = controller.getPlayerById(0).getHand().getCardAt(i);
                if (card.getFixedPosition() == null) {
                    break;
                }
                if (X >= card.getFixedPosition().x &&
                        X < card.getFixedPosition().x + controller.getLayout().getCardWidth() &&
                        Y >= card.getFixedPosition().y &&
                        Y < card.getFixedPosition().y +  controller.getLayout().getCardHeight()) {
                    move_card_ = i;
                    break;
                } else {
                    move_card_ = -1;
                }
            }
        }


        // ------------------ ButtonBar ------------------------------------------------------------

        // Statistic Button
        ButtonActionDown(X, Y, controller.getButtonBar().getStatisticsButton());

        // Tricks Button
        ButtonActionDown(X, Y, controller.getButtonBar().getTricksButton());

        // Menu Button
        ButtonActionDown(X, Y, controller.getButtonBar().getMenuButton());

        //------------------------

        //------------------- Card Exchange --------------------------------------------------------

        if (controller.getAnimation().getCardExchange().isAnimationRunning()) {
            for (int i = 0; i < controller.getPlayerById(0).getAmountOfCardsInHand(); i++) {
                Card card = controller.getPlayerById(0).getHand().getCardAt(i);
                if (X >= card.getPosition().x &&
                        X < card.getPosition().x + card.getWidth() &&
                        Y >= card.getPosition().y &&
                        Y < card.getPosition().y + card.getHeight()) {
                    controller.getAnimation().getCardExchange().prepareCardExchange(card);
                }
            }
            ButtonActionDown(X, Y, controller.getAnimation().getCardExchange().getButton());
        }


        //------------------------

        //------------------- Trick Bids --------------------------------------------------------

        //  Buttons to make Trick Bids
        if (controller.getAnimation().getTrickBids().getAnimationNumbers()) {
            List<Button> buttons = controller.getAnimation().getTrickBids().getNumberButtons();
            for (int i = 0; i < buttons.size(); i++) {
                ButtonActionDown(X, Y, buttons.get(i));
            }
        }
        // Buttons to choose the trump of the round
        else if (controller.getAnimation().getTrickBids().getAnimationSymbols()) {
            List<Button> buttons = controller.getAnimation().getTrickBids().getTrumpButtons();
            for (int i = 0; i < buttons.size(); i++) {
                ButtonActionDown(X, Y, buttons.get(i));
            }
        }

    }


    //----------------------------------------------------------------------------------------------
    // MOVE
    //
    public void ActionMove(GameController controller, int X, int Y) {


        //------------------- Play a Card ----------------------------------------------------------

        if (move_card_ >= 0) {
            controller.getPlayerById(0).getHand().getCardAt(move_card_).setPosition(
                    X -  controller.getLayout().getCardWidth() / 2,
                    Y -  controller.getLayout().getCardHeight() / 2);
        }

        //------------------------

        // ------------------ ButtonBar ------------------------------------------------------------

        // Statistic Button
        ButtonActionMove(X, Y, controller.getButtonBar().getStatisticsButton());

        // Tricks Button
        ButtonActionMove(X, Y, controller.getButtonBar().getTricksButton());

        // Menu Button
        ButtonActionMove(X, Y, controller.getButtonBar().getMenuButton());

        //------------------------

        //------------------- Card Exchange --------------------------------------------------------

        if (controller.getAnimation().getCardExchange().isAnimationRunning()) {
            ButtonActionMove(X, Y, controller.getAnimation().getCardExchange().getButton());
        }

        //------------------------

        //------------------- Trick Bids -----------------------------------------------------------

        if (controller.getAnimation().getTrickBids().getAnimationNumbers()) {
            List<Button> buttons = controller.getAnimation().getTrickBids().getNumberButtons();
            for (int i = 0; i < buttons.size(); i++) {
                if (controller.getAnimation().getTrickBids().getAnimationNumbers()) {
                    ButtonActionMove(X, Y, buttons.get(i));
                }
            }
        }
        else if (controller.getAnimation().getTrickBids().getAnimationSymbols()) {
            List<Button> buttons = controller.getAnimation().getTrickBids().getTrumpButtons();
            for (int i = 0; i < buttons.size(); i++) {
                if (controller.getAnimation().getTrickBids().getAnimationSymbols()) {
                    ButtonActionMove(X, Y, buttons.get(i));
                }
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    // Action UP
    //
    public void ActionUp(GameController controller, int X, int Y) {

        controller.getView().disableUpdateCanvasThreadOnly4TouchEvents();

        // ----------------------- ButtonBar -------------------------------------------------------

        // Statistic Button
        if (ButtonActionUp(X, Y, controller.getButtonBar().getStatisticsButton())) {
            controller.getMenu().setVisible(false);
            controller.getTricks().setVisible(false);
            controller.getStatistics().switchVisibility();
        }

        // Tricks Button
        if (ButtonActionUp(X, Y, controller.getButtonBar().getTricksButton())) {
            controller.getMenu().setVisible(false);
            controller.getTricks().switchVisibility();
            controller.getStatistics().setVisible(false);
        }

        // Menu Button
        if (ButtonActionUp(X, Y, controller.getButtonBar().getMenuButton())) {
            controller.getMenu().switchVisibility();
            controller.getTricks().setVisible(false);
            controller.getStatistics().setVisible(false);
        }

        //------------------------

        //------------------- Trick Bids -----------------------------------------------------------

        if (controller.getAnimation().getTrickBids().getAnimationNumbers()) {
            List<Button> buttons = controller.getAnimation().getTrickBids().getNumberButtons();
            for (int button_id = 0; button_id < buttons.size(); button_id++) {
                if (ButtonActionUp(X, Y, buttons.get(button_id))) {
                    controller.getAnimation().getTrickBids().setTricks(controller, button_id);
                }
            }
        }
        else if (controller.getAnimation().getTrickBids().getAnimationSymbols()) {
            List<Button> buttons = controller.getAnimation().getTrickBids().getTrumpButtons();
            for (int i = 0; i < buttons.size(); i++) {
                if (ButtonActionUp(X, Y, buttons.get(i))) {
                    controller.getAnimation().getTrickBids().setTrump(controller, i);
                }
            }
        }
        //------------------------

        //------------------- Card Exchange --------------------------------------------------------

        if (controller.getAnimation().getCardExchange().isAnimationRunning()) {
            if (ButtonActionUp(X, Y, controller.getAnimation().getCardExchange().getButton())) {
                controller.getAnimation().getCardExchange().exchangeCards(controller);
            }
        }
        //------------------------

        //------------------- Play a Card ----------------------------------------------------------
        if (move_card_ >= 0) {
            CardStack hand =  controller.getPlayerById(0).getHand();
            int card_y = hand.getCardAt(move_card_).getPosition().y;
            int fixed_y = hand.getCardAt(move_card_).getFixedPosition().y;

            boolean valid = controller.getLogic().isAValidCardPlay(hand.getCardAt(move_card_), hand, controller.getDiscardPile());

            if (card_y > fixed_y -  controller.getLayout().getCardHeight() * 1.5) {
                controller.getAnimation().getCardAnimations().returnCardToHand(
                        controller.getPlayerById(0).getHand().getCardAt(move_card_));

            }
            else if (!valid) {
                controller.getAnimation().getCardAnimations().returnCardToHand(
                        controller.getPlayerById(0).getHand().getCardAt(move_card_));
            }
            else if (valid) {
                controller.getPlayerById(0).getHand().getCardAt(move_card_).setPosition(
                        new Point(controller.getDiscardPile().getPoint(0)) );

                controller.getDiscardPile().setCardBottom(
                        controller.getPlayerById(0).getHand().getCardAt(move_card_));

                controller.getPlayerById(0).getHand().getCardStack().remove(move_card_);

                // recalculate hand positions!
                controller.getAnimation().getReAnimateHands()
                        .redrawHands(controller.getLayout(), controller.getPlayerById(0));

                // give turn to next player
                controller.getLogic().turnToNextPlayer(controller.getAmountOfPlayers());
                controller.nextTurn();

            }
            move_card_ = -1;
        }

        //------------------------
    }

    //-----------------------

    //----------------------------------------------------------------------------------------------
    // Button Actions
    //
    private void ButtonActionDown(int X, int Y, Button button) {
        if (button.isVisible() && button.IsEnabled() &&
                X >= button.getPosition().x && X < button.getPosition().x + button.getWidth() &&
                Y >= button.getPosition().y && Y < button.getPosition().y + button.getHeight()) {
            button.setPressed(true);
        }
    }

    private void ButtonActionMove(int X, int Y, Button button) {
        if (button.isVisible() && button.IsEnabled() && button.IsPressed()) {
            if ( X >= button.getPosition().x && X < button.getPosition().x + button.getWidth() &&
                    Y >= button.getPosition().y && Y < button.getPosition().y + button.getHeight()) {
                button.setPressed(true);
            }
            else {
                button.setPressed(false);
            }
        }
    }

    private boolean ButtonActionUp(int X, int Y, Button button) {
        if (button.isVisible() && button.IsEnabled() && button.IsPressed()) {
            if (X >= button.getPosition().x && X < button.getPosition().x + button.getWidth() &&
                    Y >= button.getPosition().y && Y < button.getPosition().y + button.getHeight()) {
                button.setPressed(false);
                return true;
            }
        }
        return false;
    }

}
