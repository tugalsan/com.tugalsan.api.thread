package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.runnable.client.TGS_RunnableType2;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class TS_ThreadKillableRunnableTimedType2<B> {

    private TS_ThreadKillableRunnableTimedType2(Optional<Duration> max, Optional<TGS_RunnableType2<AtomicBoolean, B>> run) {
        this.max = max;
        this.run = run;
    }
    final public Optional<Duration> max;
    final public Optional<TGS_RunnableType2<AtomicBoolean, B>> run;

    public static TS_ThreadKillableRunnableTimedType2<Object> empty() {
        return new TS_ThreadKillableRunnableTimedType2(Optional.empty(), Optional.empty());
    }

    public static <AtomicBoolean, B> TS_ThreadKillableRunnableTimedType2<B> run(TGS_RunnableType2<AtomicBoolean, B> run) {
        return new TS_ThreadKillableRunnableTimedType2(Optional.empty(), Optional.of(run));
    }

    public static <AtomicBoolean, B> TS_ThreadKillableRunnableTimedType2<B> maxTimedRun(Duration max, TGS_RunnableType2<AtomicBoolean, B> run) {
        return new TS_ThreadKillableRunnableTimedType2(Optional.of(max), Optional.of(run));
    }

    @Override
    public String toString() {
        return TS_ThreadKillableRunnableTimedType2.class.getSimpleName() + "{" + "max=" + max + ", run=" + run + '}';
    }
}
