package com.sorry.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.baidu.location.LocationClient;
import com.sorry.model.PersonalData;

import cn.smssdk.EventHandler;
import okhttp3.Callback;

/**
 * Created by sorry on 8/29/16.
 */
public interface Api {

    public final static String REGISTER = "user.registerByPhone";
    public final static String LOGIN = "user.loginByApp";
    public final static String GET_PINFO = "user.getPersonalInfo";
    public final static String PUSH_PINFO = "user.pushPersonalInfo";


    public void showToast(Context context, String msg, int aloneTime);

    public void sendSmsCode4Register(String phoneNum);

    /**
     * 核对验证码
     *
     * @param phoneNum 手机号码
     * @param code     验证码
     * @param eh       回调处理
     */
    public void verifySMSCode(Context context, String phoneNum, String code, EventHandler eh);

    /**
     * 登录
     *
     * @param phoneNum 手机号码
     * @param password 密码
     * @param callback 回调处理
     */
    public void registerByPhone(String phoneNum, String password, Callback callback);

    /**
     * 登录
     *
     * @param loginName 登录名（手机号）
     * @param password  MD5加密的密码
     */
    public void loginByApp(String loginName, String password, Callback callback);

    /**
     * 初始化定位
     *
     * @param locationClient 定位客户端
     */
    public void initLocation(LocationClient locationClient);

    /**
     * 得到天气
     *
     * @param latitude 经度
     * @param longitude 纬度
     */
    public void getWeather(String latitude, String longitude, Callback callback);

    /**
     * 获取个人信息
     *
     */
    public void getPersonalInfo(String loginName, String password, Callback callback);
    /**
     * 提交个人信息
     *
     */
    public void pushPersonalInfo(String account, PersonalData personalData, Callback callback);
    /**
     * 查找数据库每天的数据
     *
     */
    public Cursor selectAlldayData();

    /**
     * 插入数据库每天的数据
     *
     */
    public void insertIntoAlldayData(ContentValues contentValues);
    /**
     * 插入数据库当前步数
     *
     */
    public void insertIntoStepData(String step);

}
