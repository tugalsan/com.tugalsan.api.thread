package com.tugalsan.api.thread.server.sync.simultaneously;

import com.tugalsan.api.runnable.client.TGS_RunnableType4;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TS_ThreadSyncSimultaneouslyRunType4<A, B, C, D> {

    private TS_ThreadSyncSimultaneouslyRunType4(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static <A, B, C, D> TS_ThreadSyncSimultaneouslyRunType4<A, B, C, D> of(Semaphore lock) {
        return new TS_ThreadSyncSimultaneouslyRunType4(lock);
    }

    public static <A, B, C, D> TS_ThreadSyncSimultaneouslyRunType4<A, B, C, D> of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public void run(TGS_RunnableType4<A, B, C, D> run, A inputA, B inputB, C inputC, D inputD) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryAcquire()) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA, inputB, inputC, inputD), ex -> TGS_UnSafe.thrw(ex), () -> lock.release());
        }, e -> TGS_StreamUtils.runNothing());
    }

    public void runUntil(TGS_RunnableType4<A, B, C, D> run, Duration timeout, A inputA, B inputB, C inputC, D inputD) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA, inputB, inputC, inputD), ex -> TGS_UnSafe.thrw(ex), () -> lock.release());
        }, e -> TGS_StreamUtils.runNothing());
    }
}
