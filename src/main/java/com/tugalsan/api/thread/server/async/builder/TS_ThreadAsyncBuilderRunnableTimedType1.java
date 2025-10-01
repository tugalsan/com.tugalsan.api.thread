package com.tugalsan.api.thread.server.async.builder;

import module com.tugalsan.api.function;
import java.time.*;
import java.util.*;

public class TS_ThreadAsyncBuilderRunnableTimedType1<T> {

    private TS_ThreadAsyncBuilderRunnableTimedType1(Optional<Duration> max, Optional<TGS_FuncMTU_In1<T>> run) {
        this.max = max;
        this.run = run;
    }
    final public Optional<Duration> max;
    final public Optional<TGS_FuncMTU_In1<T>> run;

    public static TS_ThreadAsyncBuilderRunnableTimedType1 empty() {
        return new TS_ThreadAsyncBuilderRunnableTimedType1(Optional.empty(), Optional.empty());
    }

    public static <T> TS_ThreadAsyncBuilderRunnableTimedType1<T> run(TGS_FuncMTU_In1<T> run) {
        return new TS_ThreadAsyncBuilderRunnableTimedType1(Optional.empty(), Optional.of(run));
    }

    public static <T> TS_ThreadAsyncBuilderRunnableTimedType1<T> maxTimedRun(Duration max, TGS_FuncMTU_In1<T> run) {
        return new TS_ThreadAsyncBuilderRunnableTimedType1(Optional.of(max), Optional.of(run));
    }

    @Override
    public String toString() {
        return TS_ThreadAsyncBuilderRunnableTimedType1.class.getSimpleName() + "{" + "max=" + max + ", run=" + run + '}';
    }
}
