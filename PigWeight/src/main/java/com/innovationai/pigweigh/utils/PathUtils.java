package com.innovationai.pigweigh.utils;

import android.os.Environment;

/**
 * @Author: Lucas.Cui
 * 时   间：2019/1/21
 * 简   述：<功能简述>
 */
public class PathUtils {
    public static final String app_base_dir = "/com.innovationai.piginsurance";
    public static final String app_cache_dir = getDataPath() + app_base_dir + "/data/cache";

    public static final String app_data_dir = getDataPath() + app_base_dir + "/data/";
    public static final String weightImgPath = getDataPath() + app_base_dir + "/data/picture/weight";


    public static String getDataPath() {

        return isHaveSDCard() ? Environment.getExternalStorageDirectory().getPath() : Environment.getDataDirectory().getPath();
    }

    /**
     * 判断是否存在SD卡
     *
     * @return true，有SD卡。false，无SD卡
     */
    public static boolean isHaveSDCard() {

        String SDState = Environment.getExternalStorageState();
        return SDState.equals(Environment.MEDIA_MOUNTED);
    }
}
