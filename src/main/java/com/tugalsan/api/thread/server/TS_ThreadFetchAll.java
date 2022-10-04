package com.tugalsan.api.thread.server;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import jdk.incubator.concurrent.StructuredTaskScope;

public class TS_ThreadFetchAll<T> {

    private static class FetchAllScope<T> extends StructuredTaskScope<T> {

        @Override
        protected void handleComplete(Future<T> future) {
            switch (future.state()) {
                case RUNNING ->
                    throw new IllegalStateException("State should not be running!");
                case SUCCESS ->
                    this.results.add(future.resultNow());
                case FAILED ->
                    this.exceptions.add(future.exceptionNow());
                case CANCELLED -> {
                }
            }
        }
        public final TS_ThreadSafeLst<T> results = new TS_ThreadSafeLst();
        public final TS_ThreadSafeLst<Throwable> exceptions = new TS_ThreadSafeLst();
        public volatile boolean timeout = false;

        @Override
        public FetchAllScope<T> joinUntil(Instant deadline) throws InterruptedException {
            try {
                super.joinUntil(deadline);
            } catch (TimeoutException e) {
                super.shutdown();
                timeout = true;
            }
            return this;
        }
    }

    //until: Instant.now().plusMillis(10)
    private TS_ThreadFetchAll(Instant until, List<Callable<T>> callables) {
        try ( var scope = new FetchAllScope<T>()) {
            results = scope.results;
            exceptions = scope.exceptions;
            callables.forEach(c -> scope.fork(c));
            if (until == null) {
                scope.join();
            } else {
                scope.joinUntil(until);
            }
            timeout = scope.timeout;
        } catch (InterruptedException e) {
            if (results == null) {
                results = new TS_ThreadSafeLst();
            }
            if (exceptions == null) {
                exceptions = new TS_ThreadSafeLst();
            }
            exceptions.add(e);
        }
    }

    public boolean timeout() {
        return timeout;
    }
    private boolean timeout;

    public TS_ThreadSafeLst<T> resultLst() {
        return results;
    }
    private TS_ThreadSafeLst<T> results;

    public TS_ThreadSafeLst<Throwable> exceptionLst() {
        return exceptions;
    }
    private TS_ThreadSafeLst<Throwable> exceptions;

    public TS_ThreadExceptionPck exceptionPack() {
        return new TS_ThreadExceptionPck(exceptions);
    }

    public static <T> TS_ThreadFetchAll<T> of(Instant until, Callable<T>... callables) {
        return of(until, List.of(callables));
    }

    public static <T> TS_ThreadFetchAll<T> of(Instant until, List<Callable<T>> callables) {
        return new TS_ThreadFetchAll(until, callables);
    }
}
