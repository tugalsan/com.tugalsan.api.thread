package com.tugalsan.api.thread.server.core;

public class TS_ThreadRunParallelTimeoutException extends RuntimeException {

    public TS_ThreadRunParallelTimeoutException() {
        super("Timeout Error Occured While Running TS_ThreadRunAllX");
    }
}
