package com.json.motionmonitoring.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.json.motionmonitoring.R;
import com.json.motionmonitoring.model.Motion_User;
import com.json.motionmonitoring.util.HttpUtil;
import com.json.motionmonitoring.util.Utility;
import com.json.motionmonitoring.util.Validator;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener{

    private EditText et_username, et_pwd, et_pwdSure, et_email, et_phone;
    private Button registerButton;
    private String usernameText, pwdText, pwdSureText, emailText, phoneText;

    private LinearLayout layout_username, layout_pwd, layout_pwdSure, layout_email, layout_phone;
    private TextView tv_user, tv_pwd, tv_pwdSure, tv_email, tv_phone, tv_returnLg;
    private ToggleButton pwdHideOrDiaplayButton, pwdSureHideOrDiaplayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        initView();
        pwdHideOrDisplay();
    }

    private void initView(){
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "icons/iconfont.ttf");
        tv_user = (TextView)findViewById(R.id.username_register_text_view);
        tv_user.setTypeface(iconfont);
        tv_pwd = (TextView)findViewById(R.id.password_register_text_view);
        tv_pwd.setTypeface(iconfont);
        tv_pwdSure = (TextView)findViewById(R.id.password_sure_register_text_view);
        tv_pwdSure.setTypeface(iconfont);
        tv_email = (TextView)findViewById(R.id.email_register_text_view);
        tv_email.setTypeface(iconfont);
        tv_phone = (TextView)findViewById(R.id.phone_register_text_view);
        tv_phone.setTypeface(iconfont);
        pwdHideOrDiaplayButton = (ToggleButton)findViewById(R.id.pwd_register_hide_or_display_togglebutton);
        pwdHideOrDiaplayButton.setTypeface(iconfont);
        pwdSureHideOrDiaplayButton = (ToggleButton)findViewById(R.id.pwd_sure_register_hide_or_display_togglebutton);
        pwdSureHideOrDiaplayButton.setTypeface(iconfont);

        layout_username = (LinearLayout)findViewById(R.id.username_register_layout);
        layout_pwd = (LinearLayout)findViewById(R.id.password_register_layout);
        layout_pwdSure = (LinearLayout)findViewById(R.id.password_sure_register_layout);
        layout_email = (LinearLayout)findViewById(R.id.email_register_layout);
        layout_phone = (LinearLayout)findViewById(R.id.phone_register_layout);

        et_username = (EditText)findViewById(R.id.username_register_edit_text);
        et_pwd = (EditText)findViewById(R.id.password_register_edit_text);
        et_pwdSure = (EditText)findViewById(R.id.password_sure_register_edit_text);
        et_email = (EditText)findViewById(R.id.email_register_edit_text);
        et_phone = (EditText)findViewById(R.id.phone_register_edit_text);

        registerButton = (Button)findViewById(R.id.register_button);
        tv_returnLg = (TextView)findViewById(R.id.register_return_text_view);
        tv_returnLg.setTypeface(iconfont);

        et_username.setOnFocusChangeListener(this);
        et_pwd.setOnFocusChangeListener(this);
        et_pwdSure.setOnFocusChangeListener(this);
        et_email.setOnFocusChangeListener(this);
        et_phone.setOnFocusChangeListener(this);
        registerButton.setOnClickListener(this);
        tv_returnLg.setOnClickListener(this);

        et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        et_pwdSure.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void pwdHideOrDisplay(){
        pwdHideOrDiaplayButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        pwdSureHideOrDiaplayButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    et_pwdSure.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    et_pwdSure.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.username_register_edit_text:
                if (hasFocus){
                    tv_user.setTextColor(0xFF66C480);
                    layout_username.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                } else {
                    tv_user.setTextColor(0xFF737373);
                    layout_username.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                }
                break;

            case R.id.password_register_edit_text:
                if (hasFocus){
                    tv_pwd.setTextColor(0xFF66C480);
                    pwdHideOrDiaplayButton.setTextColor(0xFF66C480);
                    layout_pwd.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                } else {
                    tv_pwd.setTextColor(0xFF737373);
                    pwdHideOrDiaplayButton.setTextColor(0xFF737373);
                    layout_pwd.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                }
                break;

            case R.id.password_sure_register_edit_text:
                if (hasFocus){
                    tv_pwdSure.setTextColor(0xFF66C480);
                    pwdSureHideOrDiaplayButton.setTextColor(0xFF66C480);
                    layout_pwdSure.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                } else {
                    tv_pwdSure.setTextColor(0xFF737373);
                    pwdSureHideOrDiaplayButton.setTextColor(0xFF737373);
                    layout_pwdSure.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                }
                break;

            case R.id.email_register_edit_text:
                if (hasFocus){
                    tv_email.setTextColor(0xFF66C480);
                    layout_email.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                } else {
                    tv_email.setTextColor(0xFF737373);
                    layout_email.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                }
                break;

            case R.id.phone_register_edit_text:
                if (hasFocus){
                    tv_phone.setTextColor(0xFF66C480);
                    layout_phone.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                } else {
                    tv_phone.setTextColor(0xFF737373);
                    layout_phone.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                }
                break;

                default:
                    break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_return_text_view:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.register_button:
                getEditString();

                Log.d("RegisterActivity","注册username:"+usernameText);
                Log.d("RegisterActivity","注册password:"+pwdText);
                Log.d("RegisterActivity","注册email:"+emailText);
                Log.d("RegisterActivity","注册phone:"+phoneText);

                if (TextUtils.isEmpty(usernameText)){
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if (Validator.verifyName(usernameText) == false) {
                    Toast.makeText(RegisterActivity.this, "用户名格式错误，请输入4到11位的字符或数字", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(pwdText)){
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Validator.verifyPassword(pwdText) == false) {
                    Toast.makeText(RegisterActivity.this, "密码格式错误，请输入6到20位的字符或数字", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(pwdSureText)){
                    Toast.makeText(RegisterActivity.this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!pwdSureText.equals(pwdText)){
                    Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(emailText)){
                    Toast.makeText(RegisterActivity.this, "请输入电子邮箱", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Validator.verifyEmail(emailText) == false){
                    Toast.makeText(RegisterActivity.this, "电子邮箱格式错误", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(phoneText)){
                    Toast.makeText(RegisterActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Validator.verifyPhone(phoneText) == false){
                    Toast.makeText(RegisterActivity.this, "手机号码格式错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                Motion_User user = new Motion_User();
                user.setUser_name(usernameText);
                user.setPassword(pwdText);
                user.setE_mail(emailText);
                user.setPhone(phoneText);

                Gson gson = new Gson();
                String json = gson.toJson(user);
                RequestBody requestBody = FormBody.create(
                        MediaType.parse("application/json; charset=utf-8"), json);
                commitToServer("http://192.168.43.4:8080/MIMS/auRegister", requestBody);
                break;

                default:
                    break;
        }
    }

    private void commitToServer(String address, RequestBody requestBody) {
        HttpUtil.sendOkHttpPostRequest(address, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                String flag = Utility.handleValidateResponse(responseText);
                if ("USER_EXIST".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"该账号已被注册:"+responseText, Toast.LENGTH_LONG).show();
                            Log.d("RegisterActivity", "该账号已被注册");
                            return;
                        }
                    });
                } else if ("REGISTER_SUCCESS".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"注册成功:"+responseText, Toast.LENGTH_SHORT).show();
                            Log.d("LoginActivity", "注册成功");
                            returnLoginActivity();
                        }
                    });
                } else if ("REGISTER_FAIL".equals(flag)){
                    Toast.makeText(RegisterActivity.this,"注册失败:"+responseText, Toast.LENGTH_SHORT).show();
                    Log.d("RegisterActivity", "注册失败");
                    return;
                }
            }
        });
    }

    private void getEditString(){
        usernameText = et_username.getText().toString().trim();
        pwdText = et_pwd.getText().toString().trim();
        pwdSureText = et_pwdSure.getText().toString().trim();
        emailText = et_email.getText().toString().trim();
        phoneText = et_phone.getText().toString().trim();
    }

    private void returnLoginActivity(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}
