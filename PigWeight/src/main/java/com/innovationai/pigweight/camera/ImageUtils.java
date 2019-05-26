package com.innovationai.pigweight.camera;

import android.graphics.*;

import java.io.ByteArrayOutputStream;

/**
 * @Author: Lucas.Cui
 * 时   间：2019/1/23
 * 简   述：<功能简述>
 */
public class ImageUtils {

    /**
     * 旋转图片
     *
     * @param bitmap
     * @param rotation
     * @Return
     */
    public static Bitmap getRotatedBitmap(Bitmap bitmap, int rotation) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, false);
    }

    /**
     * 镜像翻转图片
     *
     * @param bitmap
     * @Return
     */
    public static Bitmap getFlipBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1);
        matrix.postTranslate(bitmap.getWidth(), 0);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, false);
    }

    /**
     * 将图片转换成720*960格式并压缩至80%品质
     *
     * @param bitmap 待处理图片
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmap) {

        // 图片按比例压缩，以长边=1080为准
        // 图片质量提升为60.
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scale = 1f;

        int max = Math.max(width, height);
        scale = 960f / max;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            newbm.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            baos.flush();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inPreferredConfig = Bitmap.Config.;
        newbm = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length, options);

        return newbm;
    }

    /**
     * 根据需要的宽高缩放处理图片为指定宽高，按宽高缩放不足的边，做补边处理保证原bitmap内容正常显示
     *
     * @param bitmap
     *
     * @return
     */
    public static Bitmap createBitmapBySize(Bitmap bitmap, float newHeight, float newWidth){

        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        float x = 0, y = 0, scaleWidth = width, scaleHeight = height;

        float originRatio = height / width;
        float newRatio = newHeight / newWidth;

        Bitmap newBitmap, scaleBitmap;

        if (newRatio >= originRatio){
            y = (newHeight - (newWidth / width * height)) / 2;
            Matrix matrix = new Matrix();
            matrix.setScale(newWidth / width, newWidth / width);
            scaleBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int)width, (int)height, matrix, true);

        }else{
            x = (newWidth - (newHeight / height * width)) / 2;
            Matrix matrix = new Matrix();
            matrix.setScale(newHeight / height, newHeight / height);
            scaleBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int)width, (int)height, matrix, true);

        }

        // 背图
        newBitmap = Bitmap.createBitmap((int)newWidth, (int)newHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // 生成白色的
        paint.setColor(Color.WHITE);
        canvas.drawBitmap(scaleBitmap, x, y, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        // 画最终bitmap
        canvas.drawRect(0, 0, newWidth, newHeight, paint);
        return newBitmap;
    }

    /**
     * 缩放图片为 16：9 不足补边
     *
     * @param bitmap
     *
     * @return
     */
    public static Bitmap ratioScaleBitmapAddSide(Bitmap bitmap, float ratio){

        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        float x = 0, y = 0, scaleWidth = width, scaleHeight = height;
        Bitmap newBitmap;
        if (ratio >= height / width){
            y = (width * ratio - height) / 2;
            scaleHeight = width * ratio;
        }else{
            x = (height / ratio - width) / 2;
            scaleWidth = height / ratio;
        }

        // 背图
        newBitmap = Bitmap.createBitmap((int)scaleWidth, (int)scaleHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // 生成白色的
        paint.setColor(Color.WHITE);
        canvas.drawBitmap(bitmap, x, y, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        // 画bitmap
        canvas.drawRect(0, 0, (int)scaleWidth, (int)scaleHeight, paint);

        return newBitmap;
    }

    /**
     * 缩放图片为 16：9 不足裁切多出部分
     *
     * @param bitmap
     *
     * @return
     */
    public static Bitmap ratioScaleBitmap(Bitmap bitmap, float ratio){

        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        float x = 0, y = 0, scaleWidth = width, scaleHeight = height;
        Bitmap newbmp;
        if (height / width >= ratio) {
            y = (height - width * ratio) / 2;
            scaleHeight = width * ratio + y;
        } else {
            x = (width - height / ratio) / 2;
            scaleWidth = height / ratio + x;
        }

        try {
            newbmp = Bitmap.createBitmap(bitmap, (int) x, (int) y, (int) scaleWidth, (int) scaleHeight, null, false);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newbmp;
    }
}