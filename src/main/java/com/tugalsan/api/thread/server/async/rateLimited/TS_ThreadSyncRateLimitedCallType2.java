package com.tugalsan.api.thread.server.async.rateLimited;

import com.tugalsan.api.callable.client.TGS_CallableType2;
import com.tugalsan.api.union.client.TGS_Union;
import com.tugalsan.api.union.server.TS_UnionUtils;

import java.time.Duration;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TS_ThreadSyncRateLimitedCallType2<R, A, B> {

    private TS_ThreadSyncRateLimitedCallType2(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static <R, A, B> TS_ThreadSyncRateLimitedCallType2<R, A, B> of(Semaphore lock) {
        return new TS_ThreadSyncRateLimitedCallType2(lock);
    }

    public static <R, A, B> TS_ThreadSyncRateLimitedCallType2<R, A, B> of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public TGS_Union<R> call(TGS_CallableType2<R, A, B> call, A inputA, B inputB) {
        return callUntil(call, null, inputA, inputB);
    }

    public TGS_Union<R> callUntil(TGS_CallableType2<R, A, B> call, Duration timeout, A inputA, B inputB) {
        try {
            if (timeout == null) {
                lock.acquire();
            } else {
                if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return TGS_Union.ofEmpty();
                }
            }
        } catch (InterruptedException ex) {
            TS_UnionUtils.throwAsRuntimeExceptionIfInterruptedException(ex);
        }
        try {
            return TGS_Union.of(call.call(inputA, inputB));
        } catch (Exception ex) {
            TS_UnionUtils.throwAsRuntimeExceptionIfInterruptedException(ex);
            return TGS_Union.ofThrowable(ex);
        } finally {
            lock.release();
        }
    }
}
