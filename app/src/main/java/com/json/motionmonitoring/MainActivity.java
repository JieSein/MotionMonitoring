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

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

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



        Button test = (Button)findViewById(R.id.select_test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Device device = new Device();
                device.setId(1);
                device.setUser_id(1);
                device.save();
                Log.d("MainActivity", "成功");
                List<Device> devices = DataSupport.findAll(Device.class);
                for (Device device : devices){
                    Log.d("MainActivity", "设备id："+device.getId());
                    Log.d("MainActivity", "用户id："+device.getUser_id());
                }*/
                List<User> users = DataSupport.findAll(User.class);
                for (User user:users){
                    Log.d("MainActivity", "id:"+user.getId());
                    Log.d("MainActivity", "username:"+user.getUser_name());
                    Log.d("MainActivity", "password:"+user.getPassword());
                    Log.d("MainActivity", "email:"+user.getE_mail());
                    Log.d("MainActivity", "phone:"+user.getPhone());
                    Log.d("MainActivity", "sex:"+user.getSex());
                    Log.d("MainActivity", "age:"+user.getAge());
                    Log.d("MainActivity", "height:"+user.getHeight());
                    Log.d("MainActivity", "weight:"+user.getWeight());
                    Log.d("MainActivity", "*****************************************");
                }
            }
        });
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
                } else if (TextUtils.isEmpty(passwordText)){
                    Toast.makeText(MainActivity.this, "请输入密码!", Toast.LENGTH_SHORT).show();
                }

                List<User> users = DataSupport.where("user_name = ? and password = ?",
                        usernameText, passwordText).find(User.class);
                for (User user : users){
                    if (user.getUser_name().toString().equals(usernameText)){
                        if (user.getPassword().toString().equals(passwordText)){
                            Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            returnHomeActivity();
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

    private void returnHomeActivity(){
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        Log.d("MainActivity", "登录名"+usernameText);
        intent.putExtra("username", usernameText);
        startActivity(intent);
    }
}
