package heroiceraser.mulatschak.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.util.Log;

import heroiceraser.mulatschak.game.GameView;

/**
 * Created by Daniel Metzner on 14.08.2017.
 */

public final class HelperFunctions {
    private HelperFunctions() {}

    public static String getStringOfFixedLength(String s, int length) {
        if (s.length() > length) {
            s = s.substring(0, length);
        }
        StringBuffer sb = new StringBuffer();
        while (s.length() < length) {
            sb.append(" ");
        }
        s += sb;
        return s;
    }

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

    public static Bitmap createBitmapOverlay(Bitmap bitmap) {
        int [] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for( int i = 0; i < pixels.length; i++ ) {
            //  shift to right to get rid of rgb(each 8 bit) & mask out sign part
            if ((pixels[i] >> 24  & 0xff) < 0xFF) {
                continue;
            }
            pixels[i] = Color.DKGRAY;
        }
        return Bitmap.createBitmap( pixels, bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888 );
    }

    public static Bitmap adjustOpacity(Bitmap bitmap, int opacity) {
        Bitmap mutableBitmap = bitmap.isMutable() ? bitmap : bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        int colour = (opacity & 0xFF) << 24;
        canvas.drawColor(colour, PorterDuff.Mode.DST_IN);
        return mutableBitmap;
    }
}
