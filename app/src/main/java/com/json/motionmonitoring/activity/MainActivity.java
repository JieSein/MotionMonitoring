package com.json.motionmonitoring.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.json.motionmonitoring.R;
import com.json.motionmonitoring.view.RippleView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_COARSE_LOCATION = 0;
    //private static final String SETADDRESS = "A8:10:87:68:79:B6";
    private static final long SCAN_PERIOD = 10000;
    public static String EXTRAS_DEVICE_NAME = "DEVICE_NAME";;
    public static String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static String EXTRAS_DEVICE_RSSI = "RSSI";
    int REQUEST_ENABLE_BT = 1;
    private ImageView tv_search;
    private Button btn_bind;
    private String rev_username, shared_address;
    private RippleView mRippleView;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning, scan_flag, device_flag;
    private SharedPreferences mSharedPreferences;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        mayRequestLocation();

        init();
        init_ble();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        rev_username = intent.getStringExtra(LoginActivity.USERNAME);
        //shared_address = intent.getStringExtra(HomeActivity.EXTRAS_DEVICE_ADDRESS);

        Log.d(TAG, "MainActivity传输的登录名: "+rev_username);
        //Log.d(TAG, "DeviceInformation传输的蓝牙address: "+shared_address);
        obtainAddress();
    }

    private void init(){
        mRippleView = (RippleView)findViewById(R.id.view_ripple);
        tv_search = (ImageView)findViewById(R.id.img_bluetooth);
        btn_bind = (Button)findViewById(R.id.bind_device);
        tv_search.setOnClickListener(this);
        btn_bind.setOnClickListener(this);
        mHandler = new Handler();
        scan_flag = true;
    }

    private void init_ble(){
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE))
        {
            Toast.makeText(this, "不支持BLE", Toast.LENGTH_SHORT).show();
            finish();
        }
        // Initializes Bluetooth adapter.
        // 获取手机本地的蓝牙适配器
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // 打开蓝牙权限
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_bluetooth:
                if (scan_flag){
                    scanLeDevice(true);
                } else {
                    scanLeDevice(false);
                    mRippleView.stopRippleAnimation();
                }
                break;
            case R.id.bind_device:
                Intent intent = new Intent(MainActivity.this, BindDeviceActivity.class);
                intent.putExtra(LoginActivity.USERNAME, rev_username);
                intent.putExtra("id", 4);
                startActivity(intent);
                break;

                default:
                    break;
        }
    }

    /**
     * 扫描蓝牙设备
     * @param enable true:扫描开始，false:扫描停止
     */
    private void scanLeDevice(final boolean enable){
        if (enable){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    scan_flag = true;
                    mRippleView.stopRippleAnimation();
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            mScanning = true;
            scan_flag = false;
            mRippleView.startRippleAnimation();
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            scan_flag = true;
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    if (device == null) {
                        return;
                    }
                    if (shared_address.equals(device.getAddress())){
                        intent.putExtra(EXTRAS_DEVICE_NAME, device.getName());
                        intent.putExtra(EXTRAS_DEVICE_ADDRESS, device.getAddress());
                        intent.putExtra(EXTRAS_DEVICE_RSSI, rssi+"");
                        intent.putExtra(LoginActivity.USERNAME, rev_username);
                        intent.putExtra("id", 0);
                        if (mScanning){
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
                            mScanning = false;
                        }
                        try {
                            // 启动Ble_Activity
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            // TODO: handle exception
                        }
                        Log.d(TAG, "name: "+device.getName());
                        Log.d(TAG, "address: "+device.getAddress());
                    }

                }
            });
        }
    };

    private void obtainAddress(){
        mSharedPreferences = getSharedPreferences("motion", MODE_PRIVATE);
        device_flag = mSharedPreferences.getBoolean("device_flag", false);
        Log.d("deviceflag", device_flag+"");
        if (device_flag){
            shared_address = mSharedPreferences.getString("deviceCode", "");
            Log.d("aaa", "缓存中的蓝牙地址"+shared_address);
        }
    }

    private void mayRequestLocation() {
        if (Build.VERSION.SDK_INT >= 18) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要 向用户解释，为什么要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                    Toast.makeText(this, "动态请求权限", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOCATION);
                return;
            } else {

            }
        } else {

        }
    }
    //系统方法,从requestPermissions()方法回调结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //确保是我们的请求
        if (requestCode == REQUEST_COARSE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限被授予", Toast.LENGTH_SHORT).show();
            } else if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
