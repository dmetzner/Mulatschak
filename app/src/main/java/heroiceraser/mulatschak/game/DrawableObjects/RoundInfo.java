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
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;

/**
 * Created by Daniel Metzner on 01.09.2017.
 */

public class RoundInfo extends DrawableObject {

    private TextField trump_;
    String trump_text_ = "Trumpf: ";

    private TextField multiplier_;

    public RoundInfo() {
        super();
        setVisible(false);
        trump_ = new TextField();
        multiplier_ = new TextField();
    }

    public void init(GameView view) {
        setWidth(view.getController().getLayout().getRoundInfoSize().x);
        setHeight(view.getController().getLayout().getRoundInfoSize().y);
        setPosition(view.getController().getLayout().getRoundInfoPositon());
        String image_name = "round_info";
        setBitmap(HelperFunctions.loadBitmap(view, image_name , getWidth(), getHeight()));
        setVisible(true);
        trump_.setWidth(view.getController().getLayout().getScreenWidth() / 2);
        trump_.init(view, trump_text_, trump_.getWidth());
    }

    public void updateRoundInfo(GameLogic logic) {
        String text_ = trump_text_;
        switch (logic.getTrump()) {
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
        trump_.update(text_);
    }

    public void draw(Canvas canvas) {
        if (isVisible()) {
            canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);
            trump_.draw(canvas, new Point(getPosition().x + 20, getPosition().y + 20));
        }
    }

}
