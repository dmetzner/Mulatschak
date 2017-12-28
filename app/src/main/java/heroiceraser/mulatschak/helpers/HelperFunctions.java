package heroiceraser.mulatschak.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;

import heroiceraser.mulatschak.game.GameView;

import static android.R.id.message;

/**
 * Created by Daniel Metzner on 14.08.2017.
 */

public final class HelperFunctions {
    private HelperFunctions() {}

    public static Bitmap loadBitmap(GameView view, String image_name, int width, int height) {

        String package_name = "drawable";

        if (width <= 0) {
            Log.w("loadBitmap",
                    "Image: " + image_name + " -> width has to be greater than 0");
            return null;
        }
        if (height <= 0) {
            Log.w("loadBitmap",
                    "Image: " + image_name +  "-> height has to be greater than 0");
            return null;
        }

        int resourceId =  view.getResources()
                .getIdentifier(image_name, package_name, view.getContext().getPackageName());

        if (resourceId == 0) {
            Log.w("loadBitmap",
                    "Image Name: " + image_name + " does not exist in package: " + package_name);
            return null;
        }

        Bitmap bitmap =
                BitmapFactory.decodeResource(view.getContext().getResources(),resourceId);

        if (bitmap == null) {
            Log.w("loadBitmap", "Decoding Image: " + image_name + " failed");
            return null;
        }

        // scale Bitmap
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

        assert(scaledBitmap != null) : "StichAnsage -> Bitmap == null";

        return (scaledBitmap);
    }


    public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        return Bitmap.createBitmap(bitmap , 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
