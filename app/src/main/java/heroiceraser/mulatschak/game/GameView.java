package heroiceraser.mulatschak.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import heroiceraser.mulatschak.DrawableBasicObjects.Button;
import heroiceraser.mulatschak.game.DrawableObjects.ButtonBar;

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
        int players = 4;
        int difficulty = 2; // normal
        int player_lives = 21;
        controller_ = new GameController(this, players, difficulty, player_lives);
        thread_ = new GameThread(this);
        thread_.setRunning(true);
        thread_.start();
    }

    public GameView(Context context, int enemies, int difficulty, int player_lives) {
        super(context);
        context_ = context;
        int players = enemies + 1;
        controller_ = new GameController(this, players, difficulty, player_lives);
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

        drawHandCards(canvas);
        drawDiscardPile(canvas);
        drawDealerButton(canvas);
        drawAnimations(canvas);

        drawGameOver(canvas);
        drawRoundInfo(canvas);
        drawStatistics(canvas);
        drawTricks(canvas);
        drawMenu(canvas);
        drawButtonBar(canvas);
        drawBorder(canvas);

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

    private void drawButtonBar(Canvas canvas) {
        if (controller_.getButtonBar().isVisible()) {
            ButtonBar button_bar = controller_.getButtonBar();

            /// draw Buttonbar
            canvas.drawBitmap(button_bar.getBitmap(), button_bar.getPosition().x,
                    button_bar.getPosition().y, null);

            // draw decoration
            canvas.drawBitmap(button_bar.getDecoration(), button_bar.getPosition().x,
                    (int) (button_bar.getPosition().y - (button_bar.getHeight())), null);

            // Statistics Button
            controller_.getButtonBar().getStatisticsButton().draw(canvas);

            // Tricks Button
            controller_.getButtonBar().getTricksButton().draw(canvas);

            // Menu Button
            controller_.getButtonBar().getMenuButton().draw(canvas);
        }

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
    //  drawDiscardPile
    //
    private void drawDiscardPile(Canvas canvas) {
        controller_.getDiscardPile().draw(canvas);
    }

    //----------------------------------------------------------------------------------------------
    //  drawAnimations()
    //
    private void drawAnimations(Canvas canvas) {
        if (!controller_.getAnimation().getTurnedOn()) {
            return;
        }

        if (controller_.getAnimation().getDealingAnimation().getAnimationRunning()) {

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
            controller_.getAnimation().getCardExchange().draw(canvas);
        } else if (controller_.getAnimation().getTrickBids().getAnimationNumbers()) {
            int amount_of_buttons = controller_.getAnimation().getTrickBids().getNumberButtons().size();
            for (int button_id = 0; button_id < amount_of_buttons; button_id++) {
                Button button = controller_.getAnimation().getTrickBids().getNumberButtonAt(button_id);
                Bitmap bitmap = button.getBitmap();
                if (button.IsPressed()) {
                    bitmap = button.getBitmapPressed();
                } else if (!button.IsEnabled()) {
                    bitmap = button.getBitmapDisabled();
                }
                canvas.drawBitmap(bitmap, button.getPosition().x,
                        button.getPosition().y, null);
            }
        } else if (controller_.getAnimation().getTrickBids().getAnimationSymbols()) {
            int amount_of_buttons = controller_.getAnimation().getTrickBids().getTrumpButtons().size();
            for (int button_id = 0; button_id < amount_of_buttons; button_id++) {
                Button button = controller_.getAnimation().getTrickBids().getTrumpButtonAt(button_id);
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
    // drawStatistics
    //
    private void drawStatistics(Canvas canvas) {
       controller_.getStatistics().draw(canvas, controller_);
    }

    //----------------------------------------------------------------------------------------------
    // drawTricks
    //
    private void drawTricks(Canvas canvas) {
        if (controller_.getTricks().isVisible()) {
            canvas.drawBitmap(controller_.getTricks().getBitmap(),
                    controller_.getTricks().getPosition().x,
                    controller_.getTricks().getPosition().y, null);
        }
    }

    //----------------------------------------------------------------------------------------------
    // drawMenu
    //
    private void drawMenu(Canvas canvas) {
        if (controller_.getMenu().isVisible()) {
            canvas.drawBitmap(controller_.getMenu().getBitmap(),
                    controller_.getMenu().getPosition().x,
                    controller_.getMenu().getPosition().y, null);
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







