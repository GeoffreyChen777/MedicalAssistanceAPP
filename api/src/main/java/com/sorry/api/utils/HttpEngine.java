package com.sorry.api.utils;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpEngine {
    private static final String TAG = "HttpEngine";
    private static final String url = "http://115.159.200.151/api.php";

    private static HttpEngine instance = null;
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    private HttpEngine(){

    }

    public static HttpEngine getInstance(){
        if(instance == null){
            instance = new HttpEngine();
        }
        return instance;
    }


    public void get(Map<String, String> paramsMap, Callback callback){

        Request request = new Request.Builder().url(joinGetParam(url, paramsMap)).build();
        okHttpClient.newCall(request).enqueue(callback);

    }

    public void getUrl(String url, Callback callback){
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public void post(Map<String, String> paramsMap, Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .post(joinPostParam(paramsMap))
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public String joinGetParam(String url, Map<String, String> paramsMap){
        url = url + "?";
        for (String key : paramsMap.keySet()) {
            url = url + key + "=" + paramsMap.get(key) + "&";
        }
        return url;
    }

    public RequestBody joinPostParam(Map<String, String> paramsMap){
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            builder.add(key, paramsMap.get(key));
        }
        return builder.build();
    }
}
