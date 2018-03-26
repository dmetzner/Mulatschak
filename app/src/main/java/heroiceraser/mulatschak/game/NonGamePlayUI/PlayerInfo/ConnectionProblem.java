package heroiceraser.mulatschak.game.NonGamePlayUI.PlayerInfo;

import android.graphics.Canvas;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.BitmapMethodes;


public class ConnectionProblem extends DrawableObject {

    public ConnectionProblem() {
        super();
    }

    public void init(GameView view) {
        setWidth(view.getController().getLayout().getPlayerInfoSize().x);
        setHeight(getWidth());
        setPosition(view.getController().getLayout().getCenter().x - getWidth(),
                view.getController().getLayout().getCenter().y - getHeight());
        setBitmap(BitmapMethodes.loadBitmap(view, "wlan" ,getWidth(), getHeight()));
        setVisible(false);
    }

    public void draw(Canvas canvas) {
        if (isVisible() && getBitmap() != null) {
            canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);
        }
    }

}
