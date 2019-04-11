package com.cloud.objects;

import android.text.TextUtils;

import com.cloud.objects.beans.TaskEntry;
import com.cloud.objects.events.RunnableParamsN;
import com.cloud.objects.handler.HandlerManager;
import com.cloud.objects.observable.ObservableComponent;
import com.cloud.objects.utils.ConvertUtils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/10
 * Description:任务管理
 * Modifier:
 * ModifyContent:
 */
public class TaskManager {

    private HashMap<String, TaskEntry<? extends Runnable>> map = new HashMap<String, TaskEntry<? extends Runnable>>();
    private SoftReference<HashMap<String, TaskEntry<? extends Runnable>>> reference = new SoftReference<HashMap<String, TaskEntry<? extends Runnable>>>(map);
    private static TaskManager taskManager;

    private TaskManager() {
        //init
    }

    public static TaskManager getInstance() {
        return taskManager == null ? taskManager = new TaskManager() : taskManager;
    }

    private <T extends Runnable> HashMap<String, TaskEntry<? extends Runnable>> getTaskMap() {
        HashMap<String, TaskEntry<? extends Runnable>> entryHashMap = reference.get();
        if (entryHashMap == null) {
            reference = new SoftReference<HashMap<String, TaskEntry<? extends Runnable>>>(map);
            entryHashMap = reference.get();
            //再次检测
            if (map == null) {
                return null;
            }
        }
        return entryHashMap;
    }

    /**
     * 添加任务,不主动执行
     *
     * @param key           任务键
     * @param task          任务执行对象
     * @param performCounts 任务被执行计数
     * @param delay         首次延迟执行毫秒数
     */
    public <T extends Runnable> void addTask(String key, T task, int performCounts, long delay) {
        if (TextUtils.isEmpty(key) || task == null || performCounts <= 0) {
            return;
        }
        HashMap<String, TaskEntry<? extends Runnable>> taskMap = getTaskMap();
        if (taskMap == null) {
            return;
        }
        TaskEntry<T> entry = new TaskEntry<T>();
        entry.setKey(key);
        entry.setTask(task);
        entry.setPerformCounts(performCounts);
        entry.setDelayTime(delay);
        taskMap.put(key, entry);
    }

    /**
     * 添加并执行任务,相同key不能重复添加按第一次为准;
     *
     * @param key           任务键
     * @param task          任务执行对象
     * @param performCounts 任务被执行计数
     * @param delay         首次延迟执行毫秒数
     */
    public <T extends Runnable> void addPerformTask(String key, T task, int performCounts, long delay) {
        if (TextUtils.isEmpty(key) || task == null || performCounts <= 0) {
            return;
        }
        HashMap<String, TaskEntry<? extends Runnable>> taskMap = getTaskMap();
        if (taskMap == null) {
            return;
        }
        //如果已经存在则不添加任务
        if (taskMap.containsKey(key)) {
            return;
        }
        TaskEntry<T> entry = new TaskEntry<T>();
        entry.setKey(key);
        entry.setTask(task);
        entry.setPerformCounts(performCounts);
        entry.setDelayTime(delay);
        taskMap.put(key, entry);
        execute(entry);
    }

    /**
     * 根据key获取执行任务对象
     *
     * @param key 任务key
     */
    public TaskEntry<? extends Runnable> getTask(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        HashMap<String, TaskEntry<? extends Runnable>> entryHashMap = reference.get();
        if (entryHashMap == null || !entryHashMap.containsKey(key)) {
            return null;
        }
        TaskEntry<? extends Runnable> taskEntry = entryHashMap.get(key);
        if (taskEntry == null) {
            return null;
        } else {
            return taskEntry;
        }
    }

    private class TaskRunable<T extends Runnable> extends RunnableParamsN {

        //任务key
        private String key;
        //执行计数
        private int performCounts;
        //延迟执行时间
        private long delayTime;
        //任务对象
        private T task;
        //执行方式
        private String implementWay;
        //任务是否自动执行
        private boolean isAutoNextTask;

        @Override
        public void run(Object... params) {
            if (ObjectJudge.isNullOrEmpty(params)) {
                return;
            }
            if (!(params[3] instanceof Runnable)) {
                return;
            }
            this.key = String.valueOf(params[0]);
            this.performCounts = ConvertUtils.toInt(params[1]);
            this.delayTime = ConvertUtils.toLong(params[2]);
            this.implementWay = String.valueOf(params[4]);
            this.isAutoNextTask = ObjectJudge.isTrue(params[5]);
            HashMap<String, TaskEntry<? extends Runnable>> taskMap = getTaskMap();
            task = (T) params[3];
            task.run();
            if (performCounts > 1) {
                TaskEntry<T> taskEntry = null;
                if (taskMap != null) {
                    if (!taskMap.containsKey(key)) {
                        taskEntry = new TaskEntry<T>();
                        taskEntry.setKey(key);
                        taskEntry.setPerformCounts(performCounts);
                        taskEntry.setTask(task);
                        taskEntry.setCount(1);
                        taskEntry.setImplementWay(implementWay);
                    } else {
                        taskEntry = (TaskEntry<T>) taskMap.get(key);
                        taskEntry.setCount(taskEntry.getCount() + 1);
                    }
                    if (isAutoNextTask) {
                        //若计数+1后仍小于等于总计数时则执行
                        //若非自动执行任务则由手动调用execute完成,一般用于异步任务
                        taskEntry.setDelayTime(delayTime + 5000);
                        taskMap.put(key, taskEntry);
                        //执行任务
                        if (TextUtils.equals(implementWay, "synchronous")) {
                            execute(taskEntry);
                        } else if (TextUtils.equals(implementWay, "asynchronous")) {
                            asyncExecute(taskEntry);
                        }
                        if (performCounts < taskEntry.getCount()) {
                            //若计数+1后大于总计数时则移除任务
                            if (taskMap.containsKey(key)) {
                                taskMap.remove(key);
                            }
                        }
                    }else {
                        taskEntry.setDelayTime(delayTime);
                        taskMap.put(key, taskEntry);
                    }
                }
            } else {
                //当计数为1时执行任务后从任务列表中移除
                if (taskMap != null && taskMap.containsKey(key)) {
                    taskMap.remove(key);
                }
            }
        }
    }

    /**
     * 执行任务
     *
     * @param taskEntry      任务对象
     * @param implementWay   执行方式
     * @param isAutoNextTask 是否自动执行下一个任务
     */
    private void execute(TaskEntry<? extends Runnable> taskEntry, String implementWay, boolean isAutoNextTask) {
        if (taskEntry == null) {
            return;
        }
        Runnable task = taskEntry.getTask();
        TaskRunable taskRunable = new TaskRunable();
        if (taskEntry.getDelayTime() > 0) {
            HandlerManager.getInstance().postDelayed(taskRunable, taskEntry.getDelayTime(), taskEntry.getKey(), taskEntry.getPerformCounts(), taskEntry.getDelayTime(), task, implementWay, isAutoNextTask);
        } else {
            taskRunable.run(taskEntry.getKey(), taskEntry.getPerformCounts(), taskEntry.getDelayTime(), task, implementWay, isAutoNextTask);
        }
    }

    /**
     * 执行任务
     *
     * @param taskEntry      任务对象
     * @param isAutoNextTask 是否自动执行下一个任务
     */
    public void execute(TaskEntry<? extends Runnable> taskEntry, boolean isAutoNextTask) {
        execute(taskEntry, "synchronous", isAutoNextTask);
    }

    /**
     * 执行任务
     * (默认只执行一次任务,如果想自动执行请调用execute(TaskEntry<? extends Runnable> taskEntry, true))
     *
     * @param taskEntry 任务对象
     */
    public void execute(TaskEntry<? extends Runnable> taskEntry) {
        execute(taskEntry, false);
    }

    /**
     * 异步任务执行
     *
     * @param taskEntry      任务对象
     * @param isAutoNextTask 是否自动执行下一个任务
     */
    public void asyncExecute(TaskEntry<? extends Runnable> taskEntry, boolean isAutoNextTask) {
        ObservableComponent<Object, TaskEntry<? extends Runnable>> component = new ObservableComponent<Object, TaskEntry<? extends Runnable>>() {
            @Override
            protected Object subscribeWith(TaskEntry<? extends Runnable>... taskEntries) {
                if (ObjectJudge.isNullOrEmpty(taskEntries)) {
                    return null;
                }
                boolean isAutoNextTask = ObjectJudge.isTrue(getExtras());
                execute(taskEntries[0], "asynchronous", isAutoNextTask);
                return null;
            }
        };
        component.setExtras(isAutoNextTask);
        component.build(taskEntry);
    }

    /**
     * 异步任务执行
     * (默认只执行一次任务,如果想自动执行请调用asyncExecute(TaskEntry<? extends Runnable> taskEntry,true))
     *
     * @param taskEntry 任务对象
     */
    public void asyncExecute(TaskEntry<? extends Runnable> taskEntry) {
        asyncExecute(taskEntry, false);
    }

    /**
     * 移除任务
     *
     * @param key 任务key
     */
    public void removeTask(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        HashMap<String, TaskEntry<? extends Runnable>> entryHashMap = reference.get();
        if (entryHashMap == null || !entryHashMap.containsKey(key)) {
            return;
        }
        entryHashMap.remove(key);
    }
}
