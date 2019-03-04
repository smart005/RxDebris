package com.cloud.objects.utils;

import com.cloud.objects.events.Action1;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/4/21
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class ThreadPoolUtils {

    private static ThreadPoolUtils threadPoolUtils = null;

    public static ThreadPoolUtils getInstance() {
        return threadPoolUtils == null ? threadPoolUtils = new ThreadPoolUtils() : threadPoolUtils;
    }

    private ThreadPoolUtils() {
        //不允许外部实例
    }

    /**
     * 清除引用
     */
    public static void clearReference() {
        threadPoolUtils = null;
    }

    /**
     * 初始化线程池
     *
     * @param corePoolSize 核心线程池大小
     * @param maxPoolSize  最大线程池大小
     * @param param        任务执行前参数
     * @param prefAction   执行之前回调
     * @return 线程执行器
     */
    public <Param> ScheduledThreadPoolExecutor fixThreadPool(int corePoolSize, int maxPoolSize, Param param, Action1<Param> prefAction) {
        if (corePoolSize <= 0) {
            corePoolSize = 1;
        }
        if (corePoolSize >= maxPoolSize) {
            maxPoolSize = corePoolSize;
        }
        //初始化线程池
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(corePoolSize, new ScheduledThreadFactory(param, prefAction));
        poolExecutor.setMaximumPoolSize(maxPoolSize);
        //线程活跃度5秒
        poolExecutor.setKeepAliveTime(5, TimeUnit.SECONDS);
        return poolExecutor;
    }

    private class ScheduledThreadFactory<Param> implements ThreadFactory {

        private Action1<Param> prefAction = null;
        private Param param = null;

        public ScheduledThreadFactory(Param param, Action1<Param> prefAction) {
            this.param = param;
            this.prefAction = prefAction;
        }

        @Override
        public Thread newThread(Runnable r) {
            //excutor pref call
            if (prefAction != null) {
                prefAction.call(param);
            }
            return new Thread(r);
        }
    }

    /**
     * 获取最大线程池
     *
     * @return cpu number * 2 + 1 即线程池中最大线程数
     */
    public int getMaxPoolSize() {
        //当前设备有效线程数(cpu数量)
        int processors = Runtime.getRuntime().availableProcessors();
        return processors * 2 + 1;
    }

    /**
     * 执行单任务
     *
     * @param runnable execute task for runable
     * @param delay    the time from now to delay execution
     * @param unit     the time unit of the delay parameter
     */
    public ScheduledFuture<?> singleTaskExecute(Runnable runnable, long delay, TimeUnit unit) {
        ScheduledThreadPoolExecutor poolExecutor = fixThreadPool(1, getMaxPoolSize(), null, null);
        ScheduledFuture<?> future = poolExecutor.schedule(runnable, delay, unit);
        return future;
    }

    /**
     * 执行单任务
     *
     * @param runnable execute task for runable
     */
    public ScheduledFuture<?> singleTaskExecute(Runnable runnable) {
        return singleTaskExecute(runnable, 0, TimeUnit.SECONDS);
    }

    /**
     * 获取多任务调度执行器
     *
     * @param corePoolSize 核心线程数
     * @return ScheduledThreadPoolExecutor
     */
    public ScheduledThreadPoolExecutor getMultiTaskExecutor(int corePoolSize) {
        ScheduledThreadPoolExecutor poolExecutor = fixThreadPool(corePoolSize, getMaxPoolSize(), null, null);
        return poolExecutor;
    }

    /**
     * 获取默认多任务调度执行器(核心线程数=5)
     *
     * @return ScheduledThreadPoolExecutor
     */
    public ScheduledThreadPoolExecutor getMultiTaskExecutor() {
        return getMultiTaskExecutor(5);
    }

    /**
     * 添加任务
     *
     * @param executor 线程池执行器
     * @param runnable execute task for runable
     * @param delay    the time from now to delay execution
     * @param unit     the time unit of the delay parameter
     */
    public ScheduledFuture<?> addTask(ScheduledThreadPoolExecutor executor, Runnable runnable, long delay, TimeUnit unit) {
        ScheduledFuture<?> future = executor.schedule(runnable, delay, unit);
        return future;
    }

    /**
     * 添加任务
     *
     * @param executor 线程池执行器
     * @param runnable execute task for runable
     */
    public ScheduledFuture<?> addTask(ScheduledThreadPoolExecutor executor, Runnable runnable) {
        return addTask(executor, runnable, 0, TimeUnit.SECONDS);
    }

    //队列任务构建器
    public class QueueTasksBuilder {

        private ThreadPoolExecutor executor = null;

        private class LinkedQueuePolicy implements RejectedExecutionHandler {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                if (executor.isShutdown()) {
                    return;
                }
                //与CallerRunsPolicy一致
                r.run();
            }
        }


        private QueueTasksBuilder() {
            executor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedTransferQueue<Runnable>(), new LinkedQueuePolicy());
        }

        /**
         * 添加任务
         *
         * @param task 任务项
         * @return Future
         */
        public Future<?> addTaskAndExecute(Runnable task) {
            Future<?> future = executor.submit(task);
            return future;
        }
    }

    /**
     * 构建队列任务
     *
     * @return QueueTasksBuilder
     */
    public QueueTasksBuilder buildQueueTasks() {
        return new QueueTasksBuilder();
    }
}