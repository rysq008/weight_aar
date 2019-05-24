package com.innovationai.pigweight.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.innovationai.pigweight.Constant;
import com.innovationai.pigweight.R;
import com.innovationai.pigweight.camera.CameraSurfaceView;
import com.innovationai.pigweight.camera.CameraUtils;
import com.innovationai.pigweight.camera.ImageUtils;
import com.innovationai.pigweight.camera.SpiritView;
import com.innovationai.pigweight.event.EventManager;
import com.innovationai.pigweight.net.bean.BaseBean;
import com.innovationai.pigweight.net.bean.RecognitionBean;
import com.innovationai.pigweight.net.netsubscribe.HttpRequestSubscribe;
import com.innovationai.pigweight.net.netutils.NetError;
import com.innovationai.pigweight.net.netutils.OnSuccessAndFaultListener;
import com.innovationai.pigweight.net.netutils.OnSuccessAndFaultSub;
import com.innovationai.pigweight.utils.SPUtils;
import com.innovationai.pigweight.utils.UIUtils;
import com.socks.library.KLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Lucas.Cui
 * 时   间：2019/5/23
 * 简   述：<功能简述>
 */
public class WeightPicCollectActivity extends AppCompatActivity implements SensorEventListener, ViewTreeObserver.OnGlobalLayoutListener, View.OnClickListener {
    private static final String TAG = "WeightPicCollectActivity";
    /**
     * 数据反馈广播action
     */
    public static final String ACTION_RECOGNITION = "innovationai.intent.action.Recognition";
    private static final String[] NEEDED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private static final int ACTION_REQUEST_PERMISSIONS = 1;

    ImageView iv_preview;
    ImageView btn_upload;
    TextView btn_finish;
    //定义水平仪的仪表盘
    SpiritView spiritwiew;
    FrameLayout fl_preview;
    CameraSurfaceView mPreviewSurfaceview;
    //定义水平仪能处理的最大倾斜角度，超过该角度气泡直接位于边界
    private int MAX_ANGLE = 30;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float timestamp;
    private float angle[] = new float[3];
    private int mOrientation;

    private boolean mSafeToTakePicture = true, mGrantedCameraRequested, isCanTakePic;
    private static long internalTime;
    private Bitmap mBitmap;
    private float mImgHeight, mImgWidth;

    public static void start(Activity context) {
        //延时2s，防止重复启动页面
        if (System.currentTimeMillis() - internalTime > 2000) {
            Intent intent = new Intent(context, WeightPicCollectActivity.class);
            context.startActivity(intent);
        }
        internalTime = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_piccollect);
        iv_preview = findViewById(R.id.iv_preview);
        btn_upload = findViewById(R.id.btn_upload);
        btn_finish = findViewById(R.id.btn_finish);
        spiritwiew = findViewById(R.id.spiritwiew);
        fl_preview = findViewById(R.id.fl_preview);
        spiritwiew.setOnClickListener(this);
        btn_finish.setOnClickListener(this);
        btn_upload.setOnClickListener(this);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        CameraUtils.setPreviewHeight(UIUtils.getWidthPixels(this) / 9 * 16);
        CameraUtils.setPreviewWidth(UIUtils.getWidthPixels(this));
        fl_preview.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {

        fl_preview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if (checkPermissions(NEEDED_PERMISSIONS)) {
            mGrantedCameraRequested = true;
            initCamera();
        } else {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        }
    }

    /**
     * 检测权限
     *
     * @param neededPermissions
     * @return
     */
    private boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    /**
     * 授权结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACTION_REQUEST_PERMISSIONS:
                boolean isAllGranted = true;
                for (int grantResult : grantResults) {
                    isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
                }
                if (isAllGranted) {
                    mGrantedCameraRequested = true;
                    initCamera();
                } else {
                    Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化camera 预览
     */
    private void initCamera() {
        mPreviewSurfaceview = new CameraSurfaceView(this);
        fl_preview.addView(mPreviewSurfaceview);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPreviewSurfaceview.getLayoutParams();
        params.width = UIUtils.getWidthPixels(this);
        params.height = UIUtils.getWidthPixels(this) * 16 / 9;
        mPreviewSurfaceview.setLayoutParams(params);
        mPreviewSurfaceview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUtils.doAutoFocus();
            }
        });
        mOrientation = CameraUtils.calculateCameraPreviewOrientation(WeightPicCollectActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGrantedCameraRequested) {
            CameraUtils.startPreview();
        }
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL, null);
    }

    @Override
    protected void onPause() {
        super.onPause();

        CameraUtils.stopPreview();
        mSensorManager.unregisterListener(this, mSensor);
    }

    @Override
    protected void onStop() {
        super.onStop();
        CameraUtils.stopPreview();
    }


    /**
     * 拍照
     */
    private void takePicture() {
        CameraUtils.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                KLog.d("origh width : " + bitmap.getWidth() + " height : " + bitmap.getHeight());
                try {
                    if (bitmap != null) {
                        bitmap = ImageUtils.getRotatedBitmap(bitmap, mOrientation);
                        bitmap = ImageUtils.compressBitmap(bitmap);
                        KLog.d("scale width : " + bitmap.getWidth() + " height : " + bitmap.getHeight());
                        btn_upload.setVisibility(View.VISIBLE);
                        iv_preview.setVisibility(View.VISIBLE);
                        iv_preview.setImageBitmap(bitmap);
                        btn_finish.setText("重拍");
                        mBitmap = bitmap;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CameraUtils.startPreview();
                mSafeToTakePicture = true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.spiritwiew) {
            if (btn_upload.getVisibility() == View.VISIBLE || !isCanTakePic) {
                return;
            }
            if (mSafeToTakePicture) {
                mSafeToTakePicture = false;
                takePicture();
            }
        } else if (i == R.id.btn_upload) {
            if (btn_upload.getVisibility() != View.VISIBLE || mBitmap == null) {
                Toast.makeText(WeightPicCollectActivity.this, "请先拍照", Toast.LENGTH_SHORT).show();
                return;
            }
            getRecognition(bitmapToBase64(mBitmap));
        } else if (i == R.id.btn_finish) {
            if (btn_upload.getVisibility() == View.VISIBLE) {
                btn_upload.setVisibility(View.GONE);
                iv_preview.setVisibility(View.GONE);
                btn_finish.setText("退出");
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 将图片转换成Base64编码
     *
     * @param bitmap 待处理图片
     * @return
     */
    private String bitmapToBase64(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 图片按比例压缩，以长边=1080为准
        // 图片质量提升为60.
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scale = 1f;

        int max = Math.max(width, height);
        if (max > 1080) {
            scale = 1080f / max;
        }
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        newbm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = new ByteArrayInputStream(baos.toByteArray());
        byte[] data = null;
        //读取图片字节数组
        try {
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String base64Str = Base64.encodeToString(data, Base64.DEFAULT);
        KLog.i("base64Str", base64Str);
        return base64Str;
    }

    /**
     * 上传图片到模型识别
     *
     * @param base64Img
     */
    private void getRecognition(String base64Img) {
        String appId = (String) SPUtils.getValue(WeightPicCollectActivity.this, "appId", "");
        HttpRequestSubscribe.getRecognition(base64Img, appId, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    BaseBean<RecognitionBean> bean;
                    Gson gson = new Gson();
                    Type type = new TypeToken<BaseBean<RecognitionBean>>() {
                    }.getType();
                    bean = gson.fromJson(result, type);
                    if (bean.getStatus() == Constant.RESULT_OK && bean.getData() != null) {
                        if (bean.getData().getStatus() == Constant.RESULT_OK) {
//                            Intent intent = new Intent(ACTION_RECOGNITION);
//                            intent.putExtra("data", (Serializable) bean.getData().getRecognitionResult());
//                            ByteArrayOutputStream baos=new ByteArrayOutputStream();
//                            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                            byte [] bitmapByte =baos.toByteArray();
//                            intent.putExtra("bitmap", bitmapByte);
//                            LocalBroadcastManager.getInstance(WeightPicCollectActivity.this).sendBroadcast(intent);
                            Bitmap bitmap = ImageUtils.ratioScaleBitmap(mBitmap);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] bitmapByte = baos.toByteArray();
                            Map<String, Object> map = new HashMap<>();
                            map.put("data", bean.getData().getRecognitionResult());
                            map.put("bitmap", bitmapByte);
                            EventManager.getInstance().postEventEvent(map);
                            KLog.d("scale compress width : " + bitmap.getWidth() + " height : " + bitmap.getHeight());

                            Toast.makeText(WeightPicCollectActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();

                            finish();
                        } else {
                            retryPreview();
                            Toast.makeText(WeightPicCollectActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(WeightPicCollectActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(WeightPicCollectActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(NetError errorMsg) {
                Toast.makeText(WeightPicCollectActivity.this, errorMsg.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, WeightPicCollectActivity.this, true));
    }

    private void retryPreview() {
        btn_upload.setVisibility(View.GONE);
        iv_preview.setVisibility(View.GONE);
        btn_finish.setText("退出");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (btn_upload.getVisibility() == View.VISIBLE) {
            spiritwiew.setColor(255);
            spiritwiew.postInvalidate();
            return;
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
// x,y,z分别存储坐标轴x,y,z上的加速度
            float x = event.values[0];

            float y = event.values[1];

            float z = event.values[2];
// 根据三个方向上的加速度值得到总的加速度值a
            float a = (float) Math.sqrt(x * x + y * y + z * z);

// 传感器从外界采集数据的时间间隔为10000微秒
//            System.out.println("magneticSensor.getMinDelay()-------->" + magneticSensor.getMinDelay());
// 加速度传感器的最大量程
//            System.out.println("event.sensor.getMaximumRange()-------->" + event.sensor.getMaximumRange());


        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

// 三个坐标轴方向上的电磁强度，单位是微特拉斯(micro-Tesla)，用uT表示，也可以是高斯(Gauss),1Tesla=10000Gauss
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

//从 x、y、z 轴的正向位置观看处于原始方位的设备，如果设备逆时针旋转，将会收到正值；否则，为负值

            if (timestamp != 0) {

// 得到两次检测到手机旋转的时间差（纳秒），并将其转化为秒

                final float dT = 1;//(event.timestamp - timestamp) * NS2S;

// 将手机在各个轴上的旋转角度相加，即可得到当前位置相对于初始位置的旋转弧度
                angle[0] += event.values[0] * dT;

                angle[1] += event.values[1] * dT;

                angle[2] += event.values[2] * dT;

                float anglex = (float) Math.toDegrees(angle[0]);

                float angley = (float) Math.toDegrees(angle[1]);

                float anglez = (float) Math.toDegrees(angle[2]);

            }
            timestamp = event.timestamp;
        } else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
//            values[0]: Azimuth(方位)，地磁北方向与y轴的角度，围绕z轴旋转(0到359)。0=North, 90=East, 180=South, 270=West 
//            values[1]: Pitch(俯仰),围绕X轴旋转(-180 to 180), 当Z轴向Y轴运动时是正值
//            values[2]: Roll(滚)，围绕Y轴旋转(-90 to 90)，当X轴向Z轴运动时是正值 -02,0.2
            float anglex = (float) (event.values[0]);

            float angley = (float) (event.values[1]);

            float anglez = (float) (event.values[2]);
            long curr_time = SystemClock.elapsedRealtime();
            long last_time = spiritwiew.getTag() == null ? 0 : (long) (spiritwiew.getTag());
            if (curr_time - last_time < 500)
                return;
            if (angley >= -3 && angley <= 3 && anglez >= -3 && anglez <= 3) {
//                btn_take.setVisibility(View.VISIBLE);
                spiritwiew.setColor(255);
                isCanTakePic = true;
            } else {
                spiritwiew.setColor(150);
                isCanTakePic = false;
//                btn_take.setVisibility(View.GONE);
            }
//            tv_position.setText("x: " + (int) anglex + "  y: " + (int) angley + "   z: " + (int) anglez);
        }
        checkPosition(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void checkPosition(SensorEvent sensorEvent) {
        float values[] = sensorEvent.values;
        //获取传感器的类型
        int sensorType = sensorEvent.sensor.getType();
        switch (sensorType) {
            case Sensor.TYPE_ORIENTATION:
                //获取与Y轴的夹角
                float yAngle = values[1];
                //获取与Z轴的夹角
                float zAngle = values[2];
                //气泡位于中间时（水平仪完全水平）
                int x = (spiritwiew.getBack().getWidth() - spiritwiew.getBubble().getWidth()) / 2;
                int y = (spiritwiew.getBack().getHeight() - spiritwiew.getBubble().getHeight()) / 2;
                //如果与Z轴的倾斜角还在最大角度之内
                if (Math.abs(zAngle) <= MAX_ANGLE) {
                    //根据与Z轴的倾斜角度计算X坐标轴的变化值
                    int deltaX = (int) ((spiritwiew.getBack().getWidth() - spiritwiew.getBubble().getWidth()) / 2
                            * zAngle / MAX_ANGLE);
                    x += deltaX;
                }
                //如果与Z轴的倾斜角已经大于MAX_ANGLE，气泡应到最左边
                else if (zAngle > MAX_ANGLE) {
                    x = 0;
                }
                //如果与Z轴的倾斜角已经小于负的Max_ANGLE,气泡应到最右边
                else {
                    x = spiritwiew.getBack().getWidth() - spiritwiew.getBubble().getWidth();
                }

                //如果与Y轴的倾斜角还在最大角度之内
                if (Math.abs(yAngle) <= MAX_ANGLE) {
                    //根据与Z轴的倾斜角度计算X坐标轴的变化值
                    int deltaY = (int) ((spiritwiew.getBack().getHeight() - spiritwiew.getBubble().getHeight()) / 2
                            * yAngle / MAX_ANGLE);
                    y += deltaY;
                }
                //如果与Y轴的倾斜角已经大于MAX_ANGLE，气泡应到最下边
                else if (yAngle > MAX_ANGLE) {
                    y = spiritwiew.getBack().getHeight() - spiritwiew.getBubble().getHeight();
                }
                //如果与Y轴的倾斜角已经小于负的Max_ANGLE,气泡应到最上边
                else {
                    y = 0;
                }

                if (true) {
                    spiritwiew.bubbleX = x;
                    spiritwiew.bubbleY = y;

                }
                //通知组件更新
                spiritwiew.postInvalidate();
                break;
        }
    }
}

