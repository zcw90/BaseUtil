package com.zcw.base;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by 朱城委 on 2017/6/16.<br><br>
 *
 * <b>此Log工具需要在Application中调用{@link #syncIsDebug(Context)}方法，以区分是否为debug模式。</b><br><br>
 *
 * Log函数有2个重载方法,x(string)和x(string, string)。<br>
 * 一个参数的方法，可定位并跳转到log所在的代码行位置;<br>
 * 二个参数的方法，只会在debug模式显示log，release模式会自动屏蔽。<br><br>
 *
 * xAndSave(string, string)方法，会根据{@code savLog}判断，是否保存log。<br><br>
 * 保存日志的路径可以通过{@link #setStorePath(String)}或者{@link #syncIsDebug(Context, String, long)}设置。
 */
public class LogUtil {
    private static final String TAG = LogUtil.class.getSimpleName();

    private static String className;    //类名
    private static String methodName;   //方法名
    private static int lineNumber;      //行数

    private static Boolean isDebug = null;

    /** 是否保存日志标示 */
    private static boolean isSaveLog = true;

    /**
     * log文件保存路径，要保存log文件，先要调用{@link #setStorePath(String)}设置log保存地址。
     */
    private static String PATH = null;

    /**
     * 初始化设置，用于判断是否为debug模式。
     * @param context
     */
    public static void syncIsDebug(Context context) {
        syncIsDebug(context, null, 7 * 1000 * 60 * 60 *24);
    }

    /**
     * 初始化设置，用于判断是否为debug模式。
     * @param context
     * @param logSavePath log保存的路径，如果为null，则路径默认为/storage/emulated/0/Android/data/包名/files/CrashLog/。
     * @param logOutOfDate log过期时间(ms)，在这个时间之前的日志会被清理掉。
     */
    public static void syncIsDebug(Context context, String logSavePath, long logOutOfDate) {
        if(isDebug == null) {
            isDebug = AppInfoUtils.isDebugApplication(context);
        }

        if(logSavePath == null) {
            logSavePath = context.getExternalFilesDir("").getPath() + "/CrashLog/";
        }
        PATH = logSavePath;

        // 清理过期的日志
        FileUtils.clearDir(PATH, logOutOfDate);
    }

    /**
     * 设置是否保存日志。如果不设置，默认为true。
     * @param save
     */
    public static void setIsSaveLog(boolean save) {
        isSaveLog = save;
    }

    /**
     * 设置log保存地址
     * @param path
     */
    public static void setStorePath(String path) {
        PATH = path;
    }

    private LogUtil(){
    }

    private static boolean isDebuggable() {
        return isDebug == null ? false : isDebug;
    }

    private static String createLog( String log ) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        buffer.append(log);
        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] element){
        className = element[1].getFileName();
        methodName = element[1].getMethodName();
        lineNumber = element[1].getLineNumber();
    }

    public static void e(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));

        if(isSaveLog) {
            storeLog(className, message);
        }
    }

    public static void e(String tag, String message) {
        if(!isDebuggable())
            return ;

        eAndSave(tag, message);
    }

    public static void eAndSave(String tag, String message) {
        Log.e(tag, message);

        if(isSaveLog) {
            storeLog("E/" + tag, message);
        }
    }

    /**
     * 显示超过4000字符串的log信息
     * @param tag
     * @param message
     */
    public static void bigE(String tag, String message) {
        if(!isDebuggable())
            return ;

        if(message.length() > 4000) {
            for(int i = 0; i < message.length(); i += 4000) {
                if(i + 4000 < message.length()) {
                    Log.e(tag, message.substring(i, i + 4000));
                }
                else {
                    Log.e(tag, message.substring(i, message.length()));
                }
            }
        }
        else {
            Log.e(tag, message);
        }
    }

    public static void i(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));

        if(isSaveLog) {
            storeLog(className, message);
        }
    }

    public static void i(String tag, String message) {
        if(!isDebuggable())
            return ;

        iAndSave(tag, message);
    }

    public static void iAndSave(String tag, String message) {
        Log.i(tag, message);

        if(isSaveLog) {
            storeLog("I/" + tag, message);
        }
    }

    public static void d(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));

        if(isSaveLog) {
            storeLog(className, message);
        }
    }

    public static void d(String tag, String message) {
        if(!isDebuggable())
            return ;

        dAndSave(tag, message);
    }

    public static void dAndSave(String tag, String message) {
        Log.d(tag, message);

        if(isSaveLog) {
            storeLog("D/" + tag, message);
        }
    }

    public static void v(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));

        if(isSaveLog) {
            storeLog(className, message);
        }
    }

    public static void v(String tag, String message) {
        if(!isDebuggable())
            return ;

        vAndSave(tag, message);
    }

    public static void vAndSave(String tag, String message) {
        Log.v(tag, message);

        if(isSaveLog) {
            storeLog("V/" + tag, message);
        }
    }

    public static void w(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));

        if(isSaveLog) {
            storeLog(className, message);
        }
    }

    public static void w(String tag, String message) {
        if(!isDebuggable())
            return ;

        wAndSave(tag, message);
    }

    public static void wAndSave(String tag, String message) {
        Log.w(tag, message);

        if(isSaveLog) {
            storeLog("W/" + tag, message);
        }
    }

    public static void wtf(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(className, createLog(message));

        if(isSaveLog) {
            storeLog(className, message);
        }
    }

    public static void wtf(String tag, String message) {
        if(!isDebuggable())
            return ;

        wtfAndSave(tag, message);
    }

    public static void wtfAndSave(String tag, String message) {
        Log.wtf(tag, message);

        if(isSaveLog) {
            storeLog("WTF/" + tag, message);
        }
    }

    /**
     * 将日志信息保存至SD卡，要保存log文件，先要调用{@link #setStorePath(String)}或者{@link #syncIsDebug(Context, String, long)}设置log保存地址。
     * @param tag LOG TAG
     * @param msg 保存的打印信息
     */
    public static void storeLog(final String tag, final String msg) {
        if(PATH == null) {
            Log.e(TAG, "Log保存失败，请设置Log保存地址");
            return ;
        }

        File fileDir = new File(PATH);
        // 判断目录是否已经存在
        if (!fileDir.exists()) {
            if (!fileDir.mkdirs()) {
                Log.e(tag, "Failed to create directory " + PATH);
                return;
            }
        }

        ThreadPoolUtils.getScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String fileName = CommonUtils.dateTime(System.currentTimeMillis(), "yyyyMMdd") + ".log";
                File file = new File(PATH + fileName);
                FileOutputStream fileOut = null;
                PrintWriter printOut = null;

                try {
                    // 判断日志文件是否已经存在
                    if (!file.exists()) {
                        if (!file.createNewFile()) {
                            Log.e(tag, "Failed to create log file " + PATH);
                            return ;
                        }
                    }

                    fileOut = new FileOutputStream(file, true);
                    printOut = new PrintWriter(fileOut);
                    printOut.println(CommonUtils.dateTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss ") + " " + tag + ": " + msg + '\r');
                    printOut.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(tag, "保存日志文件失败");
                }
                finally {
                    FileUtils.closeStream(fileOut);
                    FileUtils.closeStream(printOut);
                }
            }
        });
    }
}
