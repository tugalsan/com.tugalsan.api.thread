package com.tugalsan.api.thread.server.sync.singly;

import com.tugalsan.api.callable.client.TGS_CallableType3;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TS_ThreadSyncSinglyCallType3<R, A, B, C> {

    private TS_ThreadSyncSinglyCallType3(ReentrantLock lock) {
        this.lock = lock;
    }
    final private ReentrantLock lock;

    public static <R, A, B, C> TS_ThreadSyncSinglyCallType3<R, A, B, C> of(ReentrantLock lock) {
        return new TS_ThreadSyncSinglyCallType3(lock);
    }

    public static <R, A, B, C> TS_ThreadSyncSinglyCallType3<R, A, B, C> of() {
        return of(new ReentrantLock());
    }

    public Optional<R> call(TGS_CallableType3<R, A, B, C> call, A inputA, B inputB, C inputC) {
        return TGS_UnSafe.call(() -> {
            if (!lock.tryLock()) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call(inputA, inputB, inputC)),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.unlock());
        }, e -> Optional.empty());
    }

    public Optional<R> callUntil(TGS_CallableType3<R, A, B, C> call, Duration timeout, A inputA, B inputB, C inputC) {
        return TGS_UnSafe.call(() -> {
            if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call(inputA, inputB, inputC)),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.unlock());
        }, e -> Optional.empty());
    }
}
