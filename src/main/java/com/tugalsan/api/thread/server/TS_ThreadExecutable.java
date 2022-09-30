package com.tugalsan.api.thread.server;

import com.tugalsan.api.executable.client.*;

abstract public class TS_ThreadExecutable implements TGS_Executable {

    public boolean isKill() {
        return kill;
    }

    public void setToKill() {
        this.kill = true;
    }

    private volatile boolean kill = false;
}
