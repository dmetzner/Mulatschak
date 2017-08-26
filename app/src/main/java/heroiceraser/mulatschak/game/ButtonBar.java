package heroiceraser.mulatschak.game;


import android.graphics.Point;

/**
 * Created by Daniel Metzner on 25.08.2017.
 */

public class ButtonBar {

    private GameStatistics statistics_;

    private boolean visible_;
    private Point coordinate_top_left_;
    private int bar_width_;
    private int bar_height_;

    public ButtonBar() {
        visible_ = false;
        coordinate_top_left_ = new Point();
        bar_width_ = -1;
        bar_height_ = -1;
        statistics_ = new GameStatistics();
    }

    public void init(GameView view) {
        bar_width_ = view.getController().getLayout().getScreenWidth();
        int divided_parts = 8;
        bar_height_ = view.getController().getLayout().getScreenHeight() / divided_parts;
        coordinate_top_left_ = new Point(0, bar_height_ * (divided_parts - 1));
        visible_ = true;

        statistics_.init(view, this);

    }

    public int getBarWidth() {
        return bar_width_;
    }

    public int getBarHeight() {
        return bar_height_;
    }

    public Point getPointTopLeft() {
        return coordinate_top_left_;
    }

    public void setVisible(boolean visible) {
        visible_ = visible;
    }

    public boolean isVisible() {
        return visible_;
    }

    public GameStatistics getStatistics() {
        return statistics_;
    }
}
