package com.json.motionmonitoring;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends FragmentActivity implements View.OnClickListener {

    private LinearLayout ll_step;
    private LinearLayout ll_temp;
    private LinearLayout ll_heart;
    private LinearLayout ll_user;

    private TextView tv_step;
    private TextView tv_temp;
    private TextView tv_heart;
    private TextView tv_user;
    private TextView tv_title;

    private String step;
    private String temp;
    private String heart;
    private String user;

    private TextView icons_step;
    private TextView icons_temp;
    private TextView icons_heart;
    private TextView icons_user;

    private Fragment stepFragment;
    private Fragment tempFragment;
    private Fragment heartFragment;
    private Fragment userFragment;

    private Intent intent;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        intent = getIntent();
        data = intent.getStringExtra("username");

        initView();
        getTextView();

        initEvent();
        initFragment(0);
    }

    private void initFragment(int index){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);

        switch (index){
            case 0:
                if (stepFragment == null){
                    stepFragment = new StepFragment();
                    transaction.add(R.id.fl_content, stepFragment);
                } else {
                    transaction.show(stepFragment);
                }
                break;

            case 1:
                if (tempFragment == null){
                    tempFragment = new TempFragment();
                    transaction.add(R.id.fl_content, tempFragment);
                } else {
                    transaction.show(tempFragment);
                }
                break;

            case 2:
                if (heartFragment == null){
                    heartFragment = new HeartFragment();
                    transaction.add(R.id.fl_content, heartFragment);
                } else {
                    transaction.show(heartFragment);
                }
                break;

            case 3:
                if (userFragment == null){
                    userFragment = new UserFragment();
                    transaction.add(R.id.fl_content, userFragment);
                    dataTransfer();
                } else {
                    transaction.show(userFragment);
                }
                break;

                default:
                    break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction){
        if (stepFragment != null){
            transaction.hide(stepFragment);
        }
        if (tempFragment != null){
            transaction.hide(tempFragment);
        }
        if (heartFragment != null){
            transaction.hide(heartFragment);
        }
        if (userFragment != null){
            transaction.hide(userFragment);
        }
    }

    private void initEvent(){
        ll_step.setOnClickListener(this);
        ll_temp.setOnClickListener(this);
        ll_heart.setOnClickListener(this);
        ll_user.setOnClickListener(this);
    }

    private void initView(){
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "icons/iconfont.ttf");

        TextView textview_step = (TextView)findViewById(R.id.step_text_view);
        textview_step.setTypeface(iconfont);

        TextView textview_temp = (TextView)findViewById(R.id.temp_text_view);
        textview_temp.setTypeface(iconfont);

        TextView textview_heart = (TextView)findViewById(R.id.heart_text_view);
        textview_heart.setTypeface(iconfont);

        TextView textview_user = (TextView)findViewById(R.id.user_text_view);
        textview_user.setTypeface(iconfont);

        this.ll_step = (LinearLayout) findViewById(R.id.ll_step);
        this.ll_temp = (LinearLayout) findViewById(R.id.ll_temp);
        this.ll_heart = (LinearLayout) findViewById(R.id.ll_heart);
        this.ll_user = (LinearLayout) findViewById(R.id.ll_user);

        this.tv_step = (TextView) findViewById(R.id.tv_step);
        this.tv_temp = (TextView) findViewById(R.id.tv_temp);
        this.tv_heart = (TextView) findViewById(R.id.tv_heart);
        this.tv_user = (TextView) findViewById(R.id.tv_user);

        this.tv_title = (TextView) findViewById(R.id.tv_title);

        this.icons_step = (TextView) findViewById(R.id.step_text_view);
        this.icons_temp = (TextView) findViewById(R.id.temp_text_view);
        this.icons_heart = (TextView) findViewById(R.id.heart_text_view);
        this.icons_user = (TextView) findViewById(R.id.user_text_view);

    }

    private void getTextView(){
        step = tv_step.getText().toString().trim();
        temp = tv_temp.getText().toString().trim();
        heart = tv_heart.getText().toString().trim();
        user = tv_user.getText().toString().trim();

        icons_step.setTextColor(0xFF33B5E5);
        tv_step.setTextColor(0xFF33B5E5);
        tv_title.setText(step);
    }

    @Override
    public void onClick(View v) {
        restartButton();

        switch (v.getId()){
            case R.id.ll_step:
                icons_step.setTextColor(0xFF33B5E5);
                tv_step.setTextColor(0xFF33B5E5);
                tv_title.setText(step);
                initFragment(0);
                break;

            case R.id.ll_temp:
                icons_temp.setTextColor(0xFF33B5E5);
                tv_temp.setTextColor(0xFF33B5E5);
                tv_title.setText(temp);
                initFragment(1);
                break;

            case R.id.ll_heart:
                icons_heart.setTextColor(0xFF33B5E5);
                tv_heart.setTextColor(0xFF33B5E5);
                tv_title.setText(heart);
                initFragment(2);
                break;

            case R.id.ll_user:
                icons_user.setTextColor(0xFF33B5E5);
                tv_user.setTextColor(0xFF33B5E5);
                tv_title.setText(user);
                initFragment(3);
                break;

                default:
                    break;
        }
    }

    private void restartButton(){
        icons_step.setTextColor(0xFF717070);
        icons_temp.setTextColor(0xFF717070);
        icons_heart.setTextColor(0xFF717070);
        icons_user.setTextColor(0xFF717070);

        tv_step.setTextColor(0xFF717070);
        tv_temp.setTextColor(0xFF717070);
        tv_heart.setTextColor(0xFF717070);
        tv_user.setTextColor(0xFF717070);
    }

    private void dataTransfer(){
        Bundle bundle = new Bundle();
        bundle.putString("usernames", data);
        userFragment.setArguments(bundle);
        Log.d("Fragment", "HomeActivity传输的登录名"+data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = intent.getIntExtra("id", 0);
        data = intent.getStringExtra("username");
        Log.d("HomeActivity", "定位"+id);
        if (id == 3){
            restartButton();
            icons_user.setTextColor(0xFF33B5E5);
            tv_user.setTextColor(0xFF33B5E5);
            tv_title.setText(user);
            initFragment(id);
        }
    }
}
