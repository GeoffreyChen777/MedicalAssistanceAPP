package com.sorry.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sorry.api.utils.DatabaseEngine;
import com.sorry.api.utils.HttpEngine;
import com.sorry.model.PersonalData;
import com.sorry.model.PostData;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.sorry.api.utils.MD5.MD5;

public class ApiImpl implements Api {

    private final static String SMS_KEY = "11798364ca66a";
    private final static String SMS_SECRET = "b1ce7745656b197a54c496bcb2c4b6a0";
    private final static String BAIDU_AK = "6tYzTvGZSOpYB5Oc2YGGOKt8";
    private final static String COUNTRY_EX = "86";
    private HttpEngine httpEngine;
    private DatabaseEngine databaseEngine;

    public ApiImpl(Context context){
        SMSSDK.initSDK(context, SMS_KEY, SMS_SECRET);
        httpEngine = HttpEngine.getInstance();
        databaseEngine = DatabaseEngine.getInstance(context);
    }

    @Override
    public void showToast(Context context, String msg, int aloneTime) {
        Toast.makeText(context, msg, aloneTime).show();
    }

    @Override
    public void sendSmsCode4Register(String phoneNum){
        SMSSDK.getVerificationCode(COUNTRY_EX, phoneNum);
    }

    @Override
    public void verifySMSCode(Context context, String phoneNum, String code, EventHandler eh){
        SMSSDK.registerEventHandler(eh); //注册短信回调
        SMSSDK.submitVerificationCode("86", phoneNum, code);
    }

    @Override
    public void registerByPhone(String phoneNum, String password, Callback callback){
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("method", REGISTER);
        paramMap.put("phoneNum", phoneNum);
        paramMap.put("password", MD5(password));
        httpEngine.post(paramMap, callback);
    }

    @Override
    public void loginByApp(String loginName, String password, Callback callback){
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("method", LOGIN);
        paramMap.put("loginName", loginName);
        paramMap.put("password", MD5(password));
        httpEngine.post(paramMap, callback);
    }

    @Override
    public void initLocation(LocationClient locationClient) {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");                                            //可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(0);                                                   //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);                                           //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);                                                 //可选，默认false,设置是否使用gps
        option.setLocationNotify(false);                                         //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);                                  //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);                                   //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);                                      //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);                                   //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);                                      //可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        locationClient.setLocOption(option);
    }

    @Override
    public void getWeather(String latitude, String longitude, Callback callback) {
        String url = "http://api.map.baidu.com/telematics/v3/weather?location=" + longitude + "," + latitude + "&output=json&ak=" + BAIDU_AK;
        httpEngine.getUrl(url, callback);
    }

    @Override
    public void getPersonalInfo(String loginName, String password, Callback callback) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("method", GET_PINFO);
        paramMap.put("loginName", loginName);
        paramMap.put("password", MD5(password));
        httpEngine.post(paramMap, callback);
    }

    @Override
    public void pushPersonalInfo(String account, PersonalData personalData, Callback callback) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("method", PUSH_PINFO);
        paramMap.put("account", account);
        paramMap.put("sex", String.valueOf(personalData.getSex()));
        paramMap.put("height", String.valueOf(personalData.getHeight()));
        paramMap.put("weight", String.valueOf(personalData.getWeight()));
        paramMap.put("age", String.valueOf(personalData.getAge()));
        paramMap.put("name", personalData.getName());
        paramMap.put("emergencyNumber", personalData.getEmergencyNumber());
        httpEngine.post(paramMap, callback);
    }

    @Override
    public Cursor selectAlldayData() {
        return databaseEngine.select("select * from BodyData;");
    }

    @Override
    public void insertIntoAlldayData(ContentValues contentValues) {
        databaseEngine.insert(contentValues, "BodyData");
    }

    @Override
    public void insertIntoStepData(String step) {

        Calendar c = Calendar.getInstance();
        String month = String.format("%02d",c.get(Calendar.MONTH)+1);
        String day = String.format("%02d",c.get(Calendar.DAY_OF_MONTH));
        String time = month + "." + day;

        collectOneDayData();
        ContentValues contentValues = new ContentValues();
        contentValues.put("time", time);
        contentValues.put("step", step);
        databaseEngine.insert(contentValues, "PerdayStepData");
    }

    @Override
    public void insertIntoHeartRateData(String heartRate) {
        Calendar c = Calendar.getInstance();
        String month = String.format("%02d",c.get(Calendar.MONTH)+1);
        String day = String.format("%02d",c.get(Calendar.DAY_OF_MONTH));
        String hour = c.get(Calendar.HOUR_OF_DAY)+"";
        String minute = c.get(Calendar.MINUTE)+"";

        collectOneDayData();

        String time = month + "." + day + " " + hour + ":" + minute;
        ContentValues contentValues = new ContentValues();
        contentValues.put("time", time);
        contentValues.put("step", heartRate);
        databaseEngine.insert(contentValues, "PerdayHeartRateData");
    }

    @Override
    public void collectOneDayData() {
        Calendar c = Calendar.getInstance();
        String month = String.format("%02d",c.get(Calendar.MONTH)+1);
        String day = String.format("%02d",c.get(Calendar.DAY_OF_MONTH));
        String time = month + "." + day;
        Cursor stepCursor = databaseEngine.select("select * from PerdayStepData;");
        stepCursor.moveToFirst();
        if(stepCursor.getCount() != 0 && !stepCursor.getString(1).equals(time)) {
            Cursor heartCursor = databaseEngine.select("select * from PerdayHeartRateData;");
            heartCursor.moveToFirst();
            ContentValues contentValues = new ContentValues();
            contentValues.put("date", stepCursor.getString(1));
            contentValues.put("step", stepCursor.getString(2));
            List<Integer> heartRates = new ArrayList<Integer>();
            for (int i = 0; i < heartCursor.getCount(); i++) {
                heartRates.add(Integer.valueOf(heartCursor.getString(2)));
                heartCursor.moveToNext();
            }
            int heartRate = 0;
            for (int j = 0; j < heartRates.size(); j++) {
                heartRate += heartRates.get(j);
            }
            double avrangeHeartRate = (heartRate + 0.0) / (heartRates.size() + 0.0);
            contentValues.put("heartrate", String.valueOf(avrangeHeartRate));
            insertIntoAlldayData(contentValues);
            databaseEngine.exec("delete from PerdayStepData;");
            databaseEngine.exec("delete from PerdayHeartRateData;");
        }
    }

    @Override
    public void deleteAlldata(String table) {
        databaseEngine.exec("delete from " + table + ";");
    }

    @Override
    public void exectest(String sql) {
        databaseEngine.exec(sql);
    }

    @Override
    public void putPost(PostData postData, Callback callback) {
        Gson gson = new Gson();
        String j = gson.toJson(postData);
        Log.i("Gson", j);
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("method", PUSH_POST);
        paramMap.put("data", j);
        httpEngine.post(paramMap, callback);
    }

}
