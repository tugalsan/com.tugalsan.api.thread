package com.tugalsan.api.thread.server.sync;

import com.tugalsan.api.runnable.client.TGS_Runnable;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.util.concurrent.locks.ReentrantLock;

@Deprecated //OLD STYLE, WILL IT REALLY MATTER?
public class TS_ThreadSyncRun {

    public static ReentrantLock createNewRunGroup() {
        return new ReentrantLock();
    }

    public TS_ThreadSyncRun(ReentrantLock runGroup, TGS_Runnable run) {
        this.runGroup = runGroup;
        this.run = run;
    }
    final public ReentrantLock runGroup;
    final public TGS_Runnable run;

    public void lockOthers_runThisOneOnly_unLockOthers() {
        TGS_UnSafe.run(() -> {
            runGroup.lock();
            run.run();
        }, ex -> {
            runGroup.unlock();
            TGS_UnSafe.thrw(ex);
        }, () -> runGroup.unlock());
    }
}
