package com.cloud.components.ncomb;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/27
 * @Description:api对应的数据项继承该基类即可
 * @Modifier:
 * @ModifyContent:
 */
public class BaseNCombined {
    /**
     * 总宽度
     */
    private int totalWidth = 0;
    /**
     * 总高度
     */
    private int totalHeight = 0;
    /**
     * 数据项列表
     */
    private List<NCombinedItem> config = null;

    public int getTotalWidth() {
        return totalWidth;
    }

    public void setTotalWidth(int totalWidth) {
        this.totalWidth = totalWidth;
    }

    public int getTotalHeight() {
        return totalHeight;
    }

    public void setTotalHeight(int totalHeight) {
        this.totalHeight = totalHeight;
    }

    public List<NCombinedItem> getConfig() {
        if (config == null) {
            config = new ArrayList<NCombinedItem>();
        }
        return config;
    }

    public void setConfig(List<NCombinedItem> config) {
        this.config = config;
    }
}
