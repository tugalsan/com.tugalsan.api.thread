package com.tugalsan.api.thread.server.sync.rateLimited;


import com.tugalsan.api.function.client.maythrowexceptions.unchecked.TGS_FuncMTU_In4;
import com.tugalsan.api.function.client.TGS_FuncUtils;

import java.time.Duration;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TS_ThreadSyncRateLimitedRunType4<A, B, C, D> {

    private TS_ThreadSyncRateLimitedRunType4(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static <A, B, C, D> TS_ThreadSyncRateLimitedRunType4<A, B, C, D> of(Semaphore lock) {
        return new TS_ThreadSyncRateLimitedRunType4(lock);
    }

    public static <A, B, C, D> TS_ThreadSyncRateLimitedRunType4<A, B, C, D> of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public <A, B, C, D> void run(TGS_FuncMTU_In4<A, B, C, D> run, A inputA, B inputB, C inputC, D inputD) {
        runUntil(run, null, inputA, inputB, inputC, inputD);
    }

    public <A, B, C, D> void runUntil(TGS_FuncMTU_In4<A, B, C, D> run, Duration timeout, A inputA, B inputB, C inputC, D inputD) {
        try {
            if (timeout == null) {
                lock.acquire();
            } else {
                if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return;
                }
            }
            run.run(inputA, inputB, inputC, inputD);
        } catch (InterruptedException ex) {
            TGS_FuncUtils.throwIfInterruptedException(ex);
        } finally {
            lock.release();
        }
    }
}
