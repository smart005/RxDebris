package com.cloud.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.cloud.debris.R;
import com.cloud.dialogs.beans.BaseDialogRes;
import com.cloud.dialogs.beans.CmdItem;
import com.cloud.dialogs.enums.DialogButtonEnum;
import com.cloud.dialogs.enums.DialogButtonsEnum;
import com.cloud.dialogs.enums.MsgBoxClickButtonEnum;
import com.cloud.objects.beans.MapEntry;
import com.cloud.objects.logs.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2015-6-14 下午9:54:47
 * @Description: MessageBox基类
 * @Modifier:
 * @ModifyContent:
 */
public class BaseMessageBox {

    private String title = "";
    private String content = "";
    private View contentView = null;
    private boolean isShowTitle = false;
    private boolean isShowButtons = true;
    private boolean isShowClose = true;
    private BaseDialogRes mbdres = new BaseDialogRes();
    private String target = "";
    private Object extraData = null;
    private int mcontentgravity = Gravity.CENTER_HORIZONTAL;
    private int mtitlegravity = Gravity.LEFT;
    private CmdItem[] cmds = null;
    private List<MapEntry<String, Object>> datalist = new ArrayList<MapEntry<String, Object>>();
    private boolean isCancelable = true;
    private BaseDialogPlus baseDialogPlus = null;
    private String dialogId = "";
    private boolean isPadding = true;
    private int left = 0;
    private int top = 0;
    private int right = 0;
    private int bottom = 0;

    /**
     * dialog显示类型
     */
    private int dialogShowType = WindowManager.LayoutParams.TYPE_TOAST;

    public void setCancelable(boolean isCancelable) {
        this.isCancelable = isCancelable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public void setShowTitle(boolean isShowTitle) {
        this.isShowTitle = isShowTitle;
    }

    /**
     * 标题对齐
     *
     * @param gravity
     */
    public void setTitleGravity(int gravity) {
        mtitlegravity = gravity;
    }

    /**
     * 设置内容对齐
     *
     * @param gravity 默认Gravity.CENTER_HORIZONTAL
     */
    public void setContentGravity(int gravity) {
        mcontentgravity = gravity;
    }

    /**
     * @param isShowButtons
     */
    public void setShowButtons(boolean isShowButtons) {
        this.isShowButtons = isShowButtons;
    }

    /**
     * @param isShowClose
     */
    public void setShowClose(boolean isShowClose) {
        this.isShowClose = isShowClose;
    }

    /**
     * dialog显示类型
     *
     * @param dialogShowType
     */
    public void setDialogShowType(int dialogShowType) {
        this.dialogShowType = dialogShowType;
    }

    protected void setOnBaseDialogResChanged(BaseDialogRes mbdres) {

    }

    /**
     * 单击按钮事件
     *
     * @param v
     * @param mcbenum
     * @param target
     * @param extraData
     * @return true:处理完成后关闭dialog;false:由外部自行处理;
     */
    public boolean onItemClickListener(View v, MsgBoxClickButtonEnum mcbenum,
                                       String target, Object extraData) {
        return true;
    }

    /**
     * 单击按钮事件
     *
     * @param v
     * @param cmdid
     * @param target
     * @param extraData
     * @return true:处理完成后关闭dialog;false:由外部自行处理;
     */
    public boolean onItemClickListener(View v, String cmdid, String target,
                                       Object extraData) {
        return true;
    }

    public void onCloseListener(String target, Object extraData) {

    }

    public void onFinally() {

    }

    public BaseMessageBox() {
        mbdres.dialogbackground = R.drawable.dialog_background;
        mbdres.buttonbackground = R.drawable.dialog_button_bg;
        mbdres.splitlinebackground = R.color.color_efefef;
        mbdres.closebuttonbackground = R.drawable.dialog_close_bg;
        mbdres.buttonTextColor = Color.WHITE;
        setOnBaseDialogResChanged(mbdres);
    }

    private boolean onConfirmProcess(View v) {
        int index = datalist.size() - 1;
        MapEntry<String, Object> mditem = index >= 0 ? datalist.get(index) : new MapEntry<String, Object>();
        boolean flag = onItemClickListener(v, MsgBoxClickButtonEnum.Confirm,
                index >= 0 ? mditem.getKey() : target,
                index >= 0 ? mditem.getValue() : extraData);
        onFinally();
        if (index >= 0) {
            datalist.remove(index);
        }
        return flag;
    }

    private boolean onCancelProcess(View v) {
        int index = datalist.size() - 1;
        MapEntry<String, Object> mditem = index >= 0 ? datalist.get(index) : new MapEntry<String, Object>();
        boolean flag = onItemClickListener(v, MsgBoxClickButtonEnum.Cancel,
                index >= 0 ? mditem.getKey() : target,
                index >= 0 ? mditem.getValue() : extraData);
        onFinally();
        if (index >= 0) {
            datalist.remove(index);
        }
        return flag;
    }

    private boolean onCustomProcess(View v) {
        if (v.getTag() == null) {
            return true;
        }
        int index = datalist.size() - 1;
        MapEntry<String, Object> mditem = index >= 0 ? datalist.get(index) : new MapEntry<String, Object>();
        boolean flag = onItemClickListener(v, String.valueOf(v.getTag()),
                index >= 0 ? mditem.getKey() : target,
                index >= 0 ? mditem.getValue() : extraData);
        onFinally();
        if (index >= 0) {
            datalist.remove(index);
        }
        return flag;
    }

    private void onCloseProcess(View v) {
        int index = datalist.size() - 1;
        MapEntry<String, Object> mditem = index >= 0 ? datalist.get(index) : new MapEntry<String, Object>();
        onCloseListener(index >= 0 ? mditem.getKey() : target,
                index >= 0 ? mditem.getValue() : extraData);
        onFinally();
        if (index >= 0) {
            datalist.remove(index);
        }
    }

    private boolean onCancelLoginProcess(View v) {
        int index = datalist.size() - 1;
        MapEntry<String, Object> mditem = index >= 0 ? datalist.get(index) : new MapEntry<String, Object>();
        boolean flag = onItemClickListener(v, MsgBoxClickButtonEnum.CancelLogin,
                index >= 0 ? mditem.getKey() : target,
                index >= 0 ? mditem.getValue() : extraData);
        onFinally();
        if (index >= 0) {
            datalist.remove(index);
        }
        return flag;
    }

    private boolean onReloginProcess(View v) {
        int index = datalist.size() - 1;
        MapEntry<String, Object> mditem = index >= 0 ? datalist.get(index) : new MapEntry<String, Object>();
        boolean flag = onItemClickListener(v, MsgBoxClickButtonEnum.ReLogin,
                index >= 0 ? mditem.getKey() : target,
                index >= 0 ? mditem.getValue() : extraData);
        onFinally();
        if (index >= 0) {
            datalist.remove(index);
        }
        return flag;
    }

    private boolean onYessProcess(View v) {
        int index = datalist.size() - 1;
        MapEntry<String, Object> mditem = index >= 0 ? datalist.get(index) : new MapEntry<String, Object>();
        boolean flag = onItemClickListener(v, MsgBoxClickButtonEnum.Yes,
                index >= 0 ? mditem.getKey() : target,
                index >= 0 ? mditem.getValue() : extraData);
        onFinally();
        if (index >= 0) {
            datalist.remove(index);
        }
        return flag;
    }

    private boolean onNoProcess(View v) {
        int index = datalist.size() - 1;
        MapEntry<String, Object> mditem = index >= 0 ? datalist.get(index) : new MapEntry<String, Object>();
        boolean flag = onItemClickListener(v, MsgBoxClickButtonEnum.No,
                index >= 0 ? mditem.getKey() : target,
                index >= 0 ? mditem.getValue() : extraData);
        onFinally();
        if (index >= 0) {
            datalist.remove(index);
        }
        return flag;
    }

    public void setTarget(String target, Object extraData) {
        this.target = target;
        this.extraData = extraData;
    }

    public void setTarget(String target) {
        setTarget(target, null);
    }

    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    public void setPadding(boolean padding) {
        this.isPadding = padding;
    }

    public void show(Context context, DialogButtonsEnum btnsenum) {
        try {
            baseDialogPlus = new BaseDialogPlus() {
                @Override
                protected boolean onDialogClickListener(DialogButtonEnum dialogButtonEnum, View view) {
                    try {
                        if (dialogButtonEnum == DialogButtonEnum.Confirm) {
                            return onConfirmProcess(view);
                        } else if (dialogButtonEnum == DialogButtonEnum.Cancel) {
                            return onCancelProcess(view);
                        } else if (dialogButtonEnum == DialogButtonEnum.CancelLogin) {
                            return onCancelLoginProcess(view);
                        } else if (dialogButtonEnum == DialogButtonEnum.ReLogin) {
                            return onReloginProcess(view);
                        } else if (dialogButtonEnum == DialogButtonEnum.Yes) {
                            return onYessProcess(view);
                        } else if (dialogButtonEnum == DialogButtonEnum.No) {
                            return onNoProcess(view);
                        }
                    } catch (Exception e) {
                        Logger.error(e);
                    }
                    return true;
                }

                @Override
                protected boolean onDialogClickListener(String cmdId, View view) {
                    return onCustomProcess(view);
                }

                @Override
                protected void onDialogCloseListener(View view) {
                    onCloseProcess(view);
                }
            };
            if (!TextUtils.isEmpty(dialogId)) {
                baseDialogPlus.setDialogId(dialogId);
            }
            baseDialogPlus.setBtnsenum(btnsenum);
            baseDialogPlus.setButtons(cmds);
            baseDialogPlus.setContent(content);
            baseDialogPlus.setTitleGravity(mtitlegravity);
            baseDialogPlus.setContentGravity(mcontentgravity);
            baseDialogPlus.setContentView(contentView);
            baseDialogPlus.setIsvisiblebuttons(isShowButtons);
            baseDialogPlus.setShowClose(isShowClose);
            baseDialogPlus.setShowTitle(isShowTitle);
            baseDialogPlus.setTitle(title);
            baseDialogPlus.setCancelable(isCancelable);
            baseDialogPlus.setPadding(isPadding);
            baseDialogPlus.setContentPadding(left, top, right, bottom);
            baseDialogPlus.show(context);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public void setContentPadding(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public boolean isShowing() {
        return baseDialogPlus == null ? false : baseDialogPlus.isShowing();
    }

    public void setButtons(CmdItem[] cmds) {
        this.cmds = cmds;
    }

    public void dismiss() {
        if (baseDialogPlus != null && baseDialogPlus.isShowing()) {
            baseDialogPlus.dismiss();
        }
    }

    public void setEnabled(int position, boolean enabled) {
        if (baseDialogPlus == null) {
            return;
        }
        baseDialogPlus.setEnabled(position, enabled);
    }
}
