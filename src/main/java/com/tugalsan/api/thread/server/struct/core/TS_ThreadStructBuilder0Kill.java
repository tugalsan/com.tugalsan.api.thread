package com.tugalsan.api.thread.server.struct.core;

import com.tugalsan.api.callable.client.TGS_Callable;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.runnable.client.TGS_RunnableType2;
import com.tugalsan.api.thread.server.safe.TS_ThreadSafeTrigger;
import java.time.Duration;
import java.util.Optional;

public class TS_ThreadStructBuilder0Kill {

    public TS_ThreadStructBuilder0Kill(TS_ThreadSafeTrigger killTrigger) {
        this._killTrigger = killTrigger;
    }
    final private TS_ThreadSafeTrigger _killTrigger;

    public TS_ThreadStructBuilder1Name name(String name) {
        return new TS_ThreadStructBuilder1Name(_killTrigger, name);
    }

    public <T> TS_ThreadStructBuilder2Init<T> initEmpty() {
        return new TS_ThreadStructBuilder2Init(_killTrigger, "Unnamed", TS_ThreadStructCallableTimed.of());
    }

    public <T> TS_ThreadStructBuilder2Init<T> init(TGS_Callable<T> call) {
        return new TS_ThreadStructBuilder2Init(_killTrigger, "Unnamed", TS_ThreadStructCallableTimed.of(call));
    }

    public <T> TS_ThreadStructBuilder2Init<T> initTimed(Duration max, TGS_Callable<T> call) {
        return new TS_ThreadStructBuilder2Init(_killTrigger, "Unnamed", TS_ThreadStructCallableTimed.of(max, call));
    }

    public <T> TS_ThreadStructBuilder3Main<T> main(TGS_RunnableType1<TS_ThreadSafeTrigger> killTrigger) {
        TGS_RunnableType2<TS_ThreadSafeTrigger, Object> killTrigger_initObj = (kt, initObj) -> killTrigger.run(kt);
        return new TS_ThreadStructBuilder3Main(_killTrigger, "Unnamed", TS_ThreadStructCallableTimed.of(), TS_ThreadStructRunnableTimedType2.run(killTrigger_initObj));
    }

    public <T> TS_ThreadStructBuilder3Main<T> mainTimed(Duration max, TGS_RunnableType1<TS_ThreadSafeTrigger> killTrigger) {
        TGS_RunnableType2<TS_ThreadSafeTrigger, Object> killTrigger_initObj = (kt, initObj) -> killTrigger.run(kt);
        return new TS_ThreadStructBuilder3Main(_killTrigger, "Unnamed", TS_ThreadStructCallableTimed.of(), TS_ThreadStructRunnableTimedType2.maxTimedRun(max, killTrigger_initObj));
    }

    public <T> TS_ThreadStruct<T> asyncRun(TGS_RunnableType1<TS_ThreadSafeTrigger> killTrigger) {
        TGS_RunnableType2<TS_ThreadSafeTrigger, Object> killTrigger_initObj = (kt, initObj) -> killTrigger.run(kt);
        var main = new TS_ThreadStructBuilder3Main(_killTrigger, "Unnamed", TS_ThreadStructCallableTimed.of(), TS_ThreadStructRunnableTimedType2.run(killTrigger_initObj));
        return TS_ThreadStruct.of(main.killTrigger, main.name, main.init, main.main, TS_ThreadStructRunnableTimedType1.empty(), Optional.empty(), Optional.empty()).asyncRun();
    }

    public <T> TS_ThreadStruct<T> asyncRun(Duration max, TGS_RunnableType1<TS_ThreadSafeTrigger> killTrigger) {
        TGS_RunnableType2<TS_ThreadSafeTrigger, Object> killTrigger_initObj = (kt, initObj) -> killTrigger.run(kt);
        var main = new TS_ThreadStructBuilder3Main(_killTrigger, "Unnamed", TS_ThreadStructCallableTimed.of(), TS_ThreadStructRunnableTimedType2.maxTimedRun(max, killTrigger_initObj));
        return TS_ThreadStruct.of(main.killTrigger, main.name, main.init, main.main, TS_ThreadStructRunnableTimedType1.empty(), Optional.empty(), Optional.empty()).asyncRun();
    }

    public <T> TS_ThreadStruct<T> asyncRunAwait(TGS_RunnableType1<TS_ThreadSafeTrigger> killTrigger) {
        TGS_RunnableType2<TS_ThreadSafeTrigger, Object> killTrigger_initObj = (kt, initObj) -> killTrigger.run(kt);
        var main = new TS_ThreadStructBuilder3Main(_killTrigger, "Unnamed", TS_ThreadStructCallableTimed.of(), TS_ThreadStructRunnableTimedType2.run(killTrigger_initObj));
        return TS_ThreadStruct.of(main.killTrigger, main.name, main.init, main.main, TS_ThreadStructRunnableTimedType1.empty(), Optional.empty(), Optional.empty()).asyncRunAwait();
    }

    public <T> TS_ThreadStruct<T> asyncRunAwait(Duration max, TGS_RunnableType1<TS_ThreadSafeTrigger> killTrigger) {
        TGS_RunnableType2<TS_ThreadSafeTrigger, Object> killTrigger_initObj = (kt, initObj) -> killTrigger.run(kt);
        var main = new TS_ThreadStructBuilder3Main(_killTrigger, "Unnamed", TS_ThreadStructCallableTimed.of(), TS_ThreadStructRunnableTimedType2.run(killTrigger_initObj));
        return TS_ThreadStruct.of(main.killTrigger, main.name, main.init, main.main, TS_ThreadStructRunnableTimedType1.empty(), Optional.empty(), Optional.empty()).asyncRunAwait(max);
    }
}
