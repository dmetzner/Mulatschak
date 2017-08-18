package heroiceraser.mulatschak.game;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.helpers.Coordinate;

/**
 * Created by Daniel Metzner on 10.08.2017.
 */

//----------------------------------------------------------------------------------------------
//  A CardStack consists of an ArrayList of Cards
//
public class CardStack {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private Coordinate coordinate_;
    private List<Card> card_stack_;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public CardStack() {
        card_stack_ = new ArrayList<Card>();
        coordinate_ = new Coordinate();
    }


    //----------------------------------------------------------------------------------------------
    //  addCardToStack:
    //                  adds a Cart to the stack
    //
    public void addCard(Card card) {
        card_stack_.add(card);
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public List<Card> getCardStack() {
        return card_stack_;
    }

    public Card getCardAt(int pos) {
        return card_stack_.get(pos);
    }

    public Coordinate getCoordinate() { return coordinate_; }
    public void setCoordinate(Coordinate coordinate) { coordinate_ = new Coordinate(coordinate); }
    public void setCoordinate(int x, int y) { coordinate_ = new Coordinate(x, y); }

}
