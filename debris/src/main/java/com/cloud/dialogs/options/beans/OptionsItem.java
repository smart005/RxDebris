package com.cloud.dialogs.options.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/1
 * Description:选项数据项
 * Modifier:
 * ModifyContent:
 */
@Entity(nameInDb = "cl_tb_options")
public class OptionsItem {
    /**
     * 选项id
     */
    @Id
    @Index
    @Property(nameInDb = "id")
    private String id = "";
    /**
     * 选项名称
     */
    @Property(nameInDb = "name")
    private String name = "";
    /**
     * 父节点id
     */
    @Property(nameInDb = "parentId")
    private String parentId = "";
    /**
     * 是否选中
     */
    @Property(nameInDb = "check")
    private boolean check = false;

    @Generated(hash = 2021819240)
    public OptionsItem(String id, String name, String parentId, boolean check) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.check = check;
    }

    @Generated(hash = 711906581)
    public OptionsItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean getCheck() {
        return this.check;
    }
}
