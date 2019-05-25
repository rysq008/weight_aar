package com.innovationai.piginsurance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.innovationai.pigweight.Constant;
import com.innovationai.pigweight.event.EventManager;
import com.innovationai.pigweight.event.OnEventListener;
import com.xiangchuangtec.luolu.animalcounter.R;
import com.innovationai.pigweight.activitys.SplashActivity;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView tv_action_weight, tv_content;
    private ImageView iv_bitmap;
    private EditText et_appid, et_token;
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
        tv_content = findViewById(R.id.tv_content);
        iv_bitmap = findViewById(R.id.iv_bitmap);
        tv_action_weight = findViewById(R.id.tv_action_weight);
        tv_action_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.ACTION_APPID, et_appid.getText().toString().trim());
                bundle.putString(Constant.ACTION_TOKEN, et_token.getText().toString().trim());
                SplashActivity.start(MainActivity.this, bundle);
            }
        });
        et_appid.setText(mAppIdTest);
        et_token.setText(token);
//        //注册接收广播
//        mLocalBroadcast = new LocalBroadcast();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(WeightPicCollectActivity.ACTION_RECOGNITION);
//        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcast, filter);
        EventManager.getInstance().addEvent(mOnEventListener = new OnWeightListener());
    }

    public class OnWeightListener implements OnEventListener {
        @Override
        public void onReceive(Map<String, Object> map) {
            if (map == null) return;
            Toast.makeText(MainActivity.this, "模型结果反馈： " + map.get("data").toString(), Toast.LENGTH_LONG).show();
            tv_content.setText(map.get("data").toString());
            byte[] bis = (byte[]) map.get("bitmap");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
            iv_bitmap.setImageBitmap(bitmap);
        }
    }

    /**
     * 接收广播
     */
//    private class LocalBroadcast extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(WeightPicCollectActivity.ACTION_RECOGNITION) && intent.getSerializableExtra("data") != null) {
//                Toast.makeText(MainActivity.this, "模型结果反馈： " + intent.getSerializableExtra("data").toString(), Toast.LENGTH_LONG).show();
//                tv_content.setText(intent.getSerializableExtra("data").toString());
//                byte[] bis = intent.getByteArrayExtra("bitmap");
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
//                iv_bitmap.setImageBitmap(bitmap);
//            }
//        }
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOnEventListener != null) {
            EventManager.getInstance().removeEvent(mOnEventListener);
        }
    }
}
