package com.sorry.band.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.sorry.band.BandApplication;
import com.sorry.core.AppAction;
import com.sorry.core.ToastHanlder;
import com.sorry.core.UIAction;
import com.sorry.core.UIHanlder;
import com.zhaoxiaodan.miband.MiBand;

/**
 * Created by sorry on 8/30/16.
 */
public class BaseActivity extends Activity {
    // 上下文实例
    public Context context;
    // 应用全局的实例
    public BandApplication application;
    // 核心层的AppAction实例
    public AppAction appAction;
    // 核心层的UIAction实例
    public UIAction uiAction;
    //ToastHanlder
    public ToastHanlder toastHanlder;

    public MiBand miBand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        application = (BandApplication) this.getApplication();
        appAction = application.getAppAction();
        uiAction = application.getUiAction();
        toastHanlder = new ToastHanlder(context);
        miBand = application.getMiBand();
    }
}
