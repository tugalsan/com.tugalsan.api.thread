package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.runnable.client.TGS_Runnable;
import java.time.Duration;
import java.util.Optional;

public class TS_ThreadKillableRunnableTimed {

    private TS_ThreadKillableRunnableTimed(Optional<Duration> max, Optional<TGS_Runnable> run) {
        this.max = max;
        this.run = run;
    }
    final public Optional<Duration> max;
    final public Optional<TGS_Runnable> run;

    public static TS_ThreadKillableRunnableTimed of() {
        return new TS_ThreadKillableRunnableTimed(Optional.empty(), Optional.empty());
    }

    public static TS_ThreadKillableRunnableTimed of(TGS_Runnable run) {
        return new TS_ThreadKillableRunnableTimed(Optional.empty(), Optional.of(run));
    }

    public static TS_ThreadKillableRunnableTimed of(Duration max, TGS_Runnable run) {
        return new TS_ThreadKillableRunnableTimed(Optional.of(max), Optional.of(run));
    }

    @Override
    public String toString() {
        return TS_ThreadKillableRunnableTimed.class.getSimpleName() + "{" + "max=" + max + ", run=" + run + '}';
    }
}
