package heroiceraser.mulatschak.game;


import android.content.Context;
import android.graphics.Point;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.DrawableObjects.Card;
import heroiceraser.mulatschak.game.DrawableObjects.CardStack;

/**
 * Created by Daniel Metzner on 14.08.2017.
 */

public class TouchEvents {


    //----------------------------------------------------------------------------------------------
    // Constructor
    //
    public TouchEvents() {

    }

    //----------------------------------------------------------------------------------------------
    // Down
    //
    public void ActionDown(GameController controller, int X, int Y) {

        // enables a update thread for the canvas, in case there is no running update thread
        controller.getView().enableUpdateCanvasThreadOnly4TouchEvents();

        if (controller.waiting2) {
            // ToDO Just 4 now Hack, better solution please :D
            controller.waiting2 = false;
            controller.startRound();
        }

        //------------------------

        //------------------ Play a Card -----------------------------------------------------------
        controller.getGamePlay().getPlayACard().touchActionDown(controller, X, Y);


        // ------------------ ButtonBar ------------------------------------------------------------

        // Statistic Button
        if (ButtonActionDown(X, Y, controller.getNonGamePlayUIContainer()
                .getButtonBar().getStatisticsButton()) )  {}

        // Tricks Button
        else if (ButtonActionDown(X, Y, controller.getNonGamePlayUIContainer()
                .getButtonBar().getTricksButton()) ) {}

        // Menu Button
        else if (ButtonActionDown(X, Y, controller.getNonGamePlayUIContainer()
                .getButtonBar().getMenuButton()) ) {}

        //------------------------

        // ------------------ ButtonBar Tricks Window ----------------------------------------------
        else if (ButtonActionDown(X, Y, controller.getNonGamePlayUIContainer()
                .getTricks().getArrowButtonLeft()) ) {}
        else if (ButtonActionDown(X, Y, controller.getNonGamePlayUIContainer()
                .getTricks().getArrowButtonRight()) ) {}

        //------------------------


        //------------------- Card Exchange --------------------------------------------------------

        else if (controller.getAnimation().getCardExchange().isAnimationRunning() &&
        !controller.getNonGamePlayUIContainer().isAWindowActive()) {
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
        else if (controller.getAnimation().getTrickBids().getAnimationNumbers() &&
                !controller.getNonGamePlayUIContainer().isAWindowActive()) {
            List<MyButton> buttons = controller.getAnimation().getTrickBids().getNumberButtons();
            for (int i = 0; i < buttons.size(); i++) {
                ButtonActionDown(X, Y, buttons.get(i));
            }
        }
        // Buttons to choose the trump of the round
        else if (controller.getAnimation().getTrickBids().getAnimationSymbols() &&
                !controller.getNonGamePlayUIContainer().isAWindowActive()) {
            List<MyButton> buttons = controller.getAnimation().getTrickBids().getTrumpButtons();
            for (int i = 0; i < buttons.size(); i++) {
                ButtonActionDown(X, Y, buttons.get(i));
            }
        }

        // ------------------ Player Info ----------------------------------------------------------

        // Player Info Buttons
        else if (!controller.getNonGamePlayUIContainer().isAWindowActive()) {
            if (ButtonActionDown(X, Y, controller.getPlayerInfo().getButtonLeft()) ) {}
            else if (ButtonActionDown(X, Y, controller.getPlayerInfo().getButtonTop()) ) {}
            else if (ButtonActionDown(X, Y, controller.getPlayerInfo().getButtonRight()) ) {}
        }


        //------------------------

    }


    //----------------------------------------------------------------------------------------------
    // MOVE
    //
    public void ActionMove(GameController controller, int X, int Y) {

        // ------------------ Play a Card ----------------------------------------------------------
        controller.getGamePlay().getPlayACard().touchActionMove(controller, X, Y);

        // ------------------ ButtonBar ------------------------------------------------------------

        // Statistic Button
        ButtonActionMove(X, Y, controller.getNonGamePlayUIContainer()
                .getButtonBar().getStatisticsButton());

        // Tricks Button
        ButtonActionMove(X, Y, controller.getNonGamePlayUIContainer()
                .getButtonBar().getTricksButton());

        // Menu Button
        ButtonActionMove(X, Y, controller.getNonGamePlayUIContainer()
                .getButtonBar().getMenuButton());

        //------------------------

        // ------------------ ButtonBar Tricks Window ----------------------------------------------
        ButtonActionMove(X, Y, controller.getNonGamePlayUIContainer()
                .getTricks().getArrowButtonLeft());
        ButtonActionMove(X, Y, controller.getNonGamePlayUIContainer()
                .getTricks().getArrowButtonRight());

        //------------------------

        // ------------------ Player Info ----------------------------------------------------------

        // Player Info Buttons
        ButtonActionMove(X, Y, controller.getPlayerInfo().getButtonLeft());
        ButtonActionMove(X, Y, controller.getPlayerInfo().getButtonTop());
        ButtonActionMove(X, Y, controller.getPlayerInfo().getButtonRight());

        //------------------------

        //------------------- Card Exchange --------------------------------------------------------

        if (controller.getAnimation().getCardExchange().isAnimationRunning()) {
            ButtonActionMove(X, Y, controller.getAnimation().getCardExchange().getButton());
        }

        //------------------------

        //------------------- Trick Bids -----------------------------------------------------------

        if (controller.getAnimation().getTrickBids().getAnimationNumbers()) {
            List<MyButton> buttons = controller.getAnimation().getTrickBids().getNumberButtons();
            for (int i = 0; i < buttons.size(); i++) {
                if (controller.getAnimation().getTrickBids().getAnimationNumbers()) {
                    ButtonActionMove(X, Y, buttons.get(i));
                }
            }
        }
        else if (controller.getAnimation().getTrickBids().getAnimationSymbols()) {
            List<MyButton> buttons = controller.getAnimation().getTrickBids().getTrumpButtons();
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
        if (ButtonActionUp(X, Y, controller.getNonGamePlayUIContainer().getButtonBar().getStatisticsButton())) {
            controller.getNonGamePlayUIContainer().getMenu().setVisible(false);
            controller.getNonGamePlayUIContainer().getTricks().setVisible(false);
            controller.getNonGamePlayUIContainer().getStatistics().switchVisibility();
        }

        // Tricks Button
        if (ButtonActionUp(X, Y, controller.getNonGamePlayUIContainer().getButtonBar().getTricksButton())) {
            controller.getNonGamePlayUIContainer().getMenu().setVisible(false);
            controller.getNonGamePlayUIContainer().getTricks().switchVisibility();
            controller.getNonGamePlayUIContainer().getStatistics().setVisible(false);
        }

        // Menu Button
        if (ButtonActionUp(X, Y, controller.getNonGamePlayUIContainer().getButtonBar().getMenuButton())) {
            controller.getNonGamePlayUIContainer().getMenu().switchVisibility();
            controller.getNonGamePlayUIContainer().getTricks().setVisible(false);
            controller.getNonGamePlayUIContainer().getStatistics().setVisible(false);
        }

        //------------------------

        // ------------------ ButtonBar Tricks Window ----------------------------------------------
        if (ButtonActionUp(X, Y, controller.getNonGamePlayUIContainer()
                .getTricks().getArrowButtonLeft())) {
            controller.getNonGamePlayUIContainer().getTricks().showPrevRound();
        }
        if (ButtonActionUp(X, Y, controller.getNonGamePlayUIContainer()
                .getTricks().getArrowButtonRight())) {
            controller.getNonGamePlayUIContainer().getTricks().showNextRound();
        }

        //------------------------

        // ------------------ Player Info ----------------------------------------------------------

        // Player Info Buttons
        if (ButtonActionUp(X, Y, controller.getPlayerInfo().getButtonLeft())) {
            controller.getPlayerInfo().popUpInfoLeft();
        }
        else if (ButtonActionUp(X, Y, controller.getPlayerInfo().getButtonTop())) {
            controller.getPlayerInfo().popUpInfoTop();
        }
        else if (ButtonActionUp(X, Y, controller.getPlayerInfo().getButtonRight())) {
            controller.getPlayerInfo().popUpInfoRight();
        }

        //------------------------
        //------------------- Trick Bids -----------------------------------------------------------

        if (controller.getAnimation().getTrickBids().getAnimationNumbers()) {
            List<MyButton> buttons = controller.getAnimation().getTrickBids().getNumberButtons();
            for (int button_id = 0; button_id < buttons.size(); button_id++) {
                if (ButtonActionUp(X, Y, buttons.get(button_id))) {
                    controller.getAnimation().getTrickBids().setTricks(controller, button_id);
                }
            }
        }
        else if (controller.getAnimation().getTrickBids().getAnimationSymbols()) {
            List<MyButton> buttons = controller.getAnimation().getTrickBids().getTrumpButtons();
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
        controller.getGamePlay().getPlayACard().touchActionUp(controller);

        //------------------------
    }

    //-----------------------

    //----------------------------------------------------------------------------------------------
    // Button Actions
    //
    private boolean ButtonActionDown(int X, int Y, MyButton button) {
        if (button.isVisible() && button.IsEnabled() &&
                X >= button.getPosition().x && X < button.getPosition().x + button.getWidth() &&
                Y >= button.getPosition().y && Y < button.getPosition().y + button.getHeight()) {
            button.setPressed(true);
            return true;
        }
        return false;
    }

    private void ButtonActionMove(int X, int Y, MyButton button) {
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

    private boolean ButtonActionUp(int X, int Y, MyButton button) {
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
