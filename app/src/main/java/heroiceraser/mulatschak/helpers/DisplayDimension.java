package heroiceraser.mulatschak.helpers;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by Daniel Metzner on 10.08.2017.
 */

public final class DisplayDimension {

    private DisplayDimension() {}

    public static int getWidth(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) view.getContext()).getWindowManager().
                getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getHeight(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) view.getContext()).getWindowManager().
                getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}
