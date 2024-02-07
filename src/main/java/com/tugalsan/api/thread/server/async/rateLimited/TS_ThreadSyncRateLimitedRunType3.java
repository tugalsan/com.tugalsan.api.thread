package com.tugalsan.api.thread.server.async.rateLimited;

import com.tugalsan.api.runnable.client.TGS_RunnableType3;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TS_ThreadSyncRateLimitedRunType3<A, B, C> {

    private TS_ThreadSyncRateLimitedRunType3(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static <A, B, C> TS_ThreadSyncRateLimitedRunType3<A, B, C> of(Semaphore lock) {
        return new TS_ThreadSyncRateLimitedRunType3(lock);
    }

    public static <A, B, C> TS_ThreadSyncRateLimitedRunType3<A, B, C> of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public void run(TGS_RunnableType3<A, B, C> run, A inputA, B inputB, C inputC) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryAcquire()) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA, inputB, inputC), ex -> TGS_UnSafe.thrw(ex), () -> lock.release());
        }, e -> TGS_StreamUtils.runNothing());
    }

    public void runUntil(TGS_RunnableType3<A, B, C> run, Duration timeout, A inputA, B inputB, C inputC) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA, inputB, inputC), ex -> TGS_UnSafe.thrw(ex), () -> lock.release());
        }, e -> TGS_StreamUtils.runNothing());
    }
}
