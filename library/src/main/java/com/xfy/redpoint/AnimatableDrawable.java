package com.xfy.redpoint;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by XiongFangyu on 2017/6/19.
 *
 * 包裹drawable，可执行动画，目前只有旋转动画
 */
public class AnimatableDrawable extends Drawable {

    private Drawable mDrawable;

    private Rect containerRect;
    private int gravity;
    private int marginX, marginY;

    private float rotateDegree = 0;
    private float rotateCenterX = Float.NaN, rotateCenterY = Float.NaN;

    private float translateX, translateY;

    public AnimatableDrawable(){
    }

    public AnimatableDrawable(Drawable d) {
        this();
        setDrawable(d);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mDrawable != null && isVisible()) {
            canvas.save();
            canvas.translate(translateX, translateY);
            if (rotateDegree != 0 && !Float.isNaN(rotateCenterX) && !Float.isNaN(rotateCenterY))
                canvas.rotate(rotateDegree, rotateCenterX, rotateCenterY);
            mDrawable.draw(canvas);
            canvas.restore();
        }
    }

    @Override
    public void setAlpha(int alpha) {
        if (mDrawable != null)
            mDrawable.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        if (mDrawable != null)
            mDrawable.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        this.mDrawable = drawable;
    }

    public void setRotateCenter(float x, float y) {
        rotateCenterX = x;
        rotateCenterY = y;
    }

    public void rotate(float degree) {
        this.rotateDegree = degree;
        invalidateSelf();
    }

    public float getTranslateX() {
        return translateX;
    }

    public void setTranslateX(float translateX) {
        this.translateX = translateX;
        invalidateSelf();
    }

    public float getTranslateY() {
        return translateY;
    }

    public void setTranslateY(float translateY) {
        this.translateY = translateY;
        invalidateSelf();
    }

    public float getRotateCenterX() {
        return rotateCenterX;
    }

    public float getRotateCenterY() {
        return rotateCenterY;
    }

    public void reset() {
        rotateDegree = 0;
        rotateCenterY = Float.NaN;
        rotateCenterX = Float.NaN;
        translateX = 0;
        translateY = 0;
    }

    @Override
    public void setBounds(int l,int t,int r,int b){
        if (mDrawable != null)
            mDrawable.setBounds(l,t,r,b);
    }

    public void setContainerRect(Rect containerRect) {
        this.containerRect = containerRect;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public void setMargin(int marginX, int marginY) {
        this.marginX = marginX;
        this.marginY = marginY;
    }

    public int getGravity() {
        return gravity;
    }

    public int getMarginX() {
        return marginX;
    }

    public int getMarginY() {
        return marginY;
    }

    public Rect getContainerRect() {
        return containerRect;
    }

    @Override
    public int getIntrinsicWidth() {
        return mDrawable != null ? mDrawable.getIntrinsicWidth() : 0;
    }

    @Override
    public int getIntrinsicHeight() {
        return mDrawable != null ? mDrawable.getIntrinsicHeight() : 0;
    }

    public int getWidth() {
        return getBounds().width();
    }

    public int getHeight() {
        return getBounds().height();
    }

}
