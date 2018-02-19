package heroiceraser.mulatschak.game.GamePlay.PlayACard;

import android.graphics.Canvas;
import android.os.Handler;

import heroiceraser.mulatschak.game.BaseObjects.Card;
import heroiceraser.mulatschak.game.BaseObjects.DiscardPile;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GameView;


//-------------------------------------------------------------------------------------------------
//  Play A Card Class
//
public class PlayACardRound {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private boolean roundEnded;             //  -> touch to jump to next round
    private boolean touched;
    private boolean discardPileClicked;     //      (touch on discard pile)
    private PlayACardLogic play_a_card_logic_;
    private EnemyPlayACardLogic enemy_play_a_card_logic_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public PlayACardRound() {
        play_a_card_logic_ = new PlayACardLogic();
        enemy_play_a_card_logic_ = new EnemyPlayACardLogic();
    }


    //----------------------------------------------------------------------------------------------
    //  Init
    //
    public void init(GameView view) {
        play_a_card_logic_.init(view);
        enemy_play_a_card_logic_.init(view.getController());
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas, GameController controller) {
        play_a_card_logic_.draw(canvas, controller);
        enemy_play_a_card_logic_.draw(canvas, controller);
    }


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public void playACard(final boolean first_call, final GameController controller) {
        GameLogic logic = controller.getLogic();

        if (first_call) {
            logic.setStartingPlayer(logic.getTurn());
        }
        else {
            controller.turnToNextPlayer(true);
        }

        // only allowed if turn is player 0
        controller.getGamePlay().getPlayACardRound().setCardMovable(false);

        // ever player played played a card
        if (!first_call && logic.getTurn() == logic.getStartingPlayer()) {
            endCardRound(controller);
            return;
        }

        // player skips this round
        if (controller.getPlayerById(logic.getTurn()).getMissATurn()) {
            controller.turnToNextPlayer(true);
            playACard(false, controller);
            return;
        }

        // player 0 -> allow card movement
        if (logic.getTurn() == 0) {
            controller.getGamePlay().getPlayACardRound().setCardMovable(true);
            controller.getView().disableUpdateCanvasThread();
            // touch event calls playACard
        }

        // enemies
        else if (logic.getTurn() != 0) {
            controller.getView().enableUpdateCanvasThread();
            enemy_play_a_card_logic_.playACard(controller,
                    controller.getPlayerById(logic.getTurn()));
        }
    }



    //----------------------------------------------------------------------------------------------
    //  end card round
    //
    private void endCardRound(final GameController controller) {
        GameLogic logic = controller.getLogic();

        // choose who has best card on the discard pile
        logic.chooseCardRoundWinner(controller, controller.getDiscardPile());
        controller.getDiscardPile().setOverlaysVisible(true);

        // add those tricks to the winning player and update trick button bar window
        addTricksToWinner(controller);


        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                prepareNextCardRound(controller, true);
            }
        };

        // muli failed
        if (logic.isMulatschakRound() && logic.getRoundWinnerId() != logic.getTrumpPlayerId()) {
            controller.getPlayerInfo().setShowPlayer0Turn(false);
            controller.getGamePlay().getMulatschakResultAnimation().init(controller, false);
            handler.postDelayed(runnable,
                    (int) (1500 * controller.getSettings().getAnimationSpeed().getSpeedFactor()));
            // end of animation calls allCardsPlayedLogic
            return;
        }

        // muli succeeded
        if (controller.getGamePlay().getAllCardsPlayed().areAllCardsPlayed(controller) &&
                logic.isMulatschakRound()) {
            controller.getPlayerInfo().setShowPlayer0Turn(false);
            controller.getGamePlay().getMulatschakResultAnimation().init(controller, true);
            handler.postDelayed(runnable,
                    (int) (1500 * controller.getSettings().getAnimationSpeed().getSpeedFactor()));
            // end of animation calls allCardsPlayedLogic
            return;
        }

        // the winner can play the fist card next Round
        controller.setTurn(logic.getRoundWinnerId());
        logic.setStartingPlayer(logic.getRoundWinnerId());

        // start new round
        roundEnded = true;
        touched = false;
        discardPileClicked = false;
        Handler end_round_handler = new Handler();
        Runnable end_round_runnable = new Runnable() {
            @Override
            public void run() {
                if (!discardPileClicked) {
                    prepareNextCardRound(controller, false);
                }
            }
        };
        int max_waiting_time = (int) (2500 * controller.getSettings().getAnimationSpeed().getSpeedFactor());
        end_round_handler.postDelayed(end_round_runnable, max_waiting_time);
    }


    //----------------------------------------------------------------------------------------------
    //  prepare next card round
    //                              -> called when every player played a card
    //                              -> and the next Round button was pressed (ToDo)
    //                              -> clear discard pile & call next round
    //
    private void prepareNextCardRound(final GameController controller, boolean muli) {
        roundEnded = false;
        controller.getDiscardPile().setOverlaysVisible(false);
        controller.getDiscardPile().clear();
        if (muli) {
            // remove all hand cards
            for (MyPlayer player : controller.getPlayerList()) {
                for (Card card : player.getHand().getCardStack()) {
                    card.setVisible(false);
                }
            }
            controller.getGamePlay().getMulatschakResultAnimation().startAnimation(controller);
        }
        else {
            controller.nextCardRound();
        }
    }



    //----------------------------------------------------------------------------------------------
    //  add Tricks To Winner
    //                          -> called when every player played a card
    //                          -> player with the best card on the discard pile gets all cards
    //                              on the discard pile added to his tricks
    //
    private void addTricksToWinner(GameController controller) {

        int winner_id = controller.getLogic().getRoundWinnerId();

        // button bar window -> update tricksView Window
        controller.getNonGamePlayUIContainer().getTricks().addTricksAndUpdate(
                controller.getDiscardPile(), winner_id);

        // add the cards to the player
        DiscardPile dp = controller.getDiscardPile();
        for (int i = 0; i < dp.SIZE; i++) {
            if (dp.getCard(i) != null) {
                controller.getPlayerById(winner_id).getTricks().addCard(dp.getCard(i));
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    //  Touch Events
    //
    public void touchEventDown(int X, int Y, GameController controller) {

        // else the round just skips to the next round if player 0 is the last one to play a card
        if (!roundEnded) {
            play_a_card_logic_.touchActionDown(controller, X, Y);
            return;
        }

        touched = isDiscardPileTouched(X, Y, controller.getDiscardPile());

        // down on the hand cards is enough (feels more natural in this case)
        if (isAHandCardTouched(X, Y, controller.getPlayerById(0))) {
            discardPileClicked = true;
            prepareNextCardRound(controller, false);
        }

    }

    public void touchEventMove(int X, int Y, GameController controller) {
        if (!roundEnded) {
            play_a_card_logic_.touchActionMove(controller, X, Y);
            return;
        }

        touched = touched && isDiscardPileTouched(X, Y, controller.getDiscardPile());
    }

    public void touchEventUp(int X, int Y, GameController controller) {
        if (!roundEnded) {
            play_a_card_logic_.touchActionUp(controller);
            return;
        }
        if (touched && isDiscardPileTouched(X, Y, controller.getDiscardPile())) {
            touched = false;
            discardPileClicked = true;
            prepareNextCardRound(controller, false);
        }
    }


    private boolean isDiscardPileTouched(int X, int Y, DiscardPile discardPile) {
        return (X > discardPile.getPosition().x &&
                X < discardPile.getPosition().x + discardPile.getWidth() * 3
                && Y > discardPile.getPosition().y &&
                Y < discardPile.getPosition().y + discardPile.getHeight() * 2);
    }

    private boolean isAHandCardTouched(int X, int Y, MyPlayer player) {

        for (Card card : player.getHand().getCardStack()) {
            if (X > card.getPosition().x &&
                    X < card.getPosition().x + card.getWidth()
                    && Y > card.getPosition().y &&
                    Y < card.getPosition().y + card.getHeight()) {
                return true;
            }
        }
        return false;
    }




    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    private void setCardMovable(boolean movable) {
        play_a_card_logic_.setCardMoveable(movable);
    }
}
