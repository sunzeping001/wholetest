package com.example.pluginproject.util;

import android.util.Log;

import com.example.pluginproject.entity.BaseRequest;
import com.example.pluginproject.interf.RequestCallBack;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class HttpClient {

    private OkHttpClient client;
    private static HttpClient httpClient;
    private HttpLoggingInterceptor loggingInterceptor;
    private static final String TAG = "HttpClient";

    private HttpClient() {
        loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,
                        TimeUnit.SECONDS)
//                .addInterceptor(loggingInterceptor)
                .build();
    }

    public static HttpClient getInstance() {
        if (httpClient == null) {
            synchronized (HttpClient.class) {
                if (httpClient == null) {
                    httpClient = new HttpClient();
                }
            }
        }
        return httpClient;
    }

    public String get(String url) {
        return get_internal(url);
    }

    private String get_internal(String url) {
        String result = "";
        if (httpClient != null) {
            Response response;
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = client.newCall(request);
            try {
                response = call.execute();
                int code = response.code();
                result = response.body().string();
                Log.i(TAG, "code is: " + code + ", result is: " + result);
            } catch (IOException e) {
                e.printStackTrace();
                result = e.getLocalizedMessage();
                Log.e(TAG, "get_internal exception: " + e.getLocalizedMessage());
            }
        }
        return result;
    }

    public <T> void post(String url, BaseRequest<T> body, RequestCallBack callBack) {
        post_internal(url, body, callBack);
    }

    private <T> void post_internal(String url, BaseRequest<T> body, RequestCallBack callBack) {
        if (httpClient != null) {
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            Gson gson = new Gson();
            String json = gson.toJson(body, BaseRequest.class);
            RequestBody requestBody = RequestBody.create(mediaType, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            try {
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (callBack != null) {
                            callBack.onFail(-1, e.getLocalizedMessage());
                        }
                        Log.e(TAG, "get_internal onFailure: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        int code = response.code();
                        String result = response.body().string();
                        if (response.isSuccessful()) {
                            callBack.onSuccess(code, result);
                        } else {
                            if (callBack != null) {
                                callBack.onFail(code, result);
                            }
                            Log.i(TAG, "code is: " + code + ", result is: " + result);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "get_internal exception: " + e.getLocalizedMessage());
            }
        }
    }
}
