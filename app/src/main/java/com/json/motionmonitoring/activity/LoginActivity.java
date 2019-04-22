package com.json.motionmonitoring.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.json.motionmonitoring.R;
import com.json.motionmonitoring.util.HttpUtil;
import com.json.motionmonitoring.util.Utility;
import com.json.motionmonitoring.util.Validator;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {
    public static final String USERNAME = "USERNAME";
    private TextView RegisterTextView;
    private String usernameText, pwdText;
    private EditText et_username, et_pwd;
    private CheckBox rememberPass;
    private Button btn_login;
    private LinearLayout layout_username, layout_pwd;
    private TextView icon_user, icon_pwd, icon_register;
    private SharedPreferences.Editor editor;
    private SharedPreferences mSharedPreferences;
    private boolean isRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        init();
        rememberPassword();
    }

    private void init(){
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "icons/iconfont.ttf");
        icon_user = (TextView)findViewById(R.id.username_login_text_view);
        icon_user.setTypeface(iconfont);
        icon_pwd = (TextView)findViewById(R.id.password_login_text_view);
        icon_pwd.setTypeface(iconfont);
        icon_register = (TextView)findViewById(R.id.register_in_button);
        icon_register.setTypeface(iconfont);

        layout_username = (LinearLayout)findViewById(R.id.username_login_layout);
        layout_pwd = (LinearLayout)findViewById(R.id.password_login_layout);

        et_username = (EditText)findViewById(R.id.username_login_edit_text);
        et_pwd = (EditText)findViewById(R.id.password_login_edit_text);
        btn_login = (Button)findViewById(R.id.login_button);
        RegisterTextView = (TextView) findViewById(R.id.register_in_button);

        rememberPass = (CheckBox)findViewById(R.id.remember_pwd);

        et_username.setOnFocusChangeListener(this);
        et_pwd.setOnFocusChangeListener(this);
        btn_login.setOnClickListener(this);
        RegisterTextView.setOnClickListener(this);

        et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.username_login_edit_text:
                if (hasFocus){
                    icon_user.setTextColor(0xFF66C480);
                    layout_username.setBackgroundResource(R.drawable.edittext_focused_background);
                } else {
                    icon_user.setTextColor(0xFF737373);
                    layout_username.setBackgroundResource(R.drawable.edittext_background);
                }
                break;

            case R.id.password_login_edit_text:
                if (hasFocus){
                    icon_pwd.setTextColor(0xFF66C480);
                    layout_pwd.setBackgroundResource(R.drawable.edittext_focused_background);
                } else {
                    icon_pwd.setTextColor(0xFF737373);
                    layout_pwd.setBackgroundResource(R.drawable.edittext_background);
                }
                break;

                default:
                    break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_in_button:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;

            case R.id.login_button:
                getEditString();

                if (TextUtils.isEmpty(usernameText)){
                    Toast.makeText(LoginActivity.this, "请输入用户名!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(pwdText)){
                    Toast.makeText(LoginActivity.this, "请输入密码!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Validator.verifyPassword(pwdText) == false){
                    Toast.makeText(LoginActivity.this, "请输入6到20位字符或数字的密码!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("LoginActivity", "****用户名" + usernameText);
                Log.d("LoginActivity", "****密码" + pwdText);

                mSharedPreferences = getSharedPreferences("motion", MODE_PRIVATE);
                editor = mSharedPreferences.edit();
                if (rememberPass.isChecked()){
                    editor.putBoolean("remember_password", true);
                    editor.putString("username", usernameText);
                    editor.putString("password", pwdText);
                    editor.apply();
                } else {
                    editor.clear();
                    editor.commit();
                }

                RequestBody requestBody = new FormBody.Builder()
                        .add("username", usernameText)
                        .add("password", pwdText)
                        .build();

                commitToServer("http://192.168.43.4:8080/MIMS/auLogin", requestBody);
                break;

                default:
                    break;
        }
    }

    private void commitToServer(String address, RequestBody requestBody){
        HttpUtil.sendOkHttpPostRequest(address, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                String flag = Utility.handleValidateResponse(responseText);
                if ("LOGIN_OK".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("LoginActivity", "登录成功");
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            returnHomeActivity();
                        }
                    });
                } else if ("LOGIN_NO".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                }
            }
        });
    }

    private void getEditString(){
        usernameText = et_username.getText().toString().trim();
        pwdText = et_pwd.getText().toString().trim();
    }

    private void returnHomeActivity(){
        getEditString();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        Log.d("LoginActivity", "登录名"+usernameText);
        intent.putExtra(USERNAME, usernameText);
        startActivity(intent);
    }

    private void rememberPassword(){
        mSharedPreferences = getSharedPreferences("motion", MODE_PRIVATE);
        isRemember = mSharedPreferences.getBoolean("remember_password", false);
        if (isRemember){
            usernameText = mSharedPreferences.getString("username", "");
            pwdText = mSharedPreferences.getString("password", "");
            et_username.setText(usernameText);
            et_pwd.setText(pwdText);
            rememberPass.setChecked(true);
        }
    }
}
