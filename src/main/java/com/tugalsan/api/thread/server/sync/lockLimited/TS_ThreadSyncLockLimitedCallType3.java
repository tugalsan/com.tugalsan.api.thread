package com.tugalsan.api.thread.server.sync.lockLimited;

import com.tugalsan.api.callable.client.TGS_CallableType3;
import com.tugalsan.api.union.client.TGS_Union;
import com.tugalsan.api.union.server.TS_UnionUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TS_ThreadSyncLockLimitedCallType3<R, A, B, C> {

    private TS_ThreadSyncLockLimitedCallType3(ReentrantLock lock) {
        this.lock = lock;
    }
    final private ReentrantLock lock;

    public static <R, A, B, C> TS_ThreadSyncLockLimitedCallType3<R, A, B, C> of(ReentrantLock lock) {
        return new TS_ThreadSyncLockLimitedCallType3(lock);
    }

    public static <R, A, B, C> TS_ThreadSyncLockLimitedCallType3<R, A, B, C> of() {
        return of(new ReentrantLock());
    }

    public TGS_Union<R> call(TGS_CallableType3<R, A, B, C> call, A inputA, B inputB, C inputC) {
        return callUntil(call, null, inputA, inputB, inputC);
    }

    public TGS_Union<R> callUntil(TGS_CallableType3<R, A, B, C> call, Duration timeout, A inputA, B inputB, C inputC) {
        try {
            if (timeout == null) {
                lock.lock();
            } else {
                if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return TGS_Union.ofEmpty();
                }
            }
            return TGS_Union.of(call.call(inputA, inputB, inputC));
        } catch (InterruptedException ex) {
            return TS_UnionUtils.throwAsRuntimeExceptionIfInterruptedException(ex);
        } finally {
            lock.unlock();
        }
    }
}
