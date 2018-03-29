package heroiceraser.mulatschak.game.GamePlay.TrickBids;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.TextPaint;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameView;


//--------------------------------------------------------------------------------------------------
//  BidsField
//              ToDo
//
public class BidsField extends DrawableObject{

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    //---- circle
    private float radius;
    private int strokeWidth;
    private Paint circlePaintBorder;

    public Paint getCirclePaintDefault() {
        return circlePaintDefault;
    }

    public void setCirclePaintDefault(Paint circlePaintDefault) {
        this.circlePaintDefault = circlePaintDefault;
    }

    private Paint circlePaintDefault;

    //---- Text
    private TextPaint textPaint;
    private String text;

    //---- Animation
    private boolean animationRunning;
    private Point startPos;
    private Point offset;
    private float min_radius_;              // size changes
    private float max_radius_;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public BidsField() {
        super();
        radius = 0;
    }


    //----------------------------------------------------------------------------------------------
    //  Init
    //
    public void init(GameView view, String text, final Point position) {

        //---- position
        setPosition(new Point(position));

        //---- size
        // dealerButton <= radius <= min(cardWidth, cardHeight)
        max_radius_ = Math.min(view.getController().getLayout().getCardWidth(),
        view.getController().getLayout().getCardHeight()) / 2.03f;
        min_radius_ = view.getController().getLayout().getDealerButtonSize() / 2;
        radius = min_radius_;

        //---- border
        circlePaintBorder = new Paint();
        circlePaintBorder.setColor(Color.BLACK);
        circlePaintBorder.setAntiAlias(true);
        strokeWidth = (int) (radius / 10);
        if (strokeWidth < 2) {
            strokeWidth = 2;
        }
        circlePaintBorder.setStrokeWidth(strokeWidth);
        circlePaintBorder.setStyle(Paint.Style.STROKE);
        circlePaintBorder.setAlpha(255);

        //---- fill
        circlePaintDefault = new Paint();
        circlePaintDefault.setColor(view.getResources().getColor(R.color.metallic_blue));
        circlePaintDefault.setAntiAlias(true);
        circlePaintDefault.setStyle(Paint.Style.FILL);
        circlePaintBorder.setAlpha(255);

        //---- text
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(radius);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        this.text = text;

        animationRunning = false;
        setVisible(true);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //          -> draws a circle with a border and a text in it
    //          -> continues ending animation if it's running
    //
    public synchronized void draw(Canvas canvas) {
        if (isVisible()) {

            // reduce radius by the stroke width
            float radius = this.radius - strokeWidth;

            // draw filling
            canvas.drawCircle(getPosition().x, getPosition().y, radius, circlePaintDefault);
            // draw border
            canvas.drawCircle(getPosition().x, getPosition().y, radius, circlePaintBorder);

            canvas.drawText(text, getPosition().x,
                    getPosition().y + radius / 2.5f, textPaint);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  calculatePositionOffset
    //
    private void calculatePositionOffset(Point startPos, Point endPos) {
        this.offset = new Point (endPos.x - startPos.x, endPos.y - startPos.y);
    }


    //----------------------------------------------------------------------------------------------
    //  startAnAnimation
    //                          -> starts animation and saves start time
    //                          -> set field back to origin start position,
    //                             max alpha, min radius
    //
    public void startAnimation(GameController controller, Point startPos, Point endPos) {
        //controller.getView().enableUpdateCanvasThread();
        calculatePositionOffset(startPos, endPos);
        this.startPos = startPos;
        animationRunning = true;
    }


    //----------------------------------------------------------------------------------------------
    //  UpdateAlpha value
    //
    public void updateAlpha(int alpha) {
        circlePaintBorder.setAlpha(alpha);
        circlePaintDefault.setAlpha(alpha);
        textPaint.setAlpha(alpha);
    }


    //----------------------------------------------------------------------------------------------
    //  moveToFinalPositionBasedOnPercentage
    //
    public void moveToFinalPositionBasedOnPercentage(double percentage) {

        int offset_x = (int) (offset.x * percentage);
        int offset_y = (int) (offset.y * percentage);

        setPosition(new Point(startPos.x + offset_x,
                startPos.y + offset_y));
    }


    //----------------------------------------------------------------------------------------------
    //  changeSizeBasedOnPercentage
    //                              -> can't be smaller than min radius or bigger than max radius
    //
    public void changeSizeBasedOnPercentage(double percentage) {
        radius = (float) percentage * max_radius_;
        if (radius < min_radius_) {
            radius = min_radius_;
        }
        if (radius > max_radius_) {
            radius = max_radius_;
        }
        textPaint.setTextSize((radius));
    }



    //----------------------------------------------------------------------------------------------
    // Getter & Setter
    //
    public void setOffset(Point offset) {
        this.offset = offset;
    }

    public Point getOffset() {
        return offset;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isAnimationRunning() {
        return animationRunning;
    }

    public void setAnimationRunning(boolean animationRunning) {
        this.animationRunning = animationRunning;
    }

    public Point getStartPos() {
        return startPos;
    }

    public void setStartPos(Point startPos) {
        this.startPos = startPos;
    }
}
