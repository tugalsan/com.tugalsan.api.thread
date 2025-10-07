package com.tugalsan.api.thread.server.sync;

import java.util.concurrent.*;

public class TS_ThreadSyncSemaphore extends Semaphore {

    private final int maxPermits;

    public TS_ThreadSyncSemaphore(int permits) {
        super(permits);
        maxPermits = permits;
    }

    public TS_ThreadSyncSemaphore(int permits, boolean fair) {
        super(permits, fair);
        maxPermits = permits;
    }

    public int usedPermits() {
        return maxPermits - availablePermits();
    }

    public int maxPermits() {
        return maxPermits;
    }

    public boolean halfFull() {
        return maxPermits < usedPermits() * 2;
    }
}
