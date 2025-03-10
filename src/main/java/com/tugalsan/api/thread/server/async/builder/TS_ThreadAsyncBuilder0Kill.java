package com.tugalsan.api.thread.server.async.builder;

import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.log.server.TS_Log;

public class TS_ThreadAsyncBuilder0Kill<T> {

    final private static TS_Log d = TS_Log.of(TS_ThreadAsyncBuilder0Kill.class);

    protected TS_ThreadAsyncBuilder0Kill(TS_ThreadSyncTrigger killTrigger) {
        this.killTrigger = TS_ThreadSyncTrigger.of(d.className, killTrigger);
    }
    final private TS_ThreadSyncTrigger killTrigger;

    public TS_ThreadAsyncBuilder1Name<T> name(String name) {
        return new TS_ThreadAsyncBuilder1Name(killTrigger, name);
    }
//
//    public TS_ThreadAsyncBuilder2Init<T> initEmpty() {
//        return new TS_ThreadAsyncBuilder2Init(killTrigger, "Unnamed", TS_ThreadAsyncBuilderCallableTimed.of());
//    }
//
//    public TS_ThreadAsyncBuilder2Init<T> init(TGS_FuncMTUCE_OutTyped<T> call) {
//        return new TS_ThreadAsyncBuilder2Init(killTrigger, "Unnamed", TS_ThreadAsyncBuilderCallableTimed.of(call));
//    }
//
//    public TS_ThreadAsyncBuilder2Init<T> initTimed(Duration max, TGS_FuncMTUCE_OutTyped<T> call) {
//        return new TS_ThreadAsyncBuilder2Init(killTrigger, "Unnamed", TS_ThreadAsyncBuilderCallableTimed.of(max, call));
//    }
//
//    public TS_ThreadAsyncBuilder3Main<T> main(TGS_FuncMTUCE_In1<TS_ThreadSyncTrigger> killTrigger) {
//        TGS_FuncMTUCE_In2<TS_ThreadSyncTrigger, T> killTrigger_initObj = (kt, initObj) -> killTrigger.run(kt);
//        return new TS_ThreadAsyncBuilder3Main(this.killTrigger, "Unnamed", TS_ThreadAsyncBuilderCallableTimed.of(), TS_ThreadAsyncBuilderRunnableTimedType2.run(killTrigger_initObj));
//    }
//
//    public TS_ThreadAsyncBuilder3Main<T> mainDummyForCycle() {
//        return main(kt -> {
//        });
//    }
//
//    public TS_ThreadAsyncBuilder3Main<T> mainTimed(Duration max, TGS_FuncMTUCE_In1<TS_ThreadSyncTrigger> killTrigger) {
//        TGS_FuncMTUCE_In2<TS_ThreadSyncTrigger, T> killTrigger_initObj = (kt, initObj) -> killTrigger.run(kt);
//        return new TS_ThreadAsyncBuilder3Main(this.killTrigger, "Unnamed", TS_ThreadAsyncBuilderCallableTimed.of(), TS_ThreadAsyncBuilderRunnableTimedType2.maxTimedRun(max, killTrigger_initObj));
//    }
//
//    public TS_ThreadAsyncBuilderObject<T> asyncRun(TGS_FuncMTUCE_In1<TS_ThreadSyncTrigger> killTrigger) {
//        TGS_FuncMTUCE_In2<TS_ThreadSyncTrigger, T> killTrigger_initObj = (kt, initObj) -> killTrigger.run(kt);
//        var main = new TS_ThreadAsyncBuilder3Main(this.killTrigger, "Unnamed", TS_ThreadAsyncBuilderCallableTimed.of(), TS_ThreadAsyncBuilderRunnableTimedType2.run(killTrigger_initObj));
//        return TS_ThreadAsyncBuilderObject.of(main.killTrigger, main.name, main.init, main.main, TS_ThreadAsyncBuilderRunnableTimedType1.empty(), Optional.empty(), Optional.empty()).asyncRun();
//    }
//
//    public TS_ThreadAsyncBuilderObject<T> asyncRun(Duration max, TGS_FuncMTUCE_In1<TS_ThreadSyncTrigger> killTrigger) {
//        TGS_FuncMTUCE_In2<TS_ThreadSyncTrigger, T> killTrigger_initObj = (kt, initObj) -> killTrigger.run(kt);
//        var main = new TS_ThreadAsyncBuilder3Main(this.killTrigger, "Unnamed", TS_ThreadAsyncBuilderCallableTimed.of(), TS_ThreadAsyncBuilderRunnableTimedType2.maxTimedRun(max, killTrigger_initObj));
//        return TS_ThreadAsyncBuilderObject.of(main.killTrigger, main.name, main.init, main.main, TS_ThreadAsyncBuilderRunnableTimedType1.empty(), Optional.empty(), Optional.empty()).asyncRun();
//    }
//
//    public TS_ThreadAsyncBuilderObject<T> asyncRunAwait(TGS_FuncMTUCE_In1<TS_ThreadSyncTrigger> killTrigger) {
//        TGS_FuncMTUCE_In2<TS_ThreadSyncTrigger, T> killTrigger_initObj = (kt, initObj) -> killTrigger.run(kt);
//        var main = new TS_ThreadAsyncBuilder3Main(this.killTrigger, "Unnamed", TS_ThreadAsyncBuilderCallableTimed.of(), TS_ThreadAsyncBuilderRunnableTimedType2.run(killTrigger_initObj));
//        return TS_ThreadAsyncBuilderObject.of(main.killTrigger, main.name, main.init, main.main, TS_ThreadAsyncBuilderRunnableTimedType1.empty(), Optional.empty(), Optional.empty()).asyncRunAwait();
//    }
//
//    public TS_ThreadAsyncBuilderObject<T> asyncRunAwait(Duration max, TGS_FuncMTUCE_In1<TS_ThreadSyncTrigger> killTrigger) {
//        TGS_FuncMTUCE_In2<TS_ThreadSyncTrigger, T> killTrigger_initObj = (kt, initObj) -> killTrigger.run(kt);
//        var main = new TS_ThreadAsyncBuilder3Main(this.killTrigger, "Unnamed", TS_ThreadAsyncBuilderCallableTimed.of(), TS_ThreadAsyncBuilderRunnableTimedType2.run(killTrigger_initObj));
//        return TS_ThreadAsyncBuilderObject.of(main.killTrigger, main.name, main.init, main.main, TS_ThreadAsyncBuilderRunnableTimedType1.empty(), Optional.empty(), Optional.empty()).asyncRunAwait(max);
//    }
}
