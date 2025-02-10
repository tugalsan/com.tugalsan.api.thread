package com.tugalsan.api.thread.server.async.builder;



import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCE_In1;
import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCE_OutBool_In1;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import java.time.Duration;
import java.util.Optional;
import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCE;

public class TS_ThreadAsyncBuilder3Main<T> {

    public TS_ThreadAsyncBuilder3Main(TS_ThreadSyncTrigger killTrigger, String name,
            TS_ThreadAsyncBuilderCallableTimed<T> init, TS_ThreadAsyncBuilderRunnableTimedType2<T> main) {
        this.killTrigger = killTrigger;
        this.name = name;
        this.init = init;
        this.main = main;
    }
    final public TS_ThreadSyncTrigger killTrigger;
    final public String name;
    final public TS_ThreadAsyncBuilderCallableTimed<T> init;
    final public TS_ThreadAsyncBuilderRunnableTimedType2<T> main;

    public <T> TS_ThreadAsyncBuilder4Fin<T> finEmpty() {
        return new TS_ThreadAsyncBuilder4Fin(killTrigger, name, init, main, TS_ThreadAsyncBuilderRunnableTimedType1.empty());
    }

    public <T> TS_ThreadAsyncBuilder4Fin<T> fin(TGS_FuncMTUCE run) {
        TGS_FuncMTUCE_In1<T> initObj = o -> run.run();
        return new TS_ThreadAsyncBuilder4Fin(killTrigger, name, init, main, TS_ThreadAsyncBuilderRunnableTimedType1.run(initObj));
    }

    public <T> TS_ThreadAsyncBuilder4Fin<T> finTimed(Duration max, TGS_FuncMTUCE run) {
        TGS_FuncMTUCE_In1<T> initObj = o -> run.run();
        return new TS_ThreadAsyncBuilder4Fin(killTrigger, name, init, main, TS_ThreadAsyncBuilderRunnableTimedType1.maxTimedRun(max, initObj));
    }

    public <T> TS_ThreadAsyncBuilder4Fin<T> fin(TGS_FuncMTUCE_In1<T> initObj) {
        return new TS_ThreadAsyncBuilder4Fin(killTrigger, name, init, main, TS_ThreadAsyncBuilderRunnableTimedType1.run(initObj));
    }

    public <T> TS_ThreadAsyncBuilder4Fin<T> finTimed(Duration max, TGS_FuncMTUCE_In1<T> initObj) {
        return new TS_ThreadAsyncBuilder4Fin(killTrigger, name, init, main, TS_ThreadAsyncBuilderRunnableTimedType1.maxTimedRun(max, initObj));
    }

    @Deprecated//Complicated
    private TS_ThreadAsyncBuilderObject<T> build(Optional<TGS_FuncMTUCE_OutBool_In1<T>> valCycleMain, Optional<Duration> durPeriodCycle) {
        return TS_ThreadAsyncBuilderObject.of(killTrigger, name, init, main, TS_ThreadAsyncBuilderRunnableTimedType1.empty(), valCycleMain, durPeriodCycle);
    }

    public TS_ThreadAsyncBuilderObject<T> cycle_none() {
        return build(
                Optional.empty(),
                Optional.empty()
        );
    }

    public TS_ThreadAsyncBuilderObject<T> cycle_forever() {
        return cycle_mainValidation(o -> true);
    }

    public TS_ThreadAsyncBuilderObject<T> cycle_mainPeriod(Duration durPeriodCycle) {
        return build(
                Optional.empty(),
                durPeriodCycle == null ? Optional.empty() : Optional.of(durPeriodCycle)
        );
    }

    public TS_ThreadAsyncBuilderObject<T> cycle_mainValidation(TGS_FuncMTUCE_OutBool_In1<T> valCycleMain) {
        return build(
                valCycleMain == null ? Optional.empty() : Optional.of(valCycleMain),
                Optional.empty()
        );
    }

    public TS_ThreadAsyncBuilderObject<T> cycle_mainValidation_mainPeriod(TGS_FuncMTUCE_OutBool_In1<T> valCycleMain, Duration durPeriodCycle) {
        return build(
                valCycleMain == null ? Optional.empty() : Optional.of(valCycleMain),
                durPeriodCycle == null ? Optional.empty() : Optional.of(durPeriodCycle)
        );
    }

    public TS_ThreadAsyncBuilderObject<T> asyncRun() {
        return cycle_none().asyncRun();
    }

    public TS_ThreadAsyncBuilderObject<T> asyncRun(Duration until) {
        return cycle_none().asyncRun(until);
    }

    public TS_ThreadAsyncBuilderObject<T> asyncRunAwait() {
        return cycle_none().asyncRunAwait();
    }

    public TS_ThreadAsyncBuilderObject<T> asyncRunAwait(Duration until) {
        return cycle_none().asyncRunAwait(until);
    }
}
