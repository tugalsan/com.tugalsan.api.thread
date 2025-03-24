package com.tugalsan.api.thread.server.sync.rateLimited;


import com.tugalsan.api.function.client.maythrowexceptions.unchecked.TGS_FuncMTU_In2;
import com.tugalsan.api.function.client.TGS_FuncUtils;

import java.time.Duration;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TS_ThreadSyncRateLimitedRunType2<A, B> {

    private TS_ThreadSyncRateLimitedRunType2(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static <A, B> TS_ThreadSyncRateLimitedRunType2<A, B> of(Semaphore lock) {
        return new TS_ThreadSyncRateLimitedRunType2(lock);
    }

    public static <A, B> TS_ThreadSyncRateLimitedRunType2<A, B> of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public <A, B> void run(TGS_FuncMTU_In2<A, B> run, A inputA, B inputB) {
        runUntil(run, null, inputA, inputB);
    }

    public <A, B> void runUntil(TGS_FuncMTU_In2<A, B> run, Duration timeout, A inputA, B inputB) {
        try {
            if (timeout == null) {
                lock.acquire();
            } else {
                if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return;
                }
            }
            run.run(inputA, inputB);
        } catch (InterruptedException ex) {
            TGS_FuncUtils.throwIfInterruptedException(ex);
        } finally {
            lock.release();
        }
    }
}
