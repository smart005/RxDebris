package com.cloud.dialogs.options.events;

import android.content.Context;

import com.cloud.dialogs.options.beans.OptionsItem;

import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/1
 * Description:操作选项监听
 * Modifier:
 * ModifyContent:
 */
public interface OnOptionsListener {
    /**
     * 导入本地数据(即将json数据导入到本地数据库)
     *
     * @param context            上下文
     * @param importCompleteCall 导入完成回调
     */
    public void onImportLocalData(Context context, OnImportCompleteListener importCompleteCall);

    /**
     * 获取选项集合
     *
     * @param targetId 目标id
     * @param parentId 父节点id
     * @return 选项集合
     */
    public List<OptionsItem> getOptionsItems(String targetId, String parentId);
}
