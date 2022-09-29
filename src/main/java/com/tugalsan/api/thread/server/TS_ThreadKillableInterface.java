package com.tugalsan.api.thread.server;

public interface TS_ThreadKillableInterface {

    abstract public boolean isKillMe();

    abstract public void setKillMe(boolean killMe);
}
