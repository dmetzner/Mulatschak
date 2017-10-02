package heroiceraser.mulatschak.game;

import android.graphics.Point;
import android.text.Layout;

import heroiceraser.mulatschak.DrawableBasicObjects.MySimpleTextField;
import heroiceraser.mulatschak.R;

/**
 * Created by Daniel Metzner on 02.10.2017.
 */

public class ButtonBarWindowTitle extends MySimpleTextField {

    public ButtonBarWindowTitle() {
        super();
    }

    public void init(GameController controller, String text) {
        GameView view = controller.getView();
        GameLayout layout = controller.getLayout();

        int title_size = (int) (view.getResources()
                .getDimension(R.dimen.button_bar_window_title_size));
        int title_color = view.getResources().getColor(R.color.button_bar_window_title_color);
        Point title_position = layout.getButtonBarWindowTitlePosition();
        int max_title_width =  layout.getButtonBarWindowTitleSize().x;
        int max_title_height = layout.getButtonBarWindowTitleSize().y;

        super.init(view, title_size, title_color, title_position, text,
                Layout.Alignment.ALIGN_CENTER, max_title_width, max_title_height,
                "fonts/28_Days_Later.ttf");
    }

}
