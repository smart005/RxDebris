package com.cloud.ebus;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.cloud.objects.ObjectJudge;
import com.cloud.objects.events.Action3;
import com.cloud.objects.events.RunnableParamsN;
import com.cloud.objects.handler.HandlerManager;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.observable.ObservableComponent;
import com.cloud.objects.utils.ConvertUtils;
import com.cloud.objects.utils.JsonUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/22
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class EBus {

    private static EBus eBus = null;
    //className-values(key-EBusItem)
    private HashMap<String, HashMap<String, EBusItem>> subMethods = new HashMap<String, HashMap<String, EBusItem>>();

    //process processName-binder
    private HashMap<String, IEBusAidl> binders = new HashMap<String, IEBusAidl>();

    //concurrent send
    private ConcurrentHashMap<String, ConnInfo> conmap = new ConcurrentHashMap<String, ConnInfo>();

    public static EBus getInstance() {
        return eBus == null ? eBus = new EBus() : eBus;
    }

    /**
     * 注册EBus订阅
     *
     * @param subscriber 适用于类、Activity、Fragment等
     */
    public void registered(Object subscriber) {
        if (subscriber == null) {
            return;
        }
        Class<?> sub = subscriber.getClass();
        RegisterRunable runable = new RegisterRunable(sub, subscriber);
        runable.run();
    }

    private class RegisterRunable implements Runnable {

        private Class<?> cls = null;
        private Object subscriber = null;

        public RegisterRunable(Class<?> cls, Object subscriber) {
            this.cls = cls;
            this.subscriber = subscriber;
        }

        @Override
        public void run() {
            try {
                List<SubscribeEBus> subscribeEBuses = new ArrayList<SubscribeEBus>();
                getRegisteredAnnons(cls, subscriber, subscribeEBuses);
                //
            } catch (Exception e) {
                Logger.error(e);
            }
        }
    }

    /**
     * 反注册EBus订阅
     * (一般在onDestory中调用)
     *
     * @param subscriber 适用于类、Activity、Fragment等
     */
    public void unregister(Object subscriber) {
        if (subscriber == null) {
            return;
        }
        Class<?> sub = subscriber.getClass();
        UnRegisterRunable unRegisterRunable = new UnRegisterRunable(sub);
        unRegisterRunable.run();
    }

    private class UnRegisterRunable implements Runnable {

        private Class<?> cls = null;

        public UnRegisterRunable(Class<?> cls) {
            this.cls = cls;
        }

        @Override
        public void run() {
            try {
                //移除集合中数据
                String className = cls.getName();
                if (subMethods.containsKey(className)) {
                    subMethods.remove(className);
                }
                //移除缓存中数据
                //removeCacheDatas(className);
            } catch (Exception e) {
                Logger.error(e);
            }
        }
    }

    private void getRegisteredAnnons(Class<?> cls, Object subscriber, List<SubscribeEBus> subscribeEBuses) {
        Method[] methods = cls.getMethods();
        if (ObjectJudge.isNullOrEmpty(methods)) {
            return;
        }
        HashMap<String, EBusItem> map = null;
        String clsName = cls.getName();
        if (subMethods.containsKey(clsName)) {
            map = subMethods.get(clsName);
        }
        if (map == null) {
            map = new HashMap<String, EBusItem>();
        }
        for (Method method : methods) {
            if (!method.isAnnotationPresent(SubscribeEBus.class)) {
                continue;
            }
            SubscribeEBus annotation = method.getAnnotation(SubscribeEBus.class);
            subscribeEBuses.add(annotation);
            //添加key-method
            if (map.containsKey(annotation.receiveKey())) {
                continue;
            }
            EBusItem eBusItem = new EBusItem();
            eBusItem.setThreadMode(annotation.threadMode());
            eBusItem.setMethod(method);
            eBusItem.setSubscriber(subscriber);
            map.put(annotation.receiveKey(), eBusItem);
        }
        subMethods.put(clsName, map);
    }

    private void sendPosts(String receiveKey, Object[] args) {
        for (Map.Entry<String, HashMap<String, EBusItem>> entry : subMethods.entrySet()) {
            HashMap<String, EBusItem> value = entry.getValue();
            if (value == null || !value.containsKey(receiveKey)) {
                continue;
            }
            EBusItem busItem = value.get(receiveKey);
            if (busItem == null) {
                continue;
            }
            Method method = busItem.getMethod();
            if (method == null || busItem.getSubscriber() == null) {
                continue;
            }
            try {
                if (busItem.getThreadMode() == null || busItem.getThreadMode() == ThreadModeEBus.MAIN) {
                    busItem.setArgs(args);
                    HandlerManager.getInstance().post(new HandlerRunnable(), busItem);
                } else {
                    method.invoke(busItem.getSubscriber(), args);
                }
            } catch (Exception e) {
                Logger.error(e);
            }
        }
    }

    private class SendPostRunable implements Runnable {

        private Context context = null;
        private String processName = "";
        private String receiveKey = "";
        private Object[] event;
        private boolean isProcessTask = false;
        private int total = 0;
        private int postCount = 0;
        private Action3<Integer, Integer, Context> sendCompletedAction = null;

        public SendPostRunable(Context context, String processName, String receiveKey, Object[] event, boolean isProcessTask, int total, int postCount, Action3<Integer, Integer, Context> sendCompletedAction) {
            this.context = context;
            this.processName = processName;
            this.receiveKey = receiveKey;
            this.event = event;
            this.isProcessTask = isProcessTask;
            this.total = total;
            this.postCount = postCount;
            this.sendCompletedAction = sendCompletedAction;
        }

        @Override
        public void run() {
            try {
                if (isProcessTask) {
                    //with binnder send message to remote service and
                    //notication method.
                    if (TextUtils.isEmpty(processName)) {
                        List<IEBusAidl> busAidls = new ArrayList<IEBusAidl>(binders.values());
                        int index = 0;
                        subscriSend(index, busAidls);
                    } else {
                        if (binders.containsKey(processName)) {
                            IEBusAidl busAidl = binders.get(processName);
                            if (busAidl != null) {
                                busAidl.receiveEBusData(receiveKey, JsonUtils.toJson(event));
                            }
                        }
                    }
                    //remote call complete
                    if (sendCompletedAction != null) {
                        sendCompletedAction.call(postCount, total, context);
                    }
                } else {
                    sendPosts(receiveKey, event);
                }
            } catch (Exception e) {
                Logger.error(e);
            }
        }

        private void subscriSend(int index, List<IEBusAidl> busAidls) throws RemoteException {
            if (index < busAidls.size()) {
                IEBusAidl aidl = busAidls.get(index);
                aidl.receiveEBusData(receiveKey, JsonUtils.toJson(event));
                index++;
                subscriSend(index, busAidls);
            }
        }
    }

    /**
     * Posts the given event to the event bus
     *
     * @param context             绑定服务上下文
     * @param processName         进程名称
     * @param receiveKey          接收key
     * @param isProcessTask       是否通知进程任务
     * @param total               发送消息总记录数
     * @param postCount           发送消息数
     * @param sendCompletedAction 发送完成回调
     * @param event               事件参数(将作为接收方法传入)
     */
    private void post(final Context context, final String processName, final String receiveKey, final boolean isProcessTask, final int total, final int postCount, final Action3<Integer, Integer, Context> sendCompletedAction, final Object... event) {
        if (TextUtils.isEmpty(receiveKey)) {
            return;
        }
        ObservableComponent component = new ObservableComponent() {
            @Override
            protected Object subscribeWith(Object[] params) {
                SendPostRunable postRunable = new SendPostRunable((Context) params[0],
                        String.valueOf(params[1]),
                        String.valueOf(params[2]),
                        (Object[]) params[3],
                        ObjectJudge.isTrue(params[4]),
                        ConvertUtils.toInt(params[5]),
                        ConvertUtils.toInt(params[6]),
                        sendCompletedAction);
                postRunable.run();
                return null;
            }
        };
        component.build(context, processName, receiveKey, event, isProcessTask, total, postCount, sendCompletedAction);
    }

    /**
     * Posts the given event to the event bus
     *
     * @param receiveKey 接收key
     * @param event      事件参数(将作为接收方法传入)
     */
    public void post(String receiveKey, Object... event) {
        post(null, "", receiveKey, false, 0, 0, null, event);
    }

    /**
     * Posts the given event to the event bus for current process or other process
     *
     * @param postProcessContext post process context
     * @param receiveKey         接收key
     * @param event              事件参数(将作为接收方法传入)
     */
    public void postMultiProcess(Context postProcessContext, String receiveKey, Object... event) {
        if (postProcessContext == null) {
            return;
        }
        //send current process message
        post(receiveKey, event);
        //send other process message
        Intent intent = new Intent("com.cloud.ebus.ebusProcessDataService");
        //5.0以上安卓设备，service intent必须为显式指出
        PackageManager pm = postProcessContext.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentServices(intent, 0);
        if (resolveInfos == null || resolveInfos.size() == 0) {
            return;
        }
        //如果注册服务的进程数与已绑定的进程数不相等，则更新绑定
        if (resolveInfos.size() != conmap.size()) {
            for (ResolveInfo resolveInfo : resolveInfos) {
                // Get component info and create ComponentName
                ServiceInfo serviceInfo = resolveInfo.serviceInfo;
                //如果进程名对应的服务已存在，直接发送信息
                if (conmap.containsKey(serviceInfo.processName)) {
                    ConnInfo connInfo = conmap.get(serviceInfo.processName);
                    if (connInfo.isConnected()) {
                        post(null, serviceInfo.processName, receiveKey, true, 0, 0, null, event);
                    } else {
                        connInfo.getMessageQueues().put(receiveKey, event);
                    }
                    continue;
                }
                String packageName = serviceInfo.packageName;
                String className = serviceInfo.name;
                ComponentName component = new ComponentName(packageName, className);
                // Create a new intent. Use the old one for extras and such reuse
                Intent explicitIntent = new Intent(intent);
                // Set the component to be explicit
                explicitIntent.setComponent(component);
                if (explicitIntent == null) {
                    continue;
                }
                //添加绑定信息
                ConnInfo connInfo = new ConnInfo();
                connInfo.getMessageQueues().put(receiveKey, event);
                conmap.put(serviceInfo.processName, connInfo);
                //绑定服务
                MultiProcessServiceConnection connection = new MultiProcessServiceConnection(postProcessContext, serviceInfo.processName);
                postProcessContext.bindService(explicitIntent, connection, Context.BIND_AUTO_CREATE);
            }
        } else {
            post(null, "", receiveKey, true, 0, 0, null, event);
        }
    }

    private class MultiProcessServiceConnection implements ServiceConnection {

        private Context context = null;
        private String processName = "";

        public MultiProcessServiceConnection(Context context, String processName) {
            this.context = context;
            this.processName = processName;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service == null) {
                return;
            }
            IEBusAidl aidl = IEBusAidl.Stub.asInterface(service);
            binders.put(processName, aidl);
            if (conmap.containsKey(processName)) {
                ConnInfo connInfo = conmap.get(processName);
                connInfo.setConnected(true);
                HashMap<String, Object[]> queues = connInfo.getMessageQueues();
                List<String> keys = new ArrayList<String>(queues.keySet());
                int index = 0;
                sendQueuesMessge(context, processName, keys, index, queues);
            }
        }

        private void sendQueuesMessge(Context context, String processName, List<String> keys, int index, HashMap<String, Object[]> queues) {
            if (index < keys.size()) {
                String key = keys.get(index);
                Object[] events = queues.get(key);
                index++;
                post(context, processName, key, true, queues.size(), index, new Action3<Integer, Integer, Context>() {
                    @Override
                    public void call(Integer postCount, Integer total, Context context) {
                        //如果消息数与总记录数相等则取消本次服务绑定
                        if (postCount == total && context != null) {
                            context.unbindService(MultiProcessServiceConnection.this);
                            conmap.clear();
                        }
                    }
                }, events);
                sendQueuesMessge(context, processName, keys, index, queues);
            }
        }

        @Override
        public void onBindingDied(ComponentName name) {
            binders.clear();
            if (conmap.containsKey(processName)) {
                conmap.remove(processName);
            }
        }

        @Override
        public void onNullBinding(ComponentName name) {
            binders.clear();
            if (conmap.containsKey(processName)) {
                conmap.remove(processName);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binders.clear();
            if (conmap.containsKey(processName)) {
                conmap.remove(processName);
            }
        }
    }

    private class HandlerRunnable extends RunnableParamsN<EBusItem> {
        @Override
        public void run(EBusItem... eBusItems) {
            if (ObjectJudge.isNullOrEmpty(eBusItems)) {
                return;
            }
            EBusItem busItem = eBusItems[0];
            Method method = busItem.getMethod();
            try {
                method.invoke(busItem.getSubscriber(), busItem.getArgs());
            } catch (Exception e) {
                Logger.error(e);
            }
        }
    }
}
