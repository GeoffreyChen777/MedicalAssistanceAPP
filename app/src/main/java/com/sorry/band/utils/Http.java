package com.sorry.band.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sorry on 8/28/16.
 */
public class Http {
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    public static void get(String url, final Handler hanler){
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.i("Error","Unexpected code " + response);
                    throw new IOException("Unexpected code " + response);
                }
                if (response.isSuccessful()) {
                    if(hanler != null) {
                        String res = response.body().string();
                        JSONObject result = JSONObject.parseObject(res);
                        String code = result.getString("code");
                        String value = result.getString("value");
                        Message message = new Message();
                        message.what = Integer.valueOf(code);
                        message.obj = value;
                        hanler.sendMessage(message);
                    }
                }
            }
        });
    }
}
