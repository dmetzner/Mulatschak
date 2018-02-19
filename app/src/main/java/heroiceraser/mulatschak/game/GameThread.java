package heroiceraser.mulatschak.game;

import android.view.View;

/**
 * Created by Daniel Metzner on 12.08.2017.
 */

public class GameThread extends Thread {
    private View gameView;
    private boolean run = false;
    private boolean keep_ui_active_;

    //Measure frames per second.
    public long now;
    public int framesCount=0;
    public int framesCountAvg=0;
    public long framesTimer=0;

    //Frame speed
    public long timeNow;
    public long timePrev = 0;
    public long timePrevFrame = 0;
    public  long timeDelta;

    public GameThread(View gameView) {
        this.gameView = gameView;
        keep_ui_active_ = false;
    }

    public void setRun(boolean run) { this.run = run; }

    public void setRunning(boolean run) {
        this.run = run;
        keep_ui_active_ = false;
    }

    public boolean isRunning() {
        return run;
    }

    public boolean isKeepUiActive() {
        return keep_ui_active_;
    }

    public void setKeepUiActive(boolean bool) {
        keep_ui_active_ = bool;
    }

    @Override
    public void run() {

        while (run) {
            //limit frame rate to max 24fps
            timeNow = System.currentTimeMillis();
            timeDelta = timeNow - timePrevFrame;
            if (timeDelta < 38) {
                try {
                    Thread.sleep(38 - timeDelta);
                }
                catch(InterruptedException ignored) {

                }
            }
            timePrevFrame = System.currentTimeMillis();
            gameView.postInvalidateOnAnimation();
        }
    }
}

