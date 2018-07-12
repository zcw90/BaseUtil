package com.zcw.base.imageutils;

import android.os.Environment;

import com.zcw.base.CommonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 朱城委 on 2017/10/16.<br><br>
 * 压缩图片路径管理类
 */

public class ImagePath {
    private static final String ImageFolder = "CsairImgCompress";
    private static final String ImageFolderAbsolutePath =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + ImageFolder;

    /**
     * 获取存放压缩图片的文件夹
     * @return
     */
    public static File getImageCompressFolder() {
        File file = new File(ImageFolderAbsolutePath);
        if(!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 删除所有压缩后的图片以及目录
     * @return
     */
    public static boolean deleteAllFile() {
        File file = new File(ImageFolderAbsolutePath);
        return CommonUtils.deleteDir(file);
    }

    /**
     * 获取文件夹下所有文件
     * @return
     */
    public static List<String> getAllFile() {
        List<String> files = new ArrayList<>();
        File file = new File(ImageFolderAbsolutePath);
        if(!file.exists()) {
            return files;
        }

        String[] fileNames = file.list();
        for(String string : fileNames) {
            files.add(ImageFolderAbsolutePath + "/" + string);
        }
        return files;
    }

    /**
     * 获取存储图片文件夹的绝对路径
     * @return
     */
    public static String getImageFolderAbsolutePath() {
        return ImageFolderAbsolutePath;
    }
}
