package com.zcw.baseutildemo.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.zcw.base.AlarmUtils;
import com.zcw.base.LogUtil;

/**
 * Created by 朱城委 on 2018/7/17.<br><br>
 */
public class AlarmTestReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmTestReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.e(TAG, "接收到定时器消息， action = " + intent.getAction());

        long intervalMillis = intent.getLongExtra(AlarmUtils.INTERVAL_MILLIS, 0);
        LogUtil.e(TAG, AlarmUtils.INTERVAL_MILLIS + "-" + intervalMillis);
        long triggerAtMillis = System.currentTimeMillis() + intervalMillis;

        if(Build.VERSION.SDK_INT < 19) {
            return ;
        }

        // 再次发送广播，以达到定时的效果
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intentTemp = new Intent(context, AlarmTestReceiver.class);
        intentTemp.setAction(AlarmUtils.ACTION_ALARM_BASE);
        intentTemp.putExtra(AlarmUtils.INTERVAL_MILLIS, intervalMillis);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AlarmUtils.REQUEST_CODE_ALARM_BASE,
                intentTemp, PendingIntent.FLAG_UPDATE_CURRENT);
        if(Build.VERSION.SDK_INT >= 23) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
        else {
            manager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
    }
}
