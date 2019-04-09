package com.json.motionmonitoring;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.json.motionmonitoring.model.User;
import com.json.motionmonitoring.util.EdittextContent;
import com.json.motionmonitoring.util.HttpUtil;
import com.json.motionmonitoring.util.Utility;
import com.json.motionmonitoring.util.Validator;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInformation extends AppCompatActivity implements View.OnFocusChangeListener {

    private LinearLayout username_layout;
    private LinearLayout email_layout;
    private LinearLayout phone_layout;
    private LinearLayout sex_layout;
    private LinearLayout age_layout;
    private LinearLayout height_layout;
    private LinearLayout weight_layout;

    private TextView textview_username;
    private TextView textview_email;
    private TextView textview_phone;
    private TextView textview_sex;
    private TextView textview_age;
    private TextView textview_height;
    private TextView textview_weight;

    private TextView textview_return;

    private TextView preservation;

    private EditText usernameEdit;
    private EditText emailEdit;
    private EditText phoneEdit;
    private EditText sexEdit;
    private EditText ageEdit;
    private EditText heightEdit;
    private EditText weightEdit;

    private String usernameText;
    private String emailText;
    private String phoneText;
    private String sexText;
    private String ageText;
    private String heightText;
    private String weightText;

    private String rev_username;
    private String mDeviceAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        initView();
        initEvent();
        getEditString();

        selectUser();

        onClickPreservation();
    }


    private void initView(){
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "icons/iconfont.ttf");

        textview_username = (TextView)findViewById(R.id.username_text);
        textview_username.setTypeface(iconfont);

        textview_email = (TextView)findViewById(R.id.email_text);
        textview_email.setTypeface(iconfont);

        textview_phone = (TextView)findViewById(R.id.phone_text);
        textview_phone.setTypeface(iconfont);

        textview_sex = (TextView)findViewById(R.id.sex_text);
        textview_sex.setTypeface(iconfont);

        textview_age = (TextView)findViewById(R.id.age_text);
        textview_age.setTypeface(iconfont);

        textview_height = (TextView)findViewById(R.id.height_text);
        textview_height.setTypeface(iconfont);

        textview_weight = (TextView)findViewById(R.id.weight_text);
        textview_weight.setTypeface(iconfont);

        textview_return = (TextView)findViewById(R.id.user_information_return_text_view);
        textview_return.setTypeface(iconfont);

        username_layout = (LinearLayout) findViewById(R.id.user_information_name_layout);
        email_layout = (LinearLayout) findViewById(R.id.user_information_email_layout);
        phone_layout = (LinearLayout) findViewById(R.id.user_information_phone_layout);
        sex_layout = (LinearLayout) findViewById(R.id.user_information_sex_layout);
        age_layout = (LinearLayout) findViewById(R.id.user_information_age_layout);
        height_layout = (LinearLayout) findViewById(R.id.user_information_height_layout);
        weight_layout = (LinearLayout) findViewById(R.id.user_information_weight_layout);

        usernameEdit = (EditText) findViewById(R.id.username_edit);
        emailEdit = (EditText) findViewById(R.id.email_edit);
        phoneEdit = (EditText) findViewById(R.id.phone_edit);
        sexEdit = (EditText) findViewById(R.id.sex_edit);
        ageEdit = (EditText) findViewById(R.id.age_edit);
        heightEdit = (EditText) findViewById(R.id.height_edit);
        weightEdit = (EditText) findViewById(R.id.weight_edit);

        preservation = (TextView) findViewById(R.id.user_information_edit_button);
    }

    private void initEvent(){
        usernameEdit.setOnFocusChangeListener(this);
        emailEdit.setOnFocusChangeListener(this);
        phoneEdit.setOnFocusChangeListener(this);
        sexEdit.setOnFocusChangeListener(this);
        ageEdit.setOnFocusChangeListener(this);
        heightEdit.setOnFocusChangeListener(this);
        weightEdit.setOnFocusChangeListener(this);
    }

    private void selectUser(){
        Intent intent = getIntent();
        rev_username = intent.getStringExtra(MainActivity.USERNAME);
        mDeviceAddress = intent.getStringExtra(HomeActivity.EXTRAS_DEVICE_ADDRESS);
        Log.d("UserInformation", "Activity接收Fragment的登录名"+rev_username);
        Log.d("UserInformation", "Activity接收Fragment的蓝牙address"+mDeviceAddress);

        commitToSelServer("http://192.168.43.4:8080/MIMS/auInformation?username="+rev_username);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        ContentValues values = new ContentValues();
        if (hasFocus){
            switch (v.getId()){
                case R.id.username_edit:
                    username_layout.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                    break;
                case R.id.email_edit:
                    email_layout.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                    break;
                case R.id.phone_edit:
                    phone_layout.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                    break;
                case R.id.sex_edit:
                    sex_layout.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                    break;
                case R.id.age_edit:
                    age_layout.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                    break;
                case R.id.height_edit:
                    height_layout.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                    break;
                case R.id.weight_edit:
                    weight_layout.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                    break;
            }
        } else {
            switch (v.getId()){
                case R.id.username_edit:
                    username_layout.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                    break;
                case R.id.email_edit:
                    email_layout.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                    break;
                case R.id.phone_edit:
                    phone_layout.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                    break;
                case R.id.sex_edit:
                    sex_layout.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                    break;
                case R.id.age_edit:
                    age_layout.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                    break;
                case R.id.height_edit:
                    height_layout.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                    break;
                case R.id.weight_edit:
                    weight_layout.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                    break;
            }
        }
    }

    private void onClickPreservation(){
        textview_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnUserFragment();
            }
        });

        preservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();

                User user = new User();
                user.setUser_name(rev_username);
                user.setE_mail(emailText);
                user.setPhone(phoneText);
                if ("男".equals(sexText)){
                    user.setSex(0);
                } else if ("女".equals(sexText)){
                    user.setSex(1);
                }
                user.setAge(Integer.parseInt(ageText));
                user.setHeight(Double.parseDouble(heightText));
                user.setWeight(Double.parseDouble(weightText));

                Gson gson = new Gson();
                String json = gson.toJson(user);
                Log.d("UserInformaiton", "////"+json);

                RequestBody requestBody = FormBody.create(
                        MediaType.parse("application/json; charset=utf-8"), json);

                commitToUpdServer("http://192.168.43.4:8080/MIMS/auUpdInformation", requestBody);
            }
        });
    }

    private void commitToSelServer(String address) {
        HttpUtil.sendOkHttpGetRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UserInformation.this, "连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String responseText = response.body().string();
                final  User user = Utility.handleSelUserResponse(responseText);
                if (user != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            usernameEdit.setText(rev_username);
                            emailEdit.setText(user.getE_mail());
                            phoneEdit.setText(user.getPhone());
                            if (user.getSex() == 0){
                                sexEdit.setText("男");
                            } else if (user.getSex() == 1){
                                sexEdit.setText("女");
                            }
                            ageEdit.setText(String.valueOf(user.getAge()));
                            heightEdit.setText(String.valueOf(user.getHeight()));
                            weightEdit.setText(String.valueOf(user.getWeight()));
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserInformation.this, "查找不到用户信息", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                }
            }
        });
    }

    private void commitToUpdServer(String address, RequestBody requestBody) {
        HttpUtil.sendOkHttpPostRequest(address, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(UserInformation.this, "连接失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                String flag = Utility.handleValidateResponse(responseText);
                if ("UPDATE_OK".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("UserInformaiton", "修改成功");
                            returnUserFragment();
                        }
                    });
                } else if ("UPDATE_FAIL".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("UserInformaiton", "修改失败 ");
                            return;
                        }
                    });
                }
            }
        });
    }

    private void getEditString(){
        usernameText = usernameEdit.getText().toString().trim();
        emailText = emailEdit.getText().toString().trim();
        phoneText = phoneEdit.getText().toString().trim();
        sexText = sexEdit.getText().toString().trim();
        ageText = ageEdit.getText().toString().trim();
        heightText = heightEdit.getText().toString().trim();
        weightText = weightEdit.getText().toString().trim();
    }

    private void returnUserFragment(){
        Intent intent = new Intent(UserInformation.this, HomeActivity.class);
        intent.putExtra(MainActivity.USERNAME, rev_username);
        intent.putExtra(HomeActivity.EXTRAS_DEVICE_ADDRESS, mDeviceAddress);
        intent.putExtra("id", 3);
        startActivity(intent);
    }

}
