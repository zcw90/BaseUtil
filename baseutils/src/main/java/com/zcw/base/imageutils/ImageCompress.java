package com.zcw.base.imageutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.zcw.base.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 朱城委 on 2017/10/13.<br><br>
 * 图片压缩类
 */

public class ImageCompress {
    private static final String TAG = "ImageCompress";

    /**
     * 质量压缩图片
     * @param path 要压缩的图片
     * @param size 要压缩到多少(kb)以内
     * @return
     */
    public static boolean imageCompress(String path, int size) {
        String imgName = ImageUtils.getImageName(path);
        File file = new File(ImagePath.getImageCompressFolder(), imgName);      // 把文件保存到指定目录

        // 压缩图片，直到图片符合大小
        int quality = 100;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeFile(path);

        // 对png图片和非png图片分别处理
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
        if(path.contains("png")) {
            compressFormat = Bitmap.CompressFormat.PNG;
        }
        bitmap.compress(compressFormat, quality, baos);
        while ((baos.toByteArray().length / 1024) > size && quality >= 0) {
            quality -= 10;
            baos.reset();
            bitmap.compress(compressFormat, quality, baos);
        }
        bitmap.recycle();

        // 把压缩后的图片保存在指定的目录
        try {
            FileOutputStream fileOS = new FileOutputStream(file);
            fileOS.write(baos.toByteArray());
            fileOS.flush();
            fileOS.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 采样率压缩图片
     * @param path 图片路径
     * @param simple 采样率，有效值为小于simple的最大的2的整数次幂；比如，10有效值为8,17有效值为16。
     * @return
     */
    public static Bitmap imageCompressSimple(String path, int simple) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = simple;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 尺寸压缩图片
     * @param path 图片路径
     * @param size 压缩的尺寸，比如size = 2，代表图片的宽高都为原来的一半。
     * @return
     */
    public static Bitmap imageCompressSize(String path, int size) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        LogUtil.d(TAG, "imageCompressSize Compress before:" + bitmap.getByteCount() / 1024 + "kb");
        return imageCompressSize(bitmap, size);
    }

    /**
     * 尺寸压缩图片
     * @param bitmap
     * @param size 压缩的尺寸，比如size = 2，代表图片的宽高都为原来的一半。
     * @return
     */
    public static Bitmap imageCompressSize(Bitmap bitmap, int size) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth() / size, bitmap.getHeight() / size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bitmap.getWidth() / size, bitmap.getHeight() / size);
        canvas.drawBitmap(bitmap, null, rect, null);

        bitmap.recycle();
        LogUtil.d(TAG, "imageCompressSize Compress after:" + result.getByteCount() / 1024 + "kb");
        return result;
    }

    /**
     * 矩阵压缩
     * @param path
     * @param scale 压缩比例（0——1的小数）
     * @return
     */
    public static Bitmap imageCompressMatrix(String path, float scale) {
        LogUtil.d(TAG, "imageCompressMatrix degree:" + ImageUtils.getImageDegree(path));

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        LogUtil.d(TAG, "imageCompressMatrix Compress before:" + bitmap.getByteCount() / 1024 + "kb");
        return imageCompressMatrix(bitmap, scale);
    }

    /**
     * 矩阵压缩
     * @param bitmap
     * @param scale 压缩比例（0——1的小数）
     * @return
     */
    public static Bitmap imageCompressMatrix(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);

        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        LogUtil.d(TAG, "imageCompressMatrix Compress after:" + result.getByteCount() / 1024 + "kb");
        return result;
    }
}
