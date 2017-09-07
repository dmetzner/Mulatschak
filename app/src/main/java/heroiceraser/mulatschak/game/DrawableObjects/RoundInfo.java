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

    private TextField bids_;
    private TextField trump_;
    String trump_text_ = "Trumpf: ";
    private Point board_to_text_offset_;
    private TextField multiplier_;

    public RoundInfo() {
        super();
        setVisible(false);
        bids_ = new TextField();
        trump_ = new TextField();
        multiplier_ = new TextField();
        board_to_text_offset_ = new Point();
    }

    public void init(GameView view) {
        setWidth(view.getController().getLayout().getRoundInfoSize().x);
        setHeight(view.getController().getLayout().getRoundInfoSize().y);
        setPosition(view.getController().getLayout().getRoundInfoPositon());
        String image_name = "round_info";
        setBitmap(HelperFunctions.loadBitmap(view, image_name , getWidth(), getHeight()));
        setVisible(true);
        int width = (int) (view.getController().getLayout().getScreenWidth() * (8.0 / 10.0));
        board_to_text_offset_.x = getWidth() / 10;
        board_to_text_offset_.y = getHeight() / 3;
        bids_.init(view, "", width);
        trump_.init(view, trump_text_, width);
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
            text += "\n";
        }
        bids_.update(text, view.getController().getLayout().getSectors().get(2).y - board_to_text_offset_.y);
    }

    public void updateRoundInfo(GameController controller) {
        String text_ = trump_text_;
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
        trump_.update(text_, controller.getLayout().getSectors().get(2).y);
    }

    public void draw(Canvas canvas) {
        if (isVisible()) {
            canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);
            bids_.draw(canvas, new Point(getPosition().x + board_to_text_offset_.x, getPosition().y + board_to_text_offset_.y / 2));
            trump_.draw(canvas, new Point(getPosition().x + board_to_text_offset_.x, getPosition().y + board_to_text_offset_.y / 2));
        }
    }

    public TextField getTrickBidsTextField() {
        return bids_;
    }

    public TextField getTrumpTextField() {
        return trump_;
    }

    public void setInfoBoxEmpty() {
        bids_.setVisible(false);
        trump_.setVisible(false);
    }

}
