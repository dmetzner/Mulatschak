package heroiceraser.mulatschak.game.GamePlay;

import android.graphics.Canvas;
import android.widget.Toast;

import heroiceraser.mulatschak.game.DrawableObjects.DiscardPile;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GamePlay.CardExchange.EnemyCardExchange;
import heroiceraser.mulatschak.game.GamePlay.PlayACard.EnemyPlayACardLogic;
import heroiceraser.mulatschak.game.GamePlay.TrickBids.EnemyTrickBids;
import heroiceraser.mulatschak.game.GamePlay.TrickBids.TrickBids;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.Player;

/**
 * Created by Daniel Metzner on 21.08.2017.
 */

public class EnemyLogic {

    private int player_id_;
    private EnemyCardExchange card_exchange_logic_;
    private EnemyPlayACardLogic playACardLogic;
    private EnemyTrickBids trick_bids_;

    public EnemyLogic(int player_id) {
        trick_bids_ = new EnemyTrickBids();
        card_exchange_logic_ = new EnemyCardExchange();
        playACardLogic = new EnemyPlayACardLogic();
        player_id_ = player_id;
    }

    public void init(GameView view) {
        playACardLogic.init(view.getController());
    }


    //----------------------------------------------------------------------------------------------
    //  TrickBids
    //
    public void makeTrickBids(Player player, GameController controller) {

        // ToDo: put in some fancy logic here
        trick_bids_.makeTrickBids(player, controller);
    }


    //----------------------------------------------------------------------------------------------
    //  chooseTrump
    //
    public void chooseTrump(Player player, GameLogic logic, GameView view) {
        // ToDo: put in some fancy logic here

        int[] cards_per_suit = new int[MulatschakDeck.CARD_SUITS];

        // init array with zeros;
        for (int i = 0; i < cards_per_suit.length; i++) {
            cards_per_suit[i] = 0;
        }

        // count cards per suit
        for (int i = 0; i < player.getAmountOfCardsInHand(); i++) {
            int suit = MulatschakDeck.getCardSuit(player.getHand().getCardAt(i));
            cards_per_suit[suit]++;
        }

        int trump_to_set = 0;
        for (int i = 0; i < cards_per_suit.length; i++) {
            if (cards_per_suit[i] > trump_to_set) {
                trump_to_set = i;
            }
            if (cards_per_suit[i] == trump_to_set) {
                // ToDo choose Better cards
            }
        }

        logic.setTrump(trump_to_set);

        // DEBUG ////////////////////////////////////////////////////////////////////////////////////////
        CharSequence text = "player: " + player.getId() + " choose trumph " + 1;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(view.getContext(), text, duration);
        toast.show();//////////////////////////////////////////////////////////////////////////////////
    }

    //----------------------------------------------------------------------------------------------
    //  chooseTrump
    //

    public void makeCardExchange(Player player, GameController controller) {
        card_exchange_logic_.exchangeCard(player, controller);
        player.sortHandBasedOnPosition();
    }



    //----------------------------------------------------------------------------------------------
    //  Play Card
    //
    public void playACard(GameLogic logic, Player player, DiscardPile discard_pile) {
        playACardLogic.playACard(logic, player, discard_pile);
    }

    public boolean isAnimationRunning() {
        return isPlayACardAnimationRunning() || isCardExchangeAnimationRunning();
    }

    public boolean isPlayACardAnimationRunning() {
        return playACardLogic.isAnimationRunning();
    }

    public boolean isCardExchangeAnimationRunning() {
        return card_exchange_logic_.isAnimationRunning();
    }


    public void draw(Canvas canvas, GameController controller) {

        if (isPlayACardAnimationRunning()) {
            playACardLogic.draw(canvas);
        }

        if (isCardExchangeAnimationRunning()) {
            card_exchange_logic_.draw(canvas, controller);
        }

    }

}
