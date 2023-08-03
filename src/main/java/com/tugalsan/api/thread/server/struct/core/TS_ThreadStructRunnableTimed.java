package com.tugalsan.api.thread.server.struct.core;

import com.tugalsan.api.runnable.client.TGS_Runnable;
import java.time.Duration;
import java.util.Optional;

public class TS_ThreadStructRunnableTimed {

    private TS_ThreadStructRunnableTimed(Optional<Duration> max, Optional<TGS_Runnable> run) {
        this.max = max;
        this.run = run;
    }
    final public Optional<Duration> max;
    final public Optional<TGS_Runnable> run;

    public static TS_ThreadStructRunnableTimed of() {
        return new TS_ThreadStructRunnableTimed(Optional.empty(), Optional.empty());
    }

    public static TS_ThreadStructRunnableTimed of(TGS_Runnable run) {
        return new TS_ThreadStructRunnableTimed(Optional.empty(), Optional.of(run));
    }

    public static TS_ThreadStructRunnableTimed of(Duration max, TGS_Runnable run) {
        return new TS_ThreadStructRunnableTimed(Optional.of(max), Optional.of(run));
    }

    @Override
    public String toString() {
        return TS_ThreadStructRunnableTimed.class.getSimpleName() + "{" + "max=" + max + ", run=" + run + '}';
    }
}
