package com.innovationai.pigweigh;

import android.app.Application;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.innovationai.pigweigh.utils.SPUtils;
import com.socks.library.KLog;

public class AppConfig {
    private static final String TAG = "AppConfig";
    private static Application app;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private String mAddress = "";

    public static AppConfig newInstance() {
        return Holder.appConfig;
    }

    private static class Holder {
        static AppConfig appConfig = new AppConfig();
    }

    public void onCreate(Application application) {
        app = application;
        getCurrentLocationLatLng();
        if (BuildConfig.DEBUG) {
            KLog.init(true);
        } else {
            KLog.init(false);
        }
    }

    public static Application getAppContext() {
        return app;
    }

    public void onTerminate() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    private void getCurrentLocationLatLng() {
        //初始化定位
        mLocationClient = new AMapLocationClient(app);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        // 同时使用网络定位和GPS定位,优先返回最高精度的定位结果,以及对应的地址描述信息
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {

            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    double currentLat = amapLocation.getLatitude();//获取纬度
                    double currentLon = amapLocation.getLongitude();//获取经度
                    SPUtils.putValue(getAppContext(), Constant.LATITUDE, String.valueOf(currentLat));
                    SPUtils.putValue(getAppContext(), Constant.LONGITUDE, String.valueOf(currentLon));
                    SPUtils.putValue(getAppContext(), Constant.ADDRESS_DETAIL, amapLocation.getAddress());
                    mAddress = amapLocation.getAddress();
                    amapLocation.getAccuracy();//获取精度信息
                    KLog.d("AppConfig", currentLat + "    " + currentLon + "   " + mAddress);
                    KLog.d("AppConfig", amapLocation.toString());
                } else {
                    KLog.json("AppConfig", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                }
            }
        }
    };
}
