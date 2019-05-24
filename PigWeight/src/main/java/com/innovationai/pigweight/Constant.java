package com.innovationai.pigweight;

public class Constant {

    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String ADDRESS_DETAIL = "address_detail";

    public static final int RESULT_OK = 1;
    public static final int RESULT_FAILED = 0;
    public static final String ACTION_APPID = "appId";
    public static final String ACTION_TOKEN = "token";
    public static final String ACTION_IMGWIDTH = "imgWidth";
    public static final String ACTION_IMGHEIGHT = "imgHeight";
    public static final String ACTION_BUNDLE = "bundle";
    // 连接超时
    public static final int timeOut = 30 * 1000;
    // 建立连接
    public static final int connectOut = 30 * 1000;
    // 获取数据
    public static final int getOut = 60000;

    //1表示已下载完成
    public static final int downloadComplete = 1;
    //1表示未开始下载
    public static final int undownLoad = 0;
    //2表示已开始下载
    public static final int downInProgress = 2;
    //3表示下载暂停
    public static final int downLoadPause = 3;

    public static final int pageSize = 10;
}
