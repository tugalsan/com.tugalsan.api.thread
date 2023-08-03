package com.tugalsan.api.thread.server.struct.core;

import com.tugalsan.api.runnable.client.TGS_RunnableType2;
import com.tugalsan.api.thread.server.TS_ThreadKillTrigger;
import java.time.Duration;
import java.util.Optional;

public class TS_ThreadStructRunnableTimedType2<B> {

    private TS_ThreadStructRunnableTimedType2(Optional<Duration> max, Optional<TGS_RunnableType2<TS_ThreadKillTrigger, B>> run) {
        this.max = max;
        this.run = run;
    }
    final public Optional<Duration> max;
    final public Optional<TGS_RunnableType2<TS_ThreadKillTrigger, B>> run;

    public static TS_ThreadStructRunnableTimedType2 empty() {
        return new TS_ThreadStructRunnableTimedType2(Optional.empty(), Optional.empty());
    }

    public static <killTrigger, B> TS_ThreadStructRunnableTimedType2<B> run(TGS_RunnableType2<killTrigger, B> run) {
        return new TS_ThreadStructRunnableTimedType2(Optional.empty(), Optional.of(run));
    }

    public static <killTrigger, B> TS_ThreadStructRunnableTimedType2<B> maxTimedRun(Duration max, TGS_RunnableType2<killTrigger, B> run) {
        return new TS_ThreadStructRunnableTimedType2(Optional.of(max), Optional.of(run));
    }

    @Override
    public String toString() {
        return TS_ThreadStructRunnableTimedType2.class.getSimpleName() + "{" + "max=" + max + ", run=" + run + '}';
    }
}
