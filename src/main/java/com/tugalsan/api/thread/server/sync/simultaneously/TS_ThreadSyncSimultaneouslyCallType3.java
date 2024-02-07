package com.tugalsan.api.thread.server.sync.simultaneously;

import com.tugalsan.api.callable.client.TGS_CallableType3;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TS_ThreadSyncSimultaneouslyCallType3<R, A, B, C> {

    private TS_ThreadSyncSimultaneouslyCallType3(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static <R, A, B, C> TS_ThreadSyncSimultaneouslyCallType3<R, A, B, C> of(Semaphore lock) {
        return new TS_ThreadSyncSimultaneouslyCallType3(lock);
    }

    public static <R, A, B, C> TS_ThreadSyncSimultaneouslyCallType3<R, A, B, C> of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public Optional<R> call(TGS_CallableType3<R, A, B, C> call, A inputA, B inputB, C inputC) {
        return TGS_UnSafe.call(() -> {
            if (!lock.tryAcquire()) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call(inputA, inputB, inputC)),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.release());
        }, e -> Optional.empty());
    }

    public Optional<R> callUntil(TGS_CallableType3<R, A, B, C> call, Duration timeout, A inputA, B inputB, C inputC) {
        return TGS_UnSafe.call(() -> {
            if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call(inputA, inputB, inputC)),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.release());
        }, e -> Optional.empty());
    }
}
