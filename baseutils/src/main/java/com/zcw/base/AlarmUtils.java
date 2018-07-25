package com.zcw.base;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by 朱城委 on 2018/5/15.<br><br>
 * 定时器工具类<br><br>
 *
 * 调用{@link #startAlarm(Context, long, Class)}或者{@link #startAlarm(Context, long, long, Class)}可开始定时器。<br><br>
 * 调用{@link #stopAlarm(Context, Class)}可停止定时器。<br><br>
 * {@link #startAlarm(Context, long, Class)}、{@link #startAlarm(Context, long, long, Class)}、{@link #stopAlarm(Context, Class)}中的
 * {@link Class}参数，均为接收定时任务的{@link android.content.BroadcastReceiver}类，广播的action为{@link AlarmUtils#ACTION_ALARM_BASE}。<br><br>
 *
 * <b>注意：此定时器只会在第一次发送指定广播，后续如需隔一段时间发送一次广播，需要在接收到广播之后，再次发送，代码如下所示：<b/>
 * <blockquote><pre>
 *     public class AlarmTestReceiver extends BroadcastReceiver {
 *          private static final String TAG = AlarmTestReceiver.class.getSimpleName();
 *
 *          public void onReceive(Context context, Intent intent) {
 *              long intervalMillis = intent.getLongExtra(AlarmUtils.INTERVAL_MILLIS, 0);
 *              long triggerAtMillis = System.currentTimeMillis() + intervalMillis;
 *
 *              if(Build.VERSION.SDK_INT < 19) {
 *                  return ;
 *              }
 *
 *              // 再次发送广播，以达到定时的效果
 *              AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
 *              Intent intentTemp = new Intent(context, AlarmTestReceiver.class);
 *              intentTemp.setAction(AlarmUtils.ACTION_ALARM_BASE);
 *              intentTemp.putExtra(AlarmUtils.INTERVAL_MILLIS, intervalMillis);
 *              PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AlarmUtils.REQUEST_CODE_ALARM_BASE,
 *                  intentTemp, PendingIntent.FLAG_UPDATE_CURRENT);
 *              if(Build.VERSION.SDK_INT >= 23) {
 *                  manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
 *              }
 *              else {
 *                  manager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
 *              }
 *          }
 *      }
 * </pre></blockquote>
 */
public class AlarmUtils {
    /** 定时器所发广播Action */
    public static final String ACTION_ALARM_BASE = "com.zcw.base.alarm";

    /** 间隔时间标示常量 */
    public static final String INTERVAL_MILLIS = "intervalMillis";

    /** PendingIntent requestCode */
    public static final int REQUEST_CODE_ALARM_BASE = 0;

    /**
     * 开始定时器
     * @param context
     * @param intervalMillis 定时器执行间隔时间（ms）
     * @param cls 接收定时任务的{@link android.content.BroadcastReceiver}类。
     */
    public static void startAlarm(Context context, long intervalMillis, Class<?> cls) {
        startAlarm(context, System.currentTimeMillis(), intervalMillis, cls);
    }

    /**
     * 开始定时器
     * @param context
     * @param triggerAtMillis 定时器第一次执行时间
     * @param intervalMillis 定时器间隔时间（ms）
     * @param cls 接收定时任务的{@link android.content.BroadcastReceiver}类。
     */
    public static void startAlarm(Context context, long triggerAtMillis, long intervalMillis, Class<?> cls) {
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, cls);
        intent.setAction(ACTION_ALARM_BASE);
        intent.putExtra(INTERVAL_MILLIS, intervalMillis);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_ALARM_BASE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(Build.VERSION.SDK_INT >= 19) {
            if(Build.VERSION.SDK_INT >= 23) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
            else {
                manager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
        }
        else {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, pendingIntent);
        }
    }

    /**
     * 停止定时器
     * @param context
     * @param cls 接收定时任务的{@link android.content.BroadcastReceiver}类。
     */
    public static void stopAlarm(Context context, Class<?> cls) {
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(ACTION_ALARM_BASE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_ALARM_BASE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
    }
}
