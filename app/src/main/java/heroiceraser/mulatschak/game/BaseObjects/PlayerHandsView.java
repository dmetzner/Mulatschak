package heroiceraser.mulatschak.game.BaseObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.TextPaint;

import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.drawableBasicObjects.MyTextField;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameLogic;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.utils.BitmapMethodes;


//--------------------------------------------------------------------------------------------------
//  Player Hands
//
public class PlayerHandsView {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private MyTextField missATurnTextField;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public PlayerHandsView() {
        missATurnTextField = new MyTextField();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();
        missATurnTextField.setPosition(new Point(layout.getScreenWidth() / 2,
                layout.getHandBottom().y + layout.getCardHeight() / 2));
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.RED);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(layout.getCardHeight());
        missATurnTextField.setTextPaint(textPaint);
        missATurnTextField.setMaxWidth((int) (layout.getOnePercentOfScreenWidth() * 80));
        missATurnTextField.setBorder(Color.BLACK, 0.03f);
        missATurnTextField.setText(view.getResources().getString(R.string.player_info_miss_a_turn));
        missATurnTextField.setVisible(false);
    }


    //----------------------------------------------------------------------------------------------
    //  redrawHands
    //
    public void redrawHands(GameLayout layout, MyPlayer player) {
        switch (player.getId()) {
            case 0:
                for (int i = 0; i < player.getAmountOfCardsInHand(); i++) {
                    player.getHand().getCardAt(i).setFixedPosition(layout.getHandBottom().x +
                                    layout.getCardWidth() * i + (int) (layout.getCardWidth() *
                                    ((GameLogic.MAX_CARDS_PER_HAND -
                                            player.getAmountOfCardsInHand()) / 2.0)),
                            layout.getHandBottom().y);
                    player.getHand().getCardAt(i).setPosition(
                            player.getHand().getCardAt(i).getFixedPosition());
                }
        }
    }


    //----------------------------------------------------------------------------------------------
    //  drawHandCards()
    //
    public synchronized void drawHandCards(Canvas canvas, GameController controller) {
        for (int i = 0; i < controller.getPlayerList().size(); i++) {
            MyPlayer player = controller.getPlayerById(i);

            for (int j = 0; j < player.getAmountOfCardsInHand(); j++) {
                // skip cards that are null
                if (player.getHand().getCardAt(j).getPosition() == null) {
                    continue;
                }
                // don't draw the cards when they are still in the deck (ANIMATION not logic)
                if (player.getHand().getCardAt(j).getPosition().
                        equals(controller.getLayout().getDeckPosition())) {
                    continue;
                }
                // card must be visible
                if (!player.getHand().getCardAt(j).isVisible()) {
                    continue;
                }

                Bitmap bmp = controller.getDeck().getBacksideBitmap();

                if (i == 0) {
                    bmp = player.getHand().getCardAt(j).getBitmap();
                } else if (i == 2) {
                    bmp = controller.getDeck().getBacksideBitmap();
                } else if (i == 1 || i == 3) {
                    Bitmap tmp = controller.getDeck().getBacksideBitmap();
                    bmp = BitmapMethodes.rotateBitmap(tmp, 90);
                }

                canvas.drawBitmap(bmp,
                        player.getHand().getCardAt(j).getPosition().x,
                        player.getHand().getCardAt(j).getPosition().y, null);
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    //  draw Player 0 Hand
    //
    public synchronized void drawPlayer0Hand(Canvas canvas, GameController controller) {
        for (Card card : controller.getPlayerById(0).getHand().getCardStack()) {
            canvas.drawBitmap(card.getBitmap(), card.getPosition().x, card.getPosition().y, null);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  draw miss a turn
    //
    public synchronized void drawMissATurn(Canvas canvas) {
        missATurnTextField.draw(canvas);
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public void setMissATurnInfoVisible(boolean visible) {
        missATurnTextField.setVisible(visible);
    }
}
