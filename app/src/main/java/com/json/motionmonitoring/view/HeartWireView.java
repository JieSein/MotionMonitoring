package com.json.motionmonitoring.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class HeartWireView extends View {
    private Paint mPaint;
    private int mWindowWidth;
    private int mWindowHeight;
    private int mOffset;
    private Handler mHandler = new Handler();
    private boolean flag;

    public HeartWireView(Context context) {
        this(context, null);
    }

    public HeartWireView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartWireView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mOffset = 0;

        mPaint = new Paint();
        //设置空心
        mPaint.setStyle(Paint.Style.STROKE);
        //设置线宽
        mPaint.setStrokeWidth(2);
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //设置防抖动
        mPaint.setDither(true);
        //设置阴影
//        mPaint.setShadowLayer(25f, 5f, 10f, Color.BLACK);
        //初始化渐变颜色，因为要达到真正的透明效果，所以使用两个透明渐变到红色
        final int[] colors = new int[]{Color.argb(0, 0, 0, 0), Color.argb(0, 0, 0, 0), Color.RED};
        //启用一根新线程进行定时刷新
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    mOffset += 5;
                    //添加离散效果，让线条变得更加曲折
                    DiscretePathEffect discretePathEffect = new DiscretePathEffect(3f, 5f);
                    //添加转角圆滑
                    CornerPathEffect cornerPathEffect = new CornerPathEffect(90f);
                    //设置组合PathEffect
                    mPaint.setPathEffect(new ComposePathEffect(cornerPathEffect, discretePathEffect));
                    //设置线性渐变
                    mPaint.setShader(new LinearGradient(mOffset, 0, 800 + mOffset, 15f, colors, null, Shader.TileMode.REPEAT));
                    //刷新视图
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            invalidate();
                        }
                    });
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        //获得屏幕尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mWindowWidth = displayMetrics.widthPixels;
        mWindowHeight = displayMetrics.heightPixels;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int mWindowHeight = (Math.min(getWidth(), getHeight())) ;
        //设置折线路径
        Path path = new Path();
        //起始路径
        path.moveTo(0, mWindowHeight / 2);
        //经过路径
        path.lineTo(50, mWindowHeight / 2 + 25);
        path.lineTo(100, mWindowHeight / 2 - 25);
        path.lineTo(150, mWindowHeight / 2 + 50);
        path.lineTo(200, mWindowHeight / 2 - 50);
        path.lineTo(250, mWindowHeight / 2 + 100);
        path.lineTo(300, mWindowHeight / 2 - 75);
        path.lineTo(350, mWindowHeight / 2 + 85);
        path.lineTo(400, mWindowHeight / 2 - 95);
        path.lineTo(450, mWindowHeight / 2 + 60);
        path.lineTo(500, mWindowHeight / 2 - 45);
        path.lineTo(550, mWindowHeight / 2 + 70);
        path.lineTo(600, mWindowHeight / 2 - 50);
        path.lineTo(650, mWindowHeight / 2 + 32);
        path.lineTo(700, mWindowHeight / 2 - 45);
        path.lineTo(mWindowWidth, mWindowHeight / 2);
        //drawPath
        canvas.drawPath(path, mPaint);
    }
}
