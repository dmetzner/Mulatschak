package heroiceraser.mulatschak.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;


public final class DisplayDimension {

    private DisplayDimension() {
    }

    public static int getWidth(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) view.getContext())
                .getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }

    public static int getHeight(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) view.getContext())
                .getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        return displayMetrics.heightPixels;
    }
}
