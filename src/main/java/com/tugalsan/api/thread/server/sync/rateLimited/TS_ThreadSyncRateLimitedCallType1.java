package com.tugalsan.api.thread.server.sync.rateLimited;

import com.tugalsan.api.function.client.TGS_Func_OutTyped_In1;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;

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

    public TGS_UnionExcuse<R> call(TGS_Func_OutTyped_In1<R, A> call, A inputA) {
        return callUntil(call, null, inputA);
    }

    public TGS_UnionExcuse<R> callUntil(TGS_Func_OutTyped_In1<R, A> call, Duration timeout, A inputA) {
        try {
            if (timeout == null) {
                lock.acquire();
            } else {
                if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return TGS_UnionExcuse.ofEmpty_NullPointerException();
                }
            }
            return TGS_UnionExcuse.of(call.call(inputA));
        } catch (InterruptedException ex) {
            return TGS_UnSafe.throwIfInterruptedException(ex);
        } finally {
            lock.release();
        }
    }
}
