package heroiceraser.mulatschak;

//--------------------------------------------------------------------------------------------------
//  Message class
//
public class Message {

    //----------------------------------------------------------------------------------------------
    //  Message class
    //
    public Message() { }


    //----------------------------------------------------------------------------------------------
    // Member Variables
    //
    public int type;
    public String senderId;
    public String data;


    //----------------------------------------------------------------------------------------------
    // Constants
    //
    // don't use 0 -> we use 0 to indicate no Message

    public final static int noMessage = 0;

    public final static int gameStateWaitForSetHost                = 0;
    public final static int gameStateWaitForStartGame           = 1;
    public final static int gameStateWaitForGameReady                = 2;
    public final static int gameStateWaitForShuffleDeck          = 3;
    public final static int gameStateWaitForMulatschakDecision  = 4;
    public final static int gameStateWaitForTrickBids           = 5;
    public final static int gameStateWaitForChooseTrump           = 6;
    public final static int gameStateWaitForCardExchange           = 7;
    public final static int gameStateWaitForPlayACard           = 8;
    public final static int gameStateWaitForPlayACard2           = 9;
    public final static int gameStateWaitForPlayACard3          = 10;
    public final static int gameStateWaitForPlayACard4          = 11;
    public final static int gameStateWaitForPlayACard5           = 12;
    public final static int gameStateWaitForNextRoundButton      = 13;
    public final static int gameStateWaitForNewRound           = 14;

    public final static int setHost                     = 1000;
    public final static int requestHost                 = 1001;

    public final static int prepareGame                 = 2000;
    public final static int startGame                   = 2001;
    public final static int requestStartGame            = 2002;

    // controller
    public final static int gameReady                   = 3000;
    public final static int requestGameReady            = 3001;

    public final static int shuffledDeck                = 4000;
    public final static int requestShuffledDeck         = 4001;

    public final static int mulatschakDecision          = 5000;
    public final static int requestMulatschakDecision   = 5001;

    public final static int trickBids                   = 6000;
    public final static int requestTrickBids            = 6001;

    public final static int chooseTrump                 = 7000;
    public final static int requestChooseTrump          = 7001;

    public final static int cardExchange                = 8000;
    public final static int requestCardExchange         = 8001;

    public final static int playACard                   = 9100;
    public final static int requestPlayACard            = 9101;

    public final static int playACard2                  = 9200;
    public final static int requestPlayACard2           = 9201;

    public final static int playACard3                   = 9300;
    public final static int requestPlayACard3            = 9301;

    public final static int playACard4                   = 9400;
    public final static int requestPlayACard4            = 9401;

    public final static int playACard5                   = 9500;
    public final static int requestPlayACard5            = 9501;

    public final static int waitForNewRound             = 10000;
    public final static int requestWaitForNewRound      = 10001;

    public final static int chatMessage = 10;
    public final static int leftGameAtEnd = 99;
    public final static int heartBeat = 33;

}
