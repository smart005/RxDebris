package com.cloud.objects.events;

public interface Action4<T1, T2, T3, T4> {
    public void call(T1 t1, T2 t2, T3 t3, T4 t4);
}