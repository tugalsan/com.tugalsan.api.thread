package com.tugalsan.api.thread.server.sync;

import com.tugalsan.api.runnable.client.TGS_Runnable;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.util.concurrent.locks.ReentrantLock;

@Deprecated //OLD STYLE, WILL IT REALLY MATTER?
public class TS_ThreadSyncRun {

    public static ReentrantLock createNewGroup() {
        return new ReentrantLock();
    }

    public TS_ThreadSyncRun(ReentrantLock group, TGS_Runnable run) {
        this.lock = group;
        this.run = run;
    }
    final public ReentrantLock lock;
    final public TGS_Runnable run;

    public void lockOtherAndRunSafely() {
        TGS_UnSafe.run(() -> {
            lock.lock();
            run.run();
        }, ex -> {
            lock.unlock();
            TGS_UnSafe.thrw(ex);
        }, () -> lock.unlock());
    }
}
