package com.tugalsan.api.thread.server.sync.lockLimited;

import com.tugalsan.api.callable.client.TGS_CallableType2;
import com.tugalsan.api.union.client.TGS_Union;
import com.tugalsan.api.union.server.TS_UnionUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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

    public TGS_Union<R> call(TGS_CallableType2<R, A, B> call, A inputA, B inputB) {
        return callUntil(call, null, inputA, inputB);
    }

    public TGS_Union<R> callUntil(TGS_CallableType2<R, A, B> call, Duration timeout, A inputA, B inputB) {
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
            return TGS_Union.of(call.call(inputA, inputB));
        } catch (Exception ex) {
            TS_UnionUtils.throwAsRuntimeExceptionIfInterruptedException(ex);
            return TGS_Union.ofThrowable(ex);
        } finally {
            lock.unlock();
        }
    }
}
