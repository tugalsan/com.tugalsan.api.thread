package com.tugalsan.api.thread.server.async.builder;

import module com.tugalsan.api.function;
import module com.tugalsan.api.thread;
import java.time.*;

public class TS_ThreadAsyncBuilder2Init<T> {

    protected TS_ThreadAsyncBuilder2Init(TS_ThreadSyncTrigger killTrigger, String name, TS_ThreadAsyncBuilderCallableTimed<T> init) {
        this.killTrigger = killTrigger;
        this.name = name;
        this.init = init;
    }
    final private TS_ThreadSyncTrigger killTrigger;
    final private String name;
    final private TS_ThreadAsyncBuilderCallableTimed<T> init;

    public TS_ThreadAsyncBuilder3Main<T> mainEmpty() {
        return new TS_ThreadAsyncBuilder3Main(killTrigger, name, init, TS_ThreadAsyncBuilderRunnableTimedType2.empty());
    }

    public TS_ThreadAsyncBuilder3Main<T> main(TGS_FuncMTU_In2<TS_ThreadSyncTrigger, T> killTrigger_initObj) {
        return new TS_ThreadAsyncBuilder3Main(killTrigger, name, init, TS_ThreadAsyncBuilderRunnableTimedType2.run(killTrigger_initObj));
    }

    public TS_ThreadAsyncBuilder3Main<T> mainTimed(Duration max, TGS_FuncMTU_In2<TS_ThreadSyncTrigger, T> killTrigger_initObj) {
        return new TS_ThreadAsyncBuilder3Main(killTrigger, name, init, TS_ThreadAsyncBuilderRunnableTimedType2.maxTimedRun(max, killTrigger_initObj));
    }
}
