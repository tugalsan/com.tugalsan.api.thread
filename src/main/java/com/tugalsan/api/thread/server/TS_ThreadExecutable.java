package com.tugalsan.api.thread.server;

import com.tugalsan.api.executable.client.*;

abstract public class TS_ThreadExecutable implements TGS_Executable {

    public volatile String name = null;
    public volatile boolean killMe = false;

    public TS_ThreadExecutable setName(String name) {
        this.name = name;
        return this;
    }

    public TS_ThreadExecutable setKillMe(boolean killMe) {
        this.killMe = killMe;
        return this;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "->{" + "name=" + name + ", killMe=" + killMe + '}';
    }
}