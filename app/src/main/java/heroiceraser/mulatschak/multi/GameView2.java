package heroiceraser.mulatschak.multi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.game.DrawableObjects.ButtonBar;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameThread;
import heroiceraser.mulatschak.game.Player;

/**
 * Created by Daniel Metzner on 08.08.2017.
 */

public class GameView2 extends View {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private Context context_;
    private GameController2 controller_;
    private GameThread thread_;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameView2(Context context) {
        super(context);
        context_ = context;
        controller_ = new GameController2(this);
        thread_ = new GameThread(this);
        thread_.setRunning(true);
        thread_.start();
    }

    //----------------------------------------------------------------------------------------------
    //  onDraw()
    //
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        drawPlayerNames(canvas);
        // drawBorder(canvas);

        thread_.now = System.currentTimeMillis();
        thread_.framesCount++;
        if (thread_.now - thread_.framesTimer > 1000) {
            thread_.framesTimer = thread_.now;
            thread_.framesCountAvg = thread_.framesCount;
            thread_.framesCount = 0;
        }
    }

    private void drawBorder(Canvas canvas) {
        // draw game middle
        canvas.drawBitmap(controller_.getButtonBar().getBorderUp(), 0,
                controller_.getLayout().getSectors().get(2).y, null);
        canvas.drawBitmap(controller_.getButtonBar().getBorderDown(), 0,
                controller_.getLayout().getSectors().get(4).y, null);
    }


    private void drawPlayerNames(Canvas canvas) {
        int y = 0;
        for (int i = 0; i < controller_.getAmountOfPlayers(); i++) {
            y += 100;
            controller_.getPlayerById(i).draw(canvas, new Point(100, y));
        }
    }


    //----------------------------------------------------------------------------------------------
    //  onTouchEvent
    //
    @Override
    public synchronized boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public GameController2 getController() {
        return controller_;
    }



    public GameThread getThread() {
        return thread_;
    }

    public void setThread(GameThread thread) {
        this.thread_ = thread;
    }

    public synchronized void enableUpdateCanvasThread() {

        disableUpdateCanvasThread();
        setThread(new GameThread(this));
        getThread().setRunning(true);
        getThread().start();
    }

    public synchronized void enableUpdateCanvasThreadOnly4TouchEvents() {
        if (getThread() == null || !getThread().isRunning()) {
            setThread(new GameThread(this));
            getThread().setRun(true);
            getThread().start();
            getThread().setKeepUiActive(true);
        }
    }

    public synchronized void disableUpdateCanvasThread() {
        if (thread_ == null) {
            return;
        }

        getThread().setRunning(false);
        try {
            thread_.join();
        }
        catch (Exception e) {
            Log.w("GameThread", "Join Excpetion");
        }
    }

    public synchronized void disableUpdateCanvasThreadOnly4TouchEvents() {
        if (getThread().isKeepUiActive()) {
            disableUpdateCanvasThread();
        }
    }
}







