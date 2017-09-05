package heroiceraser.mulatschak.game;

import heroiceraser.mulatschak.game.DrawableObjects.CardStack;

/**
 * Created by Daniel Metzner on 10.08.2017.
 */

public class Player {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private int id_;
    private int position_;
    private int lives_;
    private int trumphs_to_make_;
    private boolean miss_a_turn_;
    private CardStack hand_;
    private CardStack tricks_;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public Player(int id) {
        id_ = id;
        position_ = -1;
        lives_ = -1;
        trumphs_to_make_ = -1;
        miss_a_turn_ = false;
        hand_ = new CardStack();
        tricks_ = new CardStack();
    }

    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public int getId() { return id_; }

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

    public int getTrumphsToMake() { return trumphs_to_make_; }
    public void setTrumphsToMake(int trumphs_to_make) { trumphs_to_make_ = trumphs_to_make; }

    public CardStack getTricks() {
        return tricks_;
    }

}
