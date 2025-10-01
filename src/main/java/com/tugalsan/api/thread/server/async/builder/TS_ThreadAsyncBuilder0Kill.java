package com.tugalsan.api.thread.server.async.builder;

import module com.tugalsan.api.thread;

public class TS_ThreadAsyncBuilder0Kill<T> {

    protected TS_ThreadAsyncBuilder0Kill(TS_ThreadSyncTrigger killTrigger) {
        this.killTrigger = killTrigger;
    }
    final private TS_ThreadSyncTrigger killTrigger;

    public TS_ThreadAsyncBuilder1Name<T> name(String name) {
        return new TS_ThreadAsyncBuilder1Name(killTrigger, name);
    }
}
