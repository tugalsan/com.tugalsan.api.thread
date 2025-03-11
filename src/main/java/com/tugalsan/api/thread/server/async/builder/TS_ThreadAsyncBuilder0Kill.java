package com.tugalsan.api.thread.server.async.builder;

import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.log.server.TS_Log;

public class TS_ThreadAsyncBuilder0Kill<T> {

    final private static TS_Log d = TS_Log.of(TS_ThreadAsyncBuilder0Kill.class);

    protected TS_ThreadAsyncBuilder0Kill(TS_ThreadSyncTrigger killTrigger) {
        this.killTrigger = killTrigger;
    }
    final private TS_ThreadSyncTrigger killTrigger;

    public TS_ThreadAsyncBuilder1Name<T> name(String name) {
        return new TS_ThreadAsyncBuilder1Name(killTrigger, name);
    }
}
