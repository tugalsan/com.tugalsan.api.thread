package com.tugalsan.api.thread.server.async.run;

import com.tugalsan.api.thread.server.async.await.TS_ThreadAsyncAwait;
import com.tugalsan.api.function.client.maythrowexceptions.unchecked.TGS_FuncMTU_In1;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import java.time.Duration;

//USE TS_ThreadAsyncBuilder with killTrigger if possible
public class TS_ThreadAsyncRun {

    private TS_ThreadAsyncRun() {

    }

    final private static TS_Log d = TS_Log.of(false, TS_ThreadAsyncRun.class);

    public static Thread now(TS_ThreadSyncTrigger killTrigger, TGS_FuncMTU_In1<TS_ThreadSyncTrigger> exe) {
        return Thread.startVirtualThread(() -> exe.run(killTrigger));
    }

    public static Thread until(TS_ThreadSyncTrigger killTrigger, Duration until, TGS_FuncMTU_In1<TS_ThreadSyncTrigger> exe) {
        if (until == null) {
            return now(killTrigger, exe);
        }
        return Thread.startVirtualThread(() -> {
            TS_ThreadAsyncAwait.runUntil(killTrigger, until, kt_wt -> exe.run(kt_wt));
        });
    }
}
