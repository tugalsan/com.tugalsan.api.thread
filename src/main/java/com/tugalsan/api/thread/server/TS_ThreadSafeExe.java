package com.tugalsan.api.thread.server;

import com.tugalsan.api.executable.client.TGS_Executable;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.util.concurrent.locks.ReentrantLock;

public class TS_ThreadSafeExe {

    final private ReentrantLock lock = new ReentrantLock();

    public void execute(TGS_Executable exe) {
        TGS_UnSafe.execute(() -> {
            lock.lock();
            exe.execute();
        }, ex -> TGS_UnSafe.catchMeIfUCan(ex), () -> lock.unlock());
    }
}
