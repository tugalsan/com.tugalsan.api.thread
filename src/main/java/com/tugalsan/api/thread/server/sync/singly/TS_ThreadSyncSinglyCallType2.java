package com.tugalsan.api.thread.server.sync.singly;

import com.tugalsan.api.callable.client.TGS_CallableType2;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TS_ThreadSyncSinglyCallType2<R, A, B> {

    private TS_ThreadSyncSinglyCallType2(ReentrantLock lock) {
        this.lock = lock;
    }
    final private ReentrantLock lock;

    public static <R, A, B> TS_ThreadSyncSinglyCallType2<R, A, B> of(ReentrantLock lock) {
        return new TS_ThreadSyncSinglyCallType2(lock);
    }

    public static <R, A, B> TS_ThreadSyncSinglyCallType2<R, A, B> of() {
        return of(new ReentrantLock());
    }

    public Optional<R> call(TGS_CallableType2<R, A, B> call, A inputA, B inputB) {
        return TGS_UnSafe.call(() -> {
            if (!lock.tryLock()) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call(inputA, inputB)),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.unlock());
        }, e -> Optional.empty());
    }

    public Optional<R> callUntil(TGS_CallableType2<R, A, B> call, Duration timeout, A inputA, B inputB) {
        return TGS_UnSafe.call(() -> {
            if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call(inputA, inputB)),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.unlock());
        }, e -> Optional.empty());
    }
}
