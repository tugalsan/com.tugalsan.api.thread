package com.tugalsan.api.thread.server.sync.lockLimited;

import com.tugalsan.api.runnable.client.TGS_RunnableType2;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TS_ThreadSyncLockLimitedRunType2<A, B> {

    private TS_ThreadSyncLockLimitedRunType2(ReentrantLock lock) {
        this.lock = lock;
    }
    final private ReentrantLock lock;

    public static <A, B> TS_ThreadSyncLockLimitedRunType2<A, B> of(ReentrantLock lock) {
        return new TS_ThreadSyncLockLimitedRunType2(lock);
    }

    public static <A, B> TS_ThreadSyncLockLimitedRunType2<A, B> of() {
        return of(new ReentrantLock());
    }

    public void run(TGS_RunnableType2<A, B> run, A inputA, B inputB) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryLock()) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA, inputB), ex -> TGS_UnSafe.thrw(ex), () -> lock.unlock());
        }, e -> TGS_StreamUtils.runNothing());
    }

    public void runUntil(TGS_RunnableType2<A, B> run, Duration timeout, A inputA, B inputB) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA, inputB), ex -> TGS_UnSafe.thrw(ex), () -> lock.unlock());
        }, e -> TGS_StreamUtils.runNothing());
    }
}
