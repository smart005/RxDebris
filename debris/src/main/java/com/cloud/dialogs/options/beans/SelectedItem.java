package com.cloud.dialogs.options.beans;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/1
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class SelectedItem {

    private OptionsItem optionsItem = null;

    private String targetId = "";

    public OptionsItem getOptionsItem() {
        return optionsItem;
    }

    public void setOptionsItem(OptionsItem optionsItem) {
        this.optionsItem = optionsItem;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
