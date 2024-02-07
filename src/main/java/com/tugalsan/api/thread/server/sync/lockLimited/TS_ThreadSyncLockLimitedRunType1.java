package com.tugalsan.api.thread.server.sync.lockLimited;

import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TS_ThreadSyncLockLimitedRunType1<A> {

    private TS_ThreadSyncLockLimitedRunType1(ReentrantLock lock) {
        this.lock = lock;
    }
    final private ReentrantLock lock;

    public static <A> TS_ThreadSyncLockLimitedRunType1<A> of(ReentrantLock lock) {
        return new TS_ThreadSyncLockLimitedRunType1(lock);
    }

    public static <A> TS_ThreadSyncLockLimitedRunType1<A> of() {
        return of(new ReentrantLock());
    }

    public void run(TGS_RunnableType1<A> run, A inputA) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryLock()) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA), ex -> TGS_UnSafe.thrw(ex), () -> lock.unlock());
        }, e -> TGS_StreamUtils.runNothing());
    }

    public void runUntil(TGS_RunnableType1<A> run, Duration timeout, A inputA) {
        TGS_UnSafe.run(() -> {
            if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                return;
            }
            TGS_UnSafe.run(() -> run.run(inputA), ex -> TGS_UnSafe.thrw(ex), () -> lock.unlock());
        }, e -> TGS_StreamUtils.runNothing());
    }
}
