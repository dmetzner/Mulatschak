package heroiceraser.mulatschak.game.NonGamePlayUI.BottomBar;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.TextPaint;

import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.drawableBasicObjects.MyTextField;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;


public class ButtonBarWindowTitle extends MyTextField {

    public void init(GameController controller, String text) {
        GameView view = controller.getView();
        GameLayout layout = controller.getLayout();

        int title_size = layout.getLengthOnVerticalGrid(125);
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
