package heroiceraser.mulatschak.gameSettings;

import heroiceraser.mulatschak.game.GameView;

public class GameSettings {
    public int playerLives;
    public int maxLives;
    public int enemies;
    public int difficulty;
    public String cardDesign;


    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private AnimationSpeed animation_speed_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameSettings() {
        animation_speed_ = new AnimationSpeed();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        animation_speed_.init();
    }


    //----------------------------------------------------------------------------------------------
    //  Getter && Setter
    //
    public AnimationSpeed getAnimationSpeed() {
        return animation_speed_;
    }

    public void setAnimationSpeed(int speed) {
        animation_speed_.setSpeed(speed);
    }

}