package com.tugalsan.api.thread.server.sync.rateLimited;

import module com.tugalsan.api.function;
import module com.tugalsan.api.union;
import java.time.*;
import java.util.concurrent.*;

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

    public <R> TGS_UnionExcuse<R> call(TGS_FuncMTU_OutTyped<R> call) {
        return callUntil(call, null);
    }

    public <R> TGS_UnionExcuse<R> callUntil(TGS_FuncMTU_OutTyped<R> call, Duration timeout) {
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
            return TGS_FuncUtils.throwIfInterruptedException(ex);
        } finally {
            lock.release();
        }
    }
}
