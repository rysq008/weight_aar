package com.innovationai.pigweigh.net.netsubscribe;

import com.innovationai.pigweigh.net.netutils.HttpMethods;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * @Author: Lucas.Cui
 * 时   间：2019/3/22
 * 简   述：<功能简述> 把功能模块来分别存放不同的请求方法，比如登录注册类LoginSubscribe、电影类MovieSubscribe
 */

public class HttpRequestSubscribe {

    /**
     * 身份校验
     */
    public static void getEnVerification(String appId, DisposableObserver<ResponseBody> subscriber) {
        Map<String, String> params = new HashMap<String, String>();
        Observable<ResponseBody> observable = HttpMethods.getInstance().getHttpApi().getEnVerification(params, appId);
        HttpMethods.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 上传图片,模型识别
     */
    public static void getRecognition(String base64Img, String appId, DisposableObserver<ResponseBody> subscriber) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("base64Img", base64Img);
        Observable<ResponseBody> observable = HttpMethods.getInstance().getHttpApi().getRecognition(params, appId);
        HttpMethods.getInstance().toSubscribe(observable, subscriber);
    }
}
