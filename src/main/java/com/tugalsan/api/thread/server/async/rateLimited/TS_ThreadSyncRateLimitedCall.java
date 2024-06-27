package com.tugalsan.api.thread.server.async.rateLimited;

import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import com.tugalsan.api.function.client.TGS_Func_OutTyped;

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

    public TGS_UnionExcuse<R> call(TGS_Func_OutTyped<R> call) {
        return callUntil(call, null);
    }

    public TGS_UnionExcuse<R> callUntil(TGS_Func_OutTyped<R> call, Duration timeout) {
        try {
            if (timeout == null) {
                lock.acquire();
            } else {
                if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return TGS_UnionExcuse.ofEmpty_NullPointerException();
                }
            }
            return TGS_UnionExcuse.of(call.call());
        } catch (InterruptedException ex) {
            return TGS_UnSafe.throwIfInterruptedException(ex);
        } finally {
            lock.release();
        }
    }
}
