package com.tugalsan.api.thread.server.async.rateLimited;

import com.tugalsan.api.union.client.TGS_Union;
import com.tugalsan.api.union.client.TGS_UnionUtils;
import com.tugalsan.api.union.server.TS_UnionUtils;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TS_ThreadSyncRateLimitedCall<R> {

    private TS_ThreadSyncRateLimitedCall(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static <R> TS_ThreadSyncRateLimitedCall<R> of(Semaphore lock) {
        return new TS_ThreadSyncRateLimitedCall(lock);
    }

    public static <R> TS_ThreadSyncRateLimitedCall<R> of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public TGS_Union<R> call(Callable<R> call) {
        return callUntil(call, null);
    }

    public TGS_Union<R> callUntil(Callable<R> call, Duration timeout) {
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
            return TGS_Union.of(call.call());
        } catch (Exception ex) {
            TS_UnionUtils.throwAsRuntimeExceptionIfInterruptedException(ex);
            TGS_UnionUtils.throwAsRuntimeException(ex);
            return TGS_Union.ofEmpty();
        } finally {
            lock.release();
        }
    }
}
