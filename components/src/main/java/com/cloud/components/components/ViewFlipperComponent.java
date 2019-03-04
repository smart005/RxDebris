package com.cloud.components.components;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ViewFlipper;

import com.cloud.components.R;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/28
 * Description:可对添加ViewFlipper中的view进行手势循环切换
 * Modifier:
 * ModifyContent:
 */
public class ViewFlipperComponent {

    private GestureDetector mGestureDetector;
    private Context context = null;
    private ViewFlipper viewFlipper = null;
    private OnScrollChangeListener onScrollChangeListener = null;
    private OnItemClickListener onItemClickListener = null;
    private int position = 0;

    /**
     * 构建ViewFlipper
     *
     * @param context     上下文
     * @param viewFlipper ViewFlipper对象(添加view可调用ViewFlipper.addView(...))
     */
    public void build(Context context, ViewFlipper viewFlipper) {
        if (context == null || viewFlipper == null) {
            return;
        }
        this.context = context;
        this.viewFlipper = viewFlipper;
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        mGestureDetector = new GestureDetector(context, customGestureDetector);
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });
        viewFlipper.setDisplayedChild(position);
    }

    private class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // Swipe left (next)
            if (e1.getX() > e2.getX()) {
                int childCount = viewFlipper.getChildCount();
                if (childCount > 1) {
                    viewFlipper.setInAnimation(context, R.anim.cl_flipper_left_in);
                    viewFlipper.setOutAnimation(context, R.anim.cl_flipper_left_out);
                    viewFlipper.showNext();
                    if (onScrollChangeListener != null) {
                        int position = viewFlipper.getDisplayedChild();
                        if (childCount > 0 && position >= 0 && position < childCount) {
                            ViewFlipperComponent.this.position = position;
                            onScrollChangeListener.onScrollChange(position, velocityX, velocityY);
                        }
                    }
                }
            }

            // Swipe right (previous)
            if (e1.getX() < e2.getX()) {
                int childCount = viewFlipper.getChildCount();
                if (childCount > 1) {
                    viewFlipper.setInAnimation(context, R.anim.cl_flipper_right_in);
                    viewFlipper.setOutAnimation(context, R.anim.cl_flipper_right_out);
                    viewFlipper.showPrevious();
                    if (onScrollChangeListener != null) {
                        int position = viewFlipper.getDisplayedChild();
                        if (childCount > 0 && position >= 0 && position < childCount) {
                            ViewFlipperComponent.this.position = position;
                            onScrollChangeListener.onScrollChange(position, velocityX, velocityY);
                        }
                    }
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    /**
     * 设置当前显示视图
     *
     * @param position 索引
     */
    public void setCurrentItem(int position) {
        this.position = position < 0 ? 0 : position;
    }

    public interface OnScrollChangeListener {

        /**
         * 滚动改变监听
         *
         * @param position  改变之后索引
         * @param velocityX x轴方向滑动距离
         * @param velocityY y轴方向滑动距离
         */
        public void onScrollChange(int position, float velocityX, float velocityY);
    }

    public interface OnItemClickListener {
        /**
         * Callback method to be invoked when an item in this ViewFlipper has
         * been clicked.(GestureDetector onSingleTapConfirmed)
         * <p>
         * Implementers can call getItemAtPosition(position) if they need to
         * access the data associated with the selected item.
         *
         * @param position The position of the view in the adapter.
         * @param id       The row id of the item that was clicked.
         */
        public void onItemClick(int position);
    }

    /**
     * setting ViewFlipper item click listener
     *
     * @param listener ViewFlipper item click listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    /**
     * 设置滚动改变监听
     *
     * @param listener 监听对象
     */
    public void setOnScrollChangeListener(OnScrollChangeListener listener) {
        this.onScrollChangeListener = listener;
    }
}
