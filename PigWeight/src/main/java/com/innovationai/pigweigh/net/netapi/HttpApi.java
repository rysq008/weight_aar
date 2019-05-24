package com.innovationai.pigweigh.net.netapi;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * @Author: Lucas.Cui
 * 时   间：2019/3/22
 * 简   述：<功能简述> 存放所有API
 */

public interface HttpApi {

    /**
     * 校验用户身份
     *
     * @param map
     * @return
     */
    @POST("nongxian2/app/enVerification")
//    @POST("app/enVerification")
    Observable<ResponseBody> getEnVerification(@QueryMap Map<String, String> map, @Header("appId") String appId);

    /**
     * 上传图片，模型识别
     *
     * @return
     */
    @POST("nongxian2/app/recognition")
//    @POST("app/recognition")
    Observable<ResponseBody> getRecognition(@Body Map<String, String> map, @Header("appId") String appId);
}
