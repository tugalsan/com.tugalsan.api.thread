package com.tugalsan.api.thread.server.async.ratelimited;

import com.tugalsan.api.callable.client.TGS_CallableType4;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TS_ThreadSyncRateLimitedCallType4<R, A, B, C, D> {

    private TS_ThreadSyncRateLimitedCallType4(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static <R, A, B, C, D> TS_ThreadSyncRateLimitedCallType4<R, A, B, C, D> of(Semaphore lock) {
        return new TS_ThreadSyncRateLimitedCallType4(lock);
    }

    public static <R, A, B, C, D> TS_ThreadSyncRateLimitedCallType4<R, A, B, C, D> of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public Optional<R> call(TGS_CallableType4<R, A, B, C, D> call, A inputA, B inputB, C inputC, D inputD) {
        return TGS_UnSafe.call(() -> {
            if (!lock.tryAcquire()) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call(inputA, inputB, inputC, inputD)),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.release());
        }, e -> Optional.empty());
    }

    public Optional<R> callUntil(TGS_CallableType4<R, A, B, C, D> call, Duration timeout, A inputA, B inputB, C inputC, D inputD) {
        return TGS_UnSafe.call(() -> {
            if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return Optional.empty();
            }
            return TGS_UnSafe.call(() -> Optional.of(call.call(inputA, inputB, inputC, inputD)),
                    ex -> {
                        TGS_UnSafe.thrw(ex);
                        return Optional.empty();
                    }, () -> lock.release());
        }, e -> Optional.empty());
    }
}
