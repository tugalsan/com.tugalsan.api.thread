package com.tugalsan.api.thread.server.sync.lockLimited;

import module com.tugalsan.api.function;
import module com.tugalsan.api.union;
import java.time.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class TS_ThreadSyncLockLimitedCallType4<R, A, B, C, D> {

    private TS_ThreadSyncLockLimitedCallType4(ReentrantLock lock) {
        this.lock = lock;
    }
    final private ReentrantLock lock;

    public static <R, A, B, C, D> TS_ThreadSyncLockLimitedCallType4<R, A, B, C, D> of(ReentrantLock lock) {
        return new TS_ThreadSyncLockLimitedCallType4(lock);
    }

    public static <R, A, B, C, D> TS_ThreadSyncLockLimitedCallType4<R, A, B, C, D> of() {
        return of(new ReentrantLock());
    }

    public <R, A, B, C, D> TGS_UnionExcuse<R> call(TGS_FuncMTU_OutTyped_In4<R, A, B, C, D> call, A inputA, B inputB, C inputC, D inputD) {
        return callUntil(call, null, inputA, inputB, inputC, inputD);
    }

    public <R, A, B, C, D> TGS_UnionExcuse<R> callUntil(TGS_FuncMTU_OutTyped_In4<R, A, B, C, D> call, Duration timeout, A inputA, B inputB, C inputC, D inputD) {
        try {
            if (timeout == null) {
                lock.lock();
            } else {
                if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return TGS_UnionExcuse.ofEmpty_NullPointerException();
                }
            }
            return TGS_UnionExcuse.of(call.call(inputA, inputB, inputC, inputD));
        } catch (InterruptedException ex) {
            return TGS_FuncUtils.throwIfInterruptedException(ex);
        } finally {
            lock.unlock();
        }
    }
}
