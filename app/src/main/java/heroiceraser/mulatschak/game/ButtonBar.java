package heroiceraser.mulatschak.game;


import android.graphics.Point;

/**
 * Created by Daniel Metzner on 25.08.2017.
 */

public class ButtonBar extends DrawableObject{

    private GameStatistics statistics_;

    public ButtonBar() {
        statistics_ = new GameStatistics();
    }

    public void init(GameView view) {
        setWidth(view.getController().getLayout().getScreenWidth());
        int divided_parts = 8;
        setHeight(view.getController().getLayout().getScreenHeight() / divided_parts);
        setPosition(new Point(0, getHeight() * (divided_parts - 1)));
        setVisible(true);

        statistics_.init(view, this);

    }

    public GameStatistics getStatistics() {
        return statistics_;
    }
}
