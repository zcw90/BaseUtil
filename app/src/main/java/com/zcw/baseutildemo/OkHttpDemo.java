package com.zcw.baseutildemo;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;


import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;
import okio.BufferedSink;

/**
 * Created by 朱城委 on 2019/7/31.<br><br>
 */
@SuppressLint("NewApi")
public class OkHttpDemo {
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final OkHttpClient client = new OkHttpClient();
    private final Moshi moshi = new Moshi.Builder().build();
    private final JsonAdapter<Gist> gistJsonAdapter = moshi.adapter(Gist.class);

    public static void main(String[] args) throws IOException {
        OkHttpDemo demo = new OkHttpDemo();

//        demo.accessHeader();
//        demo.postString();
//        demo.postStream();
//        demo.postFile();
//        demo.postForm();
//        demo.postMultiRequest();
//        demo.parseJsonWithMoshi();
//        demo.cacheResponse();
//        demo.cancelCall();
//        demo.timeOut();
//        demo.perCall();
//        demo.authentication();

        demo.contributor();
    }

    public void accessHeader() throws IOException {
        Request request = new Request.Builder()
                .url("https://api.github.com/repos/square/okhttp/issues")
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("Accept", "application/json; q=0.5")
                .addHeader("Accept", "application/vnd.github.v3+json")
                .build();

        try(Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            System.out.println("Server: " + response.header("Server"));
            System.out.println("Date: " + response.header("Date"));
            System.out.println("Vary: " + response.headers("Vary"));
        }
    }

    public void postString() throws IOException {
        String postBody = ""
                + "Releases\n"
                + "--------\n"
                + "\n"
                + " * _1.0_ May 6, 2013\n"
                + " * _1.1_ June 15, 2013\n"
                + " * _1.2_ August 11, 2013\n";

        Request request = new Request.Builder()
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                .url("https://api.github.com/markdown/raw")
                .build();

        try(Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            System.out.println(response.body().string());
        }
    }

    public void postStream() throws IOException {
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_MARKDOWN;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("Numbers\n");
                sink.writeUtf8("-------\n");
                for(int i = 2; i <= 997; i++) {
                    sink.writeUtf8(String.format("* %s = %s\n", i, factor(i)));
                }
            }

            private String factor(int n) {
                for (int i = 2; i < n; i++) {
                    int x = n / i;
                    if (x * i == n) return factor(x) + " × " + i;
                }
                return Integer.toString(n);
            }
        };

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(requestBody)
                .build();

        try(Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            System.out.println(response.body().string());
        }
    }

    private void postFile() throws IOException {
        File file = new File("README.md");
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();

        try(Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            System.out.println(response.body().string());
        }
    }

    private void postForm() throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add("search", "Jurassic Park")
                .build();

        Request request = new Request.Builder()
                .url("https://en.wikipedia.org/w/index.php")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }
    }

    private void postMultiRequest() throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "Square Logo")
                .addFormDataPart("image", "pic.png",
                        RequestBody.create(MEDIA_TYPE_PNG, new File("E:/pic.png")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID ...")
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }
    }

    private void parseJsonWithMoshi() throws IOException {
        Request request = new Request.Builder()
                .url("https://api.github.com/gists/c2a7c39532239ff261be")
                .build();

        try(Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            Gist gist = gistJsonAdapter.fromJson(response.body().source());
            for(Map.Entry<String, GistFile> entry : gist.files.entrySet()) {
                System.out.print(entry.getKey() + ": ");
                System.out.println(entry.getValue().content);
            }
        }
    }

    private void cacheResponse() throws IOException {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(new File("./cache"), cacheSize);

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        Request request = new Request.Builder()
                .url("https://publicobject.com/helloworld.txt")
                .build();

        String result1;
        try(Response response1 = client.newCall(request).execute()) {
            if(!response1.isSuccessful()) {
                throw new IOException("Unexpected code " + response1);
            }

            result1 = response1.body().string();
            System.out.println("Response 1 response           " + response1);
            System.out.println("Response 1 cache response:    " + response1.cacheResponse());
            System.out.println("Response 1 network response:  " + response1.networkResponse());
        }

        String result2;
        try(Response response2 = client.newCall(request).execute()) {
            if(!response2.isSuccessful()) {
                throw new IOException("Unexpected code " + response2);
            }

            result2 = response2.body().string();
            System.out.println("Response 2 response           " + response2);
            System.out.println("Response 2 cache response:    " + response2.cacheResponse());
            System.out.println("Response 2 network response:  " + response2.networkResponse());
        }

        System.out.println("result1 equals result2: " + result1.equals(result2));
    }

    private void cancelCall() {
        Request request = new Request.Builder()
                .url("http://httpbin.org/delay/2")
                .build();

        final long startNanos = System.nanoTime();
        final Call call = client.newCall(request);

        executor.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.printf("%.2f Canceling call.%n", (System.nanoTime() - startNanos) / 1e9f);
                call.cancel();
                System.out.printf("%.2f Canceled call.%n", (System.nanoTime() - startNanos) / 1e9f);
            }
        }, 1, TimeUnit.SECONDS);

        System.out.printf("%.2f Executing call.%n", (System.nanoTime() - startNanos) / 1e9f);
        try(Response response = call.execute()) {
            System.out.printf("%.2f Call was expected to fail,but completed: %s%n", (System.nanoTime() - startNanos) / 1e9f, response);
        } catch (IOException e) {
            System.out.printf("%.2f Call failed as expected: %s%n", (System.nanoTime() - startNanos) / 1e9f, e);
        }
    }

    private void timeOut() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url("http://httpbin.org/delay/2")
                .build();

        try(Response response = client.newCall(request).execute()) {
            System.out.println("Response completed: " + response);
        }
    }

    private void perCall() {
        Request request = new Request.Builder()
                .url("http://httpbin.org/delay/1")
                .build();

        OkHttpClient client1 = client.newBuilder()
                .readTimeout(500, TimeUnit.MILLISECONDS)
                .build();

        try(Response response = client1.newCall(request).execute()) {
            System.out.println("Response 1 succeeded: " + response);
        } catch (IOException e) {
            System.out.println("Response 1 failed: " + e);
        }

        OkHttpClient client2 = client.newBuilder()
                .readTimeout(3000, TimeUnit.MILLISECONDS)
                .build();
        try(Response response = client2.newCall(request).execute()) {
            System.out.println("Response 2 succeeded: " + response);
        } catch (IOException e) {
            System.out.println("Response 2 failed: " + e);
        }
    }

    private void authentication() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .authenticator(new Authenticator() {
                    @Nullable
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        if(response.request().header("Authorization") != null) {
                            return null;
                        }

                        System.out.println("Authenticating for response: " + response);
                        System.out.println("Challenges: " + response.challenges());
                        String credential = Credentials.basic("jesse", "password1");
                        return response.request().newBuilder()
                                .header("Authorization", credential)
                                .build();
                    }
                })
                .build();

        Request request = new Request.Builder()
                .url("http://publicobject.com/secrets/hellosecret.txt")
                .build();

        try(Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                 throw new IOException("Unexpected code " + response);
            }

            System.out.println(response.body().string());
        }
    }

    private void contributor() throws IOException {
        Cache cache = new Cache(new File("./cache"), 10 * 1024 * 1024);

        String ENDPOINT = "https://api.github.com/repos/square/okhttp/contributors";
        Moshi MOSHI = new Moshi.Builder().build();
        JsonAdapter<List<Contributor>> CONTRIBUTORS_JSON_ADAPTER = MOSHI.adapter(
                Types.newParameterizedType(List.class, Contributor.class));

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        // Create request for remote resource.
        Request request = new Request.Builder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .url(ENDPOINT)
                .build();

        // Execute the request and retrieve the response.
        try (Response response = client.newCall(request).execute()) {
            // Deserialize HTTP response to concrete type.
            ResponseBody body = response.body();
            List<Contributor> contributors = CONTRIBUTORS_JSON_ADAPTER.fromJson(body.source());

            // Sort list by the most contributions.
            Collections.sort(contributors, new Comparator<Contributor>() {
                @Override
                public int compare(Contributor c1, Contributor c2) {
                    return c2.contributions - c1.contributions;
                }
            });

            // Output list of contributors.
            System.out.println("Contributor: " + contributors.size());
            System.out.println("----------");
            for (Contributor contributor : contributors) {
                System.out.println(contributor.login + ": " + contributor.contributions);
            }
        }
    }

    private static class Gist {
        Map<String, GistFile> files;
    }

    private static class GistFile {
        String content;
    }

    private static class Contributor {
        String login;
        int contributions;
    }
}
