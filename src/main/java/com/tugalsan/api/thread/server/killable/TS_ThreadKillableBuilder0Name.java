package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.callable.client.TGS_Callable;
import java.time.Duration;

public class TS_ThreadKillableBuilder0Name<T> {

    protected TS_ThreadKillableBuilder0Name(String name) {
        this.name = name;
    }
    final private String name;

    public TS_ThreadKillableBuilder1Init<Object> initEmpty() {
        return new TS_ThreadKillableBuilder1Init(name, TS_ThreadKillableCallableTimed.of());
    }

    public <T> TS_ThreadKillableBuilder1Init<T> init(TGS_Callable<T> call) {
        return new TS_ThreadKillableBuilder1Init(name, TS_ThreadKillableCallableTimed.of(call));
    }

    public <T> TS_ThreadKillableBuilder1Init<T> initTimed(Duration max, TGS_Callable<T> call) {
        return new TS_ThreadKillableBuilder1Init(name, TS_ThreadKillableCallableTimed.of(max, call));
    }

}
