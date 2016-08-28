package com.sorry.band.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.sorry.band.receiver.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by sorry on 2016/8/22.
 */
public class ScanHeartrateService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("ScanHeartRateService", "executed at " + new Date().
                        toString());
                SQLiteDatabase db = openOrCreateDatabase("BodyData.db", Context.MODE_PRIVATE, null);
                List<Integer> heartRates = new ArrayList<Integer>();
                Cursor cursor = db.rawQuery("select * from PerdayHeartRateData",null);
                for(int i = 0; i < cursor.getCount(); i++){
                    heartRates.add(Integer.valueOf(cursor.getString(2)));
                }
                int heartRate = 0;
                for(int j = 0; j < heartRates.size(); j++){
                    heartRate += heartRates.get(j);
                }
                double avrangeHeartRate = (heartRate + 0.0) / (heartRates.size() + 0.0);
                Cursor cursor2 = db.rawQuery("select * from PerdayStepData",null);
                ContentValues cValue = new ContentValues();
                Calendar c = Calendar.getInstance();
                String date = (c.get(Calendar.MONTH)+1)+"."+(c.get(Calendar.DAY_OF_MONTH)+1);
                cValue.put("date", date);
                cValue.put("heartrate", avrangeHeartRate);
                cValue.put("step", cursor2.getString(2));
                db.insert("BodyData", null, cValue);

            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anDay = 24 * 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anDay;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

}
