package com.cloud.dialogs;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cloud.debris.R;
import com.cloud.dialogs.beans.CmdItem;
import com.cloud.dialogs.enums.DialogButtonsEnum;
import com.cloud.dialogs.enums.MsgBoxClickButtonEnum;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/26
 * Description:带输入的消息提示框
 * Modifier:
 * ModifyContent:
 */
public class InputMessageBox {

    public boolean onMessageCall(String text, MsgBoxClickButtonEnum mcbenum, Object extras) {
        return true;
    }

    public boolean onMessageCall(String text, String cmdid, Object extras) {
        return true;
    }

    class InputMessageBoxView {

        public TextView messageTv;
        public EditText messageEt;
        public View contentView;

        public InputMessageBoxView(Context context) {
            contentView = View.inflate(context, R.layout.dialog_message_input_view, null);
            messageTv = (TextView) contentView.findViewById(R.id.cl_message_tv);
            messageEt = (EditText) contentView.findViewById(R.id.cl_message_et);
        }
    }

    private void show(Context context,
                      String title,
                      String content,
                      DialogButtonsEnum btnsenum,
                      CmdItem[] cmds,
                      Object extras) {
        BaseMessageBox messageBox = new BaseMessageBox() {
            @Override
            public boolean onItemClickListener(View v, MsgBoxClickButtonEnum mcbenum, String target, Object extraData) {
                if (TextUtils.equals(target, "cl_input_message_dialog") && (extraData instanceof EditText)) {
                    EditText editText = (EditText) extraData;
                    if (editText == null) {
                        return true;
                    }
                    String text = editText.getText().toString().trim();
                    return onMessageCall(text, mcbenum, editText.getTag());
                }
                return super.onItemClickListener(v, mcbenum, target, extraData);
            }

            @Override
            public boolean onItemClickListener(View v, String cmdid, String target, Object extraData) {
                if (TextUtils.equals(target, "cl_input_message_dialog") && (extraData instanceof EditText)) {
                    EditText editText = (EditText) extraData;
                    if (editText == null) {
                        return true;
                    }
                    String text = editText.getText().toString().trim();
                    return onMessageCall(text, cmdid, editText.getTag());
                }
                return super.onItemClickListener(v, cmdid, target, extraData);
            }
        };
        messageBox.setTitle(title);
        messageBox.setShowTitle(true);
        messageBox.setShowClose(false);
        messageBox.setCancelable(false);
        InputMessageBoxView boxView = new InputMessageBoxView(context);
        boxView.messageTv.setText(content);
        messageBox.setContentView(boxView.contentView);
        messageBox.setButtons(cmds);
        boxView.messageEt.setTag(extras);
        messageBox.setTarget("cl_input_message_dialog", boxView.messageEt);
        messageBox.show(context, btnsenum);
    }

    /**
     * 显示带输入消息提示框
     *
     * @param context
     * @param title    标题
     * @param content  内容
     * @param btnsenum 除DialogButtonsEnum.Custom外
     * @param extras   扩展参数
     */
    public void show(Context context, String title, String content, DialogButtonsEnum btnsenum, Object extras) {
        this.show(context, title, content, btnsenum, null, extras);
    }

    /**
     * 显示带输入消息提示框
     *
     * @param context
     * @param title    标题
     * @param content  内容
     * @param btnsenum 除DialogButtonsEnum.Custom外
     */
    public void show(Context context, String title, String content, DialogButtonsEnum btnsenum) {
        show(context, title, content, btnsenum, null);
    }

    /**
     * 显示带输入消息提示框
     *
     * @param context
     * @param title   标题
     * @param content 内容
     * @param cmds    自定义按钮
     * @param extras  扩展参数
     */
    public void show(Context context, String title, String content, CmdItem[] cmds, Object extras) {
        this.show(context, title, content, DialogButtonsEnum.Custom, cmds, extras);
    }

    /**
     * 显示带输入消息提示框
     *
     * @param context
     * @param title   标题
     * @param content 内容
     * @param cmds    自定义按钮
     */
    public void show(Context context, String title, String content, CmdItem[] cmds) {
        show(context, title, content, cmds, null);
    }
}
