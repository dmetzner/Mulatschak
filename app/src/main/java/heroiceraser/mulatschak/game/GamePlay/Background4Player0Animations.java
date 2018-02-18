package heroiceraser.mulatschak.game.GamePlay;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;


//----------------------------------------------------------------------------------------------
//  Background 4 Player_0 Animations
//
public class Background4Player0Animations {


    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private Rect background;
    private Paint backgroundPaint;
    private final int ALPHA_MAX = 150;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public Background4Player0Animations() {
        background = new Rect();
        backgroundPaint = new Paint();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameLayout layout) {
        background.set(0, layout.getSectors().get(2).y, layout.getScreenWidth(),
                (int) (layout.getSectors().get(7).y));
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setAlpha(ALPHA_MAX);
    }


    //----------------------------------------------------------------------------------------------
    //  updateAlpha
    //
    public void updateAlpha(int alpha) {
        if (alpha > ALPHA_MAX) {
            alpha = ALPHA_MAX;
        }
        backgroundPaint.setAlpha(alpha);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas, GameController controller) {
        canvas.drawRect(background, backgroundPaint);
    }


    //----------------------------------------------------------------------------------------------
    //  Getter
    //
    public Rect getBackground() {
        return background;
    }

    public Paint getBackgroundPaint() {
        return backgroundPaint;
    }

}
