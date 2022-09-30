package com.tugalsan.api.thread.server;

import com.tugalsan.api.executable.client.*;

abstract public class TS_ThreadKillableExecutable implements TS_ThreadKillableInterface, TGS_Executable {

    @Override
    public boolean isKill() {
        return kill;
    }

    @Override
    public void setToKill() {
        this.kill = true;
    }

    private volatile boolean kill = false;
}
