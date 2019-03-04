package com.cloud.objects.events;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/21
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public abstract class Runnable1<T> implements Runnable {

    protected T t;

    public Runnable1(T t) {
        this.t = t;
    }
}
