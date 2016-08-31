package com.sorry.core;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.sorry.api.Api;
import com.sorry.api.ApiImpl;

/**
 * Created by sorry on 8/31/16.
 */
public class ToastHanlder extends Handler {
    public final static int TOAST = 0x01;
    private Api api;
    private Context context;

    public ToastHanlder(Context context){
        this.context = context;
        api = new ApiImpl(context);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case TOAST:{
                api.showToast(context, (String)msg.obj, Toast.LENGTH_SHORT);
                break;
            }
        }
    }
}
