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

        // Chat Button
        else if (ButtonActionDown(X, Y, controller.getNonGamePlayUIContainer()
                .getButtonBar().getChatButton()) ) {}

        // ------------------ ButtonBar Tricks Window ----------------------------------------------
        else if (ButtonActionDown(X, Y, controller.getNonGamePlayUIContainer()
                .getTricks().getArrowButtonLeft()) ) {}
        else if (ButtonActionDown(X, Y, controller.getNonGamePlayUIContainer()
                .getTricks().getArrowButtonRight()) ) {}

        // ------------------ Menu Button Bar Window -----------------------------------------------
        if (controller.getNonGamePlayUIContainer().getMenu().isVisible()) {
            controller.getNonGamePlayUIContainer().getMenu().touchEventDown(X, Y);
        }
        //------------------------
        // --------------------- Chat --------------------------------------------------------------
        controller.getNonGamePlayUIContainer().getChat().touchEventDown(X, Y, controller);


        // ------------------ All Cards Played -----------------------------------------------------
        // MyPlayer Info Buttons
        if (ButtonActionDown(X, Y, controller.getNonGamePlayUIContainer()
                .getAllCardsPlayedView().getNextRoundButton()) ) {}

        // ------------------ Game Over  -----------------------------------------------------------
        controller.getNonGamePlayUIContainer().getGameOver().touchEventDown(X, Y);

        // other UI elements not touchable while ButtonBar window is active
        if (controller.getNonGamePlayUIContainer().isAWindowActive()) {
            return;
        }





        //------------------- DecideMulatschak -----------------------------------------------------
        controller.getGamePlay().getDecideMulatschak().touchEventDown(X, Y);
        //------------------------


        //------------------- Card Exchange --------------------------------------------------------

        if (controller.getGamePlay().getCardExchange().getCardExchangeLogic().isAnimationRunning() &&
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
            List<MyTextButton> buttons = controller.getGamePlay().getTrickBids().getMakeBidsAnimation().getNumberButtons();
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

        // ------------------ Play A Card Round ----------------------------------------------------
        controller.getGamePlay().getPlayACardRound().touchEventDown(X, Y, controller);

        //------------------------

        controller.getView().postInvalidateOnAnimation();
    }


    //----------------------------------------------------------------------------------------------
    // MOVE
    //
    void ActionMove(GameController controller, int X, int Y) {

        // ------------------ Play a Card ----------------------------------------------------------
        controller.getGamePlay().getPlayACardRound().getPlayACardLogic().touchActionMove(controller, X, Y);

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

        // ChatView Button
        ButtonActionMove(X, Y, controller.getNonGamePlayUIContainer()
                .getButtonBar().getChatButton());
        
        //------------------------

        // ------------------ ButtonBar Tricks Window ----------------------------------------------
        ButtonActionMove(X, Y, controller.getNonGamePlayUIContainer()
                .getTricks().getArrowButtonLeft());
        ButtonActionMove(X, Y, controller.getNonGamePlayUIContainer()
                .getTricks().getArrowButtonRight());

        //------------------------


        // --------------------- Chat --------------------------------------------------------------
        controller.getNonGamePlayUIContainer().getChat().touchEventMove(X, Y);

        // ------------------ MyPlayer Info --------------------------------------------------------

        // MyPlayer Info Buttons
        ButtonActionMove(X, Y, controller.getPlayerInfo().getButtonLeft());
        ButtonActionMove(X, Y, controller.getPlayerInfo().getButtonTop());
        ButtonActionMove(X, Y, controller.getPlayerInfo().getButtonRight());

        //------------------------

        //------------------- DecideMulatschak -----------------------------------------------------
        controller.getGamePlay().getDecideMulatschak().touchEventMove(X, Y);
        //------------------------

        //------------------- Card Exchange --------------------------------------------------------

        if (controller.getGamePlay().getCardExchange().getCardExchangeLogic().isAnimationRunning()) {
            ButtonActionMove(X, Y, controller.getGamePlay().getCardExchange().getCardExchangeLogic().getButton());
        }

        //------------------------

        //------------------- Trick Bids -----------------------------------------------------------

        if (controller.getGamePlay().getTrickBids().getMakeBidsAnimation().getAnimationNumbers()) {
            List<MyTextButton> buttons = controller.getGamePlay().getTrickBids().getMakeBidsAnimation().getNumberButtons();
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

        // ------------------ Play A Card Round Ended ----------------------------------------------
        controller.getGamePlay().getPlayACardRound().touchEventMove(X, Y, controller);

        // ------------------ All Cards Played -----------------------------------------------------
        // MyPlayer Info Buttons

        ButtonActionMove(X, Y, controller.getNonGamePlayUIContainer()
                .getAllCardsPlayedView().getNextRoundButton());

        //-------------------

        // ------------------ Game Over  -----------------------------------------------------------
        controller.getNonGamePlayUIContainer().getGameOver().touchEventMove(X, Y);



        // ------------------ Menu Button Bar Window -----------------------------------------------
        if (controller.getNonGamePlayUIContainer().getMenu().isVisible()) {
            controller.getNonGamePlayUIContainer().getMenu().touchEventMove(X, Y);
        }
        //------------------------

        controller.getView().postInvalidateOnAnimation();

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
            controller.getNonGamePlayUIContainer().getChat().setVisible(false);
            controller.getNonGamePlayUIContainer().getTricks().setVisible(false);
            controller.getNonGamePlayUIContainer().getStatistics().switchVisibility();
        }

        // Tricks Button
        if (ButtonActionUp(X, Y, controller.getNonGamePlayUIContainer().getButtonBar().getTricksButton())) {
            controller.getNonGamePlayUIContainer().getMenu().setVisible(false);
            controller.getNonGamePlayUIContainer().getChat().setVisible(false);
            controller.getNonGamePlayUIContainer().getTricks().switchVisibility();
            controller.getNonGamePlayUIContainer().getStatistics().setVisible(false);
        }

        // Menu Button
        if (ButtonActionUp(X, Y, controller.getNonGamePlayUIContainer().getButtonBar().getMenuButton())) {
            controller.getNonGamePlayUIContainer().getMenu().switchVisibility();
            controller.getNonGamePlayUIContainer().getChat().setVisible(false);
            controller.getNonGamePlayUIContainer().getTricks().setVisible(false);
            controller.getNonGamePlayUIContainer().getStatistics().setVisible(false);
        }

        // ChatView Button
        if (ButtonActionUp(X, Y, controller.getNonGamePlayUIContainer().getButtonBar().getChatButton())) {
            controller.getNonGamePlayUIContainer().getMenu().setVisible(false);
            controller.getNonGamePlayUIContainer().getChat().switchVisibility();
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

        // --------------------- Chat --------------------------------------------------------------
        controller.getNonGamePlayUIContainer().getChat().touchEventUp(X, Y, controller);

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

        //------------------- DecideMulatschak -----------------------------------------------------
        controller.getGamePlay().getDecideMulatschak().touchEventUp(X, Y, controller);
        //------------------------

        //------------------- Trick Bids -----------------------------------------------------------

        if (controller.getGamePlay().getTrickBids().getMakeBidsAnimation().getAnimationNumbers()) {
            List<MyTextButton> buttons = controller.getGamePlay().getTrickBids().getMakeBidsAnimation().getNumberButtons();
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
        controller.getGamePlay().getPlayACardRound().getPlayACardLogic().touchActionUp(controller);

        // ------------------ Play A Card Round Ended ----------------------------------------------
        controller.getGamePlay().getPlayACardRound().touchEventUp(X, Y, controller);

        // ------------------ All Cards Played -----------------------------------------------------
        // MyPlayer Info Buttons
        if (ButtonActionUp(X, Y, controller.getNonGamePlayUIContainer()
                    .getAllCardsPlayedView().getNextRoundButton()) ) {
            controller.getNonGamePlayUIContainer()
                    .getAllCardsPlayedView().getNextRoundButton().setVisible(false);
            controller.getNonGamePlayUIContainer().getStatistics().setVisible(false);
            controller.getNonGamePlayUIContainer().getTricks().setVisible(false);
            controller.getNonGamePlayUIContainer().getMenu().setVisible(false);
            controller.getView().postInvalidateOnAnimation();
            controller.startRound();
        }

        //-------------------

        // ------------------ Menu Button Bar Window -----------------------------------------------
        if (controller.getNonGamePlayUIContainer().getMenu().isVisible()) {
            controller.getNonGamePlayUIContainer().getMenu().touchEventUp(X, Y, controller);
        }

        //------------------------

        controller.getView().postInvalidateOnAnimation();

        // ------------------ Game Over  -----------------------------------------------------------
        controller.getNonGamePlayUIContainer().getGameOver().touchEventUp(X, Y, controller);
        // nothing behind!!
    }

    //-----------------------

    //----------------------------------------------------------------------------------------------
    // Button Actions
    //
    private boolean ButtonActionDown(int X, int Y, MyTextButton button) {
        if (button.isVisible() && button.isEnabled() &&
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
        if (button.isVisible() && button.isEnabled() && button.isPressed()) {
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
        if (button.isVisible() && button.isEnabled() && button.isPressed()) {
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
