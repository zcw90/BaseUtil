package com.zcw.base.net.retrofit;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by 朱城委 on 2017/12/27.<br><br>
 * 网络请求核心类
 */

public interface AppService {

    /**
     * get请求
     * @param url 请求的url
     * @param map 请求要拼接的参数
     * @return
     */
    @GET
    Observable<Response<ResponseBody>> get(@Url String url, @QueryMap Map<String, String> map);

    /**
     * 同步get请求
     * @param url 请求url
     * @param map 请求要拼接的参数
     * @return
     */
    @GET
    @Streaming
    Call<ResponseBody> syncGet(@Url String url, @QueryMap Map<String, String> map);

    /**
     * post请求
     * @param url 请求的url
     * @param map 请求参数
     * @return
     */
    @POST
    @FormUrlEncoded
    Observable<Response<ResponseBody>> post(@Url String url, @FieldMap Map<String, String> map);

    /**
     * 同步post请求
     * @param url 请求的url
     * @param map 请求参数
     * @return
     */
    @POST
    @FormUrlEncoded
    Call<ResponseBody> syncPost(@Url String url, @FieldMap Map<String, String> map);
}
