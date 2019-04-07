package com.json.motionmonitoring;

import android.content.ContentValues;
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

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AlterPasswordActivity extends AppCompatActivity {

    private LinearLayout ll_originalPwd;
    private LinearLayout ll_newPwd;
    private LinearLayout ll_newPwdSure;

    private TextView icon_originalPwd;
    private TextView icon_newPwd;
    private TextView icon_newPwdSure;
    private TextView icon_returnButton;

    private ToggleButton tb_originalPwd;
    private ToggleButton tb_newPwd;
    private ToggleButton tb_newPwdSure;

    private EditText editText_originalPwd;
    private EditText editText_newPwd;
    private EditText editText_newPwdSure;

    private String originalPwd;
    private String newPwd;
    private String newPwdSure;

    private Button preservation;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alter_password_layout);

        initView();
        receiveData();
        focusEdit();
        pwdHideOrDisplay();
        onClickPreservation();
    }

    private void initView(){
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "icons/iconfont.ttf");

        icon_originalPwd = (TextView)findViewById(R.id.original_password_icon);
        icon_originalPwd.setTypeface(iconfont);

        icon_newPwd = (TextView)findViewById(R.id.new_password_icon);
        icon_newPwd.setTypeface(iconfont);

        icon_newPwdSure = (TextView)findViewById(R.id.new_password_sure_icon);
        icon_newPwdSure.setTypeface(iconfont);

        icon_returnButton = (TextView)findViewById(R.id.alter_password_return_text_view);
        icon_returnButton.setTypeface(iconfont);

        tb_originalPwd = (ToggleButton)findViewById(R.id.original_pwd_hide_or_display_togglebutton);
        tb_originalPwd.setTypeface(iconfont);

        tb_newPwd = (ToggleButton)findViewById(R.id.new_pwd_hide_or_display_togglebutton);
        tb_newPwd.setTypeface(iconfont);

        tb_newPwdSure = (ToggleButton) findViewById(R.id.new_pwd_sure_hide_or_display_togglebutton);
        tb_newPwdSure.setTypeface(iconfont);

        ll_originalPwd = (LinearLayout)findViewById(R.id.original_password_layout);
        ll_newPwd = (LinearLayout)findViewById(R.id.new_password_layout);
        ll_newPwdSure = (LinearLayout)findViewById(R.id.new_password_sure_layout);

        editText_originalPwd = (EditText)findViewById(R.id.original_password_edittext);
        editText_newPwd = (EditText)findViewById(R.id.new_password_edittext);
        editText_newPwdSure = (EditText)findViewById(R.id.new_password_sure_edittext);

        preservation = (Button)findViewById(R.id.alter_password_preservation_button);

        editText_originalPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText_newPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText_newPwdSure.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void pwdHideOrDisplay(){
        tb_originalPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editText_originalPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    editText_originalPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        tb_newPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editText_newPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    editText_newPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        tb_newPwdSure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editText_newPwdSure.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    editText_newPwdSure.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private void focusEdit(){
        editText_originalPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ll_originalPwd.setBackgroundResource(R.drawable.edittext_focused_background);
                } else {
                    ll_originalPwd.setBackgroundResource(R.drawable.edittext_background);
                }
            }
        });

        editText_newPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ll_newPwd.setBackgroundResource(R.drawable.edittext_focused_background);
                } else {
                    ll_newPwd.setBackgroundResource(R.drawable.edittext_background);
                }
            }
        });

        editText_newPwdSure.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ll_newPwdSure.setBackgroundResource(R.drawable.edittext_focused_background);
                } else {
                    ll_newPwdSure.setBackgroundResource(R.drawable.edittext_background);
                }
            }
        });
    }

    private void onClickPreservation(){
        icon_returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnUserFragment();
            }
        });

        preservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                if (TextUtils.isEmpty(originalPwd)){
                    Toast.makeText(AlterPasswordActivity.this, "请输入原密码：", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(newPwd)){
                    Toast.makeText(AlterPasswordActivity.this, "请输入新密码：", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(newPwdSure)){
                    Toast.makeText(AlterPasswordActivity.this, "请再次输入新密码：", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!newPwdSure.equals(newPwd)){
                    Toast.makeText(AlterPasswordActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestBody requestBody = new FormBody.Builder()
                        .add("username", data)
                        .add("originalPwd", originalPwd)
                        .add("newPwd", newPwd)
                        .build();

                commitToServer("http://192.168.43.4:8080/MIMS/auPassword", requestBody);
            }
        });
    }

    private void commitToServer(String address, RequestBody requestBody) {
        HttpUtil.sendOkHttpPostRequest(address, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(AlterPasswordActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                String flag = Utility.handleValidateResponse(responseText);
                if ("PASSWORD_ERROR".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("AlterPasswordActivity", "密码错误");
                            Toast.makeText(AlterPasswordActivity.this, "原密码错误", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                } else if ("PASSWORD_UPDATE_OK".equals(flag)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("AlterPasswordActivity", "密码修改成功");
                            returnUserFragment();
                        }
                    });
                } else if ("PASSWORD_UPDATE_FAIL".equals(flag)){
                    Log.d("AlterPasswordActivity", "密码修改失败");
                    return;
                }
            }
        });
    }

    private void getEditString(){
        originalPwd = editText_originalPwd.getText().toString().trim();
        newPwd = editText_newPwd.getText().toString().trim();
        newPwdSure = editText_newPwdSure.getText().toString().trim();
    }

    private void returnUserFragment(){
        Intent intent = new Intent(AlterPasswordActivity.this, HomeActivity.class);
        intent.putExtra("username", data);
        intent.putExtra("id", 3);
        startActivity(intent);
    }

    private void receiveData(){
        Intent intent = getIntent();
        data = intent.getStringExtra("fragment_username");
        Log.d("AlterPasswordActivity", "Activity接收Fragment的登录名"+data);
    }

}
