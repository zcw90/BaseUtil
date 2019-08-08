package com.zcw.baseutildemo.event;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by 朱城委 on 2019/8/8.<br><br>
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class Test {
    public static void main(String[] args) throws IOException {
        Test test = new Test();

//        test.listener();
        test.listener2();
    }

    private void listener() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .eventListener(new PrintingEventListener())
                .build();

        Request request = new Request.Builder()
                .url("https://publicobject.com/helloworld.txt")
                .build();

        System.out.println("REQUEST 1 (new connection)");
        try(Response response = client.newCall(request).execute()) {
            response.body().source().readByteString();
        }

        System.out.println("REQUEST 2 (new connection)");
        try(Response response = client.newCall(request).execute()) {
            response.body().source().readByteString();
        }
    }

    private void listener2() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .eventListenerFactory(PrintingEventListener2.FACTORY)
                .build();

        Request washingtonPostRequest = new Request.Builder()
                .url("https://www.washingtonpost.com/")
                .build();

        client.newCall(washingtonPostRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try(ResponseBody body = response.body()) {
                    body.source().readByteString();
                }
            }
        });

        Request newYorkTimesRequest = new Request.Builder()
                .url("https://www.nytimes.com/")
                .build();

        client.newCall(newYorkTimesRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try(ResponseBody body = response.body()) {
                    body.source().readByteString();
                }
            }
        });
    }
}
