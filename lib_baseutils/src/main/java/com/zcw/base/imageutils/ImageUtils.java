package com.zcw.base.imageutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;

import com.zcw.base.CommonUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片工具类
 * Created by 朱城委 on 2017/10/17.<br><br>
 */

public class ImageUtils {
    private static final String TAG = "ImageUtils";

    /**
     * 把图片转换为Base64字符串
     * @param path 图片路径
     * @return
     */
    public static String imageToString(String path) {
        File file = new File(path);
        return imageToString(file);
    }

    /**
     * 把图片转换为Base64字符串
     * @param file 要转换的图片文件
     * @return
     */
    public static String imageToString(File file) {
        InputStream in;
        byte[] bytes;

        try {
            in = new FileInputStream(file);
            bytes = new byte[in.available()];
            in.read(bytes);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    /**
     * 把图片转换为Base64字符串
     * @param bitmap
     * @return
     */
    public static String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            baos.flush();
            baos.close();
            byte[] data = baos.toByteArray();
            return Base64.encodeToString(data, Base64.NO_WRAP);
        }
        catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        finally {
            if(baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Base64字符串转bitmap
     * @param data
     * @return
     */
    public static Bitmap stringToImage(String data) {
        Bitmap bitmap = null;
        try {
            byte[] bytes = Base64.decode(data, Base64.NO_WRAP);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return  bitmap;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * 保存Bitmap为指定文件。<br><br>
     * <strong>Note: 保存文件后，bitmap会调用recycle()，所以之后不能再使用bitmap。</strong>
     * @param bitmap
     * @param path 保存文件的完整路径
     * @return 保存成功，返回true，否则返回false。
     */
    public static boolean storeImage(Bitmap bitmap, String path) {
        try {
            // 处理给出的文件路径不存在的情况
            String folder = path.substring(0, path.lastIndexOf("/"));
            CommonUtils.folderExist(folder);

            FileOutputStream outputStream = new FileOutputStream(path);
            Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
            if(path.contains("png")) {
                compressFormat = Bitmap.CompressFormat.PNG;
            }
            bitmap.compress(compressFormat, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 从图片路径中获取图片名
     * @param path
     * @return
     */
    public static String getImageName(String path) {
        String name;
        try {
            name = path.substring(path.lastIndexOf("/") + 1, path.length());
        }
        catch (IndexOutOfBoundsException e) {
            name = CommonUtils.dateTime(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".jpg";
            return name;
        }
        return name;
    }

    /**
     * 获取图片的旋转角度
     * @param path
     * @return
     */
    public static int getImageDegree(String path) {
        int degree = 0;

        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return degree;
        }
        return degree;
    }

    /**
     * 旋转图片
     * @param degree 选择的角度
     * @param path 图片路径
     * @return
     */
    public static Bitmap rotateImage(int degree, String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return rotateImage(degree, bitmap);
    }

    /**
     * 旋转图片
     * @param degree 选择的角度
     * @param bitmap
     * @return
     */
    public static Bitmap rotateImage(int degree, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if(result == null) {
            result = bitmap;
        }

        if(result != bitmap) {
            bitmap.recycle();
        }
        return result;
    }
}
