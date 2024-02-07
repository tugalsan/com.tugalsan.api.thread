package com.tugalsan.api.thread.server.sync.simultaneously;

import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TS_ThreadSyncSimultaneouslyRunType1<A> {

    private TS_ThreadSyncSimultaneouslyRunType1(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static <A> TS_ThreadSyncSimultaneouslyRunType1<A> of(Semaphore lock) {
        return new TS_ThreadSyncSimultaneouslyRunType1(lock);
    }

    public static <A> TS_ThreadSyncSimultaneouslyRunType1<A> of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public void run(TGS_RunnableType1<A> run, A inputA) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryAcquire()) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA), ex -> TGS_UnSafe.thrw(ex), () -> lock.release());
        }, e -> TGS_StreamUtils.runNothing());
    }

    public void runUntil(TGS_RunnableType1<A> run, Duration timeout, A inputA) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA), ex -> TGS_UnSafe.thrw(ex), () -> lock.release());
        }, e -> TGS_StreamUtils.runNothing());
    }
}
