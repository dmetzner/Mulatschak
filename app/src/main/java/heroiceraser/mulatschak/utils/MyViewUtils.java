package heroiceraser.mulatschak.utils;

import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;


public final class MyViewUtils {

    private MyViewUtils() {
    }

    public static void addOnClickListenerToElements(@NonNull View view,
                                                    @NonNull View.OnClickListener onClickListener,
                                                    @NonNull final int[] elementIDs) {
        for (int id : elementIDs) {
            view.findViewById(id).setOnClickListener(onClickListener);
        }
    }

    public static void addHTML(TextView tv, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tv.setText(Html.fromHtml(text));
        }
    }
}
