package com.sorry.band.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.github.ybq.android.spinkit.SpinKitView;
import com.sorry.band.BandApplication;
import com.sorry.band.R;
import com.sorry.band.widget.MainToolbar;
import com.sorry.band.widget.MainToolbar1;
import com.zhaoxiaodan.miband.ActionCallback;
import com.zhaoxiaodan.miband.MiBand;
import com.zhaoxiaodan.miband.listeners.HeartRateNotifyListener;
import com.zhaoxiaodan.miband.listeners.NotifyListener;
import com.zhaoxiaodan.miband.listeners.RealtimeStepsNotifyListener;
import com.zhaoxiaodan.miband.model.UserInfo;
import com.zhaoxiaodan.miband.model.VibrationMode;

import android.app.Activity;
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
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends Activity {
    private RelativeLayout panel1;
    private RelativeLayout panel2;
    private LinearLayout postScallLayout;
    private ScaleAnimation scaleAnimation;
    private MainToolbar1 postToolBar;
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
    private ImageButton inforConfirmButton;

    private RelativeLayout chartLayout;
    private LineChartView lineChart;

    private DrawerLayout menuLayout;

    private Typeface numFont;
    private final int WEATHERLOCATED = 0x01;
    private final int WEATHERGETTED = 0x02;
    private final int CONNECTED = 0x03;
    private final int UPDATESTEPNUM = 0x04;
    private final int UPDATEHEARTRATENUM = 0x05;
    private final int PERSONALINFORRESPONSE = 0x06;

    protected final int PUBLISH = 1;
    protected final int NORMAL_POST = 1;
    public static String name="Alice";
    public static int flag=0;
    public LocationClient mLocationClient = null;
    private OkHttpClient mClient;
    private MiBand miband;
    private HashMap devices = new HashMap();
    private SQLiteDatabase db;

    private RelativeLayout panel3;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
        panel1 = (RelativeLayout) findViewById(R.id.panel1);
        panel2 = (RelativeLayout) findViewById(R.id.panel2);
        panel3 = (RelativeLayout) findViewById(R.id.panel3);
        panel3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                panel3.setVisibility(View.GONE);
            }
        });
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
        highFrequencyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HighFrequencyAvtivity.class);
                startActivity(intent);
            }
        });
        menuLayout = (DrawerLayout) findViewById(R.id.menuLayout);
        menuButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuLayout.openDrawer(findViewById(R.id.lv_left_menu));
            }
        });
        scanResultScrollLayout = (ScrollView) findViewById(R.id.scanResultScrollLayout);
        connectTag = (TextView) findViewById(R.id.connectTag);
        connectAnimation = (SpinKitView) findViewById(R.id.spin_kit);
        connectButtun = (ImageButton) findViewById(R.id.connectButton);
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

        nameEditView = (EditText)findViewById(R.id.nameEditView);
        heightEditView = (EditText)findViewById(R.id.heightEditView);
        weightEditView = (EditText)findViewById(R.id.weightEditView);
        ageEditView = (EditText)findViewById(R.id.ageEditView);
        manCheckButton = (ImageButton) findViewById(R.id.manCheckButton);
        femaleCheckButton = (ImageButton)findViewById(R.id.femaleCheckButton);
        inforConfirmButton = (ImageButton)findViewById(R.id.inforConfirmButton);
        showConnectLayoutButton = (ImageButton)findViewById(R.id.showConnectLayoutButton);
        chartLayout = (RelativeLayout)findViewById(R.id.chartLayout);

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

        numFont = Typeface.createFromAsset(getResources().getAssets(), "fonts/BEBAS.ttf");
        stepNumTextView.setTypeface(numFont,Typeface.NORMAL);
        sleepTextView.setTypeface(numFont,Typeface.NORMAL);
        heartrateTextView.setTypeface(numFont,Typeface.NORMAL);
        stepTextView.setTypeface(numFont,Typeface.NORMAL);
        weatherTextView.setTypeface(numFont,Typeface.NORMAL);
        connectTag.setTypeface(numFont,Typeface.NORMAL);

        mClient = ((BandApplication)getApplication()).getOkHttpClient();
        getPersonalInfor();
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(new MyLocationListener());    //注册监听函数
        initLocation();
        mLocationClient.start();
        icWeather.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("GPS","========");
                mLocationClient.start();
                if(mLocationClient.isStarted()){
                    Log.i("GPS","on");
                }
                else{
                    Log.i("GPS","off");
                }
            }
        });

        this.miband = ((BandApplication)getApplication()).getMiBand();

        db = openOrCreateDatabase("BodyData.db", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS BodyData (date VARCHAR PRIMARY KEY, heartrate VARCHAR, step VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS PerdayHeartRateData (no INTEGER PRIMARY KEY autoincrement,time VARCHAR, heartrate VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS PerdayStepData (no INTEGER PRIMARY KEY autoincrement,time VARCHAR, step VARCHAR)");
        //generateTestData();
        lineChart = (LineChartView)findViewById(R.id.chart);
        initChart();

        heartRateIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setBandListener(2);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    miband.startHeartRateScan();
                }
                return true;
            }
        });

        sleepTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = db.rawQuery("select * from PerdayHeartRateData",null);
                Log.i("dbcount",cursor.getCount()+"");
                cursor.moveToLast();
                String heartRate = cursor.getString(2);
                String date = cursor.getString(1);
                Log.i("DB", heartRate + " " +date);
            }
        });
    }

    private void generateTestData(){
        String sql = "delete from BodyData";
        db.execSQL(sql);
        /*for(int i =0; i < 5; i++) {
            ContentValues cValue = new ContentValues();
            cValue.put("date", "08.0"+i);
            cValue.put("heartrate", "8"+i);
            cValue.put("step", "1088");
            db.insert("BodyData", null, cValue);
        }*/
    }

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

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
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



    private void initChart(){
        lineChart.setInteractive(true);
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        Cursor cursor = db.rawQuery("select * from BodyData",null);
        Log.i("dbcount",cursor.getCount()+"");
        if(cursor.getCount() == 0){
            panel3.setVisibility(View.VISIBLE);
        }
        cursor.moveToLast();
        int count = (cursor.getCount()>5?5:cursor.getCount());
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

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 220, 4.5f, 0);
        lineChart.setMaximumViewport(v);
        lineChart.setCurrentViewport(v);

        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setValueSelectionEnabled(true);
        lineChart.setLineChartData(data);
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
            Log.i("Connect",device.getName());
            this.device = device;
        }
        @Override
        public void onClick(View v) {
            Log.i("Connect","===");
            miband.connect(device, new ActionCallback() {            //device参数是扫描时获得的蓝牙设备选中的
                @Override
                public void onSuccess(Object data)                   //连接成功的方法，重写此方法，来提示用户连接成功
                {
                    miband.startVibration(VibrationMode.VIBRATION_WITH_LED);
                    Message msg = new Message();
                    msg.what = CONNECTED;
                    mainHandler.sendMessage(msg);
                    Log.i("Device","连接成功");
                }

                @Override
                public void onFail(int errorCode, String msg)        //连接失败的方法
                {
                    Log.i("Device","连接失败, code:"+errorCode+",mgs:"+msg);
                    if(chartLayout.getVisibility() == View.VISIBLE){
                        showConnectLayoutButton.setVisibility(View.VISIBLE);
                    }
                }
            });

            // 设置断开监听器, 方便在设备断开的时候进行重连或者别的处理，可写在连接成功的方法里
            miband.setDisconnectedListener(new NotifyListener()
            {
                @Override
                public void onNotify(byte[] data)
                {
                    Log.i("Device","连接断开!!!");

                    if(chartLayout.getVisibility() == View.VISIBLE){
                        showConnectLayoutButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }


    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            String locationStr = "" + location.getLongitude() + "," + location.getLatitude();
            StringBuffer sb = new StringBuffer(256);
            Message msg = new Message();
            msg.what = WEATHERLOCATED;
            msg.obj = locationStr;
            mainHandler.sendMessage(msg);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
        }
    }


    private void refreshStep(String[] stepsObj){
        stepNumTextView.setText(stepsObj[0]);
    }

    private void refreshHeartRate(String[] heartRateObj){
        heartrateTextView.setText(heartRateObj[0] + "/MIN " + heartRateObj[3] + ":" + heartRateObj[4]);
        setBandListener(1);
    }

    private void initBand(){
        if(((BandApplication)getApplication()).isUnInited()) {
            Log.i("INIT","false");
        }
        else{
            Log.i("INIT","true");
        }

        if(((BandApplication)getApplication()).isUnInited()){

            panel2.setVisibility(View.VISIBLE);
            manCheckButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BandApplication)getApplication()).setSex(1);
                    manCheckButton.setBackgroundResource(R.mipmap.ic_checked);
                    femaleCheckButton.setBackgroundResource(R.mipmap.ic_uncheck);
                }
            });
            femaleCheckButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BandApplication)getApplication()).setSex(2);
                    femaleCheckButton.setBackgroundResource(R.mipmap.ic_checked);
                    manCheckButton.setBackgroundResource(R.mipmap.ic_uncheck);
                }
            });
            inforConfirmButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!nameEditView.getText().toString().equals("")
                            && !heightEditView.getText().toString().equals("")
                            && !weightEditView.getText().toString().equals("")
                            && !ageEditView.getText().toString().equals("")) {
                        ((BandApplication)getApplication()).setName(nameEditView.getText().toString());
                        ((BandApplication) getApplication()).setHeight(Integer.valueOf(heightEditView.getText().toString()));
                        ((BandApplication) getApplication()).setWeight(Integer.valueOf(weightEditView.getText().toString()));
                        ((BandApplication) getApplication()).setAge(Integer.valueOf(ageEditView.getText().toString()));
                        ((BandApplication) getApplication()).setInit(true);
                    }
                    panel2.setVisibility(View.INVISIBLE);
                    UserInfo userInfo = new UserInfo(Integer.valueOf(((BandApplication)getApplication()).getUid()),
                            ((BandApplication)getApplication()).getSex(),
                            ((BandApplication)getApplication()).getAge(),
                            ((BandApplication)getApplication()).getHeight(),
                            ((BandApplication)getApplication()).getWeight(),
                            ((BandApplication)getApplication()).getName(),
                            ((BandApplication)getApplication()).getType());
                    miband.setUserInfo(userInfo);
                    chartLayout.setVisibility(View.VISIBLE);
                    setBandListener(2);
                    sendPinfortoServer();
                }
            });
        }
        else{
            UserInfo userInfo = new UserInfo(Integer.valueOf(((BandApplication)getApplication()).getUid()),
                    ((BandApplication)getApplication()).getSex(),
                    ((BandApplication)getApplication()).getAge(),
                    ((BandApplication)getApplication()).getHeight(),
                    ((BandApplication)getApplication()).getWeight(),
                    ((BandApplication)getApplication()).getName(),
                    ((BandApplication)getApplication()).getType());
            miband.setUserInfo(userInfo);
            setBandListener(2);
            chartLayout.setVisibility(View.VISIBLE);
        }

    }

    private void sendPinfortoServer(){
        String url = "http://115.159.200.151/personalInfor.php";
        RequestBody formBody = new FormBody.Builder()
                .add("type", "set")
                .add("name", ((BandApplication)getApplication()).getAccount())
                .add("password", MD5(((BandApplication)getApplication()).getPwd()))
                .add("sex", String.valueOf(((BandApplication)getApplication()).getSex()))
                .add("height", String.valueOf(((BandApplication)getApplication()).getHeight()))
                .add("weight", String.valueOf(((BandApplication)getApplication()).getWeight()))
                .add("age", String.valueOf(((BandApplication)getApplication()).getAge()))
                .add("nickname", String.valueOf(((BandApplication)getApplication()).getName()))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("Error","error");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.i("Error","Unexpected code " + response);
                    throw new IOException("Unexpected code " + response);

                }
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    Log.i("Response",res);
                    JSONObject result = JSONObject.parseObject(res);
                    String code = result.getString("value");
                    Message message = new Message();
                    message.what = PERSONALINFORRESPONSE;
                    message.obj = code;
                    mainHandler.sendMessage(message);
                }
            }
        });
    }

    private void setBandListener(int type){
        if(type == 1) {
            Log.i("setRealtimeSteps","OK");
            miband.setRealtimeStepsNotifyListener(new RealtimeStepsNotifyListener() {
                @Override
                public void onNotify(int steps) {
                    Log.i("WalkStep", "RealtimeStepsNotifyListener:" + steps);
                    Message msg = new Message();
                    msg.what = UPDATESTEPNUM;
                    Calendar c = Calendar.getInstance();
                    String[] obj = {steps+"", (c.get(Calendar.MONTH)+1)+"", c.get(Calendar.DAY_OF_MONTH)+"", c.get(Calendar.HOUR_OF_DAY)+"", c.get(Calendar.MINUTE)+""};
                    msg.obj =  obj;
                    mainHandler.sendMessage(msg);
                }
            });
            miband.enableRealtimeStepsNotify();
        }
        if(type == 2) {
            Log.i("setHeartRate","OK");
            miband.setHeartRateScanListener(new HeartRateNotifyListener() {
                @Override
                public void onNotify(int heartRate) {
                    Log.i("HeartRate", "heart rate: " + heartRate);
                    Calendar c = Calendar.getInstance();
                    Message msg = new Message();
                    msg.what = UPDATEHEARTRATENUM;
                    String[] obj = {heartRate+"", (c.get(Calendar.MONTH)+1)+"", c.get(Calendar.DAY_OF_MONTH)+"", c.get(Calendar.HOUR_OF_DAY)+"", c.get(Calendar.MINUTE)+""};
                    msg.obj =  obj;
                    mainHandler.sendMessage(msg);
                }
            });
        }


        /**/
    }

    private void getWeather(String location){
        String url = "http://api.map.baidu.com/telematics/v3/weather?location=" + location + "&output=json&ak=6tYzTvGZSOpYB5Oc2YGGOKt8";
        Request request = new Request.Builder()
                .url(url)
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("Error","error");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.i("Error","Unexpected code " + response);
                    throw new IOException("Unexpected code " + response);

                }
                if (response.isSuccessful()) {
                    Message message = new Message();
                    String responseStr = response.body().string();
                    message.what = WEATHERGETTED;
                    message.obj = responseStr;
                    mainHandler.sendMessage(message);
                    Log.i("Response", responseStr);
                }
            }
        });
    }

    private void refreshWeather(String weatherJson){
        JSONObject jsonObj = JSONObject.parseObject(weatherJson);
        JSONObject jsonObj2 = jsonObj.getJSONArray("results").getJSONObject(0);
        JSONArray weatherJsonArray = jsonObj2.getJSONArray("weather_data");
        JSONObject weatherObj = weatherJsonArray.getJSONObject(0);
        String weather = weatherObj.getString("weather");
        String wind = weatherObj.getString("wind");
        String temperature = weatherObj.getString("temperature");
        weatherTextView.setText(weather + "，" + wind + "，" + temperature);
    }

    private void getPersonalInfor(){
        String url = "http://115.159.200.151/personalInfor.php";
        RequestBody formBody = new FormBody.Builder()
                .add("type", "get")
                .add("name", ((BandApplication)getApplication()).getAccount())
                .add("password", MD5(((BandApplication)getApplication()).getPwd()))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("Error","error");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (!response.isSuccessful()) {
                    Log.i("Error","Unexpected code " + response);
                    throw new IOException("Unexpected code " + response);

                }
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    JSONObject result = JSONObject.parseObject(res);
                    if(result.getString("code").equals("200")) {
                        JSONObject infor = JSONObject.parseObject(result.getString("value"));
                        Message message = new Message();
                        message.what = PERSONALINFORRESPONSE;
                        message.obj = infor;
                        mainHandler.sendMessage(message);
                    }
                }
            }
        });
    }

    private void setPersonalInfor(JSONObject infor){
        ((BandApplication)getApplication()).setName(infor.getString("nickname"));
        ((BandApplication) getApplication()).setHeight(Integer.valueOf(infor.getString("height")));
        ((BandApplication) getApplication()).setWeight(Integer.valueOf(infor.getString("weight")));
        ((BandApplication) getApplication()).setAge(Integer.valueOf(infor.getString("age")));
        ((BandApplication) getApplication()).setSex(Integer.valueOf(infor.getString("sex")));
        ((BandApplication) getApplication()).setInit(true);
    }

    Handler mainHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //判断发送的消息
                case WEATHERLOCATED:{
                    getWeather(msg.obj.toString());
                    mLocationClient.stop();
                    Log.i("msgobj",msg.obj.toString());
                    break;
                }

                case WEATHERGETTED:{
                    refreshWeather(msg.obj.toString());
                    break;
                }

                case CONNECTED:{
                    initBand();
                    break;
                }

                case UPDATESTEPNUM:{
                    refreshStep((String[]) msg.obj);
                    insertStep((String[]) msg.obj);
                    break;
                }

                case UPDATEHEARTRATENUM:{
                    refreshHeartRate((String[]) msg.obj);
                    insertHeartRate((String[]) msg.obj);
                    break;
                }

                case PERSONALINFORRESPONSE:{
                    setPersonalInfor((JSONObject) msg.obj);
                    break;
                }
            }
            super.handleMessage(msg);
        }
    };
    public static String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
