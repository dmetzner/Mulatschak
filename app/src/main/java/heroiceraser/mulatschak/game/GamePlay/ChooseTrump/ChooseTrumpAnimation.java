package heroiceraser.mulatschak.game.GamePlay.ChooseTrump;


import android.graphics.Canvas;
import android.graphics.Point;
import java.util.ArrayList;
import java.util.List;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextButton;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GamePlay.Background4Player0Animations;
import heroiceraser.mulatschak.game.GameView;

//----------------------------------------------------------------------------------------------
//  Choose Trump Animation Class
//
public class ChooseTrumpAnimation {

    //----------------------------------------------------------------------------------------------
    //  MemberVariables
    //
    public static int MAX_TRUMP_COLS = 2;
    private Background4Player0Animations background;
    private boolean animatingTrumps;
    private List<MyTextButton> trumpButtons;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    ChooseTrumpAnimation() {
        background = new Background4Player0Animations();
        trumpButtons = new ArrayList<>();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();
        animatingTrumps = false;

        background.init(layout);

        String image_name = "trick_bids_button_trump_";

        for (int id = 1; id < MulatschakDeck.CARD_SUITS; id++) { // start at 1, no Joker
            MyTextButton button = new MyTextButton();
            int width = layout.getTrickBidsTrumpButtonSize().x;
            int height = layout.getTrickBidsTrumpButtonSize().y;
            button.init(view, new Point(), width, height, image_name + id, "");
            trumpButtons.add(button);
        }

        int x = layout.getTrickBidsTrumpButtonPosition().x;
        int y = layout.getTrickBidsTrumpButtonPosition().y;
        int remove_drop_shadow = (int) (layout.getTrickBidsTrumpButtonSize().y / 16.0);

        trumpButtons.get(0).setPosition(x, y);
        trumpButtons.get(1).setPosition(x,
                y + layout.getTrickBidsTrumpButtonSize().y - remove_drop_shadow);
        trumpButtons.get(2).setPosition(x + layout.getTrickBidsTrumpButtonSize().x,
                y + layout.getTrickBidsTrumpButtonSize().y - remove_drop_shadow);
        trumpButtons.get(3).setPosition(x + layout.getTrickBidsTrumpButtonSize().x, y);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas, GameController controller) {
        if (!animatingTrumps) {
            return;
        }

        background.draw(canvas, controller);
        controller.getAnimateHands().drawPlayer0Hand(canvas, controller);

        for (MyTextButton button : trumpButtons) {
            button.draw(canvas);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  setTrump
    //                 -> called by Touch events, choose trump animation
    //                 -> ends choose trump animation
    //
    private void setTrump(GameController controller, int button_id) {
        controller.getLogic().setTrump(button_id + 1); // No Joker Button (card suit include joker)
        animatingTrumps = false;
        controller.getGamePlay().getChooseTrump().getTrumpView()
                .startAnimation(controller.getLogic().getTrump(), controller.getLogic().getTrumpPlayerId(), controller);
    }


    //----------------------------------------------------------------------------------------------
    //  reEnableButtons
    //
    void reEnableButtons() {
        List<MyTextButton> buttons_sym = trumpButtons;
        for (int i = 0; i < buttons_sym.size(); i++) {
            buttons_sym.get(i).setEnabled(true);
        }
    }


    //----------------------------------------------------------------------------------------------
    // Touch Events
    //                  trump buttons
    //
    public void touchEventDown(int X, int Y) {
        if (!animatingTrumps) {
            return;
        }
        for (int i = 0; i < trumpButtons.size(); i++) {
            trumpButtons.get(i).touchEventDown(X, Y);
        }
    }

    public void touchEventMove(int X, int Y) {
        if (!animatingTrumps) {
            return;
        }
        for (int i = 0; i < trumpButtons.size(); i++) {
            trumpButtons.get(i).touchEventMove(X, Y);
        }
    }

    public void touchEventUp(int X, int Y, GameController controller) {
        if (!animatingTrumps) {
            return;
        }
        for (int i = 0; i < trumpButtons.size(); i++) {
            if (trumpButtons.get(i).touchEventUp(X, Y)) {
                setTrump(controller, i);
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    void turnOnAnimationTrumps() {
        animatingTrumps = true;
    }
}
