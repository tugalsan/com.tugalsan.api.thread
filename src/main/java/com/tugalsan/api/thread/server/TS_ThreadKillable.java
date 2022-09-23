package com.tugalsan.api.thread.server;

import com.tugalsan.api.executable.client.*;
import java.util.concurrent.atomic.AtomicBoolean;

abstract public class TS_ThreadKillable implements TGS_Executable, TS_ThreadKillableInterface {

    public boolean isKillMe() {
        return killMe.get();
    }

    public void setKillMe(boolean killMe) {
        this.killMe.set(killMe);
    }

    private AtomicBoolean killMe = new AtomicBoolean(false);
}
