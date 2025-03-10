package com.tugalsan.api.thread.server.async.builder;

import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCE_In1;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import java.time.Duration;
import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCE_In2;
import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCE_OutTyped;
import java.util.Optional;

public class TS_ThreadAsyncBuilder1Name<T> {

    protected TS_ThreadAsyncBuilder1Name(TS_ThreadSyncTrigger killTrigger, String name) {
        this.killTrigger = killTrigger;
        this.name = name;
    }
    final private TS_ThreadSyncTrigger killTrigger;
    final private String name;

    public TS_ThreadAsyncBuilder2Init<T> initEmpty() {
        return new TS_ThreadAsyncBuilder2Init(killTrigger, name, TS_ThreadAsyncBuilderCallableTimed.of());
    }

    public TS_ThreadAsyncBuilder2Init<T> init(TGS_FuncMTUCE_OutTyped<T> call) {
        return new TS_ThreadAsyncBuilder2Init(killTrigger, name, TS_ThreadAsyncBuilderCallableTimed.of(call));
    }

    public TS_ThreadAsyncBuilder2Init<T> initTimed(Duration max, TGS_FuncMTUCE_OutTyped<T> call) {
        return new TS_ThreadAsyncBuilder2Init(killTrigger, name, TS_ThreadAsyncBuilderCallableTimed.of(max, call));
    }

    public TS_ThreadAsyncBuilder3Main<T> main(TGS_FuncMTUCE_In2<TS_ThreadSyncTrigger, T> killTrigger_initObj) {
        return new TS_ThreadAsyncBuilder3Main(killTrigger, name, TS_ThreadAsyncBuilderCallableTimed.of(), TS_ThreadAsyncBuilderRunnableTimedType2.run(killTrigger_initObj));
    }

    public TS_ThreadAsyncBuilder3Main<T> mainTimed(Duration max, TGS_FuncMTUCE_In2<TS_ThreadSyncTrigger, T> killTrigger_initObj) {
        return new TS_ThreadAsyncBuilder3Main(killTrigger, name, TS_ThreadAsyncBuilderCallableTimed.of(), TS_ThreadAsyncBuilderRunnableTimedType2.maxTimedRun(max, killTrigger_initObj));
    }

    public TS_ThreadAsyncBuilder3Main<T> mainDummyForCycle() {
        return main((kt, o) -> {
        });
    }

    public TS_ThreadAsyncBuilderObject<T> asyncRun(TGS_FuncMTUCE_In1<TS_ThreadSyncTrigger> killTrigger) {
        TGS_FuncMTUCE_In2<TS_ThreadSyncTrigger, T> killTrigger_initObj = (kt, initObj) -> killTrigger.run(kt);
        var main = new TS_ThreadAsyncBuilder3Main(this.killTrigger, name, TS_ThreadAsyncBuilderCallableTimed.of(), TS_ThreadAsyncBuilderRunnableTimedType2.run(killTrigger_initObj));
        return TS_ThreadAsyncBuilderObject.of(main.killTrigger, main.name, main.init, main.main, TS_ThreadAsyncBuilderRunnableTimedType1.empty(), Optional.empty(), Optional.empty()).asyncRun();
    }

    public TS_ThreadAsyncBuilderObject<T> asyncRun(Duration max, TGS_FuncMTUCE_In1<TS_ThreadSyncTrigger> killTrigger) {
        TGS_FuncMTUCE_In2<TS_ThreadSyncTrigger, T> killTrigger_initObj = (kt, initObj) -> killTrigger.run(kt);
        var main = new TS_ThreadAsyncBuilder3Main(this.killTrigger, name, TS_ThreadAsyncBuilderCallableTimed.of(), TS_ThreadAsyncBuilderRunnableTimedType2.maxTimedRun(max, killTrigger_initObj));
        return TS_ThreadAsyncBuilderObject.of(main.killTrigger, main.name, main.init, main.main, TS_ThreadAsyncBuilderRunnableTimedType1.empty(), Optional.empty(), Optional.empty()).asyncRun();
    }

    public TS_ThreadAsyncBuilderObject<T> asyncRunAwait(TGS_FuncMTUCE_In1<TS_ThreadSyncTrigger> killTrigger) {
        TGS_FuncMTUCE_In2<TS_ThreadSyncTrigger, T> killTrigger_initObj = (kt, initObj) -> killTrigger.run(kt);
        var main = new TS_ThreadAsyncBuilder3Main(this.killTrigger, name, TS_ThreadAsyncBuilderCallableTimed.of(), TS_ThreadAsyncBuilderRunnableTimedType2.run(killTrigger_initObj));
        return TS_ThreadAsyncBuilderObject.of(main.killTrigger, main.name, main.init, main.main, TS_ThreadAsyncBuilderRunnableTimedType1.empty(), Optional.empty(), Optional.empty()).asyncRunAwait();
    }

    public TS_ThreadAsyncBuilderObject<T> asyncRunAwait(Duration max, TGS_FuncMTUCE_In1<TS_ThreadSyncTrigger> killTrigger) {
        TGS_FuncMTUCE_In2<TS_ThreadSyncTrigger, T> killTrigger_initObj = (kt, initObj) -> killTrigger.run(kt);
        var main = new TS_ThreadAsyncBuilder3Main(this.killTrigger, name, TS_ThreadAsyncBuilderCallableTimed.of(), TS_ThreadAsyncBuilderRunnableTimedType2.run(killTrigger_initObj));
        return TS_ThreadAsyncBuilderObject.of(main.killTrigger, main.name, main.init, main.main, TS_ThreadAsyncBuilderRunnableTimedType1.empty(), Optional.empty(), Optional.empty()).asyncRunAwait(max);
    }
}
