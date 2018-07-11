package com.zcw.base.net;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zcw.base.net.model.BaseResponse;
import com.zcw.base.net.retrofit.AppService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

import static com.zcw.base.net.Constants.ERROR_CUSTOM_HTTP;
import static com.zcw.base.net.Constants.NETWORK_REQUEST_FAILED;

/**
 * Created by 朱城委 on 2017/12/27.<br><br>
 * 网络请求单例类
 */
public class Network {
    private static final String TAG = Network.class.getSimpleName();

    private Retrofit retrofit;
    private AppService appService;

    private static Network Instance;

    public static Network getInstance(String baseUrl) {
        if(Instance == null) {
            synchronized (Network.class) {
                if(Instance == null) {
                    Instance = new Network(baseUrl);
                }
            }
        }

        return Instance;
    }

    public static Network getNewInstance(String baseUrl) {
        Instance = null;
        return getInstance(baseUrl);
    }

    private Network(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        appService = retrofit.create(AppService.class);
    }

    /**
     * get请求
     * @param url 请求的url
     * @param map 请求的参数
     * @param callback 请求回调
     */
    public void get(String url, Map<String, String> map, MyCallback<ResponseBody> callback) {
        appService.get(url, map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(callback));
    }

    /**
     * 同步get请求,同步网络请求需要在新的线程中开启，否则可能会报错。
     * @param url 请求的url
     * @param map 请求的参数
     * @param callback 请求回调
     */
    public void syncGet(String url, Map<String, String> map, MyCallback<ResponseBody> callback) {
        callback.onBefore();
        Call<ResponseBody> call = appService.syncGet(url, map);
        handleRequest(call, callback);
        callback.onAfter();
    }

    /**
     * post请求
     * @param url 请求的url
     * @param map 请求的参数
     * @param callback 请求回调
     */
    public void post(String url, Map<String, String> map , MyCallback<ResponseBody> callback) {
        appService.post(url,map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(callback));
    }

    /**
     * 同步post请求,同步网络请求需要在新的线程中开启，否则可能会报错。
     * @param url 请求url
     * @param map 请求的参数
     * @param callback 请求回调
     */
    public void syncPost(String url, Map<String, String> map , MyCallback<ResponseBody> callback) {
        callback.onBefore();
        Call<ResponseBody> call = appService.syncPost(url, map);
        handleRequest(call, callback);
        callback.onAfter();
    }

    /**
     * 自定义回调{@link MyCallback}转换成Retrofit回调{@link Callback}
     * @param callback
     * @param <T>
     * @return
     */
    private <T> Callback getCallback(final MyCallback<T> callback) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if(response.isSuccessful()) {
                    callback.onResponse(response.body());
                }
                else {
                    callback.onFailure(response.code(), response.message(), new Throwable(NETWORK_REQUEST_FAILED));
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onFailure(ERROR_CUSTOM_HTTP, NETWORK_REQUEST_FAILED, t);
            }
        };
    }

    /**
     * 把{@link MyCallback}回调转换为RxJava观察者。
     * @param callback
     * @return
     */
    private Observer<Response<ResponseBody>> getObserver(final MyCallback<ResponseBody> callback) {
        return new Observer<Response<ResponseBody>>() {
            @Override
            public void onSubscribe(Disposable d) {
                callback.onBefore();
            }

            @Override
            public void onNext(Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    callback.onResponse(response.body());
                }
                else {
                    callback.onFailure(response.code(), response.message(), new Throwable(NETWORK_REQUEST_FAILED));
                }
            }

            @Override
            public void onError(Throwable e) {
                callback.onFailure(ERROR_CUSTOM_HTTP, NETWORK_REQUEST_FAILED, e);
            }

            @Override
            public void onComplete() {
                callback.onAfter();
            }
        };
    }

    /**
     * 处理网络请求，并添加自定义接口{@link MyCallback}的处理。
     * @param call
     * @param callback
     * @param <T>
     */
    private <T> void handleRequest(Call<T> call, final MyCallback<T> callback) {
        try {
            Response<T> response = call.execute();
            if(response.isSuccessful()) {
                callback.onResponse(response.body());
            }
            else {
                callback.onFailure(response.code(), response.message(), new Throwable(NETWORK_REQUEST_FAILED));
            }
        } catch (IOException e) {
            e.printStackTrace();
            callback.onFailure(ERROR_CUSTOM_HTTP, NETWORK_REQUEST_FAILED, new Throwable(NETWORK_REQUEST_FAILED));
            return ;
        }
    }

    private MultipartBody getMultipartBody(List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        for(File file : files) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
            builder.addFormDataPart("File", file.getName(), requestBody);
        }

        builder.setType(MultipartBody.FORM);
        return builder.build();
    }
}
