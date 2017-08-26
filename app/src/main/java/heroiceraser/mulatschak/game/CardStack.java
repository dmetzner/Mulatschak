package heroiceraser.mulatschak.game;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

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
    private Point coordinate_;
    private List<Card> card_stack_;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public CardStack() {
        card_stack_ = new ArrayList<Card>();
        coordinate_ = new Point();
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

    public Point getPoint() { return coordinate_; }
    public void setPoint(Point coordinate) { coordinate_ = new Point(coordinate); }
    public void setPoint(int x, int y) { coordinate_ = new Point(x, y); }

}
