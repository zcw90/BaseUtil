package com.zcw.base;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.util.List;

/**
 * Created by 朱城委 on 2017/10/27.<br><br>
 * app信息工具类<br>
 * 可获取app的名称、版本号、VersionCode等信息。
 */

public class AppInfoUtils {
    private static final String TAG = AppInfoUtils.class.getSimpleName();

    private static final String UNKNOWN = "Unknown";
    private static final int ERROR = -1;

    private AppInfoUtils() {
        throw new UnsupportedOperationException("AppInfoUtils can't be instantiated!");
    }

    /**
     * 获取App名称
     *
     * @param context
     * @return 如果获取失败，返回“Unknown”。
     */
    public static String getAppName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } else {
            return UNKNOWN;
        }
    }

    /**
     * 获取应用包名
     *
     * @param context
     * @return 如果获取失败，返回“Unknown”。
     */
    public static String getAppPackage(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.packageName;
        } else {
            return UNKNOWN;
        }
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return 如果获取失败，返回“Unknown”。
     */
    public static String getAppVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionName;
        } else {
            return UNKNOWN;
        }
    }

    /**
     * 获取应用VersionCode
     *
     * @param context
     * @return 如果获取失败，返回-1。
     */
    public static int getAppVersionCodeInt(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionCode;
        } else {
            return ERROR;
        }
    }

    /**
     * 获取应用VersionCode
     *
     * @param context
     * @return 如果获取失败，返回“Unknown”。
     */
    public static String getAppVersionCodeString(Context context) {
        int versionCode = getAppVersionCodeInt(context);
        if(versionCode == ERROR) {
            return UNKNOWN;
        }
        return String.valueOf(versionCode);
    }

    /**
     * 获取AndroidManifest.xml文件中Application标签下的meta-data值。
     * @param context
     * @param key
     * @return 获取失败，返回“”。
     */
    public static String getMetaDataApplicationInt(Context context, String key) {
        String result = "";

        ApplicationInfo info;
        try {
            info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if(info != null && info.metaData != null) {
                result = String.valueOf(info.metaData.getInt(key));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return result;
        }

        return result;
    }

    /**
     * 获取应用签名文件的MD5值
     * @param context
     * @return 如果获取失败，返回“”。
     */
    public static String getSignatureMD5(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            return CommonUtils.MD5(sign.toByteArray());
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
            return "";
        }
    }

    /**
     * 获取进程名字
     * @param context
     * @param pid 要获取的进程id
     * @return 返回进程名，如果失败，返回{@link #UNKNOWN}。
     */
    public static String gerProcessName(Context context, int pid) {
        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);

        List<RunningAppProcessInfo> runningApps = manager.getRunningAppProcesses();
        if(runningApps == null) {
            return UNKNOWN;
        }

        for(RunningAppProcessInfo info : runningApps) {
            if(info.pid == pid) {
                return info.processName;
            }
        }

        return UNKNOWN;
    }

    private static PackageInfo getPackageInfo(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            return manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
