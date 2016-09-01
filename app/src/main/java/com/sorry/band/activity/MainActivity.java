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
import com.sorry.core.MiBandHanlder;
import com.sorry.core.MibandAction;
import com.sorry.core.ToastHanlder;
import com.sorry.model.MibandMessage;
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
import java.util.Arrays;
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
        heartRateIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.i("HeartRate", "down");
                    Log.i("TID",android.os.Process.myTid()+"");
                    HeartRateNotifyListener heartRateNotifyListener = new HeartRateNotifyListener() {
                        @Override
                        public void onNotify(int heartRate) {
                            Log.i("HeartRate", "heart rate: " + heartRate);
                            Calendar c = Calendar.getInstance();
                            String hour = c.get(Calendar.HOUR_OF_DAY)+"";
                            String minute = c.get(Calendar.MINUTE)+"";
                            uiAction.setText(new ViewMessage<TextView, String>(heartrateTextView, heartRate+"/MIN "+hour+":"+minute));
                            MibandMessage<Void, RealtimeStepsNotifyListener, MiBand> setStepMsg = new MibandMessage<Void, RealtimeStepsNotifyListener, MiBand>(null, new RealtimeStepsNotifyListener() {
                                @Override
                                public void onNotify(int steps) {
                                    uiAction.setText(new ViewMessage<TextView, String>(stepNumTextView, steps+""));
                                    Log.i("Strp", steps+"");

                                }
                            }, miBand);
                            mibandAction.setBand(MiBandHanlder.SET_STEP_LISTENER, setStepMsg);
                        }
                    };
                    MibandMessage<Void, HeartRateNotifyListener, MiBand> heartMsg = new MibandMessage<Void, HeartRateNotifyListener, MiBand>(null, heartRateNotifyListener, miBand);
                    mibandAction.setBand(MiBandHanlder.SET_HEART_LISTENER, heartMsg);
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    Log.i("HeartRate", "up");
                    Log.i("TID",android.os.Process.myTid()+"");
                    MibandMessage<Void, Void, MiBand> startMsg = new MibandMessage<Void, Void, MiBand>(null, null, miBand);
                    mibandAction.setBand(MiBandHanlder.START_HEART_SCAN, startMsg);
                }
                return true;
            }
        });
    }



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
            Log.i("TIDConnecting",android.os.Process.myTid()+"");
            miBand.connect(device, new ActionCallback() {            //device参数是扫描时获得的蓝牙设备选中的
                @Override
                public void onSuccess(Object data)                   //连接成功的方法，提示用户连接成功
                {
                    Log.i("TIDConnected",android.os.Process.myTid()+"");
                    //miBand.startVibration(VibrationMode.VIBRATION_WITH_LED);


                    UserInfo userInfo = new UserInfo(
                            20261234,
                        application.getPersonalData().getSex(),
                        application.getPersonalData().getAge(),
                        application.getPersonalData().getHeight(),
                        application.getPersonalData().getWeight(),
                        application.getPersonalData().getName(),
                        application.getType()
                    );
                    MibandMessage<UserInfo, Void, MiBand> initMsg = new MibandMessage<UserInfo, Void, MiBand>(userInfo, null, miBand);
                    mibandAction.setBand(MiBandHanlder.INIT_BAND, initMsg);

                    uiAction.changeVisiable(chartLayout);
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

    public void toStartPostActivity(View view){
        Intent intent = new Intent(MainActivity.this, PostActivity.class);
        startActivity(intent);
    }
}
