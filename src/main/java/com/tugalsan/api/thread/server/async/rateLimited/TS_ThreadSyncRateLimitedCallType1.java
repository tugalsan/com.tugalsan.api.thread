package com.tugalsan.api.thread.server.async.rateLimited;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.union.client.TGS_Union;
import com.tugalsan.api.union.server.TS_UnionUtils;

import java.time.Duration;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TS_ThreadSyncRateLimitedCallType1<R, A> {

    private TS_ThreadSyncRateLimitedCallType1(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static <R, A> TS_ThreadSyncRateLimitedCallType1<R, A> of(Semaphore lock) {
        return new TS_ThreadSyncRateLimitedCallType1(lock);
    }

    public static <R, A> TS_ThreadSyncRateLimitedCallType1<R, A> of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public TGS_Union<R> call(TGS_CallableType1<R, A> call, A inputA) {
        return callUntil(call, null, inputA);
    }

    public TGS_Union<R> callUntil(TGS_CallableType1<R, A> call, Duration timeout, A inputA) {
        try {
            if (timeout == null) {
                lock.acquire();
            } else {
                if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return TGS_Union.ofEmpty_NullPointerException();
                }
            }
            return TGS_Union.of(call.call(inputA));
        } catch (InterruptedException ex) {
            return TS_UnionUtils.throwAsRuntimeExceptionIfInterruptedException(ex);
        } finally {
            lock.release();
        }
    }
}
