package utils;

import android.content.Context;

/**
 * Created by Paolo on 29/01/2016.
 * Contains additional methods to help the screen-images management.
 */
public class PixelManager {

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
