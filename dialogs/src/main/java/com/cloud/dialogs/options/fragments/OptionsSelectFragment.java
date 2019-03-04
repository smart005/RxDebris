package com.cloud.dialogs.options.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cloud.dialogs.R;
import com.cloud.dialogs.options.beans.OptionsItem;
import com.cloud.dialogs.options.beans.SelectedItem;
import com.cloud.dialogs.options.events.OnFirstOptionsItemListener;
import com.cloud.dialogs.options.events.OnImportCompleteListener;
import com.cloud.dialogs.options.events.OnItemSelectedListener;
import com.cloud.dialogs.options.events.OnOptionsItemSelecteListener;
import com.cloud.dialogs.options.events.OnOptionsListener;
import com.cloud.dialogs.options.events.OnOptionsSelectedNotify;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.paths.PathsDrawable;
import com.cloud.objects.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/4/27
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class OptionsSelectFragment extends Fragment implements OnImportCompleteListener,
        OnOptionsSelectedNotify, OnFirstOptionsItemListener, OnItemSelectedListener {

    //选项标识符
    private String targetId = "";
    private ImageView loadingIv = null;
    private ListView optionsListLv = null;
    private OnOptionsListener onOptionsListener = null;
    //在position==0时getOptionsItems()未获取到数据是否需要调用onImportLocalData()导入本地数据
    private boolean isImportData = false;
    //选项索引
    private int position = 0;
    //数据列表
    private List<OptionsItem> optionsItems = new ArrayList<OptionsItem>();
    //数据列表适配器
    private OptionItemAdapter optionItemAdapter = null;
    //选项选择监听
    private OnOptionsItemSelecteListener onOptionsItemSelecteListener = null;
    private String parentId = "";

    /**
     * 设置选项监听事件
     *
     * @param onOptionsListener 选项监听事件
     */
    public void setOnOptionsListener(OnOptionsListener onOptionsListener) {
        this.onOptionsListener = onOptionsListener;
    }

    /**
     * 在position==0时getOptionsItems()未获取到数据是否需要调用onImportLocalData()导入本地数据
     * (默认为false)
     *
     * @param importData true-需要导入;false-不需要导入;
     */
    public void setImportData(boolean importData) {
        isImportData = importData;
    }

    /**
     * 设置选项选择监听
     *
     * @param listener 选项选择监听
     */
    public void setOnOptionsItemSelecteListener(OnOptionsItemSelecteListener listener) {
        this.onOptionsItemSelecteListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cl_options_select_list_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        init();
    }

    public static OptionsSelectFragment newInstance() {
        return new OptionsSelectFragment();
    }

    private void initView(View view) {
        loadingIv = (ImageView) view.findViewById(R.id.loading_iv);
        optionsListLv = (ListView) view.findViewById(R.id.options_list_lv);
        optionItemAdapter = new OptionItemAdapter();
        optionsListLv.setAdapter(optionItemAdapter);
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("TAB_TARGET_ID")) {
                targetId = bundle.getString("TAB_TARGET_ID");
            }
            if (bundle.containsKey("POSITION")) {
                position = bundle.getInt("POSITION");
            }
            if (bundle.containsKey("PARENT_ID")) {
                parentId = bundle.getString("PARENT_ID");
            }
        }

        if (onOptionsListener != null) {
            loading(true);
            List<OptionsItem> optionsItems = onOptionsListener.getOptionsItems(targetId, parentId);
            if (ObjectJudge.isNullOrEmpty(optionsItems)) {
                if (position == 0) {
                    onOptionsListener.onImportLocalData(getContext(), this);
                    //导入数据后重新获取
                    optionsItems = onOptionsListener.getOptionsItems(targetId, parentId);
                    if (!ObjectJudge.isNullOrEmpty(optionsItems)) {
                        bindDataList(optionsItems);
                        loading(false);
                    }
                }
            } else {
                bindDataList(optionsItems);
                loading(false);
            }
        }
    }

    private void loading(boolean isVisibility) {
        PathsDrawable mArrowDrawable = null;
        if (isVisibility) {
            loadingIv.setVisibility(View.VISIBLE);
            mArrowDrawable = new PathsDrawable();
            mArrowDrawable.parserColors(0xff666666);
            mArrowDrawable.parserPaths("M20,12l-1.41,-1.41L13,16.17V4h-2v12.17l-5.58,-5.59L4,12l8,8 8,-8z");
            loadingIv.setImageDrawable(mArrowDrawable);
        } else {
            mArrowDrawable = null;
            loadingIv.setImageResource(0);
            loadingIv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onImportComplete() {
        if (onOptionsListener == null) {
            return;
        }
        List<OptionsItem> optionsItems = onOptionsListener.getOptionsItems(targetId, parentId);
        if (!ObjectJudge.isNullOrEmpty(optionsItems)) {
            bindDataList(optionsItems);
        }
        loading(false);
    }

    private class ItemViewHolder {
        public View optionsRl = null;
        public TextView optionsTv = null;
        public ImageView optionsIv = null;
    }

    private class OptionItemAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return optionsItems.size();
        }

        @Override
        public Object getItem(int position) {
            return optionsItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemViewHolder holder = null;
            if (convertView == null) {
                holder = new ItemViewHolder();
                convertView = View.inflate(getContext(), R.layout.cl_options_select_list_item_view, null);
                holder.optionsRl = convertView.findViewById(R.id.options_rl);
                holder.optionsRl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkItem(v);
                    }
                });
                holder.optionsTv = (TextView) holder.optionsRl.findViewById(R.id.options_tv);
                holder.optionsTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkItem(v);
                    }
                });
                holder.optionsIv = (ImageView) holder.optionsRl.findViewById(R.id.options_iv);
                convertView.setTag(holder);
            } else {
                holder = (ItemViewHolder) convertView.getTag();
            }
            OptionsItem optionsItem = optionsItems.get(position);
            holder.optionsRl.setTag(position);
            holder.optionsTv.setTag(position);
            holder.optionsTv.setText(optionsItem.getName());
            if (optionsItem.isCheck()) {
                holder.optionsIv.setImageResource(R.drawable.cl_ico_select_pre);
            } else {
                holder.optionsIv.setImageResource(R.drawable.cl_ico_select);
            }
            return convertView;
        }
    }

    private void checkItem(View view) {
        if (onOptionsItemSelecteListener == null) {
            return;
        }
        for (int i = 0; i < optionsItems.size(); i++) {
            OptionsItem optionsItem = optionsItems.get(i);
            optionsItem.setCheck(false);
        }
        int position = ConvertUtils.toInt(view.getTag());
        if (position >= optionsItems.size()) {
            return;
        }
        OptionsItem optionsItem = optionsItems.get(position);
        optionsItem.setCheck(true);
        optionItemAdapter.notifyDataSetChanged();
        onOptionsItemSelecteListener.onOptionsItemSelected(optionsItem, OptionsSelectFragment.this.position);
    }

    private void bindDataList(List<OptionsItem> optionsItems) {
        if (optionItemAdapter == null) {
            return;
        }
        this.optionsItems.clear();
        this.optionsItems.addAll(optionsItems);
        optionItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectedNotify() {
        loading(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("PARENT_ID")) {
                parentId = bundle.getString("PARENT_ID");
            }
        }
        List<OptionsItem> optionsItems = onOptionsListener.getOptionsItems(targetId, parentId);
        if (!ObjectJudge.isNullOrEmpty(optionsItems)) {
            bindDataList(optionsItems);
        }
        loading(false);
    }

    @Override
    public OptionsItem onFirstOptionsItem() {
        if (ObjectJudge.isNullOrEmpty(optionsItems)) {
            return null;
        } else {
            return optionsItems.get(0);
        }
    }

    @Override
    public SelectedItem onItemSelected() {
        SelectedItem selectedItem = new SelectedItem();
        selectedItem.setTargetId(targetId);
        for (OptionsItem optionsItem : optionsItems) {
            if (optionsItem.isCheck()) {
                selectedItem.setOptionsItem(optionsItem);
                break;
            }
        }
        return selectedItem;
    }
}
