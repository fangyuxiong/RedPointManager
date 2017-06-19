package com.xfy.redpoint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by XiongFangyu on 2017/6/19.
 */
public class RedPointView extends View {

    private ArrayList<AnimatableDrawable> redPoints;

    public RedPointView(Context context) {
        super(context);
    }

    public RedPointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RedPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RedPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (redPoints != null) {
            for (int i = 0, l = redPoints.size(); i < l; i ++) {
                final AnimatableDrawable ad = redPoints.get(i);
                if (ad != null) {
                    ad.draw(canvas);
                }
            }
        }
    }

    public void addRedPoint(AnimatableDrawable redPoint) {
        if (redPoints == null)
            redPoints = new ArrayList<>();
        redPoints.add(redPoint);
        redPoint.setCallback(this);
        invalidate();
    }

    public void removeRedPoint(AnimatableDrawable redPoint) {
        if (redPoints != null && redPoints.contains(redPoint)) {
            redPoints.remove(redPoint);
        }
        redPoint.setCallback(null);
        invalidate();
    }

    public void removeAllRedPoint() {
        if (redPoints != null) {
            redPoints.clear();
        }
        invalidate();
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        if (drawable instanceof AnimatableDrawable) {
            invalidate();
            return;
        }
        super.invalidateDrawable(drawable);
    }
}
