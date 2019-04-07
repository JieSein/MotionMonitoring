package com.json.motionmonitoring;

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

import com.json.motionmonitoring.model.User;
import com.json.motionmonitoring.util.EdittextContent;
import com.json.motionmonitoring.util.HttpUtil;
import com.json.motionmonitoring.util.Utility;
import com.json.motionmonitoring.util.Validator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextView loginTextView;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private EditText passwordSureEdit;
    private Button registerButton;

    private String usernameText;
    private String passwordText;
    private String passwordSureText;

    private LinearLayout username_layout;
    private LinearLayout password_layout;
    private LinearLayout password_sure_layout;

    private TextView textview_user;
    private TextView textview_password;
    private TextView textview_password_sure;
    private TextView textview_login;

    private ToggleButton pwdHideOrDiaplayButton;
    private ToggleButton pwdSureHideOrDiaplayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        loginTextView = (TextView)findViewById(R.id.login_in_button);
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        initView();
        focusEdit();
        pwdHideOrDisplay();
        registerInit();
    }

    private void initView(){
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "icons/iconfont.ttf");

        textview_user = (TextView)findViewById(R.id.username_register_text_view);
        textview_user.setTypeface(iconfont);

        textview_password = (TextView)findViewById(R.id.password_register_text_view);
        textview_password.setTypeface(iconfont);

        textview_password_sure = (TextView)findViewById(R.id.password_sure_register_text_view);
        textview_password_sure.setTypeface(iconfont);

        textview_login = (TextView)findViewById(R.id.login_in_button);
        textview_login.setTypeface(iconfont);

        pwdHideOrDiaplayButton = (ToggleButton)findViewById(R.id.pwd_register_hide_or_display_togglebutton);
        pwdHideOrDiaplayButton.setTypeface(iconfont);

        pwdSureHideOrDiaplayButton = (ToggleButton)findViewById(R.id.pwd_sure_register_hide_or_display_togglebutton);
        pwdSureHideOrDiaplayButton.setTypeface(iconfont);

        username_layout = (LinearLayout)findViewById(R.id.username_register_layout);
        password_layout = (LinearLayout)findViewById(R.id.password_register_layout);
        password_sure_layout = (LinearLayout)findViewById(R.id.password_sure_register_layout);

        usernameEdit = (EditText)findViewById(R.id.username_register_edit_text);
        passwordEdit = (EditText)findViewById(R.id.password_register_edit_text);
        passwordSureEdit = (EditText)findViewById(R.id.password_sure_register_edit_text);
        registerButton = (Button)findViewById(R.id.register_button);

        passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        passwordSureEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
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

        pwdSureHideOrDiaplayButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    passwordSureEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordSureEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
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

        passwordSureEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    password_sure_layout.setBackgroundResource(R.drawable.edittext_focused_background);
                } else {
                    password_sure_layout.setBackgroundResource(R.drawable.edittext_background);
                }
            }
        });
    }

    private void registerInit(){
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();

                Log.d("RegisterActivity","注册username:"+usernameText);
                Log.d("RegisterActivity","注册password:"+passwordText);

                if (TextUtils.isEmpty(EdittextContent.getEditString(usernameEdit))){
                    Toast.makeText(RegisterActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(EdittextContent.getEditString(passwordEdit))){
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Validator.verifyPassword(EdittextContent.getEditString(passwordEdit)) == false) {
                    Toast.makeText(RegisterActivity.this, "请输入6到20位的字符", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(EdittextContent.getEditString(passwordSureEdit))){
                    Toast.makeText(RegisterActivity.this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!EdittextContent.getEditString(passwordSureEdit).equals(EdittextContent.getEditString(passwordEdit))){
                    Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestBody requestBody = new FormBody.Builder()
                        .add("username", EdittextContent.getEditString(usernameEdit))
                        .add("password", EdittextContent.getEditString(passwordEdit))
                        .build();
                commitToServer("http://192.168.43.4:8080/MIMS/auRegister", requestBody);
            }
        });
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
                            Log.d("MainActivity", "该账号已被注册");
                            return;
                        }
                    });
                } else if ("REGISTER_SUCCESS".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"注册成功:"+responseText, Toast.LENGTH_SHORT).show();
                            Log.d("MainActivity", "注册成功");
                            returnLoginActivity();
                        }
                    });
                } else if ("REGISTER_FAIL".equals(flag)){
                    Toast.makeText(RegisterActivity.this,"注册失败:"+responseText, Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", "注册失败");
                    return;
                }
            }
        });
    }

    private void getEditString(){
        usernameText = usernameEdit.getText().toString().trim();
        passwordText = passwordEdit.getText().toString().trim();
        passwordSureText = passwordSureEdit.getText().toString().trim();
    }

    private void returnLoginActivity(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
