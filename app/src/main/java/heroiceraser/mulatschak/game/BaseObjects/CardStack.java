package heroiceraser.mulatschak.game.BaseObjects;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;

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
        stack_ = new ArrayList<>();
    }


    //----------------------------------------------------------------------------------------------
    //  Sorting
    //
    static Comparator<Card> xPosComperator = new Comparator<Card>() {
        @Override
        public int compare(Card c1, Card c2) {
            return (c1.getPosition().x < c2.getPosition().x ? -1 :
                    (c1.getPosition().x == c2.getPosition().x ? 0 : 1));
        }
    };
    static Comparator<Card> yPosComperator = new Comparator<Card>() {
        @Override
        public int compare(Card c1, Card c2) {
            return (c1.getPosition().y < c2.getPosition().y ? -1 :
                    (c1.getPosition().y == c2.getPosition().y ? 0 : 1));
        }
    };

    static CardStack bubblesort(CardStack cs, Comparator<Card> comparator) {
        Card temp;
        for(int i = 1; i < cs.getCardStack().size(); i++) {
            for(int j = 0; j < cs.getCardStack().size() - i; j++) {
                if(comparator.compare(cs.getCardStack().get(j), cs.getCardStack().get(j + 1)) > 0) {
                    temp = cs.getCardAt(j);
                    cs.getCardStack().remove(j); // j+1 --> j
                    cs.getCardStack().add(j + 1, temp);
                }
            }
        }
        return cs;
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
        if (pos < stack_.size()) {
            return stack_.get(pos);
        }
        return null;
    }
}
