package heroiceraser.mulatschak.game;


import android.graphics.Point;

import java.util.List;

import heroiceraser.mulatschak.game.Animations.ReAnimateHands;

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

        if (!controller.getButtonBar().getStatistics().isOn()) {
            if (X >= controller.getButtonBar().getStatistics().getStatsButton().getPosition().x &&
                    X < controller.getButtonBar().getStatistics().getStatsButton().getPosition().x + controller.getLayout().getCardHeight() &&
                    Y >= controller.getButtonBar().getStatistics().getStatsButton().getPosition().y &&
                    Y < controller.getButtonBar().getStatistics().getStatsButton().getPosition().y + controller.getLayout().getCardHeight()) {
                controller.getButtonBar().getStatistics().getStatsButton().setPressed(true);
            }
        }

        if (controller.getLogic().getTurn() == 0 && move_card_ < 0) {
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

        if (controller.getAnimation().getStichAnsage().getAnimationNumbers()) {
            List<Button> buttons = controller.getAnimation().getStichAnsage().getNumberButtons();
            int width = controller.getLayout().getSmallButtonSize();
            for (int i = 0; i < buttons.size(); i++) {
                if (i == 6) {
                    width *= 3;
                }
                if (buttons.get(i).IsEnabled() &&
                        X >= buttons.get(i).getPosition().x &&  X < buttons.get(i)
                        .getPosition().x + width &&
                        Y >= buttons.get(i).getPosition().y &&  Y < buttons.get(i)
                        .getPosition().y + controller.getLayout().getSmallButtonSize()) {
                    buttons.get(i).setPressed(true);
                }
            }
        }
        else if (controller.getAnimation().getStichAnsage().getAnimationSymbols()) {
            List<Button> buttons = controller.getAnimation().getStichAnsage().getSymbolButtons();
            for (int i = 0; i < buttons.size(); i++) {
                if (X >= buttons.get(i).getPosition().x &&  X < buttons.get(i)
                        .getPosition().x + controller.getLayout().getSymbolButtonSize() &&
                        Y >= buttons.get(i).getPosition().y &&  Y < buttons.get(i)
                        .getPosition().y + controller.getLayout().getSymbolButtonSize()) {
                    buttons.get(i).setPressed(true);
                }
            }
        }

    }

    public void ActionMove(GameController controller, int X, int Y) {

        if (!controller.getButtonBar().getStatistics().isOn() && controller.getButtonBar().getStatistics().getStatsButton().IsPressed()) {
            if (X >= controller.getButtonBar().getStatistics().getStatsButton().getPosition().x &&
                    X < controller.getButtonBar().getStatistics().getStatsButton().getPosition().x + controller.getLayout().getCardHeight() &&
                    Y >= controller.getButtonBar().getStatistics().getStatsButton().getPosition().y &&
                    Y < controller.getButtonBar().getStatistics().getStatsButton().getPosition().y + controller.getLayout().getCardHeight()) {
                controller.getButtonBar().getStatistics().getStatsButton().setPressed(true);
            }
            else {
                controller.getButtonBar().getStatistics().getStatsButton().setPressed(false);
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
                        X >= buttons.get(i).getPosition().x &&  X < buttons.get(i)
                        .getPosition().x + width &&
                        Y >= buttons.get(i).getPosition().y &&  Y < buttons.get(i)
                        .getPosition().y + controller.getLayout().getSmallButtonSize()) {
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
                        X >= buttons.get(i).getPosition().x && X < buttons.get(i)
                        .getPosition().x + controller.getLayout().getSymbolButtonSize() &&
                        Y >= buttons.get(i).getPosition().y && Y < buttons.get(i)
                        .getPosition().y + controller.getLayout().getSymbolButtonSize()) {
                    buttons.get(i).setPressed(true);
                } else {
                    buttons.get(i).setPressed(false);
                }
            }
        }
    }

    private void returnCardToHand(GameController controller) {
        controller.getPlayerById(0).getHand().getCardAt(move_card_).setPosition(new Point(
                controller.getPlayerById(0).getHand().getCardAt(move_card_).getFixedPosition()));
    }

    public void ActionUp(GameController controller, int X, int Y) {

        if (!controller.getButtonBar().getStatistics().isOn() && controller.getButtonBar().getStatistics().getStatsButton().IsPressed()) {
            if (X >= controller.getButtonBar().getStatistics().getStatsButton().getPosition().x &&
                    X < controller.getButtonBar().getStatistics().getStatsButton().getPosition().x + controller.getLayout().getCardHeight() &&
                    Y >= controller.getButtonBar().getStatistics().getStatsButton().getPosition().y &&
                    Y < controller.getButtonBar().getStatistics().getStatsButton().getPosition().y + controller.getLayout().getCardHeight()) {
                controller.getButtonBar().getStatistics().getStatsButton().setPressed(false);
                // TODO SHOW STAT SITE
            }
        }

        if (move_card_ >= 0) {
            CardStack hand =  controller.getPlayerById(0).getHand();
            int card_y = hand.getCardAt(move_card_).getPosition().y;
            int fixed_y = hand.getCardAt(move_card_).getFixedPosition().y;

            boolean valid = controller.getLogic().isAValidCardPlay(hand.getCardAt(move_card_), hand, controller.getDiscardPile());

            if (card_y > fixed_y -  controller.getLayout().getCardHeight() * 1.5) {
                returnCardToHand(controller);
            }
            else if (!valid) {
                returnCardToHand(controller);
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
