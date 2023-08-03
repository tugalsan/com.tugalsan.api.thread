package com.tugalsan.api.thread.server.safe;

import com.tugalsan.api.runnable.client.TGS_Runnable;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.util.concurrent.locks.ReentrantLock;

@Deprecated //OLD STYLE, WILL IT REALLY MATTER?
public class TS_ThreadSafeRun {

    final public ReentrantLock lock = new ReentrantLock();

    public void run(TGS_Runnable exe) {
        TGS_UnSafe.run(() -> {
            lock.lock();
            exe.run();
        }, ex -> {
            lock.unlock();
            TGS_UnSafe.thrw(ex);
        }, () -> lock.unlock());
    }
}
