package com.zcw.baseutildemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zcw.base.AppInfoUtils;
import com.zcw.base.CommonUtils;
import com.zcw.base.LogUtil;
import com.zcw.base.view.CustomDialog;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_log:
                LogUtil.d(TAG, "Log test.");
                break;

            case R.id.btn_app_info:
                appInfoTest();
                break;
        }
    }

    private void appInfoTest() {
        StringBuilder builder = new StringBuilder();
        builder.append(AppInfoUtils.getAppName(MainActivity.this) + "\n");
        builder.append(AppInfoUtils.getAppPackage(MainActivity.this) + "\n");
        builder.append(AppInfoUtils.getAppVersionCodeInt(MainActivity.this) + "\n");
        builder.append(AppInfoUtils.getAppVersionCodeString(MainActivity.this) + "\n");
        builder.append(AppInfoUtils.getAppVersionName(MainActivity.this) + "\n");
        builder.append(AppInfoUtils.getMetaDataApplicationInt(MainActivity.this, "AAA") + "\n");
        builder.append(AppInfoUtils.getSignatureMD5(MainActivity.this) + "\n");

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
}
