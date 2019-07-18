package com.zcw.baseutildemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zcw.base.CommonUtils;
import com.zcw.base.net.MyCallback;
import com.zcw.base.net.Network;
import com.zcw.base.view.CustomDialog;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;

/**
 * Created by 朱城委 on 2019/7/18.<br><br>
 * 网络请求测试页面s
 */
public class NetActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String URL = "http://t.weather.sojson.com/api/weather/city/101030100";
    private Network network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);

        init();
    }

    private void init() {
        findViewById(R.id.btn_normal_net).setOnClickListener(this);

        network = Network.getInstance("http://t.weather.sojson.com");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_normal_net:
                normalTest();
                break;
        }
    }

    private void normalTest() {
        network.get(URL, new HashMap<String, String>(), new MyCallback<ResponseBody>() {
            @Override
            public void onResponse(ResponseBody body) {
                try {
                    final CustomDialog dialog = new CustomDialog(NetActivity.this);
                    dialog.withMessage(body.string());
                    dialog.withButton1("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    CommonUtils.toast(NetActivity.this, "网络请求失败");
                }
            }
        });
    }
}
