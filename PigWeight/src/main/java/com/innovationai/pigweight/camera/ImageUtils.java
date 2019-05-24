package com.innovationai.pigweight.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

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
     * 缩放图片为 16：9 不足补边
     *
     * @param bitmap
     * @return
     */
    public static Bitmap ratioScaleBitmap(Bitmap bitmap) {
        float ratio = 16 / 9.0f;
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