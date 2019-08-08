package com.zcw.baseutildemo.interceptor;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 朱城委 on 2019/8/7.<br><br>
 */
public class Test {
    public static void main(String[] args) throws IOException {
        Test test = new Test();

//        test.applicationInterceptor();
//        test.networkInterceptor();
        test.applicationInterceptor2();
    }

    private void applicationInterceptor() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();

        Request request = new Request.Builder()
                .url("http://www.publicobject.com/helloworld.txt")
                .addHeader("User-Agent", "OkHttp Example")
                .build();

        Response response = client.newCall(request).execute();
        response.body().close();
    }

    private void networkInterceptor() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new LoggingInterceptor())
                .build();

        Request request = new Request.Builder()
                .url("http://www.publicobject.com/helloworld.txt")
                .addHeader("User-Agent", "OkHttp Example")
                .build();

        Response response = client.newCall(request).execute();
        response.body().close();
    }

    private void applicationInterceptor2() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new GzipRequestInterceptor())
                .build();

        Request request = new Request.Builder()
                .url("http://www.publicobject.com/helloworld.txt")
                .addHeader("User-Agent", "OkHttp Example")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
        response.body().close();
    }

    private void networkInterceptor2() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new GzipRequestInterceptor())
                .build();

        Request request = new Request.Builder()
                .url("http://www.publicobject.com/helloworld.txt")
                .addHeader("User-Agent", "OkHttp Example")
                .build();

        Response response = client.newCall(request).execute();
        response.body().close();
    }

    private void applicationInterceptor3() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new RewritingResponseInterceptor())
                .build();

        Request request = new Request.Builder()
                .url("http://www.publicobject.com/helloworld.txt")
                .addHeader("User-Agent", "OkHttp Example")
                .build();

        Response response = client.newCall(request).execute();
        response.body().close();
    }

    private void networkInterceptor3() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
                .addNetworkInterceptor(new RewritingResponseInterceptor())
                .build();

        Request request = new Request.Builder()
                .url("http://www.publicobject.com/helloworld.txt")
                .addHeader("User-Agent", "OkHttp Example")
                .build();

        Response response = client.newCall(request).execute();
        response.body().close();
    }
}
