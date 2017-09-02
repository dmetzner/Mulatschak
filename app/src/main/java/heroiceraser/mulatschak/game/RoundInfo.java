package heroiceraser.mulatschak.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.StaticLayout;
import android.text.TextPaint;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.DrawableBasicObjects.TextField;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;

/**
 * Created by Daniel Metzner on 01.09.2017.
 */

public class RoundInfo extends DrawableObject {

    private TextField trump_;
    String trump_text_ = "Trumpf: ";

    private TextField multiplier_;

    public RoundInfo() {
        super();
        // ToDo set height etc
        setVisible(false);
        trump_ = new TextField();
        multiplier_ = new TextField();
    }

    public void init(GameView view) {
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
            trump_.draw(canvas, new Point(20,20));
        }
    }

}
