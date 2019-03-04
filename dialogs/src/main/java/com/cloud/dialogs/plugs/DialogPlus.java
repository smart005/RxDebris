package com.cloud.dialogs.plugs;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.cloud.dialogs.R;
import com.cloud.objects.events.HookEvent;
import com.cloud.objects.logs.Logger;

public class DialogPlus {

    private static final int INVALID = -1;

    /**
     * DialogPlus base layout root view
     */
    private ViewGroup rootView;

    /**
     * DialogPlus content container which is a different layout rather than base layout
     */
    private ViewGroup contentContainer;

    /**
     * Determines whether dialog should be dismissed by back button or touch in the black overlay
     */
    private boolean isCancelable;

    /**
     * Determines whether dialog is showing dismissing animation and avoid to repeat it
     */
    private boolean isDismissing;

    /**
     * Listener for the user to take action by clicking any item
     */
    private OnItemClickListener onItemClickListener;

    /**
     * Listener for the user to take action by clicking views in header or footer
     */
    private OnClickListener onClickListener;

    /**
     * Listener to notify the user that dialog has been dismissed
     */
    private OnDismissListener onDismissListener;

    /**
     * Listener to notify the user that dialog has been canceled
     */
    private OnCancelListener onCancelListener;

    /**
     * Listener to notify back press
     */
    private OnBackPressListener onBackPressListener;

    /**
     * Content
     */
    private Holder holder;

    /**
     * basically activity root view
     */
    private ViewGroup decorView;

    private Animation outAnim;
    private Animation inAnim;

    private String dialogId = "";

    private DialogPlusBuilder builder = null;

    public String getDialogId() {
        return dialogId;
    }

    public DialogPlusBuilder getBuilder() {
        return builder;
    }

    DialogPlus(DialogPlusBuilder builder, String dialogId) {
        this.builder = builder;
        this.dialogId = dialogId;

        if (builder.getContext() instanceof Activity) {
            //如果非Activity则不构建dialog
            LayoutInflater layoutInflater = LayoutInflater.from(builder.getContext());

            Activity activity = (Activity) builder.getContext();

            holder = builder.getHolder();

            onItemClickListener = builder.getOnItemClickListener();
            onClickListener = builder.getOnClickListener();
            onDismissListener = builder.getOnDismissListener();
            onCancelListener = builder.getOnCancelListener();
            onBackPressListener = builder.getOnBackPressListener();
            isCancelable = builder.isCancelable();

            /**
             * Avoid getting directly from the decor view because by doing that we are overlapping the black soft key on
             * nexus device. I think it should be tested on different devices but in my opinion is the way to go.
             * @link http://stackoverflow.com/questions/4486034/get-root-view-from-current-activity
             */
            decorView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
            rootView = (ViewGroup) layoutInflater.inflate(R.layout.base_dialog_plugs_container, decorView, false);
            rootView.setTag(dialogId);
            rootView.setLayoutParams(builder.getOutmostLayoutParams());

            View outmostView = rootView.findViewById(R.id.dialogplus_outmost_container);
            outmostView.setBackgroundResource(builder.getOverlayBackgroundResource());

            contentContainer = (ViewGroup) rootView.findViewById(R.id.dialogplus_content_container);
            contentContainer.setLayoutParams(builder.getContentParams());

            outAnim = builder.getOutAnimation();
            inAnim = builder.getInAnimation();
            initContentView(
                    layoutInflater,
                    builder.getHeaderView(),
                    builder.getFooterView(),
                    builder.getAdapter(),
                    builder.getContentPadding(),
                    builder.getContentMargin()
            );

            initCancelable();
            if (builder.isExpanded()) {
                initExpandAnimator(activity, builder.getDefaultContentHeight(), builder.getContentParams().gravity);
            }
        }
    }

    /**
     * 是否构建成功
     *
     * @return true-dialog构建成功;false-dialog构建失败;
     */
    public boolean isBuildSuccess() {
        View holderView = getHolderView();
        if (holderView == null || decorView == null || rootView == null || contentContainer == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 绑定dialog内容
     */
    public void binder() {
        try {
            if (rootView == null || contentContainer == null || builder == null) {
                return;
            }
            View outmostView = rootView.findViewById(R.id.dialogplus_outmost_container);
            outmostView.setBackgroundResource(builder.getOverlayBackgroundResource());

            contentContainer.setLayoutParams(builder.getContentParams());

            if (builder.isExpanded() && (builder.getContext() instanceof Activity)) {
                Activity activity = (Activity) builder.getContext();
                initExpandAnimator(activity, builder.getDefaultContentHeight(), builder.getContentParams().gravity);
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public static DialogPlusBuilder newDialog(Context context) {
        DialogPlusBuilder builder = new DialogPlusBuilder(context);
        return builder;
    }

    /**
     * It adds the dialog view into rootView which is decorView of activity
     */
    public void show() {
        if (!isShowing()) {
            onAttached(rootView);
        }
    }

    /**
     * It basically check if the rootView contains the dialog plus view.
     *
     * @return true if it contains
     */
    public boolean isShowing() {
        if (decorView == null) {
            return false;
        }
        View view = decorView.findViewWithTag(dialogId);
        if (view == null) {
            return false;
        } else {
            return view.getVisibility() == View.VISIBLE;
        }
    }

    /**
     * It is called when to dismiss the dialog, either by calling dismiss() method or with cancellable
     */
    public void dismiss() {
        try {
            if (decorView == null || contentContainer == null || outAnim == null) {
                return;
            }
            if (isDismissing) {
                View view = decorView.findViewWithTag(dialogId);
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
                if (rootView != null) {
                    decorView.removeView(rootView);
                }
                isDismissing = false;
                if (onDismissListener != null) {
                    onDismissListener.onDismiss(DialogPlus.this);
                }
                return;
            }

            outAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    decorView.post(new Runnable() {
                        @Override
                        public void run() {
                            View view = decorView.findViewWithTag(dialogId);
                            if (view != null) {
                                view.setVisibility(View.GONE);
                            }
                            if (rootView != null) {
                                decorView.removeView(rootView);
                            }
                            isDismissing = false;
                            if (onDismissListener != null) {
                                onDismissListener.onDismiss(DialogPlus.this);
                            }
                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            contentContainer.startAnimation(outAnim);
            isDismissing = true;
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    @SuppressWarnings("unused")
    public View findViewById(int resourceId) {
        if (contentContainer == null) {
            return null;
        }
        return contentContainer.findViewById(resourceId);
    }

    public View getHeaderView() {
        return holder == null ? null : holder.getHeader();
    }

    public View getFooterView() {
        return holder == null ? null : holder.getFooter();
    }

    public View getHolderView() {
        return holder == null ? null : holder.getInflatedView();
    }

    private void initExpandAnimator(Activity activity, int defaultHeight, int gravity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int displayHeight = display.getHeight() - Utils.getStatusBarHeight(activity);
        if (defaultHeight == 0) {
            defaultHeight = (displayHeight * 2) / 5;
        }
        final View view = holder.getInflatedView();
        if (view == null || !(view instanceof AbsListView)) {
            return;
        }
        final AbsListView absListView = (AbsListView) view;
        view.setOnTouchListener(ExpandTouchListener.newListener(
                activity, absListView, contentContainer, gravity, displayHeight, defaultHeight
        ));
    }

    /**
     * It is called in order to create content
     */
    private void initContentView(LayoutInflater inflater, View header, View footer, BaseAdapter adapter,
                                 int[] padding, int[] margin) {
        if (contentContainer == null) {
            return;
        }
        View contentView = createView(inflater, header, footer, adapter);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.setMargins(margin[0], margin[1], margin[2], margin[3]);
        contentView.setLayoutParams(params);
        getHolderView().setPadding(padding[0], padding[1], padding[2], padding[3]);
        contentContainer.addView(contentView);
    }

    /**
     * It is called to set whether the dialog is cancellable by pressing back button or
     * touching the black overlay
     */
    private void initCancelable() {
        if (!isCancelable || rootView == null) {
            return;
        }
        View view = rootView.findViewById(R.id.dialogplus_outmost_container);
        if (view == null) {
            return;
        }
        view.setOnTouchListener(onCancelableTouchListener);
    }

    /**
     * it is called when the content view is created
     *
     * @param inflater used to inflate the content of the dialog
     * @return any view which is passed
     */
    private View createView(LayoutInflater inflater, View headerView, View footerView, BaseAdapter adapter) {
        if (holder == null) {
            return null;
        }
        View view = holder.getView(inflater, rootView);

        if (holder instanceof ViewHolder) {
            assignClickListenerRecursively(view);
        }

        assignClickListenerRecursively(headerView);
        holder.addHeader(headerView);

        assignClickListenerRecursively(footerView);
        holder.addFooter(footerView);

        if (adapter != null && holder instanceof HolderAdapter) {
            HolderAdapter holderAdapter = (HolderAdapter) holder;
            holderAdapter.setAdapter(adapter);
            holderAdapter.setOnItemClickListener(new OnHolderListener() {
                @Override
                public void onItemClick(Object item, View view, int position) {
                    if (onItemClickListener == null) {
                        return;
                    }
                    onItemClickListener.onItemClick(DialogPlus.this, item, view, position);
                }
            });
        }
        return view;
    }

    /**
     * Loop among the views in the hierarchy and assign listener to them
     */
    public void assignClickListenerRecursively(View parent) {
        if (parent == null) {
            return;
        }
        setClickListener(parent);
        if (parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) parent;
            int childCount = viewGroup.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                View child = viewGroup.getChildAt(i);
                setClickListener(child);
                if (child instanceof ViewGroup) {
                    assignClickListenerRecursively(child);
                }
            }
        }
    }

    /**
     * It is used to setListener on view that have a valid id associated
     */
    private void setClickListener(View view) {
        if (view.getId() == INVALID) {
            return;
        }
        //adapterview does not support click listener
        if (view instanceof AdapterView) {
            return;
        }
        if (HookEvent.isRegisterListener(view)) {
            hookEvent.didHook(view);
        } else {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener == null) {
                        return;
                    }
                    onClickListener.onClick(DialogPlus.this, v);
                }
            });
        }
    }

    private HookEvent hookEvent = new HookEvent() {
        @Override
        protected void onAfterClick(View v) {
            if (onClickListener == null) {
                return;
            }
            onClickListener.onClick(DialogPlus.this, v);
        }
    };

    /**
     * It is called when the show() method is called
     *
     * @param view is the dialog plus view
     */
    private void onAttached(View view) {
        if (decorView == null || contentContainer == null || holder == null) {
            return;
        }
        View mv = decorView.findViewWithTag(dialogId);
        if (mv == null) {
            decorView.addView(view);
        } else if (mv.getVisibility() != View.VISIBLE) {
            mv.setVisibility(View.VISIBLE);
        }
        contentContainer.startAnimation(inAnim);

        contentContainer.requestFocus();
        holder.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (onBackPressListener != null) {
                        onBackPressListener.onBackPressed(DialogPlus.this);
                    }
                    if (isCancelable) {
                        onBackPressed(DialogPlus.this);
                    }
                    return true;
                } else {
                    onBackPressed(DialogPlus.this);
                    return false;
                }
            }
        });
    }

    /**
     * Dismiss the dialog when the user press the back button
     *
     * @param dialogPlus is the current dialog
     */
    public void onBackPressed(DialogPlus dialogPlus) {
        if (onCancelListener != null) {
            onCancelListener.onCancel(DialogPlus.this);
        }
    }

    /**
     * Called when the user touch on black overlay in order to dismiss the dialog
     */
    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (onCancelListener != null) {
                    onCancelListener.onCancel(DialogPlus.this);
                }
                dismiss();
                return true;
            }
            return false;
        }
    };
}
