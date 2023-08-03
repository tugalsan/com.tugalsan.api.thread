package com.tugalsan.api.thread.server.struct.core;

import com.tugalsan.api.thread.server.TS_ThreadKillTrigger;
import com.tugalsan.api.validator.client.TGS_ValidatorType1;
import java.time.Duration;
import java.util.Optional;

public class TS_ThreadStructBuilder4Fin<T> {

    public TS_ThreadStructBuilder4Fin(TS_ThreadKillTrigger killTrigger, String name,
            TS_ThreadStructCallableTimed<T> init, TS_ThreadStructRunnableTimedType2<T> main, TS_ThreadStructRunnableTimedType1<T> fin) {
        this.killTrigger = killTrigger;
        this.name = name;
        this.init = init;
        this.main = main;
        this.fin = fin;
    }
    final private TS_ThreadKillTrigger killTrigger;
    final private String name;
    final private TS_ThreadStructCallableTimed<T> init;
    final private TS_ThreadStructRunnableTimedType2<T> main;
    final private TS_ThreadStructRunnableTimedType1<T> fin;

    @Deprecated//Complicated
    private TS_ThreadStruct<T> build(Optional<TGS_ValidatorType1<T>> valCycleMain, Optional<Duration> durPeriodCycle) {
        return TS_ThreadStruct.of(killTrigger, name, init, main, fin, valCycleMain, durPeriodCycle);
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

    public TS_ThreadStruct<T> asyncRun(Duration until) {
        return cycle_none().asyncRun(until);
    }

    public TS_ThreadStruct<T> asyncRunAwait() {
        return cycle_none().asyncRunAwait();
    }

    public TS_ThreadStruct<T> asyncRunAwait(Duration until) {
        return cycle_none().asyncRunAwait(until);
    }
}
