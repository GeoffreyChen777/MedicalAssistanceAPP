package com.sorry.core;

import android.util.Log;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by sorry on 8/29/16.
 */
public class SMSEventHandler extends EventHandler {
    private final static String SMS_SUCCESS_EVENT = "SENT_SMS_CODE_SECCESS";
    private final static String SMS_SECCESS_EVENT_MESSAGE = "发送验证码成功";

    private ActionCallbackListener<Integer> actionCallbackListener;


    public SMSEventHandler(ActionCallbackListener<Integer> actionCallbackListener){
        this.actionCallbackListener = actionCallbackListener;
    }

    @Override
    public void afterEvent(int event, int result, Object data) {

        if (result == SMSSDK.RESULT_COMPLETE) {
            //回调完成
            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                //提交验证码成功
                actionCallbackListener.onSuccess(SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE);
            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                //获取验证码成功
                actionCallbackListener.onSuccess(SMSSDK.EVENT_GET_VERIFICATION_CODE);

            }
        } else {
            ((Throwable)data).printStackTrace();
            actionCallbackListener.onFailure(ErrorEvent.PARAM_NULL, "验证码错误或尝试过多");

        }
    }
}
