package com.tugalsan.api.thread.server.sync.lockLimited;

import module com.tugalsan.api.function;
import java.time.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class TS_ThreadSyncLockLimitedRun {

    private TS_ThreadSyncLockLimitedRun(ReentrantLock lock) {
        this.lock = lock;
    }
    final private ReentrantLock lock;

    public static TS_ThreadSyncLockLimitedRun of(ReentrantLock lock) {
        return new TS_ThreadSyncLockLimitedRun(lock);
    }

    public static TS_ThreadSyncLockLimitedRun of() {
        return of(new ReentrantLock());
    }

    public void run(TGS_FuncMTU run) {
        runUntil(run, null);
    }

    public void runUntil(TGS_FuncMTU run, Duration timeout) {
        try {
            if (timeout == null) {
                lock.lock();
            } else {
                if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return;
                }
            }
            run.run();
        } catch (InterruptedException ex) {
            TGS_FuncUtils.throwIfInterruptedException(ex);
        } finally {
            lock.unlock();
        }
    }
}
