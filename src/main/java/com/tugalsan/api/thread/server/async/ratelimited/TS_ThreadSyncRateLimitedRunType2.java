package com.tugalsan.api.thread.server.async.ratelimited;

import com.tugalsan.api.runnable.client.TGS_RunnableType2;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TS_ThreadSyncRateLimitedRunType2<A, B> {

    private TS_ThreadSyncRateLimitedRunType2(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static <A, B> TS_ThreadSyncRateLimitedRunType2<A, B> of(Semaphore lock) {
        return new TS_ThreadSyncRateLimitedRunType2(lock);
    }

    public static <A, B> TS_ThreadSyncRateLimitedRunType2<A, B> of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public void run(TGS_RunnableType2<A, B> run, A inputA, B inputB) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryAcquire()) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA, inputB), ex -> TGS_UnSafe.thrw(ex), () -> lock.release());
        }, e -> TGS_StreamUtils.runNothing());
    }

    public void runUntil(TGS_RunnableType2<A, B> run, Duration timeout, A inputA, B inputB) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA, inputB), ex -> TGS_UnSafe.thrw(ex), () -> lock.release());
        }, e -> TGS_StreamUtils.runNothing());
    }
}
