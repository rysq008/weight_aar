package com.innovationai.pigweight.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import io.reactivex.annotations.NonNull;

/**
 * @author wbs on 10/29/17.
 */

public class UIUtils {
    public static final int DEFAULT_VIEW_HEIGHT = 55;

    public static int getWidthPixels(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Configuration cf = context.getResources().getConfiguration();
        int ori = cf.orientation;
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
            return displayMetrics.heightPixels;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {// 竖屏
            return displayMetrics.widthPixels;
        }
        return 0;
    }

    public static int getHeightPixels(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Configuration cf = context.getResources().getConfiguration();
        int ori = cf.orientation;
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
            return displayMetrics.widthPixels;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {// 竖屏
            return displayMetrics.heightPixels;
        }
        return 0;
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void setDefaultRootViewSize(@NonNull Context context, @NonNull ViewGroup rootView){
        ViewGroup.LayoutParams rootParams = rootView.getLayoutParams();
        rootParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        rootParams.height = UIUtils.dp2px(context,45);
        rootView.setLayoutParams(rootParams);

    }
}
