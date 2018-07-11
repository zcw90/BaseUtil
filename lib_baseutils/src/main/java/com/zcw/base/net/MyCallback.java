package com.zcw.base.net;

import com.zcw.base.LogUtil;

import okhttp3.ResponseBody;

/**
 * Created by 朱城委 on 2017/12/27.<br><br>
 * 网络请求回调接口，此回调多运行在UI线程中。<br>
 * 网络请求成功，调用{@link #onResponse(Object)};<br>
 * 网络请求失败，调用{@link #onFailure(int, String, Throwable)}。
 */
public abstract class MyCallback<T> {
    private static final String TAG = MyCallback.class.getSimpleName();

    /**
     * 发送网络请求之前回调
     */
    public void onBefore() {
    }

    /**
     * 完成网络请求之后回调
     */
    public void onAfter() {

    }

    /**
     * 网络请求成功回调函数,网络请求返回码在[200, 300)时，调用此函数。
     * @param body
     */
    public abstract void onResponse(T body);

    /**
     * 网络请求失败回调函数
     * @param errorCode
     * @param message
     * @param throwable
     */
    public void onFailure(int errorCode, String message, Throwable throwable) {
        StringBuilder builder = new StringBuilder()
                .append("ErrorCode: " + errorCode + "\n")
                .append("ErrorMessage: " + message + "\n")
                .append("ThrowableMessage: " + throwable.getMessage());
        LogUtil.e(TAG, builder.toString());
    }

    public static MyCallback<ResponseBody> CALLBACK_DEFAULT = new MyCallback<ResponseBody>() {
        @Override
        public void onResponse(ResponseBody body) {

        }
    };
}
