package com.innovationai.pigweigh.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.apeng.permissions.EsayPermissions;
import com.apeng.permissions.OnPermission;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.innovationai.pigweigh.Constant;
import com.innovationai.pigweigh.R;
import com.innovationai.pigweigh.net.bean.BaseBean;
import com.innovationai.pigweigh.net.netsubscribe.HttpRequestSubscribe;
import com.innovationai.pigweigh.net.netutils.NetError;
import com.innovationai.pigweigh.net.netutils.OnSuccessAndFaultListener;
import com.innovationai.pigweigh.net.netutils.OnSuccessAndFaultSub;
import com.innovationai.pigweigh.utils.SPUtils;

import java.lang.reflect.Type;
import java.util.List;


/**
 * @Author: Lucas.Cui
 * 时   间：2019/5/22
 * 简   述：<功能简述>
 */
public class SplashActivity extends AppCompatActivity {

    //配置需要取的权限
    private static final String[] PERMISSION = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
            Manifest.permission.ACCESS_COARSE_LOCATION, //定位权限
            Manifest.permission.CAMERA
    };

    public static void start(Activity context, Bundle bundle) {

        Intent intent = new Intent(context, SplashActivity.class);
        intent.putExtra(Constant.ACTION_BUNDLE, bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // TODO: 2019/5/24 现在只在这里使用appId
        Bundle bundle = getIntent().getBundleExtra(Constant.ACTION_BUNDLE);
        if (!verifyParams(bundle)) {
            Toast.makeText(SplashActivity.this, "参数不能为空", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String appId = bundle.getString(Constant.ACTION_APPID);
        String token = bundle.getString(Constant.ACTION_TOKEN);
        if (appId.equals(SPUtils.getValue(SplashActivity.this, Constant.ACTION_APPID, ""))) {
            if (EsayPermissions.isHasPermission(getApplicationContext(), PERMISSION)) {
                toWeightPage();
            } else {
                requestPermission();
            }
        } else {
            getEnVerification(appId);
        }
    }

    private boolean verifyParams(Bundle bundle) {
        if (bundle == null) return false;
        if (TextUtils.isEmpty(bundle.getString(Constant.ACTION_APPID))) return false;
        if (TextUtils.isEmpty(bundle.getString(Constant.ACTION_TOKEN))) return false;
        return true;
    }

    /**
     * 校验企业身份
     */
    private void getEnVerification(final String appId) {
        HttpRequestSubscribe.getEnVerification(appId, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    BaseBean<Object> bean;
                    Gson gson = new Gson();
                    Type type = new TypeToken<BaseBean<Object>>() {
                    }.getType();
                    bean = gson.fromJson(result, type);
                    if (bean.getStatus() == Constant.RESULT_OK) {
                        Toast.makeText(SplashActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        SPUtils.putValue(SplashActivity.this, Constant.ACTION_APPID, (String) appId);
                        if (EsayPermissions.isHasPermission(getApplicationContext(), PERMISSION)) {
                            toWeightPage();
                        } else {
                            requestPermission();
                        }
                    } else {
                        Toast.makeText(SplashActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SplashActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailed(NetError errorMsg) {
                Toast.makeText(SplashActivity.this, errorMsg.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        }, SplashActivity.this, true));
    }

    /**
     * 请求权限
     */
    public void requestPermission() {
        EsayPermissions.with(this)
//                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
//                .permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES) //支持请求6.0悬浮窗权限8.0请求安装权限
                .permission(PERMISSION)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            toWeightPage();
                        } else {
                            Toast.makeText(SplashActivity.this, "获取权限失败", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {

                        Toast.makeText(SplashActivity.this, "获取权限失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    /**
     * 进入称重页面
     */
    private void toWeightPage() {
        WeightPicCollectActivity.start(SplashActivity.this);
        finish();
    }
}
