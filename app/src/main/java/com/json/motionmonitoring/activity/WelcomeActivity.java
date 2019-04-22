package com.json.motionmonitoring.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.json.motionmonitoring.R;

public class WelcomeActivity extends AppCompatActivity {
    private Handler mHandler;
    private TextView tv_wel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_welcome);

        init();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }, 3000);
    }

    private void init(){
        tv_wel = (TextView)findViewById(R.id.welcome);
        mHandler = new Handler();
    }
}
