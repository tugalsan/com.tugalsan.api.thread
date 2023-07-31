package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.callable.client.TGS_Callable;
import java.time.Duration;

public class TS_ThreadKillableBuilder5DurLoop<T> {

    protected TS_ThreadKillableBuilder5DurLoop(Class<T> clazz, String name, Duration durLag, Duration durMainMax, Duration durLoop) {
        this.clazz = clazz;
        this.name = name;
        this.durLag = durLag;
        this.durMainMax = durMainMax;
        this.durLoop = durLoop;
    }
    protected Class<T> clazz;
    protected String name;
    protected Duration durLag;
    protected Duration durMainMax;
    protected Duration durLoop;

    public TS_ThreadKillableBuilder6RunInit<T> runInitNone() {
        return runInit(null);
    }

    public TS_ThreadKillableBuilder6RunInit<T> runInit(TGS_Callable<T> runInit) {
        return new TS_ThreadKillableBuilder6RunInit(clazz, name, durLag, durMainMax, durLoop, runInit);
    }
}
