package heroiceraser.mulatschak.game.NonGamePlayUI.AllCardsPlayed;

import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.MyPlayer;
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
        logic.resetMultiplier();

        // update lives for statistic tab
        ui.getStatistics().updatePlayerLives(controller);

        // check if game over
        if (go.isGameOver(controller)) {
            logic.setGameOver(true);
            ui.getRoundInfo().setInfoBoxEmpty();
            ui.getRoundInfo().getGameOver().setVisible(true);
            go.setVisible(true);                                // ToDo start animation??
            return;
        }

        // else prepare a new round
        ui.getRoundInfo().setInfoBoxEmpty();
        ui.getRoundInfo().getEndOfRound().setVisible(true);
        ui.getRoundInfo().updateEndOfRound(controller);

        controller.prepareNewRound();
        controller.waiting2 = true;
        // touch events starts new round        ^^  // todo
    }


}
