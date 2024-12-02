package com.tugalsan.api.thread.server.sync.lockLimited;

import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import com.tugalsan.api.function.client.TGS_Func_OutTyped;

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

    public <R> TGS_UnionExcuse<R> call(TGS_Func_OutTyped<R> call) {
        return callUntil(call, null);
    }

    public <R> TGS_UnionExcuse<R> callUntil(TGS_Func_OutTyped<R> call, Duration timeout) {
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
            return TGS_UnSafe.throwIfInterruptedException(ex);
        } finally {
            lock.unlock();
        }
    }
}
