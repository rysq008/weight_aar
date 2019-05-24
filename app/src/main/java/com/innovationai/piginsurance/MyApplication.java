package com.innovationai.piginsurance;

import android.app.Application;

import com.innovationai.pigweight.AppConfig;


/**
 * @Author: Lucas.Cui
 * 时   间：2019/5/23
 * 简   述：<功能简述>
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        AppConfig.newInstance().onCreate(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //注销
        AppConfig.newInstance().onTerminate();
    }
}
