package com.sorry.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.View;

import com.baidu.location.LocationClient;
import com.sorry.api.ApiResponse;
import com.sorry.model.PersonalData;
import com.zhaoxiaodan.miband.MiBand;

import java.util.List;


public interface AppAction {
    public void showToast(String msg, int aloneTime, ToastHanlder hanlder);
    // 发送手机验证码
    public void sendSmsCode(String phoneNum, ActionCallbackListener<Integer> listener);
    // 验证手机验证码
    public void verifySmsCode(String phoneNum, String code, String password, ActionCallbackListener<Integer> listener);
    // 注册
    public void register(String phoneNum, String password, ActionCallbackListener<Void> registerListener);
    // 登录
    public void login(final String loginName, final String password, ActionCallbackListener<Void> listener);
    // 初始化地图定位
    public void getLocation(final ActionCallbackListener<List<Double>> listener);
    // 获取天气
    public void getWeather(final ActionCallbackListener<String> listener);
    // 获取个人信息
    public void getPersonalInfo(String phoneNum, String password, ActionCallbackListener<ApiResponse<PersonalData>> listener);
    //提交个人信息
    public void pushPersonalInfo(String account, PersonalData personalData, ActionCallbackListener<ApiResponse<Void>> listener);
    // 搜索全部身体信息
    public void getAllBodyData(ActionCallbackListener<Cursor> listener);
    // 插入身体信息数据
    public void insertIntoAllBodyData(List<ContentValues> contentValues);
    // 删除所有数据
    public void deleteAlldata(String table);
    // 测试数据函数
    public void exec(String sql);
    // 插入当前步数
    public void insertIntoPerdayStepData(String step);
    // 插入当前心跳
    public void insertIntoPerdayHeartrateData(String heartRate);
    // 发布帖子
    public void pushPost(String account, String title, String content, boolean isWithInfo, ActionCallbackListener<Void> listener);

}
