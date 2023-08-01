package com.tugalsan.api.thread.server.struct;

import com.tugalsan.api.thread.server.struct.TS_ThreadStructCallableTimed;
import com.tugalsan.api.thread.server.struct.TS_ThreadStructBuilder3Fin;
import com.tugalsan.api.thread.server.struct.TS_ThreadStructRunnableTimedType1;
import com.tugalsan.api.thread.server.struct.TS_ThreadStructRunnableTimedType2;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import java.time.Duration;

public class TS_ThreadStructBuilder2Main<T> {

    protected TS_ThreadStructBuilder2Main(String name,
            TS_ThreadStructCallableTimed<T> init, TS_ThreadStructRunnableTimedType2<T> main) {
        this.name = name;
        this.init = init;
        this.main = main;
    }
    final private String name;
    final private TS_ThreadStructCallableTimed<T> init;
    final private TS_ThreadStructRunnableTimedType2<T> main;

    public <T> TS_ThreadStructBuilder3Fin<T> finEmpty() {
        return new TS_ThreadStructBuilder3Fin(name, init, main, TS_ThreadStructRunnableTimedType1.empty());
    }

    public <T> TS_ThreadStructBuilder3Fin<T> fin(TGS_RunnableType1<T> run) {
        return new TS_ThreadStructBuilder3Fin(name, init, main, TS_ThreadStructRunnableTimedType1.run(run));
    }

    public <T> TS_ThreadStructBuilder3Fin<T> finTimed(Duration max, TGS_RunnableType1<T> run) {
        return new TS_ThreadStructBuilder3Fin(name, init, main, TS_ThreadStructRunnableTimedType1.maxTimedRun(max, run));
    }
}
