package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.callable.client.TGS_Callable;
import java.time.Duration;
import java.util.Optional;

public class TS_ThreadKillableCallableTimed<R> {

    private TS_ThreadKillableCallableTimed(Optional<Duration> max, Optional<TGS_Callable<R>> call) {
        this.max = max;
        this.call = call;
    }
    final public Optional<Duration> max;
    final public Optional<TGS_Callable<R>> call;

    public static TS_ThreadKillableCallableTimed<Object> of() {
        return new TS_ThreadKillableCallableTimed(Optional.empty(), Optional.empty());
    }

    public static <R> TS_ThreadKillableCallableTimed<R> of(TGS_Callable<R> call) {
        return new TS_ThreadKillableCallableTimed(Optional.empty(), Optional.of(call));
    }

    public static <R> TS_ThreadKillableCallableTimed<R> of(Duration max, TGS_Callable<R> call) {
        return new TS_ThreadKillableCallableTimed(Optional.of(max), Optional.of(call));
    }

    @Override
    public String toString() {
        return TS_ThreadKillableCallableTimed.class.getSimpleName() + "{" + "max=" + max + ", call=" + call + '}';
    }
}
