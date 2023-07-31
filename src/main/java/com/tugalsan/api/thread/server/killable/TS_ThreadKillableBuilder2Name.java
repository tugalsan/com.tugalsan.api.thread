package com.tugalsan.api.thread.server.killable;

import java.time.Duration;

public class TS_ThreadKillableBuilder2Name<T> {

    protected TS_ThreadKillableBuilder2Name(Class<T> clazz, String name) {
        this.clazz = clazz;
        this.name = name;
    }
    protected Class<T> clazz;
    protected String name;

    public TS_ThreadKillableBuilder3DurLag<T> durLagNone() {
        return durLag(null);
    }

    public TS_ThreadKillableBuilder3DurLag<T> durLag(Duration durLag) {
        return new TS_ThreadKillableBuilder3DurLag(clazz, name, durLag);
    }
}
