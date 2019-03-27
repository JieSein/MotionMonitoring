package com.json.motionmonitoring;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.json.motionmonitoring.model.User;

import org.litepal.crud.DataSupport;

import java.util.List;

public class UserInformation extends AppCompatActivity implements View.OnFocusChangeListener {

    private LinearLayout username_layout;
    private LinearLayout email_layout;
    private LinearLayout phone_layout;
    private LinearLayout sex_layout;
    private LinearLayout age_layout;
    private LinearLayout height_layout;
    private LinearLayout weight_layout;

    private TextView textview_username;
    private TextView textview_email;
    private TextView textview_phone;
    private TextView textview_sex;
    private TextView textview_age;
    private TextView textview_height;
    private TextView textview_weight;

    private TextView textview_return;

    private TextView preservation;

    private EditText usernameEdit;
    private EditText emailEdit;
    private EditText phoneEdit;
    private EditText sexEdit;
    private EditText ageEdit;
    private EditText heightEdit;
    private EditText weightEdit;

    private String usernameText;
    private String emailText;
    private String phoneText;
    private String sexText;
    private String ageText;
    private String heightText;
    private String weightText;

    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        initView();
        initEvent();
        getEditString();

        selectUser();

        onClickPreservation();
    }


    private void initView(){
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "icons/iconfont.ttf");

        textview_username = (TextView)findViewById(R.id.username_text);
        textview_username.setTypeface(iconfont);

        textview_email = (TextView)findViewById(R.id.email_text);
        textview_email.setTypeface(iconfont);

        textview_phone = (TextView)findViewById(R.id.phone_text);
        textview_phone.setTypeface(iconfont);

        textview_sex = (TextView)findViewById(R.id.sex_text);
        textview_sex.setTypeface(iconfont);

        textview_age = (TextView)findViewById(R.id.age_text);
        textview_age.setTypeface(iconfont);

        textview_height = (TextView)findViewById(R.id.height_text);
        textview_height.setTypeface(iconfont);

        textview_weight = (TextView)findViewById(R.id.weight_text);
        textview_weight.setTypeface(iconfont);

        textview_return = (TextView)findViewById(R.id.user_information_return_text_view);
        textview_return.setTypeface(iconfont);

        username_layout = (LinearLayout) findViewById(R.id.user_information_name_layout);
        email_layout = (LinearLayout) findViewById(R.id.user_information_email_layout);
        phone_layout = (LinearLayout) findViewById(R.id.user_information_phone_layout);
        sex_layout = (LinearLayout) findViewById(R.id.user_information_sex_layout);
        age_layout = (LinearLayout) findViewById(R.id.user_information_age_layout);
        height_layout = (LinearLayout) findViewById(R.id.user_information_height_layout);
        weight_layout = (LinearLayout) findViewById(R.id.user_information_weight_layout);

        usernameEdit = (EditText) findViewById(R.id.username_edit);
        emailEdit = (EditText) findViewById(R.id.email_edit);
        phoneEdit = (EditText) findViewById(R.id.phone_edit);
        sexEdit = (EditText) findViewById(R.id.sex_edit);
        ageEdit = (EditText) findViewById(R.id.age_edit);
        heightEdit = (EditText) findViewById(R.id.height_edit);
        weightEdit = (EditText) findViewById(R.id.weight_edit);

        preservation = (TextView) findViewById(R.id.user_information_edit_button);
    }

    private void initEvent(){
        usernameEdit.setOnFocusChangeListener(this);
        emailEdit.setOnFocusChangeListener(this);
        phoneEdit.setOnFocusChangeListener(this);
        sexEdit.setOnFocusChangeListener(this);
        ageEdit.setOnFocusChangeListener(this);
        heightEdit.setOnFocusChangeListener(this);
        weightEdit.setOnFocusChangeListener(this);
    }

    private void getEditString(){
        usernameText = usernameEdit.getText().toString().trim();
        emailText = emailEdit.getText().toString().trim();
        phoneText = phoneEdit.getText().toString().trim();
        sexText = sexEdit.getText().toString().trim();
        ageText = ageEdit.getText().toString().trim();
        heightText = heightEdit.getText().toString().trim();
        weightText = weightEdit.getText().toString().trim();
    }


    private void selectUser(){
        Intent intent = getIntent();
        data = intent.getStringExtra("fragment_username");
        Log.d("UserInformation", "Activity接收Fragment的登录名"+data);

        List<User> users = DataSupport.where("user_name = ?", data).find(User.class);
        for (User user : users){
            if (user != null){
                usernameEdit.setText(user.getUser_name());
                emailEdit.setText(user.getE_mail());
                phoneEdit.setText(user.getPhone());
                if (user.getSex() == 0){
                    sexEdit.setText("男");
                } else if (user.getSex() == 1){
                    sexEdit.setText("女");
                }
                ageEdit.setText(user.getAge().toString());
                heightEdit.setText(user.getHeight().toString());
                weightEdit.setText(user.getWeight().toString());
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        ContentValues values = new ContentValues();
        if (hasFocus){
            switch (v.getId()){
                case R.id.username_edit:
                    username_layout.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                    break;
                case R.id.email_edit:
                    email_layout.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                    break;
                case R.id.phone_edit:
                    phone_layout.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                    break;
                case R.id.sex_edit:
                    sex_layout.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                    break;
                case R.id.age_edit:
                    age_layout.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                    break;
                case R.id.height_edit:
                    height_layout.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                    break;
                case R.id.weight_edit:
                    weight_layout.setBackgroundResource(R.drawable.none_vertical_border_focus_edittext);
                    break;
            }
        } else {
            switch (v.getId()){
                case R.id.username_edit:
                    username_layout.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                    break;
                case R.id.email_edit:
                    email_layout.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                    break;
                case R.id.phone_edit:
                    phone_layout.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                    break;
                case R.id.sex_edit:
                    sex_layout.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                    break;
                case R.id.age_edit:
                    age_layout.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                    break;
                case R.id.height_edit:
                    height_layout.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                    break;
                case R.id.weight_edit:
                    weight_layout.setBackgroundResource(R.drawable.none_vertical_border_edittext);
                    break;
            }
        }
    }

    private void onClickPreservation(){
        textview_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnUserFragment();
            }
        });

        preservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("e_mail", emailEdit.getText().toString().trim());
                values.put("phone", phoneEdit.getText().toString().trim());
                if (sexEdit.getText().toString().trim() == "男"){
                    values.put("sex", 0);
                } else if (sexEdit.getText().toString().trim() == "女"){
                    values.put("sex", 1);
                }
                values.put("age", ageEdit.getText().toString().trim());
                values.put("height", heightEdit.getText().toString().trim());
                values.put("weight", weightEdit.getText().toString().trim());
                int result = DataSupport.updateAll(User.class, values, "user_name = ?", data);
                if (result > 0){
                    Log.d("UserInformation", "修改成功");
                    returnUserFragment();
                } else {
                    Log.d("UserInformation", "修改失败");
                }
            }
        });
    }

    private void returnUserFragment(){
        Intent intent = new Intent(UserInformation.this, HomeActivity.class);
        intent.putExtra("username", data);
        intent.putExtra("id", 3);
        startActivity(intent);
    }

}
