package com.cloud.debrisTest;


import com.cloud.nets.beans.BaseBean;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/17
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class SquareInfosResponse extends BaseBean {
    /**
     * 广场数据
     */
    private InfosResult Result;

    public InfosResult getResult() {
        if (Result == null) {
            return new InfosResult();
        }
        return Result;
    }

    public void setResult(InfosResult result) {
        Result = result;
    }
}
