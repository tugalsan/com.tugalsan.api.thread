package com.tugalsan.api.thread.server.async.rateLimited;

import com.tugalsan.api.runnable.client.TGS_Runnable;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TS_ThreadSyncRateLimitedRun {

    private TS_ThreadSyncRateLimitedRun(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static TS_ThreadSyncRateLimitedRun of(Semaphore lock) {
        return new TS_ThreadSyncRateLimitedRun(lock);
    }

    public static TS_ThreadSyncRateLimitedRun of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public void run(TGS_Runnable run) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryAcquire()) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(), ex -> TGS_UnSafe.thrw(ex), () -> lock.release());
        }, e -> TGS_StreamUtils.runNothing());
    }

    public void runUntil(TGS_Runnable run, Duration timeout) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(), ex -> TGS_UnSafe.thrw(ex), () -> lock.release());
        }, e -> TGS_StreamUtils.runNothing());
    }
}
