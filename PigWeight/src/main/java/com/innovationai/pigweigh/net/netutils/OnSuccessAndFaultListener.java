package com.innovationai.pigweigh.net.netutils;

/**
 * @Author: Lucas.Cui
 * 时   间：2019/3/22
 * 简   述：<功能简述> 请求回调
 */
public interface OnSuccessAndFaultListener {
    void onSuccess(String result);

    void onFailed(NetError errorMsg);
}
