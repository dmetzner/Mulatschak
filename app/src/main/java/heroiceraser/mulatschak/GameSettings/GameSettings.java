package heroiceraser.mulatschak.GameSettings;

import heroiceraser.mulatschak.game.GameView;

//----------------------------------------------------------------------------------------------
//  GameSettings
//          -> Animation Speed
//          -> Todo
//
public class GameSettings {

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
