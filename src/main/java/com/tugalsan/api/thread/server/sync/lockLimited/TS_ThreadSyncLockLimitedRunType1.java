package com.tugalsan.api.thread.server.sync.lockLimited;

import module com.tugalsan.api.function;
import java.time.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

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

    public <A> void run(TGS_FuncMTU_In1<A> run, A inputA) {
        runUntil(run, null, inputA);
    }

    public <A> void runUntil(TGS_FuncMTU_In1<A> run, Duration timeout, A inputA) {
        try {
            if (timeout == null) {
                lock.lock();
            } else {
                if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return;
                }
            }
            run.run(inputA);
        } catch (InterruptedException ex) {
            TGS_FuncUtils.throwIfInterruptedException(ex);
        } finally {
            lock.unlock();
        }
    }
}
