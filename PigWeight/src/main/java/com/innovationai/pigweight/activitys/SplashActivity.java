package com.innovationai.pigweight.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;

import com.apeng.permissions.EsayPermissions;
import com.apeng.permissions.OnPermission;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.innovationai.pigweight.Constants;
import com.innovationai.pigweight.R;
import com.innovationai.pigweight.WeightSDKManager;
import com.innovationai.pigweight.net.bean.BaseBean;
import com.innovationai.pigweight.net.netsubscribe.HttpRequestSubscribe;
import com.innovationai.pigweight.net.netutils.NetError;
import com.innovationai.pigweight.net.netutils.OnSuccessAndFaultListener;
import com.innovationai.pigweight.net.netutils.OnSuccessAndFaultSub;
import com.innovationai.pigweight.utils.SPUtils;

import java.lang.reflect.Type;
import java.util.List;


/**
 * @Author: Lucas.Cui
 * 时   间：2019/5/22
 * 简   述：<功能简述>
 */
public class SplashActivity extends Activity {

    //配置需要取的权限
    private static final String[] PERMISSION = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
            Manifest.permission.ACCESS_COARSE_LOCATION, //定位权限
            Manifest.permission.CAMERA
    };
    private Bundle bundle;

    public static void start(Activity context, Bundle bundle) {

        Intent intent = new Intent(context, SplashActivity.class);
        intent.putExtra(Constants.ACTION_BUNDLE, bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // TODO: 2019/5/24 现在只在这里使用appId
        bundle = getIntent().getBundleExtra(Constants.ACTION_BUNDLE);
        if (!verifyParams(bundle)) {
            finish();
            return;
        }
        WeightSDKManager.newInstance().init(getApplication());
    }

    @Override
    protected void onStart() {
        super.onStart();
        String appId = bundle.getString(Constants.ACTION_APPID);
        String token = bundle.getString(Constants.ACTION_TOKEN);
        if (appId.equals(SPUtils.getValue(SplashActivity.this, Constants.ACTION_APPID, ""))) {
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
        if (bundle == null) {
            return false;
        }
        if (TextUtils.isEmpty(bundle.getString(Constants.ACTION_APPID))) {
            Toast.makeText(SplashActivity.this, "参数 appId 不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(bundle.getString(Constants.ACTION_TOKEN))) {
            Toast.makeText(SplashActivity.this, "参数 token 不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ((bundle.getFloat(Constants.ACTION_IMGHEIGHT) > 0 && bundle.getFloat(Constants.ACTION_IMGHEIGHT) < 100) || bundle.getFloat(Constants.ACTION_IMGHEIGHT) > 3000) {
            Toast.makeText(SplashActivity.this, "返回图片高度参数为无效值", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ((bundle.getFloat(Constants.ACTION_IMGWIDTH) > 0 && bundle.getFloat(Constants.ACTION_IMGWIDTH) < 100) || bundle.getFloat(Constants.ACTION_IMGWIDTH) > 3000) {
            Toast.makeText(SplashActivity.this, "返回图片宽度参数为无效值", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ((bundle.getFloat(Constants.ACTION_IMG_RATIO) > 0 && bundle.getFloat(Constants.ACTION_IMG_RATIO) < 0.2) || bundle.getFloat(Constants.ACTION_IMG_RATIO) > 5) {
            Toast.makeText(SplashActivity.this, "返回图片高宽比参数为无效值", Toast.LENGTH_SHORT).show();
            return false;
        }
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
                    if (bean.getStatus() == Constants.RESULT_OK) {
                        Toast.makeText(SplashActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        SPUtils.putValue(SplashActivity.this, Constants.ACTION_APPID, (String) appId);
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
                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
//                .permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES) //支持请求6.0悬浮窗权限8.0请求安装权限
                .permission(PERMISSION)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            toWeightPage();
                        } else {
                            Toast.makeText(SplashActivity.this, "部分权限获取失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        new AlertDialog.Builder(SplashActivity.this).setTitle("提示").setMessage("请打开权限设置，同意所有权限！").
                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.fromParts("package", SplashActivity.this.getPackageName(), null));
//                                        EsayPermissions.gotoPermissionSettings(SplashActivity.this);
                                        SplashActivity.this.startActivityForResult(intent, 0);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).setCancelable(false).show();
//                        Toast.makeText(SplashActivity.this, "获取权限失败", Toast.LENGTH_SHORT).show();
//                        finish();
                    }
                });
    }

    /**
     * 进入称重页面
     */
    private void toWeightPage() {
        WeightPicCollectActivity.start(SplashActivity.this, getIntent().getBundleExtra(Constants.ACTION_BUNDLE));
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            WeightSDKManager.newInstance().onDestory();
        }
        return super.onKeyDown(keyCode, event);
    }
}
