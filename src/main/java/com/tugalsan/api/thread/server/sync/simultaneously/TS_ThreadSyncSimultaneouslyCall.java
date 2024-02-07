package com.tugalsan.api.thread.server.sync.simultaneously;

import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TS_ThreadSyncSimultaneouslyCall<R> {

    private TS_ThreadSyncSimultaneouslyCall(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static <R> TS_ThreadSyncSimultaneouslyCall<R> of(Semaphore lock) {
        return new TS_ThreadSyncSimultaneouslyCall(lock);
    }

    public static <R> TS_ThreadSyncSimultaneouslyCall<R> of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public Optional<R> call(Callable<R> call) {
        return TGS_UnSafe.call(() -> {
            if (!lock.tryAcquire()) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call()),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.release());
        }, e -> Optional.empty());
    }

    public Optional<R> callUntil(Callable<R> call, Duration timeout) {
        return TGS_UnSafe.call(() -> {
            if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call()),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.release());
        }, e -> Optional.empty());
    }
}
