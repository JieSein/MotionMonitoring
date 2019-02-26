package com.json.motionmonitoring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.json.motionmonitoring.model.User;

public class RegisterActivity extends AppCompatActivity {

    private TextView loginTextView;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private EditText passwordSureEdit;
    private Button registerButton;

    private String usernameText;
    private String passwordText;
    private String passwordSureText;

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
        registerInit();
    }

    private void registerInit(){
        usernameEdit = (EditText)findViewById(R.id.username_register_edit_text);
        passwordEdit = (EditText)findViewById(R.id.password_register_edit_text);
        passwordSureEdit = (EditText)findViewById(R.id.password_sure_register_edit_text);
        registerButton = (Button)findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();

                Log.d("RegisterActivity","注册username:"+usernameText);
                Log.d("RegisterActivity","注册password:"+passwordText);

                if (TextUtils.isEmpty(usernameText)){
                    Toast.makeText(RegisterActivity.this, "请输入用户名：", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(passwordText)){
                    Toast.makeText(RegisterActivity.this, "请输入密码：", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(passwordSureText)){
                    Toast.makeText(RegisterActivity.this, "请确认密码：", Toast.LENGTH_SHORT).show();
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
