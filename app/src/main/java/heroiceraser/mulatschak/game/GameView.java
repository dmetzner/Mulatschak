package heroiceraser.mulatschak.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
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
        drawDealerButton(canvas);
        drawHandCards(canvas);
        drawDiscardPile(canvas);
        drawAnimations(canvas);

        drawStatistics(canvas);
        drawTricks(canvas);
        drawMenu(canvas);
        drawButtonBar(canvas);
    }

    private void drawCheck(Canvas canvas) {
        canvas.drawCircle(500, 800, test_radius, new Paint(0));
        test_radius += 5;
        if (test_radius > 300) { test_radius = 0; }
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
                    (int) (button_bar.getPosition().y - (button_bar.getHeight())) , null);

            // Statistics Button
            drawButton(canvas, controller_.getButtonBar().getStatisticsButton());

            // Tricks Button
            drawButton(canvas, controller_.getButtonBar().getTricksButton());

            // Menu Button
            drawButton(canvas, controller_.getButtonBar().getMenuButton());
          }

    }

    private void drawButton(Canvas canvas, Button button) {

        if (!button.isVisible()) {
            return;
        }

        Bitmap bitmap = button.getBitmap();
        if (!button.IsEnabled()) {
            bitmap = button.getBitmapDisabled();
        }
        else if (button.IsPressed()) {
            bitmap = button.getBitmapPressed();
        }
        canvas.drawBitmap(bitmap,
                button.getPosition().x,
                button.getPosition().y, null);
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
                            player.getHand().getCardAt(j).getPosition().x,
                            player.getHand().getCardAt(j).getPosition().y, null);
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
                            player.getHand().getCardAt(j).getPosition().x,
                            player.getHand().getCardAt(j).getPosition().y, null);
                }
            } else if (i == 2) {
                for (int j = 0; j < player.getAmountOfCardsInHand(); j++) {
                    if (player.getHand().getCardAt(j).getPosition() == null) {
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
        for (int j = 0; j < controller_.getDiscardPile().getPoints().size(); j++) {
            if (controller_.getDiscardPile().getCard(j) == null) {
                canvas.drawBitmap(controller_.getDiscardPile().getBitmap(),
                        controller_.getDiscardPile().getPoint(j).x,
                        controller_.getDiscardPile().getPoint(j).y, null);
            }
            else {
                canvas.drawBitmap(controller_.getDiscardPile().getCard(j).getBitmap(),
                        controller_.getDiscardPile().getPoint(j).x,
                        controller_.getDiscardPile().getPoint(j).y, null);
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

        if (controller_.getAnimation().getDealingAnimation().getAnimationRunning()) {

            canvas.drawBitmap(controller_.getDeck().getBacksideBitmap(),
                    controller_.getDeck().getPosition().x,
                    controller_.getDeck().getPosition().y, null);

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
                canvas.drawBitmap(bitmap, button.getPosition().x,
                        button.getPosition().y, null);
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
                canvas.drawBitmap(bitmap, button.getPosition().x,
                        button.getPosition().y, null);
            }
        }

    }

    //----------------------------------------------------------------------------------------------
    // drawStatistics
    //
    private void drawStatistics(Canvas canvas) {
        if (controller_.getStatistics().isVisible()) {
            canvas.drawBitmap(controller_.getStatistics().getBitmap(),
                    controller_.getStatistics().getPosition().x,
                    controller_.getStatistics().getPosition().y, null);

            String text = "Player X:   XXX";
            TextPaint textPaint = new TextPaint();
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(40 * getResources().getDisplayMetrics().density);
            textPaint.setColor(Color.GREEN);
            int width = (int) textPaint.measureText(text);
            int x = (int) ((controller_.getLayout().getScreenWidth() - width) / 2.0);
            int y = (int) (controller_.getLayout().getScreenHeight() / 8.0 * 1.5);
            for (int i = 0; i < controller_.getAmountOfPlayers(); i++) {
                canvas.save();
                canvas.translate(x, y);
                text = "Player " + (i + 1);
                text += ":     " + controller_.getPlayerById(i).getLives();
                StaticLayout staticLayout = new StaticLayout(text, textPaint,
                        width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
                staticLayout.draw(canvas);
                canvas.restore();
                y += (int) (textPaint.getTextSize() * 1.5);
            }
        }
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


