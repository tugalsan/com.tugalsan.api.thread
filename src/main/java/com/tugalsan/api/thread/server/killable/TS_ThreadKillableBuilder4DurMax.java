package com.tugalsan.api.thread.server.killable;

import java.time.Duration;

public class TS_ThreadKillableBuilder4DurMax<T> {

    protected TS_ThreadKillableBuilder4DurMax(Class<T> clazz, String name, Duration durLag, Duration durMax) {
        this.clazz = clazz;
        this.name = name;
        this.durLag = durLag;
        this.durMax = durMax;
    }
    protected Class<T> clazz;
    protected String name;
    protected Duration durLag;
    protected Duration durMax;

    public TS_ThreadKillableBuilder5DurLoop<T> durLoopNone() {
        return durLoop(null);
    }

    public TS_ThreadKillableBuilder5DurLoop<T> durLoop(Duration durLoop) {
        return new TS_ThreadKillableBuilder5DurLoop(clazz, name, durLag, durMax, durLoop);
    }
}
