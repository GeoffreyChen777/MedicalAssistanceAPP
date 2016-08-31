package com.sorry.core;

import android.os.Message;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by sorry on 8/30/16.
 */
public class HttpCallback implements Callback {
    @Override
    public void onFailure(Call call, IOException e) {
        Log.i("Error","error");
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        if (!response.isSuccessful()) {
            Log.i("Error","Unexpected code " + response);
            throw new IOException("Unexpected code " + response);

        }
        if (response.isSuccessful()) {
            String res = response.body().string();
            Log.i("Response",res);

        }
    }
}
