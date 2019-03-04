package com.cloud.dialogs.options;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;

import com.cloud.debris.R;
import com.cloud.dialogs.DialogManager;
import com.cloud.dialogs.options.beans.OptionsItem;
import com.cloud.dialogs.options.beans.TabItem;
import com.cloud.dialogs.options.events.OnOptionsListener;
import com.cloud.dialogs.options.fragments.OptionsFragment;
import com.cloud.dialogs.plugs.DialogPlus;
import com.cloud.objects.events.Action3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/4/27
 * @Description:选项选择工具
 * @Modifier:
 * @ModifyContent:
 */
public class OptionsUtils {

    private Context context = null;
    private FragmentManager fragmentManager = null;
    private List<TabItem> tabItems = new ArrayList<TabItem>();
    private OnOptionsListener onOptionsListener = null;
    private boolean isImportData = false;
    //默认父节点id
    private String defParentId = "";

    /**
     * 获取选项对象
     *
     * @return
     */
    public static OptionsUtils getInstance() {
        return new OptionsUtils();
    }

    protected void onCheckedItems(HashMap<String, OptionsItem> checkedItems) {

    }

    /**
     * 添加选项数据
     *
     * @param tabItem 选项
     * @return OptionsUtils
     */
    public OptionsUtils addTabItem(TabItem tabItem) {
        this.tabItems.add(tabItem);
        return this;
    }

    /**
     * 添加选项数据
     *
     * @param id   选项唯一标识
     * @param name 选项名称
     * @param tip  未选择时提示信息
     * @return OptionsUtils
     */
    public OptionsUtils addTabItem(String id, String name, String tip) {
        TabItem tabItem = new TabItem(id, name);
        tabItem.setTip(tip);
        return addTabItem(tabItem);
    }

    /**
     * 设置选项监听事件
     *
     * @param onOptionsListener 选项监听事件
     */
    public void setOnOptionsListener(OnOptionsListener onOptionsListener) {
        this.onOptionsListener = onOptionsListener;
    }

    /**
     * 初始化构建器
     *
     * @param context         上下文
     * @param fragmentManager fragmentManager
     * @return
     */
    public OptionsUtils builder(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        return this;
    }

    /**
     * 在position==0时getOptionsItems()未获取到数据是否需要调用onImportLocalData()导入本地数据
     * (默认为false)
     *
     * @param importData true-需要导入;false-不需要导入;
     */
    public OptionsUtils setImportData(boolean importData) {
        isImportData = importData;
        return this;
    }

    /**
     * 设置默认父节点id
     *
     * @param defParentId 默认父节点id
     */
    public void setDefParentId(String defParentId) {
        this.defParentId = defParentId;
    }

    /**
     * 显示选择项
     *
     * @param extras 扩展数据
     */
    public void show(Object... extras) {
        if (context == null || fragmentManager == null) {
            return;
        }
        DialogManager.DialogManagerBuilder<Object[]> builder = DialogManager.getInstance().builder(context, R.layout.cl_options_panel_view);
        builder.setGravity(Gravity.BOTTOM);
        builder.setCancelable(true);
        builder.show(new Action3<View, DialogPlus, Object[]>() {
            @Override
            public void call(View view, DialogPlus dialogPlus, Object[] extras) {
                optionsFragment.setImportData(isImportData);
                optionsFragment.setDefParentId(defParentId);
                optionsFragment.build(view,
                        OptionsUtils.this.context,
                        extras,
                        dialogPlus,
                        fragmentManager,
                        tabItems,
                        onOptionsListener);
            }
        });
    }

    OptionsFragment optionsFragment = new OptionsFragment() {
        @Override
        protected void onCheckedItems(HashMap<String, OptionsItem> checkedItems) {
            OptionsUtils.this.onCheckedItems(checkedItems);
        }
    };
}
