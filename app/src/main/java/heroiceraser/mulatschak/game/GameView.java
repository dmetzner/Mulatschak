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

    private GameController controller_;

    private GameThread thread_;

    // if true, no more actions are allowed to allow a safe deletion of the game view
    public boolean stopAll = false;

    private SparseArray<PointF> mActivePointers;    // multiTouch

    public GameView(Context context) {
        super(context);
        controller_ = new GameController(this);
        thread_ = new GameThread(this);
        thread_.setRunning(true);
        thread_.start();
        mActivePointers = new SparseArray<>();
    }


    public void clear() {
        stopAll = true;
        if (thread_ != null) {
            getThread().setRunning(false);
            try {
                thread_.join();
            } catch (Exception e) {
                if (controller_.DEBUG) {
                    Log.w("GameThread", "Join Excpetion" + e);
                }
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


    private final Object drawLock = new Object();

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        synchronized (drawLock) {
            super.onDraw(canvas);
            if (stopAll || controller_ == null) {
                return;
            }

            drawBackground(canvas);

            if (controller_.isPlayerPresentationRunning()) {
                drawPlayerInfoPresentation(canvas);
                drawNonGamePlayUI(canvas); // order matters
                return;
            }

            if (!controller_.isDrawingEnabled()) {
                try {
                    drawPlayerInfo(canvas);
                    drawNonGamePlayUI(canvas);
                } catch (Exception e) {
                    return;
                }
                return;
            }

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
            }
        }
    }

    private synchronized void drawBackground(Canvas canvas) {
        controller_.getNonGamePlayUIContainer().drawBackground(canvas);
    }

    private synchronized void drawMultiplier(Canvas canvas) {
        controller_.getGamePlay().getMultiplierView().draw(canvas);
    }

    private synchronized void drawMulatschakResult(Canvas canvas) {
        controller_.getGamePlay().getMulatschakResultAnimation().draw(canvas, controller_);
    }

    private synchronized void drawDealingAnimation(Canvas canvas) {
        controller_.getGamePlay().getDealCards().getDealingAnimation().draw(canvas, controller_);
    }

    private synchronized void drawDecideMulatschak(Canvas canvas) {
        controller_.getGamePlay().getDecideMulatschak().draw(canvas, controller_);
    }

    private synchronized void drawMakeTrickBidsAnimation(Canvas canvas) {
        controller_.getGamePlay().getTrickBids().getMakeBidsAnimation().draw(canvas, controller_);
    }

    private synchronized void drawChooseTrumpAnimation(Canvas canvas) {
        controller_.getGamePlay().getChooseTrump().getChooseTrumpAnimation().draw(canvas, controller_);
    }

    private synchronized void drawExchangeCardsAnimation(Canvas canvas) {
        controller_.getGamePlay().getCardExchange().draw(canvas, controller_);
    }

    private synchronized void drawPlayACardAnimation(Canvas canvas) {
        controller_.getGamePlay().getPlayACardRound().draw(canvas, controller_);
    }

    private synchronized void drawPlayerInfo(Canvas canvas) {
        controller_.getPlayerInfo().draw(canvas);
    }

    private synchronized void drawPlayerInfoPresentation(Canvas canvas) {
        controller_.getPlayerInfo().drawPresentation(canvas, controller_);
    }

    private synchronized void drawDealerButton(Canvas canvas) {
        controller_.getDealerButton().draw(canvas, controller_);
    }

    private synchronized void drawNonGamePlayUI(Canvas canvas) {
        controller_.getNonGamePlayUIContainer().draw(canvas, controller_);
    }

    private synchronized void drawDiscardPile(Canvas canvas) {
        controller_.getDiscardPile().draw(canvas, controller_);
        controller_.getDiscardPile().drawOverlays(canvas, controller_);
    }

    private synchronized void drawBidsView(Canvas canvas) {
        controller_.getGamePlay().getTrickBids().getBidsView().draw(canvas, controller_);
    }

    private synchronized void drawTrumpView(Canvas canvas) {
        controller_.getGamePlay().getChooseTrump().getTrumpView().draw(canvas, controller_);
    }

    private synchronized void drawHandCards(Canvas canvas) {
        controller_.getPlayerHandsView().drawHandCards(canvas, controller_);
        controller_.getPlayerHandsView().drawMissATurn(canvas);
    }


    long time = 0;

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
                    if (time < System.currentTimeMillis() + 200) {
                        time = System.currentTimeMillis();
                        controller_.getTouchEvents().ActionDown(controller_, (int) f.x, (int) f.y);
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                        PointF point = mActivePointers.get(event.getPointerId(i));
                        if (point != null) {
                            point.x = event.getX(i);
                            point.y = event.getY(i);
                            if (time < System.currentTimeMillis() + 200) {
                                time = System.currentTimeMillis();
                                controller_.getTouchEvents().ActionMove(controller_,
                                        (int) point.x, (int) point.y);
                            }
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
                            if (time < System.currentTimeMillis() + 200) {
                                time = System.currentTimeMillis();
                                controller_.getTouchEvents().ActionUp(controller_,
                                        (int) point.x, (int) point.y);
                            }
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
}







