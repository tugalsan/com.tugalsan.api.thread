package com.tugalsan.api.thread.server.sync.lockLimited;

import module com.tugalsan.api.function;
import module com.tugalsan.api.union;
import java.time.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class TS_ThreadSyncLockLimitedCallType1<R, A> {

    private TS_ThreadSyncLockLimitedCallType1(ReentrantLock lock) {
        this.lock = lock;
    }
    final private ReentrantLock lock;

    public static <R, A> TS_ThreadSyncLockLimitedCallType1<R, A> of(ReentrantLock lock) {
        return new TS_ThreadSyncLockLimitedCallType1(lock);
    }

    public static <R, A> TS_ThreadSyncLockLimitedCallType1<R, A> of() {
        return of(new ReentrantLock());
    }

    public <R, A> TGS_UnionExcuse<R> call(TGS_FuncMTU_OutTyped_In1<R, A> call, A inputA) {
        return callUntil(call, null, inputA);
    }

    public <R, A> TGS_UnionExcuse<R> callUntil(TGS_FuncMTU_OutTyped_In1<R, A> call, Duration timeout, A inputA) {
        try {
            if (timeout == null) {
                lock.lock();
            } else {
                if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return TGS_UnionExcuse.ofEmpty_NullPointerException();
                }
            }
            return TGS_UnionExcuse.of(call.call(inputA));
        } catch (InterruptedException ex) {
            return TGS_FuncUtils.throwIfInterruptedException(ex);
        } finally {
            lock.unlock();
        }
    }
}
