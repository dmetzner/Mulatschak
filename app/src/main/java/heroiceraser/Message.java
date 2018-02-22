package heroiceraser;

public class Message {

    public Message() {

    }

    public int type;
    public String senderId;
    public String data;



    //
    public final static int chatMessage = 0;
    public final static int shuffledDeck = 1;
    public final static int mulatschakDecision = 2;
    public final static int trickBids = 3;
    public final static int chooseTrump = 4;
    public final static int cardExchange = 5;
    public final static int playACard = 6;
}
