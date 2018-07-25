package com.zcw.base.daemon;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zcw.base.LogUtil;

/**
 * Created by 朱城委 on 2018/4/23.<br><br>
 * 用内部Service类实现守护线程，这里利用了Android的漏洞提高当前进程的优先级。
 */

public class DaemonService extends Service {
    private static final String TAG = DaemonService.class.getSimpleName();

    /** 发送唤醒广播请求码 */
    private final static int WAKE_REQUEST_CODE = 5555;

    /** 守护进程service ID */
    private static final int DAEMON_SERVICE_ID = -5555;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG, "onStartCommand");

        // 利用Android系统漏洞提高进程优先级
        startForeground(DAEMON_SERVICE_ID, new Notification());
        if(Build.VERSION.SDK_INT >= 18) {
            Intent innerIntent = new Intent(this, DaemonInnerService.class);
            startService(innerIntent);
        }

        // 发送定时广播
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(DaemonManager.DAEMON_WAKE_ACTION);
        alarmIntent.putExtra("message", "DaemonService");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, WAKE_REQUEST_CODE, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), DaemonManager.WAKE_INTERVAL, pendingIntent);

        /**
         * 这里返回值是使用系统 Service 的机制自动重新启动，不过这种方式以下两种方式不适用：
         * 1.Service 第一次被异常杀死后会在5秒内重启，第二次被杀死会在10秒内重启，第三次会在20秒内重启，一旦在短时间内 Service 被杀死达到5次，则系统不再拉起。
         * 2.进程被取得 Root 权限的管理工具或系统工具通过 force Stop 停止掉，无法重启。
         */
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("onBind 未实现");
    }

    /**
     * 实现一个内部的 Service，实现让后台服务的优先级提高到前台服务，这里利用了 android 系统的漏洞，
     * 不保证所有系统可用，测试在7.1.1 之前大部分系统都是可以的，不排除个别厂商优化限制
     */
    public static class DaemonInnerService extends Service {
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            LogUtil.d(TAG, "Inner onStartCommand");
            startForeground(DAEMON_SERVICE_ID, new Notification());
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            throw new UnsupportedOperationException("onBind 未实现");
        }
    }
}
