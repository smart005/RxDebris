package com.cloud.dialogs;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.cloud.dialogs.plugs.DialogPlus;
import com.cloud.dialogs.plugs.DialogPlusBuilder;
import com.cloud.dialogs.plugs.Holder;
import com.cloud.dialogs.plugs.OnCancelListener;
import com.cloud.dialogs.plugs.OnDismissListener;
import com.cloud.dialogs.plugs.ViewHolder;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.events.Action3;

import java.util.HashMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/6/29
 * @Description:dialog工具类
 * @Modifier:
 * @ModifyContent:
 */
public class DialogManager {

    private static DialogManager dialogManager = null;
    private HashMap<String, DialogPlus> dialogs = new HashMap<String, DialogPlus>();
    private OnOutsideClickListener onOutsideClickListener = null;

    public static DialogManager getInstance() {
        return dialogManager == null ? dialogManager = new DialogManager() : dialogManager;
    }

    private DialogPlus buildDialog(Context context,
                                   Holder holder,
                                   String dialogId,
                                   boolean isCancelable,
                                   int gravity,
                                   int width,
                                   int height,
                                   int overlayBackgroundResource,
                                   int contentBackgroundResource,
                                   int overlayLeftMargin,
                                   int overlayTopMargin,
                                   int overlayRightMargin,
                                   int overlayBottomMargin) {
        DialogPlusBuilder builder = DialogPlus.newDialog(context)
                //内容布局
                .setContentHolder(holder)
                //点击dialog之外是否消失
                .setCancelable(isCancelable)
                .setGravity(gravity)
                //是否根据内容大小自动展开直到充满全屏
                .setExpanded(false)
                //内容宽度
                .setContentWidth(width)
                //内容高度
                .setContentHeight(height)
                //弹窗区域之外的背景
                .setOverlayBackgroundResource(overlayBackgroundResource)
                //弹窗内容背景
                .setContentBackgroundResource(contentBackgroundResource)
                //overlay背景和屏幕的边距
                .setOutMostMargin(overlayLeftMargin, overlayTopMargin, overlayRightMargin, overlayBottomMargin)
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialogPlus) {
                        destoryDialog(dialogPlus.getDialogId());
                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogPlus dialog) {
                        if (onOutsideClickListener != null) {
                            onOutsideClickListener.onOutsideClick(dialog);
                        }
                        destoryDialog(dialog.getDialogId());
                    }
                });
        return builder.create(dialogId);
    }

    private void destoryDialog(String dialogId) {
        if (ObjectJudge.isNullOrEmpty(dialogs)) {
            return;
        }
        if (!dialogs.containsKey(dialogId)) {
            return;
        }
        DialogPlus dialogPlus = dialogs.get(dialogId);
        if (dialogPlus.isShowing()) {
            dialogPlus.dismiss();
        }
        dialogs.remove(dialogId);
    }

    /**
     * 销毁dialog
     *
     * @param dialogId dialog id(可从DialogPlus中获取)
     */
    public void dismiss(String dialogId) {
        destoryDialog(dialogId);
    }

    public class DialogManagerBuilder<Extra> {
        private String dialogId = "";
        private Context context = null;
        private int layoutId = 0;
        private Extra extra = null;
        private int gravity = Gravity.BOTTOM;
        private boolean isCancelable;
        private int width = ViewGroup.LayoutParams.MATCH_PARENT;
        private int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        private int overlayBackgroundResource = R.color.semi_transparent;
        private int contentBackgroundResource = R.color.transparent;
        private int overlayLeftMargin = 0;
        private int overlayTopMargin = 0;
        private int overlayRightMargin = 0;
        private int overlayBottomMargin = 0;

        private DialogManagerBuilder(Context context, int layoutId) {
            this.context = context;
            this.layoutId = layoutId;
            //根据layoutId生成dialogId
            dialogId = String.format("dialog_%s", layoutId);
        }

        /**
         * 设置背景和屏幕的左边距
         *
         * @param overlayLeftMargin 背景和屏幕的左边距(默认0)
         * @return
         */
        public DialogManagerBuilder setOverlayLeftMargin(int overlayLeftMargin) {
            this.overlayLeftMargin = overlayLeftMargin;
            return this;
        }

        /**
         * 设置背景和屏幕的上边距
         *
         * @param overlayTopMargin 背景和屏幕的上边距(默认0)
         * @return
         */
        public DialogManagerBuilder setOverlayTopMargin(int overlayTopMargin) {
            this.overlayTopMargin = overlayTopMargin;
            return this;
        }

        /**
         * 设置背景和屏幕的右边距
         *
         * @param overlayRightMargin 背景和屏幕的右边距(默认0)
         * @return
         */
        public DialogManagerBuilder setOverlayRightMargin(int overlayRightMargin) {
            this.overlayRightMargin = overlayRightMargin;
            return this;
        }

        /**
         * 设置背景和屏幕的下边距
         *
         * @param overlayBottomMargin 背景和屏幕的下边距(默认0)
         * @return
         */
        public DialogManagerBuilder setOverlayBottomMargin(int overlayBottomMargin) {
            this.overlayBottomMargin = overlayBottomMargin;
            return this;
        }

        /**
         * 设置内容背景资源
         *
         * @param contentBackgroundResource 内容背景(默认R.color.transparent)
         * @return
         */
        public DialogManagerBuilder setContentBackgroundResource(int contentBackgroundResource) {
            this.contentBackgroundResource = contentBackgroundResource;
            return this;
        }

        /**
         * 设置弹窗区域之外的背景
         *
         * @param overlayBackgroundResource 弹窗区域之外的背景(默认R.color.semi_transparent)
         * @return
         */
        public DialogManagerBuilder setOverlayBackgroundResource(int overlayBackgroundResource) {
            this.overlayBackgroundResource = overlayBackgroundResource;
            return this;
        }

        /**
         * 设置dialog内容宽度
         *
         * @param width 内容宽度(默认ViewGroup.LayoutParams.MATCH_PARENT)
         * @return
         */
        public DialogManagerBuilder setContentWidth(int width) {
            this.width = width;
            return this;
        }

        /**
         * 设置dialog内容高度
         *
         * @param height 内容高度(默认ViewGroup.LayoutParams.WRAP_CONTENT)
         * @return
         */
        public DialogManagerBuilder setContentHeight(int height) {
            this.height = height;
            return this;
        }

        /**
         * 获取dialogId
         *
         * @return
         */
        public String getDialogId() {
            return this.dialogId;
        }

        /**
         * 获取布局id
         *
         * @return
         */
        public int getLayoutId() {
            return this.layoutId;
        }

        /**
         * 设置自定义数据(与builder中的extra一致)
         *
         * @param extra 自定义数据
         * @return
         */
        public DialogManagerBuilder setExtra(Extra extra) {
            this.extra = extra;
            return this;
        }

        /**
         * 获取自定义数据
         *
         * @return
         */
        public Extra getExtra() {
            return extra;
        }

        /**
         * 设置dialog对齐方式(left-top-right-bottom)
         *
         * @param gravity 只支持left-top-right-bottom
         * @return
         */
        public DialogManagerBuilder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * 点击dialog之外是否消失
         *
         * @param isCancelable true-关闭dialog;false-不销毁;
         * @return
         */
        public DialogManagerBuilder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        public DialogPlus show(Action3<View, DialogPlus, Extra> buildCompleted) {
            if (context == null || !dialogs.containsKey(dialogId)) {
                return null;
            }
            DialogPlus dialogPlus = dialogs.get(dialogId);
            if (dialogPlus != null && dialogPlus.isShowing()) {
                destoryDialog(dialogId);
                dialogPlus = null;
            }
            if (dialogPlus == null) {
                //视图
                ViewHolder holder = new ViewHolder(getLayoutId());
                dialogPlus = buildDialog(context,
                        holder,
                        dialogId,
                        isCancelable,
                        gravity,
                        width,
                        height,
                        overlayBackgroundResource,
                        contentBackgroundResource,
                        overlayLeftMargin,
                        overlayTopMargin,
                        overlayRightMargin,
                        overlayBottomMargin);
                if (dialogPlus == null || !dialogPlus.isBuildSuccess()) {
                    return null;
                }
                dialogs.put(dialogId, dialogPlus);
            }
            if (buildCompleted != null) {
                View holderView = dialogPlus.getHolderView();
                buildCompleted.call(holderView, dialogPlus, extra);
            }
            if (dialogPlus != null) {
                dialogPlus.show();
            }
            return dialogPlus;
        }
    }

    public <Extra> DialogManagerBuilder<Extra> builder(Context context, int layoutId) {
        DialogManagerBuilder managerBuilder = new DialogManagerBuilder(context, layoutId);
        if (context == null || layoutId == 0) {
            return managerBuilder;
        }
        String dialogId = managerBuilder.getDialogId();
        //如果集合中不存在则重新创建
        if (!dialogs.containsKey(dialogId)) {
            //添加至集合
            dialogs.put(dialogId, null);
        }
        return managerBuilder;
    }

    public interface OnOutsideClickListener {
        /**
         * 在dialog之外点击监听事件
         *
         * @param dialog 当前dialog对象
         */
        public void onOutsideClick(DialogPlus dialog);
    }

    /**
     * 设置dialog之外点击监听
     *
     * @param listener dialog之外点击监听事件
     */
    public void setOnOutsideClickListener(OnOutsideClickListener listener) {
        this.onOutsideClickListener = listener;
    }
}
