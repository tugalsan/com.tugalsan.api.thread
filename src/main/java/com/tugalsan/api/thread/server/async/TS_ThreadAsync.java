package com.tugalsan.api.thread.server.async;


import com.tugalsan.api.callable.client.TGS_CallableType1Void;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import java.time.Duration;

//USE TS_ThreadAsyncBuilder with killTrigger if possible
public class TS_ThreadAsync {

    public static Thread now(TS_ThreadSyncTrigger killTrigger, TGS_CallableType1Void<TS_ThreadSyncTrigger> exe) {
        return until(killTrigger, null, exe);
    }

    public static Thread until(TS_ThreadSyncTrigger killTrigger, Duration until, TGS_CallableType1Void<TS_ThreadSyncTrigger> exe) {
        if (until == null) {
            return Thread.startVirtualThread(() -> exe.run(killTrigger));
        } else {
            return now(killTrigger, kt2 -> TS_ThreadAsyncAwait.runUntil(killTrigger, until, kt1 -> exe.run(kt1)));
        }
    }

}
