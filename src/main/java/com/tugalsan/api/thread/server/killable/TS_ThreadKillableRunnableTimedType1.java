package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import java.time.Duration;
import java.util.Optional;

public class TS_ThreadKillableRunnableTimedType1<A> {

    private TS_ThreadKillableRunnableTimedType1(Optional<Duration> max, Optional<TGS_RunnableType1<A>> run) {
        this.max = max;
        this.run = run;
    }
    final public Optional<Duration> max;
    final public Optional<TGS_RunnableType1<A>> run;

    public static TS_ThreadKillableRunnableTimedType1 empty() {
        return new TS_ThreadKillableRunnableTimedType1(Optional.empty(), Optional.empty());
    }

    public static <A> TS_ThreadKillableRunnableTimedType1 run(TGS_RunnableType1<A> run) {
        return new TS_ThreadKillableRunnableTimedType1(Optional.empty(), Optional.of(run));
    }

    public static <A> TS_ThreadKillableRunnableTimedType1 maxTimedRun(Duration max, TGS_RunnableType1<A> run) {
        return new TS_ThreadKillableRunnableTimedType1(Optional.of(max), Optional.of(run));
    }

    @Override
    public String toString() {
        return TS_ThreadKillableRunnableTimedType1.class.getSimpleName() + "{" + "max=" + max + ", run=" + run + '}';
    }
}