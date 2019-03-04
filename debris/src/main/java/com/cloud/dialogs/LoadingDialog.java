package com.cloud.dialogs;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloud.debris.R;
import com.cloud.dialogs.plugs.DialogPlus;
import com.cloud.dialogs.plugs.DialogPlusBuilder;
import com.cloud.dialogs.plugs.OnCancelListener;
import com.cloud.dialogs.plugs.OnClickListener;
import com.cloud.dialogs.plugs.OnDismissListener;
import com.cloud.dialogs.plugs.ViewHolder;
import com.cloud.dialogs.progress.DonutProgress;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.events.Action1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2015-6-24 上午10:31:50
 * @Description: 加载中组件, 弹出后此组件将至于视图的最上层
 * @Modifier:
 * @ModifyContent:
 */
public class LoadingDialog {

    private HashMap<String, DialogPlus> dialogPlusHashMap = new HashMap<String, DialogPlus>();
    private String defDialogId = "249628312";
    private boolean isCancelable = false;

    private List<DialogPlus> getDialogPlus() {
        List<DialogPlus> lst = new ArrayList<DialogPlus>();
        for (Map.Entry<String, DialogPlus> entry : dialogPlusHashMap.entrySet()) {
            lst.add(entry.getValue());
        }
        return lst;
    }

    private void removeDialogPlus(String removeKey) {
        if (dialogPlusHashMap.containsKey(removeKey)) {
            dialogPlusHashMap.remove(removeKey);
        }
    }

    /**
     * 获取默认dialog id
     *
     * @return
     */
    public String getDefDialogId() {
        return defDialogId;
    }

    /**
     * 根据对象销毁dialog
     *
     * @param dialogPlug dialog插件
     * @return
     */
    public boolean dismiss(DialogPlus dialogPlug) {
        if (dialogPlug == null) {
            return false;
        }
        if (dialogPlug.isShowing()) {
            dialogPlug.dismiss();
            if (!TextUtils.isEmpty(dialogPlug.getDialogId())) {
                if (dialogPlusHashMap.containsKey(dialogPlug.getDialogId())) {
                    dialogPlusHashMap.remove(dialogPlug.getDialogId());
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 根据id销毁dialog
     *
     * @param dialogId dialog插件唯一标识
     * @return
     */
    public boolean dismiss(String dialogId) {
        if (TextUtils.isEmpty(dialogId)) {
            return false;
        }
        if (dialogPlusHashMap.containsKey(dialogId)) {
            DialogPlus dialogPlus = dialogPlusHashMap.get(dialogId);
            if (dialogPlus.isShowing()) {
                dialogPlus.dismiss();
            }
            dialogPlusHashMap.remove(dialogId);
        }
        return false;
    }

    /**
     * 销毁默认的dialog
     *
     * @return
     */
    public boolean dismiss() {
        DialogPlus dialogPlug = getDialogPlug(defDialogId);
        return dismiss(dialogPlug);
    }

    /**
     * 销毁所有dialog
     */
    public void dismissAllDialog() {
        for (Map.Entry<String, DialogPlus> entry : dialogPlusHashMap.entrySet()) {
            entry.getValue().dismiss();
        }
        dialogPlusHashMap.clear();
    }

    /**
     * 根据id获取dialog插件
     *
     * @param dialogId dialog唯一标识
     * @return
     */
    private DialogPlus getDialogPlug(String dialogId) {
        if (TextUtils.isEmpty(dialogId)) {
            dialogId = defDialogId;
        }
        DialogPlus dialogPlus = null;
        if (!ObjectJudge.isNullOrEmpty(dialogPlusHashMap) && dialogPlusHashMap.containsKey(dialogId)) {
            dialogPlus = dialogPlusHashMap.get(dialogId);
        }
        return dialogPlus;
    }

    /**
     * 构建dialog插件
     *
     * @param context
     * @param dialogId            用于区分DialogPlus的唯一标识
     * @param holder              DialogPlus的视图对象
     * @param dialogBuildListener
     * @param dismissAction       关闭时回调
     * @return
     */
    private DialogPlus buildDialog(Context context,
                                   String dialogId,
                                   ViewHolder holder,
                                   final OnDialogBuildListener dialogBuildListener,
                                   final Action1<DialogPlus> dismissAction) {
        if (TextUtils.isEmpty(dialogId)) {
            dialogId = defDialogId;
        }
        if (dialogPlusHashMap == null) {
            dialogPlusHashMap = new HashMap<String, DialogPlus>();
        }
        DialogPlus dialogPlus = null;
        if (dialogPlusHashMap.containsKey(dialogId)) {
            dialogPlus = dialogPlusHashMap.get(dialogId);
        }
        if (dialogPlus == null) {
            DialogPlusBuilder builder = DialogPlus.newDialog(context)
                    //内容布局
                    .setContentHolder(holder)
                    //点击dialog之外是否消失
                    .setCancelable(isCancelable)
                    .setGravity(Gravity.CENTER)
                    //是否根据内容大小自动展开直到充满全屏
                    .setExpanded(false)
                    //内容宽度
                    .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                    //内容高度
                    .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                    //弹窗区域之外的背景
                    .setOverlayBackgroundResource(R.color.semi_transparent)
                    //弹窗内容背景
                    .setContentBackgroundResource(R.color.transparent)
                    //overlay背景和屏幕的边距
                    .setOutMostMargin(0, 0, 0, 0)
                    .setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogPlus dialogPlus) {
                            if (dismissAction != null) {
                                dismissAction.call(dialogPlus);
                            }
                        }
                    })
                    .setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel(DialogPlus dialog) {
                            if (dialog == null) {
                                return;
                            }
                            dialog.dismiss();
                        }
                    });
            if (dialogBuildListener != null) {
                OnClickListener clickListener = new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        dialogBuildListener.onClickListener(dialog, view);
                    }
                };
                builder.setOnClickListener(clickListener);
            }
            dialogPlus = builder.create(dialogId);
            if (dialogPlus == null || !dialogPlus.isBuildSuccess()) {
                return null;
            }
            dialogPlusHashMap.put(dialogId, dialogPlus);
        }
        return dialogPlus;
    }

    public void setCancelable(boolean isCancelable) {
        this.isCancelable = isCancelable;
    }

    private DialogPlus showDialog(Context context, String dialogId, String message, final Action1<DialogPlus> dismissAction, boolean isShow) {
        ViewHolder holder = new ViewHolder(R.layout.dialog_loading_view);
        DialogPlus dialogPlus = buildDialog(context, dialogId, holder, null, dismissAction);
        if (dialogPlus == null) {
            return null;
        }
        View rootview = dialogPlus.getHolderView().findViewById(R.id.rl_root);
        ImageView loadIconIv = (ImageView) rootview.findViewById(R.id.loading_icon_iv);
        loadIconIv.setImageResource(RxDialog.getInstance().getBuilder().getLoadingIcon());
        View rlProgressLogo = rootview.findViewById(R.id.rl_progress_logo);
        RotateAnimation ra = new RotateAnimation(0.0F, 360.0F, 1, 0.5F, 1, 0.5F);
        ra.setRepeatMode(1);
        ra.setRepeatCount(-1);
        ra.setDuration(2000L);
        ra.setInterpolator(new LinearInterpolator());
        rlProgressLogo.setAnimation(ra);
        TextView tvContent = (TextView) rootview.findViewById(R.id.tv_content);
        tvContent.setText(TextUtils.isEmpty(message) ? "" : message);
        if (isShow && dialogPlus != null) {
            dialogPlus.show();
        }
        return dialogPlus;
    }

    /**
     * 构建dialog
     * （一般用于loading提示 ）
     *
     * @param context
     * @param dialogId      dialog唯一标识
     * @param message       提示内容
     * @param dismissAction 销毁回调
     * @return DialogPlus
     */
    public DialogPlus buildDialog(Context context, String dialogId, String message, final Action1<DialogPlus> dismissAction) {
        DialogPlus dialogPlus = showDialog(context, dialogId, message, dismissAction, false);
        return dialogPlus;
    }

    /**
     * 构建dialog
     * （一般用于loading提示 ）
     *
     * @param context
     * @param dialogId dialog唯一标识
     * @param message  提示内容
     * @return DialogPlus
     */
    public DialogPlus buildDialog(Context context, String dialogId, String message) {
        return buildDialog(context, dialogId, message, null);
    }

    /**
     * 构建dialog
     * （一般用于loading提示 ）
     *
     * @param context
     * @param dialogId      dialog唯一标识
     * @param message       提示内容
     * @param dismissAction 销毁回调
     */
    public void showDialog(Context context, String dialogId, String message, final Action1<DialogPlus> dismissAction) {
        DialogPlus dialogPlus = showDialog(context, dialogId, message, dismissAction, true);
        if (dialogPlus == null) {
            return;
        }
        dialogPlus.show();
    }

    public void showDialog(Context context,
                           String dialogId,
                           View contentView,
                           OnDialogBuildListener dialogBuildListener,
                           Action1<DialogPlus> dismissAction) {
        ViewHolder holder = new ViewHolder(contentView);
        DialogPlus dialogPlus = buildDialog(context, dialogId, holder, dialogBuildListener, dismissAction);
        if (dialogPlus == null || dialogBuildListener == null) {
            return;
        }
        View holderView = dialogPlus.getHolderView();
        dialogBuildListener.onBuilded(holderView);
        if (dialogPlus != null) {
            dialogPlus.show();
        }
    }

    public void showDialog(Context context,
                           String dialogId,
                           int layoutId,
                           OnDialogBuildListener dialogBuildListener,
                           Action1<DialogPlus> dismissAction) {
        View view = View.inflate(context, layoutId, null);
        showDialog(context, dialogId, view, dialogBuildListener, dismissAction);
    }

    /**
     * 构建dialog
     * （一般用于loading提示 ）
     *
     * @param context
     * @param message       提示内容
     * @param dismissAction 销毁回调
     */
    public void showDialog(Context context, String message, final Action1<DialogPlus> dismissAction) {
        showDialog(context, defDialogId, message, dismissAction);
    }

    /**
     * 构建dialog
     * （一般用于loading提示 ）
     *
     * @param context
     * @param resId         提示消息资源id
     * @param dismissAction 销毁回调
     */
    public void showDialog(Context context, int resId, final Action1<DialogPlus> dismissAction) {
        String message = context.getString(resId);
        showDialog(context, message, dismissAction);
    }

    /**
     * 总进度条
     *
     * @param maxProgress
     * @return
     */
    public void setMaxProgress(DialogPlus dialogPlug, int maxProgress) {
        if (dialogPlug == null || dialogPlug.getHolderView() == null) {
            return;
        }
        DonutProgress dpProgress = (DonutProgress) dialogPlug.getHolderView().findViewById(R.id.dp_progress);
        if (dpProgress == null) {
            return;
        }
        dpProgress.setProgress(0);
        dpProgress.setMax(maxProgress);
    }

    /**
     * 当前进度
     *
     * @param currentProgress
     * @return
     */
    public void setCurrentProgress(DialogPlus dialogPlug, int currentProgress) {
        if (dialogPlug == null || dialogPlug.getHolderView() == null) {
            return;
        }
        DonutProgress dpProgress = (DonutProgress) dialogPlug.getHolderView().findViewById(R.id.dp_progress);
        if (dpProgress == null) {
            return;
        }
        dpProgress.setProgress(currentProgress);
    }

    /**
     * 是否旋转
     *
     * @param isRotate
     * @return
     */
    public void setRotate(DialogPlus dialogPlug, boolean isRotate) {
        if (dialogPlug == null || dialogPlug.getHolderView() == null) {
            return;
        }
        RelativeLayout rlProgressLogo = (RelativeLayout) dialogPlug.getHolderView().findViewById(R.id.rl_progress_logo);
        if (rlProgressLogo == null) {
            return;
        }
        if (isRotate) {
            RotateAnimation ra = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            ra.setRepeatMode(Animation.RESTART);
            ra.setRepeatCount(Animation.INFINITE);
            ra.setDuration(2000);
            ra.setInterpolator(new LinearInterpolator());
            rlProgressLogo.setAnimation(ra);
        } else {
            rlProgressLogo.clearAnimation();
        }
    }

    /**
     * 显示文本
     *
     * @param content
     * @return
     */
    public void setContent(DialogPlus dialogPlug, String content) {
        if (dialogPlug == null || dialogPlug.getHolderView() == null || TextUtils.isEmpty(content)) {
            return;
        }
        TextView tvContent = (TextView) dialogPlug.getHolderView().findViewById(R.id.tv_content);
        if (tvContent == null) {
            return;
        }
        tvContent.setText(content);
    }
}
