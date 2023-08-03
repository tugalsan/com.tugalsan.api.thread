package com.tugalsan.api.thread.server.async;

import com.tugalsan.api.runnable.client.*;
import com.tugalsan.api.thread.server.safe.TS_ThreadSafeTrigger;
import java.time.Duration;
import java.util.concurrent.*;

//USE TS_ThreadStructBuilder with killTrigger if possible
public class TS_ThreadAsync {

    public static Thread now(TS_ThreadSafeTrigger killTrigger, TGS_RunnableType1<TS_ThreadSafeTrigger> exe) {
        return until(killTrigger, null, exe);
    }

    public static Thread until(TS_ThreadSafeTrigger killTrigger, Duration until, TGS_RunnableType1<TS_ThreadSafeTrigger> exe) {
        if (until == null) {
            return Thread.startVirtualThread(() -> exe.run(killTrigger));
        } else {
            return now(killTrigger, kt2 -> TS_ThreadAsyncAwait.runUntil(kt2, until, kt1 -> exe.run(kt1)));
        }
    }

}
