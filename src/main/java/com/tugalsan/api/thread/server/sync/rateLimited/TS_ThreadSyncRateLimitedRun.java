package com.tugalsan.api.thread.server.sync.rateLimited;

import module com.tugalsan.api.function;
import java.time.*;
import java.util.concurrent.*;

public class TS_ThreadSyncRateLimitedRun {

    private TS_ThreadSyncRateLimitedRun(Semaphore lock) {
        this.lock = lock;
    }
    final private Semaphore lock;

    public static TS_ThreadSyncRateLimitedRun of(Semaphore lock) {
        return new TS_ThreadSyncRateLimitedRun(lock);
    }

    public static TS_ThreadSyncRateLimitedRun of(int simultaneouslyCount) {
        return of(new Semaphore(simultaneouslyCount));
    }

    public void run(TGS_FuncMTU run) {
        runUntil(run, null);
    }

    public void runUntil(TGS_FuncMTU run, Duration timeout) {
        try {
            if (timeout == null) {
                lock.acquire();
            } else {
                if (!lock.tryAcquire(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return;
                }
            }
            run.run();
        } catch (InterruptedException ex) {
            TGS_FuncUtils.throwIfInterruptedException(ex);
        } finally {
            lock.release();
        }
    }
}
