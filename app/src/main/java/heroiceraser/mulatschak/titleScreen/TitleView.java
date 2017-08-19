package heroiceraser.mulatschak.titleScreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import heroiceraser.mulatschak.game.GameActivity;
import heroiceraser.mulatschak.R;

/**
 * Created by Daniel Metzner on 08.08.2017.
 */

public class TitleView extends View {

    private int screen_w;
    private int screen_h;
    private Context context_;
    private Bitmap titleGraphic;
    private Bitmap playButton_up, playButton_down;
    private boolean playButton_pressed;


    public TitleView(Context context) {
        super(context);
        context_ = context;
        titleGraphic = BitmapFactory.decodeResource(getResources(), R.drawable.title_graphic);
        playButton_up = BitmapFactory.decodeResource(getResources(), R.drawable.play_button_up);
        playButton_down = BitmapFactory.decodeResource(getResources(), R.drawable.play_button_down);
    }

    @Override
    public void onSizeChanged(int w, int h, int old_w, int old_h) {
        super.onSizeChanged(w, h, old_w, old_h);
        screen_w = w;
        screen_h = h;
    }

    private int getTitleGraphicX() {
        return (screen_w - titleGraphic.getWidth()) / 2;
    }

    private int getTitleGraphicY() {
        return (screen_h - titleGraphic.getHeight()) / 3;
    }

    private int getPlayButtonX() {
        return (screen_w - playButton_up.getWidth()) / 2;
    }

    private int getPlayButtonY() {
        return (getTitleGraphicY() + titleGraphic.getHeight()) +
                ((screen_h - (getTitleGraphicY() + playButton_up.getHeight())) / 10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(titleGraphic, getTitleGraphicX() , getTitleGraphicY(), null);
        if (playButton_pressed) {
            canvas.drawBitmap(playButton_down, getPlayButtonX(), getPlayButtonY(), null);
        }
        else {
            canvas.drawBitmap(playButton_up, getPlayButtonX(), getPlayButtonY(), null);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        int X = (int)event.getX();
        int Y = (int)event.getY();

        switch (eventAction) {

            case MotionEvent.ACTION_DOWN:
                if (X > getPlayButtonX() && X < getPlayButtonX() + playButton_up.getWidth() &&
                        Y > getPlayButtonY() && Y < getPlayButtonY() + playButton_up.getHeight()) {
                    playButton_pressed = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (X > getPlayButtonX() && X < getPlayButtonX() + playButton_up.getWidth() &&
                        Y > getPlayButtonY() && Y < getPlayButtonY() + playButton_up.getHeight()) {
                    playButton_pressed = true;
                }
                else {
                    playButton_pressed = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (playButton_pressed) {
                    Intent gameIntent = new Intent(context_, GameActivity.class);
                    context_.startActivity(gameIntent);
                    playButton_pressed = false;
                }
                break;
        }
        invalidate();
        return true;

    }
}
