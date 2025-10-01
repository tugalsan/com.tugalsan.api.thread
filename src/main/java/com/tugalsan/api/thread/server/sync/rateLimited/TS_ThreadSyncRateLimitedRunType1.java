package com.tugalsan.api.thread.server.sync.rateLimited;

import module com.tugalsan.api.function;
import java.time.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class TS_ThreadSyncRateLimitedRunType1<A> {

    private TS_ThreadSyncRateLimitedRunType1(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static <A> TS_ThreadSyncRateLimitedRunType1<A> of(Semaphore lock) {
        return new TS_ThreadSyncRateLimitedRunType1(lock);
    }

    public static <A> TS_ThreadSyncRateLimitedRunType1<A> of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public <A> void run(TGS_FuncMTU_In1<A> run, A inputA) {
        runUntil(run, null, inputA);
    }

    public <A> void runUntil(TGS_FuncMTU_In1<A> run, Duration timeout, A inputA) {
        try {
            if (timeout == null) {
                lock.acquire();
            } else {
                if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return;
                }
            }
            run.run(inputA);
        } catch (InterruptedException ex) {
            TGS_FuncUtils.throwIfInterruptedException(ex);
        } finally {
            lock.release();
        }
    }
}
