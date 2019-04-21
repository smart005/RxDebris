# 消息通信(一)------HandlerManager(使用)
提交自定义Runnable并携带参数至消息队列,避免并发情况下消息错乱；另外HandlerManager使用ThreadLocal以增加Handler句柄的线程安全；
`ThreadLocal<Handler> handlerThreadLocal = new ThreadLocal<Handler>()`
`handlerThreadLocal.set(mainHandler)`
###### 获取实例对象
```
HandlerManager manager = HandlerManager.getInstance();
```
###### 发送消息
```
/**
     * Causes the Runnable r to be added to the message queue.
     * The runnable will be run on the thread to which this handler is
     * attached.
     *
     * @param runnable call runnable
     * @param params   参数
     */
    public <T> void post(RunnableParamsN<T> runnable,
    T... params)
```
```
/**
     * Causes the Runnable r to be added to the message queue, to be run
     * after the specified amount of time elapses.
     * The runnable will be run on the thread to which this handler
     * is attached.
     *
     * @param runnable    call runnable
     * @param delayMillis 延迟delayMillis毫秒后回调
     * @param params      参数
     */
    public <T> void postDelayed(RunnableParamsN<T> runnable, long delayMillis, 
    T... params)
```