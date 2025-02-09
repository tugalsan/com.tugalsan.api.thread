package com.tugalsan.api.thread.server.sync;

@Deprecated //JUST USE VIRTUAL THREAD
public class TS_ThreadSyncInfo {

    public static String name() {
        return Thread.currentThread().getThreadGroup().getName();
    }

    public static int count() {
        return Thread.activeCount();
    }
}
