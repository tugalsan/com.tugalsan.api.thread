package com.tugalsan.api.thread.server.async.run;

import com.tugalsan.api.thread.server.async.await.TS_ThreadAsyncAwait;
import com.tugalsan.api.function.client.TGS_Func_In1;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import java.time.Duration;

//USE TS_ThreadAsyncBuilder with killTrigger if possible
public class TS_ThreadAsyncRun {

    public static Thread now(TS_ThreadSyncTrigger killTrigger, TGS_Func_In1<TS_ThreadSyncTrigger> exe) {
        return Thread.startVirtualThread(() -> exe.run(killTrigger));
    }

    public static Thread until(TS_ThreadSyncTrigger killTrigger, Duration until, TGS_Func_In1<TS_ThreadSyncTrigger> exe) {
        if (until == null) {
            return now(killTrigger, exe);
        }
        return Thread.startVirtualThread(() -> TS_ThreadAsyncAwait.runUntil(killTrigger, until, kt1 -> exe.run(kt1)));
    }
}
