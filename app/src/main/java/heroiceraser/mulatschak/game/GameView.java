package heroiceraser.mulatschak.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import heroiceraser.mulatschak.game.DrawableObjects.MyPlayer;

/**
 * Created by Daniel Metzner on 08.08.2017.
 */

public class GameView extends View {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private Context context_;
    private GameController controller_;
    private GameThread thread_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameView(Context context) {
        super(context);
        context_ = context;
        controller_ = new GameController(this);
        thread_ = new GameThread(this);
        thread_.setRunning(true);
        thread_.start();
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

        if (controller_ == null || !controller_.isDrawingEnabled()) {
            return;
        }

        drawDiscardPile(canvas);
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

        drawGameOver(canvas);
        drawRoundInfo(canvas);
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
        controller_.getGamePlay().getPlayACard().draw(canvas, controller_);
    }

    private void drawPlayerInfo(Canvas canvas) {
        controller_.getPlayerInfo().draw(canvas);
    }


    private void drawGameOver(Canvas canvas) {
        controller_.getGameOver().draw(canvas, controller_);
    }

    private void drawRoundInfo(Canvas canvas) {
        controller_.getRoundInfo().draw(canvas);
    }

    private void drawDealerButton(Canvas canvas) {
        controller_.getDealerButton().draw(canvas, controller_);
    }

    private void drawNonGamePlayUI(Canvas canvas) {
        controller_.getNonGamePlayUIContainer().draw(canvas, controller_);
    }


    //----------------------------------------------------------------------------------------------
    //  drawDiscardPile
    //
    private void drawDiscardPile(Canvas canvas) {
        controller_.getDiscardPile().draw(canvas);
        controller_.getDiscardPile().drawOverlays(canvas, controller_.getLogic());
    }


    //----------------------------------------------------------------------------------------------
    //  drawBidsView
    //
    private void drawBidsView(Canvas canvas) {
        controller_.getGamePlay().getTrickBids().getBidsView().draw(canvas, controller_);
    }


    //----------------------------------------------------------------------------------------------
    //  drawBidsView
    //
    private void drawTrumpView(Canvas canvas) {
        controller_.getGamePlay().getChooseTrump().getTrumpView().draw(canvas, controller_);
    }

    //----------------------------------------------------------------------------------------------
    //  drawHandCards()
    //
    private void drawHandCards(Canvas canvas) {
        for (int i = 0; i < controller_.getPlayerList().size(); i++) {
            MyPlayer myPlayer = controller_.getPlayerById(i);

            if (i == 0) {
                for (int j = 0; j < myPlayer.getAmountOfCardsInHand(); j++) {
                    if (myPlayer.getHand().getCardAt(j).getPosition() == null) {
                        break;
                    }
                    if (myPlayer.getHand().getCardAt(j).getPosition().
                            equals(controller_.getLayout().getDeckPosition())) {
                        break;
                    }
                    if (myPlayer.getHand().getCardAt(j).isVisible()) {
                        canvas.drawBitmap(myPlayer.getHand().getCardAt(j).getBitmap(),
                                myPlayer.getHand().getCardAt(j).getPosition().x,
                                myPlayer.getHand().getCardAt(j).getPosition().y, null);
                    }

                }
            } else if (i == 1 || i == 3) {
                for (int j = 0; j < myPlayer.getAmountOfCardsInHand(); j++) {
                    if (myPlayer.getHand().getCardAt(j).getPosition() == null) {
                        break;
                    }
                    if (myPlayer.getHand().getCardAt(j).getPosition().
                            equals(controller_.getLayout().getDeckPosition())) {
                        break;
                    }
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap backside = controller_.getDeck().getBacksideBitmap();
                    Bitmap rotatedBitmap = Bitmap.createBitmap(backside, 0, 0,
                            backside.getWidth(), backside.getHeight(), matrix, true);
                    if (myPlayer.getHand().getCardAt(j).isVisible()) {
                        canvas.drawBitmap(rotatedBitmap,
                                myPlayer.getHand().getCardAt(j).getPosition().x,
                                myPlayer.getHand().getCardAt(j).getPosition().y, null);
                    }
                    rotatedBitmap.recycle();
                }
            } else if (i == 2) {
                for (int j = 0; j < myPlayer.getAmountOfCardsInHand(); j++) {
                    if (myPlayer.getHand().getCardAt(j).getPosition() == null) {
                        break;
                    }
                    if (myPlayer.getHand().getCardAt(j).getPosition().
                            equals(controller_.getLayout().getDeckPosition())) {
                        break;
                    }
                    if (myPlayer.getHand().getCardAt(j).isVisible()) {
                        canvas.drawBitmap(controller_.getDeck().getBacksideBitmap(),
                                myPlayer.getHand().getCardAt(j).getPosition().x,
                                myPlayer.getHand().getCardAt(j).getPosition().y, null);
                    }
                }
            }
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
                controller_.getTouchEvents().ActionDown(controller_, X, Y);
                break;
            case MotionEvent.ACTION_MOVE:
                controller_.getTouchEvents().ActionMove(controller_, X, Y);
                break;
            case MotionEvent.ACTION_UP:
                controller_.getTouchEvents().ActionUp(controller_, X, Y);
                break;
        }

        return true;
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







