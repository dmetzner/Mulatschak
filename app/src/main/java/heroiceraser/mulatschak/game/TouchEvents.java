package heroiceraser.mulatschak.game;


import java.util.List;

import heroiceraser.mulatschak.game.Animations.ReAnimateHands;
import heroiceraser.mulatschak.helpers.Coordinate;

/**
 * Created by Daniel Metzner on 14.08.2017.
 */

public class TouchEvents {

    private int move_card_;

    public TouchEvents() {
        move_card_ = -1;
    }

    public void ActionDown(GameController controller, int X, int Y) {

        if (controller.waiting) {
            // ToDO Just 4 now Hack, better solution please :D
            controller.waiting = false;
            controller.endCardRound();
        }

        if (!controller.getStatistics().isOn()) {
            if (X >= controller.getStatistics().getStatsButton().getCoordinate().getX() &&
                    X < controller.getStatistics().getStatsButton().getCoordinate().getX() + controller.getLayout().getCardHeight() &&
                    Y >= controller.getStatistics().getStatsButton().getCoordinate().getY() &&
                    Y < controller.getStatistics().getStatsButton().getCoordinate().getY() + controller.getLayout().getCardHeight()) {
                controller.getStatistics().getStatsButton().setPressed(true);
            }
        }

        if (controller.getLogic().getTurn() == 0 && move_card_ < 0) {
            for (int i = 0; i < controller.getPlayerById(0).getAmountOfCardsInHand(); i++) {
                Card card = controller.getPlayerById(0).getHand().getCardAt(i);
                if (card.getFixedPosition() == null) {
                    break;
                }
                if (X >= card.getFixedPosition().getX() &&
                        X < card.getFixedPosition().getX() + controller.getLayout().getCardWidth() &&
                        Y >= card.getFixedPosition().getY() &&
                        Y < card.getFixedPosition().getY() +  controller.getLayout().getCardHeight()) {
                    move_card_ = i;
                    break;
                } else {
                    move_card_ = -1;
                }
            }
        }

        if (controller.getAnimation().getStichAnsage().getAnimationNumbers()) {
            List<Button> buttons = controller.getAnimation().getStichAnsage().getNumberButtons();
            int width = controller.getLayout().getSmallButtonSize();
            for (int i = 0; i < buttons.size(); i++) {
                if (i == 6) {
                    width *= 3;
                }
                if (buttons.get(i).IsEnabled() &&
                        X >= buttons.get(i).getCoordinate().getX() &&  X < buttons.get(i)
                        .getCoordinate().getX() + width &&
                        Y >= buttons.get(i).getCoordinate().getY() &&  Y < buttons.get(i)
                        .getCoordinate().getY() + controller.getLayout().getSmallButtonSize()) {
                    buttons.get(i).setPressed(true);
                }
            }
        }
        else if (controller.getAnimation().getStichAnsage().getAnimationSymbols()) {
            List<Button> buttons = controller.getAnimation().getStichAnsage().getSymbolButtons();
            for (int i = 0; i < buttons.size(); i++) {
                if (X >= buttons.get(i).getCoordinate().getX() &&  X < buttons.get(i)
                        .getCoordinate().getX() + controller.getLayout().getSymbolButtonSize() &&
                        Y >= buttons.get(i).getCoordinate().getY() &&  Y < buttons.get(i)
                        .getCoordinate().getY() + controller.getLayout().getSymbolButtonSize()) {
                    buttons.get(i).setPressed(true);
                }
            }
        }

    }

    public void ActionMove(GameController controller, int X, int Y) {

        if (!controller.getStatistics().isOn() && controller.getStatistics().getStatsButton().IsPressed()) {
            if (X >= controller.getStatistics().getStatsButton().getCoordinate().getX() &&
                    X < controller.getStatistics().getStatsButton().getCoordinate().getX() + controller.getLayout().getCardHeight() &&
                    Y >= controller.getStatistics().getStatsButton().getCoordinate().getY() &&
                    Y < controller.getStatistics().getStatsButton().getCoordinate().getY() + controller.getLayout().getCardHeight()) {
                controller.getStatistics().getStatsButton().setPressed(true);
            }
            else {
                controller.getStatistics().getStatsButton().setPressed(false);
            }
        }

        if (move_card_ >= 0) {
            controller.getPlayerById(0).getHand().getCardAt(move_card_).setPosition(
                    X -  controller.getLayout().getCardWidth() / 2,
                    Y -  controller.getLayout().getCardHeight() / 2);
        }

        if (controller.getAnimation().getStichAnsage().getAnimationNumbers()) {
            List<Button> buttons = controller.getAnimation().getStichAnsage().getNumberButtons();
            int width = controller.getLayout().getSmallButtonSize();
            for (int i = 0; i < buttons.size(); i++) {
                if (i == 6) {
                    width *= 3;
                }
                if (buttons.get(i).IsEnabled() && buttons.get(i).IsPressed() &&
                        X >= buttons.get(i).getCoordinate().getX() &&  X < buttons.get(i)
                        .getCoordinate().getX() + width &&
                        Y >= buttons.get(i).getCoordinate().getY() &&  Y < buttons.get(i)
                        .getCoordinate().getY() + controller.getLayout().getSmallButtonSize()) {
                    buttons.get(i).setPressed(true);
                }
                else {
                    buttons.get(i).setPressed(false);
                }
            }
        }
        else if (controller.getAnimation().getStichAnsage().getAnimationSymbols()) {
            List<Button> buttons = controller.getAnimation().getStichAnsage().getSymbolButtons();
            for (int i = 0; i < buttons.size(); i++) {
                if (buttons.get(i).IsPressed() &&
                        X >= buttons.get(i).getCoordinate().getX() && X < buttons.get(i)
                        .getCoordinate().getX() + controller.getLayout().getSymbolButtonSize() &&
                        Y >= buttons.get(i).getCoordinate().getY() && Y < buttons.get(i)
                        .getCoordinate().getY() + controller.getLayout().getSymbolButtonSize()) {
                    buttons.get(i).setPressed(true);
                } else {
                    buttons.get(i).setPressed(false);
                }
            }
        }
    }

    private void returnCardToHand(GameController controller) {
        controller.getPlayerById(0).getHand().getCardAt(move_card_).setPosition(new Coordinate(
                controller.getPlayerById(0).getHand().getCardAt(move_card_).getFixedPosition()));
    }

    public void ActionUp(GameController controller, int X, int Y) {

        if (!controller.getStatistics().isOn() && controller.getStatistics().getStatsButton().IsPressed()) {
            if (X >= controller.getStatistics().getStatsButton().getCoordinate().getX() &&
                    X < controller.getStatistics().getStatsButton().getCoordinate().getX() + controller.getLayout().getCardHeight() &&
                    Y >= controller.getStatistics().getStatsButton().getCoordinate().getY() &&
                    Y < controller.getStatistics().getStatsButton().getCoordinate().getY() + controller.getLayout().getCardHeight()) {
                controller.getStatistics().getStatsButton().setPressed(false);
                // TODO SHOW STAT SITE
            }
        }

        if (move_card_ >= 0) {
            CardStack hand =  controller.getPlayerById(0).getHand();
            int card_y = hand.getCardAt(move_card_).getPosition().getY();
            int fixed_y = hand.getCardAt(move_card_).getFixedPosition().getY();

            boolean valid = controller.getLogic().isAValidCardPlay(hand.getCardAt(move_card_), hand, controller.getDiscardPile());

            if (card_y > fixed_y -  controller.getLayout().getCardHeight() * 1.5) {
                returnCardToHand(controller);
            }
            else if (!valid) {
                returnCardToHand(controller);
            }
            else if (valid) {
                controller.getPlayerById(0).getHand().getCardAt(move_card_).setPosition(
                        new Coordinate(controller.getDiscardPile().getCoordinate(0)) );

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

        if (controller.getAnimation().getStichAnsage().getAnimationNumbers()) {
            List<Button> buttons = controller.getAnimation().getStichAnsage().getNumberButtons();
            for (int button_id = 0; button_id < buttons.size(); button_id++) {
                if (buttons.get(button_id).IsPressed()) {
                    buttons.get(button_id).setPressed(false);
                    controller.getAnimation().getStichAnsage().setAnimationNumbers(false);
                    controller.setNewMaxTrumphs(button_id, 0);
                    controller.trickBids();
                }
            }
        }
        else if (controller.getAnimation().getStichAnsage().getAnimationSymbols()) {
            List<Button> buttons = controller.getAnimation().getStichAnsage().getSymbolButtons();
            for (int i = 0; i < buttons.size(); i++) {
                if (buttons.get(i).IsPressed()) {
                    controller.getLogic().setTrumph(i + 1);
                    buttons.get(i).setPressed(false);
                    controller.getAnimation().getStichAnsage().setAnimationSymbols(false);
                    controller.continueAfterTrumpWasChoosen();
                }
            }
        }


    }
}
