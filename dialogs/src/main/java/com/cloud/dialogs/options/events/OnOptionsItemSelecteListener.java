package com.cloud.dialogs.options.events;

import com.cloud.dialogs.options.beans.OptionsItem;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/1
 * Description:在操作项选择监听
 * Modifier:
 * ModifyContent:
 */
public interface OnOptionsItemSelecteListener {
    /**
     * 选项选择监听
     *
     * @param optionsItem     当前选项数据
     * @param optionsPosition 页签选项索引
     */
    public void onOptionsItemSelected(OptionsItem optionsItem, int optionsPosition);
}
