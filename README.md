手环各API调用方法和返回数据  
==
v1.0.0   2016-3-25

**在项目的build.gradle 文件添加依赖：**  
`compile 'com.zhaoxiaodan.miband:miband-sdk:1.1.2'`

**实例化一个手环 参数是Context类型**  
`MiBand miband = new MiBand(context);`  
蓝牙设备devices  
`HashMap<String, BluetoothDevice> devices = new HashMap<String, BluetoothDevice>();

**扫描**
```
final ScanCallback scanCallback = new ScanCallback() {
    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        BluetoothDevice device = result.getDevice();
        Log.d(TAG,"找到附近的蓝牙设备:                               //获得所需要的设备信息进行显示
                    name:" + device.getName() + ",
                    uuid:"+ device.getUuids() + ",
                    add:"+ device.getAddress() + ",
                    type:"+ device.getType() + ",
                    bondState:"+ device.getBondState() + ",
                    rssi:" + result.getRssi());
        String item = device.getName() + "|" + device.getAddress();
        if (!devices.containsKey(item)) {
            devices.put(item, device);
            adapter.add(item);
        }
    }
};
MiBand.startScan(scanCallback);   //开始扫描
MiBand.stopScan(scanCallback);    //停止扫描
```
**手环连接**  
```
miband.connect(device, new ActionCallback() {            //device参数是扫描时获得的蓝牙设备选中的

    @Override
    public void onSuccess(Object data)                   //连接成功的方法，重写此方法，来提示用户连接成功
    {
        Log.d(TAG,"连接成功");
    }

    @Override
    public void onFail(int errorCode, String msg)        //连接失败的方法
    {
        Log.d(TAG,"连接失败, code:"+errorCode+",mgs:"+msg);
    }
});

// 设置断开监听器, 方便在设备断开的时候进行重连或者别的处理，可写在连接成功的方法里
miband.setDisconnectedListener(new NotifyListener()
{
    @Override
    public void onNotify(byte[] data)
    {
        Log.d(TAG,“连接断开!!!”);
    }
});
```
**设置UserInfo, 心跳检测之前必须设置**  
当 设置的 userid 跟之前设置的不一样时, 手环会闪动并震动, 这个时候, 需要拍一下手环; 就像官方app 配对时一样  
当 设置的 userid 跟之前一样时 手环无反应;  
```
UserInfo userInfo = new UserInfo(000000, 1, 32, 180, 55, "NAME", 1); //分别对应uid,性别，年龄，身高，体重，名字，类型（0/1）
miband.setUserInfo(userInfo);
```
**设置心跳扫描结果通知，heartRate就是获得的心率，在onNotify函数中可进行相关操作**  
```
miband.setHeartRateScanListener(new HeartRateNotifyListener()
{
    @Override
    public void onNotify(int heartRate)
    {
        Log.d(TAG, "heart rate: "+ heartRate);
    }
});
//开始心跳扫描，结果会在上个函数中返回
miband.startHeartRateScan();
```
**读取手环电池信息，结果显示在info变量中，level就是电量值，last最后充电时间**  
```
miband.getBatteryInfo(new ActionCallback() {

    @Override
    public void onSuccess(Object data)
    {
        BatteryInfo info = (BatteryInfo)data;
        Log.d(TAG, info.toString());
        //cycles:4,level:44,status:unknow,last:2015-04-15 03:37:55
    }

    @Override
    public void onFail(int errorCode, String msg)
    {
        Log.d(TAG, "readRssi fail");
    }
});
```
**震动相关**  
```
//震动2次， 三颗led亮
miband.startVibration(VibrationMode.VIBRATION_WITH_LED);
//震动2次, 没有led亮
miband.startVibration(VibrationMode.VIBRATION_WITHOUT_LED);
//震动10次, 中间led亮蓝色
miband.startVibration(VibrationMode.VIBRATION_10_TIMES_WITH_LED);
//停止震动, 震动时随时调用都可以停止
miband.stopVibration();
```

**获取实时步数通知, 设置好后, 摇晃手环(需要持续摇动10-20下才会触发), 会实时收到当天总步数通知**  
1.设置监听器，获得的步数就是变量steps
```
miband.setRealtimeStepsNotifyListener(new RealtimeStepsNotifyListener() {

    @Override
    public void onNotify(int steps)
    {
        Log.d(TAG, "RealtimeStepsNotifyListener:" + steps);
    }
});
```
2.开启通知，会实时将传感器数据传给变量steps  
```
miband.enableRealtimeStepsNotify();
//关闭(暂停)实时步数通知, 再次开启只需要再次调用miband.enableRealtimeStepsNotify()即可
miband.disableRealtimeStepsNotify();
```
