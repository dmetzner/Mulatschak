package heroiceraser.mulatschak.game.GamePlay.AllCardsPlayed;

import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;
import heroiceraser.mulatschak.game.NonGamePlayUI.GameOver.GameOver;
import heroiceraser.mulatschak.game.NonGamePlayUI.NonGamePlayUIContainer;


//----------------------------------------------------------------------------------------------
//  AllCardsPlayed
//                 -> handles the end of a round (no more cards on the field)
//                 -> update player lives, resets multiplicator
//                 -> checks for GAME over
//                 -> shows a new Round Button
//
public class AllCardsPlayed {


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public AllCardsPlayed() {  }


    //----------------------------------------------------------------------------------------------
    //  areAllCardsPlayed?
    //                    -> checks for all players if they still have cards in their hand
    //                     ... returns true if hands are empty, else false
    //
    public boolean areAllCardsPlayed(GameController controller) {

        boolean all_cards_played = true;

        for (MyPlayer myPlayer : controller.getPlayerList()) {
            if (myPlayer.getAmountOfCardsInHand() != 0) {
                all_cards_played = false;
            }
        }

        return all_cards_played;
    }


    //----------------------------------------------------------------------------------------------
    //  ToDO
    //
    public void allCardsArePlayedLogic(GameController controller) {

        // shortcuts
        GameLogic logic = controller.getLogic();
        NonGamePlayUIContainer ui = controller.getNonGamePlayUIContainer();
        GameOver go = controller.getGameOver();
        UpdatePlayerLives upl = new UpdatePlayerLives();

        // update player lives & reset multiplier
        upl.updatePlayerLives(controller);
        logic.resetMultiplier(controller);

        // update lives for statistic tab
        ui.getStatistics().updatePlayerLives(controller);

        // checkButton if game over
        if (go.isGameOver(controller)) {
            go.setGameGameOver(controller);                         // ToDo start animation??
            return;
        }

        // round is over, can't be the turn of anyone
        controller.getPlayerInfo().setActivePlayer(GameController.NOT_SET);
        controller.getPlayerInfo().setShowPlayer0Turn(false);

        controller.getNonGamePlayUIContainer().getAllCardsPlayedView().startAnimation(controller);
    }
}
