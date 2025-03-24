package com.tugalsan.api.thread.server.sync.rateLimited;

import com.tugalsan.api.function.client.maythrowexceptions.unchecked.TGS_FuncMTU_OutTyped_In2;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.function.client.TGS_FuncUtils;

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

    public <R, A, B> TGS_UnionExcuse<R> call(TGS_FuncMTU_OutTyped_In2<R, A, B> call, A inputA, B inputB) {
        return callUntil(call, null, inputA, inputB);
    }

    public <R, A, B> TGS_UnionExcuse<R> callUntil(TGS_FuncMTU_OutTyped_In2<R, A, B> call, Duration timeout, A inputA, B inputB) {
        try {
            if (timeout == null) {
                lock.acquire();
            } else {
                if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return TGS_UnionExcuse.ofEmpty_NullPointerException();
                }
            }
            return TGS_UnionExcuse.of(call.call(inputA, inputB));
        } catch (InterruptedException ex) {
            return TGS_FuncUtils.throwIfInterruptedException(ex);
        } finally {
            lock.release();
        }
    }
}
