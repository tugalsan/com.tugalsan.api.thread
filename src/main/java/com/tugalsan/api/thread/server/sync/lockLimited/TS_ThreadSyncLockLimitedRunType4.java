package com.tugalsan.api.thread.server.sync.lockLimited;

import module com.tugalsan.api.function;
import java.time.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class TS_ThreadSyncLockLimitedRunType4<A, B, C, D> {

    private TS_ThreadSyncLockLimitedRunType4(ReentrantLock lock) {
        this.lock = lock;
    }
    final private ReentrantLock lock;

    public static <A, B, C, D> TS_ThreadSyncLockLimitedRunType4<A, B, C, D> of(ReentrantLock lock) {
        return new TS_ThreadSyncLockLimitedRunType4(lock);
    }

    public static <A, B, C, D> TS_ThreadSyncLockLimitedRunType4<A, B, C, D> of() {
        return of(new ReentrantLock());
    }

    public <A, B, C, D> void run(TGS_FuncMTU_In4<A, B, C, D> run, A inputA, B inputB, C inputC, D inputD) {
        runUntil(run, null, inputA, inputB, inputC, inputD);
    }

    public <A, B, C, D> void runUntil(TGS_FuncMTU_In4<A, B, C, D> run, Duration timeout, A inputA, B inputB, C inputC, D inputD) {
        try {
            if (timeout == null) {
                lock.lock();
            } else {
                if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return;
                }
            }
            run.run(inputA, inputB, inputC, inputD);
        } catch (InterruptedException ex) {
            TGS_FuncUtils.throwIfInterruptedException(ex);
        } finally {
            lock.unlock();
        }
    }
}
