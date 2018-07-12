package com.zcw.base.net.model;

/**
 * Created by 朱城委 on 2018/1/10.<br><br>
 * 网络请求返回结果基础类
 */

public class BaseResponse<T> {
    /** 网络请求结果，为0表示成功，非0表示失败 */
    private String result;

    /** 网络请求详细信息，比如“成功”、“访问凭证有误！” */
    private String message;

    /** 网络请求真正数据，如果请求失败，则为“” */
    private T data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
