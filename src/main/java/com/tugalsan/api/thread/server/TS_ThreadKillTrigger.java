package com.tugalsan.api.thread.server;

import java.util.concurrent.atomic.AtomicBoolean;

public class TS_ThreadKillTrigger {

    private TS_ThreadKillTrigger() {

    }

    public static TS_ThreadKillTrigger of() {
        return new TS_ThreadKillTrigger();
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
