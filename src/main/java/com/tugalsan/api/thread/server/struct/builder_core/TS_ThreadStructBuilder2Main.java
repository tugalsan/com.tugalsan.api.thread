package com.tugalsan.api.thread.server.struct.builder_core;

import com.tugalsan.api.runnable.client.TGS_Runnable;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.validator.client.TGS_ValidatorType1;
import java.time.Duration;
import java.util.Optional;

public class TS_ThreadStructBuilder2Main<T> {

    public TS_ThreadStructBuilder2Main(String name,
            TS_ThreadStructCallableTimed<T> init, TS_ThreadStructRunnableTimedType2<T> main) {
        this.name = name;
        this.init = init;
        this.main = main;
    }
    final public String name;
    final public TS_ThreadStructCallableTimed<T> init;
    final public TS_ThreadStructRunnableTimedType2<T> main;

    public <T> TS_ThreadStructBuilder3Fin<T> finEmpty() {
        return new TS_ThreadStructBuilder3Fin(name, init, main, TS_ThreadStructRunnableTimedType1.empty());
    }

    public <T> TS_ThreadStructBuilder3Fin<T> fin(TGS_Runnable run) {
        TGS_RunnableType1<T> initObj = o -> run.run();
        return new TS_ThreadStructBuilder3Fin(name, init, main, TS_ThreadStructRunnableTimedType1.run(initObj));
    }

    public <T> TS_ThreadStructBuilder3Fin<T> finTimed(Duration max, TGS_Runnable run) {
        TGS_RunnableType1<T> initObj = o -> run.run();
        return new TS_ThreadStructBuilder3Fin(name, init, main, TS_ThreadStructRunnableTimedType1.maxTimedRun(max, initObj));
    }

    public <T> TS_ThreadStructBuilder3Fin<T> fin(TGS_RunnableType1<T> initObj) {
        return new TS_ThreadStructBuilder3Fin(name, init, main, TS_ThreadStructRunnableTimedType1.run(initObj));
    }

    public <T> TS_ThreadStructBuilder3Fin<T> finTimed(Duration max, TGS_RunnableType1<T> initObj) {
        return new TS_ThreadStructBuilder3Fin(name, init, main, TS_ThreadStructRunnableTimedType1.maxTimedRun(max, initObj));
    }

    @Deprecated//Complicated
    private TS_ThreadStruct<T> build(Optional<TGS_ValidatorType1<T>> valCycleMain, Optional<Duration> durPeriodCycle) {
        return TS_ThreadStruct.of(name, init, main, TS_ThreadStructRunnableTimedType1.empty(), valCycleMain, durPeriodCycle);
    }

    public TS_ThreadStruct<T> cycle_none() {
        return build(
                Optional.empty(),
                Optional.empty()
        );
    }

    public TS_ThreadStruct<T> cycle_forever() {
        return cycle_mainValidation(o -> true);
    }

    public TS_ThreadStruct<T> cycle_mainDuration(Duration durPeriodCycle) {
        return build(
                Optional.empty(),
                durPeriodCycle == null ? Optional.empty() : Optional.of(durPeriodCycle)
        );
    }

    public TS_ThreadStruct<T> cycle_mainValidation(TGS_ValidatorType1<T> valCycleMain) {
        return build(
                valCycleMain == null ? Optional.empty() : Optional.of(valCycleMain),
                Optional.empty()
        );
    }

    public TS_ThreadStruct<T> cycle_mainValidation_mainDuration(TGS_ValidatorType1<T> valCycleMain, Duration durPeriodCycle) {
        return build(
                valCycleMain == null ? Optional.empty() : Optional.of(valCycleMain),
                durPeriodCycle == null ? Optional.empty() : Optional.of(durPeriodCycle)
        );
    }

    public TS_ThreadStruct<T> asyncRun() {
        return cycle_none().asyncRun();
    }

    public TS_ThreadStruct<T> asyncAwait() {
        return cycle_none().asyncAwait();
    }

    public TS_ThreadStruct<T> asyncAwait(Duration until) {
        return cycle_none().asyncAwait(until);
    }
}