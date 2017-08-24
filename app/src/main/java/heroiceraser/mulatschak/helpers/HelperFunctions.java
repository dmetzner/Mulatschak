package heroiceraser.mulatschak.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 14.08.2017.
 */

public final class HelperFunctions {
    private HelperFunctions() {}

    public static Bitmap loadBitmap(GameView view, String image_name, int width, int height,
                              String package_name) {
        int resourceId =
                view.getResources().getIdentifier(image_name, package_name, view.getContext().getPackageName());
        Bitmap bitmap =
                BitmapFactory.decodeResource(view.getContext().getResources(),resourceId);

        // scale Bitmap
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

        assert(scaledBitmap != null) : "StichAnsage -> Bitmap == null";

        return (scaledBitmap);
    }

}
