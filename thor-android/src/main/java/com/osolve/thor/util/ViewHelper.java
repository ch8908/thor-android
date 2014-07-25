package com.osolve.thor.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.osolve.thor.activity.MainActivity;

/**
 * Created by Kros on 7/25/14.
 */
public class ViewHelper {

    public ViewHelper() {

    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getScreenHeight(final Context context) {
        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= 13) {
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return size.y;
        }
        return wm.getDefaultDisplay().getHeight();
    }

    public static int getScreenWidth(final Context context) {
        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= 13) {
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return size.x;
        }
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getDp(final Context context, final int pixel) {
        return Math.round(pixel / context.getResources().getDisplayMetrics().density);
    }

    public static int getScreenWidthInDp(final Context context) {
        return getDp(context, context.getResources().getDisplayMetrics().widthPixels);
    }

    public static int toPixel(final Context context, final int dp) {
        return Math.round(context.getResources().getDisplayMetrics().density * dp);
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
