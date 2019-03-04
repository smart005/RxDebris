package com.cloud.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/5/16
 * @Description:解决高版本sdk输入法崩溃
 * @Modifier:
 * @ModifyContent:
 */
public class InputEditText extends EditText {
    private OnFinishComposingListener mFinishComposingListener;

    public interface OnFinishComposingListener {
        public void finishComposing();
    }

    public InputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InputEditText(Context context) {
        super(context);
    }

    public void setOnFinishComposingListener(OnFinishComposingListener listener) {
        this.mFinishComposingListener = listener;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection connection = super.onCreateInputConnection(outAttrs);
        if (connection == null) {
            return null;
        }
        return new MyInputConnection(super.onCreateInputConnection(outAttrs), true);
    }

    public class MyInputConnection extends InputConnectionWrapper {
        public MyInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean finishComposingText() {
            boolean finishComposing = super.finishComposingText();
            if (mFinishComposingListener != null) {
                mFinishComposingListener.finishComposing();
            }
            return finishComposing;
        }
    }
}
