package heroiceraser.mulatschak.game.BaseObjects;

import android.graphics.Canvas;
import android.graphics.Point;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.BitmapMethodes;


// ToDo
public class DealerButton extends DrawableObject{

    private long time_start_;
    private boolean animation_running_;
    private Point start_position_;
    private Point offset_;

    public DealerButton() {
        super();
    }

    public void init(GameView view) {
        setPosition(view.getController().getLayout().getDeckPosition());
        setWidth(view.getController().getLayout().getDealerButtonSize());
        setHeight(getWidth());
        setBitmap(BitmapMethodes.loadBitmap(view, "dealer_button" ,getWidth(), getHeight()));
        setVisible(false);
    }

    public void startMoveAnimation(GameController controller, int player_position) {
        setVisible(true);
        time_start_ = System.currentTimeMillis();
        Point end_position = new Point(controller.getLayout().getDealerButtonPosition(player_position));
        start_position_ = new Point(getPosition());
        offset_ = new Point(end_position.x - getPosition().x, end_position.y - getPosition().y);
        animation_running_ = true;
        controller.getView().enableUpdateCanvasThread();
    }

    private void continueMoveAnimation(GameController controller) {
        long time = System.currentTimeMillis();
        long time_since_start = time - time_start_;
        double speed_factor = controller.getSettings().getAnimationSpeed().getSpeedFactor();
        double max_time = 1500 * speed_factor;

        double percentage = time_since_start / max_time;
        if (percentage > 1) {
            percentage = 1;
            animation_running_ = false;
        }

        setPosition(start_position_.x + (int) (offset_.x * percentage),
                start_position_.y + (int) (offset_.y * percentage));

    }

    public void draw(Canvas canvas, GameController controller) {
        if (isVisible()) {
            canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);

            if (animation_running_) {
                continueMoveAnimation(controller);
            }

        }
    }

}
