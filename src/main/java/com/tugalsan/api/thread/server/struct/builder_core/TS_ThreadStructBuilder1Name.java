package com.tugalsan.api.thread.server.struct.builder_core;

import com.tugalsan.api.callable.client.TGS_Callable;
import com.tugalsan.api.runnable.client.TGS_RunnableType2;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

public class TS_ThreadStructBuilder1Name {

    public TS_ThreadStructBuilder1Name(AtomicBoolean killTrigger, String name) {
        this.killTrigger = killTrigger;
        this.name = name;
    }
    final private AtomicBoolean killTrigger;
    final private String name;

    public <T> TS_ThreadStructBuilder2Init<T> initEmpty() {
        return new TS_ThreadStructBuilder2Init(killTrigger, name, TS_ThreadStructCallableTimed.of());
    }

    public <T> TS_ThreadStructBuilder2Init<T> init(TGS_Callable<T> call) {
        return new TS_ThreadStructBuilder2Init(killTrigger, name, TS_ThreadStructCallableTimed.of(call));
    }

    public <T> TS_ThreadStructBuilder2Init<T> initTimed(Duration max, TGS_Callable<T> call) {
        return new TS_ThreadStructBuilder2Init(killTrigger, name, TS_ThreadStructCallableTimed.of(max, call));
    }

    public <T> TS_ThreadStructBuilder3Main<T> main(TGS_RunnableType2<AtomicBoolean, T> killTrigger_initObj) {
        return new TS_ThreadStructBuilder3Main(killTrigger, name, TS_ThreadStructCallableTimed.of(), TS_ThreadStructRunnableTimedType2.run(killTrigger_initObj));
    }

    public <T> TS_ThreadStructBuilder3Main<T> mainTimed(Duration max, TGS_RunnableType2<AtomicBoolean, T> killTrigger_initObj) {
        return new TS_ThreadStructBuilder3Main(killTrigger, name, TS_ThreadStructCallableTimed.of(), TS_ThreadStructRunnableTimedType2.maxTimedRun(max, killTrigger_initObj));
    }

}
