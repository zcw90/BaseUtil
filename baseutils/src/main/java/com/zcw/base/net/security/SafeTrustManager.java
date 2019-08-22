package com.zcw.base.net.security;

import com.zcw.base.LogUtil;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Created by 朱城委 on 2019/8/20.<br><br>
 *
 * 安全的证书校验类，推荐使用。在{@link #checkServerTrusted(X509Certificate[], String)}方法中对证书进行验证。
 */
public class SafeTrustManager implements X509TrustManager {
    private static final String TAG = SafeTrustManager.class.getSimpleName();

    /** 指定证书(比如自签名证书) */
    private Certificate certificate;

    public SafeTrustManager() {
    }

    public SafeTrustManager(Certificate certificate) {
        this.certificate = certificate;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        for(X509Certificate cert : chain) {
            // 检查证书是否有效，证书未生效和证书过期都会抛出相应的异常
            cert.checkValidity();

            // 如果设置了指定证书，在这里验证指定证书（比如自签名证书）
            if(certificate != null) {
                try {
                    cert.verify(certificate.getPublicKey());
                } catch (Exception e) {
                    LogUtil.eAndSave(TAG, e.getMessage());
                    throw new CertificateException(e);
                }
            }
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
