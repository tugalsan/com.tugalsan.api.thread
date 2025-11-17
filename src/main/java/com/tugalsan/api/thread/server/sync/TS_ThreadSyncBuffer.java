package com.tugalsan.api.thread.server.sync;

import module com.tugalsan.api.log;
import java.util.List;
import java.util.Optional;

import module  com.tugalsan.api.function;

public class TS_ThreadSyncBuffer<T> {

    final static private TS_Log d = TS_Log.of(TS_ThreadSyncBuffer.class);

    private TS_ThreadSyncBuffer(TS_ThreadSyncLst<T> lst, TGS_FuncMTU_OutTyped_In1<Optional<T>, T> finder) {
        this.lst = lst;
        this.finder = finder;
    }
    final public TS_ThreadSyncLst<T> lst;
    public volatile TGS_FuncMTU_OutTyped_In1<Optional<T>, T> finder;

    public static <T> TS_ThreadSyncBuffer<T> ofSlowRead(TGS_FuncMTU_OutTyped_In1<Optional<T>, T> finder) {
        return new TS_ThreadSyncBuffer(TS_ThreadSyncLst.ofSlowRead(), finder);
    }

    public static <T> TS_ThreadSyncBuffer<T> ofSlowWrite(TGS_FuncMTU_OutTyped_In1<Optional<T>, T> finder) {
        return new TS_ThreadSyncBuffer(TS_ThreadSyncLst.ofSlowWrite(), finder);
    }

    public Optional<T> findAny(T item) {
        if (finder == null) {
            d.ce("findAny", "ERROR: finder not set!");
            return Optional.empty();
        }
        return finder.call(item);
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
