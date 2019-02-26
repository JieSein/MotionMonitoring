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

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView RegisterTextView;

    private String usernameText;
    private String passwordText;

    private EditText usernameEdit;
    private EditText passwordEdit;
    private Button loginButton;

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

        loginInit();

        Button test = (Button)findViewById(R.id.select_test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<User> users = DataSupport.findAll(User.class);
                for (User user:users){
                    Log.d("MainActivity", "username:"+user.getUser_name());
                    Log.d("MainActivity", "password:"+user.getPassword());
                }
            }
        });
    }

    private void loginInit(){
        usernameEdit = (EditText)findViewById(R.id.username_login_edit_text);
        passwordEdit = (EditText)findViewById(R.id.password_login_edit_text);
        loginButton = (Button)findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();

                if (TextUtils.isEmpty(usernameText)){
                    Toast.makeText(MainActivity.this, "请输入账号!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(passwordText)){
                    Toast.makeText(MainActivity.this, "请输入密码!", Toast.LENGTH_SHORT).show();
                }

                List<User> users = DataSupport.where("user_name = ? and password = ?",
                        usernameText, passwordText).find(User.class);
                for (User user : users){
                    if (user.getUser_name().toString().equals(usernameText)){
                        if (user.getPassword().toString().equals(passwordText)){
                            Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            returnStepActivity();
                        } else {
                            Log.d("MainActivity", "密码错误"+user.getUser_name());
                            Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "账号错误", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        });


    }

    private void getEditString(){
        usernameText = usernameEdit.getText().toString().trim();
        passwordText = passwordEdit.getText().toString().trim();
    }

    private void returnStepActivity(){
        Intent intent = new Intent(MainActivity.this, StepActivity.class);
        startActivity(intent);
    }
}
