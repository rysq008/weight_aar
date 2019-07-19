package com.innovationai.pigweight.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.innovationai.pigweight.R;
import com.innovationai.pigweight.utils.UIUtils;


/**
 * Created by Hades on 16/10/9.
 */
public class WeightSpiritView extends View {
    //定义水平仪仪表盘图片
    Bitmap back;
    //定义水平仪中气泡图标
    Bitmap bubble;
    //定义水平仪中气泡的X、Y坐标
    public int bubbleX, bubbleY;
    private Paint mPaint;

    public WeightSpiritView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取窗口管理器
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //获取屏幕的高度和宽度
//        Display display = wm.getDefaultDisplay();
//        DisplayMetrics metrics = new DisplayMetrics();
//        display.getMetrics(metrics);
        int screenWidth = UIUtils.dp2px(context, 120.0f);
        //int screenHeight = metrics.heightPixels;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.WHITE);
        //创建位图
        back = Bitmap.createBitmap(screenWidth, screenWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(back);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //设置绘制风格：仅填充
        paint.setStyle(Paint.Style.FILL);
        //创建一个线性渐变来绘制线性渐变
        Shader shader = new LinearGradient(0, screenWidth, screenWidth * 0.8f, screenWidth * 0.2f,
                Color.TRANSPARENT, Color.TRANSPARENT, Shader.TileMode.MIRROR);
        paint.setShader(shader);
        //绘制圆形
        canvas.drawCircle(screenWidth / 2, screenWidth / 2, screenWidth / 2, paint);
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        //设置绘制风格：仅绘制边框
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(5);
        paint2.setColor(Color.WHITE);
        //绘制圆形边框
        canvas.drawCircle(screenWidth / 2, screenWidth / 2, screenWidth / 2, paint2);
//        canvas.drawCircle(screenWidth / 4, screenWidth / 2, screenWidth / 2, paint2);
        //绘制水平横线
        canvas.drawLine(0, screenWidth / 2, screenWidth, screenWidth / 2, paint2);
        //绘制垂直横线
        canvas.drawLine(screenWidth / 2, 0, screenWidth / 2, screenWidth, paint2);
        //设置画笔宽度
        paint2.setStrokeWidth(10);
        paint2.setColor(Color.RED);
        //绘制中心的红色“十字”
        canvas.drawLine(screenWidth / 2 - 30, screenWidth / 2, screenWidth / 2 + 30,
                screenWidth / 2, paint2);
        canvas.drawLine(screenWidth / 2, screenWidth / 2 - 30, screenWidth / 2,
                screenWidth / 2 + 30, paint2);
        //加载气泡图片
        bubble = BitmapFactory.decodeResource(getResources(), R.mipmap.iv_take_picture_press);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制水平仪表盘
        canvas.drawBitmap(back, 0, 0, null);

        //根据气泡坐标绘制气泡
        canvas.drawBitmap(bubble, bubbleX, bubbleY, mPaint);
    }

    public void setColor(int color ){
       mPaint.setAlpha(color);
    }

    public Bitmap getBack() {
        return back;
    }

    public void setBack(Bitmap back) {
        this.back = back;
    }

    public Bitmap getBubble() {
        return bubble;
    }

    public void setBubble(Bitmap bubble) {
        this.bubble = bubble;
    }
}
