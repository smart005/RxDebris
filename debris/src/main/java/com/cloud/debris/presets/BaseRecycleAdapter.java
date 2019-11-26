package com.cloud.debris.presets;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cloud.coms.refresh.bases.BaseRecyclerViewAdapter;
import com.cloud.objects.ObjectJudge;

import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/10/10
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class BaseRecycleAdapter<T, BT extends ViewDataBinding> extends BaseRecyclerViewAdapter<BindingViewHolder<BT>> {

    private Context context;
    private List<T> datalist;
    /**
     * 列表项布局id
     */
    private int layoutItemId;
    /**
     * item 对象id
     */
    private int variableId;

    public BaseRecycleAdapter(Context context, List<T> datalist, int layoutItemId, int variableId) {
        this.context = context;
        this.datalist = datalist;
        this.layoutItemId = layoutItemId;
        this.variableId = variableId;
    }

    public BaseRecycleAdapter(Context context, List<T> datalist) {
        this(context, datalist, 0, 0);
    }

    protected BindingViewHolder<BT> buildViewHolder(ViewGroup parent, int viewType, int layoutItemId, int variableId) {
        this.layoutItemId = layoutItemId;
        this.variableId = variableId;
        LayoutInflater inflater = LayoutInflater.from(context);
        BT binding = DataBindingUtil.inflate(inflater, layoutItemId, parent, false);
        BindingViewHolder<BT> viewHolder = new BindingViewHolder<BT>(binding);
        viewHolder.setViewType(viewType);
        return viewHolder;
    }

    protected void bindViewHolder(BT binding, int variableId, int position) {
        binding.setVariable(variableId, datalist.get(position));
    }

    @Override
    public BindingViewHolder<BT> onCreateViewHolder(ViewGroup parent, int viewType) {
        return buildViewHolder(parent, viewType, layoutItemId, variableId);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder<BT> holder, int position) {
        bindViewHolder(holder.getBinding(), variableId, position);
    }

    @Override
    public int getItemCount() {
        return ObjectJudge.isNullOrEmpty(datalist) ? 0 : datalist.size();
    }

    public List<T> getDatalist() {
        return this.datalist;
    }

    public Context getContext() {
        return context;
    }
}
