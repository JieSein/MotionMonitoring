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
import com.json.motionmonitoring.util.Validator;

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

                if (TextUtils.isEmpty(usernameText)){
                    Toast.makeText(RegisterActivity.this, "请输入用户名：", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Validator.verifyEmail(usernameEdit.getText().toString().trim()) == false) {
                    Toast.makeText(RegisterActivity.this, "用户名格式错误", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(passwordText)){
                    Toast.makeText(RegisterActivity.this, "请输入密码：", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Validator.verifyPassword(passwordEdit.getText().toString().trim()) == false) {
                    Toast.makeText(RegisterActivity.this, "请输入6到20位的数字或字母", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(passwordSureText)){
                    Toast.makeText(RegisterActivity.this, "请确认密码：", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!passwordSureEdit.getText().toString().trim().equals(passwordEdit.getText().toString().trim())){
                    Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = new User();
                user.setUser_name(usernameText);
                user.setPassword(passwordText);
                boolean i = user.save();

                if (i){
                    Log.d("RegisterActivity","插入成功:");
                    returnLoginActivity();
                } else {
                    Log.d("RegisterActivity","插入失败:");
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
