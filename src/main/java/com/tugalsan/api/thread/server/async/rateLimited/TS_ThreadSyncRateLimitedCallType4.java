package com.tugalsan.api.thread.server.async.rateLimited;

import com.tugalsan.api.callable.client.TGS_CallableType4;
import com.tugalsan.api.union.client.TGS_Union;
import com.tugalsan.api.union.client.TGS_UnionUtils;
import com.tugalsan.api.union.server.TS_UnionUtils;

import java.time.Duration;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TS_ThreadSyncRateLimitedCallType4<R, A, B, C, D> {

    private TS_ThreadSyncRateLimitedCallType4(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static <R, A, B, C, D> TS_ThreadSyncRateLimitedCallType4<R, A, B, C, D> of(Semaphore lock) {
        return new TS_ThreadSyncRateLimitedCallType4(lock);
    }

    public static <R, A, B, C, D> TS_ThreadSyncRateLimitedCallType4<R, A, B, C, D> of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public TGS_Union<R> call(TGS_CallableType4<R, A, B, C, D> call, A inputA, B inputB, C inputC, D inputD) {
        return callUntil(call, null, inputA, inputB, inputC, inputD);
    }

    public TGS_Union<R> callUntil(TGS_CallableType4<R, A, B, C, D> call, Duration timeout, A inputA, B inputB, C inputC, D inputD) {
        if (timeout == null) {
            if (!lock.tryAcquire()) {
                return TGS_Union.ofEmpty();
            }
        } else {
            try {
                if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return TGS_Union.ofEmpty();
                }
            } catch (InterruptedException ex) {
                TS_UnionUtils.throwAsRuntimeExceptionIfInterruptedException(ex);
            }
        }
        try {
            return TGS_Union.of(call.call(inputA, inputB, inputC, inputD));
        } catch (Exception ex) {
            TS_UnionUtils.throwAsRuntimeExceptionIfInterruptedException(ex);
            TGS_UnionUtils.throwAsRuntimeException(ex);
            return TGS_Union.ofEmpty();
        } finally {
            lock.release();
        }
    }
}
