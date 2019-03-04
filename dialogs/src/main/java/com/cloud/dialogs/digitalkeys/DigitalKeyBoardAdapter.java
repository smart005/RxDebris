package com.cloud.dialogs.digitalkeys;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.cloud.dialogs.R;
import com.cloud.dialogs.plugs.DialogPlus;
import com.cloud.objects.events.Action1;
import com.cloud.objects.events.Func0;
import com.cloud.objects.utils.PixelUtils;

import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/9
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class DigitalKeyBoardAdapter extends BaseAdapter {

    private List<String> datalist = null;
    private Context context = null;
    private OnDigitalKeyBoardListener onDigitalKeyBoardListener = null;
    private int DATA_KEY = 445092617;
    private DialogPlus dialogPlus = null;
    private Action1<InputDigitalKeyType> closeAction = null;
    private Func0<EditText> focusEditFunction = null;
    private DigitalKeyboardUtils keyboardUtils = null;

    public void setFocusEditFunction(Func0<EditText> focusEditFunction) {
        this.focusEditFunction = focusEditFunction;
    }

    public DigitalKeyBoardAdapter(Context context, DigitalKeyboardUtils keyboardUtils, List<String> datalist, DialogPlus dialogPlus, OnDigitalKeyBoardListener listener) {
        this.context = context;
        this.datalist = datalist;
        this.dialogPlus = dialogPlus;
        this.onDigitalKeyBoardListener = listener;
        this.keyboardUtils = keyboardUtils;
    }

    public void setCloseAction(Action1<InputDigitalKeyType> action) {
        this.closeAction = action;
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder holder = null;
        if (convertView == null) {
            holder = new ItemViewHolder();
            convertView = holder.itemView;
            convertView.setTag(holder);
        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }
        String text = datalist.get(position);
        if (TextUtils.equals(text, "close")) {
            holder.digitalKeyTv.setVisibility(View.GONE);
            holder.hiddenKeyIv.setVisibility(View.VISIBLE);
            holder.itemView.setTag(DATA_KEY, "close");
        } else {
            holder.digitalKeyTv.setVisibility(View.VISIBLE);
            holder.hiddenKeyIv.setVisibility(View.GONE);
            holder.digitalKeyTv.setText(text);
            holder.itemView.setTag(DATA_KEY, text);
        }
        return convertView;
    }

    private class ItemViewHolder implements View.OnClickListener {

        public View itemView = null;
        public TextView digitalKeyTv = null;
        public View hiddenKeyIv = null;

        public ItemViewHolder() {
            itemView = View.inflate(context, R.layout.rx_digital_key_board_item_view, null);
            digitalKeyTv = (TextView) itemView.findViewById(R.id.digital_key_tv);
            hiddenKeyIv = itemView.findViewById(R.id.hidden_key_iv);
            itemView.setOnClickListener(this);
            //设置param
            ViewGroup.LayoutParams gparam = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    PixelUtils.dip2px(context, 56)
            );
            itemView.setLayoutParams(gparam);
        }

        @Override
        public void onClick(View v) {
            if (onDigitalKeyBoardListener == null) {
                return;
            }
            String tag = String.valueOf(v.getTag(DATA_KEY));
            if (TextUtils.equals(tag, "close")) {
                //onDigitalKeyBoardListener.onKeyBoardListener(InputDigitalKeyType.cancel, "");
                if (closeAction != null) {
                    closeAction.call(null);
                }
                if (dialogPlus != null) {
                    dialogPlus.dismiss();
                }
            } else {
                EditText editText = null;
                if (focusEditFunction != null) {
                    editText = focusEditFunction.call();
                }
                onDigitalKeyBoardListener.onKeyBoardListener(keyboardUtils, InputDigitalKeyType.digitalSymbol, tag, editText);
                if (editText != null) {
                    editText.setSelection(editText.length());
                }
            }
        }
    }
}
