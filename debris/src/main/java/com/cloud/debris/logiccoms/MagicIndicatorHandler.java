package com.cloud.debris.logiccoms;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.cloud.coms.magicindicator.MagicIndicator;
import com.cloud.coms.magicindicator.ViewPagerHelper;
import com.cloud.coms.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.cloud.coms.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.cloud.coms.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.cloud.coms.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.cloud.coms.options.beans.TabItem;

import java.util.LinkedList;
import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-07-01
 * Description:MagicIndicator处理类
 * Modifier:
 * ModifyContent:
 */
public abstract class MagicIndicatorHandler {

    private List<TabItem> tabItems = new LinkedList<TabItem>();
    private TargetPagerAdapter curradapter;
    private ViewPager viewPager;

    /**
     * 构建fragment
     *
     * @param tabItem 当前tab item
     */
    protected abstract Fragment onBuildFragment(TabItem tabItem);

    /**
     * 构建选项标题视图
     *
     * @param context context
     * @param index   index
     * @return IPagerTitleView
     */
    protected abstract IPagerTitleView onBuildPagerTitleView(Context context, final int index);

    /**
     * 构建分页选择器
     *
     * @param context context
     * @return IPagerIndicator
     */
    protected abstract IPagerIndicator onBuildPagerIndicator(Context context);

    /**
     * 初始化
     *
     * @param fragmentManager support or child fragment manager
     * @param viewPager       view pager
     * @param magicIndicator  tab indicator
     */
    public void initialize(FragmentManager fragmentManager, ViewPager viewPager, MagicIndicator magicIndicator) {
        if (viewPager == null || fragmentManager == null) {
            return;
        }
        this.viewPager = viewPager;
        curradapter = new TargetPagerAdapter(fragmentManager);
        viewPager.setAdapter(curradapter);
        viewPager.setOffscreenPageLimit(3);
        CommonNavigator commonNavigator = new CommonNavigator(viewPager.getContext());
        commonNavigator.setSkimOver(true);
        commonNavigator.setIndicatorOnTop(true);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(commonNavigatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    /**
     * 添加选项
     *
     * @param tabItem tab item
     */
    public void addTabItem(TabItem tabItem) {
        if (tabItem == null) {
            return;
        }
        tabItems.add(tabItem);
    }

    /**
     * 通知选项及页面渲染
     *
     * @param position 指定要显示的索引
     */
    public void notifyDataSetChanged(int position) {
        if (viewPager == null) {
            return;
        }
        curradapter.notifyDataSetChanged();
        commonNavigatorAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(position);
    }

    private CommonNavigatorAdapter commonNavigatorAdapter = new CommonNavigatorAdapter() {

        @Override
        public int getCount() {
            return tabItems.size();
        }

        @Override
        public IPagerTitleView getTitleView(Context context, final int index) {
            return onBuildPagerTitleView(context, index);
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            return onBuildPagerIndicator(context);
        }
    };

    private class TargetPagerAdapter extends FragmentPagerAdapter {

        public TargetPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return tabItems.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }

        @Override
        public Fragment getItem(int position) {
            TabItem tabItem = tabItems.get(position);
            return onBuildFragment(tabItem);
        }
    }
}
