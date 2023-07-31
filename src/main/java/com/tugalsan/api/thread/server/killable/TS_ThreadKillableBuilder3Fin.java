package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.validator.client.TGS_ValidatorType1;
import java.time.Duration;
import java.util.Optional;

public class TS_ThreadKillableBuilder3Fin<T> {

    protected TS_ThreadKillableBuilder3Fin(String name,
            TS_ThreadKillableCallableTimed<T> init, TS_ThreadKillableRunnableTimedType2<T> main, TS_ThreadKillableRunnableTimedType1<T> fin) {
        this.name = name;
        this.init = init;
        this.main = main;
        this.fin = fin;
    }
    final private String name;
    final private TS_ThreadKillableCallableTimed<T> init;
    final private TS_ThreadKillableRunnableTimedType2<T> main;
    final private TS_ThreadKillableRunnableTimedType1<T> fin;

    @Deprecated//Complicated
    public TS_ThreadKillable<T> build(Optional<TGS_ValidatorType1<T>> valCycleMain, Optional<Duration> durPeriodCycle) {
        return TS_ThreadKillable.of(name, init, main, fin, valCycleMain, durPeriodCycle);
    }

    public TS_ThreadKillable<T> build() {
        return build(
                Optional.empty(),
                Optional.empty()
        );
    }

    public TS_ThreadKillable<T> build_cyleMainDuration(Duration durPeriodCycle) {
        return build(
                Optional.empty(),
                durPeriodCycle == null ? Optional.empty() : Optional.of(durPeriodCycle)
        );
    }

    public TS_ThreadKillable<T> build_cyleMainValidation(TGS_ValidatorType1<T> valCycleMain) {
        return build(
                valCycleMain == null ? Optional.empty() : Optional.of(valCycleMain),
                Optional.empty()
        );
    }

    public TS_ThreadKillable<T> build(TGS_ValidatorType1<T> valCycleMain, Duration durPeriodCycle) {
        return build(
                valCycleMain == null ? Optional.empty() : Optional.of(valCycleMain),
                durPeriodCycle == null ? Optional.empty() : Optional.of(durPeriodCycle)
        );
    }
}
