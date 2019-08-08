package com.zcw.baseutildemo.event;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 朱城委 on 2019/8/8.<br><br>
 */
public class PrintingEventListener extends EventListener {
    private long callStartNanos;

    private void printEvent(String name) {
        long nowNanos = System.nanoTime();
        if (name.equals("callStart")) {
            callStartNanos = nowNanos;
        }
        long elapsedNanos = nowNanos - callStartNanos;
        System.out.printf("%.3f %s%n", elapsedNanos / 1000000000d, name);
    }

    @Override public void callStart(Call call) {
        printEvent("callStart");
    }

    @Override
    public void dnsStart(Call call, String domainName) {
        printEvent("dnsStart");
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        printEvent("dnsEnd");
    }

    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        printEvent("connectStart");
    }

    @Override
    public void secureConnectStart(Call call) {
        printEvent("secureConnectStart");
    }

    @Override
    public void secureConnectEnd(Call call, @Nullable Handshake handshake) {
        printEvent("secureConnectEnd");
    }

    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, @Nullable Protocol protocol) {
        printEvent("connectEnd");
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, @Nullable Protocol protocol, IOException ioe) {
        printEvent("connectFailed");
    }

    @Override
    public void connectionAcquired(Call call, Connection connection) {
        printEvent("connectionAcquired");
    }

    @Override
    public void connectionReleased(Call call, Connection connection) {
        printEvent("connectionReleased");
    }

    @Override
    public void requestHeadersStart(Call call) {
        printEvent("requestHeadersStart");
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        printEvent("requestHeadersEnd");
    }

    @Override
    public void requestBodyStart(Call call) {
        printEvent("requestBodyStart");
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        printEvent("requestBodyEnd");
    }

    @Override
    public void responseHeadersStart(Call call) {
        printEvent("responseHeadersStart");
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        printEvent("responseHeadersEnd");
    }

    @Override
    public void responseBodyStart(Call call) {
        printEvent("responseBodyStart");
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        printEvent("responseBodyEnd");
    }

    @Override
    public void callEnd(Call call) {
        printEvent("callEnd");
    }

    @Override
    public void callFailed(Call call, IOException ioe) {
        printEvent("callFailed");
    }
}
