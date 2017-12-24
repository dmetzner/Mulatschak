package heroiceraser.mulatschak.game.GamePlay;

import heroiceraser.mulatschak.game.GamePlay.PlayACard.PlayACard;
import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 22.12.2017.
 */

public class GamePlay {

    PlayACard play_a_card_;

    public GamePlay() {
        play_a_card_ = new PlayACard();
    }

    public void init(GameView view) {
        play_a_card_.init(view);
    }

    public PlayACard getPlayACard() {
        return play_a_card_;
    }
}
