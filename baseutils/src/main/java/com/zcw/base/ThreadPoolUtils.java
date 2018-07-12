package com.zcw.base;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by 朱城委 on 2018/4/18.<br><br>
 * 线程池工具类
 */

public class ThreadPoolUtils {

    /** 延时时间（ms） */
    public static final int DELAY_MILLISECONDS = 5000;

    /**
     * 返回{@link ScheduledExecutorService}线程池对象。
     * @return
     */
    public static ScheduledExecutorService getScheduledExecutor() {
        return ScheduledInstance.Instance;
    }

    private static class ScheduledInstance {
        private static ScheduledExecutorService Instance = new ScheduledThreadPoolExecutor(128);
    }
}
