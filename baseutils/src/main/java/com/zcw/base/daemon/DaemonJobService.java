package com.zcw.base.daemon;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.zcw.base.LogUtil;

/**
 * Created by 朱城委 on 2018/4/24.<br><br>
 * 5.x 以上使用 JobService 实现守护进程。
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class DaemonJobService extends JobService {
    private static final String TAG = DaemonJobService.class.getSimpleName();

    /** 发送唤醒广播请求码 */
    private final static int WAKE_REQUEST_CODE = 5655;

    @Override
    public boolean onStartJob(JobParameters params) {
        LogUtil.d(TAG, "onStartJob");

        // 利用定时器发送PendingIntent
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(DaemonManager.DAEMON_WAKE_ACTION);
        alarmIntent.putExtra("message", "DaemonJobService");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, WAKE_REQUEST_CODE, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), DaemonManager.WAKE_INTERVAL, pendingIntent);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        LogUtil.d(TAG, "onStopJob");
        return false;
    }
}
