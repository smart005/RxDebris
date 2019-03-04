package com.cloud.dialogs.digitalkeys;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.cloud.debris.R;
import com.cloud.dialogs.plugs.DialogPlus;
import com.cloud.objects.bases.BaseDialogPlugFragment;
import com.cloud.objects.events.Action1;
import com.cloud.objects.events.Func0;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/8
 * @Description:数字键盘
 * @Modifier:
 * @ModifyContent:
 */
public class DigitalKeyboardFragment extends BaseDialogPlugFragment<DigitalKeyboardUtils, DialogPlus> {

    private DigitalKeyboardViewHolder holder = null;
    private OnDigitalKeyBoardListener onDigitalKeyBoardListener = null;
    private List<String> datalist = new ArrayList<String>();
    private Action1<InputDigitalKeyType> closeAction = null;
    private Func0<EditText> focusEditFunction = null;
    private Action1<EditText> deleteAction = null;

    public void setDeleteAction(Action1<EditText> deleteAction) {
        this.deleteAction = deleteAction;
    }

    public void setFocusEditFunction(Func0<EditText> focusEditFunction) {
        this.focusEditFunction = focusEditFunction;
    }

    public void setCloseAction(Action1<InputDigitalKeyType> action) {
        this.closeAction = action;
    }

    /**
     * 设置数字键盘监听器
     *
     * @param listener
     */
    public void setOnDigitalKeyBoardListener(OnDigitalKeyBoardListener listener) {
        this.onDigitalKeyBoardListener = listener;
    }

    @Override
    public void build(View contentView, Context context, DigitalKeyboardUtils keyboardUtils, DialogPlus dialogPlus) {
        super.build(contentView, context, keyboardUtils, dialogPlus);
        holder = new DigitalKeyboardViewHolder(contentView);
        buildDataList();
        DigitalKeyBoardAdapter keyBoardAdapter = new DigitalKeyBoardAdapter(context, keyboardUtils, datalist, dialogPlus, onDigitalKeyBoardListener);
        keyBoardAdapter.setCloseAction(closeAction);
        keyBoardAdapter.setFocusEditFunction(focusEditFunction);
        holder.digitalKeybordGv.setAdapter(keyBoardAdapter);
    }

    public class DigitalKeyboardViewHolder implements View.OnClickListener {

        private View delRl;
        private View confirmTv;
        private GridView digitalKeybordGv;

        public DigitalKeyboardViewHolder(View view) {
            delRl = view.findViewById(R.id.del_rl);
            delRl.setOnClickListener(this);
            confirmTv = view.findViewById(R.id.confirm_tv);
            confirmTv.setOnClickListener(this);
            digitalKeybordGv = (GridView) view.findViewById(R.id.digital_keybord_gv);
        }

        @Override
        public void onClick(View v) {
            if (onDigitalKeyBoardListener == null) {
                return;
            }
            int id = v.getId();
            if (id == R.id.del_rl) {
                EditText editText = null;
                if (focusEditFunction != null) {
                    editText = focusEditFunction.call();
                }
                if (editText != null && deleteAction != null) {
                    deleteAction.call(editText);
                }
                onDigitalKeyBoardListener.onKeyBoardListener(getData(), InputDigitalKeyType.del, "", editText);
            } else if (id == R.id.confirm_tv) {
                EditText editText = null;
                if (focusEditFunction != null) {
                    editText = focusEditFunction.call();
                }
                onDigitalKeyBoardListener.onKeyBoardListener(getData(), InputDigitalKeyType.confirm, "", editText);
                DialogPlus dialogPlug = getDialogPlug();
                if (dialogPlug != null) {
                    dialogPlug.dismiss();
                }
                if (closeAction != null) {
                    closeAction.call(InputDigitalKeyType.confirm);
                }
            }
        }
    }

    private void buildDataList() {
        for (int i = 1; i <= 9; i++) {
            datalist.add(String.valueOf(i));
        }
        datalist.add(".");
        datalist.add("0");
        datalist.add("close");
    }
}
