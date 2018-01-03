package heroiceraser.mulatschak.game;

import java.util.List;
import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextButton;
import heroiceraser.mulatschak.game.DrawableObjects.Card;


//----------------------------------------------------------------------------------------------
//  TouchEvents
//
class TouchEvents {

    //----------------------------------------------------------------------------------------------
    // Constructor
    //
    TouchEvents() { }


    //----------------------------------------------------------------------------------------------
    // Down
    //
    void ActionDown(GameController controller, int X, int Y) {

        // enables a update thread for the canvas, in case there is no running update thread
        controller.getView().enableUpdateCanvasThreadOnly4TouchEvents();

        //------------------------



        //------------------ Play a Card -----------------------------------------------------------
        controller.getGamePlay().getPlayACard().getPlayACardLogic().touchActionDown(controller, X, Y);


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

        else if (controller.getGamePlay().getCardExchange().getCardExchangeLogic().isAnimationRunning() &&
        !controller.getNonGamePlayUIContainer().isAWindowActive()) {
            for (int i = 0; i < controller.getPlayerById(0).getAmountOfCardsInHand(); i++) {
                Card card = controller.getPlayerById(0).getHand().getCardAt(i);
                if (X >= card.getPosition().x &&
                        X < card.getPosition().x + card.getWidth() &&
                        Y >= card.getPosition().y &&
                        Y < card.getPosition().y + card.getHeight()) {
                    controller.getGamePlay().getCardExchange().getCardExchangeLogic().prepareCardExchange(card);
                }
            }
            ButtonActionDown(X, Y, controller.getGamePlay().getCardExchange().getCardExchangeLogic().getButton());
        }


        //------------------------

        //------------------- Trick Bids --------------------------------------------------------

        //  Buttons to make Trick Bids
        else if (controller.getGamePlay().getTrickBids().getMakeBidsAnimation().getAnimationNumbers() &&
                !controller.getNonGamePlayUIContainer().isAWindowActive()) {
            List<MyButton> buttons = controller.getGamePlay().getTrickBids().getMakeBidsAnimation().getNumberButtons();
            for (int i = 0; i < buttons.size(); i++) {
                ButtonActionDown(X, Y, buttons.get(i));
            }
        }


        //------------------- Choose Trump ---------------------------------------------------------

        //Buttons to choose the trump of the round
        else if (controller.getGamePlay().getChooseTrump().getChooseTrumpAnimation().getAnimationSymbols() &&
                !controller.getNonGamePlayUIContainer().isAWindowActive()) {
            List<MyButton> buttons = controller.getGamePlay().getChooseTrump().getChooseTrumpAnimation().getTrumpButtons();
            for (int i = 0; i < buttons.size(); i++) {
                ButtonActionDown(X, Y, buttons.get(i));
            }
        }

        // ------------------ MyPlayer Info --------------------------------------------------------

        // MyPlayer Info Buttons
        else if (!controller.getNonGamePlayUIContainer().isAWindowActive()) {
            if (ButtonActionDown(X, Y, controller.getPlayerInfo().getButtonLeft()) ) {}
            else if (ButtonActionDown(X, Y, controller.getPlayerInfo().getButtonTop()) ) {}
            else if (ButtonActionDown(X, Y, controller.getPlayerInfo().getButtonRight()) ) {}
        }

        //------------------------

        // ------------------ All Cards Played -----------------------------------------------------
        // MyPlayer Info Buttons
        if (ButtonActionDown(X, Y, controller.getNonGamePlayUIContainer()
                .getAllCardsPlayedView().getNextRoundButton()) ) {}
    }


    //----------------------------------------------------------------------------------------------
    // MOVE
    //
    void ActionMove(GameController controller, int X, int Y) {

        // ------------------ Play a Card ----------------------------------------------------------
        controller.getGamePlay().getPlayACard().getPlayACardLogic().touchActionMove(controller, X, Y);

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

        // ------------------ MyPlayer Info ----------------------------------------------------------

        // MyPlayer Info Buttons
        ButtonActionMove(X, Y, controller.getPlayerInfo().getButtonLeft());
        ButtonActionMove(X, Y, controller.getPlayerInfo().getButtonTop());
        ButtonActionMove(X, Y, controller.getPlayerInfo().getButtonRight());

        //------------------------

        //------------------- Card Exchange --------------------------------------------------------

        if (controller.getGamePlay().getCardExchange().getCardExchangeLogic().isAnimationRunning()) {
            ButtonActionMove(X, Y, controller.getGamePlay().getCardExchange().getCardExchangeLogic().getButton());
        }

        //------------------------

        //------------------- Trick Bids -----------------------------------------------------------

        if (controller.getGamePlay().getTrickBids().getMakeBidsAnimation().getAnimationNumbers()) {
            List<MyButton> buttons = controller.getGamePlay().getTrickBids().getMakeBidsAnimation().getNumberButtons();
            for (int i = 0; i < buttons.size(); i++) {
                if (controller.getGamePlay().getTrickBids().getMakeBidsAnimation().getAnimationNumbers()) {
                    ButtonActionMove(X, Y, buttons.get(i));
                }
            }
        }


        else if (controller.getGamePlay().getChooseTrump().getChooseTrumpAnimation().getAnimationSymbols()) {
            List<MyButton> buttons = controller.getGamePlay().getChooseTrump().getChooseTrumpAnimation().getTrumpButtons();
            for (int i = 0; i < buttons.size(); i++) {
                if (controller.getGamePlay().getChooseTrump().getChooseTrumpAnimation().getAnimationSymbols()) {
                    ButtonActionMove(X, Y, buttons.get(i));
                }
            }
        }

        // ------------------ All Cards Played -----------------------------------------------------
        // MyPlayer Info Buttons

        ButtonActionMove(X, Y, controller.getNonGamePlayUIContainer()
                .getAllCardsPlayedView().getNextRoundButton());

    }

    //----------------------------------------------------------------------------------------------
    // Action UP
    //
    void ActionUp(GameController controller, int X, int Y) {

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

        // ------------------ MyPlayer Info ----------------------------------------------------------

        // MyPlayer Info Buttons
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

        if (controller.getGamePlay().getTrickBids().getMakeBidsAnimation().getAnimationNumbers()) {
            List<MyButton> buttons = controller.getGamePlay().getTrickBids().getMakeBidsAnimation().getNumberButtons();
            for (int button_id = 0; button_id < buttons.size(); button_id++) {
                if (ButtonActionUp(X, Y, buttons.get(button_id))) {
                    controller.getGamePlay().getTrickBids().getMakeBidsAnimation().setTricks(controller, button_id);
                }
            }
        }

        else if (controller.getGamePlay().getChooseTrump().getChooseTrumpAnimation().getAnimationSymbols()) {
            List<MyButton> buttons = controller.getGamePlay().getChooseTrump().getChooseTrumpAnimation().getTrumpButtons();
            for (int i = 0; i < buttons.size(); i++) {
                if (ButtonActionUp(X, Y, buttons.get(i))) {
                    controller.getGamePlay().getChooseTrump().getChooseTrumpAnimation().setTrump(controller, i);
                }
            }
        }

        //------------------------

        //------------------- Card Exchange --------------------------------------------------------

        if (controller.getGamePlay().getCardExchange().getCardExchangeLogic().isAnimationRunning()) {
            if (ButtonActionUp(X, Y, controller.getGamePlay().getCardExchange().getCardExchangeLogic().getButton())) {
                controller.getGamePlay().getCardExchange().getCardExchangeLogic().exchangeCards(controller);
            }
        }
        //------------------------

        //------------------- Play a Card ----------------------------------------------------------
        controller.getGamePlay().getPlayACard().getPlayACardLogic().touchActionUp(controller);

        //------------------------


        // ------------------ All Cards Played -----------------------------------------------------
        // MyPlayer Info Buttons
        if (ButtonActionUp(X, Y, controller.getNonGamePlayUIContainer()
                    .getAllCardsPlayedView().getNextRoundButton()) ) {
            controller.getNonGamePlayUIContainer()
                    .getAllCardsPlayedView().getNextRoundButton().setVisible(false);
            controller.getNonGamePlayUIContainer().getStatistics().setVisible(false);
            controller.getNonGamePlayUIContainer().getTricks().setVisible(false);
            controller.getNonGamePlayUIContainer().getMenu().setVisible(false);
            controller.startRound();
        }

    }

    //-----------------------

    //----------------------------------------------------------------------------------------------
    // Button Actions
    //
    private boolean ButtonActionDown(int X, int Y, MyTextButton button) {
        if (button.isVisible() && button.IsEnabled() &&
                X >= button.getPosition().x && X < button.getPosition().x + button.getWidth() &&
                Y >= button.getPosition().y && Y < button.getPosition().y + button.getHeight()) {
            button.setPressed(true);
            return true;
        }
        return false;
    }
    private boolean ButtonActionDown(int X, int Y, MyButton button) {
        if (button.isVisible() && button.IsEnabled() &&
                X >= button.getPosition().x && X < button.getPosition().x + button.getWidth() &&
                Y >= button.getPosition().y && Y < button.getPosition().y + button.getHeight()) {
            button.setPressed(true);
            return true;
        }
        return false;
    }

    private void ButtonActionMove(int X, int Y, MyTextButton button) {
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

    private boolean ButtonActionUp(int X, int Y, MyTextButton button) {
        if (button.isVisible() && button.IsEnabled() && button.IsPressed()) {
            if (X >= button.getPosition().x && X < button.getPosition().x + button.getWidth() &&
                    Y >= button.getPosition().y && Y < button.getPosition().y + button.getHeight()) {
                button.setPressed(false);
                return true;
            }
        }
        return false;
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
