package heroiceraser.mulatschak.game;

import java.util.List;
import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextButton;
import heroiceraser.mulatschak.game.DrawableObjects.Card;


//----------------------------------------------------------------------------------------------
//  TouchEvents
//                  handles in game touch events (down/move/up)
//                  Action down enables update the canvas thread!
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
        // ------------------ ButtonBar Buttons ----------------------------------------------------
        controller.getNonGamePlayUIContainer().getButtonBar().touchEventDown(X, Y);

        // ------------------ Menu Button Bar Window -----------------------------------------------
        controller.getNonGamePlayUIContainer().getMenu().touchEventDown(X, Y);

        // ------------------ Chat Button Bar Window -----------------------------------------------
        controller.getNonGamePlayUIContainer().getChat().touchEventDown(X, Y);

        // ------------------ Tricks Button Bar Window ---------------------------------------------
        controller.getNonGamePlayUIContainer().getTricks().touchEventDown(X, Y);

        // ------------------ State of the Game Button Bar Window ----------------------------------
        controller.getNonGamePlayUIContainer().getStatistics().touchEventDown(X, Y);

        //------------------------
        // ------------------ Player Info ----------------------------------------------------------
        controller.getPlayerInfo().touchEventDown(X, Y, controller.getNonGamePlayUIContainer());



        //--------------------
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
        controller.getGamePlay().getPlayACardRound().getPlayACardLogic()
                .touchActionMove(controller, X, Y);

        //------------------------
        // ------------------ ButtonBar ------------------------------------------------------------
        controller.getNonGamePlayUIContainer().getButtonBar().touchEventMove(X, Y);

        // ------------------ Menu ButtonBar Window ------------------------------------------------
        controller.getNonGamePlayUIContainer().getMenu().touchEventMove(X, Y);

        // ------------------ Chat ButtonBar Window ------------------------------------------------
        controller.getNonGamePlayUIContainer().getChat().touchEventMove(X, Y);

        // ------------------ Tricks ButtonBar Window ----------------------------------------------
        controller.getNonGamePlayUIContainer().getTricks().touchEventMove(X, Y);

        // ------------------ State of the Game ButtonBar Window -----------------------------------
        controller.getNonGamePlayUIContainer().getStatistics().touchEventMove(X, Y);

        //------------------------
        // ------------------ Player Info ----------------------------------------------------------
        controller.getPlayerInfo().touchEventMove(X, Y, controller.getNonGamePlayUIContainer());



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


        //------------------------

        controller.getView().postInvalidateOnAnimation();

    }

    //----------------------------------------------------------------------------------------------
    // Action UP
    //
    void ActionUp(GameController controller, int X, int Y) {

        controller.getView().disableUpdateCanvasThreadOnly4TouchEvents();

        //------------------------
        // ------------------ ButtonBar ------------------------------------------------------------
        controller.getNonGamePlayUIContainer().getButtonBar()
                .touchEventUp(X, Y, controller.getNonGamePlayUIContainer());

        // ------------------ Menu Button Bar Window -----------------------------------------------
        controller.getNonGamePlayUIContainer().getMenu().touchEventUp(X, Y, controller);

        // ------------------ Chat -----------------------------------------------------------------
        controller.getNonGamePlayUIContainer().getChat().touchEventUp(X, Y, controller);

        // ------------------ ButtonBar Tricks Window ----------------------------------------------
        controller.getNonGamePlayUIContainer().getTricks().touchEventUp(X, Y);

        // ------------------ ButtonBar Tricks Window ----------------------------------------------
        controller.getNonGamePlayUIContainer().getStatistics().touchEventUp(X, Y);

        //------------------------
        // ------------------ Player Info ----------------------------------------------------------
        controller.getPlayerInfo().touchEventUp(X, Y, controller.getNonGamePlayUIContainer());


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
            controller.getNonGamePlayUIContainer().closeAllButtonBarWindows();
            controller.getView().postInvalidateOnAnimation();
            controller.startRound();
            return;
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
