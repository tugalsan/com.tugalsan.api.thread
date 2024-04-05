package com.tugalsan.api.thread.server.sync.lockLimited;

import com.tugalsan.api.runnable.client.TGS_RunnableType5;
import com.tugalsan.api.stream.client.TGS_StreamUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TS_ThreadSyncLockLimitedRunType5<A, B, C, D, E> {

    private TS_ThreadSyncLockLimitedRunType5(ReentrantLock lock) {
        this.lock = lock;
    }
    final private ReentrantLock lock;

    public static <A, B, C, D, E> TS_ThreadSyncLockLimitedRunType5<A, B, C, D, E> of(ReentrantLock lock) {
        return new TS_ThreadSyncLockLimitedRunType5(lock);
    }

    public static <A, B, C, D, E> TS_ThreadSyncLockLimitedRunType5<A, B, C, D, E> of() {
        return of(new ReentrantLock());
    }

    public void run(TGS_RunnableType5<A, B, C, D, E> run, A inputA, B inputB, C inputC, D inputD, E inputE) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryLock()) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA, inputB, inputC, inputD, inputE), ex -> TGS_UnSafe.thrw(ex), () -> lock.unlock());
        }, e -> TGS_StreamUtils.runNothing());
    }

    public void runUntil(TGS_RunnableType5<A, B, C, D, E> run, Duration timeout, A inputA, B inputB, C inputC, D inputD, E inputE) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA, inputB, inputC, inputD, inputE), ex -> TGS_UnSafe.thrw(ex), () -> lock.unlock());
        }, e -> TGS_StreamUtils.runNothing());
    }
}
