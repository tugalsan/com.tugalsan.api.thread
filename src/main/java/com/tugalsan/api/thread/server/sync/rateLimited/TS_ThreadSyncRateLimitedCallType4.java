package com.tugalsan.api.thread.server.sync.rateLimited;

import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCE_OutTyped_In4;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.function.client.TGS_FuncUtils;

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

    public <R, A, B, C, D> TGS_UnionExcuse<R> call(TGS_FuncMTUCE_OutTyped_In4<R, A, B, C, D> call, A inputA, B inputB, C inputC, D inputD) {
        return callUntil(call, null, inputA, inputB, inputC, inputD);
    }

    public <R, A, B, C, D> TGS_UnionExcuse<R> callUntil(TGS_FuncMTUCE_OutTyped_In4<R, A, B, C, D> call, Duration timeout, A inputA, B inputB, C inputC, D inputD) {
        try {
            if (timeout == null) {
                lock.acquire();
            } else {
                if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return TGS_UnionExcuse.ofEmpty_NullPointerException();
                }
            }
            return TGS_UnionExcuse.of(call.call(inputA, inputB, inputC, inputD));
        } catch (InterruptedException ex) {
            return TGS_FuncUtils.throwIfInterruptedException(ex);
        } finally {
            lock.release();
        }
    }
}
