package com.cloud.components.themes;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloud.components.R;
import com.cloud.objects.events.HookEvent;
import com.cloud.objects.utils.PixelUtils;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/6/11
 * @Description:文本主题
 * @Modifier:
 * @ModifyContent:
 */
public class TextThemeView extends RelativeLayout {

    private int leftView = 0;
    private int rightView = 0;
    private int leftViewId = 780197443;
    private int middleViewId = 302837852;
    private int rightViewId = 448357457;
    private int leftViewWidth = 0;
    private int rightViewWidth = 0;
    private OnThemeViewKeyListener onThemeViewKeyListener = null;

    private CharSequence title = "";
    private float titleSize = 0;
    private int titleColor = 0;
    private int titleGravity = 1;
    private boolean isBlod = false;
    private int titleId = 1827005233;
    //默认按钮文本大小
    private int defButtonTextSize = 0;
    //默认按钮文本颜色
    private int defButtonTextColor = 0;
    //默认按钮文本
    private String defButtonText = "";
    //显示视图ids
    private String visibilityViewIds = "";
    //隐藏视图ids
    private String hiddenViewIds = "";
    //默认按钮id(默认值为btn_tv)
    private String defButtonId = "";
    private int defBtnViewId = 0;

    public TextThemeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

        build();
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TextThemeView);
        leftView = typedArray.getResourceId(R.styleable.TextThemeView_ttv_leftView, 0);
        rightView = typedArray.getResourceId(R.styleable.TextThemeView_ttv_rightView, 0);
        title = typedArray.getText(R.styleable.TextThemeView_ttv_title);
        titleSize = typedArray.getDimension(R.styleable.TextThemeView_ttv_titleSize, PixelUtils.dip2px(getContext(), 12));
        titleColor = typedArray.getColor(R.styleable.TextThemeView_ttv_titleColor, Color.parseColor("#061d28"));
        titleGravity = typedArray.getInt(R.styleable.TextThemeView_ttv_titleGravity, 1);
        isBlod = typedArray.getBoolean(R.styleable.TextThemeView_ttv_isBlod, false);
        defButtonTextSize = (int) typedArray.getDimension(R.styleable.TextThemeView_ttv_defButtonTextSize, 0);
        defButtonTextColor = typedArray.getColor(R.styleable.TextThemeView_ttv_defButtonTextColor, Color.parseColor("#041d29"));
        defButtonText = typedArray.getString(R.styleable.TextThemeView_ttv_defButtonText);
        defButtonId = typedArray.getString(R.styleable.TextThemeView_ttv_defButtonId);
        visibilityViewIds = typedArray.getString(R.styleable.TextThemeView_ttv_visibilityViewIds);
        hiddenViewIds = typedArray.getString(R.styleable.TextThemeView_ttv_hiddenViewIds);
        typedArray.recycle();
    }

    private void build() {
        //添加左边视图
        View lview = buildLeftView();
        if (lview != null) {
            this.addView(lview);
        }

        //添加右边视图
        View rview = buildRightView();
        if (rview != null) {
            this.addView(rview);
        }

        //添加中间视图
        View view = buildMiddleView(lview, rview);
        if (view != null) {
            this.addView(view);
        }

        //已初始化视图根据条件设置隐藏显示
        dispayViews(visibilityViewIds, View.VISIBLE);
        dispayViews(hiddenViewIds, View.GONE);
    }

    public void setOnThemeViewKeyListener(OnThemeViewKeyListener listener) {
        this.onThemeViewKeyListener = listener;
    }

    //构建左边视图
    private View buildLeftView() {
        View lview = null;
        if (leftView != 0) {
            //取布局中指定的视图
            lview = View.inflate(getContext(), leftView, null);
        }
        if (lview == null) {
            return null;
        }
        //若当前视图或容器未设置id则添加rightViewId，已设置则获取；
        if (lview.getId() == View.NO_ID) {
            lview.setId(leftViewId);
        } else {
            leftViewId = lview.getId();
        }
        LayoutParams lvparam = null;
        if (lview.getLayoutParams() == null) {
            //如果布局或视图LayoutParams为空则设置默认值
            lvparam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lvparam.addRule(RelativeLayout.CENTER_VERTICAL);
            lvparam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lview.setLayoutParams(lvparam);
        } else {
            lvparam = (LayoutParams) lview.getLayoutParams();
        }
        //绑定视图控件
        bindEventKey(lview);
        //添加视图到容器中
        lvparam.addRule(RelativeLayout.ALIGN_PARENT_LEFT | RelativeLayout.CENTER_VERTICAL);
        lview.setLayoutParams(lvparam);
        //计算视图宽度
        int width = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        int height = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        lview.measure(width, height);
        leftViewWidth = lview.getMeasuredWidth();
        return lview;
    }

    //构建右边视图
    private View buildRightView() {
        View rview = null;
        if (rightView != 0) {
            //取布局中指定的视图
            rview = View.inflate(getContext(), rightView, null);
        } else {
            LinearLayout container = createLRContainer(false);
            //如果没有设置可显示的视图id，则构建默认视图
            container.addView(buildDefaultBtn());
            rview = container;
        }
        if (rview == null) {
            return null;
        }
        //若当前视图或容器未设置id则添加rightViewId，已设置则获取；
        if (rview.getId() == View.NO_ID) {
            rview.setId(rightViewId);
        } else {
            rightViewId = rview.getId();
        }
        LayoutParams rvparam = null;
        if (rview.getLayoutParams() == null) {
            //如果布局或视图LayoutParams为空则设置默认值
            rvparam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            rvparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rvparam.addRule(RelativeLayout.CENTER_VERTICAL);
            rview.setLayoutParams(rvparam);
        } else {
            rvparam = (LayoutParams) rview.getLayoutParams();
        }
        //绑定视图控件
        bindEventKey(rview);
        //添加视图到容器中
        rvparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT | RelativeLayout.CENTER_VERTICAL);
        rview.setLayoutParams(rvparam);
        //计算视图宽度
        int width = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        int height = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        rview.measure(width, height);
        rightViewWidth = rview.getMeasuredWidth();
        return rview;
    }

    private View buildMiddleView(View lview, View rview) {
        View mview = getMiddleView();
        if (mview.getId() == View.NO_ID) {
            mview.setId(middleViewId);
        } else {
            middleViewId = mview.getId();
        }
        //如果文本居中则设置两边padding
        if (titleGravity == 5) {
            int mpadd = Math.max(leftViewWidth, rightViewWidth);
            int left = mpadd + PixelUtils.dip2px(getContext(), 2);
            int right = mpadd + PixelUtils.dip2px(getContext(), 2);
            mview.setPadding(left, 0, right, 0);
        } else {
            int padd = PixelUtils.dip2px(getContext(), 2);
            mview.setPadding(padd, 0, padd, 0);
        }
        //设置文本为垂直对齐
        LayoutParams mparams = (LayoutParams) mview.getLayoutParams();
        if (mparams == null) {
            LayoutParams mvparam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            mvparam.addRule(RelativeLayout.CENTER_VERTICAL);
            mview.setLayoutParams(mvparam);
        } else {
            mparams.addRule(RelativeLayout.CENTER_VERTICAL);
        }
        return mview;
    }

    private void bindEventKey(View view) {
        if (view == null) {
            return;
        }
        int id = view.getId();
        if (id != View.NO_ID && !HookEvent.isRegisterListener(view)) {
            hookEvent.didHook(view);
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                bindEventKey(group.getChildAt(i));
            }
        }
    }

    private HookEvent hookEvent = new HookEvent() {
        @Override
        protected void onPreClick(View v) {

        }

        @Override
        protected void onAfterClick(View v) {
            if (onThemeViewKeyListener == null) {
                return;
            }
            onThemeViewKeyListener.onKeyListener(v, v.getId());
        }
    };

    /**
     * 创建左右视图容器
     *
     * @param isLeft
     * @return
     */
    private LinearLayout createLRContainer(boolean isLeft) {
        LayoutParams rlparam = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        if (isLeft) {
            rlparam.setMargins(PixelUtils.dip2px(getContext(), 8), 0, 0, 0);
            rlparam.addRule(RelativeLayout.CENTER_VERTICAL);
        } else {
            rlparam.setMargins(0, 0, PixelUtils.dip2px(getContext(), 8), 0);
            rlparam.addRule(RelativeLayout.CENTER_VERTICAL);
            rlparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(rlparam);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        if (isLeft) {
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        } else {
            linearLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        }
        return linearLayout;
    }

    /**
     * 获取视图id
     *
     * @param resName 视图id名称
     * @return
     */
    private int getId(String resName) {
        Resources resources = getContext().getResources();
        if (resources == null) {
            return 0;
        }
        String packageName = getContext().getPackageName();
        if (TextUtils.isEmpty(packageName)) {
            return 0;
        }
        return resources.getIdentifier(resName, "id", packageName);
    }

    /**
     * 获取子视图的params
     *
     * @param lparams 当前子视图的params
     * @return
     */
    private LinearLayout.LayoutParams getChildViewParams(ViewGroup.LayoutParams lparams) {
        LinearLayout.LayoutParams vparams = null;
        if (lparams == null) {
            vparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
        } else {
            vparams = new LinearLayout.LayoutParams(lparams.width, lparams.height);
            if (lparams instanceof LayoutParams) {
                LayoutParams mparams = (LayoutParams) lparams;
                vparams.setMargins(mparams.leftMargin, mparams.topMargin, mparams.rightMargin, mparams.bottomMargin);
            } else if (lparams instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams mparams = (LinearLayout.LayoutParams) lparams;
                vparams.setMargins(mparams.leftMargin, mparams.topMargin, mparams.rightMargin, mparams.bottomMargin);
            }
        }
        vparams.gravity = Gravity.CENTER_VERTICAL;
        return vparams;
    }

    private void dispayViews(String viewIds, int visibility) {
        if (TextUtils.isEmpty(viewIds)) {
            return;
        }
        String[] ids = viewIds.split(",|;\\|");
        for (String idName : ids) {
            if (TextUtils.isEmpty(idName)) {
                continue;
            }
            int id = getId(idName);
            if (id == 0) {
                continue;
            }
            View view = this.findViewById(id);
            if (view == null) {
                continue;
            }
            view.setVisibility(visibility);
        }
    }

    private TextView buildDefaultBtn() {
        int lrpadd = PixelUtils.dip2px(getContext(), 6);
        int tbpadd = PixelUtils.dip2px(getContext(), 8);
        LinearLayout.LayoutParams llparam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        llparam.setMargins(lrpadd, tbpadd, lrpadd, tbpadd);
        llparam.gravity = Gravity.CENTER_VERTICAL;
        TextView textView = (TextView) View.inflate(getContext(), R.layout.cl_cs_right_text_view, null);
        textView.setLayoutParams(llparam);
        if (!TextUtils.isEmpty(defButtonId)) {
            //如果自定义按钮不为空，则此控件的id设置为对应资源的id值
            int defId = getId(defButtonId);
            if (defId != 0) {
                textView.setId(defId);
            }
        }
        defBtnViewId = textView.getId();
        if (defButtonTextSize == 0) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, defButtonTextSize);
        }
        textView.setTextColor(defButtonTextColor);
        textView.setText(defButtonText);
        return textView;
    }

    protected View getMiddleView() {
        LayoutParams tvparam = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );
        tvparam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextView textView = new TextView(getContext());
        textView.setId(titleId);
        textView.setText(title);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextColor(titleColor);
        int left = leftViewWidth > 0 ? leftViewWidth : PixelUtils.dip2px(getContext(), 10);
        int right = rightViewWidth > 0 ? rightViewWidth : PixelUtils.dip2px(getContext(), 10);
        switch (titleGravity) {
            case 1:
                textView.setGravity(Gravity.LEFT);
                tvparam.setMargins(left, 0, right, 0);
                break;
            case 3:
                textView.setGravity(Gravity.RIGHT);
                tvparam.setMargins(left, 0, right, 0);
                break;
            case 5:
                textView.setGravity(Gravity.CENTER);
                textView.setLeft(left);
                textView.setRight(right);
                break;
        }
        textView.setLayoutParams(tvparam);
        if (isBlod) {
            TextPaint paint = textView.getPaint();
            paint.setFakeBoldText(true);
        }
        return textView;
    }

    /**
     * 设置标题文本
     *
     * @param text 标题内容
     */
    public void setTitle(CharSequence text) {
        TextView textView = (TextView) findViewById(titleId);
        if (textView == null || TextUtils.isEmpty(text)) {
            return;
        }
        textView.setText(text);
    }

    /**
     * 设置显示视图
     *
     * @param viewIds 视图ids
     */
    public void setVisibilityViews(int... viewIds) {
        if (viewIds == null || viewIds.length == 0) {
            return;
        }
        for (int viewId : viewIds) {
            if (viewId == 0) {
                continue;
            }
            View view = this.findViewById(viewId);
            if (view == null) {
                continue;
            }
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置隐藏视图
     *
     * @param viewIds 视图ids
     */
    public void setHiddenViews(int... viewIds) {
        if (viewIds == null || viewIds.length == 0) {
            return;
        }
        for (int viewId : viewIds) {
            if (viewId == 0) {
                continue;
            }
            View view = this.findViewById(viewId);
            if (view == null) {
                continue;
            }
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 设置默认按钮文本
     *
     * @param text 默认文本
     */
    public void setDefButtonText(CharSequence text) {
        TextView btnText = (TextView) findViewById(defBtnViewId);
        if (btnText == null) {
            return;
        }
        btnText.setText(text);
    }

    /**
     * 设置默认按钮是否被启用
     *
     * @param enable true-启用;false-禁用;
     */
    public void setDefButtonEnable(boolean enable) {
        TextView btnText = (TextView) findViewById(defBtnViewId);
        if (btnText == null) {
            return;
        }
        btnText.setEnabled(enable);
    }
}
