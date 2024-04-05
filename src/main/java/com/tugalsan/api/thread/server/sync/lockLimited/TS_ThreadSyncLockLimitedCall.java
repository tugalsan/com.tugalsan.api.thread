package com.tugalsan.api.thread.server.sync.lockLimited;

import com.tugalsan.api.union.client.TGS_Union;
import com.tugalsan.api.union.server.TS_UnionUtils;
import java.time.Duration;
import java.util.concurrent.Callable;
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

    public TGS_Union<R> call(Callable<R> call) {
        return callUntil(call, null);
    }

    public TGS_Union<R> callUntil(Callable<R> call, Duration timeout) {
        try {
            if (timeout == null) {
                lock.lock();
            } else {
                if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return TGS_Union.ofEmpty();
                }
            }
        } catch (InterruptedException ex) {
            TS_UnionUtils.throwAsRuntimeExceptionIfInterruptedException(ex);
        }
        try {
            return TGS_Union.of(call.call());
        } catch (Exception ex) {
            TS_UnionUtils.throwAsRuntimeExceptionIfInterruptedException(ex);
            return TGS_Union.ofThrowable(ex);
        } finally {
            lock.unlock();
        }
    }
}
