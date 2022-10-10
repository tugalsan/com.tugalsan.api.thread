package com.tugalsan.api.thread.server;

public class TS_ThreadRunAllTimeoutException extends RuntimeException {

    public TS_ThreadRunAllTimeoutException() {
        super("Timeout Error Occured While Running TS_ThreadRunAllX");
    }
}
