package com.tugalsan.api.thread.server.sync.rateLimited;


import com.tugalsan.api.function.client.maythrowexceptions.unchecked.TGS_FuncMTU_In3;
import com.tugalsan.api.function.client.TGS_FuncUtils;

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

    public <A, B, C> void run(TGS_FuncMTU_In3<A, B, C> run, A inputA, B inputB, C inputC) {
        runUntil(run, null, inputA, inputB, inputC);
    }

    public <A, B, C> void runUntil(TGS_FuncMTU_In3<A, B, C> run, Duration timeout, A inputA, B inputB, C inputC) {
        try {
            if (timeout == null) {
                lock.acquire();
            } else {
                if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return;
                }
            }
            run.run(inputA, inputB, inputC);
        } catch (InterruptedException ex) {
            TGS_FuncUtils.throwIfInterruptedException(ex);
        } finally {
            lock.release();
        }
    }
}
