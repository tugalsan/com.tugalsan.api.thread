package com.tugalsan.api.thread.server.sync.lockLimited;

import module com.tugalsan.api.function;
import module com.tugalsan.api.union;
import java.time.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class TS_ThreadSyncLockLimitedCall<R> {

    private TS_ThreadSyncLockLimitedCall(ReentrantLock lock) {
        this.lock = lock;
    }
    final private ReentrantLock lock;

    public static <R> TS_ThreadSyncLockLimitedCall<R> of(ReentrantLock lock) {
        return new TS_ThreadSyncLockLimitedCall(lock);
    }

    public static <R> TS_ThreadSyncLockLimitedCall<R> of() {
        return of(new ReentrantLock());
    }

    public <R> TGS_UnionExcuse<R> call(TGS_FuncMTU_OutTyped<R> call) {
        return callUntil(call, null);
    }

    public <R> TGS_UnionExcuse<R> callUntil(TGS_FuncMTU_OutTyped<R> call, Duration timeout) {
        try {
            if (timeout == null) {
                lock.lock();
            } else {
                if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return TGS_UnionExcuse.ofEmpty_NullPointerException();
                }
            }
            return TGS_UnionExcuse.of(call.call());
        } catch (InterruptedException ex) {
            return TGS_FuncUtils.throwIfInterruptedException(ex);
        } finally {
            lock.unlock();
        }
    }
}
