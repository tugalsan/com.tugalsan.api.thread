package com.tugalsan.api.thread.server.async.builder;

import module com.tugalsan.api.function;
import java.time.*;
import java.util.*;

public class TS_ThreadAsyncBuilderCallableTimed<T> {

    private TS_ThreadAsyncBuilderCallableTimed(Optional<Duration> max, Optional<TGS_FuncMTU_OutTyped<T>> call) {
        this.max = max;
        this.call = call;
    }
    final public Optional<Duration> max;
    final public Optional<TGS_FuncMTU_OutTyped<T>> call;

    public static TS_ThreadAsyncBuilderCallableTimed<Object> of() {
        return new TS_ThreadAsyncBuilderCallableTimed(Optional.empty(), Optional.empty());
    }

    public static <R> TS_ThreadAsyncBuilderCallableTimed<R> of(TGS_FuncMTU_OutTyped<R> call) {
        return new TS_ThreadAsyncBuilderCallableTimed(Optional.empty(), Optional.of(call));
    }

    public static <R> TS_ThreadAsyncBuilderCallableTimed<R> of(Duration max, TGS_FuncMTU_OutTyped<R> call) {
        return new TS_ThreadAsyncBuilderCallableTimed(Optional.of(max), Optional.of(call));
    }

    @Override
    public String toString() {
        return TS_ThreadAsyncBuilderCallableTimed.class.getSimpleName() + "{" + "max=" + max + ", call=" + call + '}';
    }
}
