package heroiceraser.mulatschak.game;


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

    private final Object lock = new Object();

    //----------------------------------------------------------------------------------------------
    // Down
    //
    synchronized void ActionDown(GameController controller, int X, int Y) {

        synchronized (lock) {
            // enables a update thread for the canvas, in case there is no running update thread
            controller.getView().enableUpdateCanvasThreadOnly4TouchEvents();
            //------------------------
            controller.getView().postInvalidateOnAnimation();

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

            //--------------------
            // ------------------ All Cards Played -> next Round button --------------------------------
            controller.getNonGamePlayUIContainer().getAllCardsPlayedView().touchEventDown(X, Y);

            //------------------------
            // ------------------ Game Over  -----------------------------------------------------------
            controller.getNonGamePlayUIContainer().getGameOver().touchEventDown(X, Y);

            // X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X
            // touchBreak:
            //             elements under this line not touchable while ButtonBar window is active
            if (controller.getNonGamePlayUIContainer().isAWindowActive()) { return; }
            // X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X

            //------------------------
            // ------------------ Player Info ----------------------------------------------------------
            controller.getPlayerInfo().touchEventDown(X, Y);


            //~~~~~~~~~~~~~~~~~~~~~~~~
            // ~~~~~~~~~~~~~~~~~~ GAME PLAY ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

            //------------------------
            //------------------- DecideMulatschak -----------------------------------------------------
            controller.getGamePlay().getDecideMulatschak().touchEventDown(X, Y);

            //------------------------
            //------------------- Trick Bids -----------------------------------------------------------
            controller.getGamePlay().getTrickBids().getMakeBidsAnimation().touchEventDown(X, Y);

            //------------------------
            //------------------- Choose Trump ---------------------------------------------------------
            controller.getGamePlay().getChooseTrump().getChooseTrumpAnimation().touchEventDown(X, Y);

            //------------------------
            //------------------- Card Exchange --------------------------------------------------------
            controller.getGamePlay().getCardExchange().touchEventDown(X, Y, controller);

            //------------------------
            // ------------------ Play A Card Round ----------------------------------------------------
            controller.getGamePlay().getPlayACardRound().touchEventDown(X, Y, controller);
        }

    }


    //----------------------------------------------------------------------------------------------
    // MOVE
    //

    synchronized void ActionMove(GameController controller, int X, int Y) {

        synchronized (lock) {
            //------------------------
            controller.getView().postInvalidateOnAnimation();

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

            //-------------------
            // ------------------ Game Over  -----------------------------------------------------------
            controller.getNonGamePlayUIContainer().getGameOver().touchEventMove(X, Y);

            //------------------------
            // ------------------ All Cards Played -> next Round button --------------------------------
            controller.getNonGamePlayUIContainer().getAllCardsPlayedView().touchEventMove(X, Y);

            // X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X
            // touchBreak:
            //             elements under this line not touchable while ButtonBar window is active
            if (controller.getNonGamePlayUIContainer().isAWindowActive()) {
                return;
            }
            // X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X

            //------------------------
            // ------------------ Player Info ----------------------------------------------------------
            controller.getPlayerInfo().touchEventMove(X, Y);


            //~~~~~~~~~~~~~~~~~~~~~~~~
            // ~~~~~~~~~~~~~~~~~~ GAME PLAY ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

            //------------------------
            //------------------- DecideMulatschak -----------------------------------------------------
            controller.getGamePlay().getDecideMulatschak().touchEventMove(X, Y);

            //------------------------
            //------------------- Trick Bids -----------------------------------------------------------
            controller.getGamePlay().getTrickBids().getMakeBidsAnimation().touchEventMove(X, Y);

            //------------------------
            //------------------- Choose Trump ---------------------------------------------------------
            controller.getGamePlay().getChooseTrump().getChooseTrumpAnimation().touchEventMove(X, Y);

            //------------------------
            //------------------- Card Exchange --------------------------------------------------------
            controller.getGamePlay().getCardExchange().touchEventMove(X, Y);

            //------------------------
            // ------------------ Play A Card Round Ended ----------------------------------------------
            controller.getGamePlay().getPlayACardRound().touchEventMove(X, Y, controller);
        }
    }

    //----------------------------------------------------------------------------------------------
    // Action UP
    //
    synchronized void ActionUp(GameController controller, int X, int Y) {

        synchronized (lock) {
            controller.getView().disableUpdateCanvasThreadOnly4TouchEvents();
            //------------------------
            controller.getView().postInvalidateOnAnimation();

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
            // ------------------ All Cards Played -> next Round button --------------------------------
            controller.getNonGamePlayUIContainer()
                    .getAllCardsPlayedView().touchEventUp(X, Y, controller);

            // -----------------------
            // ------------------ Game Over  -----------------------------------------------------------
            if (controller.getNonGamePlayUIContainer().getGameOver().touchEventUp(X, Y, controller)) {
                return; // destroys game! better just do nothing after clicked end game button
            }

            // X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X
            // touchBreak:
            //             elements under this line not touchable while ButtonBar window is active
            if (controller.getNonGamePlayUIContainer().isAWindowActive()) {
                return;
            }
            // X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X

            //------------------------
            // ------------------ Player Info ----------------------------------------------------------
            controller.getPlayerInfo().touchEventUp(X, Y);


            //~~~~~~~~~~~~~~~~~~~~~~~~
            // ~~~~~~~~~~~~~~~~~~ GAME PLAY ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

            //------------------------
            //------------------- DecideMulatschak -----------------------------------------------------
            controller.getGamePlay().getDecideMulatschak().touchEventUp(X, Y, controller);

            //------------------------
            //------------------- Trick Bids -----------------------------------------------------------
            controller.getGamePlay().getTrickBids().getMakeBidsAnimation()
                    .touchEventUp(X, Y, controller);

            //------------------------
            //------------------- Choose Trump ---------------------------------------------------------
            controller.getGamePlay().getChooseTrump().getChooseTrumpAnimation()
                    .touchEventUp(X, Y, controller);

            //------------------------
            //------------------- Card Exchange --------------------------------------------------------
            controller.getGamePlay().getCardExchange().touchEventUp(X, Y, controller);

            //------------------------
            // ------------------ Play A Card Round Ended ----------------------------------------------
            controller.getGamePlay().getPlayACardRound().touchEventUp(X, Y, controller);
        }
    }
}
