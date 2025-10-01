package com.tugalsan.api.thread.server.async.builder;

import module com.tugalsan.api.function;
import module com.tugalsan.api.thread;
import java.time.*;
import java.util.*;

public class TS_ThreadAsyncBuilderRunnableTimedType2<T> {

    private TS_ThreadAsyncBuilderRunnableTimedType2(Optional<Duration> max, Optional<TGS_FuncMTU_In2<TS_ThreadSyncTrigger, T>> run) {
        this.max = max;
        this.run = run;
    }
    final public Optional<Duration> max;
    final public Optional<TGS_FuncMTU_In2<TS_ThreadSyncTrigger, T>> run;

    public static TS_ThreadAsyncBuilderRunnableTimedType2 empty() {
        return new TS_ThreadAsyncBuilderRunnableTimedType2(Optional.empty(), Optional.empty());
    }

    public static <T> TS_ThreadAsyncBuilderRunnableTimedType2<T> run(TGS_FuncMTU_In2<TS_ThreadSyncTrigger, T> run) {
        return new TS_ThreadAsyncBuilderRunnableTimedType2(Optional.empty(), Optional.of(run));
    }

    public static <T> TS_ThreadAsyncBuilderRunnableTimedType2<T> maxTimedRun(Duration max, TGS_FuncMTU_In2<TS_ThreadSyncTrigger, T> run) {
        return new TS_ThreadAsyncBuilderRunnableTimedType2(Optional.of(max), Optional.of(run));
    }

    @Override
    public String toString() {
        return TS_ThreadAsyncBuilderRunnableTimedType2.class.getSimpleName() + "{" + "max=" + max + ", run=" + run + '}';
    }
}
