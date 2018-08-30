package com.zcw.base;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * Created by 朱城委 on 2017/11/29.<br><br>
 * 文件操作类
 */
public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();

    public static final String MIME_MapTable[][] = {
            // {后缀名，MIME类型}
            { ".3gp", "video/3gpp" },
            { ".apk", "application/vnd.android.package-archive" },
            { ".asf", "video/x-ms-asf" },
            { ".avi", "video/x-msvideo" },
            { ".bin", "application/octet-stream" },
            { ".bmp", "image/bmp" },
            { ".c", "text/plain" },
            { ".class", "application/octet-stream" },
            { ".conf", "text/plain" },
            { ".cpp", "text/plain" },
            { ".doc", "application/msword" },
            { ".docx",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
            { ".xls", "application/vnd.ms-excel" },
            { ".xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
            { ".exe", "application/octet-stream" },
            { ".gif", "image/gif" },
            { ".gtar", "application/x-gtar" },
            { ".gz", "application/x-gzip" },
            { ".h", "text/plain" },
            { ".htm", "text/html" },
            { ".html", "text/html" },
            { ".jar", "application/java-archive" },
            { ".java", "text/plain" },
            { ".jpeg", "image/jpeg" },
            { ".jpg", "image/jpeg" },
            { ".js", "application/x-javascript" },
            { ".log", "text/plain" },
            { ".m3u", "audio/x-mpegurl" },
            { ".m4a", "audio/mp4a-latm" },
            { ".m4b", "audio/mp4a-latm" },
            { ".m4p", "audio/mp4a-latm" },
            { ".m4u", "video/vnd.mpegurl" },
            { ".m4v", "video/x-m4v" },
            { ".mov", "video/quicktime" },
            { ".mp2", "audio/x-mpeg" },
            { ".mp3", "audio/x-mpeg" },
            { ".mp4", "video/mp4" },
            { ".mpc", "application/vnd.mpohun.certificate" },
            { ".mpe", "video/mpeg" },
            { ".mpeg", "video/mpeg" },
            { ".mpg", "video/mpeg" },
            { ".mpg4", "video/mp4" },
            { ".mpga", "audio/mpeg" },
            { ".msg", "application/vnd.ms-outlook" },
            { ".ogg", "audio/ogg" },
            { ".pdf", "application/pdf" },
            { ".png", "image/png" },
            { ".pps", "application/vnd.ms-powerpoint" },
            { ".ppt", "application/vnd.ms-powerpoint" },
            { ".pptx",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation" },
            { ".prop", "text/plain" }, { ".rc", "text/plain" },
            { ".rmvb", "audio/x-pn-realaudio" }, { ".rtf", "application/rtf" },
            { ".sh", "text/plain" }, { ".tar", "application/x-tar" },
            { ".tgz", "application/x-compressed" }, { ".txt", "text/plain" },
            { ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" },
            { ".wmv", "audio/x-ms-wmv" },
            { ".wps", "application/vnd.ms-works" }, { ".xml", "text/plain" },
            { ".z", "application/x-compress" },
            { ".zip", "application/x-zip-compressed" }, { "", "*/*" } };

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件完整路径
     * @return 如果存在，返回true；不存在，返回false。
     */
    public static boolean fileExist(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断文件夹是否存在，如果不存在，则创建
     *
     * @param folder
     * @return
     */
    public static boolean folderExist(String folder) {
        File file = new File(folder);
        if (!file.exists()) {
            file.mkdirs();
        }

        return true;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();    // 目录此时为空，可以删除
    }

    /**
     * 获取目录下所有文件名
     * @param fileDir
     * @return 如果目录为空或者目录不存在，返回空字符串数组；<br>
     *     如果{@code fileDir}为文件名，返回只含文件名的字符串数组。
     */
    public static String[] getDirFiles(String fileDir) {
        File dir = new File(fileDir);

        if(!dir.exists()) {
            return new String[] {};
        }
        else {
            if(dir.isFile()) {
                return new String[] { dir.getName() };
            }
            else {
                return dir.list();
            }
        }
    }

    /**
     * 获取文件夹下符合要求的文件
     *
     * @param fileDir
     * @param filter  文件过滤器
     * @return 如果文件夹为空，返回{}，否则返回符合要求的文件名。
     */
    public static String[] getDirFiles(String fileDir, FilenameFilter filter) {
        File dir = new File(fileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.list(filter);
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    public static String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();

        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }

        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase(Locale.CHINA);
        if(end.equals("")) {
            return type;
        }
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    /**
     * 把文件内容转换为字符串
     * @param filePath 文件路径
     * @return
     */
    public static String getFileString(String filePath) {
        String result = "";
        try {
            InputStream in = new FileInputStream(new File(filePath));

            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            result = new String(buffer, "UTF-8");

            if(in != null) {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, "打开文件失败");
            return result;
        }
        return result;
    }

    /**
     * 关闭流
     * @param closeable
     */
    public static void closeStream(Closeable closeable) {
        try {
            if(closeable != null) {
                closeable.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "关闭流失败");
        }
    }

    /**
     * 清理目录过期的文件
     * @param dir 要清理的目录
     * @param millis 过期时间(ms)
     */
    public static void clearDir(final String dir, final long millis) {
        if(dir == null) {
            LogUtil.e(TAG, "目录为空，清理失败");
            return ;
        }

        // 在新线程中删除过期的文件
        ThreadPoolUtils.getScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String[] fileList = getDirFiles(dir);
                for(int i = 0; i < fileList.length; i++) {
                    File file;
                    if(fileList.length == 1) {
                        file = new File(dir);   // 如果dir是文件名，则直接生成文件。
                    }
                    else {
                        file = new File(dir + fileList[i]);
                    }

                    long lastModified = file.lastModified();
                    if((System.currentTimeMillis() - lastModified) > millis) {
                        deleteDir(file);
                    }
                }
            }
        });
    }
}
