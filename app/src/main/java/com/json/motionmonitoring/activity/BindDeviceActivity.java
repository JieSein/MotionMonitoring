package com.json.motionmonitoring.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.json.motionmonitoring.R;
import com.json.motionmonitoring.model.Motion_Device;
import com.json.motionmonitoring.util.HttpUtil;
import com.json.motionmonitoring.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BindDeviceActivity extends AppCompatActivity {
    private EditText deviceCodeEdit;
    private Button preservationButton;
    private TextView returnButton, textview_device_code;
    private String deviceCodeText;
    private Intent intent;
    private String rev_username, mDeviceAddress;
    private int rev_flag;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean device_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bind_device);

        init();
        selectUserDevice();
        UpdateDeviceInformation();
    }

    private void init(){
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "icons/iconfont.ttf");
        textview_device_code = (TextView)findViewById(R.id.device_code_text_view);
        textview_device_code.setTypeface(iconfont);
        returnButton = (TextView) findViewById(R.id.device_information_return_text_view);
        returnButton.setTypeface(iconfont);
        deviceCodeEdit = (EditText) findViewById(R.id.device_code_edit);
        preservationButton = (Button) findViewById(R.id.device_preservation_button);
    }

    private void selectUserDevice(){
        intent = getIntent();
        rev_username = intent.getStringExtra(LoginActivity.USERNAME);
        mDeviceAddress = intent.getStringExtra(HomeActivity.EXTRAS_DEVICE_ADDRESS);
        rev_flag = intent.getIntExtra("id", 0);
        Log.d("BindDeviceActivity", "Activity接收Fragment的登录名"+rev_username);
        Log.d("BindDeviceActivity", "Activity接收Fragment的蓝牙address"+mDeviceAddress);
        Log.d("BindDeviceActivity", "Activity接收的标志位"+rev_flag);

        mSharedPreferences = getSharedPreferences("motion", MODE_PRIVATE);
        device_flag = mSharedPreferences.getBoolean("device_flag", false);
        if (device_flag){
            mDeviceAddress = mSharedPreferences.getString("deviceCode", "");
            deviceCodeEdit.setText(mDeviceAddress);
            Log.d("aaa", mDeviceAddress);
        } else {
            commitToSelServer("http://192.168.43.4:8080/MIMS/auDevInformation?username="+rev_username);
        }
    }

    private void commitToSelServer(String address){
        HttpUtil.sendOkHttpGetRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BindDeviceActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                final Motion_Device device = Utility.handleSelDevResponse(responseText);
                if (device != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSharedPreferences = getSharedPreferences("motion", MODE_PRIVATE);
                            editor = mSharedPreferences.edit();
                            editor.putBoolean("device_flag", true);
                            editor.putString("deviceCode", device.getDeviceCode());
                            editor.apply();

                            deviceCodeEdit.setText(device.getDeviceCode());
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BindDeviceActivity.this, "查询不到设备号", Toast.LENGTH_SHORT).show();
                            deviceCodeEdit.setText("");
                        }
                    });
                }
            }
        });
    }

    private void UpdateDeviceInformation(){
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnUserFragment();
            }
        });

        preservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();

                Log.d("BindDeviceActivity", rev_username);
                Log.d("BindDeviceActivity", deviceCodeText);

                mSharedPreferences = getSharedPreferences("motion", MODE_PRIVATE);
                editor = mSharedPreferences.edit();
                editor.putBoolean("device_flag", true);
                editor.putString("deviceCode", deviceCodeText);
                editor.apply();

                RequestBody requestBody = new FormBody.Builder()
                        .add("username", rev_username)
                        .add("deviceCode", deviceCodeText)
                        .build();
                commitToUpdDerver("http://192.168.43.4:8080/MIMS/auDevUpdInformation", requestBody);
            }
        });
    }

    private void commitToUpdDerver(String address, RequestBody requestBody){
        HttpUtil.sendOkHttpPostRequest(address, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(BindDeviceActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                String flag = Utility.handleValidateResponse(responseText);
                if ("UPDATE_OK".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("BindDeviceActivity", "修改成功");
                            returnUserFragment();
                        }
                    });
                } else if ("UPDATE_FAIL".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("BindDeviceActivity", "修改失败");
                            return;
                        }
                    });
                } else if ("INSERT_OK".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("BindDeviceActivity", "插入成功");
                            returnUserFragment();
                        }
                    });
                } else if ("INSERT_FAIL".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("BindDeviceActivity", "插入失败");
                            return;
                        }
                    });
                }
            }
        });
    }

    private void getEditString(){
        deviceCodeText = deviceCodeEdit.getText().toString().trim();
    }

    private void returnUserFragment(){
        if (rev_flag == 3){
            getEditString();
            Intent intent = new Intent(BindDeviceActivity.this, HomeActivity.class);
            intent.putExtra(LoginActivity.USERNAME, rev_username);
            intent.putExtra(HomeActivity.EXTRAS_DEVICE_ADDRESS, deviceCodeText);
            intent.putExtra("id", 3);
            startActivity(intent);
        }
        if (rev_flag == 4){
            getEditString();
            Intent intent = new Intent(BindDeviceActivity.this, MainActivity.class);
            intent.putExtra(LoginActivity.USERNAME, rev_username);
            intent.putExtra(HomeActivity.EXTRAS_DEVICE_ADDRESS, deviceCodeText);
            startActivity(intent);
        }
    }

}
