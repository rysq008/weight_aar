package com.innovationai.piginsurance;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.innovationai.piginsuranceweightsdk.R;
import com.innovationai.pigweight.Constants;
import com.innovationai.pigweight.event.EventManager;
import com.innovationai.pigweight.event.OnEventListener;

import java.util.Map;

public class MainActivity extends Activity {
    private TextView tv_action_weight, tv_content;
    private ImageView iv_bitmap;
    private EditText et_appid, et_token, et_height, et_width, et_ratio;
    private OnEventListener mOnEventListener;
    private String mAppIdTest = "oL-mw59d4mEgDxG49-nQVM2hIha4";
    private String token = "token";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_action_weight = findViewById(R.id.tv_action_weight);
        et_appid = findViewById(R.id.et_appid);
        et_token = findViewById(R.id.et_token);
        et_height = findViewById(R.id.et_height);
        et_width = findViewById(R.id.et_width);
        et_ratio = findViewById(R.id.et_ratio);
        tv_content = findViewById(R.id.tv_content);
        iv_bitmap = findViewById(R.id.iv_bitmap);
        tv_action_weight = findViewById(R.id.tv_action_weight);
        tv_action_weight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString(Constants.ACTION_APPID, et_appid.getText().toString().trim());
                bundle.putString(Constants.ACTION_TOKEN, et_token.getText().toString().trim());
                if (!TextUtils.isEmpty(et_width.getText().toString().trim())) {
                    bundle.putFloat(Constants.ACTION_IMGWIDTH, Float.valueOf(et_width.getText().toString().trim()));
                }

                if (!TextUtils.isEmpty(et_height.getText().toString().trim())) {
                    bundle.putFloat(Constants.ACTION_IMGHEIGHT, Float.valueOf(et_height.getText().toString().trim()));
                }
                if (!TextUtils.isEmpty(et_ratio.getText().toString().trim())) {
                    bundle.putFloat(Constants.ACTION_IMG_RATIO, Float.valueOf(et_ratio.getText().toString().trim()));
                }
//                SplashActivity.start(MainActivity.this, bundle);

                EventManager.getInstance().requestWeightApi(MainActivity.this, "2c28450ebe993dd61cb4d76eff7a916d", token, new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        if (MainActivity.this == null || MainActivity.this.isFinishing())
                            return false;
                        Map<String, Object> map = (Map<String, Object>) msg.obj;
                        if (map == null) {
                            Toast.makeText(MainActivity.this, "没有返回结果", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        try {
                            Toast.makeText(MainActivity.this, "模型结果反馈： " + map.get("data").toString() + "尸长：" + map.get("deadlength").toString(), Toast.LENGTH_LONG).show();
                            tv_content.setText(map.get("data").toString() + ";尸长" + map.get("deadlength").toString());
                            byte[] bis = (byte[]) map.get("bitmap");
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
                            iv_bitmap.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "aaaaa", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
            }
        });
        et_appid.setText("2c28450ebe993dd61cb4d76eff7a916d");
        et_token.setText(token);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventManager.getInstance().removeEvent(this);
    }
}
