package com.cloud.dialogs.options.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloud.debris.R;
import com.cloud.dialogs.options.beans.OptionsItem;
import com.cloud.dialogs.options.beans.SelectedItem;
import com.cloud.dialogs.options.beans.TabItem;
import com.cloud.dialogs.options.events.OnFirstOptionsItemListener;
import com.cloud.dialogs.options.events.OnItemSelectedListener;
import com.cloud.dialogs.options.events.OnOptionsItemSelecteListener;
import com.cloud.dialogs.options.events.OnOptionsListener;
import com.cloud.dialogs.options.events.OnOptionsSelectedNotify;
import com.cloud.dialogs.plugs.DialogPlus;
import com.cloud.dialogs.toasty.ToastUtils;
import com.cloud.objects.bases.BaseDialogPlugFragment;
import com.cloud.objects.bases.BasePagerAdapter;
import com.cloud.objects.utils.PixelUtils;
import com.cloud.views.magicindicator.MagicIndicator;
import com.cloud.views.magicindicator.ViewPagerHelper;
import com.cloud.views.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.cloud.views.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.cloud.views.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.cloud.views.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.cloud.views.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.cloud.views.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/1
 * Description:选项处理类
 * Modifier:
 * ModifyContent:
 */
public class OptionsFragment extends BaseDialogPlugFragment<Object[], DialogPlus> implements OnOptionsItemSelecteListener {

    private OptionsAdapter optionsAdapter = null;
    private OnOptionsListener onOptionsListener = null;
    private boolean isImportData = false;
    //默认父节点id
    private String defParentId = "";
    private OptionsViewHolder holder = null;

    /**
     * 设置默认父节点id
     *
     * @param defParentId 默认父节点id
     */
    public void setDefParentId(String defParentId) {
        this.defParentId = defParentId;
    }

    /**
     * 选中项回调
     *
     * @param checkedItems 当前选中项
     */
    protected void onCheckedItems(HashMap<String, OptionsItem> checkedItems) {

    }

    public void build(View contentView,
                      Context context,
                      Object[] objects,
                      DialogPlus dialogPlus,
                      FragmentManager fragmentManager,
                      List<TabItem> tabItems,
                      OnOptionsListener onOptionsListener) {
        super.build(contentView, context, objects, dialogPlus);
        this.onOptionsListener = onOptionsListener;
        //初始化数据
        optionsAdapter = new OptionsAdapter(fragmentManager);
        optionsAdapter.addAllItems(tabItems);
        //构建视图
        holder = new OptionsViewHolder();
        holder.bind(contentView);
    }

    private class OptionsViewHolder implements View.OnClickListener {

        private TextView selectedOptionsTv = null;
        private TextView optionsConfirmTv = null;
        private MagicIndicator optionsIndicatorMi = null;
        private ImageView loadingIv = null;
        private ViewPager dataContentVp = null;

        public void bind(View contentView) {
            if (contentView == null) {
                return;
            }
            selectedOptionsTv = (TextView) contentView.findViewById(R.id.selected_options_tv);
            optionsConfirmTv = (TextView) contentView.findViewById(R.id.options_confirm_tv);
            optionsConfirmTv.setOnClickListener(this);
            optionsIndicatorMi = (MagicIndicator) contentView.findViewById(R.id.options_indicator_mi);
            loadingIv = (ImageView) contentView.findViewById(R.id.loading_iv);
            dataContentVp = (ViewPager) contentView.findViewById(R.id.data_content_vp);
            initBar();
        }

        public void setCurrentItem(int position) {
            dataContentVp.setCurrentItem(position);
        }

        public void setSelectedOptionsText(String text) {
            selectedOptionsTv.setText(text);
        }

        private void initBar() {
            dataContentVp.setAdapter(optionsAdapter);
            CommonNavigator commonNavigator = new CommonNavigator(getContext());
            commonNavigator.setIndicatorOnTop(true);
            commonNavigator.setAdjustMode(true);
            commonNavigator.setAdapter(commonNavigatorAdapter);
            optionsIndicatorMi.setNavigator(commonNavigator);
            ViewPagerHelper.bind(optionsIndicatorMi, dataContentVp);
        }

        private CommonNavigatorAdapter commonNavigatorAdapter = new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return optionsAdapter.getItemCount();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#999999"));
                colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#041D29"));

                TabItem tabItem = optionsAdapter.getItem(index);
                colorTransitionPagerTitleView.setText(tabItem.getName());
                colorTransitionPagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dataContentVp.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(Color.parseColor("#2395FF"));
                indicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);
                //如果只有一个选项则不显示optionsIndicatorMi选择器
                if (optionsAdapter.getItemCount() == 1) {
                    indicator.setLineHeight(0);
                } else {
                    indicator.setLineHeight(4);
                }
                indicator.setXOffset(PixelUtils.dip2px(context, 10));
                return indicator;
            }
        };

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.options_confirm_tv) {
                LinkedHashMap<String, OptionsItem> selectedItems = getSelectedItems();
                if (isSelectedValid(selectedItems)) {
                    onCheckedItems(selectedItems);
                }
                DialogPlus dialogPlug = getDialogPlug();
                if (dialogPlug != null) {
                    dialogPlug.dismiss();
                }
            }
        }
    }

    private boolean isSelectedValid(LinkedHashMap<String, OptionsItem> map) {
        List<TabItem> items = optionsAdapter.getItems();
        for (TabItem item : items) {
            if (map.containsKey(item.getId())) {
                OptionsItem optionsItem = map.get(item.getId());
                if (optionsItem == null || TextUtils.isEmpty(optionsItem.getId())) {
                    map.put(item.getId(), new OptionsItem());
                    ToastUtils.showLong(getContext(), item.getTip());
                    return false;
                }
            } else {
                map.put(item.getId(), new OptionsItem());
                ToastUtils.showLong(getContext(), item.getTip());
                return false;
            }
        }
        return true;
    }

    private LinkedHashMap<String, OptionsItem> getSelectedItems() {
        LinkedHashMap<String, OptionsItem> map = new LinkedHashMap<String, OptionsItem>();
        List<Fragment> fragments = optionsAdapter.getFragments();
        for (Fragment fragment : fragments) {
            OnItemSelectedListener selectedListener = (OnItemSelectedListener) fragment;
            SelectedItem selectedItem = selectedListener.onItemSelected();
            map.put(selectedItem.getTargetId(), selectedItem.getOptionsItem());
        }
        return map;
    }

    private class OptionsAdapter extends BasePagerAdapter<TabItem> {
        protected OptionsAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        protected Fragment onBuildFragment(TabItem tabItem, int position) {
            Bundle bundle = new Bundle();
            bundle.putString("TAB_TARGET_ID", tabItem.getId());
            bundle.putInt("POSITION", position);
            bundle.putString("PARENT_ID", getParentId(position));

            OptionsSelectFragment selectFragment = new OptionsSelectFragment();
            selectFragment.setOnOptionsListener(onOptionsListener);
            selectFragment.setImportData(isImportData);
            selectFragment.setOnOptionsItemSelecteListener(OptionsFragment.this);
            selectFragment.setArguments(bundle);
            return selectFragment;
        }
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

    private String getParentId(int currPosition) {
        if (currPosition <= 0) {
            return defParentId;
        }
        Fragment fragment = optionsAdapter.getFragment(currPosition - 1);
        if (fragment == null) {
            return defParentId;
        }
        OnFirstOptionsItemListener firstOptionsItemListener = (OnFirstOptionsItemListener) fragment;
        if (firstOptionsItemListener == null) {
            return defParentId;
        }
        OptionsItem optionsItem = firstOptionsItemListener.onFirstOptionsItem();
        if (optionsItem == null || TextUtils.isEmpty(optionsItem.getId())) {
            return defParentId;
        } else {
            return optionsItem.getId();
        }
    }

    @Override
    public void onOptionsItemSelected(OptionsItem optionsItem, int optionsPosition) {
        //设置参数
        Fragment fragment = optionsAdapter.getFragment(optionsPosition + 1);
        if (fragment != null) {
            Bundle bundle = fragment.getArguments();
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString("PARENT_ID", optionsItem.getId());
            fragment.setArguments(bundle);
            //通知更新数据
            OnOptionsSelectedNotify selectedNotify = (OnOptionsSelectedNotify) fragment;
            selectedNotify.onSelectedNotify();
            holder.setCurrentItem(optionsPosition + 1);
        }
        //显示已选择项
        StringBuilder selectsb = new StringBuilder();
        LinkedHashMap<String, OptionsItem> selectedItems = getSelectedItems();
        for (Map.Entry<String, OptionsItem> entry : selectedItems.entrySet()) {
            OptionsItem value = entry.getValue();
            if (value == null || TextUtils.isEmpty(value.getId())) {
                continue;
            }
            selectsb.append(value.getName() + "/");
        }
        if (selectsb.length() > 0) {
            selectsb.delete(selectsb.length() - 1, selectsb.length());
        }
        holder.setSelectedOptionsText(selectsb.toString());
    }
}
