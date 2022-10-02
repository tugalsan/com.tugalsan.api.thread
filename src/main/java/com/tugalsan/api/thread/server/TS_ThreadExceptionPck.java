package com.tugalsan.api.thread.server;

import java.util.List;

public class TS_ThreadExceptionPck extends RuntimeException {

    public TS_ThreadExceptionPck(Throwable... exceptions) {
        this(null, exceptions);
    }

    public TS_ThreadExceptionPck(TS_ThreadSafeLst<Throwable> exceptions) {
        this(exceptions, null);
    }

    public TS_ThreadExceptionPck(TS_ThreadSafeLst<Throwable> exceptionsList0, Throwable... exceptionsList1) {
        if (exceptionsList0 != null) {
            exceptionsList0.forEach(this::addSuppressed);
        }
        if (exceptionsList1 != null) {
            List.of(exceptionsList1).forEach(this::addSuppressed);
        }
    }
}
