package heroiceraser.mulatschak.game.GamePlay.ChooseTrump;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import java.util.ArrayList;
import java.util.List;
import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.game.BaseObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GamePlay.Background4Player0Animations;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.BitmapMethodes;

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
    private List<MyButton> trumpButtons;


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

        String image_name = "button_blue_metallic_small";

        for (int id = 1; id < MulatschakDeck.CARD_SUITS; id++) { // start at 1, no Joker
            MyButton button = new MyButton();
            int width = layout.getTrickBidsTrumpButtonSize().x;
            int height = layout.getTrickBidsTrumpButtonSize().y;
            if (view.getController().getDeck().getDesign() == MulatschakDeck.DD_DESIGN) {
                button.init(view, new Point(), new Point(width, height), image_name,
                        new Point((int) (width * 0.6), (int) (height * 0.6)), "trump_dd_" + id, "");
            }
            else {
                button.init(view, new Point(), new Point(width, height), image_name,
                        new Point((int) (width * 0.6), (int) (height * 0.6)), "trumps_basic_" + id, "");
            }

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
    public synchronized void draw(Canvas canvas, GameController controller) {
        if (!animatingTrumps) {
            return;
        }

        background.draw(canvas);
        controller.getPlayerHandsView().drawPlayer0Hand(canvas, controller);

        for (MyButton button : trumpButtons) {
            button.draw(canvas);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  reEnableButtons
    //
    void reEnableButtons() {
        List<MyButton> buttons_sym = trumpButtons;
        for (int i = 0; i < buttons_sym.size(); i++) {
            buttons_sym.get(i).setEnabled(true);
        }
    }


    //----------------------------------------------------------------------------------------------
    // Touch Events
    //                  trump buttons
    //
    synchronized public void touchEventDown(int X, int Y) {
        if (!animatingTrumps) {
            return;
        }
        for (int i = 0; i < trumpButtons.size(); i++) {
            trumpButtons.get(i).touchEventDown(X, Y);
        }
    }

    synchronized public void touchEventMove(int X, int Y) {
        if (!animatingTrumps) {
            return;
        }
        for (int i = 0; i < trumpButtons.size(); i++) {
            trumpButtons.get(i).touchEventMove(X, Y);
        }
    }

    synchronized public void touchEventUp(int X, int Y, GameController controller) {
        if (!animatingTrumps) {
            return;
        }
        for (int i = 0; i < trumpButtons.size(); i++) {
            if (trumpButtons.get(i).touchEventUp(X, Y)) {
                animatingTrumps = false;
                controller.getGamePlay().getChooseTrump().handleMainPlayersDecision(i, controller);
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
