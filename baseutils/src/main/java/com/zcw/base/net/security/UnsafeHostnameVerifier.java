package com.zcw.base.net.security;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by 朱城委 on 2019/8/20.<br><br>
 *
 * 不安全的服务器主机名检验类，不推荐。<br>
 * 此类不会对服务器主机名进行校验，默认接受所有主机名。推荐使用如下安全主机名校验类：
 *
 * <pre>
 * public class SafeHostnameVerifier implements HostnameVerifier {
 *      public boolean verify(String hostname, SSLSession session) {
 *          if("yourHostname".equalsIgnoreCase(hostname)) {
 *              return true;
 *          }
 *          else {
 *              HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
 *              return hv.verify(hostname, session);
 *          }
 *      }
 * }
 * </pre>
 */
public class UnsafeHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        // Always return true，接受任意域名服务器
        return true;
    }
}
