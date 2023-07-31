package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import java.time.Duration;

public class TS_ThreadKillableBuilder2Main<T> {

    protected TS_ThreadKillableBuilder2Main(String name,
            TS_ThreadKillableCallableTimed<T> init, TS_ThreadKillableRunnableTimedType2<T> main) {
        this.name = name;
        this.init = init;
        this.main = main;
    }
    final private String name;
    final private TS_ThreadKillableCallableTimed<T> init;
    final private TS_ThreadKillableRunnableTimedType2<T> main;

    public <T> TS_ThreadKillableBuilder3Fin<T> finEmpty() {
        return new TS_ThreadKillableBuilder3Fin(name, init, main, TS_ThreadKillableRunnableTimedType1.empty());
    }

    public <T> TS_ThreadKillableBuilder3Fin<T> fin(TGS_RunnableType1<T> run) {
        return new TS_ThreadKillableBuilder3Fin(name, init, main, TS_ThreadKillableRunnableTimedType1.run(run));
    }

    public <T> TS_ThreadKillableBuilder3Fin<T> finTimed(Duration max, TGS_RunnableType1<T> run) {
        return new TS_ThreadKillableBuilder3Fin(name, init, main, TS_ThreadKillableRunnableTimedType1.maxTimedRun(max, run));
    }
}
