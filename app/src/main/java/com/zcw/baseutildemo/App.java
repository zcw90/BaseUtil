package com.zcw.baseutildemo;

import android.app.Application;

import com.zcw.base.LogUtil;

/**
 * Created by 朱城委 on 2018/7/12.<br><br>
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化Log工具类
        LogUtil.syncIsDebug(this);
    }
}
