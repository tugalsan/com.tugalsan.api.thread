package com.tugalsan.api.thread.server.struct.core;

import com.tugalsan.api.callable.client.TGS_Callable;
import java.time.Duration;
import java.util.Optional;

public class TS_ThreadStructCallableTimed<R> {

    private TS_ThreadStructCallableTimed(Optional<Duration> max, Optional<TGS_Callable<R>> call) {
        this.max = max;
        this.call = call;
    }
    final public Optional<Duration> max;
    final public Optional<TGS_Callable<R>> call;

    public static TS_ThreadStructCallableTimed<Object> of() {
        return new TS_ThreadStructCallableTimed(Optional.empty(), Optional.empty());
    }

    public static <R> TS_ThreadStructCallableTimed<R> of(TGS_Callable<R> call) {
        return new TS_ThreadStructCallableTimed(Optional.empty(), Optional.of(call));
    }

    public static <R> TS_ThreadStructCallableTimed<R> of(Duration max, TGS_Callable<R> call) {
        return new TS_ThreadStructCallableTimed(Optional.of(max), Optional.of(call));
    }

    @Override
    public String toString() {
        return TS_ThreadStructCallableTimed.class.getSimpleName() + "{" + "max=" + max + ", call=" + call + '}';
    }
}
