package com.tugalsan.api.thread.server;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import jdk.incubator.concurrent.StructuredTaskScope;

//IMPLEMENTATION OF https://www.youtube.com/watch?v=_fRN7tpLyPk
public class TS_ThreadCompileAll<T> {

    private static class CompileAllScope<T> extends StructuredTaskScope<T> {

        @Override
        protected void handleComplete(Future<T> future) {
            switch (future.state()) {
                case RUNNING ->
                    throw new IllegalStateException("State should not be running!");
                case SUCCESS -> {
                    var result = future.resultNow();
                    if (result != null) {
                        this.results.add(result);
                    }
                }
                case FAILED ->
                    this.exceptions.add(future.exceptionNow());
                case CANCELLED -> {
                }
            }
        }
        public volatile boolean timeout = false;
        public final TS_ThreadSafeLst<T> results = new TS_ThreadSafeLst();
        public final TS_ThreadSafeLst<Throwable> exceptions = new TS_ThreadSafeLst();

        @Override
        public CompileAllScope<T> joinUntil(Instant deadline) throws InterruptedException {
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
    private TS_ThreadCompileAll(Instant until, List<Callable<T>> callables) {
        try ( var scope = new CompileAllScope<T>()) {
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

    public boolean hasError() {
        return !exceptions.isEmpty();
    }

    public static <T> TS_ThreadCompileAll<T> of(Instant until, Callable<T>... callables) {
        return of(until, List.of(callables));
    }

    public static <T> TS_ThreadCompileAll<T> of(Instant until, List<Callable<T>> callables) {
        return new TS_ThreadCompileAll(until, callables);
    }
}
