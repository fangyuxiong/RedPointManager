package com.xfy.redpoint;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.HashMap;

/**
 * Created by XiongFangyu on 2017/6/19.
 *
 * 红点管理类，一个Activity对应一个管理类（实际是一个{@link RedPointView}）对应一个管理类
 * 默认：
 *  一个view对应一个红点，请在view没有做任何动画时添加红点
 *  添加：{@link #showRedPoint(View, int, int, int)} or {@link #showRedPoint(View, int, int)}
 *  隐藏：{@link #hideRedPoint(View)}
 *  删除：{@link #removeRedPoint(View)}
 *  是否显示：{@link #isShowRedPoint(View)}
 * 目前仅支持红点以view中心点旋转 {@link #rotateRedPoint(View, float)} {@link #rotateRedPointByView(View)}
 * 可自定义红点样式{@link #setRedPointDrawableId(int)}
 * Activity onDestroy时务必调用 {@link #unbindActivity(Activity)}
 */
public class RedPointManager {
    private static final int DEFAULT_LAYOUT_ID = R.layout.red_point_view;
    private static final int DEFAULT_RED_POINT_DRAWABLE = R.drawable.default_red_point_drawable;

    private static HashMap<Activity, RedPointManager> Pool;

    public static RedPointManager bindActivity(Activity activity) {
        RedPointManager result = findFromPool(activity);
        if (result != null) {
            return result;
        }
        ViewGroup container = (ViewGroup) activity.findViewById(android.R.id.content);
        RedPointView redPointView = (RedPointView) LayoutInflater.from(activity).inflate(DEFAULT_LAYOUT_ID, null);
        container.addView(redPointView);
        result = new RedPointManager(redPointView);
        result.getStatusBarHeight(activity);
        saveToPool(activity, result);
        return result;
    }

    public static void unbindActivity(Activity activity) {
        if (Pool != null) {
            RedPointManager redPointManager = Pool.remove(activity);
            if (redPointManager != null) {
                redPointManager.release();
            }
        }
    }

    private static RedPointManager findFromPool(Activity activity) {
        if (Pool != null) {
            return Pool.get(activity);
        }
        return null;
    }

    private static void saveToPool(Activity activity, RedPointManager ma) {
        if (Pool == null) {
            Pool = new HashMap<>();
        }
        Pool.put(activity, ma);
    }

    private RedPointView redPointView;
    private HashMap<View, AnimatableDrawable> points;
    private int redPointDrawableId = DEFAULT_RED_POINT_DRAWABLE;
    private int statusHeight = -404;

    public RedPointManager(RedPointView redPointView) {
        this.redPointView = redPointView;
    }

    public RedPointManager setRedPointDrawableId(int id) {
        redPointDrawableId = id;
        return this;
    }

    /**
     * 在targetView上添加或显示一个红点，默认gravity为{@link Gravity#RIGHT}
     * @param targetView    在此view上显示红点
     * @param xMargin       margin right
     * @param yMargin       margin bottom
     */
    public void showRedPoint(View targetView, int xMargin, int yMargin) {
        showRedPoint(targetView, Gravity.RIGHT | Gravity.TOP, xMargin, yMargin);
    }

    /**
     * 在targetView上添加或显示一个红点
     * @param targetView    在此view上显示红点
     * @param gravity       根据gravity来确定红点位置
     * @param xMargin       Offset to apply to the X axis.  If gravity is LEFT this
     *                      pushes it to the right; if gravity is RIGHT it pushes it to
     *                      the left; if gravity is CENTER_HORIZONTAL it pushes it to the
     *                      right or left; otherwise it is ignored.
     * @param yMargin       Offset to apply to the Y axis.  If gravity is TOP this pushes
     *                      it down; if gravity is BOTTOM it pushes it up; if gravity is
     *                      CENTER_VERTICAL it pushes it down or up; otherwise it is
     *                      ignored.
     */
    public void showRedPoint(View targetView, int gravity, int xMargin, int yMargin) {
        AnimatableDrawable redPoint = findRedPoint(targetView);
        if (redPoint == null) {
            redPoint = new AnimatableDrawable(getDrawable(targetView, redPointDrawableId));
            save(targetView, redPoint);
        }
        initRedPoint(targetView, redPoint, gravity, xMargin, yMargin);
        redPoint.setVisible(true, false);
        redPointView.addRedPoint(redPoint);
    }

    /**
     * 隐藏此view上的红点
     * @param targetView
     */
    public void hideRedPoint(View targetView) {
        AnimatableDrawable redPoint = findRedPoint(targetView);
        if (redPoint != null) {
            redPoint.setVisible(false, false);
            redPoint.invalidateSelf();
        }
    }

    /**
     * 删除此view上的红点
     * @param targetView
     */
    public void removeRedPoint(View targetView) {
        AnimatableDrawable redPoint = findRedPoint(targetView);
        if (redPoint != null) {
            redPointView.removeRedPoint(redPoint);
            points.remove(targetView);
        }
    }

    /**
     * 红点是否显示
     * @param targetView
     * @return
     */
    public boolean isShowRedPoint(View targetView) {
        AnimatableDrawable redPoint = findRedPoint(targetView);
        if (redPoint != null) {
            return redPoint.isVisible();
        }
        return false;
    }

    /**
     * 按照targetView中心旋转红点，旋转角度为targetView的旋转角度
     * @param targetView
     */
    public void rotateRedPointByView(View targetView) {
        rotateRedPoint(targetView, targetView.getRotation());
    }

    /**
     * 按照targetView中心旋转红点
     * @param targetView    目标view，如果没有找到对应的红点，返回
     * @param degree        旋转角度 in degree
     */
    @SuppressWarnings("Range")
    public void rotateRedPoint(View targetView, float degree) {
        AnimatableDrawable redPoint = findRedPoint(targetView);
        if (redPoint == null)
            return;
        Rect container = redPoint.getContainerRect();
        redPoint.setRotateCenter(container.centerX(), container.centerY());
        redPoint.rotate(degree);
    }

    private void release() {
        redPointView.removeAllRedPoint();
        ViewParent parent = redPointView.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(redPointView);
        }
        if (points != null) {
            points.clear();
        }
        redPointView = null;
    }

    private void initRedPoint(View targetView, AnimatableDrawable redPoint, int gravity, int xMargin, int yMargin) {
        Rect rect = new Rect(0, 0, targetView.getWidth(), targetView.getHeight());
        int[] loc = new int[2];
        targetView.getLocationInWindow(loc);
        rect.offset(loc[0], loc[1] - statusHeight);
        Rect bounds = new Rect();
        Gravity.apply(gravity, redPoint.getIntrinsicWidth(), redPoint.getIntrinsicHeight(), rect, xMargin, yMargin, bounds);
        redPoint.setBounds(bounds);
        redPoint.setContainerRect(rect);
    }

    private void save(View targetView, AnimatableDrawable redPoint) {
        if (points == null)
            points = new HashMap<>();
        points.put(targetView, redPoint);
    }

    private AnimatableDrawable findRedPoint(View targetView) {
        if (points != null) {
            return points.get(targetView);
        }
        return null;
    }

    private Drawable getDrawable(View parent, int id) {
        return parent.getResources().getDrawable(id);
    }

    private int getStatusBarHeight(Context context) {
        if (statusHeight != -404) {
            return statusHeight;
        }
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int height = context.getResources().getDimensionPixelSize(resourceId);
        if (height == 0) {
            height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25f, context.getResources().getDisplayMetrics()));
        }
        statusHeight = height;
        return statusHeight;
    }
}
