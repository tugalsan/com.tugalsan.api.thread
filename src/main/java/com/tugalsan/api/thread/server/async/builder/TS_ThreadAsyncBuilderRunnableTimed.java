package com.tugalsan.api.thread.server.async.builder;


import java.time.Duration;
import java.util.Optional;
import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCE;

public class TS_ThreadAsyncBuilderRunnableTimed {

    private TS_ThreadAsyncBuilderRunnableTimed(Optional<Duration> max, Optional<TGS_FuncMTUCE> run) {
        this.max = max;
        this.run = run;
    }
    final public Optional<Duration> max;
    final public Optional<TGS_FuncMTUCE> run;

    public static TS_ThreadAsyncBuilderRunnableTimed of() {
        return new TS_ThreadAsyncBuilderRunnableTimed(Optional.empty(), Optional.empty());
    }

    public static TS_ThreadAsyncBuilderRunnableTimed of(TGS_FuncMTUCE run) {
        return new TS_ThreadAsyncBuilderRunnableTimed(Optional.empty(), Optional.of(run));
    }

    public static TS_ThreadAsyncBuilderRunnableTimed of(Duration max, TGS_FuncMTUCE run) {
        return new TS_ThreadAsyncBuilderRunnableTimed(Optional.of(max), Optional.of(run));
    }

    @Override
    public String toString() {
        return TS_ThreadAsyncBuilderRunnableTimed.class.getSimpleName() + "{" + "max=" + max + ", run=" + run + '}';
    }
}
