package com.json.motionmonitoring;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.json.motionmonitoring.services.BluetoothLeService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "HomeActivity";
    public static String HEART_RATE_MEASUREMENT = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";;
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICE_RSSI = "RSSI";
    public static final String STEP_DATA_AVAILABLE = "STEP_DATA";
    public static final String TEMP_DATA_AVAILABLE = "TEMP_DATA";
    public static final String HEART_DATA_AVAILABLE = "HEART_DATA";

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private boolean mConnected = false;
    private String status = "disconnected";
    private TextView rev_tv, connect_state;
    int flag = 1;
    int fragment_flag = 1;

    private Bundle stepBundle, tempBundle, hearthBundle, userBundle;
    private String step_data, temp_data, heart_data;

    private static BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private static BluetoothGattCharacteristic target_chara = null;

    private LinearLayout ll_step, ll_temp, ll_heart, ll_user;
    private TextView tv_step, tv_temp, tv_heart, tv_user, tv_title;
    private String step, temp, heart, user;
    private TextView icons_step, icons_temp, icons_heart, icons_user;
    private Fragment stepFragment, tempFragment, heartFragment, userFragment;
    private String rev_username;
    private String rev_data = "";

    private String mDeviceName;
    private String mDeviceAddress;
    private String mRssi;

    private Handler mhandler = new Handler();
    private Handler myHandler = new Handler() {
        // 2.重写消息处理函数
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 判断发送的消息
                case 1:
                    String state = msg.getData().getString("connect_state");
                    connect_state.setText(state);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        initView();
        getTextView();
        stepFragment = new StepFragment();
        tempFragment = new TempFragment();
        heartFragment = new HeartFragment();
        receiveData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null){
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mGattUpdateReceiver);
        mBluetoothLeService = null;
        flag = 1;
        fragment_flag = 1;
    }

    private void initView(){
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "icons/iconfont.ttf");
        TextView textview_step = (TextView)findViewById(R.id.step_text_view);
        textview_step.setTypeface(iconfont);
        TextView textview_temp = (TextView)findViewById(R.id.temp_text_view);
        textview_temp.setTypeface(iconfont);
        TextView textview_heart = (TextView)findViewById(R.id.heart_text_view);
        textview_heart.setTypeface(iconfont);
        TextView textview_user = (TextView)findViewById(R.id.user_text_view);
        textview_user.setTypeface(iconfont);

        this.ll_step = (LinearLayout) findViewById(R.id.ll_step);
        this.ll_temp = (LinearLayout) findViewById(R.id.ll_temp);
        this.ll_heart = (LinearLayout) findViewById(R.id.ll_heart);
        this.ll_user = (LinearLayout) findViewById(R.id.ll_user);
        this.tv_step = (TextView) findViewById(R.id.tv_step);
        this.tv_temp = (TextView) findViewById(R.id.tv_temp);
        this.tv_heart = (TextView) findViewById(R.id.tv_heart);
        this.tv_user = (TextView) findViewById(R.id.tv_user);
        this.tv_title = (TextView) findViewById(R.id.tv_title);
        this.icons_step = (TextView) findViewById(R.id.step_text_view);
        this.icons_temp = (TextView) findViewById(R.id.temp_text_view);
        this.icons_heart = (TextView) findViewById(R.id.heart_text_view);
        this.icons_user = (TextView) findViewById(R.id.user_text_view);
        this.connect_state = (TextView)findViewById(R.id.connect_state);

        ll_step.setOnClickListener(this);
        ll_temp.setOnClickListener(this);
        ll_heart.setOnClickListener(this);
        ll_user.setOnClickListener(this);
    }

    private void indexFragment(int index){
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        if (fragment_flag == 1){
            transaction.add(R.id.fl_content, stepFragment);
            transaction.add(R.id.fl_content, tempFragment);
            transaction.add(R.id.fl_content, heartFragment);
            fragment_flag++;
        }
        hideFragment(transaction);
        switch (index){
            case 0:
                transaction.show(stepFragment);
                break;

            case 1:
                transaction.show(tempFragment);
                break;

            case 2:
                transaction.show(heartFragment);
                break;

            case 3:
                if (userFragment == null){
                    userFragment = new UserFragment();
                    transaction.add(R.id.fl_content, userFragment);
                    sendUserData();
                } else {
                    transaction.show(userFragment);
                }
                break;

            default:
                break;
        }
        transaction.commit();
    }

    /**
     * BluetoothLeService绑定的回调函数
     */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()){
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBluetoothLeService = null;
        }
    };

    /**
     * 广播接收器，负责接收BluetoothLeServiec类发送的数据
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)){
                mConnected = true;
                status = "connected";
                updateConnectionState(status);
                System.out.println("BroadcastReceiver :" + "device connected");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)){
                mConnected = false;
                status = "disconnected";
                updateConnectionState(status);
                System.out.println("BroadcastReceiver :" + "device disconnected");
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)){
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                System.out.println("BroadcastReceiver :" + "device SERVICES_DISCOVERED");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)){
                displayData(intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
                System.out.println("BroadcastReceiver onData:" + intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    /**
     * 更新链接状态
     * @param status
     */
    private void updateConnectionState(String status){
        Message msg = new Message();
        msg.what = 1;
        Bundle bundle = new Bundle();
        bundle.putString("connect_state", status);
        msg.setData(bundle);
        myHandler.sendMessage(msg);
        System.out.println("connect_state:" + status);
    }

    /**
     * 意图过滤器
     * @return
     */
    private static IntentFilter makeGattUpdateIntentFilter(){
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    /**
     * 处理从蓝牙接收的数据
     * @param rev_string
     */
    private void displayData(final String rev_string){
        rev_data = rev_string;
        if (flag == 1){
            flag++;
        } else {
            step_data = rev_data.substring(17, 18);
            temp_data = rev_data.substring(8,12);
            heart_data = rev_data.substring(5,8);


            Log.d(TAG, "step_data: "+step_data);
            Log.d(TAG, "temp_data: "+temp_data);
            Log.d(TAG, "heart_data: "+heart_data);
        }
        Log.d(TAG, "flag: "+flag);
    }

    /**
     * 处理蓝牙服务
     * @param gattServices
     */
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null){
            return;
        }
        String uuid = null;
        String unknownServiceString = "unknown_service";
        String unknownCharaString = "unknown_characteristic";

        // 服务数据,可扩展下拉列表的第一级数据
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();

        // 特征数据（隶属于某一级服务下面的特征值集合）
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();

        // 部分层次，所有特征值集合
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            // 获取服务列表
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();

            // 查表，根据该uuid获取对应的服务名称。SampleGattAttributes这个表需要自定义。

            gattServiceData.add(currentServiceData);

            System.out.println("Service uuid:" + uuid);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();

            // 从当前循环所指向的服务中读取特征值列表
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();

            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            // 对于当前循环所指向的服务中的每一个特征值
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();

                if (gattCharacteristic.getUuid().toString().equals(HEART_RATE_MEASUREMENT)) {
                    // 测试读取当前Characteristic数据，会触发mOnDataAvailable.onCharacteristicRead()
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mBluetoothLeService.readCharacteristic(gattCharacteristic);
                        }
                    }, 200);
                    // 接受Characteristic被写的通知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                    target_chara = gattCharacteristic;
                    // 设置数据内容
                    // 往蓝牙模块写入数据
                    // mBluetoothLeService.writeCharacteristic(gattCharacteristic);
                }
                List<BluetoothGattDescriptor> descriptors = gattCharacteristic.getDescriptors();
                for (BluetoothGattDescriptor descriptor : descriptors) {
                    System.out.println("---descriptor UUID:" + descriptor.getUuid());
                    // 获取特征值的描述
                    mBluetoothLeService.getCharacteristicDescriptor(descriptor);
                    // mBluetoothLeService.setCharacteristicNotification(gattCharacteristic,
                    // true);
                }
                gattCharacteristicGroupData.add(currentCharaData);
            }
            // 按先后顺序，分层次放入特征值集合中，只有特征值
            mGattCharacteristics.add(charas);
            // 构件第二级扩展列表（服务下面的特征值）
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }
    }

    private void hideFragment(FragmentTransaction transaction){
        if (stepFragment != null){
            transaction.hide(stepFragment);
        }
        if (tempFragment != null){
            transaction.hide(tempFragment);
        }
        if (heartFragment != null){
            transaction.hide(heartFragment);
        }
        if (userFragment != null){
            transaction.hide(userFragment);
        }
    }

    private void getTextView(){
        step = tv_step.getText().toString().trim();
        temp = tv_temp.getText().toString().trim();
        heart = tv_heart.getText().toString().trim();
        user = tv_user.getText().toString().trim();

        icons_step.setTextColor(0xFF33B5E5);
        tv_step.setTextColor(0xFF33B5E5);
        tv_title.setText(step);
    }

    @Override
    public void onClick(View v) {
        restartButton();

        switch (v.getId()){
            case R.id.ll_step:
                icons_step.setTextColor(0xFF33B5E5);
                tv_step.setTextColor(0xFF33B5E5);
                tv_title.setText(step);
                indexFragment(0);
                break;

            case R.id.ll_temp:
                icons_temp.setTextColor(0xFF33B5E5);
                tv_temp.setTextColor(0xFF33B5E5);
                tv_title.setText(temp);
                indexFragment(1);
                break;

            case R.id.ll_heart:
                icons_heart.setTextColor(0xFF33B5E5);
                tv_heart.setTextColor(0xFF33B5E5);
                tv_title.setText(heart);
                indexFragment(2);
                break;

            case R.id.ll_user:
                icons_user.setTextColor(0xFF33B5E5);
                tv_user.setTextColor(0xFF33B5E5);
                tv_title.setText(user);
                indexFragment(3);
                break;

            default:
                break;
        }
    }

    private void restartButton(){
        icons_step.setTextColor(0xFF717070);
        icons_temp.setTextColor(0xFF717070);
        icons_heart.setTextColor(0xFF717070);
        icons_user.setTextColor(0xFF717070);

        tv_step.setTextColor(0xFF717070);
        tv_temp.setTextColor(0xFF717070);
        tv_heart.setTextColor(0xFF717070);
        tv_user.setTextColor(0xFF717070);
    }

    private void sendStepData(){
        stepBundle = new Bundle();
        stepBundle.putString(MainActivity.USERNAME, rev_username);
    }

    private void sendUserData(){
        userBundle = new Bundle();
        userBundle.putString(MainActivity.USERNAME, rev_username);
        userBundle.putString(EXTRAS_DEVICE_ADDRESS, mDeviceAddress);
        userFragment.setArguments(userBundle);
        Log.d("Fragment", "HomeActivity传输的登录名"+rev_username);
    }

    private void receiveData(){
        Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(BleScan_Activity.EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(BleScan_Activity.EXTRAS_DEVICE_ADDRESS);
        mRssi = intent.getStringExtra(BleScan_Activity.EXTRAS_DEVICE_RSSI);
        rev_username = intent.getStringExtra(MainActivity.USERNAME);
        int id = intent.getIntExtra("id", 0);
        Log.d(TAG, "BleScan_Activity传输的蓝牙名称: "+mDeviceName);
        Log.d(TAG, "BleScan_Activity传输的蓝牙MAC: "+mDeviceAddress);
        Log.d(TAG, "BleScan_Activity传输的蓝牙Rssi: "+mRssi);
        Log.d(TAG, "BleScan_Activity传输的用户名: "+rev_username);
        Log.d(TAG, "定位:: "+ id);

        if (id == 0){
            restartButton();
            icons_step.setTextColor(0xFF33B5E5);
            tv_step.setTextColor(0xFF33B5E5);
            tv_title.setText(step);
            indexFragment(id);
        }
        if (id == 3){
            restartButton();
            icons_user.setTextColor(0xFF33B5E5);
            tv_user.setTextColor(0xFF33B5E5);
            tv_title.setText(user);
            indexFragment(id);
        }
    }
}
