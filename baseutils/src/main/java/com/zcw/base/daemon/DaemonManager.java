package com.zcw.base.daemon;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by 朱城委 on 2018/4/24.<br><br>
 * 守护进程管理类
 */

public class DaemonManager {

    /** 守护进程所发送唤醒广播action */
    public static final String DAEMON_WAKE_ACTION = "com.zcw.base.daemon.wake";

    /** 唤醒的间隔时间(单位：ms) */
    public static long WAKE_INTERVAL = 2 * 1000;

    /**
     * 开启守护进程
     * @param context
     * @param intervalMillis 守护进程间隔时间（ms）
     */
    public static void startDaemonService(Context context, long intervalMillis) {
        WAKE_INTERVAL = intervalMillis;
        Context c = context.getApplicationContext();
        Intent intent = new Intent(c, DaemonService.class);
        c.startService(intent);
    }

    /**
     * 5.x 以上使用 JobService 实现守护进程
     * @param context
     * @param intervalMillis 守护进程间隔时间（ms）
     */
    public static void startDaemonJobService(Context context, long intervalMillis) {
        WAKE_INTERVAL = intervalMillis;
        Context c = context.getApplicationContext();

        if(Build.VERSION.SDK_INT >= 21) {
            JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(c, DaemonJobService.class));
            builder.setPeriodic(WAKE_INTERVAL);
            builder.setPersisted(true);

            JobScheduler jobScheduler = (JobScheduler)c.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(builder.build());
        }
    }
}
