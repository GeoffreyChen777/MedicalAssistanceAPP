package com.sorry.core;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sorry.model.MibandMessage;
import com.zhaoxiaodan.miband.ActionCallback;
import com.zhaoxiaodan.miband.MiBand;
import com.zhaoxiaodan.miband.listeners.HeartRateNotifyListener;
import com.zhaoxiaodan.miband.listeners.NotifyListener;
import com.zhaoxiaodan.miband.listeners.RealtimeStepsNotifyListener;
import com.zhaoxiaodan.miband.model.UserInfo;

/**
 * Created by sorry on 9/1/16.
 */
public class MiBandHanlder extends Handler{
    public final static int INIT_BAND = 0X90;
    public final static int SET_STEP_LISTENER = 0X91;
    public final static int SET_HEART_LISTENER = 0X92;
    public final static int START_HEART_SCAN = 0X93;
    public final static int START_NORMAL_LISTENER = 0X94;
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case INIT_BAND:{
                MibandMessage<UserInfo, Void, MiBand> initMsg = (MibandMessage<UserInfo, Void, MiBand>)msg.obj;
                initBand(initMsg.getMiBand(), initMsg.getUserInfo());
                break;
            }
            case SET_STEP_LISTENER:{
                MibandMessage<Void, RealtimeStepsNotifyListener, MiBand> stepListenerMsg = (MibandMessage<Void, RealtimeStepsNotifyListener, MiBand>)msg.obj;
                setStepListener(stepListenerMsg.getMiBand(), stepListenerMsg.getListener());
                break;
            }
            case SET_HEART_LISTENER:{
                MibandMessage<Void, HeartRateNotifyListener, MiBand> heartListenerMsg = (MibandMessage<Void, HeartRateNotifyListener, MiBand>)msg.obj;
                setHeartListener(heartListenerMsg.getMiBand(), heartListenerMsg.getListener());
                break;
            }
            case START_HEART_SCAN:{
                Log.i("TIDSTART_HEART_SCAN",android.os.Process.myTid()+"");
                MibandMessage<Void, Void, MiBand> startMsg = (MibandMessage<Void, Void, MiBand>)msg.obj;
                startMsg.getMiBand().startHeartRateScan();
                break;
            }
            case START_NORMAL_LISTENER:{
                MibandMessage<Void, NotifyListener, MiBand> notifyMsg = (MibandMessage<Void, NotifyListener, MiBand>)msg.obj;
                setNotifyListener(notifyMsg.getMiBand(), notifyMsg.getListener());
                break;
            }
        }
    }

    private void initBand(MiBand miBand, final UserInfo userInfo) {

        Log.i("TIDInit",android.os.Process.myTid()+"");
        Log.i("UserInfo",userInfo.getUid()+"");
        Log.i("UserInfo",userInfo.getGender()+"");
        Log.i("UserInfo",userInfo.getAge()+"");
        Log.i("UserInfo",userInfo.getHeight()+"");
        Log.i("UserInfo",userInfo.getWeight()+"");
        Log.i("UserInfo",userInfo.getAlias()+"");
        Log.i("UserInfo",userInfo.getType()+"");
        miBand.setUserInfo(userInfo);
    }

    private void setStepListener(MiBand miBand, RealtimeStepsNotifyListener stepsNotifyListener){
        Log.i("TIDsetStepListener",android.os.Process.myTid()+"");
        Log.i("setStepLi","setting");
        miBand.setRealtimeStepsNotifyListener(stepsNotifyListener);
        miBand.enableRealtimeStepsNotify();
        Log.i("setStepLi","Done");
    }
    private void setHeartListener(MiBand miBand, HeartRateNotifyListener heartRateNotifyListener){
        Log.i("TIDsetHeartListener",android.os.Process.myTid()+"");
        Log.i("setHeartLi","setting");
        miBand.setHeartRateScanListener(heartRateNotifyListener);
        Log.i("setHeartLi","Done");
    }
    private void setNotifyListener(MiBand miband, NotifyListener notifyListener){
        miband.setNormalNotifyListener(notifyListener);
    }
}
