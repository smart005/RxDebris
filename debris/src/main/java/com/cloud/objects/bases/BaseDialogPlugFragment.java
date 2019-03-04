package com.cloud.objects.bases;

import android.content.Context;
import android.view.View;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/6/30
 * @Description:为dialogPlug弹窗时提供的fragment基类
 * @Modifier:
 * @ModifyContent:
 */
public class BaseDialogPlugFragment<DT, DP> {
    //视图
    private View contentView = null;
    //数据
    private DT data = null;
    //插件
    private DP dialogPlug = null;
    private Context context = null;

    protected View getContentView() {
        return this.contentView;
    }

    public void build(View contentView, Context context, DT dt, DP dp) {
        this.contentView = contentView;
        this.context = context;
        this.data = dt;
        this.dialogPlug = dp;
    }

    public Context getContext() {
        return this.context;
    }

    public DT getData() {
        return this.data;
    }

    public DP getDialogPlug() {
        return this.dialogPlug;
    }
}
