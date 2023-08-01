package com.tugalsan.api.thread.server.struct;

import com.tugalsan.api.callable.client.TGS_Callable;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.runnable.client.TGS_RunnableType2;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class TS_ThreadStructBuilder {

    public static TS_ThreadStructBuilder0Name name(String name) {
        return new TS_ThreadStructBuilder0Name(name);
    }

    public static <T> TS_ThreadStructBuilder1Init<T> initEmpty() {
        return new TS_ThreadStructBuilder1Init("Unnamed", TS_ThreadStructCallableTimed.of());
    }

    public static <T> TS_ThreadStructBuilder1Init<T> init(TGS_Callable<T> call) {
        return new TS_ThreadStructBuilder1Init("Unnamed", TS_ThreadStructCallableTimed.of(call));
    }

    public static <T> TS_ThreadStructBuilder1Init<T> initTimed(Duration max, TGS_Callable<T> call) {
        return new TS_ThreadStructBuilder1Init("Unnamed", TS_ThreadStructCallableTimed.of(max, call));
    }

    public static <T> TS_ThreadStructBuilder2Main<T> main(TGS_RunnableType1<AtomicBoolean> killTriggered) {
        TGS_RunnableType2<AtomicBoolean, Object> killTriggered_initObj = (kt, initObj) -> killTriggered.run(kt);
        return new TS_ThreadStructBuilder2Main("Unnamed", TS_ThreadStructCallableTimed.of(), TS_ThreadStructRunnableTimedType2.run(killTriggered_initObj));
    }

    public static <T> TS_ThreadStructBuilder2Main<T> mainTimed(Duration max, TGS_RunnableType1<AtomicBoolean> killTriggered) {
        TGS_RunnableType2<AtomicBoolean, Object> killTriggered_initObj = (kt, initObj) -> killTriggered.run(kt);
        return new TS_ThreadStructBuilder2Main("Unnamed", TS_ThreadStructCallableTimed.of(), TS_ThreadStructRunnableTimedType2.maxTimedRun(max, killTriggered_initObj));
    }

    public static <T> TS_ThreadStructBuilder2Main<T> asyncRun(TGS_RunnableType1<AtomicBoolean> killTriggered) {
        TGS_RunnableType2<AtomicBoolean, Object> killTriggered_initObj = (kt, initObj) -> killTriggered.run(kt);
        return new TS_ThreadStructBuilder2Main("Unnamed", TS_ThreadStructCallableTimed.of(), TS_ThreadStructRunnableTimedType2.run(killTriggered_initObj));
    }

    public static <T> TS_ThreadStruct<T> asyncRun(Duration max, TGS_RunnableType1<AtomicBoolean> killTriggered) {
        TGS_RunnableType2<AtomicBoolean, Object> killTriggered_initObj = (kt, initObj) -> killTriggered.run(kt);
        var main = new TS_ThreadStructBuilder2Main("Unnamed", TS_ThreadStructCallableTimed.of(), TS_ThreadStructRunnableTimedType2.maxTimedRun(max, killTriggered_initObj));
        return TS_ThreadStruct.of(main.name, main.init, main.main, TS_ThreadStructRunnableTimedType1.empty(), Optional.empty(), Optional.empty());
    }
}
