package com.tugalsan.api.thread.server.sync.singly;

import com.tugalsan.api.runnable.client.TGS_RunnableType4;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TS_ThreadSyncSinglyRunType4<A, B, C, D> {

    private TS_ThreadSyncSinglyRunType4(ReentrantLock lock) {
        this.lock = lock;
    }
    final private ReentrantLock lock;

    public static <A, B, C, D> TS_ThreadSyncSinglyRunType4<A, B, C, D> of(ReentrantLock lock) {
        return new TS_ThreadSyncSinglyRunType4(lock);
    }

    public static <A, B, C, D> TS_ThreadSyncSinglyRunType4<A, B, C, D> of() {
        return of(new ReentrantLock());
    }

    public void run(TGS_RunnableType4<A, B, C, D> run, A inputA, B inputB, C inputC, D inputD) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryLock()) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA, inputB, inputC, inputD), ex -> TGS_UnSafe.thrw(ex), () -> lock.unlock());
        }, e -> TGS_StreamUtils.runNothing());
    }

    public void runUntil(TGS_RunnableType4<A, B, C, D> run, Duration timeout, A inputA, B inputB, C inputC, D inputD) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA, inputB, inputC, inputD), ex -> TGS_UnSafe.thrw(ex), () -> lock.unlock());
        }, e -> TGS_StreamUtils.runNothing());
    }
}
