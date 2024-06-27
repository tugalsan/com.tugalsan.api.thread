package com.tugalsan.api.thread.server.async.builder;


import com.tugalsan.api.function.client.TGS_Func;
import java.time.Duration;
import java.util.Optional;

public class TS_ThreadAsyncBuilderRunnableTimed {

    private TS_ThreadAsyncBuilderRunnableTimed(Optional<Duration> max, Optional<TGS_Func> run) {
        this.max = max;
        this.run = run;
    }
    final public Optional<Duration> max;
    final public Optional<TGS_Func> run;

    public static TS_ThreadAsyncBuilderRunnableTimed of() {
        return new TS_ThreadAsyncBuilderRunnableTimed(Optional.empty(), Optional.empty());
    }

    public static TS_ThreadAsyncBuilderRunnableTimed of(TGS_Func run) {
        return new TS_ThreadAsyncBuilderRunnableTimed(Optional.empty(), Optional.of(run));
    }

    public static TS_ThreadAsyncBuilderRunnableTimed of(Duration max, TGS_Func run) {
        return new TS_ThreadAsyncBuilderRunnableTimed(Optional.of(max), Optional.of(run));
    }

    @Override
    public String toString() {
        return TS_ThreadAsyncBuilderRunnableTimed.class.getSimpleName() + "{" + "max=" + max + ", run=" + run + '}';
    }
}
