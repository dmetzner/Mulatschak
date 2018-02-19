package heroiceraser.mulatschak.game.GamePlay;

import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GamePlay.AllCardsPlayed.AllCardsPlayed;
import heroiceraser.mulatschak.game.GamePlay.Mulatschak.MulatschakResultAnimation;
import heroiceraser.mulatschak.game.GamePlay.CardExchange.CardExchange;
import heroiceraser.mulatschak.game.GamePlay.ChooseTrump.ChooseTrump;
import heroiceraser.mulatschak.game.GamePlay.DealCards.DealCards;
import heroiceraser.mulatschak.game.GamePlay.PlayACard.PlayACardRound;
import heroiceraser.mulatschak.game.GamePlay.Mulatschak.DecideMulatschak;
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
    private DealCards dealCards;
    private DecideMulatschak decideMulatschak;
    private TrickBids trickBids;
    private ChooseTrump chooseTrump;
    private CardExchange cardExchange;
    private PlayACardRound playACardRound;
    private AllCardsPlayed allCardsPlayed;
    private MulatschakResultAnimation mulatschakResultAnimation;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GamePlay() {
        dealCards = new DealCards();
        decideMulatschak = new DecideMulatschak();
        trickBids = new TrickBids();
        chooseTrump = new ChooseTrump();
        cardExchange = new CardExchange();
        allCardsPlayed = new AllCardsPlayed();
        playACardRound = new PlayACardRound();
        mulatschakResultAnimation = new MulatschakResultAnimation();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        //  dealCards.init(view);   NO init needed
        decideMulatschak.init(view);
        trickBids.init(view);
        chooseTrump.init(view);
        cardExchange.init(view);
        playACardRound.init(view);
        // allCardsPlayed -> needed only once in a while, generate everything at call
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void startRound(GameController controller) {
        trickBids.startRound(controller);
        chooseTrump.startRound();
        mulatschakResultAnimation.remove();
        decideMulatschak.startRound();
        // clean up ToDo for the rest
    }


    //----------------------------------------------------------------------------------------------
    //  Getter
    //
    public DealCards getDealCards() {
        return dealCards;
    }

    public DecideMulatschak getDecideMulatschak() {
        return decideMulatschak;
    }

    public TrickBids getTrickBids() {
        return trickBids;
    }

    public ChooseTrump getChooseTrump() {
        return chooseTrump;
    }

    public CardExchange getCardExchange() {
        return cardExchange;
    }

    public PlayACardRound getPlayACardRound() {
        return playACardRound;
    }

    public AllCardsPlayed getAllCardsPlayed() {
        return allCardsPlayed;
    }

    public MulatschakResultAnimation getMulatschakResultAnimation() {
        return mulatschakResultAnimation;
    }
}
