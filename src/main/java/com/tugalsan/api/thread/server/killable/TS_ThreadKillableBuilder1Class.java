package com.tugalsan.api.thread.server.killable;

public class TS_ThreadKillableBuilder1Class<T> {

    protected TS_ThreadKillableBuilder1Class(Class clazz) {
        this.clazz = clazz;
    }
    protected Class<T> clazz;

    public TS_ThreadKillableBuilder2Name<T> name(String name) {
        return new TS_ThreadKillableBuilder2Name(clazz, name);
    }

}
