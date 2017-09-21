package heroiceraser.mulatschak.multi;

import android.os.Handler;
import android.util.Log;

import com.google.android.gms.games.multiplayer.Participant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import heroiceraser.mulatschak.DrawableBasicObjects.Button;
import heroiceraser.mulatschak.game.Animations.GameAnimation;
import heroiceraser.mulatschak.game.Animations.TrickBids;
import heroiceraser.mulatschak.game.DrawableObjects.ButtonBar;
import heroiceraser.mulatschak.game.DrawableObjects.CardStack;
import heroiceraser.mulatschak.game.DrawableObjects.DealerButton;
import heroiceraser.mulatschak.game.DrawableObjects.DiscardPile;
import heroiceraser.mulatschak.game.DrawableObjects.GameMenu;
import heroiceraser.mulatschak.game.DrawableObjects.GameOver;
import heroiceraser.mulatschak.game.DrawableObjects.GameStatistics;
import heroiceraser.mulatschak.game.DrawableObjects.GameTricks;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.DrawableObjects.RoundInfo;
import heroiceraser.mulatschak.game.EnemyLogic;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.Player;
import heroiceraser.mulatschak.game.TouchEvents;


/**
 * Created by Daniel Metzner on 10.08.2017.
 */

public class GameController2 {

    public static int NOT_SET = -9999;

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    public ArrayList<Participant> participants_;
    public Participant host_;
    public String my_participant_id_;

    private GameView2 view_;
    private GameLogic logic_;
    private GameLayout2 layout_;

    private List<Player2> player_list_;


    private GameAnimation animations_;
    private TouchEvents touch_events_;

    private ButtonBar button_bar_;
    private GameStatistics statistics_;
    private GameTricks tricks_;
    private GameMenu menu_;

    private RoundInfo round_info_;
    private GameOver game_over_;


    private MulatschakDeck deck_;
    private DiscardPile discardPile_;
    private CardStack trash_;
    private DealerButton dealer_button_;



    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameController2(GameView2 view) {
        view_ = view;
        logic_ = new GameLogic();
        layout_ = new GameLayout2();
        player_list_ = new ArrayList<>();

    }

    public void start(ArrayList<Participant> participants, String my_id, int lives) {
        participants_ = participants;
        if (participants != null) {
            host_ = participants_.get(0);
        }
        my_participant_id_ = my_id;

        layout_.init(view_);
        logic_.init(lives);
        initPlayers();
        setPlayerPositions();

    }

    private void initPlayers() {
        for (int i = 0; i < participants_.size(); i++) {
            Player2 new_player = new Player2(view_);
            new_player.setLives(logic_.getStartLives());
            new_player.participant_ = participants_.get(i);
            int player_id = 0;
            if (participants_.get(i).getParticipantId().equals(my_participant_id_)) {
                player_id = 0;
            }
            else {
                player_id = i;
            }
            player_list_.add(player_id, new_player);
        }
    }

    private void setPlayerPositions() {
        switch (getAmountOfPlayers()) {
            case 1:
                getPlayerById(0).setPosition(layout_.POSITION_BOTTOM);
                break;
            case 2:
                getPlayerById(0).setPosition(layout_.POSITION_BOTTOM);
                getPlayerById(1).setPosition(layout_.POSITION_TOP);
                break;
            case 3:
                getPlayerById(0).setPosition(layout_.POSITION_BOTTOM);
                getPlayerById(1).setPosition(layout_.POSITION_LEFT);
                getPlayerById(2).setPosition(layout_.POSITION_TOP);
                break;
            case 4:
                getPlayerById(0).setPosition(layout_.POSITION_BOTTOM);
                getPlayerById(1).setPosition(layout_.POSITION_LEFT);
                getPlayerById(2).setPosition(layout_.POSITION_TOP);
                getPlayerById(3).setPosition(layout_.POSITION_RIGHT);
                break;
        }
    }


    //----------------------------------------------------------------------------------------------
    //  Getter
    //
    public GameView2 getView() { return view_; }

    public GameLayout2 getLayout() { return layout_; }

    public GameAnimation getAnimation() { return  animations_; }

    public GameLogic getLogic() { return logic_; }

    public TouchEvents getTouchEvents() { return touch_events_; }

    public MulatschakDeck getDeck() {
        return deck_;
    }

    public DiscardPile getDiscardPile() {
        return discardPile_;
    }

    public List<Player2> getPlayerList() { return player_list_; }

    public Player2 getPlayerById(int id) { return player_list_.get(id); }

    public int getAmountOfPlayers() { return player_list_.size(); }

    public ButtonBar getButtonBar() {
        return button_bar_;
    }

    public GameStatistics getStatistics() {
        return statistics_;
    }

    public GameTricks getTricks() { return tricks_; }

    public GameMenu getMenu() { return  menu_; }

    public DealerButton getDealerButton() {
        return dealer_button_;
    }

    public CardStack getTrash() {
        return trash_;
    }

    public GameOver getGameOver() {
        return game_over_;
    }

    public RoundInfo getRoundInfo() {
        return round_info_;
    }
}
