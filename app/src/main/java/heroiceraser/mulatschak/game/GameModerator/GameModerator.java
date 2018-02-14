package heroiceraser.mulatschak.game.GameModerator;

import heroiceraser.mulatschak.game.NonGamePlayUI.ChatView.ChatView;

//--------------------------------------------------------------------------------------------------
//  Game Moderator Class
//                          -> ToDo , can be improved but works for now
//
public class GameModerator {

    private final String NAME = "Moderator";

    //----------------------------------------------------------------------------------------------
    //  start Round Message
    //
    public void startRoundMessage(ChatView chatView, String dealerName) {
        chatView.addMessage("", "Neue Runde startet!");
        chatView.addMessage("", "Der Dealer ist " + dealerName +".");
    }
}
