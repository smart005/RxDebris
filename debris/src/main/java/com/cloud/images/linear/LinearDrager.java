package com.cloud.images.linear;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.cloud.objects.ObjectJudge;

import java.lang.ref.WeakReference;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/29
 * @Description:线性拖拽控制类
 * @Modifier:
 * @ModifyContent:
 */
public class LinearDrager<V extends LinearLayout> implements View.OnTouchListener, View.OnDragListener {

    private GestureDetector mGestureDetector;
    private float left = 0;
    private float top = 0;
    private View mDrapView = null;
    private V container = null;
    private OnLinearDragerListener onLinearDragerListener = null;

    /**
     * 获取初始化实例
     *
     * @return 初始化实例
     */
    public static LinearDrager getInstance() {
        return new LinearDrager();
    }

    /**
     * 设置线性拖拽视图回调监听
     *
     * @param listener 线性拖拽视图回调监听
     */
    public void setOnLinearDragerListener(OnLinearDragerListener listener) {
        this.onLinearDragerListener = listener;
    }

    /**
     * 初始化构建
     *
     * @param context   上下文
     * @param container 视图容器(必须继承LinearLayout)
     */
    public void builder(Context context, V container) {
        if (context == null || container == null) {
            return;
        }
        this.container = container;
        mGestureDetector = new GestureDetector(context, new DrapGestureListener());
    }

    private class DrapGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            ClipData data = ClipData.newPlainText("", "");
            MyDragShadowBuilder shadowBuilder = new MyDragShadowBuilder(
                    mDrapView);
            mDrapView.startDrag(data, shadowBuilder, mDrapView, 0);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    private class MyDragShadowBuilder extends View.DragShadowBuilder {
        private final WeakReference<View> mView;

        public MyDragShadowBuilder(View view) {
            super(view);
            mView = new WeakReference<View>(view);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            canvas.scale(1F, 1F);
            super.onDrawShadow(canvas);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            final View view = mView.get();
            if (view == null) {
                return;
            }
            shadowSize.set(view.getWidth(), view.getHeight());
            int sx = (int) (shadowSize.x / 2 - left);
            int sy = (int) (shadowSize.y / 2 - top);
            shadowTouchPoint.set(shadowSize.x / 2, shadowSize.y / 2);
            shadowTouchPoint.offset(-sx, -sy);
        }
    }

    /**
     * 设置拖拽视图
     *
     * @param views 拖拽视图
     */
    public void setDragViews(View... views) {
        if (ObjectJudge.isNullOrEmpty(views)) {
            return;
        }
        for (View view : views) {
            view.setOnTouchListener(this);
            view.setOnDragListener(this);
        }
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_ENTERED:
                v.setAlpha(0.5F);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setAlpha(1F);
                break;
            case DragEvent.ACTION_DROP:
                View view = (View) event.getLocalState();
                for (int i = 0, j = container.getChildCount(); i < j; i++) {
                    if (container.getChildAt(i) == v) {
                        container.removeView(view);
                        container.addView(view, i);
                        if (onLinearDragerListener != null) {
                            onLinearDragerListener.onLinearDrager(view, i);
                        }
                        break;
                    }
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                v.setAlpha(1F);
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mDrapView = v;
        left = event.getX();
        top = event.getY();
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        return false;
    }
}
