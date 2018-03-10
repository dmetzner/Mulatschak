package heroiceraser.mulatschak.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;


//--------------------------------------------------------------------------------------------------
//  The Game View Class
//                         ->   extends the View class
//                         ->   everything that gets drawn has to be called from the game views
//                              draw function
//                         ->   handles single touch, and a weakly implemented form of multi touch
//                              So all touch actions have to be called from this touch events
//
public class GameView extends View {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private GameController controller_;
    private GameThread thread_;
    public boolean stopAll;                         // if true, no more actions are allowed to
    //                                                  allow a safe deletion of the game view
    private SparseArray<PointF> mActivePointers;    // multiTouch


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameView(Context context) {
        super(context);
        controller_ = new GameController(this);
        thread_ = new GameThread(this);
        thread_.setRunning(true);
        thread_.start();
        stopAll = false;
        mActivePointers = new SparseArray<>();
    }


    //----------------------------------------------------------------------------------------------
    //  clear
    //
    public void clear() {
        if (thread_ != null) {
            getThread().setRunning(false);
            try {
                thread_.join();
            }
            catch (Exception e) {
                if (controller_.DEBUG) { Log.w("GameThread", "Join Excpetion" + e); }
            }
        }
        controller_.clear();
    }


    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public GameController getController() {
        return controller_;
    }


    //----------------------------------------------------------------------------------------------
    //  onDraw()
    //
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (stopAll || controller_ == null) {
            return;
        }

        if (controller_.isPlayerPresentationRunning()) {
            drawBackground(canvas);
            drawPlayerInfoPresentation(canvas);
            drawNonGamePlayUI(canvas); // order matters
            return;
        }

        if (!controller_.isDrawingEnabled()) {
            try {
                drawBackground(canvas);
                drawPlayerInfo(canvas);
                drawNonGamePlayUI(canvas);
            }
            catch (Exception e) {
                return;
            }
            return;
        }

        drawBackground(canvas);
         if (!controller_.getGameOver().isVisible()) {
             drawDiscardPile(canvas);
             drawMultiplier(canvas);
             drawBidsView(canvas);
             drawTrumpView(canvas);
             drawPlayerInfo(canvas);
             drawDealerButton(canvas);

             drawHandCards(canvas);

             drawDecideMulatschak(canvas);
             drawMakeTrickBidsAnimation(canvas);
             drawChooseTrumpAnimation(canvas);
             drawDealingAnimation(canvas);
             drawPlayACardAnimation(canvas);
             drawExchangeCardsAnimation(canvas);
             drawMulatschakResult(canvas);
         }
        drawNonGamePlayUI(canvas);

        thread_.now = System.currentTimeMillis();
        thread_.framesCount++;
        if (thread_.now - thread_.framesTimer > 1000) {
            thread_.framesTimer = thread_.now;
            thread_.framesCountAvg = thread_.framesCount;
            thread_.framesCount = 0;
            thread_.framesCount = 0;
        }
    }

    private void drawBackground(Canvas canvas) {
        controller_.getNonGamePlayUIContainer().drawBackground(canvas);
    }

    private void drawMultiplier(Canvas canvas) {
        controller_.getGamePlay().getMultiplierView().draw(canvas);
    }

    private void drawMulatschakResult(Canvas canvas) {
        controller_.getGamePlay().getMulatschakResultAnimation().draw(canvas, controller_);
    }

    private void drawDealingAnimation(Canvas canvas) {
        controller_.getGamePlay().getDealCards().getDealingAnimation().draw(canvas, controller_);
    }

    private void drawDecideMulatschak(Canvas canvas) {
        controller_.getGamePlay().getDecideMulatschak().draw(canvas, controller_);
    }

    private void drawMakeTrickBidsAnimation(Canvas canvas) {
        controller_.getGamePlay().getTrickBids().getMakeBidsAnimation().draw(canvas, controller_);
    }

    private void drawChooseTrumpAnimation(Canvas canvas) {
        controller_.getGamePlay().getChooseTrump().getChooseTrumpAnimation().draw(canvas, controller_);
    }

    private void drawExchangeCardsAnimation(Canvas canvas) {
        controller_.getGamePlay().getCardExchange().draw(canvas, controller_);
    }

    private void drawPlayACardAnimation(Canvas canvas) {
        controller_.getGamePlay().getPlayACardRound().draw(canvas, controller_);
    }

    private void drawPlayerInfo(Canvas canvas) {
        controller_.getPlayerInfo().draw(canvas);
    }

    private void drawPlayerInfoPresentation(Canvas canvas) {
        controller_.getPlayerInfo().drawPresentation(canvas, controller_);
    }

    private void drawDealerButton(Canvas canvas) {
        controller_.getDealerButton().draw(canvas, controller_);
    }

    private void drawNonGamePlayUI(Canvas canvas) {
        controller_.getNonGamePlayUIContainer().draw(canvas, controller_);
    }

    private void drawDiscardPile(Canvas canvas) {
        controller_.getDiscardPile().draw(canvas);
        controller_.getDiscardPile().drawOverlays(canvas, controller_);
    }

    private void drawBidsView(Canvas canvas) {
        controller_.getGamePlay().getTrickBids().getBidsView().draw(canvas, controller_);
    }

    private void drawTrumpView(Canvas canvas) {
        controller_.getGamePlay().getChooseTrump().getTrumpView().draw(canvas, controller_);
    }

    private void drawHandCards(Canvas canvas) {
        controller_.getPlayerHandsView().drawHandCards(canvas, controller_);
        controller_.getPlayerHandsView().drawMissATurn(canvas);
    }


    //----------------------------------------------------------------------------------------------
    //  onTouchEvent
    //
    @Override
    public synchronized boolean onTouchEvent(final MotionEvent event) {
        if (stopAll) {
            return false;
        }

        try {

            // get pointer index from the event object
            int pointerIndex = event.getActionIndex();

            // get pointer ID
            int pointerId = event.getPointerId(pointerIndex);

            // get masked (not specific to a pointer) action
            int maskedAction = event.getActionMasked();

            switch (maskedAction) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    PointF f = new PointF();
                    f.x = event.getX(pointerIndex);
                    f.y = event.getY(pointerIndex);
                    mActivePointers.put(pointerId, f);
                    controller_.getTouchEvents().ActionDown(controller_, (int) f.x, (int) f.y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                        PointF point = mActivePointers.get(event.getPointerId(i));
                        if (point != null) {
                            point.x = event.getX(i);
                            point.y = event.getY(i);
                            controller_.getTouchEvents().ActionMove(controller_,
                                    (int) point.x, (int) point.y);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:

                    try {
                        PointF point = mActivePointers.get(event.getPointerId(pointerId));

                        if (point != null) {
                            point.x = event.getX(pointerId);
                            point.y = event.getY(pointerId);
                            controller_.getTouchEvents().ActionUp(controller_,
                                    (int) point.x, (int) point.y);
                        }

                        mActivePointers.remove(pointerId);
                    } catch (Exception e) {
                        //can happen quite often
                    }
                    break;
            }
        } catch (Exception e) {
            //
        }
        return true;
    }


    //----------------------------------------------------------------------------------------------
    //  Thread Handling
    //
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
            if (controller_.DEBUG) { Log.w("GameThread", "Join Excpetion"); }
        }
    }

    public synchronized void disableUpdateCanvasThreadOnly4TouchEvents() {
        if (getThread().isKeepUiActive()) {
            disableUpdateCanvasThread();
        }
    }
}







