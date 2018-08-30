package com.zcw.baseutildemo;

/**
 * Created by 朱城委 on 2018/8/30.<br><br>
 * 常量类
 */
public class Constant {
    /**
     * 所有异常错误位置，路径为：/storage/emulated/0/Android/data/包名/files/CrashLog/
     */
    public static final String LOG_DIR_PATH = App.app.getExternalFilesDir("").getPath() + "/CrashLog/";
}
