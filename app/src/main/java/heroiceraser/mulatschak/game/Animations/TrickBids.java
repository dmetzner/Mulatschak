package heroiceraser.mulatschak.game.Animations;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.Button;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 19.08.2017.
 */

// ToDo Aussetzen einbauen

public class TrickBids {

    // for the layout
    public static int MAX_BID_ROWS = 3;
    public static int MAX_BID_COLS = 4;
    public static int MAX_TRUMP_ROWS = 2;
    public static int MAX_TRUMP_COLS = 2;

    //
    public static int MULATSCHAK = 5;
    public static int MISS_A_TURN = -1;
    public static int NO_CARD = 0;

    private boolean animating_numbers_;
    private boolean animating_trumps_;
    private List<Button> number_buttons_;
    private List<Button> trump_buttons;

    public TrickBids() {
        animating_numbers_ = false;
        number_buttons_ = new ArrayList<>();
        animating_trumps_ = false;
        trump_buttons = new ArrayList<>();
    }

    public void init(GameView view) {
        initNumberButtons(view);
        initTrumpButtons(view);
    }

    private void initNumberButtons(GameView view) {
        String image_name = "trick_bids_button_";
        GameLayout layout = view.getController().getLayout();
        int x = layout.getTrickBidsNumberButtonPosition().x;
        int y = layout.getTrickBidsNumberButtonPosition().y;
        int max_buttons_per_row = 4;
        int button_per_row = 0;
        for (int button_id = MISS_A_TURN; button_id <= MULATSCHAK; button_id++) {
            Button button = new Button();
            int width = view.getController().getLayout().getTrickBidsNumberButtonSize().x;
            int height = view.getController().getLayout().getTrickBidsNumberButtonSize().y;

            if(button_id == MISS_A_TURN) {
                width *= 4;
                button_per_row += 4;
                button.init(view, new Point(x, y), width, height, image_name + "x");
            }
            else if (button_id == NO_CARD || button_id ==  MULATSCHAK) {
                width *= 2;
                button_per_row += 2;
                button.init(view, new Point(x, y), width, height, image_name + button_id);
            }
            else {
                button_per_row++;
                button.init(view, new Point(x, y), width, height, image_name + button_id);
            }
            number_buttons_.add(button);

            if (button_per_row >= max_buttons_per_row) {
                x = layout.getHandBottom().x;
                int remove_button_drop_shadow = (int) (layout.getTrickBidsNumberButtonSize().y / 18.0);
                y += layout.getTrickBidsNumberButtonSize().y - remove_button_drop_shadow;
                button_per_row = 0;
            }
            else {
                x += width;
            }
        }
    }

    private void initTrumpButtons(GameView view) {
        String image_name = "trick_bids_button_trump_";
        GameLayout layout = view.getController().getLayout();
        for (int id = 1; id < MulatschakDeck.CARD_SUITS; id++) { // start at 1, no Joker
            Button button = new Button();
            int width = layout.getTrickBidsTrumpButtonSize().x;
            int height = layout.getTrickBidsTrumpButtonSize().y;
            button.init(view, new Point(), width, height, image_name + id);
            trump_buttons.add(button);
        }

        int x = layout.getTrickBidsTrumpButtonPosition().x;
        int y = layout.getTrickBidsTrumpButtonPosition().y;
        int remove_drop_shadow = (int) (layout.getTrickBidsTrumpButtonSize().y / 16.0);

        trump_buttons.get(0).setPosition(x, y);
        trump_buttons.get(1).setPosition(x,
                y + layout.getTrickBidsTrumpButtonSize().y - remove_drop_shadow);
        trump_buttons.get(2).setPosition(x + layout.getTrickBidsTrumpButtonSize().x,
                y + layout.getTrickBidsTrumpButtonSize().y - remove_drop_shadow);
        trump_buttons.get(3).setPosition(x + layout.getTrickBidsTrumpButtonSize().x, y);
        
    }

    public void setTricks(GameController controller, int button_id) {
        animating_numbers_ = false;
        button_id--;  // because of miss a turn button
        if (button_id == MISS_A_TURN) {
            controller.makeTrickBids();
            return;
        }
        int player_id = 0;
        controller.setNewMaxTrumphs(button_id, player_id);
        controller.makeTrickBids();
    }
    
    public void setTrump(GameController controller, int button_id) {
        controller.getLogic().setTrump(button_id + 1); // No Joker Button
        setAnimationTrumps(false);
        controller.continueAfterTrumpWasChoosen();
    }

    //
    // Getter & Setter
    //
    public boolean getAnimationNumbers() {
        return animating_numbers_;
    }
    public void setAnimationNumbers(boolean animating) {
        animating_numbers_ = animating;
    }

    public boolean getAnimationSymbols() {
        return animating_trumps_;
    }
    public void setAnimationTrumps(boolean animating) {
        animating_trumps_ = animating;
    }

    public List<Button> getNumberButtons() {
        return number_buttons_;
    }

    public List<Button> getTrumpButtons() {
        return trump_buttons;
    }

    public Button getNumberButtonAt(int pos) {
        if (pos < number_buttons_.size()) {
            return number_buttons_.get(pos);
        }
        return null;
    }

    public Button getTrumpButtonAt(int pos) {
        if (pos < trump_buttons.size()) {
            return trump_buttons.get(pos);
        }
        return null;
    }


}
