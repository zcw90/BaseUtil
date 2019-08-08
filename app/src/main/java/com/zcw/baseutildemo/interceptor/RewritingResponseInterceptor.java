package com.zcw.baseutildemo.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by 朱城委 on 2019/8/7.<br><br>
 */
public class RewritingResponseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .addHeader("Cache-Control", "max-age=60")
                .build();
    }
}
