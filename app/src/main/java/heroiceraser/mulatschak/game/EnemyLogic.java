package heroiceraser.mulatschak.game;

import android.graphics.Canvas;
import android.widget.Toast;

import heroiceraser.mulatschak.game.DrawableObjects.DiscardPile;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;

/**
 * Created by Daniel Metzner on 21.08.2017.
 */

public class EnemyLogic {

    EnemyCardExchangeLogic enemyCardExchangeLogic;
    EnemyPlayACardLogic playACardLogic;

    public EnemyLogic() {
        enemyCardExchangeLogic = new EnemyCardExchangeLogic();
        playACardLogic = new EnemyPlayACardLogic();
    }


    public void init(GameView view) {
        playACardLogic.init(view.getController());
    }


    //----------------------------------------------------------------------------------------------
    //  TrickBids
    //
    public void makeTrickBids(Player player, GameController controller) {

        // ToDo: put in some fancy logic here

        int[] cards_per_trumph = new int[5];
        for (int i = 0; i < player.getAmountOfCardsInHand(); i++) {
            int trumph = (player.getHand().getCardAt(i).getId() / 100) % 5;
            cards_per_trumph[trumph]++;
        }

        int max = 0;
        for (int i = 0; i < cards_per_trumph.length; i++){
            if (cards_per_trumph[i] > max) {
                max = cards_per_trumph[i];
            }
        }

        if (max > 0) {
            max--;
        }

        controller.setNewMaxTrumps(0, player.getId());
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
        enemyCardExchangeLogic.exchangeCard(player, controller);
        player.sortHandBasedOnPosition();
    }



    //----------------------------------------------------------------------------------------------
    //  Play Card
    //
    public void playACard(GameLogic logic, Player player, DiscardPile discard_pile) {
        playACardLogic.playACard(logic, player, discard_pile);
    }

    public boolean isPlayACardAnimationRunning() {
        return playACardLogic.isAnimationRunning();
    }

    public void draw(Canvas canvas) {

        if (isPlayACardAnimationRunning()) {
            playACardLogic.draw(canvas);
        }

    }

}
