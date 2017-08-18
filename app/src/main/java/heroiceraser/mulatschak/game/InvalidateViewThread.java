package heroiceraser.mulatschak.game;

import android.view.View;

/**
 * Created by Daniel Metzner on 12.08.2017.
 */


public class InvalidateViewThread {
    private View view_;

    public InvalidateViewThread(View view) {
        view_ = view;

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(33);
                        view_.postInvalidate();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

/*
    private View view_;
    private int sleep_;
    private boolean run_;

    public InvalidateViewThread(View v){
        view_ = v;
        sleep_ = 100;
    }

    public void run() {
        run_ = true;
        while(run_){
            try {
                Thread.sleep(sleep_);
                view_.postInvalidate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void stop() {
        run_ = false;
    }
}

*/
