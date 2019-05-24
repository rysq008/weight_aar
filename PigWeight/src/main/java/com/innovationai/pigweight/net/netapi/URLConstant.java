package com.innovationai.pigweight.net.netapi;

import com.innovationai.pigweight.net.netutils.HttpMethods;

/**
 * @Author: Lucas.Cui
 * 时   间：2019/3/22
 * 简   述：<功能简述> 开发环境管理
 */

public class URLConstant {
    //存放全部的URL（可分为开发、测试、正式）
//    public static String BASE_URL_TEST = "http://test1.innovationai.cn:8086/";
    // TODO: 2019/3/26 添加正式环境后，修改为正式环境 
//    public static String BASE_URL_RELEASE = "";
//    public static String BASE_URL_RELEASE = "http://192.168.1.109:8081/";
    public static String BASE_URL_RELEASE = "http://47.92.167.61:8081/";
    public static String BASE_URL = BASE_URL_RELEASE;


    public static void resetIp(String baseUrl) {
        BASE_URL = baseUrl;
        HttpMethods.getInstance().changeBaseUrl(baseUrl);
    }

    public static boolean isOnlineHost() {
        return BASE_URL_RELEASE.equals(BASE_URL);
    }
}
