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
    public final static int chatMessage = 10;
    public final static int shuffledDeck = 1;
    public final static int mulatschakDecision = 2;
    public final static int trickBids = 3;
    public final static int chooseTrump = 4;
    public final static int cardExchange = 5;
    public final static int playACard = 6;
    public final static int prepareGame = 9;
    public final static int startGame = 7;
    public final static int gameReady = 17;
    public final static int setHost = 8;
    public final static int leftGameAtEnd = 99;
}
