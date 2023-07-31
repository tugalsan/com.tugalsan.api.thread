package com.tugalsan.api.thread.server.killable;

import java.time.Duration;

public class TS_ThreadKillableBuilder4durMainMax<T> {

    protected TS_ThreadKillableBuilder4durMainMax(Class<T> clazz, String name, Duration durLag, Duration durMainMax) {
        this.clazz = clazz;
        this.name = name;
        this.durLag = durLag;
        this.durMainMax = durMainMax;
    }
    protected Class<T> clazz;
    protected String name;
    protected Duration durLag;
    protected Duration durMainMax;

    public TS_ThreadKillableBuilder5DurLoop<T> durLoopNone() {
        return durLoop(null);
    }

    public TS_ThreadKillableBuilder5DurLoop<T> durLoop(Duration durLoop) {
        return new TS_ThreadKillableBuilder5DurLoop(clazz, name, durLag, durMainMax, durLoop);
    }
}
