package com.zcw.baseutildemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zcw.base.LogUtil;

/**
 * Created by 朱城委 on 2018/7/17.<br><br>
 */
public class DaemonTestReceiver extends BroadcastReceiver {
    private static final String TAG = DaemonTestReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        LogUtil.e(TAG, "接收到守护进程" + message);
        LogUtil.d(TAG, "接收到守护进程" + message);
    }
}
