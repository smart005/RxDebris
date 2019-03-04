package com.cloud.objects;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/1/15
 * Description:程序包加载管理
 * Modifier:
 * ModifyContent:
 */
public class LoaderManager {

    /**
     * 加载静态库文件
     *
     * @param libName 静态库名称
     * @return 是否加载成功
     */
    public static boolean loadLibrary(String libName) {
        try {
            System.loadLibrary(libName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
