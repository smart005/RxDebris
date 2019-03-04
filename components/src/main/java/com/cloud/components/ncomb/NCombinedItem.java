package com.cloud.components.ncomb;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/27
 * @Description:每一项数据结构
 * @Modifier:
 * @ModifyContent:
 */
public class NCombinedItem {
    /**
     * 布局方向
     */
    private String orientation = "";
    /**
     * 视图宽度
     */
    private int width = 0;
    /**
     * 视图高度
     */
    private int height = 0;
    /**
     * 视图块属性
     */
    private NCBlockItem block = null;
    /**
     * 数据
     */
    private Object data = null;
    /**
     * 子项
     */
    private List<NCombinedItem> children = null;

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public NCBlockItem getBlock() {
        if (block == null) {
            block = new NCBlockItem();
        }
        return block;
    }

    public void setBlock(NCBlockItem block) {
        this.block = block;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<NCombinedItem> getChildren() {
        if (children == null) {
            children = new ArrayList<NCombinedItem>();
        }
        return children;
    }

    public void setChildren(List<NCombinedItem> children) {
        this.children = children;
    }
}
