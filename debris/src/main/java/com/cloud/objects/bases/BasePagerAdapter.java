package com.cloud.objects.bases;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.cloud.objects.ObjectJudge;

import java.util.ArrayList;
import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/15
 * Description:分页适配器(未用系统提供的适配器，避免部分机型在7.0系统版本上快速滑动崩溃问题)
 * Modifier:
 * ModifyContent:
 */
public abstract class BasePagerAdapter<TItem> extends PagerAdapter {

    private List<TItem> items = new ArrayList<TItem>();
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private FragmentManager fragmentManager = null;

    protected BasePagerAdapter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    /**
     * fragment创建监听
     *
     * @param item     tab数据信息
     * @param position 索引
     * @return
     */
    protected abstract Fragment onBuildFragment(TItem item, int position);

    /**
     * 切换至当前fragment监听
     *
     * @param fragment fragment对象
     * @param position 分页索引
     */
    protected void onFragmentChangedListener(Fragment fragment, int position) {
        //current fragment listener
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (fragments.size() > position) {
            Fragment fragment = fragments.get(position);
            container.removeView(fragment.getView());
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = null;
        try {
            if (fragments.size() > position) {
                fragment = fragments.get(position);
            } else {
                TItem item = items.get(position);
                fragment = onBuildFragment(item, position);
                fragments.add(fragment);
            }
            onFragmentChangedListener(fragment, position);
            //如果fragment还没有added
            if (!fragment.isAdded()) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(fragment, String.format("clpager_%s", position));
                ft.commit();
                /**
                 * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
                 * 会在进程的主线程中，用异步的方式来执行。
                 * 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
                 * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
                 */
                fragmentManager.executePendingTransactions();
            }
            if (fragment.getView().getParent() == null) {
                container.addView(fragment.getView()); // 为viewpager增加布局
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragment.getView();
    }

    /**
     * 清空列表
     */
    public void clearItems() {
        items.clear();
    }

    /**
     * 添加数据项
     *
     * @param item 数据项
     */
    public void addItem(TItem item) {
        if (item == null) {
            return;
        }
        items.add(item);
    }

    /**
     * 添加数据项
     *
     * @param items 数据项集
     */
    public void addAllItems(List<TItem> items) {
        if (ObjectJudge.isNullOrEmpty(items)) {
            return;
        }
        this.items.addAll(items);
    }

    /**
     * 设置数据项
     *
     * @param items 数据项
     */
    protected void setItems(List<TItem> items) {
        if (items == null) {
            return;
        }
        this.items = items;
    }

    /**
     * 获取数据计数
     *
     * @return
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * 获取数据项
     *
     * @param position 数据索引
     * @return
     */
    public TItem getItem(int position) {
        return items.get(position);
    }

    /**
     * 获取fragment集合
     *
     * @return
     */
    public List<Fragment> getFragments() {
        return this.fragments;
    }

    /**
     * 获取fragment对象
     *
     * @param position 当前fragment索引
     * @return
     */
    public Fragment getFragment(int position) {
        if (position >= fragments.size() || position < 0) {
            return null;
        }
        Fragment fragment = fragments.get(position);
        return fragment;
    }

    /**
     * 获取tab item集合
     *
     * @return
     */
    public List<TItem> getItems() {
        return this.items;
    }
}
