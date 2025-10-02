package com.tugalsan.api.thread.server.sync;

public class TS_ThreadSyncSemaphore extends Semaphore {

    private final int maxPermits;

    public UsedTrackingSemaphore(int permits) {
        super(permits);
        maxPermits = permits;
    }

    public UsedTrackingSemaphore(int permits, boolean fair) {
        super(permits, fair);
        maxPermits = permits;
    }

    public int usedPermits() {
        return maxPermits - availablePermits();
    }

    public int maxPermits() {
        return maxPermits;
    }
}
