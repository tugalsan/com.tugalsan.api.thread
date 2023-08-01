package com.tugalsan.api.thread.server.struct;

import com.tugalsan.api.thread.server.struct.TS_ThreadStructRunnableTimedType2;
import com.tugalsan.api.validator.client.TGS_ValidatorType1;
import java.time.Duration;
import java.util.Optional;

public class TS_ThreadStructBuilder3Fin<T> {

    protected TS_ThreadStructBuilder3Fin(String name,
            TS_ThreadStructCallableTimed<T> init, TS_ThreadStructRunnableTimedType2<T> main, TS_ThreadStructRunnableTimedType1<T> fin) {
        this.name = name;
        this.init = init;
        this.main = main;
        this.fin = fin;
    }
    final private String name;
    final private TS_ThreadStructCallableTimed<T> init;
    final private TS_ThreadStructRunnableTimedType2<T> main;
    final private TS_ThreadStructRunnableTimedType1<T> fin;

    @Deprecated//Complicated
    public TS_ThreadStruct<T> build(Optional<TGS_ValidatorType1<T>> valCycleMain, Optional<Duration> durPeriodCycle) {
        return TS_ThreadStruct.of(name, init, main, fin, valCycleMain, durPeriodCycle);
    }

    public TS_ThreadStruct<T> single() {
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
