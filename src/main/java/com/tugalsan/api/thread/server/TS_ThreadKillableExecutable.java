package com.tugalsan.api.thread.server;

import com.tugalsan.api.executable.client.*;
import java.util.concurrent.atomic.AtomicBoolean;

abstract public class TS_ThreadKillableExecutable implements TS_ThreadKillableInterface, TGS_Executable {

    public boolean isKillMe() {
        return killMe.get();
    }

    public void setKillMe(boolean killMe) {
        this.killMe.set(killMe);
    }

    private AtomicBoolean killMe = new AtomicBoolean(false);
}
