package com.tugalsan.api.thread.server.struct.builder_core;

import com.tugalsan.api.runnable.client.TGS_RunnableType2;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

public class TS_ThreadStructBuilder1Init<T> {

    public TS_ThreadStructBuilder1Init(String name, TS_ThreadStructCallableTimed<T> init) {
        this.name = name;
        this.init = init;
    }
    final private String name;
    final private TS_ThreadStructCallableTimed<T> init;

    public <T> TS_ThreadStructBuilder2Main<T> mainEmpty() {
        return new TS_ThreadStructBuilder2Main(name, init, TS_ThreadStructRunnableTimedType2.empty());
    }

    public <T> TS_ThreadStructBuilder2Main<T> main(TGS_RunnableType2<AtomicBoolean, T> killTrigger_initObj) {
        return new TS_ThreadStructBuilder2Main(name, init, TS_ThreadStructRunnableTimedType2.run(killTrigger_initObj));
    }

    public <T> TS_ThreadStructBuilder2Main<T> mainTimed(Duration max, TGS_RunnableType2<AtomicBoolean, T> killTrigger_initObj) {
        return new TS_ThreadStructBuilder2Main(name, init, TS_ThreadStructRunnableTimedType2.maxTimedRun(max, killTrigger_initObj));
    }
}
