package com.cloud.mixed.h5;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/11/21
 * @Description:注册脚本接口对象
 * @Modifier:
 * @ModifyContent:
 */
public class JavascriptInterfaceItem {
    /**
     * 注册脚本接口
     */
    private Object javascriptInterface = null;
    /**
     * js接口名
     */
    private String jsInterfaceName = "";

    public JavascriptInterfaceItem(Object javascriptInterface, String jsInterfaceName) {
        this.javascriptInterface = javascriptInterface;
        this.jsInterfaceName = jsInterfaceName;
    }

    public Object getJavascriptInterface() {
        return javascriptInterface;
    }

    public String getJsInterfaceName() {
        return jsInterfaceName;
    }
}
