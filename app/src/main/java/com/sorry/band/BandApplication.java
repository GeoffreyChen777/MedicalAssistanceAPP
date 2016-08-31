package com.sorry.band;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sorry.core.AppAction;
import com.sorry.core.AppActionImpl;
import com.sorry.core.UIAction;
import com.sorry.core.UIActionImpl;
import com.sorry.core.UIHanlder;
import com.sorry.model.PersonalData;
import com.zhaoxiaodan.miband.MiBand;

import okhttp3.OkHttpClient;

/**
 * Created by sorry on 2016/8/10.
 */
public class BandApplication extends Application {


    private String account;
    private String pwd;
    private PersonalData personalData;

    private boolean inited = false;
    private AppAction appAction;
    private UIAction uiAction;
    private UIHanlder uiHanlder;



    @Override
    public void onCreate() {
        super.onCreate();
        appAction = new AppActionImpl(this);
        uiHanlder = new UIHanlder();
        uiAction = new UIActionImpl(uiHanlder);
        personalData = new PersonalData();
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }

    public UIAction getUiAction() {
        return uiAction;
    }

    public AppAction getAppAction() {
        return appAction;
    }

    public void setInited(boolean inited) {
        this.inited = inited;
    }

    private int type = 0;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public MiBand getMiBand() {
        return miBand;
    }

    public void setMiBand(MiBand miBand) {
        this.miBand = miBand;
    }

    private MiBand miBand = new MiBand(this);

    public boolean isInited() {
        return inited;
    }
    public boolean isUnInited() {
        return !inited;
    }

    public void setInit(boolean inited) {
        this.inited = inited;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String uid = "20000000";

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



}
