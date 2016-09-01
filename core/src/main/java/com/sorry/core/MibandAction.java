package com.sorry.core;

import com.sorry.model.MibandMessage;
import com.zhaoxiaodan.miband.MiBand;
import com.zhaoxiaodan.miband.listeners.HeartRateNotifyListener;
import com.zhaoxiaodan.miband.listeners.RealtimeStepsNotifyListener;
import com.zhaoxiaodan.miband.model.UserInfo;

/**
 * Created by sorry on 9/1/16.
 */
public interface MibandAction {
    public void setBand(int method, MibandMessage mibandMessage);
}
