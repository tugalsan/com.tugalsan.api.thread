package com.tugalsan.api.thread.server.sync.lockLimited;


import com.tugalsan.api.function.client.TGS_Func_In2;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TS_ThreadSyncLockLimitedRunType2<A, B> {

    private TS_ThreadSyncLockLimitedRunType2(ReentrantLock lock) {
        this.lock = lock;
    }
    final private ReentrantLock lock;

    public static <A, B> TS_ThreadSyncLockLimitedRunType2<A, B> of(ReentrantLock lock) {
        return new TS_ThreadSyncLockLimitedRunType2(lock);
    }

    public static <A, B> TS_ThreadSyncLockLimitedRunType2<A, B> of() {
        return of(new ReentrantLock());
    }

    public <A, B> void run(TGS_Func_In2<A, B> run, A inputA, B inputB) {
        runUntil(run, null, inputA, inputB);
    }

    public <A, B> void runUntil(TGS_Func_In2<A, B> run, Duration timeout, A inputA, B inputB) {
        try {
            if (timeout == null) {
                lock.lock();
            } else {
                if (!lock.tryLock(timeout.toSeconds(), TimeUnit.SECONDS)) {
                    return;
                }
            }
            run.run(inputA, inputB);
        } catch (InterruptedException ex) {
            TGS_UnSafe.throwIfInterruptedException(ex);
        } finally {
            lock.unlock();
        }
    }
}
