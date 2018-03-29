package heroiceraser.mulatschak.game.GamePlay.TrickBids;

import android.graphics.Canvas;
import android.graphics.Point;
import java.util.ArrayList;
import java.util.List;
import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.BaseObjects.CardStack;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GamePlay.Background4Player0Animations;
import heroiceraser.mulatschak.game.GameView;


//----------------------------------------------------------------------------------------------
//  MakeBidsAnimation Class
//
public class MakeBidsAnimation {

    //----------------------------------------------------------------------------------------------
    //  Constants
    //
    public static int MAX_BID_COLS = 3;
    public static int MULATSCHAK = 6;
    static final int MISS_A_TURN = -1;

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private Background4Player0Animations background;
    private List<MyButton> numberButtons;
    private boolean animatingNumbers;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    MakeBidsAnimation() {
        background = new Background4Player0Animations();
        animatingNumbers = false;
        numberButtons = new ArrayList<>();
    }

    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();

        background.init(layout);

        int x = layout.getTrickBidsNumberButtonPosition().x;
        int y = layout.getTrickBidsNumberButtonPosition().y;
        int width =  layout.getTrickBidsNumberButtonSize().x;
        int height = layout.getTrickBidsNumberButtonSize().y;

        MyButton missATurnButton = new MyButton();
        String missATurnText = view.getResources().getString(R.string.miss_a_turn_button);
        missATurnButton.init(view, new Point(x, y), width * 3, height,
                "button_blue_metallic_large", missATurnText);
        numberButtons.add(missATurnButton);

        // offset
        width -= layout.getOnePercentOfScreenWidth() * 0.5f;

        for (int i = 0; i <= GameLogic.MAX_CARDS_PER_HAND; i++) {
            int newX = x;
            int newY = y;
            newX += width * (i % MAX_BID_COLS);
            // offset
            newX += (layout.getOnePercentOfScreenWidth() * 0.75f * (i % MAX_BID_COLS));
            newY += height * ((i / MAX_BID_COLS) + 1);
            MyButton numberButton = new MyButton();
            Point position = new Point(newX, newY);
            numberButton.init(view, position, width, height,
                    "button_blue_metallic_small", i + "");
            numberButtons.add(numberButton);
        }

    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public synchronized void draw(Canvas canvas, GameController controller) {
        if (animatingNumbers) {
            background.draw(canvas);
            controller.getPlayerHandsView().drawPlayer0Hand(canvas, controller);

            for (MyButton button : numberButtons) {
                button.draw(canvas);
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    //  clear Hand
    //                 -> moves all player 0 hand cards to the trash
    //
    void clearHand(GameController controller, int playerId) {
        CardStack hand = controller.getPlayerById(playerId).getHand();
        for (int i = 0; i < hand.getCardStack().size(); i++) {
            hand.getCardAt(i).setPosition(controller.getLayout().getDeckPosition());
            hand.getCardAt(i).setFixedPosition(controller.getLayout().getDeckPosition());
            controller.moveCardToTrash(hand.getCardAt(i));
            hand.getCardStack().remove(i);
            i--;
        }
    }


    //----------------------------------------------------------------------------------------------
    //  reEnableButtons
    //
    void reEnableButtons(GameController controller) {
        for (int i = 0; i < numberButtons.size(); i++) {
            // there have to be more than 2 players to pass this round
            // also it is not possible to miss a turn if the last round was missed
            if (i == 0 && (controller.getAmountOfPlayers() <= 2 ||
                            controller.getPlayerByPosition(0).getMissATurn() ) ) {
                numberButtons.get(i).setEnabled(false);
            }
            else {
                numberButtons.get(i).setEnabled(true);
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    //  prepareAnimation
    //                          -> checks if there are enough player to enable skipp round button
    //
    void prepareAnimationButtons(GameController controller) {

        // miss a turn handler
        int players = controller.getAmountOfPlayers();
        for (MyPlayer player : controller.getPlayerList()) {
            if (player.getMissATurn()) {
                players--;
            }
        }
        if (players <= 2) {
            numberButtons.get(0).setEnabled(false);
        }

        // always possible to make 0 tricks
        numberButtons.get(1).setEnabled(true);

        for (int i = 2; i < numberButtons.size(); i++) {
            // button
            if ((i - 1) > controller.getLogic().getTricksToMake()) {
                numberButtons.get(i).setEnabled(true);
            }

            // except dealer can outbid with same amount
            else if ((i - 1) == controller.getLogic().getTricksToMake() &&
                    controller.getLogic().getDealer() == 0) {
                numberButtons.get(i).setEnabled(true);
            }
            // disable rest
            else {
                numberButtons.get(i).setEnabled(false);
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    // Touch Events
    //                  trick bid buttons
    //
    synchronized public void touchEventDown(int X, int Y) {
        if (!animatingNumbers) {
            return;
        }
        for (int i = 0; i < numberButtons.size(); i++) {
            numberButtons.get(i).touchEventDown(X, Y);
        }
    }

    synchronized public void touchEventMove(int X, int Y) {
        if (!animatingNumbers) {
            return;
        }
        for (int i = 0; i < numberButtons.size(); i++) {
            numberButtons.get(i).touchEventMove(X, Y);
        }
    }

    synchronized public void touchEventUp(int X, int Y, GameController controller) {
        if (!animatingNumbers) {
            return;
        }
        for (int i = 0; i < numberButtons.size(); i++) {
            if (numberButtons.get(i).touchEventDown(X, Y)) {
                animatingNumbers = false;
                controller.getGamePlay().getTrickBids().handleMainPlayersDecision(i, controller);
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    void turnOnAnimationNumbers() {
        animatingNumbers = true;
    }

    List<MyButton> getNumberButtons() {
        return numberButtons;
    }
}
