package heroiceraser.mulatschak.game.DrawableObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

import com.google.android.gms.games.multiplayer.Participant;

import heroiceraser.mulatschak.DrawableBasicObjects.TextField;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 10.08.2017.
 */

public class MyPlayer {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    public Participant participant_;
    private int id_;
    private String display_name_;

    private GameView view_;
    private TextField textField_;

    private int position_;
    private int lives_;

    private int tricks_to_make_;
    private boolean miss_a_turn_;
    private CardStack hand_;
    private CardStack tricks_;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public MyPlayer(GameView view) {
        view_ = view;
        position_ = GameController.NOT_SET;
        lives_ = GameController.NOT_SET;
        tricks_to_make_ = GameController.NOT_SET;
        miss_a_turn_ = false;
        hand_ = new CardStack();
        tricks_ = new CardStack();
        initTextField();
    }

    private void initTextField() {
        textField_ = new TextField();
        String text = "";
        textField_.init(view_, text,
                (int)(view_.getController().getLayout().getScreenWidth() * (8.0 / 10.0)),
                Color.GREEN);

        if (participant_ != null) {
            text = participant_.getDisplayName();
        }
        else if (id_ == 0) {
            text = "Not signed in";
        }
        else {
            text = "enemie_bot" + id_;
        }
        textField_.update(text, 500);
    }

    public void draw(Canvas canvas, Point pos) {
        textField_.draw(canvas, pos);
    }

    public int getAmountOfTricks(GameController controller) {
        int amount = tricks_.getCardStack().size();
        int players = controller.getAmountOfPlayers();
        for (int i = 0; i < controller.getAmountOfPlayers(); i++) {
            if (miss_a_turn_) {
                players--;
            }
        }
        if (players != 0) {
            amount /= players;
        }

        return amount;
    }

    public void sortHandBasedOnPosition() {

        // sort based on x-coord
        if (position_ % 2 == 0) {
            CardStack.bubblesort(hand_, CardStack.xPosComperator);
        }
        // sort based on y-coord
        else {
            CardStack.bubblesort(hand_, CardStack.yPosComperator);
        }
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

    public int getTricksToMake() { return tricks_to_make_; }
    public void setTricksToMake(int trumphs_to_make) { tricks_to_make_ = trumphs_to_make; }

    public CardStack getTricks() {
        return tricks_;
    }

    public void setId(int id) {
        this.id_ = id;
    }

    public int getId() {
        return id_;
    }

    public void setDisplayName(String display_name) {
        this.display_name_ = display_name;
    }

    public String getDisplayName() {
        return display_name_;
    }
}

