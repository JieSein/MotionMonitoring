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
import com.json.motionmonitoring.util.Validator;

import org.litepal.crud.DataSupport;

import java.util.List;

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

    private String selectPwdByName(){
        List<User> users = DataSupport.where("user_name = ?", data).find(User.class);
        for (User user : users){
            if (user != null){
                Log.d("AlterPasswordActivity", "selectPwdByName: "+user.getPassword());
                return  user.getPassword();
            }
        }
        return "";
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
                String password = selectPwdByName();
                if (TextUtils.isEmpty(EdittextContent.getEditString(editText_originalPwd))){
                    Toast.makeText(AlterPasswordActivity.this, "请输入原密码：", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!EdittextContent.getEditString(editText_originalPwd).equals(password)) {
                    Toast.makeText(AlterPasswordActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(EdittextContent.getEditString(editText_newPwd))){
                    Toast.makeText(AlterPasswordActivity.this, "请输入新密码：", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Validator.verifyPassword(EdittextContent.getEditString(editText_newPwd)) == false) {
                    Toast.makeText(AlterPasswordActivity.this, "请输入6到20位的数字或字母", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(EdittextContent.getEditString(editText_newPwdSure))){
                    Toast.makeText(AlterPasswordActivity.this, "请再次输入新密码：", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!EdittextContent.getEditString(editText_newPwdSure).equals(EdittextContent.getEditString(editText_newPwd))){
                    Toast.makeText(AlterPasswordActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues values = new ContentValues();
                values.put("password", EdittextContent.getEditString(editText_newPwd));
                int result = DataSupport.updateAll(User.class, values, "user_name = ?", data);
                if (result > 0){
                    Log.d("AlterPasswordActivity", "密码修改成功");
                    returnUserFragment();
                } else {
                    Log.d("AlterPasswordActivity", "密码修改失败");
                }
            }
        });
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
