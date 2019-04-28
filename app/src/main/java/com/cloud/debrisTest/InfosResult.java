package com.cloud.debrisTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/16
 * Description:版块信息数据
 * Modifier:
 * ModifyContent:
 */
public class InfosResult {
    /**
     * 版本信息列表
     */
    private List<Info> InfoList;

    public List<Info> getInfoList() {
        if (InfoList == null) {
            return new ArrayList<>();
        }
        return InfoList;
    }

    public void setInfoList(List<Info> infoList) {
        InfoList = infoList;
    }
}
