package heroiceraser.mulatschak.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;

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

        drawPlayerInfo(canvas);
        drawDiscardPile(canvas);
        drawHandCards(canvas);
        drawDealerButton(canvas);
        drawAnimations(canvas);

        drawRoundInfo(canvas);

        drawGameOver(canvas);

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
        if (controller_.getDealerButton().isVisible()) {
            canvas.drawBitmap(controller_.getDealerButton().getBitmap(),
                    controller_.getDealerButton().getPosition().x,
                    controller_.getDealerButton().getPosition().y, null);
        }
    }

    private void drawNonGamePlayUI(Canvas canvas) {
        controller_.getNonGamePlayUIContainer().draw(canvas);
    }


    //----------------------------------------------------------------------------------------------
    //  drawDiscardPile
    //
    private void drawDiscardPile(Canvas canvas) {
        controller_.getDiscardPile().draw(canvas);
        controller_.getDiscardPile().drawOverlays(canvas, controller_.getLogic());
    }

    //----------------------------------------------------------------------------------------------
    //  drawHandCards()
    //
    private void drawHandCards(Canvas canvas) {
        for (int i = 0; i < controller_.getPlayerList().size(); i++) {
            Player player = controller_.getPlayerById(i);
            if (i == 0) {
                for (int j = 0; j < player.getAmountOfCardsInHand(); j++) {
                    if (player.getHand().getCardAt(j).getPosition() == null) {
                        break;
                    }
                    if (player.getHand().getCardAt(j).getPosition().
                            equals(controller_.getLayout().getDeckPosition())) {
                        break;
                    }
                    canvas.drawBitmap(player.getHand().getCardAt(j).getBitmap(),
                            player.getHand().getCardAt(j).getPosition().x,
                            player.getHand().getCardAt(j).getPosition().y, null);
                }
            } else if (i == 1 || i == 3) {
                for (int j = 0; j < player.getAmountOfCardsInHand(); j++) {
                    if (player.getHand().getCardAt(j).getPosition() == null) {
                        break;
                    }
                    if (player.getHand().getCardAt(j).getPosition().
                            equals(controller_.getLayout().getDeckPosition())) {
                        break;
                    }
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap backside = controller_.getDeck().getBacksideBitmap();
                    Bitmap rotatedBitmap = Bitmap.createBitmap(backside, 0, 0,
                            backside.getWidth(), backside.getHeight(), matrix, true);
                    canvas.drawBitmap(rotatedBitmap,
                            player.getHand().getCardAt(j).getPosition().x,
                            player.getHand().getCardAt(j).getPosition().y, null);
                    rotatedBitmap.recycle();
                }
            } else if (i == 2) {
                for (int j = 0; j < player.getAmountOfCardsInHand(); j++) {
                    if (player.getHand().getCardAt(j).getPosition() == null) {
                        break;
                    }
                    if (player.getHand().getCardAt(j).getPosition().
                            equals(controller_.getLayout().getDeckPosition())) {
                        break;
                    }
                    canvas.drawBitmap(controller_.getDeck().getBacksideBitmap(),
                            player.getHand().getCardAt(j).getPosition().x,
                            player.getHand().getCardAt(j).getPosition().y, null);
                }
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    //  drawAnimations()
    //
    private void drawAnimations(Canvas canvas) {
        if (!controller_.getAnimation().getTurnedOn()) {
            return;
        }


        if (controller_.getEnemyLogic().isPlayACardAnimationRunning()) {
            controller_.getEnemyLogic().draw(canvas);
        }


        else if (controller_.getAnimation().getDealingAnimation().getAnimationRunning()) {

            canvas.drawBitmap(controller_.getDeck().getBacksideBitmap(),
                    controller_.getDeck().getPosition().x,
                    controller_.getDeck().getPosition().y, null);

            controller_.getAnimation().getDealingAnimation().deal();

            canvas.drawBitmap(controller_.getAnimation().getDealingAnimation().getBitmap(),
                    controller_.getAnimation().getDealingAnimation().getHandCardX(),
                    controller_.getAnimation().getDealingAnimation().getHandCardY(), null);
        }

        //----- CardExchange
        else if (controller_.getAnimation().getCardExchange().isAnimationRunning()) {
            // Help Text
            Point position = controller_.getLayout().getCardExchangeTextPosition();
            controller_.getAnimation().getCardExchange().getHelpText().draw(canvas, position);

            // exchange Buttons
            controller_.getAnimation().getCardExchange().draw(canvas, controller_);
        }

        else if (controller_.getAnimation().getTrickBids().getAnimationNumbers()) {
            int amount_of_buttons = controller_.getAnimation().getTrickBids().getNumberButtons().size();
            for (int button_id = 0; button_id < amount_of_buttons; button_id++) {
                MyButton button = controller_.getAnimation().getTrickBids().getNumberButtonAt(button_id);
                Bitmap bitmap = button.getBitmap();
                if (button.IsPressed()) {
                    bitmap = button.getBitmapPressed();
                } else if (!button.IsEnabled()) {
                    bitmap = button.getBitmapDisabled();
                }
                canvas.drawBitmap(bitmap, button.getPosition().x,
                        button.getPosition().y, null);
            }
        }

        else if (controller_.getAnimation().getTrickBids().getAnimationSymbols()) {
            int amount_of_buttons = controller_.getAnimation().getTrickBids().getTrumpButtons().size();
            for (int button_id = 0; button_id < amount_of_buttons; button_id++) {
                MyButton button = controller_.getAnimation().getTrickBids().getTrumpButtonAt(button_id);
                Bitmap bitmap = button.getBitmap();
                if (button.IsPressed()) {
                    bitmap = button.getBitmapPressed();
                }
                canvas.drawBitmap(bitmap, button.getPosition().x,
                        button.getPosition().y, null);
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







