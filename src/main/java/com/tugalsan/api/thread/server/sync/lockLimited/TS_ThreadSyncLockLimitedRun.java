package com.tugalsan.api.thread.server.sync.lockLimited;

import com.tugalsan.api.runnable.client.TGS_Runnable;
import com.tugalsan.api.union.server.TS_UnionUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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

    public void run(TGS_Runnable run) {
        runUntil(run, null);
    }

    public void runUntil(TGS_Runnable run, Duration timeout) {
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
            TS_UnionUtils.throwAsRuntimeExceptionIfInterruptedException(ex);
        } finally {
            lock.unlock();
        }
    }
}
