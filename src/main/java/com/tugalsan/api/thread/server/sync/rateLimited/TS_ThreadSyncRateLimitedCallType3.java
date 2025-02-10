package com.tugalsan.api.thread.server.sync.rateLimited;

import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCE_OutTyped_In3;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.function.client.TGS_FuncUtils;

import java.time.Duration;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TS_ThreadSyncRateLimitedCallType3<R, A, B, C> {

    private TS_ThreadSyncRateLimitedCallType3(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static <R, A, B, C> TS_ThreadSyncRateLimitedCallType3<R, A, B, C> of(Semaphore lock) {
        return new TS_ThreadSyncRateLimitedCallType3(lock);
    }

    public static <R, A, B, C> TS_ThreadSyncRateLimitedCallType3<R, A, B, C> of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public <R, A, B, C> TGS_UnionExcuse<R> call(TGS_FuncMTUCE_OutTyped_In3<R, A, B, C> call, A inputA, B inputB, C inputC) {
        return callUntil(call, null, inputA, inputB, inputC);
    }

    public <R, A, B, C> TGS_UnionExcuse<R> callUntil(TGS_FuncMTUCE_OutTyped_In3<R, A, B, C> call, Duration timeout, A inputA, B inputB, C inputC) {
        try {
            if (timeout == null) {
                lock.acquire();
            } else {
                if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return TGS_UnionExcuse.ofEmpty_NullPointerException();
                }
            }
            return TGS_UnionExcuse.of(call.call(inputA, inputB, inputC));
        } catch (InterruptedException ex) {
            return TGS_FuncUtils.throwIfInterruptedException(ex);
        } finally {
            lock.release();
        }
    }
}
