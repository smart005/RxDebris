package com.cloud.debris;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import com.cloud.cache.DerivedCache;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-08-19
 * Description:window生命周期管理
 * Modifier:
 * ModifyContent:
 */
public class WindowLifeCycleManager {

    private static WindowLifeCycleManager windowLifeCycleManager;

    public static WindowLifeCycleManager getInstance() {
        if (windowLifeCycleManager == null) {
            synchronized (WindowLifeCycleManager.class) {
                if (windowLifeCycleManager == null) {
                    windowLifeCycleManager = new WindowLifeCycleManager();
                }
            }
        }
        return windowLifeCycleManager;
    }

    /**
     * 绑定指定activity window层监听
     * (相对于当前activity的window,因此在调用bindEvent时需要进行对应的activity instanceof判断)
     *
     * @param activity activity
     */
    public void bindEvent(Activity activity) {
        if (activity == null) {
            return;
        }
        Window window = activity.getWindow();
        if (window == null) {
            //如果activity未显示可能会返回null
            return;
        }
        final Window.Callback callback = window.getCallback();
        if (callback == null) {
            //window未初始化完成时可能会返回null
            return;
        }
        String name = activity.getClass().getName();
        window.setCallback(new ActivityWindowCallback(name, callback));
    }

    private class ActivityWindowCallback implements Window.Callback {

        private String className;
        private Window.Callback callback;

        public ActivityWindowCallback(String className, Window.Callback callback) {
            this.className = className;
            this.callback = callback;
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            if (DerivedCache.getInstance().getBoolean(className)) {
                return false;
            }
            return callback.dispatchKeyEvent(event);
        }

        @Override
        public boolean dispatchKeyShortcutEvent(KeyEvent event) {
            return callback.dispatchKeyShortcutEvent(event);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            return callback.dispatchTouchEvent(event);
        }

        @Override
        public boolean dispatchTrackballEvent(MotionEvent event) {
            return callback.dispatchTrackballEvent(event);
        }

        @Override
        public boolean dispatchGenericMotionEvent(MotionEvent event) {
            return callback.dispatchGenericMotionEvent(event);
        }

        @Override
        public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
            return callback.dispatchPopulateAccessibilityEvent(event);
        }

        @Override
        public View onCreatePanelView(int featureId) {
            return callback.onCreatePanelView(featureId);
        }

        @Override
        public boolean onCreatePanelMenu(int featureId, Menu menu) {
            return callback.onCreatePanelMenu(featureId, menu);
        }

        @Override
        public boolean onPreparePanel(int featureId, View view, Menu menu) {
            return callback.onPreparePanel(featureId, view, menu);
        }

        @Override
        public boolean onMenuOpened(int featureId, Menu menu) {
            return callback.onMenuOpened(featureId, menu);
        }

        @Override
        public boolean onMenuItemSelected(int featureId, MenuItem item) {
            return callback.onMenuItemSelected(featureId, item);
        }

        @Override
        public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {
            callback.onWindowAttributesChanged(attrs);
        }

        @Override
        public void onContentChanged() {
            callback.onContentChanged();
        }

        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
            callback.onWindowFocusChanged(hasFocus);
        }

        @Override
        public void onAttachedToWindow() {
            callback.onAttachedToWindow();
        }

        @Override
        public void onDetachedFromWindow() {
            callback.onDetachedFromWindow();
        }

        @Override
        public void onPanelClosed(int featureId, Menu menu) {
            callback.onPanelClosed(featureId, menu);
        }

        @Override
        public boolean onSearchRequested() {
            return callback.onSearchRequested();
        }

        @SuppressLint("NewApi")
        @Override
        public boolean onSearchRequested(SearchEvent searchEvent) {
            return callback.onSearchRequested(searchEvent);
        }

        @Override
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            return null;
        }

        @Override
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
            return null;
        }

        @Override
        public void onActionModeStarted(ActionMode mode) {
            callback.onActionModeStarted(mode);
        }

        @Override
        public void onActionModeFinished(ActionMode mode) {
            callback.onActionModeFinished(mode);
        }
    }

    /**
     * 屏蔽相应事件操作(包括固件)
     *
     * @param activity activity
     */
    public void blockingEvents(Activity activity) {
        if (activity == null) {
            return;
        }
        String name = activity.getClass().getName();
        DerivedCache.getInstance().put(name, true);
    }

    /**
     * 恢复相应事件操作(包括固件)
     *
     * @param activity activity
     */
    public void restoreEvents(Activity activity) {
        if (activity == null) {
            return;
        }
        String name = activity.getClass().getName();
        DerivedCache.getInstance().remove(name);
    }
}
