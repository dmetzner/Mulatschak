package heroiceraser.mulatschak.game.GamePlay.MultiplierView;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.TextPaint;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameView;



public class MultiplierView extends DrawableObject{

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private TextPaint text_paint_;
    private Point multi_pos_;
    private String multi_text_;
    private Point game_position_;
    private Point game_size_;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public MultiplierView() {
        text_paint_ = new TextPaint();
        game_position_ = new Point();
        game_size_ = new Point();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        game_position_ = new Point(view.getController().getLayout().getScreenWidth() / 45,
                view.getController().getLayout().getSectors().get(2).y +
                        (int) (view.getController().getLayout().getSectors().get(1).y * 0.1)
        );

        game_size_ = new Point(view.getController().getLayout().getPlayerInfoSize());
        game_size_.x *= 0.8;
        game_size_.y *= 0.9;

        multi_text_ = "";
        multi_pos_ = new Point(game_position_);
        multi_pos_.x += game_size_.x * 1.1;
        multi_pos_.y += game_size_.y * 0.7;

        text_paint_.setStyle(Paint.Style.FILL);
        text_paint_.setAntiAlias(true);
        text_paint_.setColor(Color.BLACK);
        text_paint_.setTextSize(game_size_.y * 0.45f);

        updateMultiplier(view.getController().getLogic().getMultiplier());
        setVisible(true);
    }


    //----------------------------------------------------------------------------------------------
    //  update
    //
    public void updateMultiplier(int multiplier) {
        if (multiplier > 1) {
            multi_text_ = "x" + multiplier;
        }
        else {
            multi_text_ = "";
        }
    }

    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas) {
        if (isVisible() && !multi_text_.equals("")) {
            canvas.drawText(multi_text_, multi_pos_.x, multi_pos_.y, text_paint_);
        }
    }

}
