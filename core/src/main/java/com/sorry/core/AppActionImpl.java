package com.sorry.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sorry.api.Api;
import com.sorry.api.ApiImpl;
import com.sorry.api.ApiResponse;
import com.sorry.model.PersonalData;
import com.sorry.model.PostData;
import com.sorry.model.SignData;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by sorry on 8/30/16.
 */
public class AppActionImpl implements AppAction {
    private Context context;
    private Api api;
    private SMSEventHandler smsEventHandler = null;
    private LocationClient locationClient = null;

    public AppActionImpl(Context context) {
        this.context = context;
        this.api = new ApiImpl(context);
    }

    @Override
    public void showToast(String msg, int aloneTime, ToastHanlder hanlder){
        Message message = new Message();
        message.what = ToastHanlder.TOAST;
        message.obj = msg;
        hanlder.sendMessage(message);
    }

    @Override
    public void sendSmsCode(final String phoneNum, final ActionCallbackListener<Integer> listener) {
        // 参数检查
        if (TextUtils.isEmpty(phoneNum)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "手机号为空");
            }
            return;
        }
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(phoneNum);
        if (!matcher.matches()) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            }
            return;
        }
        api.sendSmsCode4Register(phoneNum);
    }

    @Override
    public void verifySmsCode(String phoneNum, String code, String password, ActionCallbackListener<Integer> listener){
        if (TextUtils.isEmpty(phoneNum)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "手机号为空");
            }
            return;
        }
        if (TextUtils.isEmpty(code)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "验证码为空");
            }
            return;
        }
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "密码为空");
            }
            return;
        }
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(phoneNum);
        if (!matcher.matches()) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            }
            return;
        }

        Log.i("SMS","unregisterAllEventHandler");
        // 请求Api
        if(this.smsEventHandler == null) {
            this.smsEventHandler = new SMSEventHandler(listener);
        }
        api.verifySMSCode(context, phoneNum, code, this.smsEventHandler);
    }

    @Override
    public void register(final String phoneNum, final String password, final ActionCallbackListener<Void> registerListener) {
        api.registerByPhone(phoneNum, password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                registerListener.onFailure(ErrorEvent.NET_ERROR, "请求服务器失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.i("AppAction","Unexpected code " + response);
                    registerListener.onFailure(ErrorEvent.NET_ERROR, "请求服务器失败");
                    throw new IOException("Unexpected code " + response);
                }
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ApiResponse<Void>>() {}.getType();
                    ApiResponse<Void> apiResponse = gson.fromJson(response.body().string(), type);
                    if (apiResponse.isSuccess()) {
                        registerListener.onSuccess(null);
                    } else {
                        registerListener.onFailure(apiResponse.getEvent(), apiResponse.getMsg());
                    }
                }
            }
        });

    }

    @Override
    public void login(final String loginName, final String password, final ActionCallbackListener<Void> listener) {
        // 参数检查
        if (TextUtils.isEmpty(loginName)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "登录名为空");
            }
            return;
        }
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "密码为空");
            }
            return;
        }

        // 请求Api
        api.loginByApp(loginName, password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure(ErrorEvent.NET_ERROR, "请求服务器失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.i("AppAction","Unexpected code " + response);
                    listener.onFailure(ErrorEvent.NET_ERROR, "请求服务器失败");
                    throw new IOException("Unexpected code " + response);
                }
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ApiResponse<Void>>() {}.getType();
                    ApiResponse<Void> apiResponse = gson.fromJson(response.body().string(), type);

                    if (apiResponse.isSuccess()) {
                        listener.onSuccess(null);

                    } else {
                        listener.onFailure(apiResponse.getEvent(), apiResponse.getMsg());
                    }
                }
            }
        });
    }


    @Override
    public void getLocation(final ActionCallbackListener<List<Double>> listener) {
        if(this.locationClient == null) {
            this.locationClient = new LocationClient(context);
            api.initLocation(this.locationClient);
            this.locationClient.registerLocationListener(new BDLocationListener() {  //注册监听函数
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    if (bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {  // 定位成功
                        List<Double> lal = new ArrayList<Double>();
                        lal.add(bdLocation.getLatitude());
                        lal.add(bdLocation.getLongitude());
                        listener.onSuccess(lal);
                    }
                    else{
                        listener.onFailure(ErrorEvent.GPS_ERROR, "定位失败");
                    }
                }
            });
        }
        this.locationClient.start();
    }

    @Override
    public void getWeather(final ActionCallbackListener<String> listener) {
        this.getLocation(new ActionCallbackListener<List<Double>>() {
            @Override
            public void onSuccess(List<Double> data) {
                api.getWeather(data.get(0).toString(), data.get(1).toString(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        listener.onFailure(ErrorEvent.NET_ERROR, "请求服务器失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            Log.i("AppAction","Unexpected code " + response);
                            listener.onFailure(ErrorEvent.NET_ERROR, "请求服务器失败");
                            throw new IOException("Unexpected code " + response);
                        }
                        if (response.isSuccessful()) {
                            String weatherJson = response.body().string();
                            Log.i("Weather", weatherJson);
                            JSONObject jsonObj = JSONObject.parseObject(weatherJson);
                            JSONObject jsonObj2 = jsonObj.getJSONArray("results").getJSONObject(0);
                            JSONArray weatherJsonArray = jsonObj2.getJSONArray("weather_data");
                            JSONObject weatherObj = weatherJsonArray.getJSONObject(0);
                            String weather = weatherObj.getString("weather");
                            String wind = weatherObj.getString("wind");
                            String temperature = weatherObj.getString("temperature");
                            String weatherInfor = weather + "，" + wind + "，" + temperature;
                            listener.onSuccess(weatherInfor);
                        }
                    }
                });
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                listener.onFailure(errorEvent, message);
            }
        });
    }


    @Override
    public void getPersonalInfo(String phoneNum, String password, final ActionCallbackListener<ApiResponse<PersonalData>> listener) {
        if (TextUtils.isEmpty(phoneNum)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "登录名为空");
            }
            return;
        }
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "密码为空");
            }
            return;
        }
        api.getPersonalInfo(phoneNum, password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure(ErrorEvent.NET_ERROR, "请求服务器失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.i("AppAction","Unexpected code " + response);
                    listener.onFailure(ErrorEvent.NET_ERROR, "请求服务器失败");
                    throw new IOException("Unexpected code " + response);
                }
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ApiResponse<PersonalData>>(){}.getType();
                    String json = response.body().string();
                    ApiResponse<PersonalData> apiResponse = gson.fromJson(json, type);
                    if(apiResponse.isSuccess()) {
                        listener.onSuccess(apiResponse);
                    }
                    else{
                        listener.onFailure(apiResponse.getEvent(), apiResponse.getMsg());
                    }
                }
            }
        });
    }

    @Override
    public void pushPersonalInfo(String account, PersonalData personalData, final ActionCallbackListener<ApiResponse<Void>> listener) {
        api.pushPersonalInfo(account, personalData, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure(ErrorEvent.NET_ERROR, "提交个人信息失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.i("AppAction","Unexpected code " + response);
                    listener.onFailure(ErrorEvent.NET_ERROR, "请求服务器失败");
                    throw new IOException("Unexpected code " + response);
                }
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ApiResponse<Void>>(){}.getType();
                    String json = response.body().string();
                    ApiResponse<PersonalData> apiResponse = gson.fromJson(json, type);
                    Log.i("PushR",json);
                    if(apiResponse.isSuccess()) {
                        listener.onSuccess(null);
                    }
                    else{
                        listener.onFailure(apiResponse.getEvent(), apiResponse.getMsg());
                    }
                }
            }
        });
    }

    @Override
    public void getAllBodyData(ActionCallbackListener<Cursor> listener) {
        Cursor cursor = api.selectAlldayData();
        if(cursor.getCount() == 0){
            listener.onFailure(ErrorEvent.NO_DATA, "无历史数据");
        }
        else{
            listener.onSuccess(cursor);
        }
    }

    @Override
    public void insertIntoAllBodyData(List<ContentValues> contentValues) {
        for(int i = 0; i < contentValues.size(); i++) {
            api.insertIntoAlldayData(contentValues.get(i));
        }
    }

    @Override
    public void deleteAlldata(String table) {
        api.deleteAlldata(table);
    }

    @Override
    public void exec(String sql) {
        api.exectest(sql);
    }

    @Override
    public void insertIntoPerdayStepData(String step) {
        api.insertIntoStepData(step);
    }

    @Override
    public void insertIntoPerdayHeartrateData(String heartRate) {
        api.insertIntoHeartRateData(heartRate);
    }

    @Override
    public void pushPost(String account, String title, String content, boolean isWithInfo, final ActionCallbackListener<Void> listener) {
        PostData postData = new PostData();
        postData.setAccount(account);
        postData.setTitle(title);
        postData.setContent(content);

        if(isWithInfo) {
            List<SignData> signDataList = new ArrayList<>();
            Cursor cursor = api.selectAlldayData();
            while (cursor.moveToNext()) {
                SignData signData = new SignData();
                signData.setDate(cursor.getString(0));
                signData.setHeartRate(Integer.valueOf(cursor.getString(1)));
                signData.setStepNum(Integer.valueOf(cursor.getString(2)));
                signDataList.add(signData);
            }

            postData.setSignDataList(signDataList);
        }
        else{
            postData.setSignDataList(null);
        }
        api.putPost(postData, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure(ErrorEvent.NET_ERROR, "发送失败请重试");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    listener.onFailure(ErrorEvent.NET_ERROR, "发送失败请重试");
                }
                else {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ApiResponse<Void>>(){}.getType();
                    String json = response.body().string();
                    ApiResponse<PersonalData> apiResponse = gson.fromJson(json, type);
                    if(apiResponse.isSuccess()) {
                        listener.onSuccess(null);
                    }
                    else{
                        listener.onFailure(apiResponse.getEvent(), apiResponse.getMsg());
                    }
                }
            }
        });

    }

}
