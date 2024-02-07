package com.tugalsan.api.thread.server.sync.lockLimited;

import com.tugalsan.api.runnable.client.TGS_Runnable;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TS_ThreadSyncLockLimitedRun {

    private TS_ThreadSyncLockLimitedRun(ReentrantLock lock) {
        this.lock = lock;
    }
    final private ReentrantLock lock;

    public static TS_ThreadSyncLockLimitedRun of(ReentrantLock lock) {
        return new TS_ThreadSyncLockLimitedRun(lock);
    }

    public static TS_ThreadSyncLockLimitedRun of() {
        return of(new ReentrantLock());
    }

    public void run(TGS_Runnable run) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryLock()) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(), ex -> TGS_UnSafe.thrw(ex), () -> lock.unlock());
        }, e -> TGS_StreamUtils.runNothing());
    }

    public void runUntil(TGS_Runnable run, Duration timeout) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(), ex -> TGS_UnSafe.thrw(ex), () -> lock.unlock());
        }, e -> TGS_StreamUtils.runNothing());
    }
}
