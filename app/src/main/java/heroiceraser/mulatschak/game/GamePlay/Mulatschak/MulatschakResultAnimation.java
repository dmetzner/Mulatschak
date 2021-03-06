package heroiceraser.mulatschak.game.GamePlay.Mulatschak;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.text.TextPaint;

import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.drawableBasicObjects.MyTextField;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GamePlay.GameAreaOverlay;


//--------------------------------------------------------------------------------------------------
//  MulatschakResultAnimation
//
public class MulatschakResultAnimation {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private GameAreaOverlay gameAreaOverlay;
    private boolean animating;
    private long startTime;
    private MyTextField textField;
    private MyTextField textField2;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public MulatschakResultAnimation() {
        textField = null;
        textField2 = null;
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameController controller, boolean succeeded) {
        GameLayout layout = controller.getLayout();

        gameAreaOverlay = new GameAreaOverlay(layout);
        gameAreaOverlay.updateAlpha(0);

        textField = new MyTextField();
        textField2 = new MyTextField();

        textField.setPosition(new Point(layout.getScreenWidth() / 2,
                layout.getDiscardPilePositions().get(0).y - layout.getCardHeight() / 3));
        textField2.setPosition(new Point(layout.getScreenWidth() / 2,
                layout.getDiscardPilePositions().get(0).y + layout.getCardHeight() / 3));
        TextPaint tmp = new TextPaint();
        tmp.setAntiAlias(true);
        tmp.setTextSize(layout.getCardHeight() / 2);
        tmp.setTextAlign(Paint.Align.CENTER);
        tmp.setColor(Color.WHITE);
        tmp.setAlpha(0);
        textField.setText(controller.getView().getResources().getString(R.string.mulatschak));
        textField.setTextPaint(tmp);
        if (succeeded) {
            textField2.setText(controller.getView().getResources().getString(R.string.mulatschak_succeeded_2));
            textField2.setTextPaint(tmp);
            textField2.setBorder(Color.GREEN, 0.2f);
            textField.setBorder(Color.GREEN, 0.2f);
        } else {
            textField2.setText(controller.getView().getResources().getString(R.string.mulatschak_failed_2));
            textField2.setTextPaint(tmp);
            textField2.setBorder(Color.RED, 0.2f);
            textField.setBorder(Color.RED, 0.2f);
        }
        textField.setMaxWidth((int) (layout.getOnePercentOfScreenWidth() * 55));
        textField2.setMaxWidth((int) (layout.getOnePercentOfScreenWidth() * 48));
        textField.setVisible(false);
        textField2.setVisible(false);
    }


    //----------------------------------------------------------------------------------------------
    //  remove
    //
    public void remove() {
        textField = null;
        textField2 = null;
    }


    //----------------------------------------------------------------------------------------------
    //  startAnimation
    //
    public void startAnimation(GameController controller) {
        //controller.getView().enableUpdateCanvasThread();
        startTime = System.currentTimeMillis();
        animating = true;
        gameAreaOverlay.updateAlpha(0);
        textField.updateAlpha(0);
        textField2.updateAlpha(0);
        textField.setVisible(true);
        textField2.setVisible(true);
        controller.getPlayerHandsView().setMissATurnInfoVisible(false);
    }


    private void continueAnimation(GameController controller) {
        double maxTime = 1000 * controller.getSettings().getAnimationSpeed().getSpeedFactor();
        long time = System.currentTimeMillis();
        long timeSinceStart = time - startTime;

        double percentage = timeSinceStart / maxTime;
        if (percentage >= 1) {
            percentage = 1;
        }
        int alpha = (int) (255 * percentage);
        gameAreaOverlay.updateAlpha(alpha);
        textField.updateAlpha(alpha);
        textField2.updateAlpha(alpha);

        if (percentage >= 1) {
            endingAnimation(controller);
        }
    }


    private void endingAnimation(final GameController controller) {
        animating = false;
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                textField = null;
                controller.getGamePlay().getAllCardsPlayed().allCardsArePlayedLogic(controller);
            }
        };
        handler.postDelayed(runnable,
                (int) (2000 * controller.getSettings().getAnimationSpeed().getSpeedFactor()));
    }


    public synchronized void draw(Canvas canvas, GameController controller) {
        if (textField != null) {
            gameAreaOverlay.draw(canvas);
            textField.draw(canvas);
            textField2.draw(canvas);
            if (animating) {
                continueAnimation(controller);
            }
        }
    }

}
