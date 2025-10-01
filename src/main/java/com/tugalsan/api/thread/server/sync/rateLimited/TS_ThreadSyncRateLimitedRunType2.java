package com.tugalsan.api.thread.server.sync.rateLimited;

import module com.tugalsan.api.function;
import java.time.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

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
