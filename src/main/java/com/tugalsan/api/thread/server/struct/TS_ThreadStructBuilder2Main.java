package com.tugalsan.api.thread.server.struct;

import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.validator.client.TGS_ValidatorType1;
import java.time.Duration;
import java.util.Optional;

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
}
