package com.tugalsan.api.thread.server.safe;

import com.tugalsan.api.runnable.client.*;

//TODO: WHY NOT USE INTERRUPT EXCEPTION AND STRUCTURED SCOPE?
abstract public class TS_ThreadSafeRunnable implements TGS_Runnable {

    public volatile String name = null;
    public volatile boolean killMe = false;

    public TS_ThreadSafeRunnable setName(String name) {
        this.name = name;
        return this;
    }

    public TS_ThreadSafeRunnable setKillMe(boolean killMe) {
        this.killMe = killMe;
        return this;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "->{" + "name=" + name + ", killMe=" + killMe + '}';
    }
}