package com.sorry.band.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sorry.band.service.ScanHeartrateService;

public class AlarmReceiver extends BroadcastReceiver {
    //重写onReceive方法
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, ScanHeartrateService.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(i);
    }

}