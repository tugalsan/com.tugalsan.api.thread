package com.tugalsan.api.thread.server.sync.lockLimited;


import com.tugalsan.api.union.client.TGS_Union;
import java.time.Duration;
import java.util.Optional;
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
        return TGS_UnSafe.call(() -> {
            if (!lock.tryLock()) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call()),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.unlock());
        }, e -> Optional.empty());
    }

    public TGS_Union<R> callUntil(Callable<R> call, Duration timeout) {
        return TGS_UnSafe.call(() -> {
            if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call()),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.unlock());
        }, e -> Optional.empty());
    }
}