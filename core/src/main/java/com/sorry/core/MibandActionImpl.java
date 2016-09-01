package com.sorry.core;

import android.os.Message;

import com.sorry.model.MibandMessage;
import com.zhaoxiaodan.miband.MiBand;
import com.zhaoxiaodan.miband.model.UserInfo;

/**
 * Created by sorry on 9/1/16.
 */
public class MibandActionImpl implements MibandAction {

    private MiBandHanlder miBandHanlder;

    public MibandActionImpl(MiBandHanlder miBandHanlder){
        this.miBandHanlder = miBandHanlder;
    }

    @Override
    public void setBand(int method, MibandMessage mibandMessage) {
        Message msg = new Message();
        msg.what = method;
        msg.obj = mibandMessage;
        miBandHanlder.sendMessage(msg);
    }
}
