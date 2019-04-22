package com.json.motionmonitoring.activity;

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

import com.json.motionmonitoring.R;

public class HomeActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "HomeActivity";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";;
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICE_RSSI = "RSSI";
    public static final String STEP_DATA_AVAILABLE = "STEP_DATA";
    public static final String TEMP_DATA_AVAILABLE = "TEMP_DATA";
    public static final String HEART_DATA_AVAILABLE = "HEART_DATA";

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    int fragment_flag = 1;

    private Bundle userBundle;
    private Bundle address_bundle;

    private LinearLayout ll_step, ll_temp, ll_heart, ll_user;
    private TextView tv_step, tv_temp, tv_heart, tv_user, tv_title;
    private String step, temp, heart, user;
    private TextView icons_step, icons_temp, icons_heart, icons_user;
    private Fragment stepFragment, tempFragment, heartFragment, userFragment;
    private String rev_username;

    private String mDeviceName;
    private String mDeviceAddress;
    private String mRssi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        Log.d(TAG, "HomeActivity onCreate: ");
        initView();
        getTextView();
        stepFragment = new StepFragment();
        tempFragment = new TempFragment();
        heartFragment = new HeartFragment();

        receiveData();

        address_bundle = new Bundle();
        address_bundle.putString(HomeActivity.EXTRAS_DEVICE_ADDRESS, mDeviceAddress);
        stepFragment.setArguments(address_bundle);
        tempFragment.setArguments(address_bundle);
        heartFragment.setArguments(address_bundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "HomeActivity onResume: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

        ll_step.setOnClickListener(this);
        ll_temp.setOnClickListener(this);
        ll_heart.setOnClickListener(this);
        ll_user.setOnClickListener(this);
    }

    private void indexFragment(int index){
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        if (fragment_flag == 1){
            transaction.add(R.id.fl_content, stepFragment);
            transaction.add(R.id.fl_content, tempFragment);
            transaction.add(R.id.fl_content, heartFragment);
            fragment_flag++;
        }
        hideFragment(transaction);
        switch (index){
            case 0:
                transaction.show(stepFragment);
                break;

            case 1:
                transaction.show(tempFragment);
                break;

            case 2:
                transaction.show(heartFragment);
                break;

            case 3:
                if (userFragment == null){
                    userFragment = new UserFragment();
                    transaction.add(R.id.fl_content, userFragment);
                    sendUserData();
                } else {
                    userFragment.setArguments(address_bundle);
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

    private void getTextView(){
        step = tv_step.getText().toString().trim();
        temp = tv_temp.getText().toString().trim();
        heart = tv_heart.getText().toString().trim();
        user = tv_user.getText().toString().trim();

        icons_step.setTextColor(0xFF66C480);
        tv_step.setTextColor(0xFF66C480);
        tv_title.setText(step);
    }

    @Override
    public void onClick(View v) {
        restartButton();

        switch (v.getId()){
            case R.id.ll_step:
                icons_step.setTextColor(0xFF66C480);
                tv_step.setTextColor(0xFF66C480);
                tv_title.setText(step);
                indexFragment(0);
                break;

            case R.id.ll_temp:
                icons_temp.setTextColor(0xFF66C480);
                tv_temp.setTextColor(0xFF66C480);
                tv_title.setText(temp);
                indexFragment(1);
                break;

            case R.id.ll_heart:
                icons_heart.setTextColor(0xFF66C480);
                tv_heart.setTextColor(0xFF66C480);
                tv_title.setText(heart);
                indexFragment(2);
                break;

            case R.id.ll_user:
                icons_user.setTextColor(0xFF66C480);
                tv_user.setTextColor(0xFF66C480);
                tv_title.setText(user);
                indexFragment(3);
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

    private void sendUserData(){
        userBundle = new Bundle();
        userBundle.putString(LoginActivity.USERNAME, rev_username);
        userBundle.putString(EXTRAS_DEVICE_ADDRESS, mDeviceAddress);
        userFragment.setArguments(userBundle);
        Log.d("Fragment", "HomeActivity传输的登录名"+rev_username);
    }

    private void receiveData(){
        Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(MainActivity.EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(MainActivity.EXTRAS_DEVICE_ADDRESS);
        mRssi = intent.getStringExtra(MainActivity.EXTRAS_DEVICE_RSSI);
        rev_username = intent.getStringExtra(LoginActivity.USERNAME);
        int id = intent.getIntExtra("id", 0);
        Log.d(TAG, "BleScan_Activity传输的蓝牙名称: "+mDeviceName);
        Log.d(TAG, "BleScan_Activity传输的蓝牙MAC: "+mDeviceAddress);
        Log.d(TAG, "BleScan_Activity传输的蓝牙Rssi: "+mRssi);
        Log.d(TAG, "BleScan_Activity传输的用户名: "+rev_username);
        Log.d(TAG, "定位:: "+ id);

        if (id == 0){
            restartButton();
            icons_step.setTextColor(0xFF66C480);
            tv_step.setTextColor(0xFF66C480);
            tv_title.setText(step);
            indexFragment(id);
        }
        if (id == 3){
            restartButton();
            icons_user.setTextColor(0xFF66C480);
            tv_user.setTextColor(0xFF66C480);
            tv_title.setText(user);
            indexFragment(id);
        }
    }

}
