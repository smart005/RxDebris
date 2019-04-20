package com.cloud.objects.tasks;

import com.cloud.objects.events.OnChainRunnable;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/18
 * Description:同步链式任务(即task间执行同步,对于task内是否同步不关心)
 * Modifier:
 * ModifyContent:
 */
public class SyncChainTasks {

    private SyncChainTasks() {
        //init
    }

    //获取实例
    public static SyncChainTasks getInstance() {
        return new SyncChainTasks();
    }

    private LinkedList<OnChainRunnable> tasks = new LinkedList<OnChainRunnable>();
    private Object[] extras;

    /**
     * 添加链式
     *
     * @param runnable 可执行对象
     * @param <T>      runnable对象类型
     * @return SyncChainTasks
     */
    public <T extends OnChainRunnable> SyncChainTasks addChain(T runnable) {
        if (runnable == null) {
            return this;
        }
        tasks.add(runnable);
        return this;
    }

    /**
     * 获取自定义扩展参数
     *
     * @return 对象数组
     */
    public Object[] getExtras() {
        return extras;
    }

    /**
     * 构建并执行任务
     *
     * @param param  首次传入参数
     * @param extras 全局扩展数据
     * @param <T>    首次传入参数数据类型
     */
    public <T> void build(T param, Object... extras) {
        this.extras = extras;
        Iterator<OnChainRunnable> iterator = tasks.iterator();
        if (!iterator.hasNext()) {
            return;
        }
        OnChainRunnable first = tasks.getFirst();
        first.start();
        Object mret = first.run(param, extras);
        first.finish();
        iterator.next();
        while (iterator.hasNext()) {
            OnChainRunnable next = iterator.next();
            next.start();
            mret = next.run(mret, extras);
            next.finish();
        }
        tasks.clear();
    }
}
