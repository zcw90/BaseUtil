package com.zcw.baseutildemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zcw.base.AlarmUtils;
import com.zcw.base.AppInfoUtils;
import com.zcw.base.ClickUtils;
import com.zcw.base.CommonUtils;
import com.zcw.base.LogUtil;
import com.zcw.base.daemon.DaemonManager;
import com.zcw.base.view.CustomDialog;
import com.zcw.baseutildemo.receiver.AlarmTestReceiver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        findViewById(R.id.btn_log).setOnClickListener(this);
        findViewById(R.id.btn_app_info).setOnClickListener(this);
        findViewById(R.id.btn_click_faster).setOnClickListener(this);
        findViewById(R.id.btn_click_continue).setOnClickListener(this);

        findViewById(R.id.btn_daemon1).setOnClickListener(this);
        findViewById(R.id.btn_daemon2).setOnClickListener(this);
        findViewById(R.id.btn_alarm_start).setOnClickListener(this);
        findViewById(R.id.btn_alarm_stop).setOnClickListener(this);

        findViewById(R.id.btn_dialog).setOnClickListener(this);
        findViewById(R.id.btn_dialog_custom).setOnClickListener(this);

        findViewById(R.id.btn_net_test).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_log:
                logTest();
                break;

            case R.id.btn_app_info:
                appInfoTest();
                break;

            case R.id.btn_click_faster:
                clickFasterTest();
                break;

            case R.id.btn_click_continue:
                clickContinueTest();
                break;

            case R.id.btn_daemon1:
                DaemonManager.startDaemonJobService(MainActivity.this, 2000);
                break;

            case R.id.btn_daemon2:
                DaemonManager.startDaemonService(MainActivity.this, 4000);
                break;

            case R.id.btn_alarm_start:
                AlarmUtils.startAlarm(MainActivity.this,6000, AlarmTestReceiver.class);
                break;

            case R.id.btn_alarm_stop:
                AlarmUtils.stopAlarm(MainActivity.this, AlarmTestReceiver.class);
                break;

            case R.id.btn_dialog:
                dialogDemo();
                break;

            case R.id.btn_dialog_custom:
                dialogCustomDemo();
                break;

            case R.id.btn_net_test:
                netTest();
                break;
        }
    }

    private void logTest() {
        LogUtil.d(TAG, "Log test.");
        LogUtil.d(TAG, "Meta-data com.zcw.base.meta.data.test1: " + AppInfoUtils.getMetaDataApplication(MainActivity.this,
                "com.zcw.base.meta.data.test1", "string"));
        LogUtil.d(TAG, "Meta-data com.zcw.base.meta.data.test2: " + AppInfoUtils.getMetaDataApplication(MainActivity.this,
                "com.zcw.base.meta.data.test2", "int"));
        LogUtil.d(TAG, "Meta-data com.zcw.base.meta.data.test3: " + AppInfoUtils.getMetaDataApplication(MainActivity.this,
                "com.zcw.base.meta.data.test3", "float"));
        LogUtil.d(TAG, "Meta-data com.zcw.base.meta.data.test4: " + AppInfoUtils.getMetaDataApplication(MainActivity.this,
                "com.zcw.base.meta.data.test4", "boolean"));
        LogUtil.d(TAG, "Meta-data com.zcw.base.meta.data.test4: " + AppInfoUtils.getMetaDataApplication(MainActivity.this,
                "com.zcw.base.meta.data.test4", "double"));
    }

    private void appInfoTest() {
        StringBuilder builder = new StringBuilder();
        builder.append(AppInfoUtils.getAppName(MainActivity.this) + "\n");
        builder.append(AppInfoUtils.getAppPackage(MainActivity.this) + "\n");
        builder.append(AppInfoUtils.getAppVersionCodeInt(MainActivity.this) + "\n");
        builder.append(AppInfoUtils.getAppVersionCodeString(MainActivity.this) + "\n");
        builder.append(AppInfoUtils.getAppVersionName(MainActivity.this) + "\n");
        builder.append(AppInfoUtils.getMetaDataApplication(MainActivity.this, "AAA", "string") + "\n");
        builder.append(AppInfoUtils.getSignatureMD5(MainActivity.this));

        final CustomDialog dialog = new CustomDialog(MainActivity.this);
        dialog.withButton1("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.withMessage(builder.toString());
        dialog.show();
    }

    private void clickFasterTest() {
        if(ClickUtils.isFastClick()) {
            CommonUtils.toast(MainActivity.this, "请不要快速点击");
            return ;
        }

        CommonUtils.toast(MainActivity.this, "按钮被点击");
    }

    private void clickContinueTest() {
        int count = ClickUtils.continueClick(20);
        if(count != 0) {
            CommonUtils.toast(MainActivity.this, "还需点击 " + count + " 次");
            return ;
        }

        final CustomDialog dialog = new CustomDialog(MainActivity.this);
        dialog.withButton1("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.withMessage("恭喜你，完成了20次点击");
        dialog.show();
    }

    private void dialogDemo() {
        final CustomDialog dialog = new CustomDialog(MainActivity.this);
        dialog.withTitle("This is title");
        dialog.withMessage("Dialog Demo!!!");
        dialog.withButton1("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.withButton2("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void dialogCustomDemo() {
        final CustomDialog dialog = new CustomDialog(MainActivity.this, R.style.dialog_transparent_custom1);
        dialog.withTitle("This is title");
        dialog.withMessage("Dialog Demo!!!");
        dialog.withButton1("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.withButton2("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void netTest() {
        Intent intent = new Intent(MainActivity.this, NetActivity.class);
        startActivity(intent);
    }
}
