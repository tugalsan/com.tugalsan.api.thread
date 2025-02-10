package com.tugalsan.api.thread.server.async.builder;


import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCE_In1;
import java.time.Duration;
import java.util.Optional;

public class TS_ThreadAsyncBuilderRunnableTimedType1<A> {

    private TS_ThreadAsyncBuilderRunnableTimedType1(Optional<Duration> max, Optional<TGS_FuncMTUCE_In1<A>> run) {
        this.max = max;
        this.run = run;
    }
    final public Optional<Duration> max;
    final public Optional<TGS_FuncMTUCE_In1<A>> run;

    public static TS_ThreadAsyncBuilderRunnableTimedType1 empty() {
        return new TS_ThreadAsyncBuilderRunnableTimedType1(Optional.empty(), Optional.empty());
    }

    public static <A> TS_ThreadAsyncBuilderRunnableTimedType1<A> run(TGS_FuncMTUCE_In1<A> run) {
        return new TS_ThreadAsyncBuilderRunnableTimedType1(Optional.empty(), Optional.of(run));
    }

    public static <A> TS_ThreadAsyncBuilderRunnableTimedType1<A> maxTimedRun(Duration max, TGS_FuncMTUCE_In1<A> run) {
        return new TS_ThreadAsyncBuilderRunnableTimedType1(Optional.of(max), Optional.of(run));
    }

    @Override
    public String toString() {
        return TS_ThreadAsyncBuilderRunnableTimedType1.class.getSimpleName() + "{" + "max=" + max + ", run=" + run + '}';
    }
}
