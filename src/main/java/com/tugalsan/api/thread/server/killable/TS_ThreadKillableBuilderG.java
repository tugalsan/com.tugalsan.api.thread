package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.callable.client.TGS_Callable;
import java.time.Duration;

public class TS_ThreadKillableBuilderG<T> {

    protected TS_ThreadKillableBuilderG(Class<T> clazz, String name, Duration durLag, Duration durMax, Duration durLoop, TGS_Callable<T> runInit) {
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

    public TS_ThreadKillableBuilderG<T> durMaxNone() {
        return durMax(null);
    }

    public TS_ThreadKillableBuilderG<T> durMax(Duration durMax) {
        return new TS_ThreadKillableBuilderG(clazz, name, durLag, durMax);
    }
}
