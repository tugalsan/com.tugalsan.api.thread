package com.tugalsan.api.thread.server.async.rateLimited;

import com.tugalsan.api.callable.client.TGS_Callable;
import com.tugalsan.api.union.client.TGS_Union;
import com.tugalsan.api.union.server.TS_UnionUtils;
import java.time.Duration;
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

    public TGS_Union<R> call(TGS_Callable<R> call) {
        return callUntil(call, null);
    }

    public TGS_Union<R> callUntil(TGS_Callable<R> call, Duration timeout) {
        try {
            if (timeout == null) {
                lock.acquire();
            } else {
                if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return TGS_Union.ofEmpty();
                }
            }
            return TGS_Union.of(call.call());
        } catch (InterruptedException ex) {
            return TS_UnionUtils.throwAsRuntimeExceptionIfInterruptedException(ex);
        } finally {
            lock.release();
        }
    }
}
