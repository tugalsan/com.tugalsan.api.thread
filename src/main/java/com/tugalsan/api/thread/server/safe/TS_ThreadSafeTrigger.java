package com.tugalsan.api.thread.server.safe;

import java.util.concurrent.atomic.AtomicBoolean;

public class TS_ThreadSafeTrigger {

    private TS_ThreadSafeTrigger() {

    }

    public static TS_ThreadSafeTrigger of() {
        return new TS_ThreadSafeTrigger();
    }

    private AtomicBoolean value = new AtomicBoolean(false);

    public void trigger() {
        value.set(true);
    }

    public boolean hasTriggered() {
        return value.get();
    }

    public boolean hasNotTriggered() {
        return !hasTriggered();
    }

}
