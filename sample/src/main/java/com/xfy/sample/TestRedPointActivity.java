package com.xfy.sample;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xfy.redpoint.RedPointManager;

/**
 * Created by XiongFangyu on 2017/6/19.
 */

public class TestRedPointActivity extends Activity implements View.OnClickListener{

    private Button button;
    private TextView textView;
    private ImageView imageView;
    private ImageView imageView2;
    private ImageView imageView3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_red_point_activity);
        initView();
        initEvent();
    }

    private void initView() {
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);

    }

    private void initEvent() {
        button.setOnClickListener(this);
        textView.setOnClickListener(this);
        imageView.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
    }
    boolean bind = false;
    @Override
    public void onClick(View v) {
        if (v == button) {
            /*if (bind) {
                RedPointManager.unbindActivity(this);
                bind = false;
            }*/
            ValueAnimator anim = ValueAnimator.ofFloat(0, 90).setDuration(1000);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    final float v = (float) animation.getAnimatedValue();
                    setViewRotate(v, textView, imageView, imageView2, imageView3);
                }
            });
            anim.start();
            return;
        }
        bind = true;
        RedPointManager utils = RedPointManager.bindActivity(this)/*.setRedPointDrawableId(R.drawable.default_red_point_drawable1)*/;
        if (!utils.isShowRedPoint(v)) {
            utils.showRedPoint(v, 10, 10);
        } else {
            utils.hideRedPoint(v);
        }
    }

    private void setViewRotate(float d, View... view) {
        for (View v : view) {
            v.setRotation(d);
            RedPointManager.bindActivity(this).rotateRedPointByView(v);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RedPointManager.unbindActivity(this);
    }
}
