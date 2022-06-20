package com.tugalsan.api.thread.server;

import com.tugalsan.api.executable.client.*;

abstract public class TS_ThreadKillable implements TGS_Executable, TS_ThreadKillableInterface {

    public boolean isKillMe() {
        return killMe;
    }

    public void setKillMe(boolean killMe) {
        this.killMe = killMe;
    }

    private boolean killMe = false;
}
