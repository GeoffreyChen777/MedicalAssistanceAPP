package com.sorry.band;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zhaoxiaodan.miband.MiBand;

import okhttp3.OkHttpClient;

/**
 * Created by sorry on 2016/8/10.
 */
public class BandApplication extends Application {

    private int sex;
    private int age;
    private int height;
    private int weight;
    private String name;
    private String account;
    private String pwd;
    private String emergencyNumber = "18505390011";
    private OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
    private boolean inited = false;
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

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }


    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



}
