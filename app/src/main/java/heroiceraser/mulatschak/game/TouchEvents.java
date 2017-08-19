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

        if (controller.getAnimation().getStichAnsage().getAnimationRunning()) {
            List<Button> buttons = controller.getAnimation().getStichAnsage().getButtons();
            int width = controller.getLayout().getSmallButtonSize();
            for (int i = 0; i < buttons.size(); i++) {
                if (i == 6) {
                    width *= 3;
                }
                if (X >= buttons.get(i).getCoordinate().getX() &&  X < buttons.get(i)
                        .getCoordinate().getX() + width &&
                        Y >= buttons.get(i).getCoordinate().getY() &&  Y < buttons.get(i)
                        .getCoordinate().getY() + controller.getLayout().getSmallButtonSize()) {
                    buttons.get(i).setPressed(true);
                }
            }
        }

    }

    public void ActionMove(GameController controller, int X, int Y) {
        if (move_card_ >= 0) {
            controller.getPlayerById(0).getHand().getCardAt(move_card_).setPosition(
                    X -  controller.getLayout().getCardWidth() / 2,
                    Y -  controller.getLayout().getCardHeight() / 2);
        }

        if (controller.getAnimation().getStichAnsage().getAnimationRunning()) {
            List<Button> buttons = controller.getAnimation().getStichAnsage().getButtons();
            int width = controller.getLayout().getSmallButtonSize();
            for (int i = 0; i < buttons.size(); i++) {
                if (i == 6) {
                    width *= 3;
                }
                if (X >= buttons.get(i).getCoordinate().getX() &&  X < buttons.get(i)
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
    }

    public void ActionUp(GameController controller, int X, int Y) {
        if (move_card_ >= 0) {
            int card_y = controller.getPlayerById(0).getHand().getCardAt(move_card_).getPosition().getY();
            int fixed_y = controller.getPlayerById(0).getHand().getCardAt(move_card_).getFixedPosition().getY();;
            // return card to hand
            if (card_y > fixed_y -  controller.getLayout().getCardHeight() * 1.5)
            {
                controller.getPlayerById(0).getHand().getCardAt(move_card_).setPosition(new Coordinate(
                        controller.getPlayerById(0).getHand().getCardAt(move_card_).getFixedPosition()));
            }
            else {
                // Todo check valid card
                controller.getPlayerById(0).getHand().getCardAt(move_card_).setPosition(
                        new Coordinate(controller.getDiscardPile().getCoordinate(0)) );

                controller.getDiscardPile().setCardBottom(
                        controller.getPlayerById(0).getHand().getCardAt(move_card_));

                controller.getPlayerById(0).getHand().getCardStack().remove(move_card_);

                // TODO recalculate hand positions!
                controller.getAnimation().getReAnimateHands()
                        .redrawHands(controller.getLayout(), controller.getPlayerById(0));


                // ToDo give turn to next player
            }
            move_card_ = -1;
        }

        if (controller.getAnimation().getStichAnsage().getAnimationRunning()) {
            List<Button> buttons = controller.getAnimation().getStichAnsage().getButtons();
            for (int button_id = 0; button_id < buttons.size(); button_id++) {
                if (buttons.get(button_id).IsPressed())
                {
                    controller.getPlayerById(0).setTrumphsToMake(button_id);
                    buttons.get(button_id).setPressed(false);
                    controller.getAnimation().getStichAnsage().setAnimatingRunning(false);
                }
            }
        }

    }
}
