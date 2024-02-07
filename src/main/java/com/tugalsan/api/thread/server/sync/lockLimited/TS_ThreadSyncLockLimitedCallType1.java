package com.tugalsan.api.thread.server.sync.lockLimited;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.Optional;
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

    public Optional<R> call(TGS_CallableType1<R, A> call, A inputA) {
        return TGS_UnSafe.call(() -> {
            if (!lock.tryLock()) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call(inputA)),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.unlock());
        }, e -> Optional.empty());
    }

    public Optional<R> callUntil(TGS_CallableType1<R, A> call, Duration timeout, A inputA) {
        return TGS_UnSafe.call(() -> {
            if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call(inputA)),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.unlock());
        }, e -> Optional.empty());
    }
}
