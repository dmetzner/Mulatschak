package heroiceraser.mulatschak.game.BaseObjects;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import heroiceraser.mulatschak.game.GameView;


//----------------------------------------------------------------------------------------------
//  A Mulatschak Deck consists out of 33 different cards:
//      1 joker: Weli
//      4 suits: diamonds, clubs, hearts, spades
//      8 cards per suit: 7, 8, 9, 10, Prince, Queen, King, Ace
//
public class MulatschakDeck extends CardStack {

    //--------------------------------------------------------------------------------------------------
    //  Constants for the naming convention of the card images
    //
    public final static int WELI = 0;            // IMG_NAME0 -> the Joker value 14
    public final static int JOKER = 14;
    public final static int DIAMOND = 1;            // IMG_NAME107 - IMG_NAME115
    public final static int CLUB = 2;            // IMG_NAME207 - IMG_NAME215
    public final static int HEART = 3;            // IMG_NAME307 - IMG_NAME315
    public final static int SPADE = 4;            // IMG_NAME407 - IMG_NAME415

    public final static String DD_DESIGN = "todo";
    //
    public final static int CARDS_PER_DECK = 33;
    public final static int CARD_SUITS = 5;
    private final static int FIRST_CARD = 7;
    public final static int LAST_CARD = 15;
    private final static int BACKSIDE = -1;


    private String design = "";

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //
    private Card backside_;

    //----------------------------------------------------------------------------------------------
    //  Constructor:
    //
    public MulatschakDeck() {
        super();
    }

    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public Bitmap getBacksideBitmap() {
        return backside_.getBitmap();
    }

    //----------------------------------------------------------------------------------------------
    //  initDeck
    //            initialises the mulatschak deck
    //
    public void initDeck(GameView view, String cardDesign) {

        setWidth(0);
        setHeight(0);
        setPosition(view.getController().getLayout().getDeckPosition());
        String IMG_PRE_NAME = "card";
        backside_ = new Card(BACKSIDE);
        String img_name = IMG_PRE_NAME + "_back";
        backside_.initCard(view, img_name);

        for (int i = 0; i < CARD_SUITS; i++) {
            if (i == WELI) {
                Card card = new Card(JOKER);
                img_name = IMG_PRE_NAME + WELI + JOKER;
                card.initCard(view, img_name);
                addCard(card);
            } else {
                for (int j = FIRST_CARD; j <= LAST_CARD; j++) {
                    if (j == JOKER) {
                        continue;
                    }
                    int id = (i * 100) + j;
                    Card card = new Card(id);
                    img_name = IMG_PRE_NAME + id;
                    card.initCard(view, img_name);
                    addCard(card);
                }
            }
        }

        if (getCardStack().size() != CARDS_PER_DECK) {
            if (view.getController().DEBUG) {
                Log.e("Mulatschak Deck", "Not enough Cards");
            }
        }

        setVisible(true);
    }


    public void shuffleDeck() {
        Collections.shuffle(getCardStack(), new Random());
    }

    public void sortByIdList(ArrayList<Integer> cardIds) {
        List<Card> tmp = new ArrayList<>();
        for (int i = 0; i < cardIds.size(); i++) {
            for (int deckId = 0; deckId < getCardStack().size(); deckId++) {
                if (cardIds.get(i) == getCardAt(deckId).getId()) {
                    tmp.add(getCardAt(deckId));
                }
            }
        }
        setCardStack(tmp);
    }


    public static int getCardSuit(Card card) {
        return (card.getId() / 100) % CARD_SUITS;
    }

    public static int getCardValue(Card card) {
        return (card.getId() % 100);
    }

    public String getDesign() {
        return design;
    }
}