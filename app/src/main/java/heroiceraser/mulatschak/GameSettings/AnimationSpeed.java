package heroiceraser.mulatschak.GameSettings;


//--------------------------------------------------------------------------------------------------
//  Animation Speed Class
//                          -> handles speed for all animations
//
public class AnimationSpeed {

    //----------------------------------------------------------------------------------------------
    //  Constants
    //
    public static final int SPEED_SLOW     = 4501;
    public static final int SPEED_NORMAL   = 4502;
    public static final int SPEED_FAST     = 4503;
    public static final int NO_ANIMATION   = 4504;


    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private double speed_setting_factor_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public AnimationSpeed() {}


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init() {
        setSpeed(SPEED_NORMAL);
    }


    //----------------------------------------------------------------------------------------------
    //  setSpeed
    //              -> switch case that configures the speed_factor
    //
    public void setSpeed(int speed) {
        switch (speed) {
            case SPEED_SLOW:
                speed_setting_factor_ = 1.3;
                break;

            case SPEED_NORMAL:
                speed_setting_factor_ = 0.9;
                break;

            case SPEED_FAST:
                speed_setting_factor_ = 0.5;
                break;

            case NO_ANIMATION:
                speed_setting_factor_ = 0;
                break;
        }
    }


    //----------------------------------------------------------------------------------------------
    //  getSpeedFactor
    //
    public double getSpeedFactor() {
        return speed_setting_factor_;
    }

}
