package com.json.motionmonitoring;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BleScan_Activity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BleScan_Activity";
    private static final int REQUEST_COARSE_LOCATION = 0;
    private static final String SETADDRESS = "A8:10:87:68:79:B6";
    private static final long SCAN_PERIOD = 10000;
    public static String EXTRAS_DEVICE_NAME = "DEVICE_NAME";;
    public static String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static String EXTRAS_DEVICE_RSSI = "RSSI";
    int REQUEST_ENABLE_BT = 1;

    private TextView tv_search;
    private Button btn_bind;
    private String rev_username;
    private String rev_address;

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private boolean scan_flag;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ble_scan_activity);
        mayRequestLocation();



        init();
        init_ble();
        scan_flag = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        rev_username = intent.getStringExtra(MainActivity.USERNAME);
        rev_address = intent.getStringExtra(HomeActivity.EXTRAS_DEVICE_ADDRESS);

        Log.d(TAG, "MainActivity传输的登录名: "+rev_username);
        Log.d(TAG, "DeviceInformation传输的蓝牙address: "+rev_address);
    }

    private void init(){
        tv_search = (TextView)findViewById(R.id.search_device);
        btn_bind = (Button)findViewById(R.id.bind_device);
        tv_search.setOnClickListener(this);
        btn_bind.setOnClickListener(this);
        mHandler = new Handler();
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
            case R.id.search_device:
                if (scan_flag){
                    scanLeDevice(true);
                } else {
                    scanLeDevice(false);
                    tv_search.setText("Search...");
                }
                break;
            case R.id.bind_device:
                Intent intent = new Intent(BleScan_Activity.this, DeviceInformation.class);
                intent.putExtra(MainActivity.USERNAME, rev_username);
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
                    tv_search.setText("Searche...");
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            mScanning = true;
            scan_flag = false;
            tv_search.setText("Searching...");
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
                    final Intent intent = new Intent(BleScan_Activity.this, HomeActivity.class);
                    if (device == null) {
                        return;
                    }
                    if (SETADDRESS.equals(device.getAddress())){
                        intent.putExtra(EXTRAS_DEVICE_NAME, device.getName());
                        intent.putExtra(EXTRAS_DEVICE_ADDRESS, device.getAddress());
                        intent.putExtra(EXTRAS_DEVICE_RSSI, rssi+"");
                        intent.putExtra(MainActivity.USERNAME, rev_username);
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
