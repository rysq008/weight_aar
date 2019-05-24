package com.innovationai.pigweigh.net.netutils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.innovationai.pigweigh.Constant;
import com.innovationai.pigweigh.net.utils.CompressUtils;
import com.socks.library.KLog;

import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * @Author: Lucas.Cui
 * 时   间：2019/3/22
 * 简   述：<功能简述> 请求回调
 * * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理   成功时 通过result是否等于1分别回调onSuccess和onFault，默认处理了401错误转登录。
 * 回调结果为String，需要手动序列化
 */

public class OnSuccessAndFaultSub extends DisposableObserver<ResponseBody>
        implements ProgressCancelListener {
    /**
     * 是否需要显示默认Loading
     */
    private boolean showProgress = true;
    private OnSuccessAndFaultListener mOnSuccessAndFaultListener;

    private Context context;
    private ProgressDialog progressDialog;
    private String mMsg = "正在加载中";


    /**
     * @param mOnSuccessAndFaultListener 成功回调监听
     */
    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
    }


    /**
     * @param mOnSuccessAndFaultListener 成功回调监听
     * @param context                    上下文
     */
    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener, Context context) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
        this.context = context;
    }

    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener, Context context, String msg) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
        this.context = context;
        this.mMsg = msg;
    }

    /**
     * @param mOnSuccessAndFaultListener 成功回调监听
     * @param context                    上下文
     * @param showProgress               是否需要显示默认Loading
     */
    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener, Context context, boolean showProgress) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
        this.context = context;
        this.showProgress = showProgress;
    }

    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener, Context context, String msg, boolean showProgress) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
        this.context = context;
        this.mMsg = msg;
        this.showProgress = showProgress;
    }

    private void showProgressDialog() {
        // TODO: 2019/5/23 加载弹框处理
        if (showProgress && context != null) {
            progressDialog = ProgressDialog.show(context, "请稍候", mMsg);
//            AbDialogUtil.showProgressDialog(context, R.drawable.progress_circular,mMsg);
        }
    }


    private void dismissProgressDialog() {
        if (null != context && progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        showProgressDialog();
    }


    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onComplete() {
        dismissProgressDialog();
        progressDialog = null;
    }


    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     */
    @Override
    public void onError(Throwable e) {
        try {

            if (e instanceof SocketTimeoutException) {//请求超时
            } else if (e instanceof ConnectException) {//网络连接超时
//                                ToastManager.showShortToast("网络连接超时");
                mOnSuccessAndFaultListener.onFailed(new NetError(-2, "网络连接失败"));
            } else if (e instanceof SSLHandshakeException) {//安全证书异常
                //                ToastManager.showShortToast("安全证书异常");
                mOnSuccessAndFaultListener.onFailed(new NetError(-2, "安全证书异常"));
            } else if (e instanceof HttpException) {//请求的地址不存在
                int code = ((HttpException) e).code();
                if (code == 504) {
                    //                    ToastManager.showShortToast("网络异常，请检查您的网络状态");
                    mOnSuccessAndFaultListener.onFailed(new NetError(-2, "网络异常，请检查您的网络状态"));
                } else if (code == 404) {
                    //                    ToastManager.showShortToast("请求的地址不存在");
                    mOnSuccessAndFaultListener.onFailed(new NetError(-2, "请求的地址不存在"));
                } else {
                    //                    ToastManager.showShortToast("请求失败");
                    mOnSuccessAndFaultListener.onFailed(new NetError(-2, "请求失败"));
                }
            } else if (e instanceof UnknownHostException) {//域名解析失败
                //                ToastManager.showShortToast("域名解析失败");
                mOnSuccessAndFaultListener.onFailed(new NetError(-2, "域名解析失败"));
            } else {
                //                ToastManager.showShortToast("error:" + e.getMessage());
                mOnSuccessAndFaultListener.onFailed(new NetError(-2, "error:" + e.getMessage()));
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            Log.e("OnSuccessAndFaultSub", "error:" + e.getMessage());
            //            mOnSuccessAndFaultListener.onFailed("error:" + e.getMessage());
            dismissProgressDialog();
            progressDialog = null;

        }
        try {
//            saveErrorMessage((Exception) e);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }


    /**
     * 当result等于1回调给调用者，否则自动显示错误信息，若错误信息为401跳转登录页面。
     * ResponseBody  body = response.body();//获取响应体
     * InputStream inputStream = body.byteStream();//获取输入流
     * byte[] bytes = body.bytes();//获取字节数组
     * String str = body.string();//获取字符串数据
     */
    @Override
    public void onNext(ResponseBody body) {
        try {
            final String result = CompressUtils.decompress(body.byteStream());
            KLog.json("okhttp body", result);
            JSONObject jsonObject = new JSONObject(result);
            int resultCode = jsonObject.getInt("status");
            if (resultCode == Constant.RESULT_OK) {
                mOnSuccessAndFaultListener.onSuccess(result);
            } else {
                String errorMsg = jsonObject.getString("msg");
                mOnSuccessAndFaultListener.onFailed(new NetError(resultCode, errorMsg));
                KLog.e("OnSuccessAndFaultSub", "errorMsg: " + errorMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            saveErrorMessage(e);
        }
    }


    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isDisposed()) {
            this.dispose();
        }
    }
//
//    public static void saveErrorMessage(Exception e) {
////         测试 SDK 是否正常工作的代码
//        AVObject avObject = new AVObject("Android_phone");
////        avObject.put("Cookie", SharedPreUtil.getSessionId());
//        avObject.put("userId", SPUtils.getValue(MyApplication.getContext(), "userId", ""));
//        avObject.put("brand", SystemUtil.getDeviceBrand());
//        avObject.put("model", SystemUtil.getSystemModel());
//        avObject.put("systemversion", SystemUtil.getSystemVersion());
//        avObject.put("sdkVersion", SystemUtil.getSDKVersion());
//        avObject.put("versionName", SystemUtil.getLocalVersionName());
////        avObject.put("registrationid", JPushInterface.getRegistrationID(App.getInstance()));
//        avObject.put("plam", System.getProperty("os.name"));
//
//        StringBuilder sb = new StringBuilder();
//
//        Writer writer = new StringWriter();
//        PrintWriter pw = new PrintWriter(writer);
//        e.printStackTrace(pw);
////        Throwable cause = e.getCause();
////        // 循环取出Cause
////        while (cause != null) {
////            cause.printStackTrace(pw);
////            cause = e.getCause();
////        }
//        pw.close();
//        String result = writer.toString();
//        sb.append(result);
//        avObject.put("error", e.toString() + "--＞" + sb.toString());
//
//        avObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(AVException e) {
//                if (e == null) {
//                    Log.d("saved", "success!");
//                }
//            }
//        });
//
//    }
}
