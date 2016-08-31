package com.sorry.band.activity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.github.ybq.android.spinkit.SpinKitView;
import com.sorry.api.ApiResponse;
import com.sorry.band.BandApplication;
import com.sorry.band.R;
import com.sorry.band.widget.MainToolbar;
import com.sorry.core.ActionCallbackListener;
import com.sorry.core.AppAction;
import com.sorry.core.ToastHanlder;
import com.sorry.model.PersonalData;
import com.sorry.model.ViewMessage;
import com.zhaoxiaodan.miband.ActionCallback;
import com.zhaoxiaodan.miband.MiBand;
import com.zhaoxiaodan.miband.listeners.HeartRateNotifyListener;
import com.zhaoxiaodan.miband.listeners.NotifyListener;
import com.zhaoxiaodan.miband.listeners.RealtimeStepsNotifyListener;
import com.zhaoxiaodan.miband.model.UserInfo;
import com.zhaoxiaodan.miband.model.VibrationMode;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends BaseActivity {
    private RelativeLayout panel1;
    private RelativeLayout panel2;
    private TextView stepNumTextView;
    private TextView stepTextView;
    private TextView heartrateTextView;
    private TextView sleepTextView;
    private TextView weatherTextView;
    private ImageView icWeather;
    private MainToolbar titleBar;
    private TextView titleView;
    private TextView connectTag;
    private ImageButton connectButtun;
    private SpinKitView connectAnimation;
    private LinearLayout scanResultLayout;
    private ScrollView scanResultScrollLayout;
    private ImageView heartRateIcon;
    private ImageButton highFrequencyButton;
    private ImageButton menuButton;
    private ImageButton showConnectLayoutButton;
    private EditText nameEditView;
    private EditText heightEditView;
    private EditText weightEditView;
    private EditText ageEditView;
    private ImageButton manCheckButton;
    private ImageButton femaleCheckButton;
    private CheckBox manCheckBox;
    private CheckBox femaleCheckBox;
    private EditText emPhoneEditView;
    private ImageButton inforConfirmButton;

    private RelativeLayout chartLayout;
    private LineChartView lineChart;

    private DrawerLayout menuLayout;

    private Typeface numFont;
    private final int CONNECTED = 0x03;
    private final int UPDATESTEPNUM = 0x04;
    private final int UPDATEHEARTRATENUM = 0x05;
    private HashMap devices = new HashMap();

    private RelativeLayout panel3;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

        initView();

        //initData();
    }

    private void initView(){
        panel1 = (RelativeLayout) findViewById(R.id.panel1);
        panel2 = (RelativeLayout) findViewById(R.id.panel2);
        panel3 = (RelativeLayout) findViewById(R.id.panel3);
        stepNumTextView = (TextView) findViewById(R.id.stepNumTextView);
        stepTextView = (TextView) findViewById(R.id.stepTextView);
        heartrateTextView = (TextView) findViewById(R.id.heartRateNumTextView);
        sleepTextView = (TextView) findViewById(R.id.sleepTextView);
        weatherTextView = (TextView) findViewById(R.id.weatherTextView);
        icWeather = (ImageView) findViewById(R.id.icWeather);
        titleBar = (MainToolbar) findViewById(R.id.mainToolBar);
        titleView = titleBar.getTitleView();
        highFrequencyButton = titleBar.getHighFrequencyButton();
        menuButton= titleBar.getMenuButton();
        heartRateIcon = (ImageView) findViewById(R.id.icHeartRate);
        scanResultLayout = (LinearLayout) findViewById(R.id.scanResultLayout);
        menuLayout = (DrawerLayout) findViewById(R.id.menuLayout);
        scanResultScrollLayout = (ScrollView) findViewById(R.id.scanResultScrollLayout);
        connectTag = (TextView) findViewById(R.id.connectTag);
        connectAnimation = (SpinKitView) findViewById(R.id.spin_kit);
        connectButtun = (ImageButton) findViewById(R.id.connectButton);
        nameEditView = (EditText)findViewById(R.id.nameEditView);
        heightEditView = (EditText)findViewById(R.id.heightEditView);
        weightEditView = (EditText)findViewById(R.id.weightEditView);
        ageEditView = (EditText)findViewById(R.id.ageEditView);
        manCheckButton = (ImageButton) findViewById(R.id.manCheckButton);
        femaleCheckButton = (ImageButton)findViewById(R.id.femaleCheckButton);
        manCheckBox = (CheckBox) findViewById(R.id.manCheckBox);
        femaleCheckBox = (CheckBox) findViewById(R.id.femaleCheckBox);
        emPhoneEditView = (EditText) findViewById(R.id.emPhoneEditView);
        inforConfirmButton = (ImageButton)findViewById(R.id.inforConfirmButton);
        showConnectLayoutButton = (ImageButton)findViewById(R.id.showConnectLayoutButton);
        chartLayout = (RelativeLayout)findViewById(R.id.chartLayout);

        numFont = Typeface.createFromAsset(getResources().getAssets(), "fonts/BEBAS.ttf");
        stepNumTextView.setTypeface(numFont,Typeface.NORMAL);
        sleepTextView.setTypeface(numFont,Typeface.NORMAL);
        heartrateTextView.setTypeface(numFont,Typeface.NORMAL);
        stepTextView.setTypeface(numFont,Typeface.NORMAL);
        weatherTextView.setTypeface(numFont,Typeface.NORMAL);
        connectTag.setTypeface(numFont,Typeface.NORMAL);
        lineChart = (LineChartView)findViewById(R.id.chart);

        toGetPersonalInfor();
        toGetWeather();
        initChart(lineChart);

        icWeather.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toGetWeather();
            }
        });

        panel3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                panel3.setVisibility(View.GONE);
            }
        });
        highFrequencyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HighFrequencyAvtivity.class);
                startActivity(intent);
            }
        });
        menuButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuLayout.openDrawer(findViewById(R.id.lv_left_menu));
            }
        });
        showConnectLayoutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
                if(blueadapter.isEnabled()) {
                    chartLayout.setVisibility(View.INVISIBLE);
                    showConnectLayoutButton.setVisibility(View.INVISIBLE);
                }
                else{
                    Toast.makeText(MainActivity.this, "Please turn on bluetooth", Toast.LENGTH_SHORT).show();
                }
            }
        });
        connectButtun.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                connectButtun.setVisibility(View.INVISIBLE);
                connectButtun.setActivated(false);
                connectAnimation.setVisibility(View.VISIBLE);
                connectTag.setText("Searching...");
                scanDevices();
            }
        });
        manCheckButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                manCheckBox.setChecked(true);
                femaleCheckBox.setChecked(false);
                manCheckButton.setBackgroundResource(R.mipmap.ic_checked);
                femaleCheckButton.setBackgroundResource(R.mipmap.ic_uncheck);
            }
        });
        femaleCheckButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                manCheckBox.setChecked(false);
                femaleCheckBox.setChecked(true);
                femaleCheckButton.setBackgroundResource(R.mipmap.ic_checked);
                manCheckButton.setBackgroundResource(R.mipmap.ic_uncheck);
            }
        });
        //*************特别注意此方法**********
        heartRateIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setBandListener(2);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    miBand.startHeartRateScan();
                }
                return true;
            }
        });
    }

    private void generateTestData(){
        List<ContentValues> contentValuesList = new ArrayList<>();
        for(int i = 1; i <= 5; i++) {
            ContentValues cValue = new ContentValues();
            cValue.put("date", "08.0"+i);
            cValue.put("heartrate", "8"+i);
            cValue.put("step", "1088");
            contentValuesList.add(cValue);
        }
        appAction.insertIntoAllBodyData(contentValuesList);
    }
/*
    private void insertHeartRate(String[] heartRateObj){
        Cursor cursor = db.rawQuery("select * from PerdayHeartRateData",null);
        cursor.moveToLast();
        if(cursor.getCount() != 0) {
            String date = cursor.getString(1);
            if(!date.equals(heartRateObj[1]+heartRateObj[2]+"")){
                insertAvrangeData(heartRateObj[1]+"."+heartRateObj[2]);
            }
        }
        ContentValues cValue = new ContentValues();
        cValue.put("time", heartRateObj[1]+heartRateObj[2]+"");
        cValue.put("heartrate", heartRateObj[0]+"");
        db.insert("PerdayHeartRateData", null, cValue);
    }

    private void insertStep(String[] stepObj){
        Cursor cursor = db.rawQuery("select * from PerdayStepData",null);
        cursor.moveToLast();
        if(cursor.getCount() != 0) {
            String date = cursor.getString(1);
            if(!date.equals(stepObj[1]+stepObj[2]+"")){
                insertAvrangeData(stepObj[1]+"."+stepObj[2]);
            }
        }
        ContentValues cValue = new ContentValues();
        cValue.put("time", stepObj[1]+stepObj[2]+"");
        cValue.put("step", stepObj[0]+"");
        db.insert("PerdayStepData", null, cValue);
    }

    private void insertAvrangeData(String date){
        List<Integer>heartRates = new ArrayList<Integer>();
        Cursor cursor = db.rawQuery("select * from PerdayHeartRateData",null);
        for(int i = 0; i < cursor.getCount(); i++){
            heartRates.add(Integer.valueOf(cursor.getString(2)));
        }
        int heartRate = 0;
        for(int j = 0; j < heartRates.size(); j++){
            heartRate += heartRates.get(j);
        }
        double avrangeHeartRate = (heartRate + 0.0)/(heartRates.size() + 0.0);
        Cursor cursor2 = db.rawQuery("select * from PerdayStepData",null);
        ContentValues cValue = new ContentValues();
        cValue.put("date", date);
        cValue.put("heartrate", avrangeHeartRate);
        cValue.put("step", cursor2.getString(2));
        db.insert("BodyData", null, cValue);
    }



    private List<String> getDateArray(){
        List<String> days = new ArrayList<String>();

        Calendar c = Calendar.getInstance();
        Log.i("Date",c.getTime().toString());
        c.add(Calendar.DAY_OF_MONTH, -6);
        for(int i = 0; i < 5; i++) {
            c.add(Calendar.DAY_OF_MONTH, 1);
            int month = c.get(Calendar.MONTH)+1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            days.add(month + "." + day);
        }
        return days;
    }

*/

    private void initChart(final LineChartView lineChart){
        lineChart.setInteractive(true);
        appAction.getAllBodyData(new ActionCallbackListener<Cursor>() {
            @Override
            public void onSuccess(Cursor cursor) {
                List<AxisValue> axisValues = new ArrayList<AxisValue>();
                List<PointValue> values = new ArrayList<PointValue>();
                cursor.moveToLast();
                int count = (cursor.getCount() > 5 ? 5 : cursor.getCount());
                for (int i = 0; i < count; i++) {
                    String heartRate = cursor.getString(1);
                    String date = cursor.getString(0);
                    values.add(new PointValue(i, Integer.valueOf(heartRate)));
                    axisValues.add(new AxisValue(i).setLabel(date));
                    cursor.moveToPrevious();
                }
                cursor.close();

                Line line = new Line(values).setColor(Color.parseColor("#5B6C86"));
                line.setStrokeWidth(3);
                line.setPointRadius(4);
                List<Line> lines = new ArrayList<Line>();
                lines.add(line);
                LineChartData data = new LineChartData();

                data.setLines(lines);

                Axis axisX = new Axis(axisValues);
                axisX.setHasSeparationLine(false);
                data.setAxisXBottom(axisX);
                axisX.setTextColor(Color.parseColor("#161D37"));
                axisX.setTypeface(numFont);

                Axis axisY = new Axis();
                axisY.setMaxLabelChars(3);
                axisY.setTextColor(Color.parseColor("#161D37"));
                axisY.setTypeface(numFont);
                data.setAxisYLeft(axisY);
                lineChart.setViewportCalculationEnabled(false);

                Viewport v = new Viewport(0, 220, 4.5f, 0);
                lineChart.setMaximumViewport(v);
                lineChart.setCurrentViewport(v);

                lineChart.setZoomType(ZoomType.HORIZONTAL);
                lineChart.setValueSelectionEnabled(true);
                lineChart.setLineChartData(data);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                Log.i("INITCHART", errorEvent+message);
                LineChartData data = new LineChartData();
                Axis axisY = new Axis();
                axisY.setMaxLabelChars(3);
                axisY.setTextColor(Color.parseColor("#161D37"));
                axisY.setTypeface(numFont);
                data.setAxisYLeft(axisY);
                lineChart.setViewportCalculationEnabled(false);

                Viewport v = new Viewport(0, 220, 4.5f, 0);
                lineChart.setMaximumViewport(v);
                lineChart.setCurrentViewport(v);

                lineChart.setZoomType(ZoomType.HORIZONTAL);
                lineChart.setValueSelectionEnabled(true);
                lineChart.setLineChartData(data);
            }
        });

    }

    private void setBandListener(int type){
        if(type == 1) {
            Log.i("setRealtimeSteps","OK");
            miBand.setRealtimeStepsNotifyListener(new RealtimeStepsNotifyListener() {
                @Override
                public void onNotify(int steps) {
                    Log.i("WalkStep", "RealtimeStepsNotifyListener:" + steps);
                    Message msg = new Message();
                    msg.what = UPDATESTEPNUM;
                    Calendar c = Calendar.getInstance();
                    String[] obj = {steps+"", (c.get(Calendar.MONTH)+1)+"", c.get(Calendar.DAY_OF_MONTH)+"", c.get(Calendar.HOUR_OF_DAY)+"", c.get(Calendar.MINUTE)+""};
                    msg.obj =  obj;
                    //mainHandler.sendMessage(msg);
                }
            });
            miBand.enableRealtimeStepsNotify();
        }
        if(type == 2) {
            Log.i("setHeartRate","OK");
            miBand.setHeartRateScanListener(new HeartRateNotifyListener() {
                @Override
                public void onNotify(int heartRate) {
                    Log.i("HeartRate", "heart rate: " + heartRate);
                    Calendar c = Calendar.getInstance();
                    Message msg = new Message();
                    msg.what = UPDATEHEARTRATENUM;
                    String[] obj = {heartRate+"", (c.get(Calendar.MONTH)+1)+"", c.get(Calendar.DAY_OF_MONTH)+"", c.get(Calendar.HOUR_OF_DAY)+"", c.get(Calendar.MINUTE)+""};
                    msg.obj =  obj;
                    //mainHandler.sendMessage(msg);
                }
            });
        }


    }

    private void toGetWeather(){
        appAction.getWeather(new ActionCallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                uiAction.setText(new ViewMessage<TextView, String>(weatherTextView, data));
            }

            @Override
            public void onFailure(String errorEvent, String message) {
            }
        });
    }

    private void toGetPersonalInfor(){
        String loginName = application.getAccount();
        String pwd = application.getPwd();
        appAction.getPersonalInfo(loginName, pwd, new ActionCallbackListener<ApiResponse<PersonalData>>() {
            @Override
            public void onSuccess(ApiResponse<PersonalData> data) {
                Log.i("GET", data.getEvent()+data.getMsg());
                application.setPersonalData(data.getObj());
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(errorEvent.equals("103")){
                    Log.i("GET", errorEvent+message);
                    uiAction.changeVisiable(panel2);
                }
            }
        });
    }

    public void toPushPersonalInfor(View view){
        int sex;
        if(manCheckBox.isChecked()){
            sex = 1;
        }
        else{
            sex = 2;
        }
        PersonalData personalData = new PersonalData(
                sex,
                Integer.valueOf(ageEditView.getText().toString()),
                Integer.valueOf(heightEditView.getText().toString()),
                Integer.valueOf(weightEditView.getText().toString()),
                nameEditView.getText().toString(),
                emPhoneEditView.getText().toString()
        );
        application.setPersonalData(personalData);
        appAction.pushPersonalInfo(application.getAccount(), personalData, new ActionCallbackListener<ApiResponse<Void>>() {
            @Override
            public void onSuccess(ApiResponse<Void> data) {
                appAction.showToast("提交个人信息成功！", Toast.LENGTH_SHORT, toastHanlder);
                uiAction.changeVisiable(panel2);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                appAction.showToast(message, Toast.LENGTH_SHORT, toastHanlder);
            }
        });
    }

    private void scanDevices(){
        final ScanCallback scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                BluetoothDevice device = result.getDevice();
                Log.i("Bluetooth",device.getName());
                Log.i("Bluetooth","name:" + device.getName() +
                        ",uuid:"+ device.getUuids() +
                        ",add:"+ device.getAddress() +
                        ",type:"+ device.getType() +
                        ",bondState:" + device.getBondState() +
                        ",rssi:" + result.getRssi());
                String item = device.getName() + "|" + device.getAddress();
                if (!devices.containsKey(item)) {
                    devices.put(item, device);
                    TextView textView = new TextView(MainActivity.this);
                    textView.setText(device.getName() + " " + device.getAddress());
                    textView.setGravity(Gravity.CENTER);
                    Typeface numFont = Typeface.createFromAsset(getResources().getAssets(), "fonts/BEBAS.ttf");
                    textView.setTypeface(numFont);
                    textView.setTextSize(16);
                    textView.setOnClickListener(new onDeviceClickListener(device));
                    scanResultLayout.addView(textView);
                    connectTag.setVisibility(View.INVISIBLE);
                    connectAnimation.setVisibility(View.INVISIBLE);
                    scanResultScrollLayout.setVisibility(View.VISIBLE);
                }
            }
        };
        Log.i("Bluetoth","StartScanning");
        MiBand.startScan(scanCallback);
    }

    private class onDeviceClickListener implements OnClickListener{
        BluetoothDevice device;
        public onDeviceClickListener(BluetoothDevice device){
            this.device = device;
        }
        @Override
        public void onClick(View v) {
            Log.i("Miband","Connecting...");

            miBand.connect(device, new ActionCallback() {            //device参数是扫描时获得的蓝牙设备选中的
                @Override
                public void onSuccess(Object data)                   //连接成功的方法，提示用户连接成功
                {
                    miBand.startVibration(VibrationMode.VIBRATION_WITH_LED);
                    initBand();
                    Log.i("Miband","连接成功");

                }

                @Override
                public void onFail(int errorCode, String msg)        //连接失败的方法
                {
                    Log.i("Miband","连接失败, code:"+errorCode+",mgs:"+msg);
                    if(chartLayout.getVisibility() == View.VISIBLE){
                        showConnectLayoutButton.setVisibility(View.VISIBLE);
                    }
                }
            });

            // 设置断开监听器, 方便在设备断开的时候进行重连或者别的处理，可写在连接成功的方法里
            miBand.setDisconnectedListener(new NotifyListener()
            {
                @Override
                public void onNotify(byte[] data)
                {
                    Log.i("Miband","连接断开!!!");
                    if(chartLayout.getVisibility() == View.VISIBLE){
                        uiAction.changeVisiable(showConnectLayoutButton);
                    }
                }
            });
        }
    }

    private void initBand() {
        UserInfo userInfo = new UserInfo(
                Integer.valueOf(application.getUid()),
                application.getPersonalData().getSex(),
                application.getPersonalData().getAge(),
                application.getPersonalData().getHeight(),
                application.getPersonalData().getWeight(),
                application.getPersonalData().getName(),
                application.getType()
        );
        miBand.setUserInfo(userInfo);
        toSetStepListener();
        miBand.pair(new ActionCallback() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onFail(int errorCode, String msg) {
                Log.i("Pair", errorCode+msg);
            }
        });
        uiAction.changeVisiable(chartLayout);
    }

    private void toSetStepListener(){
        appAction.setStepListener(miBand, new ActionCallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                uiAction.setText(new ViewMessage<TextView, String>(stepNumTextView, data));
            }

            @Override
            public void onFailure(String errorEvent, String message) {

            }
        });

    }
}
