package com.tugalsan.api.thread.server.sync.lockLimited;

import com.tugalsan.api.runnable.client.TGS_RunnableType3;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TS_ThreadSyncLockLimitedRunType3<A, B, C> {

    private TS_ThreadSyncLockLimitedRunType3(ReentrantLock lock) {
        this.lock = lock;
    }
    final private ReentrantLock lock;

    public static <A, B, C> TS_ThreadSyncLockLimitedRunType3<A, B, C> of(ReentrantLock lock) {
        return new TS_ThreadSyncLockLimitedRunType3(lock);
    }

    public static <A, B, C> TS_ThreadSyncLockLimitedRunType3<A, B, C> of() {
        return of(new ReentrantLock());
    }

    public void run(TGS_RunnableType3<A, B, C> run, A inputA, B inputB, C inputC) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryLock()) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA, inputB, inputC), ex -> TGS_UnSafe.thrw(ex), () -> lock.unlock());
        }, e -> TGS_StreamUtils.runNothing());
    }

    public void runUntil(TGS_RunnableType3<A, B, C> run, Duration timeout, A inputA, B inputB, C inputC) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA, inputB, inputC), ex -> TGS_UnSafe.thrw(ex), () -> lock.unlock());
        }, e -> TGS_StreamUtils.runNothing());
    }
}
