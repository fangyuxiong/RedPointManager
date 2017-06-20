package com.xfy.sample;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xfy.redpoint.RedPointManager;

/**
 * Created by XiongFangyu on 2017/6/20.
 */
public class TestRedPointForFullScreen extends BaseFullScreenActivity implements View.OnClickListener{

    private Button button2;
    private ImageView imageView8;
    private ImageView imageView9;
    private ImageView imageView10;
    private ImageView imageView11;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_red_point_for_fullscreen);
        initView();
    }

    private void initView() {
        button2 = (Button) findViewById(R.id.button2);
        imageView8 = (ImageView) findViewById(R.id.imageView8);
        imageView9 = (ImageView) findViewById(R.id.imageView9);
        imageView10 = (ImageView) findViewById(R.id.imageView10);
        imageView11 = (ImageView) findViewById(R.id.imageView11);

        button2.setOnClickListener(this);
        imageView8.setOnClickListener(this);
        imageView9.setOnClickListener(this);
        imageView10.setOnClickListener(this);
        imageView11.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button2:
                /*if (imageView10.getVisibility() != View.VISIBLE) {
                    imageView10.setVisibility(View.VISIBLE);
                } else {
                    imageView10.setVisibility(View.GONE);
                }
                RedPointManager manager = RedPointManager.bindActivity(this);
                manager.resetRedPointBaseOnView(imageView8);
                manager.resetRedPointBaseOnView(imageView9);
                manager.resetRedPointBaseOnView(imageView10);
                manager.resetRedPointBaseOnView(imageView11);*/
                ValueAnimator anim = ValueAnimator.ofFloat(0, 500).setDuration(1000);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        final float v = (float) animation.getAnimatedValue();
                        imageView11.setTranslationX(v);
                        imageView11.setTranslationY(v);
                        RedPointManager.bindActivity(TestRedPointForFullScreen.this).translateRedPointByView(imageView11);
                    }
                });
                anim.start();
                break;
            case R.id.imageView8:
                RedPointManager.bindActivity(this).showRedPoint(v, Gravity.CENTER, 0, 0);
                break;
            case R.id.imageView9:
                RedPointManager.bindActivity(this).showRedPoint(v, Gravity.CENTER, 0, 0);
                break;
            case R.id.imageView10:
                RedPointManager.bindActivity(this).showRedPoint(v, Gravity.CENTER, 0, 0);
                break;
            case R.id.imageView11:
                RedPointManager.bindActivity(this).showRedPoint(v, Gravity.CENTER, 0, 0);
                break;
        }
    }
}
