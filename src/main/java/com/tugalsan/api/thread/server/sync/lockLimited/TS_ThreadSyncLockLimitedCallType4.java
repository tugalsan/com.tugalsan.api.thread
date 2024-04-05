package com.tugalsan.api.thread.server.sync.lockLimited;

import com.tugalsan.api.callable.client.TGS_CallableType4;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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

    public Optional<R> call(TGS_CallableType4<R, A, B, C, D> call, A inputA, B inputB, C inputC, D inputD) {
        return TGS_UnSafe.call(() -> {
            if (!lock.tryLock()) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call(inputA, inputB, inputC, inputD)),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.unlock());
        }, e -> Optional.empty());
    }

    public Optional<R> callUntil(TGS_CallableType4<R, A, B, C, D> call, Duration timeout, A inputA, B inputB, C inputC, D inputD) {
        return TGS_UnSafe.call(() -> {
            if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call(inputA, inputB, inputC, inputD)),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.unlock());
        }, e -> Optional.empty());
    }
}
