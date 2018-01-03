package heroiceraser.mulatschak.game.GamePlay.ChooseTrump;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;

//----------------------------------------------------------------------------------------------
//  Choose Trump Animation Class
//
public class ChooseTrumpAnimation {

    //----------------------------------------------------------------------------------------------
    //  MemberVariables
    //
    public static int MAX_TRUMP_ROWS = 2;
    public static int MAX_TRUMP_COLS = 2;

    private boolean animating_trumps_;
    private List<MyButton> trump_buttons;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    ChooseTrumpAnimation() {
        trump_buttons = new ArrayList<>();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        animating_trumps_ = false;

        String image_name = "trick_bids_button_trump_";
        GameLayout layout = view.getController().getLayout();
        for (int id = 1; id < MulatschakDeck.CARD_SUITS; id++) { // start at 1, no Joker
            MyButton button = new MyButton();
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


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas) {
        for (MyButton button : trump_buttons) {
            Bitmap bitmap = button.getBitmap();
            if (button.IsPressed()) {
                bitmap = button.getBitmapPressed();
            }
            canvas.drawBitmap(bitmap, button.getPosition().x,
                    button.getPosition().y, null);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  setTrump
    //                 -> called by Touch events, choose trump animation
    //                 -> ends choose trump animation
    //
    public void setTrump(GameController controller, int button_id) {
        controller.getLogic().setTrump(button_id + 1); // No Joker Button (card suit include joker)
        animating_trumps_ = false;
        controller.getGamePlay().getChooseTrump().getTrumpView()
                .startAnimation(controller.getLogic().getTrump(), controller.getLogic().getTrumpPlayerId(), controller);
    }


    //----------------------------------------------------------------------------------------------
    //  reEnableButtons
    //
    void reEnableButtons() {
        List<MyButton> buttons_sym = trump_buttons;
        for (int i = 0; i < buttons_sym.size(); i++) {
            buttons_sym.get(i).setEnabled(true);
        }
    }


    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public boolean getAnimationSymbols() {
        return animating_trumps_;
    }

    void turnOnAnimationTrumps() {
        animating_trumps_ = true;
    }

    public List<MyButton> getTrumpButtons() {
        return trump_buttons;
    }
}
