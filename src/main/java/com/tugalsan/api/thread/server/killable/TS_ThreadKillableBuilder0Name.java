package com.tugalsan.api.thread.server.killable;

public class TS_ThreadKillableBuilder0Name<T> {

    protected TS_ThreadKillableBuilder0Name(String name) {
        this.name = name;
    }
    final private String name;

    public <T> TS_ThreadKillableBuilder1Init<T> init(TS_ThreadKillableCallableTimed<T> init) {
        return new TS_ThreadKillableBuilder1Init(name, init);
    }
}
