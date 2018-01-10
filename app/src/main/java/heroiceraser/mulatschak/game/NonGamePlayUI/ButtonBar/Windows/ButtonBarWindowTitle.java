package heroiceraser.mulatschak.game.NonGamePlayUI.ButtonBar.Windows;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.TextPaint;

import heroiceraser.mulatschak.DrawableBasicObjects.MySimpleTextField;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextField;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;


//----------------------------------------------------------------------------------------------
//  ButtonBarWindowTitle
//
public class ButtonBarWindowTitle extends MyTextField {

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    ButtonBarWindowTitle() {
        super();
    }


    //----------------------------------------------------------------------------------------------
    //  Init
    //
    public void init(GameController controller, String text) {
        GameView view = controller.getView();
        GameLayout layout = controller.getLayout();

        int title_size = (int) (layout.getSectors().get(1).y);
        int title_color = view.getResources().getColor(R.color.button_bar_window_title_color);
        Point title_position = layout.getButtonBarWindowTitlePosition();

        setText(text);
        setPosition(title_position);
        setVisible(true);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        String font = "fonts/28_Days_Later.ttf";
        Typeface tf = Typeface.createFromAsset(view.getContext().getAssets(), font);
        textPaint.setTypeface(tf);
        textPaint.setColor(title_color);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(title_size);
        setTextPaint(textPaint);
        setMaxWidth((int) (layout.getOnePercentOfScreenWidth() * 80));
    }

}
