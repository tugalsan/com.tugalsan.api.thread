package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.callable.client.TGS_Callable;
import java.time.Duration;

public class TS_ThreadKillableBuilder5DurLoop<T> {

    protected TS_ThreadKillableBuilder5DurLoop(Class<T> clazz, String name, Duration durLag, Duration durMax, Duration durLoop) {
        this.clazz = clazz;
        this.name = name;
        this.durLag = durLag;
        this.durMax = durMax;
        this.durLoop = durLoop;
    }
    protected Class<T> clazz;
    protected String name;
    protected Duration durLag;
    protected Duration durMax;
    protected Duration durLoop;

    public TS_ThreadKillableBuilder6RunInit<T> runInitNone() {
        return runInit(null);
    }

    public TS_ThreadKillableBuilder6RunInit<T> runInit(TGS_Callable<T> runInit) {
        return new TS_ThreadKillableBuilder6RunInit(clazz, name, durLag, durMax, durLoop, runInit);
    }
}
