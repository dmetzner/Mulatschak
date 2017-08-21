package heroiceraser.mulatschak.game.Animations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.game.Button;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 19.08.2017.
 */

public class StichAnsage {

    private boolean animating_;
    private List<Button> buttons_;

    public StichAnsage() {
        animating_ = false;
        buttons_ = new ArrayList<Button>();
    }

    private Bitmap loadBitmap(GameView view, String image_name, int width, int height,
                              String package_name) {
        int resourceId =
                view.getResources().getIdentifier(image_name, package_name, view.getContext().getPackageName());
        Bitmap bitmap =
                BitmapFactory.decodeResource(view.getContext().getResources(),resourceId);

        // scale Bitmap
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

        assert(scaledBitmap != null) : "StichAnsage -> Bitmap == null";

       return (scaledBitmap);
    }

    public void init(GameView view) {

        String image_name = "button_";
        String package_name = "drawable";

        for (int button_id = 0; button_id < 7; button_id++) {
            Button button = new Button();
            int width = view.getController().getLayout().getSmallButtonSize();
            int height = view.getController().getLayout().getSmallButtonSize();

            if (button_id == 6) {
                width *= 3;
            }

            button.setBitmap(loadBitmap(view, image_name + button_id, width, height, package_name));
            button.setBitmapPressed(loadBitmap(view, image_name + button_id + "_pressed", width, height, package_name));
            buttons_.add(button);
        }

        GameLayout layout = view.getController().getLayout();
        int x = layout.getHandBottom().getX() + layout.getCardWidth() / 5;
        int y = layout.getHandBottom().getY() - 10 - layout.getSmallButtonSize();
        final int max_buttons_per_row = 3;
        buttons_.get(6).setCoordinate(x, y);
        int buttons_per_row = max_buttons_per_row;
        int amount_of_buttons = getButtons().size();
        for (int button_id = amount_of_buttons - 2; button_id >= 0; button_id--) {
            if (buttons_per_row >= max_buttons_per_row) {
                buttons_per_row = 0;
                x = layout.getHandBottom().getX() + layout.getCardWidth() / 5 + 2 * layout.getSmallButtonSize();
                y -= layout.getSmallButtonSize();
            }
            buttons_.get(button_id).setCoordinate(x, y);
            x -= layout.getSmallButtonSize();
            buttons_per_row++;
        }


    }

    public boolean getAnimationRunning() {
        return animating_;
    }
    public void setAnimatingRunning(boolean animating) {
        animating_ = animating;
    }

    public List<Button> getButtons() {
        return buttons_;
    }

    public Button getButtonAt(int pos) {
        if (pos < buttons_.size()) {
            return buttons_.get(pos);
        }
        return null;
    }
}
