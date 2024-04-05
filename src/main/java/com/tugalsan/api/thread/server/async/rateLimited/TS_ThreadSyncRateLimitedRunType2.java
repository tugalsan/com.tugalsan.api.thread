package com.tugalsan.api.thread.server.async.rateLimited;

import com.tugalsan.api.runnable.client.TGS_RunnableType2;
import com.tugalsan.api.union.client.TGS_UnionUtils;
import com.tugalsan.api.union.server.TS_UnionUtils;

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

    public void run(TGS_RunnableType2<A, B> run, A inputA, B inputB) {
        runUntil(run, null, inputA, inputB);
    }

    public void runUntil(TGS_RunnableType2<A, B> run, Duration timeout, A inputA, B inputB) {
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
            run.run(inputA, inputB);
        } catch (Exception ex) {
            TS_UnionUtils.throwAsRuntimeExceptionIfInterruptedException(ex);
            TGS_UnionUtils.throwAsRuntimeException(ex);
        } finally {
            lock.release();
        }
    }
}
