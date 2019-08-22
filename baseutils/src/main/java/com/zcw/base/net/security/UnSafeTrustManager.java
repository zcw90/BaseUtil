package com.zcw.base.net.security;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Created by 朱城委 on 2019/8/20.<br><br>
 *
 * 不安全的证书校验类，不推荐。<br>
 *
 * 正确的做法，是在{@link UnSafeTrustManager#checkServerTrusted(X509Certificate[], String)}方法中，校验服务端证书的合法性，参见{@link SafeTrustManager}。
 */
public class UnSafeTrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // do nothing，接受任意客户端证书
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // do nothing，接受任意服务端证书
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
