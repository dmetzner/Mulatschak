package heroiceraser.mulatschak.game.GamePlay;

import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GamePlay.AllCardsPlayed.AllCardsPlayed;
import heroiceraser.mulatschak.game.GamePlay.CardExchange.CardExchange;
import heroiceraser.mulatschak.game.GamePlay.ChooseTrump.ChooseTrump;
import heroiceraser.mulatschak.game.GamePlay.DealCards.DealCards;
import heroiceraser.mulatschak.game.GamePlay.PlayACard.PlayACard;
import heroiceraser.mulatschak.game.GamePlay.TrickBids.TrickBids;
import heroiceraser.mulatschak.game.GameView;


//--------------------------------------------------------------------------------------------------
//  Game Play Class
//                      ToDo Explain me
//
public class GamePlay {


    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private DealCards deal_cards_;
    private TrickBids trick_bids_;
    private ChooseTrump choose_trump_;
    private CardExchange card_exchange_;
    private PlayACard play_a_card_;
    private AllCardsPlayed all_cards_played_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GamePlay() {
        deal_cards_ = new DealCards();
        trick_bids_ = new TrickBids();
        choose_trump_ = new ChooseTrump();
        card_exchange_ = new CardExchange();
        all_cards_played_ = new AllCardsPlayed();
        play_a_card_ = new PlayACard();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        //  deal_cards_.init(view);   NO init needed
        trick_bids_.init(view);
        choose_trump_.init(view);
        card_exchange_.init(view);
        play_a_card_.init(view);
        // all_cards_played_ -> needed only once in a while, generate everything at call
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void startRound(GameController controller) {
        trick_bids_.startRound(controller);
        choose_trump_.startRound(controller);
        // clean up ToDo for the rest
    }


    //----------------------------------------------------------------------------------------------
    //  Getter
    //
    public DealCards getDealCards() {
        return deal_cards_;
    }

    public TrickBids getTrickBids() {
        return trick_bids_;
    }

    public ChooseTrump getChooseTrump() {
        return choose_trump_;
    }

    public CardExchange getCardExchange() {
        return card_exchange_;
    }

    public PlayACard getPlayACard() {
        return play_a_card_;
    }

    public AllCardsPlayed getAllCardsPlayed() {
        return all_cards_played_;
    }
}
