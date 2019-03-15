package com.cloud.nets.events;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/2
 * Description:bean解析监听
 * Modifier:
 * ModifyContent:
 */
public interface OnBeanParsingJsonListener {

    /**
     * json解析处理
     *
     * @param response             数据
     * @param dataClass            解析数据类型
     * @param isCollectionDataType true-集合;false-对象;
     * @return 返回bean对象
     */
    public Object onBeanParsingJson(String response, Class dataClass, boolean isCollectionDataType);
}
