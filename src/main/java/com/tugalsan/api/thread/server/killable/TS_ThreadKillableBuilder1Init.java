package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.runnable.client.TGS_RunnableType2;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

public class TS_ThreadKillableBuilder1Init<T> {

    protected TS_ThreadKillableBuilder1Init(String name, TS_ThreadKillableCallableTimed<T> init) {
        this.name = name;
        this.init = init;
    }
    final private String name;
    final private TS_ThreadKillableCallableTimed<T> init;

    public <T> TS_ThreadKillableBuilder2Main<T> mainEmpty() {
        return new TS_ThreadKillableBuilder2Main(name, init, TS_ThreadKillableRunnableTimedType2.empty());
    }

    public <T> TS_ThreadKillableBuilder2Main<T> main(TGS_RunnableType2<AtomicBoolean, T> run) {
        return new TS_ThreadKillableBuilder2Main(name, init, TS_ThreadKillableRunnableTimedType2.run(run));
    }

    public <T> TS_ThreadKillableBuilder2Main<T> mainTimed(Duration max, TGS_RunnableType2<AtomicBoolean, T> run) {
        return new TS_ThreadKillableBuilder2Main(name, init, TS_ThreadKillableRunnableTimedType2.maxTimedRun(max, run));
    }
}
