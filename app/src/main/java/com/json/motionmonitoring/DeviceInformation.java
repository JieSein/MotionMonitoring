package com.json.motionmonitoring;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.json.motionmonitoring.model.Device;
import com.json.motionmonitoring.model.User;
import com.json.motionmonitoring.util.HttpUtil;
import com.json.motionmonitoring.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeviceInformation extends AppCompatActivity {

    private EditText deviceCodeEdit;
    private Button preservationButton;
    private TextView returnButton;

    private TextView textview_device_code;

    private String deviceCodeText;

    private Intent intent;
    private String data;

    private int id;
    private int user_id;
    private int users_id;

    private Cursor userCursor;
    private Cursor deviceCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_information);

        initView();
        getEditString();

        selectUserDevice();

        UpdateDeviceInformation();

    }

    private void initView(){
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
        data = intent.getStringExtra("fragment_username");
        Log.d("DeviceInformation", "Activity接收Fragment的登录名"+data);

        commitToSelServer("http://192.168.43.4:8080/MIMS/auDevInformation?username="+data);

    }

    private void commitToSelServer(String address){
        HttpUtil.sendOkHttpGetRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(DeviceInformation.this, "连接失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                final Device device = Utility.handleSelDevResponse(responseText);
                if (device != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            deviceCodeEdit.setText(String.valueOf(device.getId()));
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DeviceInformation.this, "查询不到设备号", Toast.LENGTH_SHORT).show();
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

                Log.d("DeviceInformation", data);
                Log.d("DeviceInformation", deviceCodeText);

                RequestBody requestBody = new FormBody.Builder()
                        .add("username", data)
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
                Toast.makeText(DeviceInformation.this, "连接失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                String flag = Utility.handleValidateResponse(responseText);
                if ("UPDATE_OK".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("DeviceInformation", "修改成功");
                            returnUserFragment();
                        }
                    });
                } else if ("UPDATE_FAIL".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("DeviceInformation", "修改失败");
                            return;
                        }
                    });
                } else if ("INSERT_OK".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("DeviceInformation", "插入成功");
                            returnUserFragment();
                        }
                    });
                } else if ("INSERT_FAIL".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("DeviceInformation", "插入失败");
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
        Intent intent = new Intent(DeviceInformation.this, HomeActivity.class);
        intent.putExtra("username", data);
        intent.putExtra("id", 3);
        startActivity(intent);
    }

}
