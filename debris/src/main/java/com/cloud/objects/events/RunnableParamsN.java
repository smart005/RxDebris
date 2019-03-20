package com.cloud.objects.events;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/21
 * @Description:N个参数执行类
 * @Modifier:
 * @ModifyContent:
 */
public abstract class RunnableParamsN<Params> {

    /**
     * 回调
     *
     * @param params 参数
     */
    public abstract void run(Params... params);
}
