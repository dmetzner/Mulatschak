package heroiceraser.mulatschak.game.NonGamePlayUI.RoundInfo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.DrawableBasicObjects.TextField;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GamePlay.TrickBids.MakeBidsAnimation;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 01.09.2017.
 */

public class RoundInfo extends DrawableObject {

    private TextField new_round_;
    private TextField trick_bids_;
    private TextField choose_trump_;
    private TextField round_info_;
    private TextField end_of_card_round_;
    private TextField end_of_round_;
    private TextField game_over_;

    private Point monitor_size_;
    private Point board_to_text_offset_;


    public RoundInfo() {
        super();
        setVisible(false);
        new_round_ = new TextField();
        trick_bids_ = new TextField();
        choose_trump_ = new TextField();
        round_info_ = new TextField();
        end_of_card_round_ = new TextField();
        end_of_round_ = new TextField();
        game_over_ = new TextField();

        monitor_size_ = new Point();
        board_to_text_offset_ = new Point();
    }

    public void init(GameView view) {
        setWidth(view.getController().getLayout().getRoundInfoSize().x);
        setHeight(view.getController().getLayout().getRoundInfoSize().y);
        setPosition(view.getController().getLayout().getRoundInfoPositon());
        String image_name = "round_info";
        setBitmap(HelperFunctions.loadBitmap(view, image_name , getWidth(), getHeight()));
        setVisible(true);
        board_to_text_offset_.x = getWidth() / 10;
        board_to_text_offset_.y = getHeight() / 6;
        monitor_size_.x = (int) (view.getController().getLayout().getScreenWidth() * (8.0 / 10.0));
        monitor_size_.y = view.getController().getLayout().getSectors().get(2).y - 2 * board_to_text_offset_.y;
        new_round_.init(view, "", monitor_size_.x, Color.WHITE);
        trick_bids_.init(view, "", monitor_size_.x, Color.WHITE);
        choose_trump_.init(view, "", monitor_size_.x, Color.WHITE);
        round_info_.init(view, "", monitor_size_.x, Color.WHITE);
        end_of_card_round_.init(view, "", monitor_size_.x, Color.WHITE);
        end_of_round_.init(view, "", monitor_size_.x, Color.WHITE);
        game_over_.init(view, "Game Over", monitor_size_.x, Color.WHITE);
    }

    public void updateEndOfCardRound(GameController controller) {
        String text = "";
        text += controller.getPlayerById(controller.getLogic().getRoundWinnerId()).getDisplayName()
                + " hat diesen Stich gewonnen.";
        end_of_card_round_.update(text, monitor_size_.y);
    }

    public void updateEndOfRound(GameController controller) {
        String text = "";
        for (int i = 0; i < controller.getAmountOfPlayers(); i++) {
            if (i == controller.getLogic().getTrumpPlayerId()) {
                text +=  controller.getPlayerById(i).getDisplayName()
                        + ": " + controller.getPlayerById(i).getAmountOfTricks(controller)
                        + "/" + controller.getLogic().getTricksToMake()
                        + " Stiche";
            }
            else {
                text += controller.getPlayerById(i).getDisplayName()
                        + ": " + controller.getPlayerById(i).getAmountOfTricks(controller)
                        + " Stiche";
            }
            if (i != controller.getAmountOfPlayers() - 1) {
                text += "\n";
            }
        }
        end_of_round_.update(text, monitor_size_.y);
    }

    public void updateNewRound(GameController controller) {
        String text = "";
        text +=   "Neue Runde!";
        text += "\n\n" + controller.getPlayerById(controller.getLogic().getDealer()).getDisplayName()
                + " ist der Dealer";
        new_round_.update(text, monitor_size_.y);
    }

    public void updateBids(GameView view) {
        String text = "";
        for (int i = 0; i < view.getController().getAmountOfPlayers(); i++) {
            text += view.getController().getPlayerById(i).getDisplayName() + ": ";
            if (view.getController().getLogic().getTurn() == i &&
                    view.getController().getPlayerById(i).getTricksToMake() == GameController.NOT_SET) {
                text += "ist am Zug";
            }
            else if (view.getController().getPlayerById(i).getTricksToMake() == GameController.NOT_SET) {
                text += "wartet ...";
            }
            else if (view.getController().getPlayerById(i).getTricksToMake() == MakeBidsAnimation.MISS_A_TURN) {
                text += "setzt diese Runde aus";
            }
            else if (view.getController().getPlayerById(i).getTricksToMake() == 0) {
                text += "macht keine Stiche";
            }
            else {
                text += "will " + view.getController().getPlayerById(i).getTricksToMake() + " Stiche machen";
            }
            if (i != view.getController().getAmountOfPlayers() - 1) {
                text += "\n";
            }

        }
        trick_bids_.update(text, monitor_size_.y);
    }

    public void updateChooseTrump(GameController controller, int special_case) {
        String text = "";
        if (special_case == 0) { // 0 tricks
            text += "Keiner Spieler macht einen Stich \n";
            text += "Neue Runde startet! Diese zählt doppelt!";
        }
        else if (special_case == 1){  // 1 trick -> heart
            int id = controller.getLogic().getTrumpPlayerId();
            if (id == 0) {
                text += "Du bist der Höchstbietende. \n";
                text += "Das Trumpf dieser Runde ist Herz.\n";
                text += "Herz Runden zählen doppelt!";
            }
            else {
                text += controller.getPlayerById(controller.getLogic().getTrumpPlayerId()).getDisplayName()
                        + " ist der Höchstbietende. \n";
                text += "Das Trumpf dieser Runde ist Herz.\n";
                text += "Herz Runden zählen doppelt!";
            }
        }
        else {
            updateChooseTrump(controller);
            return;
        }
        choose_trump_.update(text, monitor_size_.y);
    }

    public void updateChooseTrump(GameController controller) {
        String text = "";
        int id = controller.getLogic().getTrumpPlayerId();
        if (id == 0) {
            text += "\nDu bist der Höchstbietende. \n";
            text += "Wähle das Trumpf";
        }
        else {
            text += "\n" + controller.getPlayerById(id).getDisplayName() + " ist der Höchstbietende. \n";
            text += controller.getPlayerById(id).getDisplayName() + " wählt das Trumpf";
        }
        choose_trump_.update(text, monitor_size_.y);
    }

    public void updateRoundInfo(GameController controller) {
        String text_ = "Trumpf:   ";
        switch (controller.getLogic().getTrump()) {
            case MulatschakDeck.HEART:
                text_ += "Herz";
                break;
            case MulatschakDeck.DIAMOND:
                text_ += "Karo";
                break;
            case MulatschakDeck.CLUB:
                text_ += "Kreuz";
                break;
            case MulatschakDeck.SPADE:
                text_ += "Pik";
                break;
        }
        text_ += "\n";
        text_ += "Punkte:    x" + controller.getLogic().getMultiplier();
        text_ += "\n";
        text_ += controller.getPlayerById(controller.getLogic().getTrumpPlayerId()).getDisplayName()
                + ":   ";
        text_ += controller.getPlayerById(
                controller.getLogic().getTrumpPlayerId()).getAmountOfTricks(controller);
        text_ += "/" + controller.getLogic().getTricksToMake() + " Stiche\n";

        if (controller.getLogic().getTurn() == 0) {
            text_ +=  "Du bist am Zug";
        }
        else {
            text_ += controller.getPlayerById(controller.getLogic().getTurn()).getDisplayName()
                    + ":   ist am Zug";
        }

        round_info_.update(text_, monitor_size_.y);
    }

    public void draw(Canvas canvas) {
        if (isVisible()) {
            canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);
            Point pos =  new Point(getPosition().x + board_to_text_offset_.x,
                    getPosition().y + board_to_text_offset_.y);
            new_round_.draw(canvas, pos);
            trick_bids_.draw(canvas, pos);
            choose_trump_.draw(canvas, pos);
            round_info_.draw(canvas, pos);
            end_of_card_round_.draw(canvas, pos);
            end_of_round_.draw(canvas, pos);
            game_over_.draw(canvas, pos);
        }
    }

    public TextField getEndOfRound() {
        return end_of_round_;
    }

    public TextField getNewRound() {
        return new_round_;
    }

    public TextField getTrickBidsTextField() {
        return trick_bids_;
    }

    public TextField getChooseTrumpTextField() {
        return choose_trump_;
    }

    public TextField getRoundInfoTextField() {
        return round_info_;
    }

    public TextField getGameOver() {
        return game_over_;
    }

    public TextField getEndOfCardRound() {
        return end_of_card_round_;
    }

    public void setInfoBoxEmpty() {
        new_round_.setVisible(false);
        trick_bids_.setVisible(false);
        choose_trump_.setVisible(false);
        round_info_.setVisible(false);
        end_of_card_round_.setVisible(false);
        game_over_.setVisible(false);
        end_of_round_.setVisible(false);
    }

}
