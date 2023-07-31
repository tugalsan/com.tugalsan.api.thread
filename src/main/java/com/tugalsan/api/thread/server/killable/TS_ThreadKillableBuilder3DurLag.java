package com.tugalsan.api.thread.server.killable;

import java.time.Duration;

public class TS_ThreadKillableBuilder3DurLag<T> {

    protected TS_ThreadKillableBuilder3DurLag(Class<T> clazz, String name, Duration durLag) {
        this.clazz = clazz;
        this.name = name;
        this.durLag = durLag;
    }
    protected Class<T> clazz;
    protected String name;
    protected Duration durLag;

    public TS_ThreadKillableBuilder4durMainMax<T> durMainMaxNone() {
        return durMainMax(null);
    }

    public TS_ThreadKillableBuilder4durMainMax<T> durMainMax(Duration durMainMax) {
        return new TS_ThreadKillableBuilder4durMainMax(clazz, name, durLag, durMainMax);
    }

}
