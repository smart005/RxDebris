# Handler、AIDL
## 引言
Handler是管理某个线程(也可能是进程)的消息队列，比如让Handler处理主线程的消息队列，这样就可以将一些耗时任务放到其他线程之中，待任务完成之后就往主线程的消息队列中添加一个消息，这样Handler的Callback，即handleMessage就会被调用。但是Handler并不是线程安全的，因此官方文档中建议将Handler作为一个静态内部类；即Handler只是处理任务、线程、进程间的消息通信；
## 主要解决问题及封装目的
1. 线程通信内存泄漏问题；
2. 利用ThreadLocal解决并发导致的通信问题；
3. 利用AIDL+Handler解决服务、模块、进程或线程间有通信；

[HandlerManager](handler-manager.md)
[本地线程安全](local-thread.md)
[进程间通信](process-commu.md)