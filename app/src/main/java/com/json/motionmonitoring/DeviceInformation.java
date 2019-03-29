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

import com.json.motionmonitoring.model.Device;
import com.json.motionmonitoring.model.User;

import org.litepal.crud.DataSupport;

import java.util.List;

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

    private void getEditString(){
        deviceCodeText = deviceCodeEdit.getText().toString().trim();
    }

    private void selectUserDevice(){
        intent = getIntent();
        data = intent.getStringExtra("fragment_username");
        Log.d("DeviceInformation", "Activity接收Fragment的登录名"+data);

        userCursor = DataSupport.findBySQL("select id from user where user_name = ?", data);
        if (userCursor.moveToFirst()){
            do {
                users_id = userCursor.getInt(userCursor.getColumnIndex("id"));
                Log.d("DeviceInformation", "用户id："+users_id);

                deviceCursor = selectDevice(users_id);
                if (deviceCursor.moveToFirst()){
                    do {
                        id = deviceCursor.getInt(deviceCursor.getColumnIndex("id"));
                        user_id = deviceCursor.getInt(deviceCursor.getColumnIndex("user_id"));
                        Log.d("DeviceInformation", "device表中的设备id："+id);
                        Log.d("DeviceInformation", "device表中的用户id："+user_id);

                        deviceCodeEdit.setText(String.valueOf(id));
                    } while (deviceCursor.moveToNext());
                }
            } while (userCursor.moveToNext());
        }

    }

    private Cursor selectDevice(int code){
        Cursor cursor = DataSupport.findBySQL("select * from device where user_id = ?", String.valueOf(code));
        return cursor;
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
                Device device = new Device();
                Cursor cursor = selectDevice(users_id);
                if (cursor.moveToFirst()){
                    ContentValues values = new ContentValues();
                    values.put("id", deviceCodeEdit.getText().toString().trim());
                    int result = DataSupport.updateAll(Device.class, values, "user_id = ?", String.valueOf(users_id));
                    if (result > 0){
                        Log.d("DeviceInformation", "修改设备号成功");
                        returnUserFragment();
                    } else {
                        Log.d("DeviceInformation", "修改设备号失败");
                    }
                } else {
                    device.setId(id);
                    device.setUser_id(users_id);
                    boolean i = device.save();
                    if (i){
                        Log.d("DeviceInformation","插入成功:");
                        returnUserFragment();
                    } else {
                        Log.d("DeviceInformation","插入失败:");
                    }

                }
                Log.d("DeviceInformation", "设备号编辑框："+deviceCodeEdit.getText().toString().trim());
            }
        });
    }

    private void returnUserFragment(){
        Intent intent = new Intent(DeviceInformation.this, HomeActivity.class);
        intent.putExtra("username", data);
        intent.putExtra("id", 3);
        startActivity(intent);
    }

}
