package com.tugalsan.api.thread.server.sync.lockLimited;

import module com.tugalsan.api.function;
import module com.tugalsan.api.union;
import java.time.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class TS_ThreadSyncLockLimitedCallType2<R, A, B> {

    private TS_ThreadSyncLockLimitedCallType2(ReentrantLock lock) {
        this.lock = lock;
    }
    final private ReentrantLock lock;

    public static <R, A, B> TS_ThreadSyncLockLimitedCallType2<R, A, B> of(ReentrantLock lock) {
        return new TS_ThreadSyncLockLimitedCallType2(lock);
    }

    public static <R, A, B> TS_ThreadSyncLockLimitedCallType2<R, A, B> of() {
        return of(new ReentrantLock());
    }

    public <R, A, B> TGS_UnionExcuse<R> call(TGS_FuncMTU_OutTyped_In2<R, A, B> call, A inputA, B inputB) {
        return callUntil(call, null, inputA, inputB);
    }

    public <R, A, B> TGS_UnionExcuse<R> callUntil(TGS_FuncMTU_OutTyped_In2<R, A, B> call, Duration timeout, A inputA, B inputB) {
        try {
            if (timeout == null) {
                lock.lock();
            } else {
                if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return TGS_UnionExcuse.ofEmpty_NullPointerException();
                }
            }
            return TGS_UnionExcuse.of(call.call(inputA, inputB));
        } catch (InterruptedException ex) {
            return TGS_FuncUtils.throwIfInterruptedException(ex);
        } finally {
            lock.unlock();
        }
    }
}
