package com.zcw.base;

/**
 * Created by 朱城委 on 2018/4/3.<br><br>
 * 点击工具类
 */

public class ClickUtils {
    /** 两次点击间隔时间(单位：ms) */
    private static final long MIN_DELAY_TIME = 500;

    /** 保存上一次点击时间 */
    private static long lastClickTime;

    /** 连续点击次数 */
    private static int continueClickCount;

    /**
     * 是否快速点击
     * @return 如果两次点击间隔时间,小于500ms，返回true；否则返回false。
     */
    public static boolean isFastClick() {
        boolean flag = true;
        long currentTime = System.currentTimeMillis();

        if((currentTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentTime;

        return flag;
    }

    /**
     * 连续点击{@code count}次，每次点击时间间隔不超过500ms。
     * @param count 连续点击的次数
     * @return 返回剩余的点击次数
     */
    public static int continueClick(int count) {
        // 连续点击有效间隔
        long interval = 500;
        long currentTime = System.currentTimeMillis();
        if((currentTime - lastClickTime) < interval) {
            continueClickCount++;
        }
        else {
            continueClickCount = 1;
        }

        lastClickTime = currentTime;
        if(continueClickCount == count) {
            continueClickCount = 0;
            return continueClickCount;
        }

        return count - continueClickCount;
    }
}
