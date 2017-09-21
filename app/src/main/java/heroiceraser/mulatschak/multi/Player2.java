package heroiceraser.mulatschak.multi;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

import com.google.android.gms.games.multiplayer.Participant;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.DrawableBasicObjects.TextField;
import heroiceraser.mulatschak.game.DrawableObjects.CardStack;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 10.08.2017.
 */

public class Player2 {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    public Participant participant_;
    private GameView2 view_;

    private int position_;
    private int lives_;

    private int tricks_to_make_;
    private boolean miss_a_turn_;
    private CardStack hand_;
    private CardStack tricks_;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public Player2(GameView2 view) {
        view_ = view;
        position_ = GameController.NOT_SET;
        lives_ = GameController.NOT_SET;
        tricks_to_make_ = GameController.NOT_SET;
        miss_a_turn_ = false;
        hand_ = new CardStack();
        tricks_ = new CardStack();
    }

    public void draw(Canvas canvas, Point pos) {
        TextField textField = new TextField();
        String text = participant_.getDisplayName();
        textField.init(view_, text, 300, Color.GREEN);
        textField.draw(canvas, pos);
    }

    public int getAmountOfTricks(GameController controller) {
        int amount = tricks_.getCardStack().size();
        int players = controller.getAmountOfPlayers();
        for (int i = 0; i < controller.getAmountOfPlayers(); i++) {
            if (controller.getPlayerById(i).getMissATurn()) {
                players--;
            }
        }
        amount /= players;
        return amount;
    }

    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //

    public boolean getMissATurn() {
        return miss_a_turn_;
    }

    public void setMissATurn(boolean miss_a_turn) {
        this.miss_a_turn_ = miss_a_turn;
    }

    public int getPosition() {
        return position_;
    }
    public void setPosition(int position) {
        position_ = position;
    }

    public CardStack getHand(){
        return hand_;
    }

    public int getAmountOfCardsInHand() {
        return hand_.getCardStack().size();
    }

    public int getLives() { return lives_; }
    public void setLives(int lives) { lives_ = lives; }

    public int getTrickToMake() { return tricks_to_make_; }
    public void setTricksToMake(int trumphs_to_make) { tricks_to_make_ = trumphs_to_make; }

    public CardStack getTricks() {
        return tricks_;
    }

}
