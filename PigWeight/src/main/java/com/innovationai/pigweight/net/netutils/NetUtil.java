package com.innovationai.pigweight.net.netutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.innovationai.pigweight.WeightSDKManager;

/**
 * @Author: Lucas.Cui
 * 时   间：2019/3/22
 * 简   述：<功能简述> 网络请求工具类
 */
public class NetUtil {

    /**
     * 判断是否有网络连接
     *
     * @return
     */
    public static boolean isNetworkConnected() {

        ConnectivityManager mConnectivityManager = (ConnectivityManager) WeightSDKManager.getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager
                .getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }

        return false;
    }

    /**
     * 判断WIFI网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) WeightSDKManager.getAppContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    public static boolean isWiFiActive() {
        ConnectivityManager connectivity = (ConnectivityManager) WeightSDKManager.getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型信息
     *
     * @return
     */
    public static int getConnectedType() {
        if (WeightSDKManager.getAppContext() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) WeightSDKManager.getAppContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
}
