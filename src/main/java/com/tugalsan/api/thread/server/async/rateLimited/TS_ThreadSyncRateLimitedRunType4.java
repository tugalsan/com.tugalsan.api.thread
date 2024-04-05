package com.tugalsan.api.thread.server.async.rateLimited;

import com.tugalsan.api.runnable.client.TGS_RunnableType4;
import com.tugalsan.api.union.client.TGS_UnionUtils;
import com.tugalsan.api.union.server.TS_UnionUtils;

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

    public void run(TGS_RunnableType4<A, B, C, D> run, A inputA, B inputB, C inputC, D inputD) {
        runUntil(run, null, inputA, inputB, inputC, inputD);
    }

    public void runUntil(TGS_RunnableType4<A, B, C, D> run, Duration timeout, A inputA, B inputB, C inputC, D inputD) {
        try {
            if (timeout == null) {
                lock.acquire();
            } else {
                if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return;
                }
            }
        } catch (InterruptedException ex) {
            TS_UnionUtils.throwAsRuntimeExceptionIfInterruptedException(ex);
        }
        try {
            run.run(inputA, inputB, inputC, inputD);
        } catch (Exception ex) {
            TS_UnionUtils.throwAsRuntimeExceptionIfInterruptedException(ex);
            TGS_UnionUtils.throwAsRuntimeException(ex);
        } finally {
            lock.release();
        }
    }
}
