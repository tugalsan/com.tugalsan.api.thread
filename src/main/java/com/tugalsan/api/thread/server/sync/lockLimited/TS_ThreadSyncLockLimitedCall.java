package com.tugalsan.api.thread.server.sync.lockLimited;

import com.tugalsan.api.callable.client.TGS_Callable;
import com.tugalsan.api.union.client.TGS_Union;
import com.tugalsan.api.union.server.TS_UnionUtils;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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

    public TGS_Union<R> call(TGS_Callable<R> call) {
        return callUntil(call, null);
    }

    public TGS_Union<R> callUntil(TGS_Callable<R> call, Duration timeout) {
        try {
            if (timeout == null) {
                lock.lock();
            } else {
                if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return TGS_Union.ofEmpty_NullPointerException();
                }
            }
            return TGS_Union.of(call.call());
        } catch (InterruptedException ex) {
            return TS_UnionUtils.throwAsRuntimeExceptionIfInterruptedException(ex);
        } finally {
            lock.unlock();
        }
    }
}
