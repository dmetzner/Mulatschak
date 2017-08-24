package heroiceraser.mulatschak.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.helpers.Coordinate;

import static android.R.attr.bitmap;

/**
 * Created by Daniel Metzner on 08.08.2017.
 */

public class GameView extends View {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private Context context_;
    private GameController controller_;

    int test_radius = 0;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public GameView(Context context) {
        super(context);
        context_ = context;
        controller_ = new GameController(this, 4);  // hardcoded!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public GameController getController() { return controller_; }


    //----------------------------------------------------------------------------------------------
    //  onDraw()
    //
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // TESTCASE
           // drawCheck(canvas);
        drawButtonBar(canvas);
        drawStatsButton(canvas);
        drawHandCards(canvas);
        drawDiscardPile(canvas);
        drawAnimations(canvas);
    }

    private void drawCheck(Canvas canvas) {
        canvas.drawCircle(500, 800, test_radius, new Paint(0));
        test_radius += 5;
        if (test_radius > 300) { test_radius = 0; }
    }

    private void drawButtonBar(Canvas canvas) {
        int size = controller_.getLayout().getButtonBarSize(); // Button size
        int width = controller_.getLayout().getScreenWidth();
        int height = controller_.getLayout().getScreenHeight();
        Coordinate coordinate = controller_.getLayout().getButtonBar();
        Paint paint = new Paint();
        paint.setColor(Color.DKGRAY);
        canvas.drawRect(coordinate.getX(), coordinate.getY(), width, height, paint );
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
                    canvas.drawBitmap(player.getHand().getCardAt(j).getBitmap(),
                            player.getHand().getCardAt(j).getPosition().getX(),
                            player.getHand().getCardAt(j).getPosition().getY(), null);
                }
            } else if (i == 1 || i == 3) {
                for (int j = 0; j < player.getAmountOfCardsInHand(); j++) {
                    if (player.getHand().getCardAt(j).getPosition() == null) {
                        break;
                    }
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap backside = controller_.getDeck().getBacksideBitmap();
                    Bitmap rotatedBitmap = Bitmap.createBitmap(backside, 0, 0,
                            backside.getWidth(), backside.getHeight(), matrix, true);
                    canvas.drawBitmap(rotatedBitmap,
                            player.getHand().getCardAt(j).getPosition().getX(),
                            player.getHand().getCardAt(j).getPosition().getY(), null);
                }
            } else if (i == 2) {
                for (int j = 0; j < player.getAmountOfCardsInHand(); j++) {
                    if (player.getHand().getCardAt(j).getPosition() == null) {
                        break;
                    }
                    canvas.drawBitmap(controller_.getDeck().getBacksideBitmap(),
                            player.getHand().getCardAt(j).getPosition().getX(),
                            player.getHand().getCardAt(j).getPosition().getY(), null);
                }
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    //  drawDiscardPile
    //
    private void drawDiscardPile(Canvas canvas) {
        for (int j = 0; j < controller_.getDiscardPile().getCoordinates().size(); j++) {
            if (controller_.getDiscardPile().getCard(j) == null) {
                canvas.drawBitmap(controller_.getDiscardPile().getBitmap(),
                        controller_.getDiscardPile().getCoordinate(j).getX(),
                        controller_.getDiscardPile().getCoordinate(j).getY(), null);
            }
            else {
                canvas.drawBitmap(controller_.getDiscardPile().getCard(j).getBitmap(),
                        controller_.getDiscardPile().getCoordinate(j).getX(),
                        controller_.getDiscardPile().getCoordinate(j).getY(), null);
            }

        }
    }

    //----------------------------------------------------------------------------------------------
    // drawStats()
    //
    private void drawStatsButton(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        int width = controller_.getLayout().getCardWidth();
        Coordinate coordinate = controller_.getStatistics().getStatsButton().getCoordinate();
        Bitmap bitmap = controller_.getStatistics().getStatsButton().getBitmap();
        if (controller_.getStatistics().getStatsButton().IsPressed()) {
            bitmap = controller_.getStatistics().getStatsButton().getBitmapPressed();
        }
        canvas.drawBitmap(bitmap, coordinate.getX(), coordinate.getY(), null);

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
                    controller_.getDeck().getCoordinate().getX(),
                    controller_.getDeck().getCoordinate().getY(), null);

            controller_.getAnimation().getDealingAnimation().deal();

            canvas.drawBitmap(controller_.getAnimation().getDealingAnimation().getBitmap(),
                    controller_.getAnimation().getDealingAnimation().getHandCardX(),
                    controller_.getAnimation().getDealingAnimation().getHandCardY(), null);
        }

        else if (controller_.getAnimation().getStichAnsage().getAnimationNumbers()) {
            int amount_of_buttons = controller_.getAnimation().getStichAnsage().getNumberButtons().size();
            for (int button_id = 0; button_id < amount_of_buttons; button_id++) {
                Button button = controller_.getAnimation().getStichAnsage().getNumberButtonAt(button_id);
                Bitmap bitmap = button.getBitmap();
                if (button.IsPressed()) {
                    bitmap = button.getBitmapPressed();
                }
                else if (!button.IsEnabled()) {
                    bitmap = button.getBitmapDisabled();
                }
                canvas.drawBitmap(bitmap, button.getCoordinate().getX(),
                        button.getCoordinate().getY(), null);
            }
        }

        else if (controller_.getAnimation().getStichAnsage().getAnimationSymbols()) {
            int amount_of_buttons = controller_.getAnimation().getStichAnsage().getSymbolButtons().size();
            for (int button_id = 0; button_id < amount_of_buttons; button_id++) {
                Button button = controller_.getAnimation().getStichAnsage().getSymbolButtonAt(button_id);
                Bitmap bitmap = button.getBitmap();
                if (button.IsPressed()) {
                    bitmap = button.getBitmapPressed();
                }
                canvas.drawBitmap(bitmap, button.getCoordinate().getX(),
                        button.getCoordinate().getY(), null);
            }
        }

    }

    //----------------------------------------------------------------------------------------------
    //  onTouchEvent
    //
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        int X = (int)event.getX();
        int Y = (int)event.getY();

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
}


