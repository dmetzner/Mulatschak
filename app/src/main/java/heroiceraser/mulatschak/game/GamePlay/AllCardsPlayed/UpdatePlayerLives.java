package heroiceraser.mulatschak.game.GamePlay.AllCardsPlayed;

import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.DrawableObjects.MyPlayer;


//----------------------------------------------------------------------------------------------
//  UpdatePlayerLives
//                      -> updates the players lives
//                      -> 3 cases: Succeeded, Failed Muli & default
//                         * Succeeded & failed handle: trump player, missATurn, others
//                         * Default handle: missATurn, failed bid, no tricks, others
//
class UpdatePlayerLives {

    //----------------------------------------------------------------------------------------------
    //  Constants
    //
    private final int POINTS_MULI_ = 10;
    private final int POINTS_MISS_A_TURN_ = 2;
    private final int POINTS_LOST_ = 5;

    //----------------------------------------------------------------------------------------------
    //  Constants
    //
    UpdatePlayerLives() { }


    //----------------------------------------------------------------------------------------------
    //  updatePlayerLives
    //                      -> updates the players lives
    //                      -> 3 cases: Succeeded, Failed Muli & default
    //                         * Succeeded & failed handle: trump player, missATurn, others
    //                         * Default handle: missATurn, failed bid, no tricks, others
    //
    void updatePlayerLives(GameController controller) {

        GameLogic logic = controller.getLogic();

        // checkButton if it's a Mulatschak round
        if (logic.isMulatschakRound()) {

            // checkButton if the Mulatschak was successful
            boolean muli_succeeded = wasMuliSuccessful(controller);

            if (muli_succeeded) {
                successfulMuliRound(controller);
            }
            else  {
                failedMuliRound(controller);
            }
        }
        // normal rounds
        else {
            defaultRound(controller);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  checks if Muli was successful
    //                                 -> muli player has to have all won cards
    //
    private boolean wasMuliSuccessful(final GameController controller) {

        MyPlayer muli_My_player = controller.getPlayerById(controller.getLogic().getTrumpPlayerId());
        int muli_player_won_cards = muli_My_player.getTricks().getCardStack().size();
        int amount_of_players = controller.getAmountOfPlayers();

        return  (muli_player_won_cards / amount_of_players) == GameLogic.MAX_CARDS_PER_HAND;
    }


    //----------------------------------------------------------------------------------------------
    //  case for a successful Muli round
    //                                            -  Life Updates -
    //                                    -> trump player    - Muli Points * multiplicator
    //                                    -> miss a turn     + Miss Points * multiplicator
    //                                    -> others          + Muli Points * multiplicator
    //
    private void successfulMuliRound(GameController controller) {

        int multiplier = controller.getLogic().getMultiplier();
        int trump_player_id = controller.getLogic().getTrumpPlayerId();
        int add_lives;

        for (int i = 0; i < controller.getAmountOfPlayers(); i++) {
            if (i == trump_player_id) {
                add_lives = (-1) * POINTS_MULI_ * multiplier;
            }
            else {
                add_lives = POINTS_MULI_ * multiplier;
            }
            setNewPlayerLives(controller.getPlayerById(i), add_lives);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  case for a failed Muli round
    //                                            -  Life Updates -
    //                                    -> trump player    + Muli Points * multiplicator
    //                                    -> miss a turn     + Miss Points * multiplicator
    //                                    -> others          - Muli Points * multiplicator
    //
    private void failedMuliRound(GameController controller) {

        int multiplier = controller.getLogic().getMultiplier();
        int trump_player_id = controller.getLogic().getTrumpPlayerId();
        int add_lives;

        for (int i = 0; i < controller.getAmountOfPlayers(); i++) {
            if (i == trump_player_id) {
                add_lives = POINTS_MULI_ * multiplier;
            }
            else {
                add_lives = (-1) * POINTS_MULI_ * multiplier;
            }
            setNewPlayerLives(controller.getPlayerById(i), add_lives);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  default round
    //                                            -  Life Updates -
    //                                    -> miss a turn     + Miss Points     * multiplicator
    //                                    -> failed bid      + 2 * Lost Points * multiplicator
    //                                    -> no tricks       + Lost Points     * multiplicator
    //                                    -> others          - Muli Points     * multiplicator
    //
    private void defaultRound(GameController controller) {

        int multiplier = controller.getLogic().getMultiplier();
        int trump_player_id = controller.getLogic().getTrumpPlayerId();
        int add_lives;
        
        for (int i = 0; i < controller.getAmountOfPlayers(); i++) {

            MyPlayer myPlayer = controller.getPlayerById(i);
            int tricks = myPlayer.getAmountOfTricks(controller);

            if (myPlayer.getMissATurn()) {
                add_lives = POINTS_MISS_A_TURN_ * multiplier;
            }
            // trump myPlayer failed his bids
            else if (i == trump_player_id && tricks < controller.getLogic().getTricksToMake()) {
                add_lives = 2 * POINTS_LOST_ * multiplier;
            }
            // no tricks were made
            else if (tricks <= 0) {
                add_lives = POINTS_LOST_ * multiplier;
            }
            // default case
            else {
                add_lives = (-1) * tricks * multiplier;
            }
            setNewPlayerLives(myPlayer, add_lives);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  set new MyPlayer lives
    //                       -> lives can't reach a value below 0!
    //
    private void setNewPlayerLives(MyPlayer myPlayer, int additional_lives) {
        int new_lives = myPlayer.getLives() + additional_lives;
        if (new_lives <= 0 ) {
            new_lives = 0;
        }
        myPlayer.setLives(new_lives);
    }

}
