package heroiceraser.mulatschak.game.DrawableObjects;

import android.graphics.Bitmap;
import android.util.Log;

import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 08.08.2017.
 */

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
    public final static int WELI     = 0;            // IMG_NAME0 -> the Joker value 14
    private final int JOKER          = 14;
    public final static int DIAMOND  = 1;            // IMG_NAME107 - IMG_NAME115
    public final static int CLUB     = 2;            // IMG_NAME207 - IMG_NAME215
    public final static int HEART    = 3;            // IMG_NAME307 - IMG_NAME315
    public final static int SPADE    = 4;            // IMG_NAME407 - IMG_NAME415
    //
    private final String PACKAGE_NAME = "drawable";
    private final String IMG_PRE_NAME = "card";
    public final static int CARDS_PER_DECK = 33;
    public final static int CARD_SUITS = 5;
    public final static int FIRST_CARD = 7;
    public final static int LAST_CARD = 15;
    public final static int BACKSIDE = -1;

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
    public Bitmap getBacksideBitmap() { return backside_.getBitmap(); }

    //----------------------------------------------------------------------------------------------
    //  initDeck
    //            initialises the mulatschak deck
    //
    public void initDeck(GameView view) {
        // backside cards
        setWidth(0);
        setHeight(0);
        setPosition(view.getController().getLayout().getDeckPosition());
        backside_ = new Card(BACKSIDE);
        String img_name = IMG_PRE_NAME + "_back";
        backside_.initCard(view, img_name, PACKAGE_NAME);

        for (int i = 0; i < CARD_SUITS; i++) {
            if (i == WELI) {
                Card card = new Card(JOKER);
                img_name = IMG_PRE_NAME + WELI + JOKER;
                card.initCard(view, img_name, PACKAGE_NAME);
                addCard(card);
            }
            else {
                for (int j = FIRST_CARD; j <= LAST_CARD; j++) {
                    if (j == JOKER) {
                        continue;
                    }
                    int id = (i * 100) + j;
                    Card card = new Card(id);
                    img_name = IMG_PRE_NAME + id;
                    card.initCard(view, img_name, PACKAGE_NAME);
                    addCard(card);
                }
            }
        }

        if (getCardStack().size() != CARDS_PER_DECK) {
            Log.e("Mulatschak Deck", "Not enough Cards");
        }

        setVisible(true);
    }

    public static int getCardSuit(Card card) {
        return (card.getId() / 100) % CARD_SUITS;
    }

    public static int getCardValue(Card card) {
        return (card.getId() % 100);
    }

}
