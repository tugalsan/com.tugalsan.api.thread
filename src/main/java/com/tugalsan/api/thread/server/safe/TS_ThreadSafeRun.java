package com.tugalsan.api.thread.server.safe;

import com.tugalsan.api.runnable.client.TGS_Runnable;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.util.concurrent.locks.ReentrantLock;

public class TS_ThreadSafeRun {

    final private ReentrantLock lock = new ReentrantLock();

    public void run(TGS_Runnable exe) {
        TGS_UnSafe.run(() -> {
            lock.lock();
            exe.run();
        }, ex -> TGS_UnSafe.thrw(ex), () -> lock.unlock());
    }
}
