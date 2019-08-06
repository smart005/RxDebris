package com.cloud.debris.presets;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-08-06
 * Description:view model数据项改变监听;用于特殊视图需要对view进行操作时处理;
 * Modifier:
 * ModifyContent:
 */
public interface OnViewModelPresetsChanged<T> {
    /**
     * 数据模型发生改变时回调
     *
     * @param model model
     */
    public void onModelChanged(T model);
}
