package heroiceraser.mulatschak.game;

import android.graphics.Bitmap;
import android.support.compat.BuildConfig;
import android.view.View;

import java.util.List;

import heroiceraser.mulatschak.helpers.Coordinate;

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
    //      WELI      = 0;            // IMG_NAME0 -> the Joker
    //      DIAMONDS  = 1;            // IMG_NAME107 - IMG_NAME114
    //      CLUBS     = 2;            // IMG_NAME207 - IMG_NAME214
    //      HEARTS    = 3;            // IMG_NAME307 - IMG_NAME314
    //      SPADES    = 4;            // IMG_NAME407 - IMG_NAME414
    //
    private final String PACKAGE_NAME = "drawable";
    private final String IMG_PRE_NAME = "card";
    public final static int CARDS_PER_DECK = 33;
    public final static int CARD_SUITS = 4;
    public final static int FIRST_CARD = 7;
    public final static int LAST_CARD = 14;
    public final static int JOKER = 0;
    public final static int BACKSIDE = -1;

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //
    private Card backside_;
    private Card backside_rotated_;

    //----------------------------------------------------------------------------------------------
    //  Constructor:
    //
    public MulatschakDeck() { }

    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public List<Card> getStack() {  return getCardStack(); }
    public Bitmap getBacksideBitmap() { return backside_.getBitmap(); }

    //----------------------------------------------------------------------------------------------
    //  initDeck
    //            initialises the mulatschak deck
    //
    public void initDeck(GameView view) {
        // backside cards
        backside_ = new Card(BACKSIDE);
        String img_name = IMG_PRE_NAME + "_back";
        backside_.initCard(view, img_name, PACKAGE_NAME);
        backside_rotated_ = new Card(BACKSIDE);
        backside_rotated_.initCard(view, img_name, PACKAGE_NAME);

        for (int i = 0; i <= CARD_SUITS; i++) {
            if (i == JOKER) {
                Card card = new Card(JOKER);
                img_name = IMG_PRE_NAME + JOKER;
                card.initCard(view, img_name, PACKAGE_NAME);
                addCard(card);
            }
            else {
                for (int j = FIRST_CARD; j <= LAST_CARD; j++) {
                    int id = (i * 100) + j;
                    Card card = new Card(id);
                    img_name = IMG_PRE_NAME + id;
                    card.initCard(view, img_name, PACKAGE_NAME);
                    addCard(card);
                }
            }
        }
        if (BuildConfig.DEBUG && getStack().size() == CARDS_PER_DECK) {
            throw new AssertionError("MulatschakDeck: 'decksize != CARDS_PER_DECK' Error");
        }
    }

}
