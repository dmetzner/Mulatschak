package heroiceraser.mulatschak.game.DrawableObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.StaticLayout;
import android.text.TextPaint;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.DrawableBasicObjects.TextField;
import heroiceraser.mulatschak.game.Animations.TrickBids;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLogic;
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
        new_round_.init(view, "", monitor_size_.x);
        trick_bids_.init(view, "", monitor_size_.x);
        choose_trump_.init(view, "", monitor_size_.x);
        round_info_.init(view, "", monitor_size_.x);
        game_over_.init(view, "Game Over", monitor_size_.x);
    }

    public void updateNewRound(GameController controller) {
        String text = "";
        text +=   "Neue Runde!";
        text += "\n\n Spieler " + (controller.getLogic().getDealer() + 1) + " ist der Dealer";
        new_round_.update(text, monitor_size_.y);
    }

    public void updateBids(GameView view) {
        String text = "";
        for (int i = 0; i < view.getController().getAmountOfPlayers(); i++) {
            text += "Player " + (i + 1) + ": ";
            if (view.getController().getLogic().getTurn() == i &&
                    view.getController().getPlayerById(i).getTrumphsToMake() == GameController.NOT_SET) {
                text += "ist am Zug";
            }
            else if (view.getController().getPlayerById(i).getTrumphsToMake() == GameController.NOT_SET) {
                text += "wartet ...";
            }
            else if (view.getController().getPlayerById(i).getTrumphsToMake() == TrickBids.MISS_A_TURN) {
                text += "setzt diese Runde aus";
            }
            else if (view.getController().getPlayerById(i).getTrumphsToMake() == 0) {
                text += "macht keine Stiche";
            }
            else {
                text += "will " + view.getController().getPlayerById(i).getTrumphsToMake() + " Stiche machen";
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
            int id = controller.getLogic().getTrumphPlayerId();
            if (id == 0) {
                text += "Du bist der Höchstbietende. \n";
                text += "Das Trumpf dieser Runde ist Herz.\n";
                text += "Herz Runden zählen doppelt!";
            }
            else {
                text += "Spieler " + (controller.getLogic().getTrumphPlayerId() + 1) + " ist der Höchstbietende. \n";
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
        int id = controller.getLogic().getTrumphPlayerId();
        if (id == 0) {
            text += "\nDu bist der Höchstbietende. \n";
            text += "Wähle das Trumpf";
        }
        else {
            text += "\nSpieler " +(id + 1) + " ist der Höchstbietende. \n";
            text += "Spieler " + (id + 1) + " wählt das Trumpf";
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
        text_ += "Spieler " + (controller.getLogic().getTrumphPlayerId() + 1) + ":   ";
        text_ += controller.getPlayerById(
                controller.getLogic().getTrumphPlayerId()).getAmountOfTricks(controller);
        text_ += "/" + controller.getLogic().getTrumphsToMake() + " Stiche\n";

        if (controller.getLogic().getTurn() == 0) {
            text_ +=  "Du bist am Zug";
        }
        else {
            text_ += "Spieler " + (controller.getLogic().getTurn() + 1) + ":   ist am Zug";
        }

        round_info_.update(text_, monitor_size_.y);
    }

    public void draw(Canvas canvas) {
        if (isVisible()) {
            canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);
            Point pos =  new Point(getPosition().x + board_to_text_offset_.x, getPosition().y + board_to_text_offset_.y);
            new_round_.draw(canvas, pos);
            trick_bids_.draw(canvas, pos);
            choose_trump_.draw(canvas, pos);
            round_info_.draw(canvas, pos);
        }
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

    public void setInfoBoxEmpty() {
        new_round_.setVisible(false);
        trick_bids_.setVisible(false);
        choose_trump_.setVisible(false);
        round_info_.setVisible(false);
        game_over_.setVisible(false);
    }

}
