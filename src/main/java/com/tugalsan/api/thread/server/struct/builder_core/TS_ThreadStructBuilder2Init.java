package com.tugalsan.api.thread.server.struct.builder_core;

import com.tugalsan.api.runnable.client.TGS_RunnableType2;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

public class TS_ThreadStructBuilder2Init<T> {

    public TS_ThreadStructBuilder2Init(AtomicBoolean killTrigger, String name, TS_ThreadStructCallableTimed<T> init) {
        this.killTrigger = killTrigger;
        this.name = name;
        this.init = init;
    }
    final private AtomicBoolean killTrigger;
    final private String name;
    final private TS_ThreadStructCallableTimed<T> init;

    public <T> TS_ThreadStructBuilder3Main<T> mainEmpty() {
        return new TS_ThreadStructBuilder3Main(killTrigger, name, init, TS_ThreadStructRunnableTimedType2.empty());
    }

    public <T> TS_ThreadStructBuilder3Main<T> main(TGS_RunnableType2<AtomicBoolean, T> killTrigger_initObj) {
        return new TS_ThreadStructBuilder3Main(killTrigger, name, init, TS_ThreadStructRunnableTimedType2.run(killTrigger_initObj));
    }

    public <T> TS_ThreadStructBuilder3Main<T> mainTimed(Duration max, TGS_RunnableType2<AtomicBoolean, T> killTrigger_initObj) {
        return new TS_ThreadStructBuilder3Main(killTrigger, name, init, TS_ThreadStructRunnableTimedType2.maxTimedRun(max, killTrigger_initObj));
    }
}
