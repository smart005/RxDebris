package com.cloud.dialogs.toasty;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-05-17
 * Description:在部分手机NotificationManagerService权限未开启情况下使用
 * Modifier:
 * ModifyContent:
 */
public class CustomToast implements IToast {

    private Handler mHandler = new Handler();

    /**
     * 维护toast的队列
     */
    private BlockingQueue<CustomToast> mQueue = new LinkedBlockingDeque<>();

    /**
     * 原子操作：判断当前是否在读取{@linkplain #mQueue 队列}来显示toast
     */
    private AtomicInteger mAtomicInteger = new AtomicInteger(0);

    private WindowManager mWindowManager;

    private long mDurationMillis;

    private View mView;

    private WindowManager.LayoutParams mParams;

    private Context mContext;

    public void makeTextShow(String text, long duration) {
        new CustomToast(mContext)
                .setText(text)
                .setDuration(duration)
                .setGravity(Gravity.BOTTOM, 0, 30).show();
    }

    public IToast makeText(Context context, String text, long duration) {
        return new CustomToast(context)
                .setText(text)
                .setDuration(duration)
                .setGravity(Gravity.BOTTOM, 0, 30);
    }

    /**
     * 参照Toast源码TN()写
     *
     * @param context
     */
    public CustomToast(Context context) {
        mContext = context;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = android.R.style.Animation_Toast;
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.setTitle("Toast");
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager
                .LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        // 默认居中
        mParams.gravity = Gravity.CENTER;
    }

    /**
     * Set the location at which the notification should appear on the screen.
     *
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public IToast setGravity(int gravity, int xOffset, int yOffset) {
        // We can resolve the Gravity here by using the Locale for getting
        // the layout direction
        final int finalGravity;
        if (Build.VERSION.SDK_INT >= 14) {
            final Configuration config = mView.getContext().getResources().getConfiguration();
            finalGravity = Gravity.getAbsoluteGravity(gravity, config.getLayoutDirection());
        } else {
            finalGravity = gravity;
        }
        mParams.gravity = finalGravity;
        if ((finalGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
            mParams.horizontalWeight = 1.0f;
        }
        if ((finalGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
            mParams.verticalWeight = 1.0f;
        }
        mParams.y = yOffset;
        mParams.x = xOffset;
        return this;
    }

    @Override
    public IToast setDuration(long durationMillis) {
        if (durationMillis < 0) {
            mDurationMillis = 0;
        }
        if (durationMillis == Toast.LENGTH_SHORT) {
            mDurationMillis = 2000;
        } else if (durationMillis == Toast.LENGTH_LONG) {
            mDurationMillis = 3500;
        } else {
            mDurationMillis = durationMillis;
        }
        return this;
    }

    /**
     * 不能和{@link #setText(String)}一起使用，要么{@link #setView(View)} 要么{@link #setView(View)}
     *
     * @param view 传入view
     * @return 自身对象
     */
    @Override
    public IToast setView(View view) {
        mView = view;
        return this;
    }

    @Override
    public IToast setMargin(float horizontalMargin, float verticalMargin) {
        mParams.horizontalMargin = horizontalMargin;
        mParams.verticalMargin = verticalMargin;
        return this;
    }

    /**
     * 不能和{@link #setView(View)}一起使用，要么{@link #setView(View)} 要么{@link #setView(View)}
     *
     * @param text 字符串
     * @return 自身对象
     */
    public IToast setText(String text) {
        // 模拟Toast的布局文件 com.android.internal.R.layout.transient_notification
        // 虽然可以手动用java写，但是不同厂商系统，这个布局的设置好像是不同的，因此我们自己获取原生Toast的view进行配置
        View view = Toast.makeText(mContext, text, Toast.LENGTH_SHORT).getView();
        if (view != null) {
            TextView tv = (TextView) view.findViewById(android.R.id.message);
            tv.setText(text);
            setView(view);
        }
        return this;
    }

    @Override
    public void show() {
        if (0 == mAtomicInteger.get() && mQueue.isEmpty()) {
            // 1. 将本次需要显示的toast加入到队列中
            mQueue.offer(this);
            // 2. 如果队列还没有激活，就激活队列，依次展示队列中的toast
            if (0 == mAtomicInteger.get()) {
                mAtomicInteger.incrementAndGet();
                mHandler.post(mActivite);
            }
        } else {
            CustomToast poll = mQueue.poll();
            if (poll != null) {
                if (poll.mView.getParent() != null) {
                    poll.mWindowManager.removeView(poll.mView);
                }
                // 同时从队列中移除这个toast
                mQueue.poll();
            }
            if (mAtomicInteger.intValue() > 0) {
                mAtomicInteger.decrementAndGet();
            }
            show();
        }
    }

    @Override
    public void cancel() {
        // 1. 如果队列已经处于非激活状态或者队列没有toast了，就表示队列没有toast正在展示了，直接return
        if (0 == mAtomicInteger.get() && mQueue.isEmpty()) return;

        // 2. 当前显示的toast是否为本次要取消的toast，如果是的话
        // 2.1 先移除之前的队列逻辑
        // 2.2 立即暂停当前显示的toast
        // 2.3 重新激活队列
        if (this.equals(mQueue.peek())) {
            mHandler.removeCallbacks(mActivite);
            mHandler.post(mHide);
            mHandler.post(mActivite);
        }
    }

    private void handleShow() {
        if (mView != null) {
            if (mView.getParent() != null) {
                mWindowManager.updateViewLayout(mView, mParams);
            } else {
                mWindowManager.addView(mView, mParams);
            }
        }
    }

    private void handleHide() {
        if (mView != null) {
            // note: checking parent() just to make sure the view has
            // been added...  i have seen cases where we get here when
            // the view isn't yet added, so let's try not to crash.
            if (mView.getParent() != null) {
                mWindowManager.removeView(mView);
                // 同时从队列中移除这个toast
                mQueue.poll();
            }
            mView = null;
        }
    }

    private void activeQueue() {
        CustomToast toast = mQueue.peek();
        if (toast == null) {
            // 如果不能从队列中获取到toast的话，那么就表示已经暂时完所有的toast了
            // 这个时候需要标记队列状态为：非激活读取
            if (mAtomicInteger.intValue() > 0) {
                mAtomicInteger.decrementAndGet();
            }
        } else {
            // 如果还能从队列中获取到toast的话，那么就表示还有toast没有展示
            // 展示队首的toast
            mHandler.post(toast.mShow);
            //设置一定时间后主动采取toast消失措施
            mHandler.postDelayed(toast.mHide, toast.mDurationMillis);
            //设置展示完毕之后再次执行本逻辑，以展示下一个toast
            mHandler.postDelayed(mActivite, toast.mDurationMillis);
        }

    }

    private final Runnable mShow = new Runnable() {
        @Override
        public void run() {
            handleShow();
        }
    };

    private final Runnable mHide = new Runnable() {
        @Override
        public void run() {
            handleHide();
        }
    };

    private final Runnable mActivite = new Runnable() {
        @Override
        public void run() {
            activeQueue();
        }
    };
}
