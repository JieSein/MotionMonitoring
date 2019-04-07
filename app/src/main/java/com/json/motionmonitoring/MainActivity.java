package com.json.motionmonitoring;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import com.json.motionmonitoring.model.Device;
import com.json.motionmonitoring.model.User;
import com.json.motionmonitoring.util.EdittextContent;
import com.json.motionmonitoring.util.HttpUtil;
import com.json.motionmonitoring.util.Utility;
import com.json.motionmonitoring.util.Validator;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView RegisterTextView;

    private String usernameText;
    private String passwordText;

    private EditText usernameEdit;
    private EditText passwordEdit;
    private Button loginButton;

    private LinearLayout username_layout;
    private LinearLayout password_layout;

    private TextView textview_user;
    private TextView textview_password;
    private TextView textview_register;

    private ToggleButton pwdHideOrDiaplayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RegisterTextView = (TextView) findViewById(R.id.register_in_button);
        RegisterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        initView();
        focusEdit();
        pwdHideOrDisplay();
        loginInit();

    }

    private void initView(){
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "icons/iconfont.ttf");

        textview_user = (TextView)findViewById(R.id.username_login_text_view);
        textview_user.setTypeface(iconfont);

        textview_password = (TextView)findViewById(R.id.password_login_text_view);
        textview_password.setTypeface(iconfont);

        textview_register = (TextView)findViewById(R.id.register_in_button);
        textview_register.setTypeface(iconfont);

        pwdHideOrDiaplayButton = (ToggleButton) findViewById(R.id.pwd_login_hide_or_display_togglebutton);
        pwdHideOrDiaplayButton.setTypeface(iconfont);

        username_layout = (LinearLayout)findViewById(R.id.username_login_layout);
        password_layout = (LinearLayout)findViewById(R.id.password_login_layout);

        usernameEdit = (EditText)findViewById(R.id.username_login_edit_text);
        passwordEdit = (EditText)findViewById(R.id.password_login_edit_text);
        loginButton = (Button)findViewById(R.id.login_button);

        passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void pwdHideOrDisplay(){
        pwdHideOrDiaplayButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    passwordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private void focusEdit(){
        usernameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    username_layout.setBackgroundResource(R.drawable.edittext_focused_background);
                } else {
                    username_layout.setBackgroundResource(R.drawable.edittext_background);
                }
            }
        });

        passwordEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    password_layout.setBackgroundResource(R.drawable.edittext_focused_background);
                } else {
                    password_layout.setBackgroundResource(R.drawable.edittext_background);
                }
            }
        });
    }

    private void loginInit(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();

                if (TextUtils.isEmpty(usernameText)){
                    Toast.makeText(MainActivity.this, "请输入账号!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(passwordText)){
                    Toast.makeText(MainActivity.this, "请输入密码!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Validator.verifyPassword(passwordText) == false){
                    Toast.makeText(MainActivity.this, "请输入6到20位的密码!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("MainActivity", "****账号" + usernameText);
                Log.d("MainActivity", "****密码" + passwordText);

                RequestBody requestBody = new FormBody.Builder()
                        .add("username", usernameText)
                        .add("password", passwordText)
                        .build();

                commitToServer("http://192.168.43.4:8080/MIMS/auLogin", requestBody);
            }
        });
    }

    private void commitToServer(String address, RequestBody requestBody){
        HttpUtil.sendOkHttpPostRequest(address, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                String flag = Utility.handleValidateResponse(responseText);
                if ("OK".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("MainActivity", "登录成功");
                            returnHomeActivity();
                        }
                    });
                } else if ("NO".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "账号错误", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                }
            }
        });
    }

    private void getEditString(){
        usernameText = usernameEdit.getText().toString().trim();
        passwordText = passwordEdit.getText().toString().trim();
    }

    private void returnHomeActivity(){
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        Log.d("MainActivity", "登录名"+usernameText);
        intent.putExtra("username", usernameText);
        startActivity(intent);
    }
}
