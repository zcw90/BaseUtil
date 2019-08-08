package com.zcw.baseutildemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zcw.base.CommonUtils;
import com.zcw.base.LogUtil;
import com.zcw.base.ThreadPoolUtils;
import com.zcw.base.net.MyCallback;
import com.zcw.base.net.Network;
import com.zcw.base.view.CustomDialog;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by 朱城委 on 2019/7/18.<br><br>
 * 网络请求测试页面s
 */
public class NetActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = NetActivity.class.getSimpleName();

    /** 同步get网络请求标识 */
    private static final int MESSAGE_SYN_GET = 100;

    /** 同步post网络请求标识 */
    private static final int MESSAGE_SYN_POST = 110;

    /** 异步get网络请求标识 */
    private static final int MESSAGE_ASYN_GET = 120;

    private static MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private static final String URL = "http://t.weather.sojson.com/api/weather/city/101030100";
    private Network network;
    private MyHandler handler;

    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);

        init();
    }

    private void init() {
        findViewById(R.id.btn_normal_net).setOnClickListener(this);
        findViewById(R.id.btn_okhttp_syn_get).setOnClickListener(this);
        findViewById(R.id.btn_okhttp_syn_post).setOnClickListener(this);
        findViewById(R.id.btn_okhttp_asyn_get).setOnClickListener(this);

        network = Network.getInstance("http://t.weather.sojson.com");
        handler = new MyHandler(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_normal_net:
                normalTest();
                break;

            case R.id.btn_okhttp_syn_get:
                okHttpSynGet();
                break;

            case R.id.btn_okhttp_syn_post:
                okHttpSynPost();
                break;

            case R.id.btn_okhttp_asyn_get:
                okHttpAsyncGet();
                break;
        }
    }

    private void normalTest() {
        network.get(URL, new HashMap<String, String>(), new MyCallback<ResponseBody>() {
            @Override
            public void onResponse(ResponseBody body) {
                try {
                    showDialog("Retrofit普通请求", body.string());
                } catch (IOException e) {
                    e.printStackTrace();
                    CommonUtils.toast(NetActivity.this, "网络请求失败");
                }
            }
        });
    }

    private void okHttpSynGet() {
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("https://publicobject.com/helloworld.txt")
                .build();

        ThreadPoolUtils.getScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    Message message = new Message();
                    message.what = MESSAGE_SYN_GET;
                    message.obj = response.body().string();
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtil.e(TAG, "网络请求失败");
                }
            }
        });
    }

    private void okHttpSynPost() {
        String json = bowlingJson("Jesse", "Jake");
        RequestBody body = RequestBody.create(MEDIA_TYPE, json);

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://www.roundsapp.com/post")
                .post(body)
                .build();

        ThreadPoolUtils.getScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    Message message = new Message();
                    message.what = MESSAGE_SYN_POST;
                    message.obj = response.body().string();
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtil.e(TAG, "网络请求失败");
                }
            }
        });
    }

    private String bowlingJson(String player1, String player2) {
        return "{'winCondition':'HIGH_SCORE',"
                + "'name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                + "]}";
    }

    private void okHttpAsyncGet() {
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("https://publicobject.com/helloworld.txt")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e(TAG, "网络请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                message.what = MESSAGE_ASYN_GET;
                message.obj = response.body().string();
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 显示Dialog
     * @param message dialog显示的内容
     */
    private void showDialog(String title, String message) {
        if(dialog == null) {
            dialog = new CustomDialog(NetActivity.this);
            dialog.withButton1("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        dialog.withTitle(title);
        dialog.withMessage(message);
        dialog.show();
    }

    private static class MyHandler extends Handler {
        private WeakReference<NetActivity> netActivity;

        public MyHandler(NetActivity netActivity) {
            this.netActivity = new WeakReference<>(netActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            String title = "OkHttp同步post";
            switch (msg.what) {
                case MESSAGE_SYN_GET:
                    title = "OkHttp同步get";
                    break;

                case MESSAGE_SYN_POST:
                    title = "OkHttp同步post";
                    break;

                case MESSAGE_ASYN_GET:
                    title = "OkHttp异步get";
                    break;
            }

            String message = (String) msg.obj;
            netActivity.get().showDialog(title, message);
        }
    }
}
