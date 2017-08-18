package heroiceraser.mulatschak.game;

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
    private CardStack hand_;
    private CardStack tricks_;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public Player(int id) {
        id_ = id;
        hand_ = new CardStack();
        tricks_ = new CardStack();
    }

    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public int getId() { return id_; }

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
}
