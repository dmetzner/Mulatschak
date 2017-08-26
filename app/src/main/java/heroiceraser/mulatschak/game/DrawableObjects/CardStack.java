package heroiceraser.mulatschak.game.DrawableObjects;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;

/**
 * Created by Daniel Metzner on 10.08.2017.
 */

//----------------------------------------------------------------------------------------------
//  A CardStack consists of an ArrayList of Cards
//
public class CardStack extends DrawableObject {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private List<Card> stack_;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public CardStack() {
        super();
        stack_ = new ArrayList<Card>();
    }


    //----------------------------------------------------------------------------------------------
    //  addCardToStack:
    //                  adds a Cart to the stack
    //
    public void addCard(Card card) {
        stack_.add(card);
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public List<Card> getCardStack() {
        return stack_;
    }

    public Card getCardAt(int pos) {
        return stack_.get(pos);
    }

}
