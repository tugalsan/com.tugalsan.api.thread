package com.tugalsan.api.thread.server.sync.lockLimited;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.union.client.TGS_Union;
import com.tugalsan.api.union.server.TS_UnionUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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

    public TGS_Union<R> call(TGS_CallableType1<R, A> call, A inputA) {
        return callUntil(call, null, inputA);
    }

    public TGS_Union<R> callUntil(TGS_CallableType1<R, A> call, Duration timeout, A inputA) {
        try {
            if (timeout == null) {
                lock.lock();
            } else {
                if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return TGS_Union.ofEmpty_NullPointerException();
                }
            }
            return TGS_Union.of(call.call(inputA));
        } catch (InterruptedException ex) {
            return TS_UnionUtils.throwAsRuntimeExceptionIfInterruptedException(ex);
        } finally {
            lock.unlock();
        }
    }
}
