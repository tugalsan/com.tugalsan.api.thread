package com.tugalsan.api.thread.server.sync.lockLimited;

import module com.tugalsan.api.function;
import java.time.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

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

    public <A, B, C> void run(TGS_FuncMTU_In3<A, B, C> run, A inputA, B inputB, C inputC) {
        runUntil(run, null, inputA, inputB, inputC);
    }

    public <A, B, C> void runUntil(TGS_FuncMTU_In3<A, B, C> run, Duration timeout, A inputA, B inputB, C inputC) {
        try {
            if (timeout == null) {
                lock.lock();
            } else {
                if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return;
                }
            }
            run.run(inputA, inputB, inputC);
        } catch (InterruptedException ex) {
            TGS_FuncUtils.throwIfInterruptedException(ex);
        } finally {
            lock.unlock();
        }
    }
}
