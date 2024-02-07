package com.tugalsan.api.thread.server.sync.simultaneously;

import com.tugalsan.api.callable.client.TGS_CallableType2;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TS_ThreadSyncSimultaneouslyCallType2<R, A, B> {

    private TS_ThreadSyncSimultaneouslyCallType2(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static <R, A, B> TS_ThreadSyncSimultaneouslyCallType2<R, A, B> of(Semaphore lock) {
        return new TS_ThreadSyncSimultaneouslyCallType2(lock);
    }

    public static <R, A, B> TS_ThreadSyncSimultaneouslyCallType2<R, A, B> of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public Optional<R> call(TGS_CallableType2<R, A, B> call, A inputA, B inputB) {
        return TGS_UnSafe.call(() -> {
            if (!lock.tryAcquire()) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call(inputA, inputB)),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.release());
        }, e -> Optional.empty());
    }

    public Optional<R> callUntil(TGS_CallableType2<R, A, B> call, Duration timeout, A inputA, B inputB) {
        return TGS_UnSafe.call(() -> {
            if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call(inputA, inputB)),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.release());
        }, e -> Optional.empty());
    }
}
