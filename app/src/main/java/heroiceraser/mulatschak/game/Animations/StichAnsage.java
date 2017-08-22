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

    private boolean animating_numbers_;
    private boolean animating_smybols_;
    private List<Button> number_buttons_;
    private List<Button> symbol_buttons_;

    public StichAnsage() {
        animating_numbers_ = false;
        number_buttons_ = new ArrayList<Button>();
        animating_smybols_ = false;
        symbol_buttons_ = new ArrayList<Button>();
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
        initNumberButtons(view);
        initSymbols(view);
    }

    private void initSymbols(GameView view) {
        String image_name = "symbol_";
        String package_name = "drawable";

        for (int button_id = 1; button_id <= 4; button_id++) {
            Button button = new Button();
            int width = view.getController().getLayout().getSymbolButtonSize();
            int height = view.getController().getLayout().getSymbolButtonSize();

            button.setBitmap(loadBitmap(view, image_name + button_id, width, height, package_name));
            button.setBitmapPressed(loadBitmap(view, image_name + button_id + "_pressed", width, height, package_name));
            symbol_buttons_.add(button);
        }

        GameLayout layout = view.getController().getLayout();
        int x = layout.getHandBottom().getX() + layout.getCardWidth() / 2;
        int y = layout.getHandBottom().getY() - 10 - layout.getSymbolButtonSize() / 2;
        final int max_buttons_per_row = 2;
        int buttons_per_row = max_buttons_per_row;
        for (int button_id = 0; button_id < symbol_buttons_.size(); button_id++) {
            if (buttons_per_row >= max_buttons_per_row) {
                buttons_per_row = 0;
                x = layout.getHandBottom().getX() + layout.getCardWidth() / 2;
                y -= layout.getSymbolButtonSize();
            }
            symbol_buttons_.get(button_id).setCoordinate(x, y);
            x += layout.getSymbolButtonSize();
            buttons_per_row++;
        }

    }

    private void initNumberButtons(GameView view) {
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
            number_buttons_.add(button);
        }

        GameLayout layout = view.getController().getLayout();
        int x = layout.getHandBottom().getX() + layout.getCardWidth() / 5;
        int y = layout.getHandBottom().getY() - 10 - layout.getSmallButtonSize();
        final int max_buttons_per_row = 3;
        number_buttons_.get(6).setCoordinate(x, y);
        int buttons_per_row = max_buttons_per_row;
        int amount_of_buttons = number_buttons_.size();
        for (int button_id = amount_of_buttons - 2; button_id >= 0; button_id--) {
            if (buttons_per_row >= max_buttons_per_row) {
                buttons_per_row = 0;
                x = layout.getHandBottom().getX() + layout.getCardWidth() / 5 + 2 * layout.getSmallButtonSize();
                y -= layout.getSmallButtonSize();
            }
            number_buttons_.get(button_id).setCoordinate(x, y);
            x -= layout.getSmallButtonSize();
            buttons_per_row++;
        }
    }

    public boolean getAnimationNumbers() {
        return animating_numbers_;
    }
    public void setAnimationNumbers(boolean animating) {
        animating_numbers_ = animating;
    }

    public boolean getAnimationSymbols() {
        return animating_smybols_;
    }
    public void setAnimationSymbols(boolean animating) {
        animating_smybols_ = animating;
    }

    public List<Button> getNumberButtons() {
        return number_buttons_;
    }

    public List<Button> getSymbolButtons() {
        return symbol_buttons_;
    }

    public Button getNumberButtonAt(int pos) {
        if (pos < number_buttons_.size()) {
            return number_buttons_.get(pos);
        }
        return null;
    }

    public Button getSymbolButtonAt(int pos) {
        if (pos < symbol_buttons_.size()) {
            return symbol_buttons_.get(pos);
        }
        return null;
    }


}
