package com.tugalsan.api.thread.server.sync;

import java.util.List;

import module  com.tugalsan.api.function;

public class TS_ThreadSyncBuffer<T> {

    private TS_ThreadSyncBuffer(TS_ThreadSyncLst<T> lst, TGS_FuncMTU_OutBool_In1<T> exists) {
        this.lst = lst;
        this.exists = exists;
    }
    final private TS_ThreadSyncLst<T> lst;
    final private TGS_FuncMTU_OutBool_In1<T> exists;

    public static <T> TS_ThreadSyncBuffer<T> ofSlowRead(TGS_FuncMTU_OutBool_In1<T> exists) {
        return new TS_ThreadSyncBuffer(TS_ThreadSyncLst.ofSlowRead(), exists);
    }

    public static <T> TS_ThreadSyncBuffer<T> ofSlowWrite(TGS_FuncMTU_OutBool_In1<T> exists) {
        return new TS_ThreadSyncBuffer(TS_ThreadSyncLst.ofSlowWrite(), exists);
    }

    public boolean isExists(T item) {
        return exists.validate(item);
    }

    public T add(T item) {
        return lst.add(item);
    }

    public List<T> add(List<T> items) {
        return lst.add(items);
    }

    public boolean remove(T item) {
        return lst.removeAll(item);
    }

    public TS_ThreadSyncLst<T> clear(T item) {
        return lst.clear();
    }
}
